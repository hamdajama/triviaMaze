package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortAnswerFactory implements QuestionFactory {
    @Override
    public Question createQuestion(ResultSet rs) throws SQLException {
        String question = rs.getString("question");
        String answer = rs.getString("correct_answer");
        return new ShortAnswer(question, answer);
    }
}