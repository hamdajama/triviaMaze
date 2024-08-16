/**
 * TCSS 360 - Trivia Maze
 * MoveEventTest.java
 */
package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


/**
 * Unit test for MoveEvent class.
 *
 * @author Eric John
 * @version 08/15/2024
 */
public class MoveEventTest {

    /**
     * Tests the constructor and the getters of the class.
     */
    @Test
    void testMoveEventConstructorAndGetters() {
        Room mockRoom = new Room(null);
        Direction direction = Direction.NORTH;
        int x = 1;
        int y = 2;

        MoveEvent moveEvent = new MoveEvent(mockRoom, direction, x, y);

        assertSame(mockRoom, moveEvent.getRoom());
        assertEquals(x, moveEvent.getX());
        assertEquals(y, moveEvent.getY());
    }
}