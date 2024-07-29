package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {
    private Random myRandom;
    private DatabaseConnector myDataConn ;
    public QuestionGenerator(DatabaseConnector theDBConn) {
        this.myDataConn = theDBConn;
        this.myRandom = new Random();
    }
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

    private List<String> choices(int theID) {
        int ID = theID;
        List<String> choices = new ArrayList<>();
        String query = "SELECT choice_text FROM MultipleChoice WHERE question_id = " + ID;

        try(Connection conn  = myDataConn.getDataSource().getConnection();
           Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                choices.add(rs.getString("choice_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return choices;
    }
}
