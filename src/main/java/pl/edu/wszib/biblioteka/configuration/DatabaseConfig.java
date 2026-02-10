package pl.edu.wszib.biblioteka.configuration;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@Component
public class DatabaseConfig {
    private static final String URL = "jdbc:h2:file:./library_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void initDatabase() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("schema.sql");
            
            if (inputStream != null) {
                String sql = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
                
                statement.execute(sql);
                System.out.println("Database initialized successfully.");
            } else {
                System.err.println("schema.sql not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
