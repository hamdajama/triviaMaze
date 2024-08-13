package view;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import model.Direction;
import model.Maze;
import model.PlayerCharacter;

import java.util.Map;

/**
 * The maze panel for the game that shows the player and the maze.
 * @author Eric John
 * @version 8/4/2024
 */
public class MazePanel extends JPanel {

    /**
     * The maze of the game.
     */
    private final Maze myMaze;

    /**
     * The character.
     */
    private final PlayerCharacter myPlayerCharacter;

    private int myFrameIndex;

    private String myDirection;

    private final Map<String, BufferedImage[]> myCharacterImages;


    /**
     * How big the cell sizes should be.
     */
    private final int cellSize = 75;

    /**
     * Creates the maze panel for the game.
     * @param theMaze - The maze for the game.
     * @param thePlayerCharacter - The character for the game.
     */
    public MazePanel(Maze theMaze, PlayerCharacter thePlayerCharacter,
                     int theFrameIndex, Map<String, BufferedImage[]> theCharacterImage,
                     String theDirection) {
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
    protected void paintComponent(Graphics theG) {
        super.paintComponent(theG);
        drawMaze(theG);
        drawPlayer(theG);

    }

    /**
     * Draws the maze for the game.
     * @param theG - The graphics for the game.
     */
    private void drawMaze(Graphics theG) {
//        //TEMPORARY CALL TO CHECK IF THE ROOM CHANGES COLOR
//        myMaze.getRoom(0,0).setAnswered(true);
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
    private void drawPlayer(Graphics theG) {
        myPlayerCharacter.setMazeDimensions(myMaze.getRoomSize(), myMaze.getRoomSize());
        BufferedImage[] images = myCharacterImages.get(myDirection.toUpperCase());
        BufferedImage currentImage = images[myFrameIndex];
        theG.drawImage(currentImage, myPlayerCharacter.getX() * cellSize, myPlayerCharacter.getY()*cellSize + 10, this);
    }

    public void updateFrame(final int theFrameIndex) {
        myFrameIndex = theFrameIndex;
        repaint();
    }

    public void updateDirectionAndFrame(final String theNewDirection, final int theNewFrameIndex) {
        myDirection = theNewDirection.toUpperCase();
        myFrameIndex = theNewFrameIndex;
        repaint();
    }
}
