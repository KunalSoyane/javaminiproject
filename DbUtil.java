package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_vote_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0001";

    public static Connection getConnection() throws SQLException {
        try {
            // This line is only needed for older JDBC drivers, but is good practice.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
