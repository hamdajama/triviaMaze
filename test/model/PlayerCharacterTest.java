/**
 * TCSS 360 - Trivia Maze
 * PlayerCharacterTest.java
 */
package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the PlayerCharacter class
 *
 * @author Eric John
 * @version 08/15/2024
 */
class PlayerCharacterTest {

    /**
     * Tests the constructor of the class.
     */
    @Test
    void testPlayerCharacterConstructor() {
        PlayerCharacter player = new PlayerCharacter(3, 4);
        assertEquals(3, player.getMyX());
        assertEquals(4, player.getMyY());
    }

    /**
     * Tests the setPosition method.
     */
    @Test
    void testSetPosition() {
        PlayerCharacter player = new PlayerCharacter(0, 0);
        player.setPosition(5, 6);
        assertEquals(5, player.getMyX());
        assertEquals(6, player.getMyY());
    }
}