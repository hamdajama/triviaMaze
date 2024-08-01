package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/**
 * The QuestionGenerator class is responsible for generating random questions
 * from the database for the TriviaMaze game. It fetches questions from
 * True/False, Short Answer, and Multiple Choice tables.
 */
public class QuestionGenerator {
    private Random myRandom;
    private DatabaseConnector myDataConn ;
    /**
     * Constructs a new QuestionGenerator with the given DatabaseConnector.
     *
     * @param theDBConn The DatabaseConnector object for accessing the database.
     */
    public QuestionGenerator(DatabaseConnector theDBConn) {
        this.myDataConn = theDBConn;
        this.myRandom = new Random();
    }
    
    /**
     * Retrieves a random question from the database.
     *
     * @return A random Question object, or null if no questions are found.
     * @throws SQLException If an error occurs during database access.
     */
    public Question getRandomQes () throws SQLException {
        List<Question> questions = new ArrayList<>();
        try(Connection conn = myDataConn.getDataSource().getConnection();
            Statement stmt  = conn.createStatement()) {
            questions.addAll(getTable(stmt, "TrueFalse"));
            questions.addAll(getTable(stmt,"ShortAnswer"));
            questions.addAll(getTable(stmt,"MultipleQuestion"));
        } catch (SQLException e) {
            e.printStackTrace();
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
    private List<Question> getTable(Statement theStmt, String theTableName) throws SQLException {
        Statement stmt = theStmt;
        String table = theTableName;
        List<Question> questions  = new ArrayList<>();
        String query = "SELECT * FROM " + table;
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String questionText = rs.getString("question");
            switch (table) {
                case "TrueFalse":
                    int correctAnswerTF = rs.getInt("correct_answer");
                    questions.add(new TrueFalse(id, questionText, correctAnswerTF));
                    break;
                case "shortAnswer" :
                    String correctAnsSA = rs.getString("correct_answer");
                    questions.add(new ShortAnswer(id, questionText, correctAnsSA));
                    break;
                case "MultipleQuestion" :
                    String correctAnswerMQ = rs.getString("correct_answer");
                    questions.add(new MultipleChoice(id, questionText, choices(id), correctAnswerMQ));
                    break;
                default:
                    break;
            }
        }
        return questions;
    }
   /**
     * Retrieves multiple choice options for a given question ID.
     *
     * @param theID The ID of the question.
     * @return A map of choice, to choice_text.
     */
    private Map<String, String> choices(int theID) {
        int ID = theID;
        Map<String,String> choices = new HashMap<>();
        String query = "SELECT choice, choice_text FROM MultipleChoice WHERE question_id = " + ID;

        try(Connection conn  = myDataConn.getDataSource().getConnection();
           Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String choice = rs.getString("choice"); // - A, B, C, D
                String choice_Text = rs.getString("choice_text"); 
                choices.put(choice,choice_Text);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return choices;
    }
}
