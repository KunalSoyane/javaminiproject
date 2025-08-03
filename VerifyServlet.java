package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/verify-otp")
public class VerifyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("userDetails");
        String generatedOtp = (String) session.getAttribute("generatedOtp");
        String submittedOtp = request.getParameter("otp");

        if (generatedOtp != null && generatedOtp.equals(submittedOtp)) {
            // Correct SQL with all 5 columns
            String sql = "INSERT INTO users (name, mobile, password, address, role) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DbUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, user.getName());
                stmt.setString(2, user.getMobile());
                stmt.setString(3, user.getPassword());
                // Add the missing lines for address and role
                stmt.setString(4, user.getAddress());
                stmt.setString(5, user.getRole());

                stmt.executeUpdate();

                out.println("<html><body><h2>Registration and Verification Successful! ✅</h2>");
                out.println("<p>Your account has been created. You can now log in.</p>");
                out.println("<a href='loginpage.html'>Go to Login Page</a></body></html>");

                session.invalidate();

            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<h2>Database Error: Could not save user data.</h2>");
            }
        } else {
            out.println("<h2>Error: Invalid OTP.</h2><a href='verify.html'>Try Again</a>");
        }
    }
}