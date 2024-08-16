/**
 * TCSS 360 - Trivia Maze
 * QuestionGenerator.java
 */
package model;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The QuestionGenerator class is responsible for generating random questions
 * from the database for the TriviaMaze game. It fetches questions from
 * True/False, Short Answer, and Multiple Choice tables.
 */
public class QuestionGenerator implements Serializable {
    /**
     * Serial for QuestionGenerator
     */
    @Serial
    private static final long serialVersionUID = 6L;

    /**
     * A random variable to randomize the type of question.
     */
    private final Random myRandom;

    /**
     * The database connector
     */
    private final DatabaseConnector myDataConn;

    /**
     * A question factory provider.
     */
    private final QuestionFactoryProvider myFactoryProvider;
    /**
     * Constructs a new QuestionGenerator with the given DatabaseConnector.
     *
     * @param theDBConn The DatabaseConnector object for accessing the database.
     */
    public QuestionGenerator(final DatabaseConnector theDBConn) {
        this.myDataConn = theDBConn;
        this.myRandom = new Random();
        this.myFactoryProvider = new QuestionFactoryProvider(theDBConn);
    }

    /**
     * Retrieves a random question from the database.
     *
     * @return A random Question object, or null if no questions are found.
     */
    public Question getRandomQes () {
        List<Question> questions = new ArrayList<>();
        try(Connection conn = myDataConn.getDataSource().getConnection();
            Statement stmt  = conn.createStatement()) {
            questions.addAll(getTable(stmt, "TrueFalse"));
            questions.addAll(getTable(stmt,"ShortAnswer"));
            questions.addAll(getTable(stmt,"MultipleQuestion"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (questions.isEmpty()) {
            return null;
        }
        return questions.get(myRandom.nextInt(questions.size()));
    }

    /**
     * Retrieves questions from a specific table and converts them to Question objects.
     *
     * @param theStmt The Statement object for executing the query.
     * @param theTableName The name of the table to query.
     * @return A list of Question objects.
     * @throws SQLException If a database access error occurs.
     */
    private List<Question> getTable(final Statement theStmt, final String theTableName)
                                    throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM " + theTableName;
        ResultSet rs = theStmt.executeQuery(query);

        QuestionFactory factory = myFactoryProvider.getFactory(theTableName);
        while (rs.next()) {
            questions.add(factory.createQuestion(rs));
        }
        return questions;
    }
}
