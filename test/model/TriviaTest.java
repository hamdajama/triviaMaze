/**
 * TCSS 360 - Trivia Maze
 * TriviaTest.java
 */
package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Trivia class.
 *
 * @author Eric John
 * @version 7/19/2024
 */
public class TriviaTest {

    /**
     * Tests the constructor of trivia.
     */
    @Test
    public void testTriviaConstructor() {
        final Trivia person = new Trivia("Bob");

        assertEquals("Bob", person.getName(),
                "The name for the player is incorrect");
        assertEquals(0, person.getRightAnswer(),
                "The player doesn't start with a right answer count of 0.");
        assertEquals(0, person.getWrongAnswer(),
                "The player doesn't start with a wrong answer count of 0.");
        assertEquals(0, person.getTrys(),
                "The player doesn't start with a try count of 1");

    }

    /**
     * Tests if the try count increases and decreases.
     */
    @Test
    public void testIncrementAndDecrementTrys() {
        final Trivia person = new Trivia("Bob");
        person.incrementTrys();
        assertEquals(1, person.getTrys(),
                "The player doesn't have a try count of 2");
        person.incrementTrys();
        assertEquals(2, person.getTrys(),
                "The player doesn't have a try count of 3");
        person.decrementTrys();
        assertEquals(1, person.getTrys(),
                "The player doesn't have a try count of 2 after decrementing from 3");

    }

    /**
     * Test if the right answer count increases.
     */
    @Test
    public void testIncrementRightAnswer() {
        final Trivia person = new Trivia("Bob");
        person.incrementRightAnswer();
        person.incrementRightAnswer();
        assertEquals(2, person.getRightAnswer(),
                "The player doesn't have a right answer count of 3");
    }

    /**
     * Test if the wrong person count increases.
     */
    @Test
    public void testIncrementWrongAnswer() {
        final Trivia person = new Trivia("Bob");
        person.incrementWrongAnswer();
        assertEquals(1, person.getWrongAnswer(),
                "The player doesn't have a wrong answer count of 2");
    }

    /**
     * Test the isRight method by providing the given answer, correct answer, and player answer.
     */
    @Test
    public void testIsRightAnswer() {
        final Trivia person = new Trivia("Bob");
        final String givenAnswer = "a";
        final String correctAnswer = "a";
        final String playerAnswer = "b";
        assertFalse(person.isRightAnswer(playerAnswer, correctAnswer),
                "This should return false. The answer given is false.");
        assertTrue(person.isRightAnswer(givenAnswer, correctAnswer),
                "This should return true. The given answer is the same as the correct answer.");
    }

    /**
     * Test the timer method for the Trivia.
     * NOTE: More thorough testing should be made when the game is playable from the GUI.
     */
    @Test
    public void testGetTime() {
        final Trivia person = new Trivia("Bob");
        assertEquals(0, person.getTime(), "Timer didn't start. " +
                "Total time should be 0");
        person.startTimer();
        person.stopTimer();
        assertEquals(0, person.getTime(), "Timer started and stopped. " +
                "It should be 0.");
    }

}
