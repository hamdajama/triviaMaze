package sqlManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

public class SqlManager {
    private static final String PROPERTIES_FILE = "db.properties";
    private static SqlManager instance;
    private Connection connection;

    private String url;
    private String user;
    private String password;

    private SqlManager() {
        loadProperties();
        initializeDatabase();
    }

    public static SqlManager getInstance() {
        if (instance == null) {
            instance = new SqlManager();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find " + PROPERTIES_FILE);
                return;
            }
            properties.load(input);
            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection()) {
            executeSqlFile(conn, "SRS/Triviamaze.sql");
            executeSqlFile(conn, "SRS/insertMultipleChoice.sql");
            executeSqlFile(conn, "SRS/insertShortAnswer.sql");
            executeSqlFile(conn, "SRS/insertTrueFalse.sql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    private void executeSqlFile(Connection conn, String filePath) {
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

    public void executeUpdate(String query, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            setParameters(pstmt, params);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query, Object... params) {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            setParameters(pstmt, params);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }

    public void createTable(String query) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            int rv = stmt.executeUpdate(query);
            System.out.println("executedUpdate() returned " + rv);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void insertData(String query) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            int rv = stmt.executeUpdate(query);
            System.out.println("executedUpdate() returned " + rv);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void displayData(String query) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString("QUESTION") + " " + rs.getString("ANSWER"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
