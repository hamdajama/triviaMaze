/**
 * TCSS 360 - Trivia Maze
 * MultipleChoice.java
 */
package model;


import java.util.Map;
import java.util.HashMap;
import java.util.Random;

/**
 * The multiple choice class that presents the user with the multiple choice questions.
 * @author Eric John
 * @version 7/20/2024
 */
public class MultipleChoice extends Question{
    /**
     * The choices available to the player given with the key being the letter and
     * the value being the answer to that choice.
     */
    private final Map<String, String> myChoices;

    /**
     * The correct answer among the 4 choices from the sql file.
     */
    private final String myRightAnswer;


    /**
     * The constructor for a multiple choice question.
     * @param theQuestionID - The question ID from the sql file.
     * @param theQuestion - The question from the sql file.
     * @param theChoices - The possible choices from the sql file.
     * @param theAnswer - The correct answer from the sql file.
     */
    public MultipleChoice(final int theQuestionID, final String theQuestion,
                          final Map<String, String> theChoices, final String theAnswer) {

        super(theQuestionID, theQuestion);
        myChoices = new HashMap<>(theChoices);
        myRightAnswer = theAnswer;
    }

    /**
     * Deletes two wrong choices from the question.
     */
    protected void deleteChoice() {

        //Create two different maps.
        Map<String, String> wrongChoices = new HashMap<>();
        Map<String, String> correctChoice = new HashMap<>();
        Random randChoice = new Random();

        //Separate the correct choice from the wrong choice.
        for (Map.Entry<String, String> entry : myChoices.entrySet()) {
            if (!entry.getKey().equals(myRightAnswer)) {
                wrongChoices.put(entry.getKey(), entry.getValue());
            } else {
                correctChoice.put(entry.getKey(), entry.getValue());
            }
        }

        Object[] keys = wrongChoices.keySet().toArray();

        // Randomly remove two choices from the wrong choice map.
        while (wrongChoices.size() > 1) {
            String removeKeys = (String) keys[randChoice.nextInt(keys.length)];
            wrongChoices.remove(removeKeys);
            keys = wrongChoices.keySet().toArray();
        }


        //Clear all the choices then put the correct choice and an incorrect choice.
        myChoices.clear();
        myChoices.putAll(correctChoice);
        myChoices.putAll(wrongChoices);

    }

    /**
     * Gets the choices of the question.
     * @return The choices of the question.
     */
    protected Map<String, String> getChoices() {
        return myChoices;
    }

    /**
     * Gets the answer of the question.
     * @return The answer of the question.
     */
    protected String getAnswer() {
        return myRightAnswer;
    }

    /**
     * Checks if the user answer is the same as the right answer.
     * @param thePlayerAnswer - The answer given by the player.
     * @return True if the answer matched. False otherwise.
     */
    @Override
    protected boolean isMatch(final String thePlayerAnswer) {
        return thePlayerAnswer.equals(myRightAnswer);
    }
}
