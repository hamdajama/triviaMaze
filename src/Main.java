import model.DatabaseConnector;
import view.GUI;

import java.sql.SQLException;

public final class Main {

    static DatabaseConnector myDB = new  DatabaseConnector();

    public static void main(final String[] theArgs) throws SQLException {
        new GUI(myDB);
    }
}
