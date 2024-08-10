
package model;
import model.DoorState;

import java.io.Serializable;
/**
 * The Door class represents a door in a room of the trivia maze.
 * A door can be either open or closed. Once a door is closed due to
 * an incorrect answer, it remains permanently closed. The door is also
 * associated with a direction.
 * @author hamda jama
 * @version 7/21/2024
 */
public class Door implements Serializable {
    private static final long serialVersionUID = 4L;
    /**
     * The direction of the door (e.g., NORTH, SOUTH, EAST, WEST).
     */
    private final Direction myDirection;
    /**
     * The current state of the door (e.g., OPEN, CLOSED).
     */
    private DoorState myState;

    /**
     * Constructs a new Door, initially closed.
     */
    public Door(Direction theDirection) {
        this.myDirection = theDirection;
        this.myState = DoorState.OPEN; // Doors are initally open.

    }
    public Direction getDirection() {
        return myDirection;
    }

    /**
     * Checks if the door is closed.
     *
     * @return True if the door is closed, false otherwise.
     */
    public boolean isClosed() {
        return myState == DoorState.CLOSED;
    }
    /**
     * Opens the door.
     */
    public void open() {
        myState = DoorState.OPEN;
    }
    /**
     * Closes the door permanetly .
     */
    public void close() {
        myState = DoorState.CLOSED;
    }
}
