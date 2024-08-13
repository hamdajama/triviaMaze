package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface QuestionFactory {

    Question createQuestion(ResultSet rs) throws SQLException;
}
