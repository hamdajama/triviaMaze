package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.JPanel;

import model.Maze;
import model.PlayerCharacter;

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

    private final Map<String, BufferedImage[]> myCharacterImage;


    /**
     * How big the cell sizes should be.
     */
    private final int cellSize = 75;

    private String myCurrentDirection;
    private int myFrameIndex;

    /**
     * Creates the maze panel for the game.
     * @param theMaze - The maze for the game.
     * @param thePlayerCharacter - The character for the game.
     */
    public MazePanel(Maze theMaze, PlayerCharacter thePlayerCharacter, String theDirection, int theFrameIndex,
                     Map<String, BufferedImage[]> theCharacterImage) {
        myMaze = theMaze;
        myPlayerCharacter = thePlayerCharacter;
        myCurrentDirection = theDirection;
        myCharacterImage = theCharacterImage;
        myFrameIndex = theFrameIndex;
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
        BufferedImage[] images = myCharacterImage.get(myCurrentDirection);
        BufferedImage currentImage = images[myFrameIndex];
        theG.drawImage(currentImage, myPlayerCharacter.getX() * cellSize, myPlayerCharacter.getY() * cellSize, this);
    }

    public void updateDirectionAndFrame(final String theNewDirection, final int theNewFrameIndex) {
        myCurrentDirection = theNewDirection;
        myFrameIndex = theNewFrameIndex;
        repaint();
    }
}
