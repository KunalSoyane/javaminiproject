import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // <-- New import for session management
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * This servlet handles the FIRST step of new user registration.
 * It generates an OTP, sends it via email, and stores user data in the session
 * pending verification. It does NOT save the user to the database directly.
 */
@WebServlet("/Signin")
public class SigninServlet extends HttpServlet {

    // Database connection details (still needed for other servlets, but not used here)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Smart_vote";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0001";

    /**
     * Loads the MySQL JDBC driver when the servlet is first loaded.
     */
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Handles the HTTP POST request to start the registration and OTP process.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 1. Generate a 6-digit OTP
        String otp = String.format("%06d", new java.util.Random().nextInt(999999));

        // 2. Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // 3. Store user data and OTP temporarily in the session
        HttpSession session = request.getSession();
        session.setAttribute("signupEmail", email);
        session.setAttribute("signupHashedPassword", hashedPassword);
        session.setAttribute("signupOtp", otp);
        // Set a timeout for the session data (e.g., 10 minutes)
        session.setMaxInactiveInterval(10 * 60);

        try {
            // 4. Send the OTP email using your EmailService utility class
            EmailService.sendOtpEmail(email, otp);

            // 5. Redirect to the OTP verification page
            response.sendRedirect("verify-otp.jsp");

        } catch (Exception e) {
            // Handle any failure during email sending
            System.err.println("Failed to send OTP email for: " + email);
            e.printStackTrace();
            throw new ServletException("Error sending OTP email.", e);
        }
    }
}