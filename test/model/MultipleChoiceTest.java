/**
 * TCSS 360 - Trivia Maze
 * MultipleChoiceTest.java
 */

package model;

import java.util.HashMap;
import java.util.Map;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the multiple choice class. NOTE: Uses ID 41 for the tests.
 * @author Eric John
 * @version 7/21/2024
 */
public class MultipleChoiceTest {


    /**
     * The question to ask the user.
     */
    private static final String QUESTION = "What house is Harry Potter sorted into?";

    /**
     * The multiple choice questions to ask the user.
     */
    private static final Map<String, String> MULTIPLE_CHOICE = new HashMap<>();



    private static final String RIGHT_ANSWER = "A";


    /**
     * Tests the constructor of the multiple choice class.
     */
    @Test
    public void testMultipleChoiceConstructor() {
        MULTIPLE_CHOICE.put("A", "Gryffindor");
        MULTIPLE_CHOICE.put("B", "Hufflepuff");
        MULTIPLE_CHOICE.put("C", "Ravenclaw");
        MULTIPLE_CHOICE.put("D", "Slytherin");

        final MultipleChoice Question41 = new MultipleChoice(QUESTION, MULTIPLE_CHOICE, RIGHT_ANSWER);

        Assertions.assertAll("Three argument constructor test.",
                () -> Assertions.assertEquals(QUESTION, Question41.getQuestion(), "The question should be \"" + QUESTION + "\""),
                () -> Assertions.assertEquals(MULTIPLE_CHOICE, Question41.getChoices(), " The choices should be \"" + MULTIPLE_CHOICE + "\""),
                () -> Assertions.assertEquals(RIGHT_ANSWER, Question41.getAnswer(), "The correct answer is \"" + RIGHT_ANSWER + "\""));

    }

    /**
     * Tests if the delete choice method removes two of the incorrect choices and the size of the map is two and
     * the correct answer is one of them.
     */
    @Test
    public void testDeleteChoice() {
        MULTIPLE_CHOICE.put("A", "Gryffindor");
        MULTIPLE_CHOICE.put("B", "Hufflepuff");
        MULTIPLE_CHOICE.put("C", "Ravenclaw");
        MULTIPLE_CHOICE.put("D", "Slytherin");

        final MultipleChoice Question41 = new MultipleChoice(QUESTION, MULTIPLE_CHOICE, RIGHT_ANSWER);

        Question41.deleteChoice();

        Assertions.assertEquals(2, Question41.getChoices().size(), "There should only be two options for this question");
        Assertions.assertTrue(Question41.getChoices().containsKey(RIGHT_ANSWER), "The correct answer should still be there");
    }

    /**
     * Tests if the incorrect answer the player typed is false.
     */
    @Test
    public void testIncorrectMatch() {
        MULTIPLE_CHOICE.put("A", "Gryffindor");
        MULTIPLE_CHOICE.put("B", "Hufflepuff");
        MULTIPLE_CHOICE.put("C", "Ravenclaw");
        MULTIPLE_CHOICE.put("D", "Slytherin");

        final MultipleChoice Question41 = new MultipleChoice(QUESTION, MULTIPLE_CHOICE, RIGHT_ANSWER);

        Assertions.assertFalse(Question41.isMatch("B"), "Should return false. The answer given is incorrect");
    }

    /**
     * Tests if the correct answer the player typed is true.
     */
    @Test
    public void testCorrectMatch() {
        MULTIPLE_CHOICE.put("A", "Gryffindor");
        MULTIPLE_CHOICE.put("B", "Hufflepuff");
        MULTIPLE_CHOICE.put("C", "Ravenclaw");
        MULTIPLE_CHOICE.put("D", "Slytherin");

        final MultipleChoice Question41 = new MultipleChoice(QUESTION, MULTIPLE_CHOICE, RIGHT_ANSWER);

        Assertions.assertTrue(Question41.isMatch("A"), "Should return true. The answer given is correct");
        Assertions.assertTrue(Question41.isMatch(RIGHT_ANSWER), "Should return true. The answer given is correct");
    }
}
