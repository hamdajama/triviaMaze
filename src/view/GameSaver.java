package view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The {@code GameSaver} class provides methods to save and load the game state.
 * This class handles the serialization and deserialization of the game state,
 * enabling the game to be saved and restored across different sessions.
 * <p>
 * This class uses {@link ObjectOutputStream} and {@link ObjectInputStream} for
 * serialization and deserialization respectively.
 * </p>
 *
 * <pre>
 * Example usage:
 * {@code
 * try {
 *     GameSaver.saveGame(game);
 *     GUI loadedGame = GameSaver.loadGame();
 * } catch (IOException | ClassNotFoundException e) {
 *     e.printStackTrace();
 * }
 * }
 * </pre>
 *
 * @author Masumi Yano
 * @since 1.0
 */
public class GameSaver {

    /** The file name for saving the game state. */
    private static final String SAVE_FILE = "game_state.ser";

    /**
     * Saves the current game state to a file.
     *
     * @param gameState the current game state to be saved
     * @throws IOException if an I/O error occurs while saving the game state
     */
    public static void saveGame(GUI gameState) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(SAVE_FILE);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gameState);
        }
    }

    /**
     * Loads the game state from a file.
     *
     * @return the loaded game state
     * @throws IOException if an I/O error occurs while loading the game state
     * @throws ClassNotFoundException if the class of the serialized object cannot be found
     */
    public static GUI loadGame() throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(SAVE_FILE);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (GUI) in.readObject();
        }
    }
}
