import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement; // Changed from Statement
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabasePrinter {

    public static void main(String[] args) {

        // --- 1. Database Connection Details (Best to load from a properties file) ---
        String url = "jdbc:mysql://localhost:3306/Smart_vote";
        String user = "root";
        String password = "";

        // --- 2. SQL Query ---
        // Using '?' as a placeholder is how you would add parameters
        // Example: "SELECT id, name, email FROM employees WHERE id = ?"
        String sqlQuery = "SELECT id, name, email FROM employees";

        System.out.println("Connecting to database and fetching data...");

        // --- 3. Try-with-resources with PreparedStatement ---
        try (Connection conn = DriverManager.getConnection(url, user, password);

             // Use PreparedStatement instead of Statement
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {


            System.out.println("--- Employee Data ---");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email);
            }
            System.out.println("---------------------");

        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
