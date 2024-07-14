/**
 * TCSS 360 GUI Class
 * @author - Eric John
 * Summer 2024
 */
package view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 * Created a GUI class for user interactions. It will handle keyboard events
 * along with communicating with the model and controller to update the GUI.
 * @author Eric John
 * @version 7/13/2024
 */
public class GUI {

    /**
     * Creates a new GUI instance and initializes the File and Help menu of the game.
     */
    public GUI() {
        super();
        setupFrame();

    }

    /**
     * Sets up the frame for the GUI.
     */
    private static void setupFrame() {
        final int frameWidth = 800;
        final int frameHeight = 800;
        final JFrame frame = new JFrame("Trivia Maze");
        frame.setLocationRelativeTo(null);
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);


        setupMenuBar(frame);
        frame.setVisible(true);
    }

    /**
     * Sets up the menu bar.
     * @param theFrame - The frame to display it to.
     */
    private static void setupMenuBar(final JFrame theFrame) {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu menuFile = new JMenu("File");
        final JMenu helpFile = new JMenu("Help");
        menuBar.add(menuFile);
        menuBar.add(helpFile);

        setupMenuFile(menuFile);
        setupHelpFile(helpFile);



        theFrame.setJMenuBar(menuBar);

    }

    /**
     * Sets up the menuFile for the frame.
     * @param theMenuFile - The JMenu for the frame.
     */
    private static void setupMenuFile(final JMenu theMenuFile) {
        final JMenuItem saveFileItem = new JMenuItem("Save game");
        theMenuFile.add(saveFileItem);

        final JMenuItem loadFileItem = new JMenuItem("Load game");
        theMenuFile.add(loadFileItem);


        final JMenuItem exitFileItem = new JMenuItem("Exit game");
        theMenuFile.add(exitFileItem);
    }

    /**
     * Sets up the helpFile for the frame.
     * @param theHelpFile - The JMenu for the frame.
     */
    private static void setupHelpFile(final JMenu theHelpFile) {
        final JMenuItem aboutFileItem = new JMenuItem("About");
        theHelpFile.add(aboutFileItem);

        final JMenuItem instructionFileItem = new JMenuItem("Instructions");
        theHelpFile.add(instructionFileItem);
    }
}
