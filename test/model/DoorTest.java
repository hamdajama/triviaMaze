package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class DoorTest {

    @Test
    public void testDoorConstructor() {
        Door door = new Door();
        Assertions.assertTrue(door.isClosed(), "Door should initially be closed");
    }

    @Test
    void testOpenDoor() {
        Door door = new Door();
        door.open();
        Assertions.assertFalse(door.isClosed(), "Door should be open after calling openDoor method");
    }

    @Test
    public void testCloseDoor() {
        Door door = new Door();
        door.open();
        door.close();
        Assertions.assertTrue(door.isClosed(), "Door should be closed after calling open then close door method");
    }
}
