package org.example;
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

/**
 * Handles the submission of a user's vote.
 * This servlet uses a database transaction to ensure data integrity.
 */
@WebServlet("/vote")
public class VoteServlet extends HttpServlet {

    // --- Database Connection Details ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Smart_vote";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0001"; // Replace with your password

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC Driver not found", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // 'false' means don't create a new session

        // 1. --- Security Check: Ensure user is logged in ---
        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect("login.html?error=session");
            return;
        }

        String userEmail = (String) session.getAttribute("userEmail");
        String votedForParty = request.getParameter("party"); // The party name from the form

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // --- Start of Transaction ---
            conn.setAutoCommit(false);

            // 2. --- Check if user has already voted ---
            int Id = -1;
            boolean hasVoted = false;
            String checkVoteStatusSql = "SELECT id, has_voted FROM users WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkVoteStatusSql)) {
                checkStmt.setString(1, userEmail);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        Id = rs.getInt("id");
                        hasVoted = rs.getBoolean("has_voted");
                    } else {
                        throw new ServletException("User not found in database.");
                    }
                }
            }

            if (hasVoted) {
                // User has already voted, so we don't proceed.
                conn.rollback(); // End the transaction
                response.sendRedirect("dashboard.jsp?error=alreadyvoted");
                return;
            }

            // 3. --- Record the anonymous vote in the 'votes' table ---
            String insertVoteSql = "INSERT INTO votes (voted_for_party) VALUES (?)";
            try (PreparedStatement insertVoteStmt = conn.prepareStatement(insertVoteSql)) {
                insertVoteStmt.setString(1, votedForParty);
                insertVoteStmt.executeUpdate();
            }

            // 4. --- Mark the user as 'voted' in the 'users' table ---
            String updateUserSql = "UPDATE users SET has_voted = TRUE WHERE id = ?";
            try (PreparedStatement updateUserStmt = conn.prepareStatement(updateUserSql)) {
                updateUserStmt.setInt(1, Id);
                updateUserStmt.executeUpdate();
            }

            // --- Commit Transaction ---
            // If both operations succeed, commit the changes to the database.
            conn.commit();

            // 5. --- Redirect to a 'thank you' page ---
            response.sendRedirect("thankyou.html");

        } catch (SQLException e) {
            // If any database error occurs, roll back all changes.
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new ServletException("Database transaction failed.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset to default behavior
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}