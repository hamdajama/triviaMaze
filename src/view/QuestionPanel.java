
package view;

import java.awt.*;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import model.*;

/**
 * The QuestionPanel class represents the panel where questions are displayed to the player.
 * It handles displaying multiple choice, short answer, and true/false questions,
 * and processes the player's answers.
 * @author Eric John
 * @version 7/28/2024
 *
 */
public class QuestionPanel extends JPanel {
    private Question myCurrentQuestion;
    private Maze myMaze;
    private ButtonGroup myButtonGroup;
    private JPanel myQuestionPanel;
    private Direction myCurrentDirection;

    /**
     * Constructs a new QuestionPanel.
     *
     * @param maze The Maze object containing the game's state and logic.
     */
    public QuestionPanel(Maze maze) {
        super();
        this.myMaze = maze;
        this.myButtonGroup = new ButtonGroup();
        setBackground(Color.BLACK);
        myQuestionPanel = new JPanel(new GridLayout(0, 1));
        add(myQuestionPanel, BorderLayout.SOUTH);
        setFocusable(true);
    }

    /**
     * Sets the current question to be displayed on the panel.
     *
     * @param theQuestion The Question object to be displayed.
     */
    public void setQuestion(final Question theQuestion, Direction theDirection) {
        myCurrentQuestion = theQuestion;
        myCurrentDirection = theDirection;
        displayQuestion();
        setVisible(true);
    }

    /**
     * Displays the current question on the panel.
     */
    private void displayQuestion() {
        myQuestionPanel.removeAll();
        myButtonGroup = new ButtonGroup();

        JLabel questionLabel = new JLabel(myCurrentQuestion.getQuestion());
        myQuestionPanel.add(questionLabel);

        if (myCurrentQuestion instanceof MultipleChoice) {
            displayMultipleChoice((MultipleChoice) myCurrentQuestion);
        } else if (myCurrentQuestion instanceof ShortAnswer) {
            displayShortAnswer((ShortAnswer) myCurrentQuestion);
        } else if (myCurrentQuestion instanceof TrueFalse) {
            displayTrueFalse((TrueFalse) myCurrentQuestion);
        }

        revalidate();
        repaint();
    }

    /**
     * Displays a multiple choice question on the panel.
     *
     * @param theQuestion The MultipleChoice object to be displayed.
     */
    private void displayMultipleChoice(final MultipleChoice theQuestion) {
        Map<String, String> choices = theQuestion.getChoices();
        for (Map.Entry<String, String> entry : choices.entrySet()) {
            JRadioButton optionButton = new JRadioButton(entry.getValue());
            optionButton.setActionCommand(entry.getKey());
            myButtonGroup.add(optionButton);
            myQuestionPanel.add(optionButton);
        }

        createSubmitButton(theQuestion);
    }

    /**
     * Displays a short answer question on the panel.
     *
     * @param theQuestion The ShortAnswer object to be displayed.
     */
    private void displayShortAnswer(final ShortAnswer theQuestion) {
        JTextField answerField = new JTextField();

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String selectedChoice = answerField.getText();
            boolean isCorrect = theQuestion.isMatch(selectedChoice);
            handleAnswer(isCorrect);
        });

        myQuestionPanel.add(answerField);
        myQuestionPanel.add(submitButton);
    }

    /**
     * Displays a true/false question on the panel.
     *
     * @param theQuestion The TrueFalse object to be displayed.
     */
    private void displayTrueFalse(final TrueFalse theQuestion) {
        JRadioButton trueButton = new JRadioButton("True");
        trueButton.setActionCommand("True");
        JRadioButton falseButton = new JRadioButton("False");
        falseButton.setActionCommand("False");
        myButtonGroup.add(trueButton);
        myButtonGroup.add(falseButton);

        myQuestionPanel.add(trueButton);
        myQuestionPanel.add(falseButton);

        createSubmitButton(theQuestion);
    }

    /**
     * Handles the player's answer and updates the game state accordingly.
     *
     * @param isCorrect Whether the player's answer is correct.
     */
    private void handleAnswer(boolean isCorrect) {
        if (isCorrect) {
            JOptionPane.showMessageDialog(this, "Correct answer!");
            myMaze.processAnswer(myCurrentDirection, true);
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect answer!");
            myMaze.processAnswer(myCurrentDirection, false);
            if (myMaze.getCurrentRoom().allClosed()) {
                JOptionPane.showMessageDialog(this, "Game Over!");
            }
        }
        //setVisible(false);
        getParent().requestFocusInWindow(); // Request focus back to the parent frame after answering
    }

    /**
     * Creates a submit button for the current question and adds it to the panel.
     *
     * @param theQuestion The Question object for which the submit button is created.
     */
    private void createSubmitButton(final Question theQuestion) {
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String selectedChoice = myButtonGroup.getSelection().getActionCommand();
            boolean isCorrect = theQuestion.isMatch(selectedChoice);
            handleAnswer(isCorrect);
        });

        myQuestionPanel.add(submitButton);
    }

    public void clearQuestion() {
        myCurrentQuestion = null;
        myCurrentDirection = null;
        setVisible(false);
        revalidate();
        repaint();
    }
}
