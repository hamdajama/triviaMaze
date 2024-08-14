package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code PlayerCharacter} class represents a player resources. character within a game
 * that can move around a maze. This class extends the {@link GameObject} class to
 * inherit its position properties and methods, and adds functionality to move
 * within specified maze dimensions.
 * <p>
 * This class provides methods to set the maze dimensions and move the player resources.character
 * up, down, left, or right within the bounds of the maze.
 * </p>
 *
 * <pre>
 * Example usage:
 * {@code
 * PlayerCharacter player = new PlayerCharacter(0, 0);
 * player.setMazeDimensions(10, 10);
 * player.moveRight();
 * player.moveDown();
 * player.displayPosition(); // Output: PlayerCharacter is at (1, 1)
 * }
 * </pre>
 *
 * @author Masumi Yano
 * @since 1.0
 */
public final class PlayerCharacter extends GameObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** The width of the maze in which the player resources. character can move. */
    private int myMazeWidth;

    /** The height of the maze in which the player resources. character can move. */
    private int myMazeHeight;

    /**
     * Constructs a {@code PlayerCharacter} with the specified initial position.
     *
     * @param theX the initial x-coordinate of the player resources. character
     * @param theY the initial y-coordinate of the player resources. character
     */
    public PlayerCharacter(final int theX, final int theY) {
        super(theX, theY);
    }

    /**
     * Sets the dimensions of the maze in which the player resources. character can move.
     *
     * @param theMazeWidth the width of the maze
     * @param theMazeHeight the height of the maze
     */
    public void setMazeDimensions(final int theMazeWidth, final int theMazeHeight) {
        this.myMazeWidth = theMazeWidth;
        this.myMazeHeight = theMazeHeight;
    }

//    /**
//     * Moves the player resources. character up by one unit if it is not at the top edge of the maze.
//     */
//    public void moveUp() {
//        if (myY > 0) {
//            myY--;
//        }
//    }
//
//    /**
//     * Moves the player resources. character down by one unit if it is not at the bottom edge of the maze.
//     */
//    public void moveDown() {
//        if (myY < myMazeHeight - 1) {
//            myY++;
//        }
//    }
//
//    /**
//     * Moves the player resources. character left by one unit if it is not at the left edge of the maze.
//     */
//    public void moveLeft() {
//        if (myX > 0) {
//            myX--;
//        }
//    }
//
//    /**
//     * Moves the player resources. character right by one unit if it is not at the right edge of the maze.
//     */
//    public void moveRight() {
//        if (myX < myMazeWidth - 1) {
//            myX++;
//        }
//    }
//
//    /**
//     * Moves the player in a given direction.
//     * @param theDirection - The direction the player is going.
//     */
//    public void move(final String theDirection) {
//        switch (theDirection) {
//            case "NORTH": moveUp(); break;
//            case "SOUTH": moveDown(); break;
//            case "WEST": moveLeft(); break;
//            case "EAST": moveRight(); break;
//        }
//    }

    /**
     * Sets the position of the player character
     * @param theX - The x coordinate
     * @param theY - The y coordinate
     */
    public void setPosition (final int theX, final int theY) {
        myX = theX;
        myY = theY;
    }

    /**
     * Displays the position of the player resources. character in the format:
     * {@code PlayerCharacter is at (x, y)}.
     */
    @Override
    public void displayPosition() {
        System.out.println("PlayerCharacter is at (" + myX + ", " + myY + ")");
    }
}
