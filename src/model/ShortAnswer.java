/**
 * TCSS 360 - Trivia Maze
 * ShortAnswer.java
 */
package model;

/**
 * Creates a short answer type question to the player.
 * @author Eric John
 * @version 7/20/2024
 */
public class ShortAnswer {

    /**
     * The ID of the question from the sql file.
     */
    private final int myID;

    /**
     * The short answer question from the sql file.
     */
    private final String myQuestion;

    /**
     * The correct answer for the short answer from the sql file.
     */
    private final String myAnswer;


    /**
     * Creates a short answer question that displays it to the player.
     * @param theID - The ID of the question.
     * @param theQuestion - The question to ask the player.
     * @param theAnswer - The correct answer for the question.
     */
    public ShortAnswer(final int theID, final String theQuestion,
                       final String theAnswer) {
        myID = theID;
        myQuestion = theQuestion;
        myAnswer = theAnswer;
    }

    /**
     * Checks if the typed answer from the user is the same as the correct answer.
     * @param theAnswer - The answer typed from the user.
     * @return True if the answer matched. False otherwise.
     */
    protected boolean isMatch(final String theAnswer) {
        return theAnswer.equals(myAnswer);
    }

    /**
     * Gives the player a hint to the problem.
     * @param theAnswer - The correct answer to the problem.
     * @return The first character of the correct answer.
     */
    protected String getHint(final String theAnswer) {
        return "The word starts with a \"" + theAnswer.charAt(0) + "\"";
    }

    /**
     * Gets the ID of the question.
     * @return The ID of the question.
     */
    protected int getID() {
        return myID;
    }

    /**
     * Gets the question to ask the user.
     * @return The question.
     */
    protected String getQuestion() {
        return myQuestion;
    }

    /**
     * Gets the correct answer for the question.
     * @return The correct answer.
     */
    protected String getAnswer() {
        return myAnswer;
    }
}
