/**
 * TCSS 360 - Trivia Maze
 * QuestionPanel.java
 */
package view;

import java.awt.*;

import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;



import model.MultipleChoice;
import model.Question;
import model.ShortAnswer;
import model.TrueFalse;

/**
 * Displays the question to a JPanel
 * @author Eric John
 * @version 7/28/2024
 */
public class QuestionPanel extends JPanel {

    /**
     * The question to be displayed
     */
    private Question myCurrentQuestion;
    /**
     * The choices for multiple choice and True/False
     */
    private ButtonGroup myButtonGroup;
    /**
     * The panel to display the questions
     */
    private final JPanel myQuestionPanel;


    /**
     * Creates the panel for the question. Has a gridLayout(0,1)
     */
    public QuestionPanel() {
        super();
        myButtonGroup = new ButtonGroup();
        setBackground(Color.BLACK);
        myQuestionPanel = new JPanel(new GridLayout(0, 1));
        add(myQuestionPanel, BorderLayout.SOUTH);
    }


    /**
     * Sets the question to ask the player
     * @param theQuestion - The question to ask the player.
     */
    public void setQuestion(final Question theQuestion) {
        myCurrentQuestion = theQuestion;
        displayQuestion();
    }

    /**
     * Displays the question to the panel.
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
     * Displays a multiple choice question to the panel.
     * @param theQuestion - The multiple choice question.
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
     * Displays a short answer question to the panel.
     * @param theQuestion - The short answer question
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

        createSubmitButton(theQuestion);
    }

    /**
     * Displays a true false question to the panel
     * @param theQuestion - The true/false question.
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
     * Helper method to test if the answer is correct and display it to the screen.
     * @param isCorrect - Whether the answer given by the player is correct or not.
     */
    private void handleAnswer(boolean isCorrect) {
        if (isCorrect) {
            JOptionPane.showMessageDialog(this, "Correct answer!");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect answer!");
        }
    }

    /**
     * Helper method for creating the submit button.
     * @param theQuestion - The question that is asked to the player.
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


}
