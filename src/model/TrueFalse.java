/**
 * TCSS 360 Trivia Maze
 * TrueFalse.java
 */
package model;

/**
 * A True/False class to ask a true or false question.
 */
public class TrueFalse {

    /**
     * The ID of the question from the sql file.
     */
    private final int myID;

    /**
     * The question from the sql file.
     */
    private final String myQuestion;

    /**
     * The answer from the sql file.
     */
    private final int myAnswer;

    /**
     * Creates a True/False question to ask the player.
     * @param theID - The ID of the question.
     * @param theQuestion - The question to ask the user.
     * @param theAnswer - The answer of the question. NOTE: 0 means FALSE
     *                    and 1 means TRUE.
     */
    public TrueFalse(final int theID, final String theQuestion, final int theAnswer) {

        if (theAnswer != 0 && theAnswer != 1) {
            throw new IllegalArgumentException("The answer must be 0 for FALSE " +
                                                "or 1 for TRUE.");
        }

        myID = theID;
        myQuestion = theQuestion;
        myAnswer = theAnswer;
    }

    /**
     * Checks if the answer from the player is the correct answer.
     * @param theAnswer - The answer of the question. NOTE: 0 means FALSE and
     *                    1 means TRUE.
     * @return True if the answer match. False otherwise.
     */
    protected boolean isMatch(final int theAnswer) {
        return theAnswer == myAnswer;
    }

    /**
     * Gives a user a hint for a true/false question.
     * @param theAnswer - The correct answer for the question.
     * @return What is not the right answer.
     */
    protected String getHint(final int theAnswer) {
        StringBuilder hint = new StringBuilder();
        hint.append("This question is not ");
        if (theAnswer == 0) {
            hint.append("true!");
        } else {
            hint.append("false!");
        }
        return hint.toString();
    }

    /**
     * Gets the ID of the question.
     * @return The ID.
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
     * Gets the correct answer for the question.
     * @return The correct answer.
     */
    protected int getAnswer() {
        return myAnswer;
    }


}
