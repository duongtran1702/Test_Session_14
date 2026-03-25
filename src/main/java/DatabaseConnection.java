import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static class Helper {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }

    private DatabaseConnection() {
    }

    public static DatabaseConnection getInstance() {
        return Helper.INSTANCE;
    }

    private static final String url = "jdbc:mysql://localhost:3306/db_account";
    private static final String user = "root";
    private static final String password = "Duong170226@";


    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.err.println("Cannot connect to database: " + url);
            throw e;
        }
    }
}
