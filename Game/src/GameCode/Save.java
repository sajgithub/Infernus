package PaooGame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Save {

    public void database() {
        createTable();
    }

    // Method to create the table if it doesn't already exist
    private static void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Save3 " +
                    "(col1 INT NOT NULL, " +
                    " col2 INT NOT NULL,"+
                    " col3 INT NOT NULL,"+
                    " col4 INT NOT NULL,"+
                    " col5 INT NOT NULL)";
            stmt.execute(sql);
            System.out.println("Table created successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    // Method to insert data into the table
    public static void insertData(int col1,int col2,int col3, int col4,int col5) {
        Connection c = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            String sql = "INSERT INTO Save3(col1, col2,col3,col4,col5) VALUES (?, ?, ?, ?, ?)";
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, col1);
            pstmt.setInt(2, col2);
            pstmt.setInt(3, col3);
            pstmt.setInt(4, col4);
            pstmt.setInt(5, col5);
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    // Method to get all data from the table
    public static List<String[]> getData() {
        List<String[]> data = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Save3");

            while (rs.next()) {
                int col1 = rs.getInt("col1");
                int col2 = rs.getInt("col2");
                int col3 = rs.getInt("col3");
                int col4 = rs.getInt("col4");
                int col5 = rs.getInt("col5");
                String[] row = new String[] {
                        String.valueOf(col1),
                        String.valueOf(col2),
                        String.valueOf(col3),
                        String.valueOf(col4),
                        String.valueOf(col5),
                };
                System.out.println("Retrieved row: " + Arrays.toString(row)); // Log retrieved row
                data.add(row);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return data;
    }
    public static int converterData(){
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        int data=0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Save3");

            while (rs.next()) {
                data = rs.getInt("col1");
                int col2 = rs.getInt("col2");
                int col3 = rs.getInt("col3");
                int col4 = rs.getInt("col4");
                int col5 = rs.getInt("col5");
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return data;
    }
}
