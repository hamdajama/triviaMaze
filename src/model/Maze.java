package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;

/**
 * Maze class represents a 5x5 grid of rooms in the TriviaMaze game.
 * It handles the setup of rooms with questions, player movement, and game state events.
 * @author Hamda Jama
 */
public class Maze {
    private int mySize = 5;
    private Room[][] myMap;
    private int myEndX, myEndY;
    private int myCurrentX, myCurrentY;
    private PropertyChangeSupport mySupport;
    private DatabaseConnector myDBConn;
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
        this.myMap = new Room[mySize][mySize];
        buildMap();
        setEnd();
        myCurrentX = 0;
        myCurrentY = 0;
    }

    /**
     * Builds the map with rooms, each containing a randomly assigned question.
     *
     * @throws SQLException If an error occurs during question retrieval from the database.
     */
    private void buildMap() throws SQLException {
        for (int i = 0; i < mySize; i++) {
            for (int j = 0; j < mySize; j++) {
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
        myEndX = mySize - 1;
        myEndY = mySize - 1;
    }

    /**
     * Retrieves the room at the specified coordinates.
     *
     * @param theX The X-coordinate of the room.
     * @param theY The Y-coordinate of the room.
     * @return The Room object at the specified coordinates, or null if out of bounds.
     */
    public Room getRoom(int theX, int theY) {
        if (theX >= 0 && theX < mySize && theY >= 0 && theY < mySize) {
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
     * @param theAnswer The answer provided by the player.
     */
    public void processAnswer(String theAnswer) {
        Room currentRoom = getCurrentRoom();
        if (!currentRoom.answerQuestion(theAnswer)) {
            currentRoom.wrongAnswer(theAnswer);
            if (currentRoom.allClosed()) {
                mySupport.firePropertyChange("game over", null, getCurrentRoom());
            }
        } else {
            mySupport.firePropertyChange("correct answer", null, getCurrentRoom());
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
                newX--;
                break;
            case "SOUTH":
                newX++;
                break;
            case "EAST":
                newY++;
                break;
            case "WEST":
                newY--;
                break;
            default:
                return;
        }

        if (newX >= 0 && newX < mySize && newY >= 0 && newY < mySize) {
            myCurrentX = newX;
            myCurrentY = newY;
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
     * Gets how big the maze will be.
     * @return An int representing the n x n for the size of the maze
     */
    public int getRoomSize() {
        return mySize;
    }

}
