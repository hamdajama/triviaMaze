/**
 * TCSS 360 - Trivia Maze
 * Direction.java
 */

package model;

/**
 * Enumeration class for directions in the maze.
 */
public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    /**
     * Opposite direction for the direction.
     * @return The opposite direction. (North <--> South AND West <--> East
     */
    public Direction getOpposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}