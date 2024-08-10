///**
// * TCSS 360 - Trivia Maze
// * RoomTest.java
// */
//package model;
//
//import java.util.Map;
//
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
///**
// * Unit tests for the room class.
// * @author Eric John
// * @version 8/7/2024
// */
//public class RoomTest {
//
//    /**
//     * Creating a true/false question to test the methods.
//     */
//    private final Question myQuestion = new TrueFalse("Princess Leia is a Sith.", "0");
//
//    /**
//     * The created room for the unit tests.
//     */
//    private final Room myRoom = new Room(myQuestion);
//
//
//    /**
//     * Tests to see that the number of doors is 4 and that two of the doors are automatically closed.
//     */
//    @Test
//    void testConstructor() {
//        Map<String, Door> doors = myRoom.getDoors();
//        Assertions.assertEquals(4, doors.size(),"The room doesn't have 4 doors");
//        Assertions.assertTrue(doors.get("North").isClosed(), "The North door should be closed for the beginning of the maze.");
//        Assertions.assertTrue(doors.get("West").isClosed(),"The West door should be closed for the beginning of the maze.");
//        Assertions.assertFalse(doors.get("South").isClosed(),"South door should be open for the beginning of the maze");
//        Assertions.assertFalse(doors.get("East").isClosed(),"East door should be open for the beginning of the maze");
//    }
//
//    /**
//     * Tests to see if each door is not null.
//     */
//    @Test
//    void testGetDoors() {
//        Assertions.assertNotNull(myRoom.getDoor("North"), "North door shouldn't be null");
//        Assertions.assertNotNull(myRoom.getDoor("South"), "South door shouldn't be null");
//        Assertions.assertNotNull(myRoom.getDoor("West"), "West door shouldn't be null");
//        Assertions.assertNotNull(myRoom.getDoor("East"), "East door shouldn't be null");
//    }
//
//    /**
//     * Tests to see if the room class correctly gets the trivia question.
//     */
//    @Test
//    void testGetTrivia() {
//        Assertions.assertEquals(myQuestion, myRoom.getTrivia(), "Question should match the trivia question");
//    }
//
//    /**
//     * Tests if the answer question method returns true and false based on the player answer.
//     */
//    @Test
//    void testAnswerQuestion() {
//        Assertions.assertTrue(myRoom.answerQuestion("False"),"The return statement should be true.");
//        Assertions.assertFalse(myRoom.answerQuestion("True"),"The return statement should be false.");
//    }
//
//    /**
//     * Tests to see if an additional door is closed when answered incorrectly.
//     */
//    @Test
//    void testWrongAnswer() {
//        myRoom.wrongAnswer("True");
//        Assertions.assertEquals(3, myRoom.getDoors().values().stream().filter(Door::isClosed).count());
//    }
//
//    /**
//     * Tests to see if none of the doors close if the player answered correctly.
//     */
//    @Test
//    void testCorrectAnswer() {
//        myRoom.wrongAnswer("False");
//        Assertions.assertEquals(2,myRoom.getDoors().values().stream().filter(Door::isClosed).count());
//    }
//
//    /**
//     * Tests the close door method.
//     */
//    @Test
//    void testCloseDoor() {
//        myRoom.closeDoor();
//        long closedDoorCount = myRoom.getDoors().values().stream().filter(Door::isClosed).count();
//        Assertions.assertEquals(3, closedDoorCount);
//    }
//
//    /**
//     * Tests if the allClosed method returns true when all the doors are closed.
//     */
//    @Test
//    void testAllClosedDoor() {
//        myRoom.closeDoor();
//        myRoom.closeDoor();
//        Assertions.assertTrue(myRoom.allClosed());
//    }
//
//    /**
//     * Tests the boolean if the question has been answered.
//     */
//    @Test
//    void testIsAnswered() {
//        Assertions.assertFalse(myRoom.isAnswered());
//    }
//
//    /**
//     * Tests to see if the boolean switches state from true to false.
//     */
//    @Test
//    void testSetAnswered() {
//        myRoom.setAnswered(true);
//        Assertions.assertTrue(myRoom.isAnswered());
//    }
//}
