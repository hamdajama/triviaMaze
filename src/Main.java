import view.GUI;
import controller.SqlManager;

public final class Main {
    public static void main(final String[] theArgs) {
        new GUI();
        SqlManager  sqlManager = SqlManager.getInstance();
    }
}
