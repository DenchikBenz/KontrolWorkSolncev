package WeatherTask.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseUtils {

    private DatabaseUtils() {}

    private static Connection connection;


    public static Connection getConnection() {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/cities",
                        "danilbezin",
                        "postgres"
                );
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        return connection;
    }

}