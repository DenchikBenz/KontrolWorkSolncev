<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <title>Welcome</title>
</head>
<body>

<h2>Check Weather</h2>
<form action="weather" method="post">
    <label for="city">Enter City Name:</label>
    <input type="text" id="city" name="city" required><br><br>
    <button type="submit">Get Weather</button>
</form>
<c:if test="${not empty weatherData}">
    <h2>Weather in ${weatherData.city}:</h2>
    <p>Temperature: ${weatherData.temperature}°C</p>
    <p>Description: ${weatherData.description}</p>
</c:if>

<p><a href="index.jsp">Log out</a></p>
</body>
</html>