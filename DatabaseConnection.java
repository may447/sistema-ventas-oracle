package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "Mayra";
    private static final String PASSWORD = "Mayra2007";
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println(" Conexión establecida con Oracle.");
            }
        } catch (SQLException e) {
            System.err.println(" Error de conexión: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println(" Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println(" Error al cerrar conexión: " + e.getMessage());
        }
    }
}

