package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ShortAnswerTest {
    private static final int QUESTION_ID = 26;

    private static final String QUESTION = "What is the name of the toy cowboy in Toy Story?";

    private static final String ANSWER = "Woody";

    @Test
    public void testShortAnswerConstructor() {
        final ShortAnswer Question26 = new ShortAnswer(QUESTION_ID, QUESTION, ANSWER);

        assertAll("Three argument constructor test,",
                () -> assertEquals(QUESTION_ID, Question26.getID(), "The ID does not match."),
                () -> assertEquals(QUESTION, Question26.getQuestion(), "The answer displayed is not the same as the database"),
                () -> assertEquals(ANSWER, Question26.getAnswer(), "The answer is not the same as the database"));
    }

    @Test
    public void testGetQuestionID() {
        final ShortAnswer Question26 = new ShortAnswer(QUESTION_ID, QUESTION, ANSWER);
        assertEquals(QUESTION_ID, Question26.getID(),"The method does not get the correct question id");
    }

    @Test
    public void testGetQuestion() {
        final ShortAnswer Question26 = new ShortAnswer(QUESTION_ID, QUESTION, ANSWER);
        assertEquals(QUESTION, Question26.getQuestion(),"The method does not get the correct question");
    }

    @Test
    public void testGetAnswer() {
        final ShortAnswer Question26 = new ShortAnswer(QUESTION_ID, QUESTION, ANSWER);
        assertEquals(ANSWER, Question26.getAnswer(), "The method does not get the correct answer");
    }

    @Test
    public void testIncorrectAnswer() {
        final ShortAnswer Question26 = new ShortAnswer(QUESTION_ID, QUESTION, ANSWER);
        assertFalse(Question26.isMatch("Buzz Lightyear"),"The method should return false");
    }

    @Test
    public void testCorrectAnswer() {
        final ShortAnswer Question26 = new ShortAnswer(QUESTION_ID, QUESTION, ANSWER);
        assertTrue(Question26.isMatch("Woody"),"The method should return true");
    }

    @Test
    public void testGetHint() {
        final ShortAnswer Question26 = new ShortAnswer(QUESTION_ID, QUESTION, ANSWER);
        assertEquals("The word starts with a \"W\"", Question26.getHint(),"The first character is wrong");
    }


}
