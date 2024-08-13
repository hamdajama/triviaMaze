package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrueFalseFactory implements QuestionFactory {

    @Override
    public Question createQuestion(ResultSet rs) throws SQLException {
        String question = rs.getString("question");
        int answer = rs.getInt("correct_answer");
        return new TrueFalse(question, answer);
    }
}
