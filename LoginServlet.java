import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Import the BCrypt library for password verification
import org.mindrot.jbcrypt.BCrypt;

/**
 * This servlet handles the user login process.
 * It verifies user credentials against the database using secure practices.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Smart_vote";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    /**
     * Loads the MySQL JDBC driver when the servlet is first initialized.
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
     * Handles the HTTP POST request sent from the login form.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // The SQL query to find a user by their email address
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                // 1. Check if a user with the given email exists
                if (rs.next()) {
                    // User found, retrieve the hashed password from the database
                    String hashedPasswordFromDB = rs.getString("password");

                    // 2. Securely verify the submitted password against the stored hash
                    if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
                        // --- SUCCESSFUL LOGIN ---

                        // 3. Create a new session to keep the user logged in
                        HttpSession session = request.getSession();
                        session.setAttribute("userEmail", email); // Store user's email in the session

                        // Redirect to the user's dashboard
                        response.sendRedirect("dashboard.jsp");
                    } else {
                        // --- FAILED LOGIN: Incorrect Password ---
                        response.sendRedirect("login.html?error=1");
                    }
                } else {
                    // --- FAILED LOGIN: User Not Found ---
                    response.sendRedirect("login.html?error=1");
                }
            }
        } catch (SQLException e) {
            // Handle any database errors
            throw new ServletException("Database error during login", e);
        }
    }

}
