package view;

import controller.GameSaver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.KeyboardFocusManager;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.DatabaseConnector;
import model.Direction;
import model.Maze;
import model.MoveEvent;
import model.PlayerCharacter;
import model.Question;
import model.QuestionEvent;
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


    /**
     * Serial for the GUI
     */
    @Serial
    private static final long serialVersionUID = 3L;


    /**
     * The player character for the maze
     */
    private PlayerCharacter myPlayerCharacter;

    /**
     * The frame for the game
     */
    private transient JFrame myFrame;

    /**
     * The maze panel to display the maze
     */
    private transient MazePanel myMazePanel;

    /**
     * The maze the player will traverse through
     */
    private transient Maze myMaze;

    /**
     * Constant North Direction
     */
    private static final String UP = "NORTH";

    /**
     * Constant East Direction
     */
    private static final String RIGHT = "EAST";

    /**
     * Constant South Direction
     */
    private static final String DOWN = "SOUTH";

    /**
     * Constant West Direction
     */
    private static final String LEFT = "WEST";

    /**
     * The direction the player intends to go.
     */
    private String myCurrentDirection = DOWN;

    /**
     * Represents the sprites for the character.
     */
    private int myFrameIndex = 0;

    /**
     * The character images.
     */
    private transient Map<String, BufferedImage[]> myCharacterImages;

    private Map<String, String[]> myCharacterImagePaths;

    private boolean isBackgroundMusicPlaying = false;



    /**
     * RoomPanel to display the room
     */
    private RoomPanel myRoomPanel;

    /**
     * QuestionPanel to display the question
     */
    private QuestionPanel myQuestionPanel;

    /**
     * Boolean for saving and loading the game.
     */
    private boolean isKeyDispatcherAdded = false;

    /**
     * Boolean if the player is answering a question
     */
    private boolean isAnsweringQuestion = false;

    /**
     * Boolean if the player is in the initial position of the maze
     */
    private boolean isFirstStep = true;

    /**
     * The audio for the game.
     */
    private transient SoundPlayer mySound;

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
        mySound = SoundPlayer.getInstance();
        initializeCharacterImagePaths();
        loadCharacterImages();
        setupFrame();
        setupAnimationTimer();
        try {
            mySound.playBackgroundMusic();
        } catch (final Exception e) {
            System.out.println("Error playing background music: " + e.getMessage());
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

    /**
     * Key events for the game.
     */
    private void addKeyEventDispatcher() {
        if (!isKeyDispatcherAdded) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED && !myMaze.isQuestionPending()) {
                    Direction direction = switch (e.getKeyCode()) {
                        case KeyEvent.VK_W -> Direction.NORTH;
                        case KeyEvent.VK_S -> Direction.SOUTH;
                        case KeyEvent.VK_A -> Direction.WEST;
                        case KeyEvent.VK_D -> Direction.EAST;
                        default -> null;
                    };
                    if (direction != null) {
                        handleMovement(direction);
                    }
                }
                return false;
            });
            isKeyDispatcherAdded = true;
        }
    }

    /**
     * Handles the movement for the game
     * @param theDirection - The direction the player is heading.
     */
    private void handleMovement(final Direction theDirection) {
        if (myMaze.isQuestionPending()) {
            return;
        }

        myCurrentDirection = String.valueOf(theDirection);

        myMazePanel.updateDirectionAndFrame(myCurrentDirection, myFrameIndex);
        myRoomPanel.updateDirectionAndFrame(myCurrentDirection, myFrameIndex);

        isFirstStep = false;
        mySound.playSFX("audio/mixkit-player-jumping-in-a-video-game-2043.wav");
        System.out.println("Key pressed: " + theDirection);

        if (myMaze.canMove(theDirection)) {
            myMaze.move(theDirection);
        } else {
            System.out.println("Cannot move in direction: " + theDirection);
        }

        // Repaint both panels
        myMazePanel.repaint();
        myRoomPanel.repaint();
    }




    /**
     * Loads the character images for the player character.
     * */
    private void loadCharacterImages() {
        myCharacterImages = new HashMap<>();
        for (Map.Entry<String, String[]> entry : myCharacterImagePaths.entrySet()) {
            String direction = entry.getKey();
            String[] paths = entry.getValue();
            BufferedImage[] images = new BufferedImage[paths.length];
            for (int i = 0; i < paths.length; i++) {
                try {
                    images[i] = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(paths[i])));
                } catch (IOException e) {
                    System.err.println("Error loading character image: " + paths[i]);
                }
            }
            myCharacterImages.put(direction, images);
        }
    }

    private void initializeCharacterImagePaths() {
        myCharacterImagePaths = new HashMap<>();
        myCharacterImagePaths.put(UP, new String[] {
                "resources/character/character_up_0.png",
                "resources/character/character_up_1.png",
                "resources/character/character_up_2.png"
        });
        myCharacterImagePaths.put(RIGHT, new String[] {
                "resources/character/character_right_0.png",
                "resources/character/character_right_1.png",
                "resources/character/character_right_2.png"
        });
        myCharacterImagePaths.put(DOWN, new String[] {
                "resources/character/character_down_0.png",
                "resources/character/character_down_1.png",
                "resources/character/character_down_2.png"
        });
        myCharacterImagePaths.put(LEFT, new String[] {
                "resources/character/character_left_0.png",
                "resources/character/character_left_1.png",
                "resources/character/character_left_2.png"
        });
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(myMaze);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        myMaze = (Maze) in.readObject();

        if (myMaze == null) {
            throw new IOException("Maze object is null after deserialization");
        }

        mySound = SoundPlayer.getInstance();
        loadCharacterImages();
    }

    /**
     * Animation timer to display animation.
     */
    private void setupAnimationTimer() {
        Timer myAnimationTimer = new Timer(200, e -> {
            myFrameIndex = (myFrameIndex + 1) % 3;
            if (isAnsweringQuestion) {
                myMazePanel.updateFrame(myFrameIndex);
            }
            myMazePanel.repaint();
            if (!isFirstStep) {
                myRoomPanel.updateFrame(myFrameIndex);
                myRoomPanel.repaint();
            }

        });
        myAnimationTimer.start();
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

        setupMenuFile(menuFile);
        setupHelpFile(helpFile, theFrame);

        theFrame.setJMenuBar(menuBar);
    }

    /**
     * Sets up the File menu and its items.
     *
     * @param theMenuFile The File menu.
     */
    private void setupMenuFile(final JMenu theMenuFile) {
        final JMenuItem saveFileItem = new JMenuItem("Save game");
        saveFileItem.addActionListener(e -> saveGameState());
        theMenuFile.add(saveFileItem);

        final JMenuItem loadFileItem = new JMenuItem("Load game");
        loadFileItem.addActionListener(e -> loadGameState());
        theMenuFile.add(loadFileItem);

        final JMenuItem exitFileItem = new JMenuItem("Exit game");
        exitFileItem.addActionListener(e -> System.exit(0));
        theMenuFile.add(exitFileItem);
    }


    /**
     * Saves the game.
     */
    private void saveGameState() {
        isBackgroundMusicPlaying = mySound.isBackgroundMusicRunning();
        try {
            GameSaver.save(this, "game_state.ser");
            JOptionPane.showMessageDialog(myFrame, "Game saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(myFrame, "Error saving game state: " +
                    e.getMessage());
        }
    }

    /**
     * Loads the game.
     */
    private void loadGameState() {
        try {
            GUI loadedState = GameSaver.load("game_state.ser");

            this.myPlayerCharacter = loadedState.myPlayerCharacter;
            this.myMaze = loadedState.myMaze;

            if (this.myMaze == null) {
                throw new IOException("Maze object is null after loading");
            }

            DatabaseConnector dbConnector = new DatabaseConnector();
            this.myMaze.reinitializeDatabaseConnector(dbConnector);

            mySound = SoundPlayer.getInstance();
            loadCharacterImages();
            reinitializeGUI();

            JOptionPane.showMessageDialog(myFrame, "Game loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game state: " + e.getMessage());
            JOptionPane.showMessageDialog(myFrame, "Error loading game: " +
                    e.getMessage());
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
    private void setupMazePanel(final JFrame theFrame, final int theHalfWidth) {
        myMazePanel = new MazePanel(myMaze, myPlayerCharacter, myFrameIndex,
                                    myCharacterImages, myCurrentDirection);
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
    private void setupRightPanel(final JFrame theFrame, final int theHalfWidth,
                                 final int theHalfHeight) {
        final JPanel rightPanel = new JPanel();
        rightPanel.setBounds(theHalfWidth, 0, theHalfWidth, theFrame.getHeight());
        final BoxLayout boxLayout = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        rightPanel.setLayout(boxLayout);
        rightPanel.setPreferredSize(new Dimension(theHalfWidth, theFrame.getHeight()));
        theFrame.add(rightPanel, BorderLayout.EAST);

        myRoomPanel = new RoomPanel(myMaze, myFrameIndex, myCharacterImages,
                                    myCurrentDirection);
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

                QuestionEvent questionEvent = (QuestionEvent) evt.getNewValue();
                displayQuestion(questionEvent.getQuestion(), questionEvent.getDirection());

            } else if ("move".equals(evt.getPropertyName())) {

                MoveEvent moveEvent = (MoveEvent) evt.getNewValue();
                myPlayerCharacter.setPosition(moveEvent.getX(), moveEvent.getY());
                myMazePanel.updatePlayerCharacter(myPlayerCharacter);
                updateRoomPanel(moveEvent.getRoom(), moveEvent.getX(),moveEvent.getY());
                myQuestionPanel.clearQuestion();

            } else if ("correct answer".equals(evt.getPropertyName())) {

                mySound.playSFX("audio/mixkit-correct-answer-reward-952.wav");
                myMaze.getTrivia().incrementTrys();
                myMaze.getTrivia().incrementRightAnswer();

            } else if ("wrong answer".equals(evt.getPropertyName())) {

                myMazePanel.updateDirectionAndFrame(DOWN, myFrameIndex);
                myRoomPanel.updateDirectionAndFrame(DOWN, myFrameIndex);
                updateRoomPanel(myMaze.getCurrentRoom(), myMaze.getCurrentX(),
                                myMaze.getCurrentY());
                mySound.playSFX("audio/mixkit-player-losing-or-failing-2042.wav");
                myMaze.getTrivia().incrementTrys();
                myMaze.getTrivia().incrementWrongAnswer();
                myMaze.isGameOver();

            } else if ("game over".equals(evt.getPropertyName())) {

                boolean playerWon = (boolean) evt.getNewValue();
                showGameOverDialog(playerWon);
            }
        });
    }

    /**
     * Shows the game over dialog based on if they won or lost the game.
     * @param theResult - If true, creates a congrats message. If false, creates
     *                    a game over message.
     */
    private void showGameOverDialog(final boolean theResult) {
        myMaze.getTrivia().stopTimer();
        String message = theResult ? "Congratulations, you won!" : "Game over, you lost!";
        message += "\nTime taken: " + myMaze.getTrivia().getTime() / 1000 + " seconds";
        message += "\nTries used: " + myMaze.getTrivia().getTrys();
        message += "\nCorrect Answers: " + myMaze.getTrivia().getRightAnswer();
        message += "\nWrong Answers " + myMaze.getTrivia().getWrongAnswer();
        JOptionPane.showMessageDialog(null, message, "Game Results",
                                        JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }


    /**
     * Displays the question to the panel.
     * @param theQuestion - The question to ask the player.
     * @param theDirection - The direction the player is going.
     */
    private void displayQuestion(final Question theQuestion,
                                 final Direction theDirection) {
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
        if (myMaze == null) {
            throw new IllegalStateException("Maze is null during GUI reinitialization");
        }
        setupFrame();
        setupAnimationTimer();

        myMazePanel = new MazePanel(myMaze, myPlayerCharacter, myFrameIndex,
                                    myCharacterImages, myCurrentDirection);
        myRoomPanel = new RoomPanel(myMaze, myFrameIndex, myCharacterImages,
                                    myCurrentDirection);
        myQuestionPanel = new QuestionPanel(myMaze);
        myQuestionPanel.setGUI(this);
        isKeyDispatcherAdded = false;

        if (myFrame != null) {
            setupPanels(myFrame);
        } else {
            throw new IllegalStateException("Frame is null during GUI " +
                    "reinitialization");
        }

        if (isBackgroundMusicPlaying) {
            mySound.playBackgroundMusic();
        }

        myFrame.revalidate();
        myFrame.repaint();
    }

    /**
     * Stops the animation for the game
     */
    public void stopAnsweringAnimation() {
        isAnsweringQuestion = false;
    }
}

