package view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.io.Serializable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.GameSaver;
import model.*;


import model.PlayerCharacter;

/**
 * Created a GUI class for user interactions. It will handle keyboard events
 * along with communicating with the model and controller to update the GUI.
 * @author Eric John
 * @version 7/13/2024
 */
public class GUI implements Serializable {

    private static final long serialVersionUID = 2L;
    private final PlayerCharacter playerCharacter;
    private transient JFrame frame;
    private transient JPanel mazePanel;
    private transient final Maze myMaze;

    // Directional constants
    private static final String UP = "up";
    private static final String RIGHT = "right";
    private static final String DOWN = "down";
    private static final String LEFT = "left";
    private String currentDirection = DOWN;
    private int frameIndex = 0; // To cycle through 0, 1, 2 for animation.
    private Map<String, BufferedImage[]> characterImages; // Image map for storing directional images.
    private transient Timer myAnimationTimer;

    /**
     * Creates a new GUI instance and initializes the File and Help menu of the game.
     */
    public GUI(DatabaseConnector theDBConnector) throws SQLException {
        super();
        myMaze = new Maze(theDBConnector);
        playerCharacter = new PlayerCharacter(0, 0);
        loadCharacterImages();
        setupFrame();
        setupAnimationTimer();
    }

    /**
     * Loads the character images for the player character.
     */
    private void loadCharacterImages() {
        characterImages = new HashMap<>();
        try {
            characterImages.put(UP, new BufferedImage[]{
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_up_0.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_up_1.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_up_2.png"))),
            });
            characterImages.put(RIGHT, new BufferedImage[]{
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_right_0.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_right_1.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_right_2.png"))),
            });
            characterImages.put(DOWN, new BufferedImage[]{
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_down_0.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_down_1.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_down_2.png"))),
            });
            characterImages.put(LEFT, new BufferedImage[]{
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_left_0.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_left_1.png"))),
                    ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/character/character_left_2.png"))),
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sets up the frame for the GUI.
     */
    private void setupFrame() {
        final int frameWidth =  800;
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
                System.out.println("Key pressed: " + e.getKeyCode()); // Debugging statement
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        playerCharacter.moveUp();
                        break;
                    case KeyEvent.VK_S:
                        playerCharacter.moveDown();
                        break;
                    case KeyEvent.VK_A:
                        playerCharacter.moveLeft();
                        break;
                    case KeyEvent.VK_D:
                        playerCharacter.moveRight();
                        break;
                }
                playerCharacter.displayPosition();
                mazePanel.revalidate();
                mazePanel.repaint();
            }
            return false;
        });

        frame.setVisible(true);
    }

    /**
     * Sets up the animation timer.
     */
    private void setupAnimationTimer() {
        myAnimationTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameIndex = (frameIndex + 1) % 3;
                mazePanel.repaint();
            }
        });
        myAnimationTimer.start();
    }

    /**
     * Sets up the menu bar.
     * @param theFrame - The frame to display it to.
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
     * Sets up the menuFile for the frame.
     * @param theMenuFile - The JMenu for the frame.
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
     * Sets up the helpFile for the frame.
     * @param theHelpFile - The JMenu for the frame.
     */
    private void setupHelpFile(final JMenu theHelpFile, final JFrame theFrame) {
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
    private JMenuItem getJMenuAboutItem(final JFrame theFrame) {
        final JMenuItem aboutFileItem = new JMenuItem("About");
        aboutFileItem.addActionListener(e -> JOptionPane.showMessageDialog(theFrame,
                "Welcome to Trivia Maze!\n\n" +
                        "In this game, you start from the entry point and try to get to\n" +
                        "the exit by answering questions correctly. The questions type will\n"+
                        "be either multiple choice, short answer, or true/false. When  you\n" +
                        "get a questions wrong, the door will be locked and you will have to\n"+
                        "find another way to reach the exit. The game is over when you\n"+
                        "reached the exit or there are no available paths to the exit.\n\n"+
                        "Developed by:\n"+
                        "Eric John\n"+
                        "Hamnda Jama\n"+
                        "Masumi Yano",
                "AboutGame",
                JOptionPane.INFORMATION_MESSAGE));
        return aboutFileItem;
    }

    /**
     * Creates a JOptionPane for the instruction file menu.
     * @param theFrame - The frame to send it to.
     * @return A message when the user clicks on the information menu.
     */
    private JMenuItem getJMenuInstructionItem(final JFrame theFrame) {
        final JMenuItem instructionFileItem = new JMenuItem("Instruction");
        instructionFileItem.addActionListener(e -> JOptionPane.showMessageDialog(theFrame,
                "Instructions:\n\n"+
                        "In this game, you interact with questions by left clicking the mouse\n"+
                        "or touchpad. When you think you have the right answer, click on the\n"+
                        "submit button!\n\n"+
                        "True/False: You will be given a statement and you would have to decide\n"+
                        "if the answer is correct or not.\n\n"+
                        "Multiple Choice: You are given 4 options and you will have to pick the\n"+
                        "correct one in order to unlock the door.\n\n"+
                        "Short Answer: When doing a short answer question, respond with only one\n"+
                        "word in order to unlock the door.",
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
    private void setupPanels(final JFrame theFrame) {
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
    private void setupMazePanel(JFrame theFrame, final int theHalfWidth) {
        mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.MAGENTA);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLUE);
                // Simple Character representation
                int cellSize = 10; // Size of each cell in the grid
                BufferedImage currentImage = characterImages.get(currentDirection)[frameIndex];
                g.drawImage(currentImage, playerCharacter.getX() * cellSize, playerCharacter.getY() * cellSize, cellSize, cellSize, null);
                // Update the playerCharacter with the current maze dimensions
                playerCharacter.setMazeDimensions(getWidth() / cellSize, getHeight() / cellSize);
            }
        };
        mazePanel = new MazePanel(myMaze, playerCharacter);
        mazePanel.setBackground(Color.BLACK);
        mazePanel.setPreferredSize(new Dimension(theHalfWidth, theFrame.getHeight()));
        theFrame.add(mazePanel, BorderLayout.CENTER);
    }

    /**
     * Sets up the right panel that contains the maze.
     * @param theFrame - The frame for the window.
     * @param theHalfWidth - Half the width of the given frame.
     * @param theHalfHeight - Half the height of the given frame.
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

        final QuestionPanel questionPanel = new QuestionPanel();
        questionPanel.setBackground(Color.RED);
        questionPanel.setBounds(theHalfWidth, theHalfHeight, theHalfWidth, theHalfHeight);
        rightPanel.add(questionPanel);

        //Multiple choice

        Map<String, String> choices = new HashMap<>();
        choices.put("A", "Red");
        choices.put("B", "Green");
        choices.put("C", "Blue");
        choices.put("D", "Purple");
        Question multipleChoice = new MultipleChoice( "What color is Yoda's Lightsaber?", choices, "B");
        questionPanel.setQuestion(multipleChoice);

        //Short Answer
//        Question shortAnswer = new ShortAnswer(22, "Who is Luke Skywalkers sister?", "Leia");
//        questionPanel.setQuestion(shortAnswer);

        //True False
//        Question trueFalse = new TrueFalse(2, "Darth Vader is Luke Skywalkers father", 1);
//        questionPanel.setQuestion(trueFalse);
    }

    /**
     * Reinitialize the GUI after loading the game state.
     */
    private void reinitializeGUI() {
        setupFrame();
        mazePanel.repaint();
    }
}
