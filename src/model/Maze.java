package model;

import javax.xml.crypto.Data;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Random;

/**
 * Maze class represents a 5x5 grid of rooms in the TriviaMaze game.
 * It handles the setup of rooms with questions, player movement, and game state events.
 *
 * @version 8/7/2024
 */
public class Maze implements Serializable {
    private static final long serialVersionUID = 1l;
    private static final int MAZE_SIZE = 5;
    private Room[][] myMap;
    private int myEndX, myEndY;
    private int myCurrentX, myCurrentY;
    private boolean movementAllowed;
    private PropertyChangeSupport mySupport;
    private transient DatabaseConnector myDBConn;
    private QuestionGenerator myQesGen;

    /**
     * Constructs a new Maze, initializing the game grid and questions.
     *
     * @param theDBConn The DatabaseConnector object for accessing the question database.
     * @throws SQLException If an error occurs during database access.
     */
    public Maze(DatabaseConnector theDBConn) throws SQLException {
        this.myDBConn = theDBConn;
        this.myQesGen = new QuestionGenerator(theDBConn);
        this.mySupport = new PropertyChangeSupport(this);
        this.myMap = new Room[MAZE_SIZE][MAZE_SIZE];
        buildMap();
        setEnd();
        myCurrentX = 0;
        myCurrentY = 0;
        movementAllowed = false; // Initially, movement is not allowed until the first question is answered correctly
    }

    /**
     * Builds the map with rooms, each containing a randomly assigned question.
     *
     * @throws SQLException If an error occurs during question retrieval from the database.
     */
    private void buildMap() throws SQLException {
        for (int i = 0; i < MAZE_SIZE; i++) {
            for (int j = 0; j < MAZE_SIZE; j++) {
                Question question = myQesGen.getRandomQes();
                myMap[i][j] = new Room(question);
            }
        }
    }

    /**
     * Adds a PropertyChangeListener to listen for property changes in the game state.
     *
     * @param theListener The PropertyChangeListener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener theListener) {
        mySupport.addPropertyChangeListener(theListener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     *
     * @param theListener The PropertyChangeListener to remove.
     */
    public void removePropertyChangeListener(PropertyChangeListener theListener) {
        mySupport.removePropertyChangeListener(theListener);
    }

    /**
     * Sets the end point of the maze to the bottom-right corner.
     */
    private void setEnd() {
        myEndX = MAZE_SIZE - 1;
        myEndY = MAZE_SIZE - 1;
    }

    /**
     * Retrieves the room at the specified coordinates.
     *
     * @param theX The X-coordinate of the room.
     * @param theY The Y-coordinate of the room.
     * @return The Room object at the specified coordinates, or null if out of bounds.
     */
    public Room getRoom(int theX, int theY) {
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
     * Processes the player's answer and updates the game state accordingly.
     *
     * @param answerType The type of answer provided by the player ("correct answer" or "wrong answer").
     */
    public void processAnswer(String answerType) {
        Room currentRoom = getCurrentRoom();
        if ("correct answer".equals(answerType)) {
            currentRoom.setAnswered(true);
            movementAllowed = true;
            for (Door door : currentRoom.getDoors().values()) {
                door.open();
            }
            mySupport.firePropertyChange("correct answer", null, currentRoom);
        } else {
            movementAllowed = false;
            currentRoom.wrongAnswer(answerType);
            checkGameOver();
            mySupport.firePropertyChange("wrong answer", null, currentRoom);

        }
    }

    /**
     * Checks if the player has reached the end of the maze.
     *
     * @return True if the player is at the end of the maze, false otherwise.
     */
    public boolean isAtEnd() {
        return myCurrentX == myEndX && myCurrentY == myEndY;
    }

    /**
     * Moves the player in the specified direction, if possible.
     *
     * @param theDirection The direction to move ("NORTH", "SOUTH", "EAST", "WEST").
     */
    public void move(String theDirection) {
        int newX = myCurrentX, newY = myCurrentY;
        switch (theDirection.toUpperCase()) {
            case "NORTH":
                newY--;
                break;
            case "SOUTH":
                newY++;
                break;
            case "EAST":
                newX++;
                break;
            case "WEST":
                newX--;
                break;
            default:
                return;
        }

        if (newX >= 0 && newX < MAZE_SIZE && newY >= 0 && newY < MAZE_SIZE) {
            myCurrentX = newX;
            myCurrentY = newY;
            movementAllowed = false;
            mySupport.firePropertyChange("move", null, getCurrentRoom());
        }
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
     * Gets the size of the maze.
     *
     * @return The size of the maze.
     */
    public int getRoomSize() {
        return MAZE_SIZE;
    }

    /**
     * Checks if the player movement is allowed.
     *
     * @return True if movement is allowed, false otherwise.
     */
    public boolean isMovementAllowed() {
        return movementAllowed;
    }

    public void reinitializeDatabaseConnector(final DatabaseConnector theDbConnector) {
        this.myDBConn = theDbConnector;
        this.myQesGen = new QuestionGenerator(theDbConnector);
    }

    /**
     * Checks if the player can't reach the exit.
     */
    public void checkGameOver() {
        boolean[][] visited = new boolean[MAZE_SIZE][MAZE_SIZE];
        if (!canReachExit(myCurrentX, myCurrentY, visited)) {
            System.out.println("Game is over");
            mySupport.firePropertyChange("game over", null, getCurrentRoom());
        }
    }

    /**
     * Checks if the player can reach the exit.
     * @param theXCoordinate - Current x coordinate the player is in.
     * @param theYCoordinate - Current y coordinate the player is in.
     * @param theVisitedRoom - A true/false value that determines if the player can still win the game.
     * @return True if the player can still reach the exit. False otherwise.
     */
    private boolean canReachExit(int theXCoordinate, int theYCoordinate, boolean[][] theVisitedRoom) {
        if (theXCoordinate < 0 || theXCoordinate >= MAZE_SIZE || theYCoordinate < 0
                || theYCoordinate >= MAZE_SIZE || theVisitedRoom[theXCoordinate][theYCoordinate]) {
            return false;
        }

        theVisitedRoom[theXCoordinate][theYCoordinate] = true;

        String[] directions = {"North", "South", "West", "East"};
        int[] xdirection = {-1, 1, 0, 0};
        int[] ydirection = {0, 0, -1, 1};

        boolean nearbyDoors = false;

        for (int i = 0; i < 4; i++) {
            int newX = theXCoordinate + xdirection[i];
            int newY = theYCoordinate + ydirection[i];

            Room currentRoom = myMap[theXCoordinate][theYCoordinate];
            Door connectingDoor = currentRoom.getDoor(directions[i]);

            if (connectingDoor != null && !connectingDoor.isClosed()) {
                nearbyDoors = true;
                if (canReachExit(newX, newY, theVisitedRoom)) {
                    return true;
                }
            }
        }
        return nearbyDoors;
    }

}
