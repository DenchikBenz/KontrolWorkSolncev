package WeatherTask.servlet;
import WeatherTask.dao.impl.AuthAttemptDao;
import WeatherTask.dao.impl.UserDao;
import WeatherTask.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        UserDao userDao = new UserDao();
        AuthAttemptDao authAttemptDao = new AuthAttemptDao();
        this.authService = new AuthService(userDao, authAttemptDao);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean isAuthenticated = authService.login(username, password);

        if (isAuthenticated) {
            resp.sendRedirect("welcome.jsp");
        } else {
            req.setAttribute("errorMessage", "Invalid username or password.");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}