package WeatherTask.model.impl;

public class AuthAttempt {
    private int id;
    private String username;
    private boolean success;

    public AuthAttempt(int id, String username, boolean success) {
        this.id = id;
        this.username = username;
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
