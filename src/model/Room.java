package model;
/**
 * TCSS 360 - Trivia Maze
 * Room.java
 */

import model.Door;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/**
 * The Room class represents a room in the trivia maze.
 * Each room contains a set of doors and a trivia question.
 * @author Hamda Jama
 * @version 7/21/2024
 */
public class Room {
    /**
     * A map of doors in the room, keyed by direction (e.g., "North", "East", "South", "West").
     */
    private Map<String, Door> myRoom;
    /**
     * The trivia question associated with the room.
     */
    private Question myTrivia;

    /**
     * A property change support object that helps changes the state in other classes.
     */
    private final PropertyChangeSupport myPcs = new PropertyChangeSupport(this);

    /**
     * A boolean that checks if the answer for the room has been answered or not.
     */
    private boolean isAnswered;

    /**
     * Constructs a new Room with the given trivia question.
     * Initializes the doors in the room.
     *
     * @param theTrivia The trivia question for the room.
     */
    public Room(Question theTrivia) {
        this.myTrivia = theTrivia;
        myRoom = new HashMap<>();
        myRoom.put("North", new Door());
        myRoom.put("East", new Door());
        myRoom.put("South", new Door());
        myRoom.put("West", new Door());

        //Since starting at the first door, make sure the east door and south door are open.
        myRoom.get("East").open();
        myRoom.get("South").open();

        isAnswered = false;

    }
    /**
     * Retrieves the door in the specified direction.
     *
     * @param theDirection The direction of the door (e.g., "North").
     * @return The door in the specified direction.
     */

    public Door getDoor(String theDirection) {
        return myRoom.get(theDirection);
    }
    /**
     * get all doors for the room.
     * @return all the doors in the room.
     */
    public Map<String, Door> getDoors() {
        return myRoom;
    }
    /**
     * Retrieves the trivia question associated with the room.
     *
     * @return The trivia question.
     */
    public Question getTrivia () {
        return myTrivia;
    }
    /**
     * Checks if the player's answer matches the correct answer to the trivia question.
     *
     * @param theAnswer The answer given by the player.
     * @return True if the answer matches, false otherwise.
     */
    public boolean answerQuestion(String theAnswer) {
        return myTrivia.isMatch(theAnswer);
    }
    /**
     * Handles the player's wrong answer. If the answer is incorrect, closes a random door.
     * Otherwise, allows the player to choose a door and move to the next room.
     *
     * @param theAnswer The answer given by the player.
     */

    public void wrongAnswer(String theAnswer) {
        if (!answerQuestion(theAnswer)) {
            closeDoor();
        }
        else {
            //we can choose a door and go into the room.
            // Logic to move to another room will be handled in TriviaMaze class
            Door door = new Door();
            door.open();
        }
    }
    /**
     * Closes a random open door in the room.
     */
    public void closeDoor() {
        Random rand  = new Random();
        Object[] dir = myRoom.keySet().toArray();
        boolean closed = false;
        while (!closed) {
            String randDir = (String) dir[rand.nextInt(dir.length)];
            Door randDoor = myRoom.get(randDir);
            if (!randDoor.isClosed()) {
                randDoor.close();
                myPcs.firePropertyChange(randDir, null, randDoor.isClosed());
                closed =true;
            }
        }
    }
    /**
     * Checks if all doors in the room are closed.
     *
     * @return True if all doors are closed, false otherwise.
     */
    public boolean allClosed() {
        for (Door D : myRoom.values() ) {
            if (!D.isClosed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the current room's question has been answered.
     * @return True if the question has been answered. False if not.
     */
    public boolean isAnswered() {
        return isAnswered;
    }

    /**
     * Changes the state of the boolean depending on if the question has been answered.
     * @param answered - The state to change the isAnswered boolean to.
     */
    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

}
