package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class GameObjectTest {

    @Test
    public void testConstructorAndGetters() {
        GameObject obj = new GameObject(10, 20);

        Assertions.assertEquals(10, obj.getMyX(),"Starting x coordinate should be 10");
        Assertions.assertEquals(20, obj.getMyY(),"Starting y coordinate should be 20");
    }

    @Test
    public void testSetX() {
        GameObject obj = new GameObject(10, 20);

        obj.setMyX(30);
        
        Assertions.assertEquals(30, obj.getMyX(),"Starting x coordinate should be changed from 10 to 30");
    }

    @Test
    public void testSetY() {
        GameObject obj = new GameObject(10, 20);

        obj.setMyY(40);

        Assertions.assertEquals(40, obj.getMyY(), "The y coordinate should be changed from 20 to 40");
    }

    @Test
    public void testDisplayPosition() {
        GameObject obj = new GameObject(10, 20);

        String expectedOutput = "GameObject is at (10, 20)\n";
        expectedOutput = expectedOutput.replace("\n", System.lineSeparator());



        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        obj.displayPosition();

        Assertions.assertEquals(expectedOutput, outContent.toString(), "displayPosition should output the correct position");
    }
}


