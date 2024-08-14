/**
 * TCSS 360 - Trivia Maze
 * Door.java
 */
package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * The Door class represents a door in a room of the trivia maze.
 * A door can be either open or closed.
 * @author hamda jama
 * @version 7/21/2024
 */
public class Door implements Serializable {
    /**
     * Serial ID for the class
     */
    @Serial
    private static final long serialVersionUID = 4L;

    /**
     * Indicates whether the door is closed.
     */
    private boolean myClosed;

    /**
     * Indicates whether the door was answered incorrectly
     */
    private boolean myAnswerIncorrect;

    /**
     * Constructs a new Door, initially closed and answerIncorrect is false.
     */
    public Door() {

        this.myClosed = true;
        this.myAnswerIncorrect = false;
    }

    /**
     * Constructs a door to change it's state to open.
     * @param initiallyOpen - The state of the door
     */
    public Door(boolean initiallyOpen) {
        this.myClosed = !initiallyOpen;
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


    /**
     * Checks if the question to the door has been answered incorrectly
     * @return True if the player answered wrong and false otherwise
     */
    public boolean hasBeenAnsweredIncorrectly() {
        return myAnswerIncorrect;
    }

}
