package WeatherTask.dao.impl;

import WeatherTask.model.impl.AuthAttempt;
import WeatherTask.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class AuthAttemptDao {

    public void saveAuthAttempt(AuthAttempt authAttempt) {
        String query = "INSERT INTO auth_attempts (username, success) VALUES (?, ?)";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, authAttempt.getUsername());
            ps.setBoolean(2, authAttempt.isSuccess());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}