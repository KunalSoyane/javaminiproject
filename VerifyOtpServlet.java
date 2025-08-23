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
import java.sql.SQLException;

@WebServlet("/verify-otp")
public class VerifyOtpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String submittedOtp = request.getParameter("otp");

        // Retrieve the data stored in the session
        String storedOtp = (String) session.getAttribute("signupOtp");
        String email = (String) session.getAttribute("signupEmail");
        String hashedPassword = (String) session.getAttribute("signupHashedPassword");

        // 1. Check if the submitted OTP matches the stored OTP
        if (submittedOtp != null && submittedOtp.equals(storedOtp)) {
            // OTP is correct, proceed to save user to the database
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Smart_vote", "root", "");
                 PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (email, password) VALUES (?, ?)")) {

                pstmt.setString(1, email);
                pstmt.setString(2, hashedPassword);
                pstmt.executeUpdate();

                // Clear the temporary session attributes
                session.removeAttribute("signupOtp");
                session.removeAttribute("signupEmail");
                session.removeAttribute("signupHashedPassword");

                // Redirect to the login page with a success message
                response.sendRedirect("login.html?registration=success");

            } catch (SQLException e) {
                throw new ServletException("Database error during final registration.", e);
            }
        } else {
            // OTP is incorrect, redirect back to the verification page with an error
            response.sendRedirect("verify-otp.jsp?error=1");
        }
    }

}
