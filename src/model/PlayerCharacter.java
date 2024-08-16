/**
 * TCSS 360 - Trivia Maze
 * PlayerCharacter.java
 */
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
public class PlayerCharacter extends GameObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
