package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.*;

public class Room implements Serializable {
    private static final long serialVersionUID = 2L;
    private Map<Direction, Door> myDoors;
    private Map<Direction, Room> myAdjacentRooms;
    private Question myTrivia;
    private final PropertyChangeSupport myPcs = new PropertyChangeSupport(this);
    private boolean isAnswered;

    public Room(Question theTrivia) {
        this.myTrivia = theTrivia;
        myDoors = new EnumMap<>(Direction.class);
        myAdjacentRooms = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            myDoors.put(dir, new Door());
        }
        isAnswered = false;
    }

    public void setAdjacentRoom(Direction direction, Room room) {
        myAdjacentRooms.put(direction, room);
    }

    public Door getDoor(Direction direction) {
        return myDoors.get(direction);
    }

    public Map<Direction, Door> getDoors() {
        return myDoors;
    }

    public Question getTrivia() {
        return myTrivia;
    }

    public boolean answerQuestion(String theAnswer) {
        return myTrivia.isMatch(theAnswer);
    }

    public void processDoorState(Direction direction, boolean isCorrect) {
        Door door = myDoors.get(direction);
        if (isCorrect) {
            door.open();
            Room adjacentRoom = myAdjacentRooms.get(direction);
            if (adjacentRoom != null) {
                adjacentRoom.getDoor(direction.getOpposite()).open();
            }
        } else {
            door.close();
            Room adjacentRoom = myAdjacentRooms.get(direction);
            if (adjacentRoom != null) {
                adjacentRoom.getDoor(direction.getOpposite()).close();
            }
        }
        myPcs.firePropertyChange(direction.name(), !isCorrect, isCorrect);
    }

    public boolean allClosed() {
        return myDoors.values().stream().allMatch(Door::isClosed);
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    // PropertyChangeListener methods remain the same
}