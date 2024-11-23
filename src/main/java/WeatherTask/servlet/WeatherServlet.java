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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {
    private static final String API_KEY = "bd5e378503939ddaee76f12ad7a97608";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final WeatherDao weatherDao = new WeatherDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String city = req.getParameter("city");
        Map<String, String> weatherData = getWeatherData(city);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (weatherData != null) {
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(weatherData);
            System.out.println("Response: " + jsonResponse);
            resp.getWriter().write(jsonResponse);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Unable to retrieve weather data.\"}");
        }
    }

    private Map<String, String> getWeatherData(String city) {
        try {
            String urlString = API_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric&lang=ru";
            System.out.println("API URL: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream(), "UTF-8");
                JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();
                System.out.println("JSON Response: " + jsonResponse);

                if (jsonResponse.has("main") && jsonResponse.getAsJsonObject("main").has("temp") &&
                        jsonResponse.has("weather") && jsonResponse.getAsJsonArray("weather").size() > 0) {

                    String temperature = jsonResponse.getAsJsonObject("main").get("temp").getAsString();
                    String description = jsonResponse.getAsJsonArray("weather").get(0)
                            .getAsJsonObject().get("description").getAsString();

                    Map<String, String> weatherData = new HashMap<>();
                    weatherData.put("city", city);
                    weatherData.put("temperature", temperature);
                    weatherData.put("description", description);

                    System.out.println("Parsed weather data: " + weatherData);
                    return weatherData;
                } else {
                    System.err.println("Invalid JSON structure: " + jsonResponse);
                }
            } else {
                System.err.println("Error: Response code " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
