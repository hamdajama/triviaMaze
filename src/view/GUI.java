
package view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.*;
import controller.GameSaver;
import controller.GameSaver;
import model.DatabaseConnector;
import model.Maze;
import model.PlayerCharacter;
import model.Room;
//import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

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
public class GUI {


    private PlayerCharacter myPlayerCharacter;
    private transient JFrame myFrame;
    private transient JPanel myMazePanel;
    private transient Maze myMaze;
    private static final String UP = "up";
    private static final String RIGHT = "right";
    private static final String DOWN = "down";
    private static final String LEFT = "left";
    private String currentDirection = DOWN;
    private int frameIndex = 0;
    private Map<String, BufferedImage[]> characterImages;
    private transient Timer animationTimer;

    private static final long serialVersionUID = 2L;

    private RoomPanel myRoomPanel;
    private QuestionPanel myQuestionPanel;
    private boolean isKeyDispatcherAdded = false;

    /**
     * Creates a new GUI instance and initializes the game.
     *
     * @param theDBConnector The DatabaseConnector object for accessing the question database.
     * @throws SQLException If an error occurs during database access.
     */
    public GUI(DatabaseConnector theDBConnector) throws SQLException {
        super();
        myMaze = new Maze(theDBConnector);
        myMaze.setMovementAllowed(true);
        myPlayerCharacter = new PlayerCharacter(0, 0);
        setupFrame();
        setupAnimationTimer();
    }

    /**
     * Sets up the main game window and its components.
     */
    private void setupFrame() {
        final int frameWidth = 800;
        final int frameHeight = 800;

        myFrame = new JFrame("Trivia Maze");
        myFrame.setSize(frameWidth, frameHeight);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setLayout(new BorderLayout());
        myFrame.setResizable(false);

        setupMenuBar(myFrame);
        setupPanels(myFrame);

        addKeyEventDispatcher();

        myFrame.setVisible(true);
    }

    private void addKeyEventDispatcher() {
        if (!isKeyDispatcherAdded) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED && myMaze.isMovementAllowed()) {
                    Direction direction = null;
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                            direction = Direction.NORTH;
                            currentDirection = UP;
                            break;
                        case KeyEvent.VK_S:
                            direction = Direction.SOUTH;
                            currentDirection = DOWN;
                            break;
                        case KeyEvent.VK_A:
                            direction = Direction.WEST;
                            currentDirection = LEFT;
                            break;
                        case KeyEvent.VK_D:
                            direction = Direction.EAST;
                            currentDirection = RIGHT;
                            break;
                    }
                    if (direction != null) {
                        Room currentRoom = myMaze.getCurrentRoom();
                        Question doorQues = currentRoom.getQuesDoor(direction);

                        if (doorQues != null){
                            myQuestionPanel.setQuestion(doorQues,direction);
                            myMaze.setMovementAllowed(false);
                            myQuestionPanel.setVisible(true);
                        } else {
                            movePlayer(direction);
                        }
                        myMazePanel.revalidate();
                        myMazePanel.repaint();
                    }
                }
                return false;
            });
            isKeyDispatcherAdded = true;
        }
    }
    private void movePlayer(Direction theDirection) {
        Direction direction = theDirection;
        switch (direction) {
            case NORTH:
                myPlayerCharacter.moveUp();
                break;
            case SOUTH:
                myPlayerCharacter.moveDown();
                break;
            case WEST:
                myPlayerCharacter.moveLeft();
                break;
            case EAST:
                myPlayerCharacter.moveRight();
                break;
        }
        myPlayerCharacter.displayPosition();
        myMaze.processAnswer("correct answer", direction);
    }


    /**
     * Loads the character images for the player character.
     * */
    private void loadCharacterImages() {
        characterImages = new HashMap<>();
        try {
            characterImages.put(UP, new BufferedImage[] {
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_up_0.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_up_1.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_up_2.png"))),
            });
            characterImages.put(RIGHT, new BufferedImage[] {
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_right_0.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_right_1.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_right_2.png"))),
            });
            characterImages.put(DOWN, new BufferedImage[] {
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_down_0.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_down_1.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_down_2.png"))),
            });
            characterImages.put(LEFT, new BufferedImage[] {
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_left_0.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_left_1.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_left_2.png"))),
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupAnimationTimer() {
        animationTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameIndex = (frameIndex + 1) % 3;
                myMazePanel.repaint();
            }
        });
        animationTimer.start();
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
        saveFileItem.addActionListener(e -> saveGameState());
        theMenuFile.add(saveFileItem);

        final JMenuItem loadFileItem = new JMenuItem("Load game");
        loadFileItem.addActionListener(e -> {
            loadGameState();
        });
        theMenuFile.add(loadFileItem);

        final JMenuItem exitFileItem = new JMenuItem("Exit game");
        exitFileItem.addActionListener(e -> System.exit(0));
        theMenuFile.add(exitFileItem);
    }


    private void saveGameState() {
        try {
            GameSaver.save(myPlayerCharacter, "player_character.ser");
            GameSaver.save(myMaze, "maze.ser");
            GameSaver.save(myRoomPanel, "room_panel.ser");
            JOptionPane.showMessageDialog(myFrame, "Game saved successfully!!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(myFrame, "Error saving game state: " + e.getMessage());
        }
    }

    private void loadGameState() {
        try {
            PlayerCharacter loadedPlayer = GameSaver.load("player_character.ser");
            Maze loadedMaze = GameSaver.load("maze.ser");
            RoomPanel loadedRoomPanel = GameSaver.load("room_panel.ser");

            DatabaseConnector dbConnector = new DatabaseConnector();
            loadedMaze.reinitializeDatabaseConnector(dbConnector);

            this.myPlayerCharacter = loadedPlayer;
            this.myMaze = loadedMaze;
            this.myRoomPanel = loadedRoomPanel;

            reinitializeGUI();

            JOptionPane.showMessageDialog(myFrame, "Game loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(myFrame, "Error loading game: " + e.getMessage());
        }
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
                        be either multiple choice, short answer, or true/false. When  you
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
        myMazePanel = new MazePanel(myMaze, myPlayerCharacter);
        myMazePanel.setBackground(Color.BLACK);
        myMazePanel.setPreferredSize(new Dimension(theHalfWidth, theFrame.getHeight()));
        theFrame.add(myMazePanel, BorderLayout.CENTER);
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

        final RoomPanel roomPanel = new RoomPanel();
        roomPanel.setBackground(Color.BLACK);
        roomPanel.setBounds(theHalfWidth, 0, theHalfWidth, theHalfHeight);
        rightPanel.add(roomPanel);

        myQuestionPanel = new QuestionPanel(myMaze);
        myQuestionPanel.setBackground(Color.BLACK);
        myQuestionPanel.setBounds(theHalfWidth, theHalfHeight, theHalfWidth, theHalfHeight);
        rightPanel.add(myQuestionPanel);

        myMaze.addPropertyChangeListener(evt -> {
            if ("move".equals(evt.getPropertyName())) {
                myRoomPanel.updateRoomPanel(myMaze.getCurrentRoom());
            } else if ("correct answer".equals(evt.getPropertyName()) || "wrong answer".equals(evt.getPropertyName())) {
                myRoomPanel.updateRoomPanel(myMaze.getCurrentRoom());
            } else if ("win".equals(evt.getPropertyName()) || "lose".equals(evt.getPropertyName())) {
                boolean isWin = "win".equals(evt.getPropertyName());
                showResults(isWin);
            }
        });
    }


    /**
     * Displays the current room's question in the question panel.
     *
     * @param questionPanel The question panel.
     */
    private void displayCurrentRoomQuestion(QuestionPanel questionPanel, Direction direction) {
        Room currentRoom = myMaze.getCurrentRoom();
        Question doorQues = currentRoom.getQuesDoor(direction);
        //questionPanel.setQuestion(doorQues, );
    }

    /**
     * Shows the game results when the player wins or loses.
     *
     * @param theWin Whether the player won or lost.
     */
    public void showResults(boolean theWin) {
        myMaze.getTrivia().stopTimer();
        String message = theWin ? "Congratulations, you won!" : "Game over, you lost!";
        message += "\nTime taken: " + myMaze.getTrivia().getTime() / 1000 + " seconds";
        message += "\nTries used: " + myMaze.getTrivia().getTrys();
        message += " Correct Answers: " + myMaze.getTrivia().getRightAnswer();
        message += " Wrong Answers " + myMaze.getTrivia().getWrongAnswer();
        JOptionPane.showMessageDialog(null, message, "Game Results", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    /**
     * Reinitialize the GUI after loading the game state.
     */
    private void reinitializeGUI() {
        if (myFrame != null) {
            myFrame.dispose();
        }

        setupFrame();

        if (animationTimer != null) {
            animationTimer.stop();
        }
        setupAnimationTimer();

        myFrame.revalidate();
        myFrame.repaint();
    }

}

