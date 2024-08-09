/**
 * TCSS 360 - Trivia Maze
 * RoomPanel.java
 */
package view;

import model.PlayerCharacter;
import model.Room;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.JPanel;

/**
 * This class creates the panel for the room and displays the door and what doors the player can go through
 * @author Eric John
 * @version 7/27/2024
 */
public class RoomPanel extends JPanel implements PropertyChangeListener, Serializable {
    private static final long serialVersionUID = 3L;

    /**
     * A boolean representing if the north door can be opened.
     */
    private boolean myNorthDoor;

    /**
     * A boolean representing if the south door can be opened.
     */
    private boolean mySouthDoor;

    /**
     * A boolean representing if the east door can be opened.
     */
    private boolean myEastDoor;

    /**
     * A boolean representing if the west door can be opened.
     */
    private boolean myWestDoor;

    private PlayerCharacter myPlayerCharacter;
    private String myDirection;
    private int myFrameIndex;

    /**
     * Constructor for the room panel.
     */
    public RoomPanel(PlayerCharacter thePlayerCharacter, String theDirection, int theFrameIndex) {
        super();
        myNorthDoor = false;
        mySouthDoor = true;
        myEastDoor = true;
        myWestDoor = false;
        myPlayerCharacter = thePlayerCharacter;
        myDirection = theDirection;
        myFrameIndex = theFrameIndex;
    }


    /**
     * Paints the text and images on the screen.
     * @param theG - The graphics for the project
     */
    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG);
        final Graphics2D graphics2D = (Graphics2D) theG;
        drawRoom(graphics2D);
    }

    /**
     * Draws the room to display to the player. To start out, it should show that the right and bottom door is open.
     * @param theGraphics2D - The graphics for the project.
     */
    private void drawRoom(final Graphics2D theGraphics2D) {
        final int width = getWidth();
        final int height = getHeight();
        final int roomSize = 40;

        theGraphics2D.setColor(Color.WHITE);
        theGraphics2D.drawRect(roomSize, roomSize, width - 2 * roomSize, height - 2 * roomSize);

        final int doorSize = 30;


        theGraphics2D.setColor(myNorthDoor ? Color.GREEN : Color.RED);
        theGraphics2D.fillRect(width / 2 - roomSize / 2, 10, doorSize, doorSize);

        theGraphics2D.setColor(myEastDoor ? Color.GREEN : Color.RED);
        theGraphics2D.fillRect(width - roomSize, height / 2 - roomSize / 2, doorSize, doorSize);

        theGraphics2D.setColor(mySouthDoor ? Color.GREEN : Color.RED);
        theGraphics2D.fillRect(width / 2 - roomSize / 2, height - roomSize, doorSize, doorSize);

        theGraphics2D.setColor(myWestDoor ? Color.GREEN : Color.RED);
        theGraphics2D.fillRect(10, height / 2 - roomSize / 2, doorSize, doorSize);



        drawText(theGraphics2D, width, height);

    }

    /**
     * Draws the "North", "South", "East" and "West" directions along with what room the player is in.
     * @param theGraphics2D - The graphics to display
     * @param theWidth - The width of the panel.
     * @param theHeight - The height of the panel.
     */
    private void drawText(final Graphics2D theGraphics2D, final int theWidth, final int theHeight) {
        BufferedImage[] images = myPlayerCharacter.getCharacterImages(myDirection);
        BufferedImage currentImage = images[myFrameIndex];

        theGraphics2D.drawImage(currentImage, (theWidth / 2) - 20, (theHeight / 2) - 20, 40, 40, this);

        theGraphics2D.setColor(Color.WHITE);
        theGraphics2D.setFont(new Font("Verdana", Font.BOLD, 10));

        theGraphics2D.drawString("Move North", (theWidth / 2) - 40, 50);
        theGraphics2D.drawString("Move East", theWidth - 100, theHeight / 2);
        theGraphics2D.drawString("Move South", (theWidth / 2) - 40, 250);
        theGraphics2D.drawString("Move West", theWidth - 350, theHeight / 2);
    }


    public void updateDirectionAndFrame(String newDirection, int newFrameIndex) {
        myDirection = newDirection;
        myFrameIndex = newFrameIndex;
        repaint();
    }


    public void updateRoomPanel(Room theRoom) {
        this.myNorthDoor = !theRoom.getDoor("North").isClosed();
        this.mySouthDoor = !theRoom.getDoor("South").isClosed();
        this.myEastDoor = !theRoom.getDoor("East").isClosed();
        this.myWestDoor = !theRoom.getDoor("West").isClosed();
        repaint();
    }

    /**
     * The property change methods changes the color of the door from green to red.
     * @param theEvt - A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    public void propertyChange(final PropertyChangeEvent theEvt) {
        switch (theEvt.getPropertyName()) {
            case "North":
                myNorthDoor = !(boolean) theEvt.getNewValue();
                break;
            case "East":
                myEastDoor = !(boolean) theEvt.getNewValue();
                break;
            case "South":
                mySouthDoor = !(boolean) theEvt.getNewValue();
                break;
            case "West":
                myWestDoor = !(boolean) theEvt.getNewValue();
                break;
        }
        repaint();

    }


}
