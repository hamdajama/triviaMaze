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
     * The question to be asked to the player, as given in the sql file.
     */
    private final String myQuestion;

    private final String myAnswer;

    /**
     * The question constructor. Super should be called in Multiple choice, short answer,
     * and T/F classes.
     * @param theQuestion - The question to be asked.
     * @param theAnswer - The answer for the question.
     */
    public Question(final String theQuestion, final String theAnswer) {
        myQuestion = theQuestion;
        myAnswer = theAnswer;
    }


    /**
     * Gets the question to ask the player.
     * @return The question.
     */
    public String getQuestion() {
        return myQuestion;
    }

    /**
     * Returns the answer of the question.
     * @return The answer
     */
    public String getAnswer() {
        return myAnswer;
    }

    /**
     * Checks if the player answer is the same as the correct answer.
     * @param thePlayerAnswer - The answer given by the player.
     * @return True if the answer matched. False otherwise.
     */
    public abstract boolean isMatch(final String thePlayerAnswer);

}
