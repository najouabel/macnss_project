package macnss.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private DatabaseConnection() {
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/macnss", "postgres", "najoua"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception for error handling
        }
    }

    public static void closeConnection(Connection connection) {
    }
}
