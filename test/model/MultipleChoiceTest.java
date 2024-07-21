package model;

import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the multiple choice class. NOTE: Uses ID 41 for the tests.
 * @author Eric John
 * @version 7/21/2024
 */
public class MultipleChoiceTest {

    /**
     * Question ID 41 from the sql file.
     */
    private static final int QUESTION_ID = 41;

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

        final MultipleChoice Question41 = new MultipleChoice(QUESTION_ID, QUESTION, MULTIPLE_CHOICE, RIGHT_ANSWER);

        assertAll("Four argument constructor test.",
                () -> assertEquals(QUESTION_ID, Question41.getID(), "Question ID should be " + QUESTION_ID),
                () -> assertEquals(QUESTION, Question41.getQuestion(), "The question should be \"" + QUESTION + "\""),
                () -> assertEquals(MULTIPLE_CHOICE, Question41.getChoices(), " The choices should be \"" + MULTIPLE_CHOICE + "\""),
                () -> assertEquals(RIGHT_ANSWER, Question41.getAnswer(), "The correct answer is \"" + RIGHT_ANSWER + "\""));

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

        final MultipleChoice Question41 = new MultipleChoice(QUESTION_ID, QUESTION, MULTIPLE_CHOICE, RIGHT_ANSWER);

        Question41.deleteChoice();

        assertEquals(2, Question41.getChoices().size(), "There should only be two options for this question");
        assertTrue(Question41.getChoices().containsKey(RIGHT_ANSWER), "The correct answer should still be there");
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

        final MultipleChoice Question41 = new MultipleChoice(QUESTION_ID, QUESTION, MULTIPLE_CHOICE, RIGHT_ANSWER);

        assertFalse(Question41.isMatch("B"), "Should return false. The answer given is incorrect");
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

        final MultipleChoice Question41 = new MultipleChoice(QUESTION_ID, QUESTION, MULTIPLE_CHOICE, RIGHT_ANSWER);

        assertTrue(Question41.isMatch("A"), "Should return true. The answer given is correct");
        assertTrue(Question41.isMatch(RIGHT_ANSWER), "Should return true. The answer given is correct");
    }
}
