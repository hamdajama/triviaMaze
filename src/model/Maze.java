/**
 * TCSS 360 - Trivia Maze
 * Maze.java
 */

package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLException;

import java.io.*;

/**
 * Maze class represents a 5x5 grid of rooms in the TriviaMaze game.
 * It handles the setup of rooms with questions, player movement, and game state events.
 *
 * @version 8/7/2024
 */
public class Maze implements Serializable {
    /**
     * Constant Maze size for the maze.
     */
    private static final int MAZE_SIZE = 5;

    /**
     * Serial for the Maze
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Array full of rooms representing the map
     */
    private Room[][] myMap;

    /**
     * Current x position of the player
     */
    private int myCurrentX;

    /**
     * Current y position of the player
     */
    private int myCurrentY;

    /**
     * Property change support to communicate to the view
     */
    private PropertyChangeSupport mySupport;

    /**
     * Database connection for the maze
     */
    private transient DatabaseConnector myDBConn;

    /**
     * Question generator that generates a question
     */
    private QuestionGenerator myQesGen;

    /**
     * A boolean asking if there is a question waiting to be asked.
     */
    private boolean myQuestionPending = false;

    /**
     * The direction the player is heading.
     */
    private Direction myPendingDirection = null;

    /**
     * The trivia for the game.
     */
    private final Trivia myTrivia;

    private QuestionFactoryProvider questionFactoryProvider;

    /**
     * Constructs a new Maze, initializing the game grid and questions.
     *
     * @param theDBConn The DatabaseConnector object for accessing the question database.
     * @throws SQLException If an error occurs during database access.
     */
    public Maze(final DatabaseConnector theDBConn) throws SQLException {
        this.myDBConn = theDBConn;
        this.questionFactoryProvider = new QuestionFactoryProvider(theDBConn);
        this.myQesGen = new QuestionGenerator(theDBConn);
        this.mySupport = new PropertyChangeSupport(this);
        this.myMap = new Room[MAZE_SIZE][MAZE_SIZE];
        buildMap();
        setAdjacentRooms();
        myCurrentX = 0;
        myCurrentY = 0;
        setAdjacentRooms();
        myTrivia = new Trivia("Player");
        myTrivia.startTimer();
    }

    /**
     * Builds the map with rooms, each containing a randomly assigned question.
     */
    private void buildMap() {
        myMap = new Room[MAZE_SIZE][MAZE_SIZE];
        for (int i = 0; i < MAZE_SIZE; i++) {
            for (int j = 0; j < MAZE_SIZE; j++) {
                Question question = myQesGen.getRandomQes();
                Room room = new Room(question);

                for (Direction dir : Direction.values()) {
                    room.setDoorOpen(dir,true);
                }

                if (i == 0) room.setDoorOpen(Direction.NORTH, false);
                if (j == 0) room.setDoorOpen(Direction.WEST, false);


                myMap[i][j] = room;
            }
        }
    }

    /**
     * Sets up adjacent rooms for the maze.
     */
    private void setAdjacentRooms() {
        for (int i = 0; i < MAZE_SIZE; i++) {
            for (int j = 0; j < MAZE_SIZE; j++) {
                if (i > 0) myMap[i][j].setAdjacentRoom(Direction.NORTH, myMap[i-1][j]);
                if (i < MAZE_SIZE-1) myMap[i][j].setAdjacentRoom(Direction.SOUTH, myMap[i+1][j]);
                if (j > 0) myMap[i][j].setAdjacentRoom(Direction.WEST, myMap[i][j-1]);
                if (j < MAZE_SIZE-1) myMap[i][j].setAdjacentRoom(Direction.EAST, myMap[i][j+1]);
            }
        }
    }

    /**
     * Adds a PropertyChangeListener to listen for property changes in the game state.
     *
     * @param theListener The PropertyChangeListener to add.
     */
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        mySupport.addPropertyChangeListener(theListener);
    }

    /**
     * Returns the size of the maze
     * @return The size of the maze
     */
    public int getMazeSize() {
        return MAZE_SIZE;
    }

    /**
     * Retrieves the room at the specified coordinates.
     *
     * @param theX The X-coordinate of the room.
     * @param theY The Y-coordinate of the room.
     * @return The Room object at the specified coordinates, or null if out of bounds.
     */
    public Room getRoom(final int theX, final int theY) {
        if (theX >= 0 && theX < MAZE_SIZE && theY >= 0 && theY < MAZE_SIZE) {
            return myMap[theX][theY];
        }
        return null;
    }

    /**
     * Starts the game by placing the player at the starting position.
     */
    public void startGame() {
        myCurrentX = 0;
        myCurrentY = 0;
        mySupport.firePropertyChange("start game", null, getCurrentRoom());
    }


    /**
     * Processes the answer and determines if the player moves in the given direction.
     * @param theDirection - The direction the player is headed.
     * @param isCorrect - True if the question is correct and false otherwise.
     */
    public void processAnswer(final Direction theDirection, final boolean isCorrect) {
        Room currentRoom = getCurrentRoom();
        if (isCorrect && theDirection == myPendingDirection) {
            currentRoom.getDoor(theDirection).open();

            myCurrentX += (theDirection == Direction.EAST ? 1 : (theDirection == Direction.WEST ? -1 : 0));
            myCurrentY += (theDirection == Direction.SOUTH ? 1 : (theDirection == Direction.NORTH ? -1 : 0));
            myQuestionPending = false;
            myPendingDirection = null;

             Room newRoom = getCurrentRoom();
             newRoom.getDoor(theDirection.getOpposite()).open();

            System.out.println("Moving to: " + myCurrentX + ", " + myCurrentY);

            mySupport.firePropertyChange("move", null,
                                            new MoveEvent(newRoom, theDirection, myCurrentX, myCurrentY));
            mySupport.firePropertyChange("correct answer", null, getCurrentRoom());

            if (isExit(myCurrentX, myCurrentY)) {
                mySupport.firePropertyChange("game over", null, true);
            }
        } else {
            currentRoom.getDoor(theDirection).close();
            myQuestionPending = false;
            myPendingDirection = null;
            mySupport.firePropertyChange("wrong answer", null, currentRoom);

            if (isGameOver()) {
                mySupport.firePropertyChange("game over", null, false);
            }
        }
    }

    /**
     * Checks if there is a question that needs to be answered
     * @return True if there is a question waiting to be answered. False otherwise.
     */
    public boolean isQuestionPending() {
        return myQuestionPending;
    }

    /**
     * Moves the player in the specified direction, if possible.
     *
     * @param theDirection The direction to move ("NORTH", "SOUTH", "EAST", "WEST").
     */
    public void move(final Direction theDirection) {
        System.out.println("Maze move method called");
        if (canMove(theDirection)) {
            Room currentRoom = getCurrentRoom();
            System.out.println("Can move in direction: " + theDirection);

            if (isValidMove(myCurrentX, myCurrentY)) {
                System.out.println("Setting up question for direction: " + theDirection);
                myQuestionPending = true;
                myPendingDirection = theDirection;
                mySupport.firePropertyChange("question", null,
                                                new QuestionEvent(currentRoom.getTrivia(), theDirection));
            }
        } else {
            System.out.println("Cannot move in direction: " + theDirection);
        }
    }

    /**
     * Checks if the player can move in the given direction in the maze
     * @param theDirection - The direction the player is heading
     * @return True if the player can move in that direction, false otherwise
     */
    public boolean canMove(Direction theDirection) {
        int newX = myCurrentX;
        int newY = myCurrentY;

        switch (theDirection) {
            case NORTH:
                newY--;
                break;
            case SOUTH:
                newY++;
                break;
            case EAST:
                newX++;
                break;
            case WEST:
                newX--;
                break;
        }


        if (newX < 0 || newX >= MAZE_SIZE || newY < 0 || newY >= MAZE_SIZE) {
            System.out.println("Move is out of bounds");
            return false;
        }


        Room currentRoom = myMap[myCurrentY][myCurrentX];
        boolean isDoorOpen = currentRoom.isDoorOpen(theDirection);
        boolean isIncorrectlyAnswered = currentRoom.hasBeenAnsweredIncorrectly(theDirection);



        return isDoorOpen && !isIncorrectlyAnswered;

    }

    /**
     * Retrieves the current room where the player is located.
     *
     * @return The current Room object.
     */
    public Room getCurrentRoom() {
        return getRoom(myCurrentX, myCurrentY);
    }

    /**
     * Gets the x coordinate
     * @return The x coordinate
     */
    public int getCurrentX() {
        return myCurrentX;
    }

    /**
     * Gets the y coordinate
     * @return The y coordinate.
     */
    public int getCurrentY() {
        return myCurrentY;
    }

    /**
     * Checks if a given move is valid.
     * @param theX - The x coordinate of the player.
     * @param theY - The y coordinate of the player
     * @return True if it's a valid move. False otherwise.
     */
    private boolean isValidMove(int theX, int theY) {
        return theX >= 0 && theX < MAZE_SIZE &&
                theY >= 0 && theY < MAZE_SIZE || isExit(theX, theY);
    }

    /**
     * Gets the size of the maze.
     *
     * @return The size of the maze.
     */
    public int getRoomSize() {
        return MAZE_SIZE;
    }

    /**
    * Gets the instance of the Trivia class.
    * @return the instance of the trivia class.
    */
    public Trivia getTrivia() {
        return myTrivia;
    }


    /**
     * Reinitializes the database connector
     * @param theDbConnector - the database connector
     */
    public void reinitializeDatabaseConnector(final DatabaseConnector theDbConnector) {
        try {
            this.myDBConn = theDbConnector;
            this.myQesGen = new QuestionGenerator(theDbConnector);
            this.questionFactoryProvider = new QuestionFactoryProvider(theDbConnector);

            for (int i = 0; i < MAZE_SIZE; i++) {
                for (int j = 0; j < MAZE_SIZE; j++) {
                    if (myMap[i][j].getTrivia() == null) {
                        myMap[i][j].setTrivia(myQesGen.getRandomQes());
                    }
                }
            }
            System.out.println("Maze database connector reinitialized successfully");
        } catch (Exception e) {
            System.err.println("Error reinitializing Maze database connector: "
                                + e.getMessage());
        }
    }

    /**
     * Checks if the game is over through backtracking.
     * @return True if the game is over. False otherwise.
     */
    public boolean isGameOver() {
        return !hasPathToExit(myCurrentX, myCurrentY, new boolean[MAZE_SIZE][MAZE_SIZE]);
    }

    /**
     * Maze backtracking to determine if the player can reach the exit.
     * @param theX - The x coordinate
     * @param theY - The y coordinate
     * @param theVisited - A boolean array representing what places the maze visited
     * @return True if there is a path to the exit. False otherwise
     */
    private boolean hasPathToExit(final int theX, final int theY, final boolean[][] theVisited) {
        if (theX == MAZE_SIZE - 1 && theY == MAZE_SIZE - 1) {
            return true; // Reached the exit
        }
        if (theX < 0 || theX >= MAZE_SIZE || theY < 0 || theY >= MAZE_SIZE || theVisited[theX][theY]) {
            return false;
        }

        theVisited[theX][theY] = true;

        for (Direction dir : Direction.values()) {
            if (myMap[theX][theY].isDoorOpen(dir)) {
                int newX = theX + (dir == Direction.EAST ? 1 : (dir == Direction.WEST ? -1 : 0));
                int newY = theY + (dir == Direction.SOUTH ? 1 : (dir == Direction.NORTH ? -1 : 0));
                if (hasPathToExit(newX, newY, theVisited)) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Checks if the player is on the exit coordinate
     * @param theX - The x coordinate
     * @param theY - The y coordinate
     * @return True if at exit coordinate, false otherwise
     */
    private boolean isExit(final int theX, final int theY) {
        return theX == MAZE_SIZE - 1 && theY == MAZE_SIZE - 1;
    }

    /**
     * Checks if the player is next to the exit.
     * @param theDirection - The direction to search at.
     * @return True if next to the exit. False otherwise.
     */
    public boolean isAdjacentToExit(final Direction theDirection) {
        int newX = myCurrentX + (theDirection == Direction.EAST ? 1 : (theDirection == Direction.WEST ? -1 : 0));
        int newY = myCurrentY + (theDirection == Direction.SOUTH ? 1 : (theDirection == Direction.NORTH ? -1 : 0));
        return isExit(newX, newY);
    }

    /**
     * Writes the state of the maze.
     * @param theOut - The state of the maze
     * @throws IOException When it cannot write the state of the maze.
     */
    @Serial
    private void writeObject(final ObjectOutputStream theOut) throws IOException {
        theOut.defaultWriteObject();
    }


    /**
     * Writes the state of the maze.
     * @param theIn - The state of the maze
     * @throws IOException When it cannot write the state of the maze.
     * @throws ClassNotFoundException When it cannot find the class
     */
    @Serial
    private void readObject(ObjectInputStream theIn) throws IOException,
                            ClassNotFoundException {
        theIn.defaultReadObject();
        mySupport = new PropertyChangeSupport(this);
    }

}
