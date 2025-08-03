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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String mobile = request.getParameter("mobile");
        String password = request.getParameter("pass");

        try (Connection conn = DbUtil.getConnection()) {
            String sql = "SELECT * FROM users WHERE mobile = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mobile);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // --- Login Successful ---
                // Create a session and store user info
                HttpSession session = request.getSession();
                session.setAttribute("userName", rs.getString("name"));
                session.setAttribute("userRole", rs.getString("role"));

                // Redirect to a dashboard page
                response.sendRedirect("dashboard.html");
            } else {
                // --- Login Failed ---
                out.println("<h2>Error: Invalid credentials.</h2>");
                out.println("<p>Please check your mobile number and password.</p>");
                out.println("<a href='loginpage.html'>Try Again</a>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h2>Database Error: " + e.getMessage() + "</h2>");
        }
    }
}