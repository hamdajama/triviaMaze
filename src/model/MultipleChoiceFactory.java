package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MultipleChoiceFactory implements QuestionFactory {

    private final DatabaseConnector dbConnector;

    public MultipleChoiceFactory(DatabaseConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public Question createQuestion(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String question = rs.getString("question");
        String answer = rs.getString("correct_answer");
        Map<String, String> choices = fetchChoices(id);
        return new MultipleChoice(question, choices, answer);
    }

    private Map<String, String> fetchChoices(int questionId) {
        Map<String, String> choices = new HashMap<>();
        String query = "SELECT choice, choice_text FROM MultipleChoice WHERE question_id = " + questionId;

        try (Connection conn = dbConnector.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String choice = rs.getString("choice"); // A, B, C, D
                String choiceText = rs.getString("choice_text");
                choices.put(choice, choiceText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider logging the error or throwing a custom exception
        }

        return choices;
    }

}
