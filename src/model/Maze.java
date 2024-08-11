package model;

import view.GUI;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;
import java.io.Serializable;

/**
 * Maze class represents a 5x5 grid of rooms in the TriviaMaze game.
 * It handles the setup of rooms with questions, player movement, and game state events.
 *
 * @version 8/7/2024
 */
public class Maze implements Serializable {
    private static final long serialVersionUID = 1l;
    private static final int mySize = 5;
    private Room[][] myMap;
    private int myEndX, myEndY;
    private int myCurrentX, myCurrentY;
    private boolean myMovementAllowed;
    private PropertyChangeSupport mySupport;
    private DatabaseConnector myDBConn;
    private QuestionGenerator myQesGen;
    private Trivia myTrivia;
    private GUI myGui;

    /**
     * Constructs a new Maze, initializing the game grid and questions.
     *
     * @param theDBConn The DatabaseConnector object for accessing the question database.
     * @throws SQLException If an error occurs during database access.
     */
    public Maze(DatabaseConnector theDBConn) throws SQLException {
        this.myDBConn = theDBConn;
        this.myQesGen = new QuestionGenerator(myDBConn);
        this.mySupport = new PropertyChangeSupport(this);
        this.myMap = new Room[mySize][mySize];
        map();
        setEnd();
        myCurrentX = 0;
        myCurrentY = 0;
        //this.movementAllowed = false; // Initially, movement is not allowed until the first question is answered correctly
        myTrivia = new Trivia("Player");
        myTrivia.startTimer();
    }

    /**
     * Builds the map with rooms, each containing a randomly assigned question for each door.
     *
     * @throws SQLException If an error occurs during question retrieval from the database.
     */
    private void map() throws SQLException {
        for (int i = 0; i < mySize; i++) {
            for (int j = 0; j < mySize; j++) {
                Map<Direction,Question> questions = new EnumMap<>(Direction.class);
                for (Direction direction : Direction.values()) {
                    questions.put(direction, myQesGen.getRandomQes());
                }
                myMap[i][j] = new Room(questions);
            }
        }
        setAdjacent();
    }

    private void setAdjacent() {
        for (int i = 0 ; i < mySize; i++) {
            for (int j = 0; j <mySize; j++) {
                Room room  = myMap[i][j];
                if (i > 0 ) {
                    room.setAdjacentRooms(Direction.NORTH, myMap[i-1][j]);
                }
                if ( i < mySize - 1) {
                    room.setAdjacentRooms(Direction.SOUTH, myMap[i+1][j]);
                }
                if ( j > 0) {
                    room.setAdjacentRooms(Direction.WEST, myMap[i][j-1]);
                }
                if ( j < mySize - 1) {
                    room.setAdjacentRooms(Direction.EAST, myMap[i][j+1]);
                }
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
     * Processes the player's answer and updates the game state accordingly.
     *
     * @param theAnswer The type of answer provided by the player ("correct answer" or "wrong answer").
     * @param theDirection the direction the player intends to move to.
     */
    public void processAnswer(String theAnswer, Direction theDirection) {
        Room currentRoom = getCurrentRoom();
        Question quesDoor = currentRoom.getQuesDoor(theDirection);
        boolean isCorrect = myTrivia.isRightAnswer(theAnswer, quesDoor.getAnswer());
        if (isCorrect) {
            move(theDirection);
            setMovementAllowed(true);
            mySupport.firePropertyChange("correct answer", null, currentRoom);
            if(isAtEnd()) {
                gameOver(true);
            }
        } else {
            currentRoom.closeDoor(theDirection);
            Room adjacentRoom = getAdjacentRoom(currentRoom, theDirection);
            if (adjacentRoom != null) {
                Direction oppositeDirection = getOppositeDirection(theDirection);
                adjacentRoom.closeDoor(oppositeDirection);
            }
            setMovementAllowed(false);
            mySupport.firePropertyChange("wrong answer", null, currentRoom);
            if (currentRoom.allClosed() || isMazeBlocked()) {
                gameOver(false);
            }
        }
    }

    private Room getAdjacentRoom(Room currentRoom, Direction direction) {
        int newX = myCurrentX, newY = myCurrentY;
        switch (direction) {
            case NORTH: newY--; break;
            case SOUTH: newY++; break;
            case EAST: newX++; break;
            case WEST: newX--; break;
        }
        return getRoom(newX, newY);
    }

    private Direction getOppositeDirection(Direction direction) {
        switch (direction) {
            case NORTH: return Direction.SOUTH;
            case SOUTH: return Direction.NORTH;
            case EAST: return Direction.WEST;
            case WEST: return Direction.EAST;
            default: throw new IllegalArgumentException("Invalid direction");
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
    public void move(Direction theDirection) {

        int newX = myCurrentX, newY = myCurrentY;
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
            default:
                return;
        }

        if (newX >= 0 && newX < mySize && newY >= 0 && newY < mySize) {
            Room nextRoom = getRoom(newX,newY);
            if (!nextRoom.allClosed()) {
                myCurrentX = newX;
                myCurrentY = newY;
                mySupport.firePropertyChange("move", null, nextRoom);
            }
        }
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
        return mySize;
    }

    /**
     * sets whether the player movement is allowed.
     *
     * @param theMovement  True if movement is allowed, false otherwise.
     */
    public void setMovementAllowed(boolean theMovement) {
        this.myMovementAllowed = theMovement;
    }
    /**
     * Checks if the player movement is allowed.
     *
     * @return True if movement is allowed, false otherwise.
     */
    public boolean isMovementAllowed() {
        return myMovementAllowed;
    }

    public void reinitializeDatabaseConnector(final DatabaseConnector theDbConnector) {
        this.myDBConn = theDbConnector;
        this.myQesGen = new QuestionGenerator(theDbConnector);
    }
    /**
     * Checks if the maze is blocked (i.e., there is no way to reach the exit).
     *
     * @return True if the maze is blocked, false otherwise.
     */
    private boolean isMazeBlocked() {
        // Implement a pathfinding algorithm like DFS or BFS to check if the exit is reachable
        return false; // Placeholder: implement this based on your maze traversal logic
    }
    /**
     * Ends the game with a win or lose state.
     *
     * @param theWin True if the game is won, false if lost.
     */
    private void gameOver(boolean theWin) {
        myTrivia.stopTimer();
        mySupport.firePropertyChange(theWin ? "win" : "lose", null, null);
    }

    /**
     * Gets the trivia object for the game.
     *
     * @return The trivia object.
     */
    public Trivia getTrivia() {
        return myTrivia;
    }
}


