/**
 * TCSS 360 - Trivia Maze
 * QuestionFactoryProvider.java
 */
package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A QuestionFactoryProvider that puts the other factories in this class.
 *
 * @author Eric John
 * @version 08/13/2024
 */
public class QuestionFactoryProvider implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * A map that contains the question factories for the type of question.
     */
    private final Map<String, QuestionFactory> myFactories;

    /**
     * The Database connector
     */
    private transient DatabaseConnector myDBConnector;

    /**
     * Puts all the factories into a hashmap
     * @param theDBConnector - The database of the game.
     */
    public QuestionFactoryProvider(final DatabaseConnector theDBConnector) {
        myDBConnector = theDBConnector;
        myFactories = new HashMap<>();
        myFactories.put("TrueFalse", new TrueFalseFactory(theDBConnector));
        myFactories.put("ShortAnswer", new ShortAnswerFactory(theDBConnector));
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

    /**
     * Sets up the database
     * @param theDBConnector - The database connector
     */
    public void setDatabaseConnector(final DatabaseConnector theDBConnector) {
        this.myDBConnector = theDBConnector;
        for (QuestionFactory factory : myFactories.values()) {
            if (factory instanceof TrueFalseFactory) {
                ((TrueFalseFactory) factory).setDatabaseConnector(theDBConnector);
            } else if (factory instanceof ShortAnswerFactory) {
                ((ShortAnswerFactory) factory).setDatabaseConnector(theDBConnector);
            } else if (factory instanceof MultipleChoiceFactory) {
                ((MultipleChoiceFactory) factory).setDatabaseConnector(theDBConnector);
            }
        }
    }


}
