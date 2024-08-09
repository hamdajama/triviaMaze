package model;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * The {@code PlayerCharacter} class represents a player character within a game
 * that can move around a maze. This class extends the {@link GameObject} class to
 * inherit its position properties and methods, and adds functionality to move
 * within specified maze dimensions.
 * <p>
 * This class provides methods to set the maze dimensions and move the player character
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
public final class PlayerCharacter extends GameObject {

    /** The width of the maze in which the player character can move. */
    private int mazeWidth;

    /** The height of the maze in which the player character can move. */
    private int mazeHeight;

    /** Map to hold character images for each direction. */
    private Map<String, BufferedImage[]> characterImages;

    /**
     * Constructs a {@code PlayerCharacter} with the specified initial position.
     *
     * @param x the initial x-coordinate of the player character
     * @param y the initial y-coordinate of the player character
     */
    public PlayerCharacter(int x, int y) {
        super(x, y);
    }

    /**
     * Sets the dimensions of the maze in which the player character can move.
     *
     * @param mazeWidth the width of the maze
     * @param mazeHeight the height of the maze
     */
    public final void setMazeDimensions(int mazeWidth, int mazeHeight) {
        this.mazeWidth = mazeWidth;
        this.mazeHeight = mazeHeight;
    }

    /**
     * Sets the character images for each direction.
     *
     * @param images the map of direction keys to image arrays
     */
    public void setCharacterImages(Map<String, BufferedImage[]> images) {
        this.characterImages = images;
    }

    /**
     * Gets the character images for the current direction.
     *
     * @param direction the current direction of the player
     * @return the array of images for the current direction
     */
    public BufferedImage[] getCharacterImages(String direction) {
        return characterImages.get(direction);
    }

    /**
     * Moves the player character up by one unit if it is not at the top edge of the maze.
     */
    public final void moveUp() {
        if (y > 0) {
            y--;
        }
    }

    /**
     * Moves the player character down by one unit if it is not at the bottom edge of the maze.
     */
    public final void moveDown() {
        if (y < mazeHeight - 1) {
            y++;
        }
    }

    /**
     * Moves the player character left by one unit if it is not at the left edge of the maze.
     */
    public final void moveLeft() {
        if (x > 0) {
            x--;
        }
    }

    /**
     * Moves the player character right by one unit if it is not at the right edge of the maze.
     */
    public final void moveRight() {
        if (x < mazeWidth - 1) {
            x++;
        }
    }

    /**
     * Displays the position of the player character in the format:
     * {@code PlayerCharacter is at (x, y)}.
     */
    @Override
    public final void displayPosition() {
        System.out.println("PlayerCharacter is at (" + x + ", " + y + ")");
    }
}
