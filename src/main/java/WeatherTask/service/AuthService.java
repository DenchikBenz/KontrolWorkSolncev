package WeatherTask.service;
import WeatherTask.dao.impl.AuthAttemptDao;
import WeatherTask.dao.impl.UserDao;
import WeatherTask.model.impl.AuthAttempt;
import WeatherTask.model.impl.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {
    private final UserDao userDao;
    private final AuthAttemptDao authAttemptDao;

    public AuthService(UserDao userDao, AuthAttemptDao authAttemptDao) {
        this.userDao = userDao;
        this.authAttemptDao = authAttemptDao;
    }

    public boolean register(String username, String password) {
        username = username.trim().toLowerCase();
        if (userDao.isUsernameTaken(username)) {
            return false;
        }
        String hashedPassword = hashPassword(password);
        User user = new User(0, username, hashedPassword);
        return userDao.registerUser(user);
    }

    public boolean login(String username, String password) {
        User user = userDao.findUserByUsername(username);
        boolean success = user != null && user.getPasswordHash().equals(hashPassword(password));
        authAttemptDao.saveAuthAttempt(new AuthAttempt(0, username, success));
        return success;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}