/**
 * TCSS 360 - Trivia Maze
 * MoveEvent.java
 */

package model;

/**
 * Small class to handle moving the player between rooms
 *
 * @author Eric John
 * @version 8/15/2024
 */
public class MoveEvent {
    private final Room myRoom;
    private final Direction myDirection;
    private final int myX;
    private final int myY;

    /**
     * Moves the player to the given room
     * @param theRoom - The room to head to
     * @param theDirection - The direction the room is at
     * @param theX - The x position
     * @param theY - The y position
     */
    public MoveEvent(final Room theRoom, final Direction theDirection,
                     final int theX, final int theY) {
        myRoom = theRoom;
        myDirection = theDirection;
        myX = theX;
        myY = theY;
    }

    /**
     * Gets the room to go to
     * @return The room
     */
    public Room getRoom() {
        return myRoom;
    }

    /**
     * Gets the x coordinate
     * @return The x coordinate
     */
    public int getX() {
        return myX;
    }

    /**
     * Gets the y coordinate
     * @return The y coordinate
     */
    public int getY() {
        return myY;
    }
}
