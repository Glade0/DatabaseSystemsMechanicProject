import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=DBProject;encrypt=true;trustServerCertificate=true";
    private static final String USER = "TestUser"; // Replace with your SQL Server username
    private static final String PASSWORD = "m"; // Replace with your SQL Server password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("✅ Successfully connected to the database!");

            // --- Create a Table (if it doesn't exist) ---
            String createTableSQL = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'MyTestTable') " +
                    "BEGIN " +
                    "CREATE TABLE MyTestTable ( " +
                    "ID INT PRIMARY KEY, " +
                    "Name VARCHAR(255) " +
                    "); " +
                    "END";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTableSQL);
                System.out.println("✅ Table created (if it didn't exist)!");
            } catch (SQLException e) {
                System.err.println("❌ Error creating table:");
                e.printStackTrace();
            }

            // --- Insert Data into the Table ---
            String insertSQL = "INSERT INTO MyTestTable (ID, Name) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setInt(1, 1);
                preparedStatement.setString(2, "Test Data");
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✅ Data inserted successfully!");
                } else {
                    System.out.println("❌ Failed to insert data.");
                }
            } catch (SQLException e) {
                System.err.println("❌ Error inserting data:");
                e.printStackTrace();
            }

            // --- Update the Name in the Table ---
            String updateSQL = "UPDATE MyTestTable SET Name = ? WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                preparedStatement.setString(1, "Updated Data"); // The new name
                preparedStatement.setInt(2, 1); // The ID of the row to update
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✅ Data updated successfully!");
                } else {
                    System.out.println("❌ Failed to update data.");
                }
            } catch (SQLException e) {
                System.err.println("❌ Error updating data:");
                e.printStackTrace();
            }

            // --- Query the Table to Verify ---
            String selectSQL = "SELECT * FROM MyTestTable";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSQL)) {
                System.out.println("--- Data in MyTestTable ---");
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    System.out.println("ID: " + id + ", Name: " + name);
                }
            } catch (SQLException e) {
                System.err.println("❌ Error querying data:");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to the database:");
            e.printStackTrace();
        }
    }
}
