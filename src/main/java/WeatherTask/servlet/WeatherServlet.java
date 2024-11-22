package WeatherTask.servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import WeatherTask.dao.impl.WeatherDao;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {
    private static final String API_KEY = "bd5e378503939ddaee76f12ad7a97608";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final  WeatherDao weatherDao = new WeatherDao();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String city = req.getParameter("city");
        Map<String, String> weatherData = getWeatherData(city);

        if (weatherData != null) {
            weatherDao.saveWeatherRequest(
                    weatherData.get("city"),
                    weatherData.get("temperature"),
                    weatherData.get("description")
            );
            req.setAttribute("weatherData", weatherData);
        } else {
            req.setAttribute("errorMessage", "Unable to retrieve weather data. Please try again.");
        }
        req.getRequestDispatcher("welcome.jsp").forward(req, resp);
    }

    private Map<String, String> getWeatherData(String city) {
        try {
            String urlString = API_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric&lang=ru";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();

                String temperature = jsonResponse.getAsJsonObject("main").get("temp").getAsString();
                String description = jsonResponse.getAsJsonArray("weather").get(0)
                        .getAsJsonObject().get("description").getAsString();

                Map<String, String> weatherData = new HashMap<>();
                weatherData.put("city", city);
                weatherData.put("temperature", temperature);
                weatherData.put("description", description);
                return weatherData;
            } else {
                System.err.println("Error: Response code " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

