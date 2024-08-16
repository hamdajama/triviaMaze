/**
 * TCSS 360 - Trivia Maze
 * MultipleChoiceFactory
 */
package model;

import java.io.Serial;
import java.io.Serializable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for the multiple choice class.
 * @author Eric John
 * @version 08/13/2024
 */
public class MultipleChoiceFactory implements QuestionFactory, Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The database connector.
     */
    private transient DatabaseConnector myDBConnector;

    /**
     * Multiple choice factory for the classes.
     * @param theDBConnector - THe connector to get the choices
     */
    public MultipleChoiceFactory(final DatabaseConnector theDBConnector) {
        this.myDBConnector = theDBConnector;
    }

    /**
     * Creates the question for multiple choice.
     * @param theRS - The results for a multiple choice question
     * @return A multiple choice question.
     * @throws SQLException when it cannot access the database.
     */
    @Override
    public Question createQuestion(final ResultSet theRS) throws SQLException {
        int id = theRS.getInt("id");
        String question = theRS.getString("question");
        String answer = theRS.getString("correct_answer");
        Map<String, String> choices = fetchChoices(id);
        return new MultipleChoice(question, choices, answer);
    }

    /**
     * Gets the choices for the multiple choice question
     * @param theQuestionId - The ID associated with the question
     * @return The choices available for the question.
     */
    private Map<String, String> fetchChoices(final int theQuestionId) {
        Map<String, String> choices = new HashMap<>();
        String query = "SELECT choice, choice_text FROM MultipleChoice WHERE question_id = "
                        + theQuestionId;

        try (Connection conn = myDBConnector.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String choice = rs.getString("choice");
                String choiceText = rs.getString("choice_text");
                choices.put(choice, choiceText);
            }
        } catch (SQLException e) {
           System.out.println("There's seem to be an SQLException: " + e.getMessage());
        }

        return choices;
    }

    /**
     * Sets the database connector
     * @param theDBConnector - The database connector
     */
    public void setDatabaseConnector(final DatabaseConnector theDBConnector) {
        this.myDBConnector = theDBConnector;
    }

}
