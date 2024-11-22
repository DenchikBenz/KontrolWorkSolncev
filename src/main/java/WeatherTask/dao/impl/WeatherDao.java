package WeatherTask.dao.impl;

import WeatherTask.utils.DatabaseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WeatherDao {
    public void saveWeatherRequest(String city, String temperature, String description) {
        String query = "INSERT INTO weather_requests (city, temperature, description) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, city);
            ps.setString(2, temperature);
            ps.setString(3, description);

            int rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

