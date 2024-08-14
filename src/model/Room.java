package model;



import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Room class for the maze that contains doors.
 *
 * @author Eric John
 * @version 08/13/2024
 */
public class Room implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * A map for the doors with the key being direction and the value being the door.
     */
    private final Map<Direction, Door> myDoors;

    /**
     * A map for adjacent rooms with the key being direction and the value being the door.
     */
    private final Map<Direction, Room> myAdjacentRooms;

    /**
     * The trivia for the game.
     */
    private final Question myTrivia;

    /**
     * Boolean that checks if the door is answered
     */
    private final boolean isAnswered;

    /**
     * Creates a room object with the trivia question.
     * @param theTrivia The question that goes with the room.
     */
    public Room(final Question theTrivia) {
        this.myTrivia = theTrivia;
        myDoors = new EnumMap<>(Direction.class);
        myAdjacentRooms = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            myDoors.put(dir, new Door(true));
        }
        isAnswered = false;
    }

    /**
     * Setting up the adjacent rooms in the maze
     * @param theDirection - The direction.
     * @param theRoom - The room
     */
    public void setAdjacentRoom(final Direction theDirection, final Room theRoom) {
        myAdjacentRooms.put(theDirection, theRoom);
    }

    /**
     * Gets the door in a given direction.
     * @param theDirection - The direction to get the door.
     * @return - The door in the given direction
     */
    public Door getDoor(final Direction theDirection) {
        return myDoors.get(theDirection);
    }

    public Question getTrivia() {
        return myTrivia;
    }

    /**
     * Checks if all the doors are closed.
     * @return - True if it is closed. False otherwise.
     */
    public boolean allClosed() {
        return myDoors.values().stream().allMatch(Door::isClosed);
    }


    /**
     * Returns the isAnswered field
     * @return The boolean if the question has been answered.
     */
    public boolean isAnswered() {
        return isAnswered;
    }

    /**
     * Checks if a door is open.
     * @param theDirection The direction of the door.
     * @return - True if the door is open. False otherwise.
     */
    public boolean isDoorOpen(final Direction theDirection) {
        Door door = myDoors.get(theDirection);
        return door != null && !door.isClosed();
    }

    /**
     * Checks if the question has been answered correctly.
     * @param theDirection - The direction of the door.
     * @return True if it has been answered incorrectly, false otherwise.
     */
    public boolean hasBeenAnsweredIncorrectly(final Direction theDirection) {
        return myDoors.get(theDirection).hasBeenAnsweredIncorrectly();
    }

    /**
     * Sets the door in the given direction open.
     * @param theDirection - The direction of the door.
     * @param isOpen - Checks if the door is open or closed.
     */
    public void setDoorOpen(final Direction theDirection, final boolean isOpen) {
        Door door = myDoors.get(theDirection);
        if (isOpen) {
            door.open();
        } else {
            door.close();
        }
    }

}