/**
 * TCSS 360 - Trivia Maze
 * QuestionEvent.java
 */
package model;

/**
 * Small class to handle the questions for the game.
 *
 * @author Eric John
 * @version 8/15/2024
 */
public class QuestionEvent {
    private final Question myQuestion;
    private final Direction myDirection;

    /**
     * Handles a question being passed to the room panel
     * @param question - The question to be asked
     * @param direction - The direction the player is going
     */
    public QuestionEvent(final Question question, final Direction direction) {
        myQuestion = question;
        myDirection = direction;
    }

    /**
     * Gets the question to ask the player
     * @return The question to ask the player
     */
    public Question getQuestion() {
        return myQuestion;
    }

    /**
     * Gets the direction the player is going
     * @return The direction the player is heading
     */
    public Direction getDirection() {
        return myDirection;
    }
}