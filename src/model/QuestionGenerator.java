package model;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * The QuestionGenerator class is a simple factory for creating different types of questions.
 * It fetches questions from True/False, Short Answer, and Multiple Choice tables.
 */
public class QuestionGenerator implements Serializable{
    private static final long serialVersionUID = 6L;

    private final DatabaseConnector dbConn;
    private final Random random;

    /**
     * Constructs a QuestionGenerator with the given DatabaseConnector.
     *
     * @param dbConn The DatabaseConnector object for accessing the database.
     */
    public QuestionGenerator(DatabaseConnector dbConn) {
        this.dbConn = dbConn;
        this.random = new Random();
    }

    /**
     * Factory method to create a random question.
     *
     * @return A randomly selected Question object.
     * @throws SQLException If an error occurs during database access.
     */
    public Question getRandomQes() throws SQLException {
        int questionType = random.nextInt(3); // Assuming 3 types of questions
        switch (questionType) {
            case 0:
                return createTrueFalseQuestion();
            case 1:
                return createMultipleChoiceQuestion();
            case 2:
                return createShortAnswerQuestion();
            default:
                throw new IllegalArgumentException("Invalid question type");
        }
    }

    /**
     * Creates a TrueFalse question from the database.
     *
     * @return A TrueFalse question.
     * @throws SQLException If an error occurs during database access.
     */
    private TrueFalse createTrueFalseQuestion() throws SQLException {
        String query = "SELECT * FROM TrueFalse ORDER BY RANDOM() LIMIT 1";
        try (Connection conn = dbConn.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {
             ResultSet rs = stmt.executeQuery(query) ;
            if (rs.next()) {
                int id = rs.getInt("id");
                String question = rs.getString("question");
                int correctAnswer = rs.getInt("correct_answer");
                return new TrueFalse(id, question, correctAnswer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new SQLException("No TrueFalse questions available");
    }

    /**
     * Creates a MultipleChoice question from the database.
     *
     * @return A MultipleChoice question.
     * @throws SQLException If an error occurs during database access.
     */
    private MultipleChoice createMultipleChoiceQuestion() throws SQLException {
        String questionQuery = "SELECT * FROM MultipleQuestion ORDER BY RANDOM() LIMIT 1";
        String choicesQuery = "SELECT * FROM MultipleChoice WHERE question_id = ?";

        try (Connection conn = dbConn.getDataSource().getConnection();
             Statement stmt = conn.createStatement() ) {
             ResultSet rs = stmt.executeQuery(questionQuery);

            if (rs.next()) {
                int id = rs.getInt("id");
                String question = rs.getString("question");
                String correctAnswer = rs.getString("correct_answer");

                Map<String, String> choices = new HashMap<>();
                try (ResultSet choiceRs = stmt.executeQuery(choicesQuery.replace("?", String.valueOf(id)))) {
                    while (choiceRs.next()) {
                        String choiceKey = choiceRs.getString("choice");
                        String choiceText = choiceRs.getString("choice_text");
                        choices.put(choiceKey, choiceText);
                    }
                }

                return new MultipleChoice(id, question, choices, correctAnswer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new SQLException("No MultipleChoice questions available");
    }

    /**
     * Creates a ShortAnswer question from the database.
     *
     * @return A ShortAnswer question.
     * @throws SQLException If an error occurs during database access.
     */
    private ShortAnswer createShortAnswerQuestion() throws SQLException {
        String query = "SELECT * FROM ShortAnswer ORDER BY RANDOM() LIMIT 1";
        try (Connection conn = dbConn.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {
             ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                int id = rs.getInt("id");
                String question = rs.getString("question");
                String correctAnswer = rs.getString("correct_answer");
                return new ShortAnswer(id, question, correctAnswer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new SQLException("No ShortAnswer questions available");
    }
}
