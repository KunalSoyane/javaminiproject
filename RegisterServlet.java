package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // 1. Get all the parameters from the form
        String name = request.getParameter("name");
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("pass");
        String confirmPassword = request.getParameter("cpass");
        String address = request.getParameter("add");
        String role = request.getParameter("role");

        // --> ADD THESE LINES TO CHECK THE VALUES <--
        System.out.println("--- New Registration Received ---");
        System.out.println("Name: " + name);
        System.out.println("Mobile: " + mobile);
        System.out.println("Password: " + password);
        System.out.println("Address: " + address);
        System.out.println("Role: " + role);
        System.out.println("---------------------------------");

        // 2. Basic Validation: Check if passwords match
        if (!password.equals(confirmPassword)) {
            out.println("<h2>Error: Passwords do not match!</h2>");
            out.println("<a href='index.html'>Try Again</a>");
            return; // Stop further execution
        }

        // 3. Use a try-with-resources block for database connection
        try (Connection conn = DbUtil.getConnection()) {
            String sql = "INSERT INTO users (name, mobile, password, address, role) VALUES (?, ?, ?, ?, ?)";

            // Using PreparedStatement to prevent SQL injection attacks
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, mobile);
            stmt.setString(3, password); // Note: In a real app, you should HASH the password!
            stmt.setString(4, address);
            stmt.setString(5, role);

            // 4. Execute the query
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Success
                out.println("<html><body>");
                out.println("<h2>Registration Successful! ✅</h2>");
                out.println("<p>You can now log in.</p>");
                out.println("<a href='loginpage.html'>Go to Login Page</a>");
                out.println("</body></html>");
            } else {
                // Failure
                out.println("<h2>Registration Failed. Please try again.</h2>");
            }

        } catch (SQLException e) {
            // Handle potential duplicate mobile number error
            if (e.getSQLState().equals("23000")) { // '23000' is the SQLSTATE for integrity constraint violation
                out.println("<h2>Error: A user with this mobile number already exists!</h2>");
                out.println("<a href='index.html'>Try Again</a>");
            } else {
                out.println("<h2>Database Error: " + e.getMessage() + "</h2>");
            }
            e.printStackTrace();
        }
    }
}
