/**
 * TCSS 360 - Trivia Maze
 * Door.java
 */
package model;

import java.io.Serializable;

/**
 * The Door class represents a door in a room of the trivia maze.
 * A door can be either open or closed.
 * @author hamda jama
 * @version 7/21/2024
 */
public class Door implements Serializable {
    private static final long serialVersionUID = 4L;
    /**
     * Indicates whether the door is closed.
     */
    private boolean myClosed;
    /**
     * Constructs a new Door, initially closed.
     */
    public Door() {
        this.myClosed = true;
    }
    /**
     * Checks if the door is closed.
     *
     * @return True if the door is closed, false otherwise.
     */
    public boolean isClosed() {
        return myClosed;
    }
    /**
     * Opens the door.
     */
    public void open() {
        myClosed = false;
    }
    /**
     * Closes the door.
     */
    public void close() {
        myClosed = true;
    }
}
