/**
 * TCSS 360 Trivia Class
 * Summer 2024
 */
package model;

/**
 * Creates a Trivia logic for the game along with keeping track of the players right
 * and wrong answers when going through the maze. Along with that, calculates the time
 * it takes to reach the exit.
 * @author Eric John
 * @version 7/18/2024
 */
public class Trivia {

    /**
     * The name of the player.
     */
    private static String myName;

    /**
     * How many right answers the player got.
     */
    private static int myRightAnswer;

    /**
     * How many wrong answers the player got.
     */
    private static int myWrongAnswer;

    /**
     * How many tries left the player has before the door is locked.
     */
    private static int myTrys;

    /**
     * The starting time of the timer
     */
    private static long myStartTime;

    /**
     * The total time it takes for the player to reach the exit.
     */
    private static long myTotalTime;

    /**
     * Creates the trivia and adds the inputted name to myName.
     * @param theName - The name of the player.
     */
    protected Trivia(final String theName) {
        myName = theName;
        myRightAnswer = 0;
        myWrongAnswer = 0;
        myTrys = 1;
        myStartTime = 0;
        myTotalTime = 0;
    }

    /**
     * Gets the name of the player.
     * @return The name of the player
     */
    protected String getName() {
        return myName;
    }

    /**
     * Gets the amount of tries for the player.
     * @return The number of tries left.
     */
    protected int getTrys() {
        return myTrys;
    }

    /**
     * When the player finds the treasure, increment the try count.
     */
    protected void incrementTrys() {
        myTrys++;
    }

    /**
     * When the player gets a question wrong, decrement the try count.
     */
    protected void decrementTrys() {
        myTrys--;
    }

    /**
     * Gets the count of how many questions the player got right.
     * @return The right answer.
     */
    protected int getRightAnswer() {
        return myRightAnswer;
    }

    /**
     * Increments the right answer count.
     */
    protected void incrementRightAnswer() {
        myRightAnswer++;
    }

    /**
     * Gets the count of how many questions the player got wrong.
     * @return The wrong answer.
     */
    protected int getWrongAnswer() {
        return myWrongAnswer;
    }

    /**
     * Increments the wrong answer count.
     */
    protected void incrementWrongAnswer() {
        myWrongAnswer++;
    }

    /**
     * Checks if the given answer of the player is the right answer for the question.
     * @param theGivenAnswer - The given answer of the player.
     * @param theRightAnswer - The right answer of the question.
     * @return - True if the player answered correctly. False otherwise.
     */
    protected boolean isRightAnswer(final String theGivenAnswer,final String theRightAnswer) {
        return theGivenAnswer.equals(theRightAnswer);
    }

    /**
     * Starts the timer of the game.
     */
    protected void startTimer() {
        myStartTime = System.currentTimeMillis();
    }

    /**
     * When the timer stops, this method calculates the time from starting to stopping.
     */
    protected void stopTimer() {
        if (myStartTime != 0) {
            myTotalTime += System.currentTimeMillis() - myStartTime;
            myStartTime = 0;
        }
    }

    /**
     * Gets the time.
     * @return The total time it takes.
     */
    protected long getTime() {
        return myTotalTime;
    }
}
