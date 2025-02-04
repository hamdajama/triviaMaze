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
     * Creates a short answer question that displays it to the player.
     * @param theQuestion - The question to ask the player.
     * @param theAnswer - The correct answer for the question.
     */
    public ShortAnswer(final String theQuestion,
                       final String theAnswer) {
        super(theQuestion, theAnswer);
    }

    /**
     * Checks if the typed answer from the user is the same as the correct answer.
     * @param thePlayerAnswer - The answer typed from the user.
     * @return True if the answer matched. False otherwise.
     */
    @Override
    public boolean isMatch(final String thePlayerAnswer) {
        return thePlayerAnswer.equals(getAnswer());
    }

    /**
     * Gives the player a hint to the problem.
     * @return The first resources.character of the correct answer.
     */
    protected String getHint() {
        return "The word starts with a \"" + getAnswer().charAt(0) + "\"";
    }

}
