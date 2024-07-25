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
public class ShortAnswer extends Question {
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
        super(theID, theQuestion);
        myAnswer = theAnswer;
    }

    /**
     * Checks if the typed answer from the user is the same as the correct answer.
     * @param thePlayerAnswer - The answer typed from the user.
     * @return True if the answer matched. False otherwise.
     */
    @Override
    protected boolean isMatch(final String thePlayerAnswer) {
        return thePlayerAnswer.equals(myAnswer);
    }

    /**
     * Gives the player a hint to the problem.
     * @return The first character of the correct answer.
     */
    protected String getHint() {
        return "The word starts with a \"" + myAnswer.charAt(0) + "\"";
    }


    /**
     * Gets the correct answer for the question.
     * @return The correct answer.
     */
    protected String getAnswer() {
        return myAnswer;
    }
}
