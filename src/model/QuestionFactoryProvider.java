package model;

import java.util.HashMap;
import java.util.Map;

public class QuestionFactoryProvider {
    private final Map<String, QuestionFactory> factories;

    public QuestionFactoryProvider(DatabaseConnector dbConnector) {
        factories = new HashMap<>();
        factories.put("TrueFalse", new TrueFalseFactory());
        factories.put("ShortAnswer", new ShortAnswerFactory());
        factories.put("MultipleQuestion", new MultipleChoiceFactory(dbConnector));
    }

    public QuestionFactory getFactory(String tableName) {
        return factories.get(tableName);
    }
}
