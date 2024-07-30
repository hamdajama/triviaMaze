package object;

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
    private static final long serialVersionUID = 1L;

    /** The x-coordinate of the game object. */
    protected int x;

    /** The y-coordinate of the game object. */
    protected int y;

    /**
     * Constructs a {@code GameObject} with the specified x and y coordinates.
     *
     * @param x the x-coordinate of the game object
     * @param y the y-coordinate of the game object
     */
    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of this game object.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of this game object.
     *
     * @param x the new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the y-coordinate of this game object.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of this game object.
     *
     * @param y the new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Displays the position of this game object in the format:
     * {@code ClassName is at (x, y)}.
     */
    public void displayPosition() {
        System.out.println(this.getClass().getSimpleName() + " is at (" + x + ", " + y + ")");
    }
}
