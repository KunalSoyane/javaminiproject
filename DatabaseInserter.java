import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInserter {

    public static void main(String[] args) {

        // --- Database Connection Details ---
        String url = "jdbc:mysql://localhost:3306/Smart_vote";
        String user = "root";
        String password = "0001";

        // --- 1. The SQL INSERT Statement with Placeholders ---
        // The '?' will be replaced by the values we set later.
        String sql = "INSERT INTO employees (id, name, email) VALUES (?, ?, ?)";

        System.out.println("Connecting to the database to insert a new record...");

        // Use try-with-resources to automatically close the connection and statement
        try (Connection conn = DriverManager.getConnection(url, user, password);
             // --- 2. Create a PreparedStatement ---
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // --- 3. Set the values for the placeholders ---
            // The first parameter is the index of the '?' (starting from 1)
            pstmt.setInt(1, 11);                         // Set the value for the first '?'
            pstmt.setString(2, "ok");            // Set the value for the second '?'
            pstmt.setString(3, "ok@example.com"); // Set the value for the third '?'

            // --- 4. Execute the update ---
            // executeUpdate() returns the number of rows affected
            int rowsAffected = pstmt.executeUpdate();

            // Check if the insertion was successful
            if (rowsAffected > 0) {
                System.out.println("A new employee was inserted successfully!");
            }

        } catch (SQLException e) {
            // Handle potential errors, such as a duplicate ID
            System.err.println("Database insertion error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}