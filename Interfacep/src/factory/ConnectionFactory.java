package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost:3306/bdDeepCode";
    private static final String USER = "root";
    private static final String PASSWORD = "32001josue@A"; //Para cada usuário haverá uma senha distinta.

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erro na conexão com o banco: " + e.getMessage());
            throw new RuntimeException("Erro na conexão com o banco", e);
        }
    }
}