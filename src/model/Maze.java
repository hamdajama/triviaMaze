package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
/**
 * Maze class represents a 5x5 grid of rooms in the TriviaMaze game.
 * It handles the setup of rooms with questions, player movement, and game state events.
 */
public class Maze {
    private int mySize = 5;
    private Room[][] myMap;
    private int myEndX, myEndY;
    private PropertyChangeSupport mySupport;
    private DatabaseConnector myDBConn;
    private QuestionGenerator myQesGen;
/**
     * Constructs a new Maze, initializing the game grid and questions.
     *
     * @param theDBConn The DatabaseConnector object for accessing the question database.
     * @throws SQLException If an error occurs during database access.
     */
    public Maze(DatabaseConnector theDBConn ) throws SQLException {
        this.myDBConn = theDBConn;
       this.myQesGen = new QuestionGenerator(theDBConn);
        this.mySupport = new PropertyChangeSupport(this);
        this.myMap = new Room[mySize][mySize];
        buildMap();
        setEnd();
    }
    /**
     * Builds the map with rooms, each containing a randomly assigned question.
     *
     * @throws SQLException If an error occurs during question retrieval from the database.
     */
    public void buildMap() throws SQLException {
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
    **
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
    private void setEnd( ){
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
        int x = theX, y = theX
        if (x >= 0 && x < mySize && y >= 0 && y < mySize) {
            return myMap[x][y];
        }
        return null;
    }
    /**
     * Starts the game by placing the player at the starting position.
     *
     * @param player The Player object.
     */
    public void startGame(Player player) {
        player.setCurrentRoom(myMap[0][0]);
        mySupport.firePropertyChange("start game", null, player);
    }
    /**
     * Processes the player's answer and updates the game state accordingly.
     *
     * @param thePlayer The Player object.
     * @param theAnswer The answer provided by the player.
     */
    public void winOrLose (Player thePlayer, String TheAnswer) {
        Room currentRoom = thePlayer.getCurrentRoom();
        if (!currentRoom.answerQuestion(theAnswer)) {
            currentRoom.wrongAnswer(theAnswer);
            if (currentRoom.allClosed()) {
                mySupport.firePropertyChange("gamer over", null, thePlayer);
            }
        } else {
            mySupport.firePropertyChange("correct answer", null, thePlayer);
        }
    }
    /**
     * Checks if the player has reached the end of the maze.
     *
     * @param thePlayer The Player object.
     * @return True if the player is at the end of the maze, false otherwise.
     */
    private boolean isPlayerAtEnd(Player thePlayer) {
        Player player = thePlayer
        Room currentRoom = player.getCurrentRoom();
        return currentRoom.getCoordinates()[0] == myEndX && currentRoom.getCoordinates()[1] == myEndY;
    }
    /**
     * Determines the next room based on the player's current position and the direction moved.
     *
     * @param player The player object.
     * @param direction The direction in which the player wishes to move (e.g., "North", "South").
     * @return The next Room object, or null if the move is invalid.
     */
    public Room getNextRoom(Player thePlayer, String theDirection) {
        Player player  =thePlayer;
        String direction =  theDirection;
        int currentX = player.getCurrentRoomX();
        int currentY = player.getCurrentRoomY();
        int newX = currentX, newY = currentY;

        switch (direction.toUpperCase()) {
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
                return null;
        }
        // Check if the new position is within bounds
        if (newX >= 0 && newX < SIZE && newY >= 0 && newY < SIZE) {
            return map[newX][newY];
        }
        return null; // Out of bounds
    }

}
