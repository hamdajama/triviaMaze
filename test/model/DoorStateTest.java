/**
 * TCSS 360 - Trivia Maze
 * DoorState.java
 */

package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the DoorState Enum
 *
 * @author Eric John
 * @version 8/15/2024
 */
public class DoorStateTest {

    /**
     * Tests the state of the enums.
     */
    @Test
    void testEnumValues() {
        DoorState[] states = DoorState.values();
        assertEquals(3, states.length);
        assertTrue(containsState(states, "OPEN"));
        assertTrue(containsState(states, "CLOSED"));
        assertTrue(containsState(states, "EXIT"));
    }

    /**
     * Tests the string of the enum values.
     */
    @Test
    void testEnumValuesUnchanged() {
        assertEquals(DoorState.OPEN, DoorState.valueOf("OPEN"));
        assertEquals(DoorState.CLOSED, DoorState.valueOf("CLOSED"));
        assertEquals(DoorState.EXIT, DoorState.valueOf("EXIT"));
    }

    /**
     * Private helper that sees if the state name is equal to the name in the enum class.
     * @param theStates - The states of the door
     * @param theStateName - The name to represent the state of the door.
     * @return - True if it matches. False otherwise.
     */
    private boolean containsState(final DoorState[] theStates, final String theStateName) {
        for (DoorState state : theStates) {
            if (state.name().equals(theStateName)) {
                return true;
            }
        }
        return false;
    }
}