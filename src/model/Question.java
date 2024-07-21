/**
 * TCSS 360 - Trivia Maze
 * Question.java
 */
package model;

/**
 * An abstract class that is given to the Multiple choice, short answer, and T/F classes.
 */
public abstract class Question {

    /**
     * The ID of the question, as given in the sql file.
     */
    private final int myID;

    /**
     * The question to be asked to the player, as given in the sql file.
     */
    private final String myQuestion;

    /**
     * The question constructor. Super should be called in Multiple choice, short answer,
     * and T/F classes.
     * @param theID - The ID of the question.
     * @param theQuestion - The question to be asked.
     */
    public Question(final int theID, final String theQuestion) {
        myID = theID;
        myQuestion = theQuestion;
    }

    /**
     * Gets the ID from the sql file.
     * @return The ID of the question.
     */
    protected int getID() {
        return myID;
    }

    /**
     * Gets the question to ask the player.
     * @return The question.
     */
    protected String getQuestion() {
        return myQuestion;
    }

    /**
     * Checks if the player answer is the same as the correct answer.
     * @param thePlayerAnswer - The answer given by the player.
     * @return True if the answer matched. False otherwise.
     */
    protected abstract boolean isMatch(final String thePlayerAnswer);

}
