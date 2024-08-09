package model;

import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * DatabaseConnector is a class responsible for setting up and managing the SQLite database
 * used in the TriviaMaze game. It creates tables, inserts initial data, and provides
 * methods for querying the database.
 */
public class DatabaseConnector implements Serializable {
    private final static long serialVersionUID = 7L;
    private final String myDS_url = "jdbc:sqlite:TriviaMaze.db";
    private transient SQLiteDataSource myDS;
    /**
     * Constructs a new DatabaseConnector, initializing the SQLiteDataSource
     * and setting up the database.
     */

    public DatabaseConnector() {
            myDS = new SQLiteDataSource();
            myDS.setUrl(myDS_url);
            initializeData();

    }
    /**
     * Returns the SQLiteDataSource used for database connections.
     *
     * @return the SQLiteDataSource object.
     */
    public SQLiteDataSource getDataSource() {
        return myDS;
    }
    /**
     * Initializes the database by creating tables and inserting initial data.
     */
    private void initializeData() {
        try (Connection conn = myDS.getConnection();
             Statement stmt = conn.createStatement()) {
            createTables(stmt);
            insertData(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Creates the necessary tables for storing questions in the database if they do not already exist.
     *
     * @param theStmt the SQL statement object used to execute SQL commands.
     * @throws SQLException if a database access error occurs.
     */
    private void createTables(Statement theStmt) throws SQLException {
        Statement stmt = theStmt;
        String createTFTable = "CREATE TABLE IF NOT EXISTS TrueFalse (" +
                "id INTEGER PRIMARY KEY," +
                "question TEXT NOT NULL," +
                "correct_answer BOOLEAN NOT NULL)";
        String createSATable = "CREATE TABLE IF NOT EXISTS ShortAnswer (" +
                "id INTEGER PRIMARY KEY," +
                "question TEXT NOT NULL," +
                "correct_answer TEXT NOT NULL)";
        String createMQTable = "CREATE TABLE IF NOT EXISTS MultipleQuestion (" +
                "id INTEGER PRIMARY KEY," +
                "question TEXT NOT NULL," +
                "correct_answer CHAR(1) NOT NULL)";
        String createMCTable = "CREATE TABLE IF NOT EXISTS MultipleChoice (" +
                "question_id INTEGER NOT NULL," +
                "choice CHAR(1) NOT NULL," +
                "choice_text TEXT NOT NULL," +
                "FOREIGN KEY (question_id) REFERENCES MultipleQuestion(id))";
        stmt.executeUpdate(createTFTable);
        stmt.executeUpdate(createSATable);
        stmt.executeUpdate(createMQTable);
        stmt.executeUpdate(createMCTable);
    }
    /**
     * Inserts initial data into the tables for questions in the database.
     *
     * @param theStmt the SQL statement object used to execute SQL commands.
     * @throws SQLException if a database access error occurs.
     */
    private void insertData(Statement theStmt) throws SQLException {
        Statement stmt = theStmt;
        String insertTF = "INSERT INTO TrueFalse (id, question, correct_answer) VALUES " +
                "(1, 'Harry Potter is a Hufflepuff.', 0), " +
                "(2, 'Darth Vader is Luke Skywalkers father.', 1), " +
                "(3, 'SpongeBob SquarePants lives in a pineapple.', 1), " +
                "(4, 'Bugs Bunny is a rabbit from the Looney Tunes show.', 1), " +
                "(5, 'Monkey D. Luffy is the main resources.character of One Piece.', 1), " +
                "(6, 'Woody is a toy cowboy from Toy Story.', 1), " +
                "(7, 'Hermione Granger became a headmaster of Hogwarts.', 0), " +
                "(8, 'Yoda trained Luke Skywalker.', 1), " +
                "(9, 'Patrick Star is SpongeBobs best friend.', 1), " +
                "(10, 'Daffy Duck is a resources.character from One Piece.', 0), " +
                "(11, 'Nami is a navigator in One Piece.', 1), " +
                "(12, 'Buzz Lightyear believes he is a real space ranger.', 1), " +
                "(13, 'Draco Malfoy is a Gryffindor.', 0), " +
                "(14, 'Han Solo was frozen in carbonite.', 1), " +
                "(15, 'Sandy Cheeks is a squirrel.', 1), " +
                "(16, 'Porky Pig is a resources.character from the Looney Tunes show.', 1), " +
                "(17, 'Roronoa Zoro is a chef in One Piece.', 0), " +
                "(18, 'Jessie is a cowgirl from Toy Story.', 1), " +
                "(19, 'Albus Dumbledore was a headmaster of Hogwarts.', 1), " +
                "(20, 'Princess Leia is a Sith.', 0) ";
        String insertSA = "INSERT INTO ShortAnswer (id, question, correct_answer) VALUES " +
                "(21, 'What is Harry Potters owl name?', 'Hedwig'), " +
                "(22, 'Who is Luke Skywalkers sister?', 'Leia'), " +
                "(23, 'What kind of pet does SpongeBob have?', 'Snail'), " +
                "(24, 'Who is Bugs Bunnys roommate?', 'Daffy Duck'), " +
                "(25, 'What is the name of Luffys pirate crew?', 'Straw Hat Pirates'), " +
                "(26, 'What is the name of the toy cowboy in Toy Story?', 'Woody'), " +
                "(27, 'What is the name of Hermiones cat?', 'Crookshanks'), " +
                "(28, 'Who trained Anakin Skywalker?', 'Obi-Wan Kenobi'), " +
                "(29, 'What is the name of SpongeBobs boss?', 'Mr. Krabs'), " +
                "(30, 'What is the Tasmanian Devils name in looney tunes?', 'Taz'), " +
                "(31, 'What is scabbers real identity?', 'Peter Pettigrew' ), " +
                "(32, 'What is the name of Buzz Lightyears arch-nemesis?', 'Emperor Zurg'), " +
                "(33, 'What is Ron Weasleys pet rats name?', 'Scabbers'), " +
                "(34, 'What planet is Chewbacca from?', 'Kashyyyk'), " +
                "(35, 'Who is SpongeBobs driving instructor?', 'Mrs. Puff'), " +
                "(36, 'What is the name of Tweetys owner?', 'Granny'), " +
                "(37, 'What is the name of the doctor in the strawhats crew?', 'Tony Tony Chopper'), " +
                "(38, 'What is the name of Woodys horse?', 'Bullseye'), " +
                "(39, 'What is the name of Voldemorts snake?', 'Nagini'), " +
                "(40, 'Who is Darth Vaders master?', 'Emperor Palpatine')";
        String insertMQ = "INSERT INTO MultipleQuestion (id, question, correct_answer) VALUES " +
                "(41, 'What house is Harry Potter sorted into?', 'A'), " +
                "(42, 'What color is Yodas lightsaber?', 'B'), " +
                "(43, 'What instrument does Squidward play?', 'C'), " +
                "(44, 'What is Bugs Bunnys catchphrase?', 'A'), " +
                "(45, 'What is Luffys dream?', 'A'), "+
                "(46, 'Who is Woodys best friend in Toy Story?', 'B'), " +
                "(47, 'What is the name of the potion master at Hogwarts?', 'C'), " +
                "(48, 'What planet is Luke Skywalker from?', 'D'), "+
                "(49, 'What is SpongeBobs favorite activity?', 'A'), " +
                "(50, 'What is Daffy Ducks signature phrase?', 'B'), " +
                "(51, 'What is Zoros goal in One Piece?', 'C'), " +
                "(52, 'What is Buzz Lightyears catchphrase?', 'D'), " +
                "(53, 'What is the name of Gryffindors ghost?', 'A'), " +
                "(54, 'What is the name of Han Solos ship?', 'B'), " +
                "(55, 'What is the name of SpongeBobs snail?', 'C'), " +
                "(56, 'Who is the main antagonist in the Looney Tunes show?', 'D'), " +
                "(57, 'What is the name of Luffys brother?', 'A'), " +
                "(58, 'What is the name of the space ranger in Toy Story?', 'B'), " +
                "(59, 'What is Hermiones last name?', 'C'), " +
                "(60, 'What is the name of the robot in Star Wars?', 'A')";

        String insertMC = "INSERT INTO MultipleChoice (question_id, choice, choice_text) VALUES " +
                "(41, 'A', 'Gryffindor'), (41, 'B', 'Hufflepuff'), (41, 'C', 'Ravenclaw'), (41, 'D', 'Slytherin'), " +
                "(42, 'A', 'Red'), (42, 'B', 'Green'), (42, 'C', 'Blue'), (42, 'D', 'Purple'), " +
                "(43, 'A', 'Guitar'), (43, 'B', 'Trumpet'), (43, 'C', 'Clarinet'), (43, 'D', 'Piano'), " +
                "(44, 'A', 'Whats up, Doc?'), (44, 'B', 'Thats all folks!'), (44, 'C', 'Eh, whats cookin'), (44, 'D', 'Whats going on?'), " +
                "(45, 'A', 'To become the Pirate King'), (45, 'B', 'To find the One Piece'), (45, 'C', 'To become a swordsman'), (45, 'D', 'To explore the world'), " +
                "(46, 'A', 'Buzz Lightyear'), (46, 'B', 'Slinky Dog'), (46, 'C', 'Mr. Potato Head'), (46, 'D', 'Rex'), " +
                "(47, 'A', 'Minerva McGonagall'), (47, 'B', 'Albus Dumbledore'), (47, 'C', 'Severus Snape'), (47, 'D', 'Remus Lupin'), " +
                "(48, 'A', 'Naboo'), (48, 'B', 'Endor'), (48, 'C', 'Coruscant'), (48, 'D', 'Tatooine'), " +
                "(49, 'A', 'Jellyfishing'), (49, 'B', 'Cooking'), (49, 'C', 'Cleaning'), (49, 'D', 'Sleeping'), " +
                "(50, 'A', 'Youre despicable!'), (50, 'B', 'Youre dethpicable!'), (50, 'C', 'Thats all folks!'), (50, 'D', 'Whats up, Doc?'), " +
                "(51, 'A', 'To become the Pirate King'), (51, 'B', 'To find the One Piece'), (51, 'C', 'To become the worlds greatest swordsman'), (51, 'D', 'To explore the world'), " +
                "(52, 'A', 'Prepare for trouble!'), (52, 'B', 'Reach for the sky!'), (52, 'C', 'I am Buzz Lightyear!'), (52, 'D', 'To infinity, and beyond!'), " +
                "(53, 'A', 'Nearly Headless Nick'), (53, 'B', 'Moaning Myrtle'), (53, 'C', 'The Grey Lady'), (53, 'D', 'The Bloody Baron'), " +
                "(54, 'A', 'X-wing'), (54, 'B', 'Millennium Falcon'), (54, 'C', 'TIE Fighter'), (54, 'D', 'Star Destroyer'), " +
                "(55, 'A', 'Rex'), (55, 'B', 'Sheldon'), (55, 'C', 'Gary'), (55, 'D', 'Larry'), " +
                "(56, 'A', 'Elmer Fudd'), (56, 'B', 'Yosemite Sam'), (56, 'C', 'Sylvester the Cat'), (56, 'D', 'Marvin the Martian'), " +
                "(57, 'A', 'Ace'), (57, 'B', 'Zoro'), (57, 'C', 'Shanks'), (57, 'D', 'Sanji'), " +
                "(58, 'A', 'Woody'), (58, 'B', 'Buzz Lightyear'), (58, 'C', 'Rex'), (58, 'D', 'Mr. Potato Head'), " +
                "(59, 'A', 'Jean'), (59, 'B', 'Jane'), (59, 'C', 'Granger'), (59, 'D', 'Lily'), " +
                "(60, 'A', 'R2-D2'), (60, 'B', 'C-3PO'), (60, 'C', 'BB-8'), (60, 'D', 'K-2SO')";

        stmt.executeUpdate(insertTF);
        stmt.executeUpdate(insertSA);
        stmt.executeUpdate(insertMQ);
        stmt.executeUpdate(insertMC);
    }
    /**
     * Queries the database for all questions and displays them.
     */
    public void queryQes () {
        String query = "SELECT * FROM TrueFalse UNION ALL SELECT * FROM ShortAnswer UNION ALL SELECT * FROM MultipleQuestion";
        try (Connection conn = myDS.getConnection();
            Statement stmt = conn.createStatement(); ) {
            ResultSet rs  = stmt.executeQuery(query);

            while (rs.next()) {
                String question = rs.getString("question");
                String answer  = rs.getString("correct_answer");
                //System.out.println("Question: " + question + " - Answer: " + answer);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
