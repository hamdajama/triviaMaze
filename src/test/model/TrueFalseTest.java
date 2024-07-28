///**
// * TCSS 360 - Trivia Maze
// * TrueFalseTest.java
// */
//package test.model;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import model.TrueFalse;
//import org.junit.jupiter.api.Test;
//
//
//
//public class TrueFalseTest {
//
//    /**
//     * The question ID from the sql file.
//     */
//    private static final int QUESTION_ID = 8;
//
//    /**
//     * The question that comes with questionID 8
//     */
//    private static final String QUESTION = "Yoda trained Luke Skywalker";
//
//    /**
//     * The answer to the question.
//     */
//    private static final int ANSWER = 1;
//
//    /**
//     * Tests the constructor of the true false class.
//     */
//    @Test
//    public void testTrueFalseConstructor() {
//        final TrueFalse Question8 = new TrueFalse(QUESTION_ID, QUESTION, ANSWER);
//        assertAll("Three argument constructor test.",
//                () -> assertEquals(QUESTION_ID,  Question8.getID(), "The ID does not match"),
//                () -> assertEquals(QUESTION, Question8.getQuestion(), "The question does not match"),
//                () -> assertEquals(ANSWER, Question8.getAnswer(), "The answer does not match"));
//    }
//
//    /**
//     * Tests if the class gets the correct question ID.
//     */
//    @Test
//    public void testGetID() {
//        final TrueFalse Question8 = new TrueFalse(QUESTION_ID, QUESTION, ANSWER);
//        assertEquals(QUESTION_ID, Question8.getID(), "The method doesn't get the correct " +
//                "ID number");
//    }
//
//    /**
//     * Tests if the class gets the correct question.
//     */
//    @Test
//    public void testGetQuestion() {
//        final TrueFalse Question8 = new TrueFalse(QUESTION_ID, QUESTION, ANSWER);
//        assertEquals(QUESTION, Question8.getQuestion(), "The question being displayed is " +
//                "not the same as the one in the db.");
//    }
//
//    /**
//     * Tests if the class gets the correct answer.
//     */
//    @Test
//    public void testGetAnswer() {
//        final TrueFalse Question8 = new TrueFalse(QUESTION_ID, QUESTION, ANSWER);
//        assertEquals(ANSWER, Question8.getAnswer(), "The answer being received is not " +
//                "the same as the one in the db");
//
//    }
//
//    /**
//     * Tests if the method returns false for an incorrect answer.
//     */
//    @Test
//    public void testWrongAnswer() {
//        final TrueFalse Question8 = new TrueFalse(QUESTION_ID, QUESTION, ANSWER);
//        assertFalse(Question8.isMatch("F"), "This test should return false");
//    }
//
//    /**
//     * Tests if the method returns true for a correct answer.
//     */
//    @Test
//    public void testCorrectAnswer() {
//        final TrueFalse Question8 = new TrueFalse(QUESTION_ID, QUESTION, ANSWER);
//        assertTrue(Question8.isMatch("T"), "This test should return true");
//    }
//
//    /**
//     * Tests if the method displays the correct hint for the question.
//     */
//    @Test
//    public void testGetHint() {
//        final TrueFalse Question8 = new TrueFalse(QUESTION_ID, QUESTION, ANSWER);
//        assertEquals("This question is not false!", Question8.getHint(), "The output is " +
//                "displayed incorrectly");
//    }
//
//}
