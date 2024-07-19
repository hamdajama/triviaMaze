import view.GUI;
import sqlManager.SqlManager;

public final class Main {
    public static void main(final String[] theArgs) {
        new GUI();
        SqlManager  sqlManager = SqlManager.getInstance();
    }
}