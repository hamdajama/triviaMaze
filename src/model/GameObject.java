/**
 * TCSS 360 - Trivia Maze
 * GameObject.java
 */

package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code GameObject} class represents an object within a game that has
 * a position defined by x and y coordinates. This class serves as a base
 * class for all game objects since it provides common functionality to manipulate
 * and retrieve the object's position.
 * <p>
 * This class implements {@link Serializable} to allow game objects to be
 * serialized. Enables them to be saved and restored across different game
 * sessions.
 * </p>
 *
 * <pre>
 * Example usage:
 * {@code
 * GameObject obj = new GameObject(10, 20);
 * obj.displayPosition(); // Output: GameObject is at (10, 20)
 * }
 * </pre>
 *
 * @author Masumi Yano
 * @since 1.0
 */
public class GameObject implements Serializable {

    /** The serial version UID for this class. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** The x-coordinate of the game object. */
    protected int myX;

    /** The y-coordinate of the game object. */
    protected int myY;

    /**
     * Constructs a {@code GameObject} with the specified x and y coordinates.
     *
     * @param theX the x-coordinate of the game object
     * @param theY the y-coordinate of the game object
     */
    public GameObject(final int theX, int theY) {
        this.myX = theX;
        this.myY = theY;
    }

    /**
     * Returns the x-coordinate of this game object.
     *
     * @return the x-coordinate
     */
    public final int getMyX() {
        return myX;
    }

    /**
     * Sets the x-coordinate of this game object.
     *
     * @param theX the new x-coordinate
     */
    public final void setMyX(final int theX) {
        this.myX = theX;
    }

    /**
     * Returns the y-coordinate of this game object.
     *
     * @return the y-coordinate
     */
    public final int getMyY() {
        return myY;
    }

    /**
     * Sets the y-coordinate of this game object.
     *
     * @param theY the new y-coordinate
     */
    public final void setMyY(final int theY) {
        this.myY = theY;
    }

    /**
     * Displays the position of this game object in the format:
     * {@code ClassName is at (x, y)}.
     */
    public void displayPosition() {
        System.out.println(this.getClass().getSimpleName() + " is at (" + myX + ", " + myY + ")");
    }
}
