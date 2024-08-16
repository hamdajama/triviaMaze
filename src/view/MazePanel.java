/**
 * TCSS 360 - Trivia Maze
 * MazePanel.java
 */
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;

import javax.swing.JPanel;

import java.util.Map;

import model.Maze;
import model.PlayerCharacter;


/**
 * The maze panel for the game that shows the player and the maze.
 * @author Eric John
 * @version 8/4/2024
 */
public class MazePanel extends JPanel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The maze of the game.
     */
    private final Maze myMaze;

    /**
     * The character.
     */
    private  PlayerCharacter myPlayerCharacter;

    /**
     * The frame index displaying animation
     */
    private int myFrameIndex;

    /**
     * The direction the player is heading.
     */
    private String myDirection;

    /**
     * The character sprites for the playercharacter
     */
    private final transient Map<String, BufferedImage[]> myCharacterImages;


    /**
     * How big the cell sizes should be.
     */
    private final int cellSize = 75;

    /**
     * Creates the maze panel for the game.
     * @param theMaze - The maze for the game.
     * @param thePlayerCharacter - The character for the game.
     */
    public MazePanel(final Maze theMaze, final PlayerCharacter thePlayerCharacter,
                     final int theFrameIndex,
                     final Map<String, BufferedImage[]> theCharacterImage,
                     final String theDirection) {
        myMaze = theMaze;
        myPlayerCharacter = thePlayerCharacter;
        myFrameIndex = theFrameIndex;
        myCharacterImages = theCharacterImage;
        myDirection = theDirection;
        myMaze.addPropertyChangeListener(e -> repaint());
    }

    /**
     * Paints the maze and the player to the panel.
     * @param theG - The graphics for the game.
     */
    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG);
        drawMaze(theG);
        drawPlayer(theG);

    }

    /**
     * Draws the maze for the game.
     * @param theG - The graphics for the game.
     */
    private void drawMaze(final Graphics theG) {
        for (int x = 0; x < myMaze.getRoomSize(); x++) {
            for (int y = 0; y < myMaze.getRoomSize(); y++) {
                if (myMaze.getRoom(x,y).isAnswered()) {
                    theG.setColor(Color.MAGENTA);
                } else {
                    theG.setColor(Color.BLACK);
                }
                theG.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                theG.setColor(Color.WHITE);
                theG.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
                if (x == myMaze.getRoomSize() - 1 && y == myMaze.getRoomSize() - 1) {
                    theG.setFont(new Font("Verdana", Font.BOLD, 30));
                    theG.drawString("X", (x * cellSize) + 50, (y * cellSize) + 70);
                }
            }
        }
    }

    /**
     * Draws the character for the maze.
     * @param theG - The graphics of the game.
     */
    private void drawPlayer(final Graphics theG) {
        BufferedImage[] images = myCharacterImages.get(myDirection.toUpperCase());
        BufferedImage currentImage = images[myFrameIndex];
        theG.drawImage(currentImage, myPlayerCharacter.getMyX() * cellSize,
                    myPlayerCharacter.getMyY()*cellSize + 10, this);
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
     * Updates the characterSprite in a given direction
     * @param theNewDirection - The new direction the player is heading
     * @param theNewFrameIndex - The frame index of the character sprite.
     */
    public void updateDirectionAndFrame(final String theNewDirection,
                                        final int theNewFrameIndex) {
        myDirection = theNewDirection.toUpperCase();
        myFrameIndex = theNewFrameIndex;
        repaint();
    }

    /**
     * Updates the playerCharacter
     * @param thePlayerCharacter - The playerCharacter
     */
    public void updatePlayerCharacter(final PlayerCharacter thePlayerCharacter) {
        this.myPlayerCharacter = thePlayerCharacter;
        repaint();
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
