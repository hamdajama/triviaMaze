package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import java.awt.image.BufferedImage;
import java.io.IOException;

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
    private transient MazePanel myMazePanel;
    private transient Maze myMaze;
    private static final String UP = "NORTH";
    private static final String RIGHT = "EAST";
    private static final String DOWN = "SOUTH";
    private static final String LEFT = "WEST";
    /**
     * The direction the player intends to go.
     */
    private String currentDirection = DOWN;
    private int frameIndex = 0;
    private Map<String, BufferedImage[]> characterImages;
    private transient Timer animationTimer;

    private static final long serialVersionUID = 2L;

    private RoomPanel myRoomPanel;
    private QuestionPanel myQuestionPanel;
    private boolean isKeyDispatcherAdded = false;
    private boolean isAnsweringQuestion = false;
    private boolean isFirstStep = true;

    /**
     * The audio for the game.
     */
    private final SoundPlayer mySound = SoundPlayer.getInstance();

    /**
     * Creates a new GUI instance and initializes the game.
     *
     * @param theDBConnector The DatabaseConnector object for accessing the question database.
     * @throws SQLException If an error occurs during database access.
     */
    public GUI(DatabaseConnector theDBConnector) throws SQLException {
        super();
        myMaze = new Maze(theDBConnector);
        myPlayerCharacter = new PlayerCharacter(0, 0);
        loadCharacterImages();
        setupFrame();
        setupAnimationTimer();
        try {
            mySound.playBackgroundMusic();
        } catch (final Exception e) {
            System.out.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
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
                if (e.getID() == KeyEvent.KEY_PRESSED && !myMaze.isQuestionPending()) {
                    Direction direction = null;
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                            direction = Direction.NORTH;
                            break;
                        case KeyEvent.VK_S:
                            direction = Direction.SOUTH;
                            break;
                        case KeyEvent.VK_A:
                            direction = Direction.WEST;
                            break;
                        case KeyEvent.VK_D:
                            direction = Direction.EAST;
                            break;
                    }
                    if (direction != null) {
                        currentDirection = String.valueOf(direction);
                      //  myPlayerCharacter.displayPosition();
                        myMazePanel.updateDirectionAndFrame(String.valueOf(direction), frameIndex);
                        isFirstStep = false;
                        myRoomPanel.updateDirectionAndFrame(String.valueOf(direction), frameIndex);
                        mySound.playSFX("audio/mixkit-player-jumping-in-a-video-game-2043.wav");
                        System.out.println("Key pressed: " + direction);
                        myMaze.move(direction);
                    } else {
                        System.out.println("Cannot move " + direction);
                    }
                }
                return false;
            });
            isKeyDispatcherAdded = true;
        }
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
            System.err.println("Error loading character images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupAnimationTimer() {
        animationTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameIndex = (frameIndex + 1) % 3;
                if (isAnsweringQuestion) {
                    myMazePanel.updateFrame(frameIndex);
                }
                myMazePanel.repaint();
                if (!isFirstStep) {
                    myRoomPanel.updateFrame(frameIndex);
                    myRoomPanel.repaint();
                }

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
        myMazePanel = new MazePanel(myMaze, myPlayerCharacter, frameIndex, characterImages, currentDirection);
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

        myRoomPanel = new RoomPanel(myMaze, frameIndex, characterImages, currentDirection);
        myRoomPanel.setBackground(Color.BLACK);
        myRoomPanel.setBounds(theHalfWidth, 0, theHalfWidth, theHalfHeight);
        rightPanel.add(myRoomPanel);

        myQuestionPanel = new QuestionPanel(myMaze);
        myQuestionPanel.setGUI(this);
        myQuestionPanel.setBackground(Color.BLACK);
        myQuestionPanel.setBounds(theHalfWidth, theHalfHeight, theHalfWidth, theHalfHeight);
        rightPanel.add(myQuestionPanel);

        myMaze.addPropertyChangeListener(evt -> {
            if ("question".equals(evt.getPropertyName())) {

                Maze.QuestionEvent questionEvent = (Maze.QuestionEvent) evt.getNewValue();
                displayQuestion(questionEvent.getQuestion(), questionEvent.getDirection());

            } else if ("move".equals(evt.getPropertyName())) {

                Maze.MoveEvent moveEvent = (Maze.MoveEvent) evt.getNewValue();
                myPlayerCharacter.move(currentDirection);
                updateRoomPanel(moveEvent.getRoom(), moveEvent.getX(),moveEvent.getY());
                myQuestionPanel.clearQuestion();

            } else if ("correct answer".equals(evt.getPropertyName())) {

                mySound.playSFX("audio/mixkit-correct-answer-reward-952.wav");
                myMaze.getTrivia().incrementTrys();  // Increment tries
                myMaze.getTrivia().incrementRightAnswer();  // Increment right answer

            } else if ("wrong answer".equals(evt.getPropertyName())) {

                updateRoomPanel(myMaze.getCurrentRoom(), myMaze.getCurrentX(), myMaze.getCurrentY());
                mySound.playSFX("audio/mixkit-player-losing-or-failing-2042.wav");
                myMaze.getTrivia().incrementTrys();         // increment tries
                myMaze.getTrivia().incrementWrongAnswer();  // Increment wrong answer
                myMaze.isGameOver();

            } else if ("game over".equals(evt.getPropertyName())) {

                boolean playerWon = (boolean) evt.getNewValue();
                showGameOverDialog(playerWon);
            }
        });
    }

    /**
     * Shows the game over dialog based on if they won or lost the game.
     * @param theResult - If true, creates a congrats message. If false, creates a game over message.
     */
    private void showGameOverDialog(final boolean theResult) {
        myMaze.getTrivia().stopTimer();
        String message = theResult ? "Congratulations, you won!" : "Game over, you lost!";
        message += "\nTime taken: " + myMaze.getTrivia().getTime() / 1000 + " seconds";
        message += "\nTries used: " + myMaze.getTrivia().getTrys();
        message += "\nCorrect Answers: " + myMaze.getTrivia().getRightAnswer();
        message += "\nWrong Answers " + myMaze.getTrivia().getWrongAnswer();
        JOptionPane.showMessageDialog(null, message, "Game Results", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }


    /**
     * Displays the question to the panel.
     * @param theQuestion - The question to ask the player.
     * @param theDirection - The direction the player is going.
     */
    private void displayQuestion(final Question theQuestion, final Direction theDirection) {
        System.out.println("Displaying question: " + theQuestion.getQuestion());
        isAnsweringQuestion = true;
        myQuestionPanel.setQuestion(theQuestion, theDirection);
        myQuestionPanel.setVisible(true);
    }

    /**
     * Updates the room panel with the current room's door statuses.
     *
     * @param theRoom The current room.
     */
    private void updateRoomPanel(final Room theRoom, final int theX, final int theY) {
        System.out.println("Updating RoomPanel: x=" + theX + ", y=" + theY);
        myRoomPanel.updateRoomPanel(theRoom, theX, theY);
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

    public void stopAnsweringAnimation() {
        isAnsweringQuestion = false;
    }
}

