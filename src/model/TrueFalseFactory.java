/**
 * TCSS 360 - Trivia Maze
 * TrueFalseFactory.java
 */
package model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Factory class for the true false class.
 * @author Eric John
 * @version 08/13/2024
 */
public class TrueFalseFactory implements QuestionFactory {

    /**
     * Creates the question for true false.
     * @param theRS - The results for a true false question
     * @return A true false question.
     * @throws SQLException when it cannot access the database.
     */
    @Override
    public Question createQuestion(ResultSet theRS) throws SQLException {
        String question = theRS.getString("question");
        int answer = theRS.getInt("correct_answer");
        return new TrueFalse(question, answer);
    }
}
