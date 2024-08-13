package model;



import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Room implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private final Map<Direction, Door> myDoors;
    private final Map<Direction, Room> myAdjacentRooms;
    private final Question myTrivia;
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
    public boolean isDoorOpen(Direction theDirection) {
        Door door = myDoors.get(theDirection);
        return door != null && !door.isClosed();
    }

    public boolean hasBeenAnsweredIncorrectly(Direction direction) {
        return myDoors.get(direction).hasBeenAnsweredIncorrectly();
    }

    public void setDoorOpen(Direction dir, boolean isOpen) {
        Door door = myDoors.get(dir);
        if (isOpen) {
            door.open();
        } else {
            door.close();
        }
    }

    public void debugPrintDoors() {
        System.out.println("Room Door States:");
        for (Direction dir : Direction.values()) {
            System.out.println(dir + ": open=" + isDoorOpen(dir) + ", incorrectlyAnswered=" + hasBeenAnsweredIncorrectly(dir));
        }
    }




}