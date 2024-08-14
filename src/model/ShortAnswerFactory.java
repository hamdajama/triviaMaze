/**
 * TCSS 360 - Trivia Maze
 * 08/13/2024
 */
package model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Factory class for the short answer class.
 * @author Eric John
 * @version 08/13/2024
 */
public class ShortAnswerFactory implements QuestionFactory {

    /**
     * Creates the question for short answer.
     * @param theRS - The results for a short answer question
     * @return A short answer question.
     * @throws SQLException when it cannot access the database.
     */
    @Override
    public Question createQuestion(final ResultSet theRS) throws SQLException {
        String question = theRS.getString("question");
        String answer = theRS.getString("correct_answer");
        return new ShortAnswer(question, answer);
    }
}