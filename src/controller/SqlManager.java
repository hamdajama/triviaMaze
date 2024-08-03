package controller;

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

/**
 * The {@code SqlManager} class provides a singleton instance to manage SQL
 * database connections and execute SQL queries. It loads database connection
 * properties from a configuration file, establishes connections, and provides
 * methods to execute SQL statements, including updates and queries.
 * <p>
 * This class is designed to facilitate interaction with an SQL database,
 * including creating tables, inserting data, and displaying data from the database.
 * </p>
 *
 * <pre>
 * Example usage:
 * {@code
 * SqlManager sqlManager = SqlManager.getInstance();
 * sqlManager.executeUpdate("INSERT INTO users (name, email) VALUES (?, ?)", "John Doe", "john.doe@example.com");
 * ResultSet rs = sqlManager.executeQuery("SELECT * FROM users WHERE email = ?", "john.doe@example.com");
 * while (rs.next()) {
 *     System.out.println(rs.getString("name"));
 * }
 * }
 * </pre>
 *
 * @author Masumi Yano
 * @since 1.0
 */
public class SqlManager {

    /** The name of the properties file containing database connection details. */
    private static final String PROPERTIES_FILE = "db.properties";

    /** The singleton instance of the SqlManager class. */
    private static  SqlManager instance;

    /** The database connection object. */
    private Connection connection;

    /** The database URL. */
    private String url;

    /** The database user. */
    private String user;

    /** The database password. */
    private String password;

    /**
     * Private constructor to prevent instantiation from other classes.
     * Initializes the database connection by loading properties and setting up the database.
     */
    private SqlManager() {
        loadProperties();
        initializeDatabase();
    }

    /**
     * Returns the singleton instance of the SqlManager class.
     * If the instance does not exist, it creates a new one.
     *
     * @return the singleton instance of SqlManager
     */
    public final static SqlManager getInstance() {
        if (instance == null) {
            instance = new SqlManager();
        }
        return instance;
    }

    /**
     * Loads database connection properties from the properties file.
     */
    private final void loadProperties() {
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

    /**
     * Initializes the database by executing SQL scripts to set up initial tables and data.
     */
    private final void initializeDatabase() {
        try (Connection conn = getConnection()) {
            executeSqlFile(conn, "SRS/Triviamaze.sql");
            executeSqlFile(conn, "SRS/insertMultipleChoice.sql");
            executeSqlFile(conn, "SRS/insertShortAnswer.sql");
            executeSqlFile(conn, "SRS/insertTrueFalse.sql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a database connection. If the connection is closed or null, it establishes a new connection.
     *
     * @return the database connection
     * @throws SQLException if a database access error occurs
     */
    private final Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    /**
     * Executes SQL statements from a file.
     *
     * @param conn the database connection
     * @param filePath the path to the SQL file
     */
    private final void executeSqlFile(Connection conn, String filePath) {
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

    /**
     * Executes an update SQL statement with parameters.
     *
     * @param query the SQL query
     * @param params the parameters for the SQL query
     */
    public final void executeUpdate(String query, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            setParameters(pstmt, params);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a query SQL statement with parameters and returns the result set.
     *
     * @param query the SQL query
     * @param params the parameters for the SQL query
     * @return the result set of the query
     */
    public final ResultSet executeQuery(String query, Object... params) {
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

    /**
     * Sets the parameters for a prepared statement.
     *
     * @param pstmt the prepared statement
     * @param params the parameters to set
     * @throws SQLException if a database access error occurs
     */
    private final void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }

    /**
     * Executes a SQL statement to create a table.
     *
     * @param query the SQL query
     * @throws SQLException if a database access error occurs
     */
    public final void createTable(String query) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            int rv = stmt.executeUpdate(query);
            System.out.println("executedUpdate() returned " + rv);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Executes a SQL statement to insert data into a table.
     *
     * @param query the SQL query
     * @throws SQLException if a database access error occurs
     */
    public final void insertData(String query) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            int rv = stmt.executeUpdate(query);
            System.out.println("executedUpdate() returned " + rv);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Executes a SQL statement to display data from a table.
     *
     * @param query the SQL query
     * @throws SQLException if a database access error occurs
     */
    public final void displayData(String query) throws SQLException {
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
