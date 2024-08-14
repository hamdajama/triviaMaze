/**
 * TCSS 360 Trivia Maze
 * TrueFalse.java
 */
package model;


/**
 * A True/False class to ask a true or false question.
 *
 * @author Eric John
 * @version 08/13/2024
 */
public class TrueFalse extends Question {

    /**
     * Creates a True/False question to ask the player.
     * @param theQuestion - The question to ask the user.
     * @param theAnswer - The correct answer of the question. NOTE: 0 means FALSE
     *                    and 1 means TRUE.
     */
    public TrueFalse(final String theQuestion, final int theAnswer) {
        super(theQuestion, String.valueOf(theAnswer));
    }

    /**
     * Checks if the answer from the player is the correct answer.
     * @param thePlayerAnswer - The answer typed from the user. NOTE: 0 means FALSE and
     *                    1 means TRUE.
     * @return True if the answer match. False otherwise.
     */
    @Override
    public boolean isMatch(final String thePlayerAnswer) {
        String convertAnswer = convertToTF(getAnswer());
        return thePlayerAnswer.equals(convertAnswer);
    }

    /**
     * Gives a user a hint for a true/false question.
     * @return What is not the right answer.
     */
    protected String getHint() {
        StringBuilder hint = new StringBuilder();
        hint.append("This question is not ");
        if (getAnswer().equals("0")) {
            hint.append("true!");
        } else {
            hint.append("false!");
        }
        return hint.toString();
    }

    /**
     * Converts the given number from 0 or 1 to True or False. A private method for the overridden
     * isMatch method.
     * @param theAnswer - The answer given by the player.
     * @return "F" if the given answer is 0. "T" if the given answer is one.
     */
    private String convertToTF(final String theAnswer) {
        String result;
        if (theAnswer.equals("0")) {
            result = "False";
        } else {
            result = "True";
        }
        return result;
    }


}
