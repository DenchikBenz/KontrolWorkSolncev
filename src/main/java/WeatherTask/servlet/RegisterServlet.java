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
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
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
        boolean isRegistered = authService.register(username, password);

        if (isRegistered) {
            resp.sendRedirect("index.jsp");
        } else {
            req.setAttribute("errorMessage", "Username is already taken. Please choose another.");
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
        }
    }
}
