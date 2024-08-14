/**
 * TCSS 360 - Trivia Maze
 * QuestionFactoryProvider
 */
package model;

import java.util.HashMap;
import java.util.Map;

/**
 * A QuestionFactoryProvider that puts the other factories in this class.
 *
 * @author Eric John
 * @version 08/13/2024
 */
public class QuestionFactoryProvider {
    /**
     * A map that contains the question factories for the type of question.
     */
    private final Map<String, QuestionFactory> myFactories;

    /**
     * Puts all the factories into a hashmap
     * @param theDBConnector - The database of the game.
     */
    public QuestionFactoryProvider(final DatabaseConnector theDBConnector) {
        myFactories = new HashMap<>();
        myFactories.put("TrueFalse", new TrueFalseFactory());
        myFactories.put("ShortAnswer", new ShortAnswerFactory());
        myFactories.put("MultipleQuestion", new MultipleChoiceFactory(theDBConnector));
    }

    /**
     * Gets the specific factory for the database
     * @param theTableName - The type of question
     * @return The factory for the type of question.
     */
    public QuestionFactory getFactory(final String theTableName) {
        return myFactories.get(theTableName);
    }
}
