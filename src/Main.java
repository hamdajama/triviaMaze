import view.GUI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public final class Main {
    // THIS IS PLACE FOLDER
    private static final String URL = "jdbc:mysql://localhost:3306/yourDatabase";
    private static final String USER = "username";
    private static final String PASSWORD = "password";

    public static void main(final String[] theArgs) {
        new GUI();
        initializeDatabase();
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            executeSqlFile(conn, "SRS/Triviamaze.sql");
            executeSqlFile(conn, "SRS/insertMultipleChoice.sql");
            executeSqlFile(conn, "SRS/insertShortAnswer.sql");
            executeSqlFile(conn, "SRS/insertTrueFalse.sql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeSqlFile(Connection conn, String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] queries = content.split(";");
            try (Statement stmt = conn.createStatement()) {
                for (String query : queries) {
                    if (query.trim().isEmpty()) {
                        continue;
                    }
                    stmt.executeUpdate(query);
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}