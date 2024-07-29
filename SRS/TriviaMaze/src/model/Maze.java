package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;

public class Maze {
    private int mySize = 5;
    private Room[][] myMap;
    private int myEndX, myEndY;
    private PropertyChangeSupport mySupport;
    private DatabaseConnector myDBConn;
    private QuestionGenerator myQesGen;

    public Maze(DatabaseConnector theDBConn ) throws SQLException {
        this.myDBConn = theDBConn;
       this.myQesGen = new QuestionGenerator(theDBConn);
        this.mySupport = new PropertyChangeSupport(this);
        this.myMap = new Room[mySize][mySize];
        buildMap();
        setEnd();
    }
    public void buildMap() throws SQLException {
        for (int i = 0; i < mySize; i++) {
            for (int j = 0; j < mySize; j++) {
                Question question = myQesGen.getRandomQes();
                myMap[i][j] = new Room(question);
            }
        }
    }
    public void addPropertyChangeListener(PropertyChangeListener theListener) {
        mySupport.addPropertyChangeListener(theListener);
    }
    public void removePropertyChangeListener(PropertyChangeListener theListener) {
        mySupport.removePropertyChangeListener(theListener);
    }
    private void setEnd( ){
        myEndX = mySize - 1;
        myEndY = mySize - 1;
    }
    public Room getRoom(int theX, int theY) {
        int x = theX, y = theX
        if (x >= 0 && x < mySize && y >= 0 && y < mySize) {
            return myMap[x][y];
        }
        return null;
    }
    public void startGame(Player player) {
        player.setCurrentRoom(myMap[0][0]);
        mySupport.firePropertyChange("start game", null, player);
    }

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
