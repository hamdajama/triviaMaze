/**
 * TCSS 360 - Trivia Maze
 * RoomTest.java
 */
package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



/**
 * Unit tests for the Room class.
 *
 * @author Eric John
 * @version 8/15/2024
 */
public class RoomTest {

    private Room myRoom;
    private Question myMockQuestion;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        myMockQuestion = new Question("Test Question", "Test Answer") {
            @Override
            public boolean isMatch(String thePlayerAnswer) {
                return false;
            }
        };
        myRoom = new Room(myMockQuestion);
    }

    /**
     * Tests the Room constructor and initial state of the room.
     */
    @Test
    void testRoomConstructor() {
        assertNotNull(myRoom);
        assertEquals(myMockQuestion, myRoom.getTrivia());
        for (Direction dir : Direction.values()) {
            assertTrue(myRoom.isDoorOpen(dir));
        }
    }

    /**
     * Tests the setAdjacentRoom and getDoor methods.
     */
    @Test
    void testSetAdjacentRoomAndGetDoor() {
        Room adjacentRoom = new Room(myMockQuestion);
        myRoom.setAdjacentRoom(Direction.NORTH, adjacentRoom);

        assertNotNull(myRoom.getDoor(Direction.NORTH));
        assertTrue(myRoom.isDoorOpen(Direction.NORTH));
    }

    /**
     * Tests the setTrivia and getTrivia methods.
     */
    @Test
    void testSetAndGetTrivia() {
        Question newQuestion = new Question("New Question", "New Answer") {
            @Override
            public boolean isMatch(String thePlayerAnswer) {
                return false;
            }
        };
        myRoom.setTrivia(newQuestion);
        assertEquals(newQuestion, myRoom.getTrivia());
    }

    /**
     * Tests the allClosed method.
     */
    @Test
    void testAllClosed() {
        assertFalse(myRoom.allClosed());

        for (Direction dir : Direction.values()) {
            myRoom.setDoorOpen(dir, false);
        }

        assertTrue(myRoom.allClosed());
    }

    /**
     * Tests the isAnswered method.
     */
    @Test
    void testIsAnswered() {
        assertFalse(myRoom.isAnswered());
    }

    /**
     * Tests the isDoorOpen and setDoorOpen methods.
     */
    @Test
    void testIsDoorOpenAndSetDoorOpen() {
        assertTrue(myRoom.isDoorOpen(Direction.NORTH));
        myRoom.setDoorOpen(Direction.NORTH, false);
        assertFalse(myRoom.isDoorOpen(Direction.NORTH));
    }
}