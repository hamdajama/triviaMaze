/**
 * TCSS 360 - Trivia Maze
 * MazeTest.java
 */
package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;



/**
 * Unit tests for the maze
 *
 * @author Eric John
 * @version 8/15/2024
 */
public class MazeTest {

    /**
     * The maze for the test.
     */
    private Maze myMaze;

    /**
     * The database connector for the test.
     */
    private DatabaseConnector myDBConnector;

    /**
     * Sets up the maze and database connector
     * @throws SQLException - Cannot access the database.
     */
    @BeforeEach
    void setUp() throws SQLException {
        myDBConnector = new DatabaseConnector();
        myMaze = new Maze(myDBConnector);
    }

    /**
     * Tests the initialization of the maze.
     */
    @Test
    void testMazeInitialization() {
        assertEquals(5, myMaze.getMazeSize());
        assertEquals(0, myMaze.getCurrentX());
        assertEquals(0, myMaze.getCurrentY());
        assertNotNull(myMaze.getCurrentRoom());
    }

    /**
     * Tests the movement of the maze.
     */
    @Test
    void testMovement() {
        assertTrue(myMaze.canMove(Direction.EAST));
        myMaze.move(Direction.EAST);
        assertTrue(myMaze.isQuestionPending());
    }

    /**
     * Tests if the maze can process a correct answer.
     */
    @Test
    void testProcessCorrectAnswer() {
        myMaze.move(Direction.EAST);
        myMaze.processAnswer(Direction.EAST, true);
        assertEquals(1, myMaze.getCurrentX());
        assertEquals(0, myMaze.getCurrentY());
        assertFalse(myMaze.isQuestionPending());
    }

    /**
     * Tests if the maze can process an incorrect answer.
     */
    @Test
    void testProcessIncorrectAnswer() {
        myMaze.move(Direction.EAST);
        myMaze.processAnswer(Direction.EAST, false);
        assertEquals(0, myMaze.getCurrentX());
        assertEquals(0, myMaze.getCurrentY());
        assertFalse(myMaze.isQuestionPending());
        assertFalse(myMaze.getCurrentRoom().isDoorOpen(Direction.EAST));
    }

    /**
     * Tests if the maze recognizes when it's at the exit point.
     */
    @Test
    void testIsExit() {
        assertFalse(myMaze.isAdjacentToExit(Direction.EAST));
        for (int i = 0; i < 3; i++) {
            myMaze.move(Direction.EAST);
            myMaze.processAnswer(Direction.EAST, true);
        }
        for (int i = 0; i < 4; i++) {
            myMaze.move(Direction.SOUTH);
            myMaze.processAnswer(Direction.SOUTH, true);
        }
        assertTrue(myMaze.isAdjacentToExit(Direction.EAST));
    }

}