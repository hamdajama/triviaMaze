/**
 * TCSS 360 - Trivia Maze
 * GameSaver.java
 */

package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The {@code GameSaver} class provides methods to save and load the game state or any serializable object.
 * This class handles the serialization and deserialization of objects, enabling the state to be saved and restored.
 * <p>
 * This class uses {@link ObjectOutputStream} and {@link ObjectInputStream} for serialization and deserialization respectively.
 * </p>
 *
 * <pre>
 * Example usage:
 * {@code
 * try {
 *     GameSaver.save(gameState, "game_state.ser");
 *     GUI loadedGame = GameSaver.load("game_state.ser");
 * } catch (IOException | ClassNotFoundException e) {
 *     e.printStackTrace();
 * }
 * }
 * </pre>
 *
 * @version 25/07/2024
 */
public final class GameSaver {

    /**
     * Saves the given object state to a file.
     *
     * @param theObject the object to be saved
     * @param theFileName the name of the file where the object will be saved
     * @throws IOException if an I/O error occurs while saving the object
     */
    public static <T extends Serializable> void save(final T theObject, final String theFileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(theFileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
             out.writeObject(theObject);
        }
    }

    /**
     * Loads the object state from a file.
     *
     * @param theFileName the name of the file from which to load the object
     * @param <T> the type of the object being loaded
     * @return the loaded object
     * @throws IOException if an I/O error occurs while loading the object
     * @throws ClassNotFoundException if the class of the serialized object cannot be found
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T load(final String theFileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(theFileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (T) in.readObject();
        }
    }
}
