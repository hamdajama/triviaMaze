package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuestionTest {

    private String QUESTION = "What is the name of Tweetys owner?";

    private String ANSWER = "Granny";

    @Test
    public void testQuestionConstructor() {
        Question testQuestion = new ShortAnswer(QUESTION,ANSWER);
        Assertions.assertEquals(QUESTION, testQuestion.getQuestion(), "This method does not return the question.");
        Assertions.assertEquals(ANSWER, testQuestion.getAnswer(),"This method does not return the answer.");
    }

    @Test
    public void testGetQuestion() {
        Question testQuestion = new ShortAnswer(QUESTION,ANSWER);
        Assertions.assertEquals(QUESTION, testQuestion.getQuestion(), "This method does not return the question.");
    }

    @Test
    public void testGetAnswer() {
        Question testQuestion = new ShortAnswer(QUESTION,ANSWER);
        Assertions.assertEquals(ANSWER, testQuestion.getAnswer(), "This method does not return the answer.");
    }
}
