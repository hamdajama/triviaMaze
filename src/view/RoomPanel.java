/**
 * TCSS 360 - Trivia Maze
 * RoomPanel.jave
 */
package view;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;


import javax.swing.JPanel;

import java.util.EnumMap;
import java.util.Map;

import model.Direction;
import model.DoorState;
import model.Maze;
import model.MoveEvent;
import model.Room;

public class RoomPanel extends JPanel implements PropertyChangeListener, Serializable {

    /**
     * Serial for the RoomPanel
     */
    @Serial
    private static final long serialVersionUID = 3L;


    /**
     * DoorStates for a door in a given direction
     */
    private final Map<Direction, DoorState> myDoorStates;

    /**
     * Player position x coordinate
     */
    private int myPlayerX;

    /**
     * Player position y coordinate
     */
    private int myPlayerY;

    /**
     * Maze for the game
     */
    private final Maze myMaze;

    /**
     * Images for the character sprite
     */
    private final transient Map<String, BufferedImage[]> myImages;

    /**
     * The direction the player is heading
     */
    private String myDirection;

    /**
     * Frame index for the character sprite
     */
    private int myFrameIndex;

    /**
     * Creates the panel for the room.
     * @param theMaze - The maze for the game.
     * @param theFrameIndex - The frame index of the character sprite
     * @param theImages - The images of the character sprite
     * @param theDirection - The direction the player is heading.
     */
    public RoomPanel(final Maze theMaze, final int theFrameIndex,
                     final Map<String, BufferedImage[]> theImages,
                     final String theDirection) {
        super();
        myDoorStates = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            if (dir.equals(Direction.NORTH) || dir.equals(Direction.WEST)) {
                myDoorStates.put(dir, DoorState.CLOSED);
            } else {
                myDoorStates.put(dir, DoorState.OPEN);
            }

        }
        myPlayerX = 0;
        myPlayerY = 0;
        myMaze = theMaze;
        myImages = theImages;
        myDirection = theDirection;
        myFrameIndex = theFrameIndex;
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
        theGraphics2D.drawRect(roomSize, roomSize, width - 2 * roomSize,
                        height - 2 * roomSize);

        drawDoor(theGraphics2D, Direction.NORTH, width / 2 - roomSize / 2, 10);
        drawDoor(theGraphics2D, Direction.EAST, width - roomSize, height / 2 - roomSize / 2);
        drawDoor(theGraphics2D, Direction.SOUTH, width / 2 - roomSize / 2, height - roomSize);
        drawDoor(theGraphics2D, Direction.WEST, 10, height / 2 - roomSize / 2);

        drawText(theGraphics2D, width, height);

//        printDoorStates();
    }

    /**
     * Draws the door for the room.
     * @param theG - The graphics of the game
     * @param theDirection - The direction of the door.
     * @param theX - The x position to draw the door.
     * @param theY - The y position to draw the door.
     */
    private void drawDoor(final Graphics2D theG, final Direction theDirection, final int theX,
                          final int theY) {
        switch (myDoorStates.get(theDirection)) {
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
        theG.fillRect(theX, theY, 30, 30);
    }

    /**
     * Draws the text for the game.
     * @param theGraphics2D - The graphics
     * @param theWidth - The width of the frame
     * @param theHeight - The height of the frame.
     */
    private void drawText(final Graphics2D theGraphics2D, final int theWidth, final int theHeight) {
        BufferedImage[] images = myImages.get(myDirection.toUpperCase());
        BufferedImage currentImage = images[myFrameIndex];

        theGraphics2D.drawImage(currentImage, (theWidth / 2) - 20, (theHeight / 2) - 20,
                          40, 40, this);

        theGraphics2D.setColor(Color.WHITE);
        theGraphics2D.setFont(new Font("Verdana", Font.BOLD, 10));

        theGraphics2D.drawString("Move North", (theWidth / 2) - 40, 50);
        theGraphics2D.drawString("Move East", theWidth - 100, theHeight / 2);
        theGraphics2D.drawString("Move South", (theWidth / 2) - 40, 250);
        theGraphics2D.drawString("Move West", theWidth - 350, theHeight / 2);

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
        for (Direction dir : Direction.values()) {

            boolean isOpen = theRoom.isDoorOpen(dir);
            boolean isEdge = isEdge(dir);
            boolean isExit = myMaze.isAdjacentToExit(dir);
            boolean isIncorrect = theRoom.hasBeenAnsweredIncorrectly(dir);

            System.out.println(dir + " door - Open: " + isOpen + ", Edge: " + isEdge +
                    ", Exit: " + isExit + ", Incorrect: " + isIncorrect);


            if (myMaze.isAdjacentToExit(dir)) {
                myDoorStates.put(dir, DoorState.EXIT);
            } else if (isEdge(dir)) {
                myDoorStates.put(dir, DoorState.CLOSED);
            } else if (theRoom.hasBeenAnsweredIncorrectly(dir)) {
                myDoorStates.put(dir, DoorState.CLOSED);
            } else if (theRoom.isDoorOpen(dir)) {
                myDoorStates.put(dir, DoorState.OPEN);
            } else {
                myDoorStates.put(dir, DoorState.CLOSED);
            }
            System.out.println("Door state for " + dir + ": " + myDoorStates.get(dir));
        }
        repaint();
    }

    /**
     * Checks if the player is at the edge of the maze.
     * @param theDirection - The direction of the doors.
     * @return - True if the door is on the edge of the maze or false otherwise.
     */
    private boolean isEdge(final Direction theDirection) {
        return  (myPlayerY == 0 && theDirection == Direction.NORTH) ||
                (myPlayerY == myMaze.getMazeSize() - 1 && theDirection == Direction.SOUTH) ||
                (myPlayerX == 0 && theDirection == Direction.WEST) ||
                (myPlayerX == myMaze.getMazeSize() - 1 && theDirection == Direction.EAST);
    }

    /**
     * Updates the characterSprite in a given direction
     * @param theDirection - The new direction the player is heading
     * @param theFrameIndex - The frame index of the character sprite.
     */
    public void updateDirectionAndFrame(final String theDirection,
                                        final int theFrameIndex) {
        myDirection = theDirection.toUpperCase();
        myFrameIndex = theFrameIndex;
        repaint();
    }

    /**
     * Updates the characterSprite
     * @param theFrameIndex - The frame index of a character sprite
     */
    public void updateFrame(final int theFrameIndex) {
        myFrameIndex = theFrameIndex;
        repaint();
    }

    /**
     * Property change for handling the move event.
     * @param theEvt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvt) {
        if (theEvt.getPropertyName().equals("move")) {
            MoveEvent moveEvent = (MoveEvent) theEvt.getNewValue();
            updateRoomPanel(moveEvent.getRoom(), moveEvent.getX(), moveEvent.getY());
        }
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
    private void readObject(final ObjectInputStream theIn) throws IOException,
                            ClassNotFoundException {
        theIn.defaultReadObject();
    }

}