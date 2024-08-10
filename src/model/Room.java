package model;

import model.Door;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.*;

/**
 * The Room class represents a room in the trivia maze.
 * Each room contains a set of doors and a trivia question.
 * @author Hamda Jama
 * @version 7/21/2024
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 2L;
    /**
     * A map of doors in the room, keyed by direction (e.g., "North", "East", "South", "West").
     */
    private Map<Direction, Door> myDoors;
    private Map<Direction,Question> myQuestions;
    private Map<Direction,Room> myAdjacentRooms;

    /**
     * The trivia question associated with the room.
     */
    private Question myTrivia;


    /**
     * A property change support object that helps changes the state in other classes.
     */
    private final PropertyChangeSupport myPcs;

    /**
     * A boolean that checks if the answer for the room has been answered or not.
     */
    private boolean isAnswered;

    /**
     * Constructs a new Room with the given trivia question.
     * Initializes the doors in the room.
     *
     * @param theDoorQues The trivia question for each door.
     */
    public Room(Map<Direction, Question> theDoorQues) {
        myDoors = new EnumMap<>(Direction.class);
        myQuestions = new EnumMap<>(theDoorQues);
        myAdjacentRooms = new EnumMap<>(Direction.class);
        myPcs = new PropertyChangeSupport(this);
        for (Direction direction : Direction.values()) {
            myDoors.put(direction, new Door(direction));
        }
        //Since starting at the first door, make sure the east door and south door are open.
        // myRoom.get("East").open();
        // myRoom.get("South").open();


    }
    public void addPropertyChangeListener(PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(theListener);
    }

    // public void removePropertyChangeListener(PropertyChangeListener theListener) {
    //   myPcs.removePropertyChangeListener(theListener);
    //}
    /**
     * Retrieves the door in the specified direction.
     *

     * @return The door in the specified direction.
     */

    public Door getDoor(Direction theDirection) {
        return myDoors.get(theDirection);
    }
    /**
     * Retrieves the question associated with the door in the specified direction.
     *
     * @param thedirection The direction of the door.
     * @return The question associated with the door in that direction.
     */
    public Question getQuesDoor (Direction thedirection) {
        return myQuestions.get(thedirection);
    }
    /**
     * Sets the adjacent room in a specific direction.
     *
     * @param theDirection The direction of the adjacent room.
     * @param theRoom  The adjacent Room object.
     */
    public void setAdjacentRooms(Direction theDirection, Room theRoom) {
        myAdjacentRooms.put(theDirection,theRoom);
    }

    /**
     * get all doors for the room.
     * @return all the doors in the room.
     */
    // public Map<String, Door> getDoors() {
    //   return myRoom;
    // }
    /**
     * Checks if the player's answer matches the correct answer to the trivia question.
     *

     * @param theAnswer The answer given by the player.
     */

    // public void wrongAnswer(String theAnswer) {
    //   if (!answerQuestion(theAnswer)) {
    //     closeDoor();
    //    } else {
    //      Door door =  new Door();
    //    door.open();
    //    }
    // }
    /**
     * permanetly closes the door in the current Room and that door in a adjacent room.
     * @param theDirection the direction of the door to close.
     */
    public void closeDoor(Direction theDirection) {
        Door door = myDoors.get(theDirection);
        if (door != null && !door.isClosed() ) {
            door.close();
            myPcs.firePropertyChange(theDirection.name(),true,false);
            Room adjacent = myAdjacentRooms.get(theDirection);
            if (adjacent != null) {
                Direction opposite =getOpposite(theDirection);
                adjacent.getDoor(opposite).close();
                adjacent.myPcs.firePropertyChange(opposite.name(),true, false);
            }
        }

    }
    /**
     * Determines the opposite direction of a door.
     * @param theDirection the initial direction.
     * @return the opposite direction.
     */
    private Direction getOpposite(Direction theDirection) {
        switch (theDirection) {
            case NORTH:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.NORTH;
            case EAST:
                return Direction.WEST;
            case WEST:
                return Direction.EAST;
            default:
                throw new IllegalStateException("invalid Direction");
        }
    }
    /**

     * @return True if all doors are closed, false otherwise.
     */
    public boolean allClosed() {
        for (Door D : myDoors.values() ) {
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
