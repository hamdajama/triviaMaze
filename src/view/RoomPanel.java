package view;

import model.Maze;
import model.Room;
import model.Direction;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serial;
import java.io.Serializable;
import javax.swing.JPanel;
import java.util.EnumMap;
import java.util.Map;

public class RoomPanel extends JPanel implements PropertyChangeListener, Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    private enum DoorState {
        CLOSED, OPEN, EXIT
    }

    private final Map<Direction, DoorState> doorStates;

    private int myPlayerX;
    private int myPlayerY;
    private final Maze myMaze;

    /**
     * Creates the panel for the room.
     * @param theMaze - The maze for the game.
     */
    public RoomPanel(Maze theMaze) {
        super();
        doorStates = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            if (dir.equals(Direction.NORTH) || dir.equals(Direction.WEST)) {
                doorStates.put(dir, DoorState.CLOSED);
            } else {
                doorStates.put(dir, DoorState.OPEN);
            }

        }
        myPlayerX = 0;
        myPlayerY = 0;
        myMaze = theMaze;
    }


    /**
     * Paints the room panel.
     * @param theG - The graphics for the game.
     */
    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG);
        final Graphics2D graphics2D = (Graphics2D) theG;
        drawRoom(graphics2D);
    }

    /**
     * Draws the room for the player
     * @param theGraphics2D - The graphics.
     */
    private void drawRoom(final Graphics2D theGraphics2D) {
        final int width = getWidth();
        final int height = getHeight();
        final int roomSize = 40;

        theGraphics2D.setColor(Color.WHITE);
        theGraphics2D.drawRect(roomSize, roomSize, width - 2 * roomSize, height - 2 * roomSize);

        final int doorSize = 30;

        drawDoor(theGraphics2D, Direction.NORTH, width / 2 - roomSize / 2, 10, doorSize, doorSize);
        drawDoor(theGraphics2D, Direction.EAST, width - roomSize, height / 2 - roomSize / 2, doorSize, doorSize);
        drawDoor(theGraphics2D, Direction.SOUTH, width / 2 - roomSize / 2, height - roomSize, doorSize, doorSize);
        drawDoor(theGraphics2D, Direction.WEST, 10, height / 2 - roomSize / 2, doorSize, doorSize);

        drawText(theGraphics2D, width, height);

//        printDoorStates();
    }

    /**
     * Draws the door for the room.
     * @param theG - The graphics of the game
     * @param theDirection - The direction of the door.
     * @param theX - The x position to draw the door.
     * @param theY - The y position to draw the door.
     * @param theWidth - The width of the text
     * @param theHeight - The height of the text
     */
    private void drawDoor(final Graphics2D theG, final Direction theDirection, final int theX,
                          final int theY, final int theWidth, final int theHeight) {
        switch (doorStates.get(theDirection)) {
            case CLOSED:
                theG.setColor(Color.RED);
                break;
            case OPEN:
                theG.setColor(Color.GREEN);
                break;
            case EXIT:
                theG.setColor(Color.BLUE);
                break;
        }
        theG.fillRect(theX, theY, theWidth, theHeight);
    }

    /**
     * Draws the text for the game.
     * @param theGraphics2D - The graphics
     * @param theWidth - The width of the frame
     * @param theHeight - The height of the frame.
     */
    private void drawText(final Graphics2D theGraphics2D, final int theWidth, final int theHeight) {
        theGraphics2D.setColor(Color.WHITE);
        theGraphics2D.setFont(new Font("Verdana", Font.BOLD, 10));

        theGraphics2D.drawString("Move North", (theWidth / 2) - 40, 50);
        theGraphics2D.drawString("Move East", theWidth - 100, theHeight / 2);
        theGraphics2D.drawString("Move South", (theWidth / 2) - 40, 250);
        theGraphics2D.drawString("Move West", theWidth - 350, theHeight / 2);

        theGraphics2D.setFont(new Font("Verdana", Font.BOLD, 30));
        theGraphics2D.drawString("1", (theWidth/2) - 15, theHeight/2);
    }

    /**
     * Updates the room panel for the game.
     * @param theRoom - The room the player is in.
     * @param theX - The x coordinate.
     * @param theY - The y coordinate.
     */
    public void updateRoomPanel(final Room theRoom, final int theX, final int theY) {
        myPlayerX = theX;
        myPlayerY = theY;
        System.out.println("Updating RoomPanel for position (" + theX + ", " + theY + ")");
        theRoom.debugPrintDoors();
        for (Direction dir : Direction.values()) {

            boolean isOpen = theRoom.isDoorOpen(dir);
            boolean isEdge = isEdge(dir);
            boolean isExit = myMaze.isAdjacentToExit(dir);
            boolean isIncorrect = theRoom.hasBeenAnsweredIncorrectly(dir);

            System.out.println(dir + " door - Open: " + isOpen + ", Edge: " + isEdge +
                    ", Exit: " + isExit + ", Incorrect: " + isIncorrect);


            if (myMaze.isAdjacentToExit(dir)) {
                doorStates.put(dir, DoorState.EXIT);
            } else if (isEdge(dir)) {
                doorStates.put(dir, DoorState.CLOSED);
            } else if (theRoom.hasBeenAnsweredIncorrectly(dir)) {
                doorStates.put(dir, DoorState.CLOSED);
            } else if (theRoom.isDoorOpen(dir)) {
                doorStates.put(dir, DoorState.OPEN);
            } else {
                doorStates.put(dir, DoorState.CLOSED);
            }
            System.out.println("Door state for " + dir + ": " + doorStates.get(dir));
        }
        repaint();
    }

    /**
     * Checks if the player is at the edge of the maze.
     * @param theDirection - The direction of the doors.
     * @return - True if the door is on the edge of the maze or false otherwise.
     */
    private boolean isEdge(Direction theDirection) {
        return (myPlayerY == 0 && theDirection == Direction.NORTH) ||
                (myPlayerY == myMaze.getMazeSize() - 1 && theDirection == Direction.SOUTH) ||
                (myPlayerX == 0 && theDirection == Direction.WEST) ||
                (myPlayerX == myMaze.getMazeSize() - 1 && theDirection == Direction.EAST);
    }

    /**
     * Helper method that prints where the player is at and what state each dooe is in.
     */
    private void printDoorStates() {
        System.out.println("Door states at position (" + myPlayerX + ", " + myPlayerY + "):");
        for (Direction dir : Direction.values()) {
            System.out.println(dir + ": " + doorStates.get(dir));
        }
    }

    /**
     * Property change for handling the move event.
     * @param theEvt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvt) {
        if (theEvt.getPropertyName().equals("move")) {
            Maze.MoveEvent moveEvent = (Maze.MoveEvent) theEvt.getNewValue();
            updateRoomPanel(moveEvent.getRoom(), moveEvent.getX(), moveEvent.getY());
        }
    }
}