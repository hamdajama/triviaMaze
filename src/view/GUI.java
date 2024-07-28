/**
 * TCSS 360 GUI Class
 * @author - Eric John
 * Summer 2024
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.*;


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
        final int frameWidth =  800;
        final int frameHeight = 800;


        final JFrame frame = new JFrame("Trivia Maze");
        frame.setLocationRelativeTo(null);
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);


        setupMenuBar(frame);
        setupPanels(frame);
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

        setupMenuFile(menuFile, theFrame);
        setupHelpFile(helpFile, theFrame);



        theFrame.setJMenuBar(menuBar);

    }

    /**
     * Sets up the menuFile for the frame.
     * @param theMenuFile - The JMenu for the frame.
     */
    private static void setupMenuFile(final JMenu theMenuFile, final JFrame theFrame) {
        final JMenuItem saveFileItem = new JMenuItem("Save game");
        saveFileItem.addActionListener(e -> JOptionPane.showMessageDialog(theFrame, 
                "Saving the game!"));
        theMenuFile.add(saveFileItem);

        final JMenuItem loadFileItem = new JMenuItem("Load game");
        loadFileItem.addActionListener(e -> JOptionPane.showMessageDialog(theFrame, 
                "Loading saved game!"));
        theMenuFile.add(loadFileItem);


        final JMenuItem exitFileItem = new JMenuItem("Exit game");
        exitFileItem.addActionListener(e -> System.exit(0));
        theMenuFile.add(exitFileItem);
    }

    /**
     * Sets up the helpFile for the frame.
     * @param theHelpFile - The JMenu for the frame.
     */
    private static void setupHelpFile(final JMenu theHelpFile, final JFrame theFrame) {
        final JMenuItem aboutFileItem = getJMenuAboutItem(theFrame);
        theHelpFile.add(aboutFileItem);

        final JMenuItem instructionFileItem = getJMenuInstructionItem(theFrame);
        theHelpFile.add(instructionFileItem);
    }

    /**
     * Creates a JOptionPane for the about menu file.
     * @param theFrame - The frame to send it to.
     * @return A message when a user clicks on about.
     */
    private static JMenuItem getJMenuAboutItem(final JFrame theFrame) {
        final JMenuItem aboutFileItem = new JMenuItem("About");
        aboutFileItem.addActionListener(e -> JOptionPane.showMessageDialog(theFrame,
                """
                        Welcome to Trivia Maze!
                        
                        In this game, you start from the entry point and try to get to
                        the exit by answering questions correctly. The question type will
                        be either multiple choice, short answer, or true/false.  When you
                        get a question wrong, the door will be locked and you will have to
                        find another way to reach the exit. The game is over when you
                        reached the exit or there are no available paths to the exit.
                        
                        Developed by:
                        Eric John
                        Hamda Jama
                        Masumi Yano
                        """,
                "AboutGame",
                JOptionPane.INFORMATION_MESSAGE));
        return aboutFileItem;
    }

    /**
     * Creates a JOptionPane for the instruction file menu.
     * @param theFrame - The frame to send it to.
     * @return A message when the user clicks on the information menu.
     */
    private static JMenuItem getJMenuInstructionItem(final JFrame theFrame) {
        final JMenuItem instructionFileItem = new JMenuItem("Instruction");
        instructionFileItem.addActionListener(e -> JOptionPane.showMessageDialog(theFrame,
                """
                        Instructions:
                        
                        In this game, you interact with questions by left clicking the mouse
                        or touchpad. When you think you have the right answer, click on the
                        submit button!
                        
                        True/False: You will be given a statement and you would have to decide
                        if the answer given is correct or not.
                        
                        Multiple Choice: You are given 4 options and you will have to pick the
                        correct one in order to unlock the door.
                        
                        Short Answer: When doing a short answer question, respond with only one
                        word in order to unlock the door.
                        """,
                "Trivia Instruction",
                JOptionPane.INFORMATION_MESSAGE
                ));
        return instructionFileItem;
    }

    /**
     * Creates the panels for the Trivia maze. In this method, it calls setupMazePanel
     * and setupRightPanel
     * @param theFrame - The frame for the window.
     */
    private static void setupPanels(final JFrame theFrame) {
        final int halfWidth = (int) Math.floor(theFrame.getWidth() * 0.5);
        final int halfHeight = (int) Math.floor(theFrame.getHeight() * 0.5);

        setupMazePanel(theFrame, halfWidth);
        setupRightPanel(theFrame, halfWidth, halfHeight);
    }

    /**
     * Sets up the left panel that contains the maze.
     * @param theFrame - The frame for the window.
     * @param theHalfWidth - Half the width of the given frame.
     */
    private static void setupMazePanel(JFrame theFrame, final int theHalfWidth) {
        final JPanel mazePanel = new JPanel();
        mazePanel.setBackground(Color.MAGENTA);
        mazePanel.setPreferredSize(new Dimension(theHalfWidth, theFrame.getHeight()));
        theFrame.add(mazePanel);
    }

    /**
     * Sets up the right panel that contains the maze.
     * @param theFrame - The frame for the window.
     * @param theHalfWidth - Half the width of the given frame.
     * @param theHalfHeight - Half the height of the given frame.
     */
    private static void setupRightPanel(JFrame theFrame, final int theHalfWidth, final int theHalfHeight) {
        final JPanel rightPanel = new JPanel();
        rightPanel.setBounds(theHalfWidth,0,theHalfWidth, theFrame.getHeight());
        final BoxLayout boxLayout = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        rightPanel.setLayout(boxLayout);
        rightPanel.setPreferredSize(new Dimension(theHalfWidth, theFrame.getHeight()));
        theFrame.add(rightPanel, BorderLayout.EAST);


        final RoomPanel roomPanel = new RoomPanel();
        roomPanel.setBackground(Color.BLACK);
        roomPanel.setBounds(theHalfWidth,0, theHalfWidth, theHalfHeight);
        rightPanel.add(roomPanel, boxLayout);

        // Random question to check if the closeDoor method works.
        TrueFalse question = new TrueFalse(1, "Sample question", 1);
        Room room = new Room(question);

        room.addPropertyChangeListener(roomPanel);
//        room.closeDoor();


        final JPanel questionPanel = new JPanel();
        questionPanel.setBackground(Color.RED);
        questionPanel.setBounds(theHalfWidth,theHalfHeight, theHalfWidth, theHalfHeight);
        rightPanel.add(questionPanel, boxLayout);
    }
}
