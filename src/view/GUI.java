
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import controller.GameSaver;
import model.DatabaseConnector;
import model.Maze;
import model.PlayerCharacter;
import model.Room;

/**
 * The GUI class is responsible for creating and managing the graphical user interface
 * of the Trivia Maze game. It handles user interactions, displays the maze, and shows
 * the current question and room status.
 *
 * <p>This class sets up the main game window, including the menu bar, maze panel, and
 * question panel. It also handles keyboard input for player movement and updates the
 * game state based on user actions.</p>
 *
 * <p>The GUI class implements Serializable to allow the game state to be saved and
 * loaded.</p>
 *
 * @author Eric John
 * @version 7/13/2024
 */
public class GUI implements Serializable {

    private static final long serialVersionUID = 2L;
    private final PlayerCharacter playerCharacter;
    private transient JFrame frame;
    private transient JPanel mazePanel;
    private final Maze myMaze;
    private RoomPanel myRoomPanel;
    private QuestionPanel myQuestionPanel;

    /**
     * Creates a new GUI instance and initializes the game.
     *
     * @param theDBConnector The DatabaseConnector object for accessing the question database.
     * @throws SQLException If an error occurs during database access.
     */
    public GUI(DatabaseConnector theDBConnector) throws SQLException {
        super();
        myMaze = new Maze(theDBConnector);
        playerCharacter = new PlayerCharacter(0, 0);
        setupFrame();
    }

    /**
     * Sets up the main game window and its components.
     */
    private void setupFrame() {
        final int frameWidth = 800;
        final int frameHeight = 800;

        frame = new JFrame("Trivia Maze");
        frame.setLocationRelativeTo(null);
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        setupMenuBar(frame);
        setupPanels(frame);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (myMaze.isMovementAllowed()) {
                    System.out.println("Key pressed: " + e.getKeyCode());
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                            myMaze.move("NORTH");
                            break;
                        case KeyEvent.VK_S:
                            myMaze.move("SOUTH");
                            break;
                        case KeyEvent.VK_A:
                            myMaze.move("WEST");
                            break;
                        case KeyEvent.VK_D:
                            myMaze.move("EAST");
                            break;
                    }
                    playerCharacter.displayPosition();
                    mazePanel.revalidate();
                    mazePanel.repaint();
                    displayCurrentRoomQuestion(myQuestionPanel);
                }
            }
            return false;
        });

        frame.setVisible(true);
    }

    /**
     * Sets up the menu bar for the main game window.
     *
     * @param theFrame The main game window frame.
     */
    private void setupMenuBar(final JFrame theFrame) {
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
     * Sets up the File menu and its items.
     *
     * @param theMenuFile The File menu.
     * @param theFrame    The main game window frame.
     */
    private void setupMenuFile(final JMenu theMenuFile, final JFrame theFrame) {
        final JMenuItem saveFileItem = new JMenuItem("Save game");
        saveFileItem.addActionListener(e -> {
            try {
                GameSaver.saveGame(GUI.this); // Save the current instance
                JOptionPane.showMessageDialog(theFrame, "Game saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(theFrame, "Error saving game: " + ex.getMessage());
            }
        });
        theMenuFile.add(saveFileItem);

        final JMenuItem loadFileItem = new JMenuItem("Load game");
        loadFileItem.addActionListener(e -> {
            try {
                GUI loadedGame = GameSaver.loadGame();
                loadedGame.reinitializeGUI();
                frame.dispose(); // Dispose of the current frame
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(theFrame, "Error loading game: " + ex.getMessage());
            }
        });
        theMenuFile.add(loadFileItem);

        final JMenuItem exitFileItem = new JMenuItem("Exit game");
        exitFileItem.addActionListener(e -> System.exit(0));
        theMenuFile.add(exitFileItem);
    }

    /**
     * Sets up the Help menu and its items.
     *
     * @param theHelpFile The Help menu.
     * @param theFrame    The main game window frame.
     */
    private void setupHelpFile(final JMenu theHelpFile, final JFrame theFrame) {
        final JMenuItem aboutFileItem = getJMenuAboutItem(theFrame);
        theHelpFile.add(aboutFileItem);

        final JMenuItem instructionFileItem = getJMenuInstructionItem(theFrame);
        theHelpFile.add(instructionFileItem);
    }

    /**
     * Creates a JOptionPane for the About menu item.
     *
     * @param theFrame The main game window frame.
     * @return A JMenuItem for the About menu item.
     */
    private JMenuItem getJMenuAboutItem(final JFrame theFrame) {
        final JMenuItem aboutFileItem = new JMenuItem("About");
        aboutFileItem.addActionListener(e -> JOptionPane.showMessageDialog(theFrame,
                """
                        Welcome to Trivia Maze!

                        In this game, you start from the entry point and try to get to
                        the exit by answering questions correctly. The questions type will
                        be either multiple choice, short answer, or true/false. When you
                        get a questions wrong, the door will be locked and you will have to
                        find another way to reach the exit. The game is over when you
                        reached the exit or there are no available paths to the exit.

                        Developed by:
                        Eric John
                        Hamnda Jama
                        Masumi Yano""",
                "AboutGame",
                JOptionPane.INFORMATION_MESSAGE));
        return aboutFileItem;
    }

    /**
     * Creates a JOptionPane for the Instructions menu item.
     *
     * @param theFrame The main game window frame.
     * @return A JMenuItem for the Instructions menu item.
     */
    private JMenuItem getJMenuInstructionItem(final JFrame theFrame) {
        final JMenuItem instructionFileItem = new JMenuItem("Instructions");
        instructionFileItem.addActionListener(e -> JOptionPane.showMessageDialog(theFrame,
                """
                        Instructions:

                        In this game, you interact with questions by left clicking the mouse
                        or touchpad. When you think you have the right answer, click on the
                        submit button!

                        True/False: You will be given a statement and you would have to decide
                        if the answer is correct or not.

                        Multiple Choice: You are given 4 options and you will have to pick the
                        correct one in order to unlock the door.

                        Short Answer: When doing a short answer question, respond with only one
                        word in order to unlock the door.""",
                "Trivia Instructions",
                JOptionPane.INFORMATION_MESSAGE
        ));
        return instructionFileItem;
    }

    /**
     * Sets up the panels for the main game window.
     *
     * @param theFrame The main game window frame.
     */
    private void setupPanels(final JFrame theFrame) {
        final int halfWidth = (int) Math.floor(theFrame.getWidth() * 0.5);
        final int halfHeight = (int) Math.floor(theFrame.getHeight() * 0.5);

        setupMazePanel(theFrame, halfWidth);
        setupRightPanel(theFrame, halfWidth, halfHeight);
    }

    /**
     * Sets up the maze panel.
     *
     * @param theFrame    The main game window frame.
     * @param theHalfWidth Half the width of the main game window frame.
     */
    private void setupMazePanel(JFrame theFrame, final int theHalfWidth) {
        mazePanel = new MazePanel(myMaze, playerCharacter);
        mazePanel.setBackground(Color.BLACK);
        mazePanel.setPreferredSize(new Dimension(theHalfWidth, theFrame.getHeight()));
        theFrame.add(mazePanel, BorderLayout.CENTER);
    }

    /**
     * Sets up the right panel containing the room panel and question panel.
     *
     * @param theFrame     The main game window frame.
     * @param theHalfWidth  Half the width of the main game window frame.
     * @param theHalfHeight Half the height of the main game window frame.
     */
    private void setupRightPanel(JFrame theFrame, final int theHalfWidth, final int theHalfHeight) {
        final JPanel rightPanel = new JPanel();
        rightPanel.setBounds(theHalfWidth, 0, theHalfWidth, theFrame.getHeight());
        final BoxLayout boxLayout = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        rightPanel.setLayout(boxLayout);
        rightPanel.setPreferredSize(new Dimension(theHalfWidth, theFrame.getHeight()));
        theFrame.add(rightPanel, BorderLayout.EAST);

        myRoomPanel = new RoomPanel();
        myRoomPanel.setBackground(Color.BLACK);
        myRoomPanel.setBounds(theHalfWidth, 0, theHalfWidth, theHalfHeight);
        rightPanel.add(myRoomPanel);

        myQuestionPanel = new QuestionPanel(myMaze);
        myQuestionPanel.setBackground(Color.BLACK);
        myQuestionPanel.setBounds(theHalfWidth, theHalfHeight, theHalfWidth, theHalfHeight);
        rightPanel.add(myQuestionPanel);

        // Display the current room's question
        displayCurrentRoomQuestion(myQuestionPanel);
        myMaze.addPropertyChangeListener(evt -> {
            if ("move".equals(evt.getPropertyName())) {
                displayCurrentRoomQuestion(myQuestionPanel);
                updateRoomPanel(myMaze.getCurrentRoom());
            } else if ("correct answer".equals(evt.getPropertyName()) || "wrong answer".equals(evt.getPropertyName())) {
                updateRoomPanel(myMaze.getCurrentRoom());
            }
        });
    }

    /**
     * Displays the current room's question in the question panel.
     *
     * @param questionPanel The question panel.
     */
    private void displayCurrentRoomQuestion(QuestionPanel questionPanel) {
        Room currentRoom = myMaze.getCurrentRoom();
        questionPanel.setQuestion(currentRoom.getTrivia());
    }

    /**
     * Updates the room panel with the current room's door statuses.
     *
     * @param theRoom The current room.
     */
    private void updateRoomPanel(Room theRoom) {
        myRoomPanel.updateRoomPanel(theRoom);
    }

    /**
     * Reinitializes the GUI after loading the game state.
     */
    private void reinitializeGUI() {
        setupFrame();
        mazePanel.repaint();
    }
}
