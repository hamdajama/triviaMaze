/**
 * TCSS 360 - Trivia Maze
 * QuestionFactory.java
 */
package model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An interface that requires any classes that inherit this to create a question
 *
 * @author  Eric John
 * @version 8/13/2024
 */
public interface QuestionFactory {

    /**
     * Creates a question for the trivia maze.
     * @param theRS - The result set.
     * @return A question.
     * @throws SQLException when game can't access database.
     */
    Question createQuestion(final ResultSet theRS) throws SQLException;
}
