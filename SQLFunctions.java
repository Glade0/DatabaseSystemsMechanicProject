import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLFunctions 
{

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=MechanicShopProject;encrypt=true;trustServerCertificate=true";
    private static final String USER = "Mechanic";
    private static final String PASSWORD = "m";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    private static ResultSet executeQuery(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }

    private static int executeUpdate(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeUpdate();
    }

// Function to add a customer
public static void addCustomer(Connection connection, String firstName, String lastName, String phoneNumber) throws SQLException { // Modified parameters
    String sql = "INSERT INTO Customers (FirstName, LastName, PhoneNumber) VALUES (?, ?, ?)"; // Modified SQL
    executeUpdate(connection, sql, firstName, lastName, phoneNumber); // Modified executeUpdate
    }

    // Function to add a car
    public static void addCar(Connection connection, String make, String model, String licensePlate) throws SQLException {
        int makeModelId = getOrAddMakeModel(connection, make, model);
        String sql = "INSERT INTO Cars (MakeModel_ID, LicensePlate) VALUES (?, ?)";
        executeUpdate(connection, sql, makeModelId, licensePlate);
    }

    // Helper function to get or add MakeModel
    private static int getOrAddMakeModel(Connection connection, String make, String model) throws SQLException {
        String selectSql = "SELECT MM_ID FROM MakeModel WHERE Make = ? AND Model = ?";
        ResultSet resultSet = executeQuery(connection, selectSql, make, model);
        if (resultSet.next()) {
            return resultSet.getInt("MM_ID");
        } else {
            String insertSql = "INSERT INTO MakeModel (Make, Model) VALUES (?, ?)";
            executeUpdate(connection, insertSql, make, model);
            // Retrieve the generated ID
            resultSet = executeQuery(connection, selectSql, make, model);
            resultSet.next();
            return resultSet.getInt("MM_ID");
        }
    }

    // Function to add a technician
    public static void addTechnician(Connection connection, String firstName, String lastName) throws SQLException {
        String sql = "INSERT INTO Technicians (FirstName, LastName) VALUES (?, ?)";
        executeUpdate(connection, sql, firstName, lastName);
    }

  // Function to add a service
  public static void addService(Connection connection, String serviceName, double cost) throws SQLException {
    String sql = "INSERT INTO Services (ServiceName, Cost) VALUES (?, ?)";
    executeUpdate(connection, sql, serviceName, cost);
    }
    // Function to get a customer by ID
    public static Customer getCustomer(Connection connection, int customerId) throws SQLException {
        String sql = "SELECT FirstName, LastName, PhoneNumber FROM Customers WHERE CUST_ID = ?";
        ResultSet resultSet = executeQuery(connection, sql, customerId);
        if (resultSet.next()) {
            return new Customer(
                    resultSet.getString("FirstName"),
                    resultSet.getString("LastName"),
                    resultSet.getString("PhoneNumber")
            );
        }
        return null; // Or throw an exception if appropriate
    }

    public static int getCustomerId(Connection connection, String firstName, String lastName) throws SQLException {
        String sql = "SELECT id FROM Customers WHERE FirstName = ? AND LastName = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        ResultSet resultSet = preparedStatement.executeQuery();
       
      
        if (resultSet.next()) {
        return resultSet.getInt("id");
        } else {
        throw new SQLException("Customer not found: " + firstName + " " + lastName);
        }
        }
        public static void addCarToCustomer(Connection connection, int customerId, int carId) throws SQLException {
            String sql = "INSERT INTO CustomerCars (CustomerID, CarID) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, carId);
            preparedStatement.executeUpdate();
            }

    // Function to get a car by ID
    public static Car getCar(Connection connection, int carId) throws SQLException {
        String sql = "SELECT MakeModel_ID, LicensePlate FROM Cars WHERE CAR_ID = ?";
        ResultSet resultSet = executeQuery(connection, sql, carId);
        if (resultSet.next()) {
            return new Car(
                    resultSet.getInt("MakeModel_ID"),
                    resultSet.getString("LicensePlate")
            );
        }
        return null;
    }

    // Function to get a technician by ID
    public static Technician getTechnician(Connection connection, int technicianId) throws SQLException {
        String sql = "SELECT FirstName, LastName FROM Technicians WHERE TECH_ID = ?";
        ResultSet resultSet = executeQuery(connection, sql, technicianId);
        if (resultSet.next()) {
            return new Technician(
                    resultSet.getString("FirstName"),
                    resultSet.getString("LastName")
            );
        }
        return null;
    }

    // Function to get a service by ID
    public static Service getService(Connection connection, int serviceId) throws SQLException {
        String sql = "SELECT ServiceName, Cost FROM Services WHERE SERVICE_ID = ?";
        ResultSet resultSet = executeQuery(connection, sql, serviceId);
        if (resultSet.next()) {
            return new Service(
                    resultSet.getString("ServiceName"),
                    resultSet.getDouble("Cost")
            );
        }
        return null;
    }

    // -- Helper Classes to Represent Data --
    public static class Customer {
        public String firstName;
        public String lastName;
        public String phoneNumber;

        public Customer(String firstName, String lastName, String phoneNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNumber = phoneNumber;
        }

        @Override
        public String toString() {
            return "Customer{" +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    '}';
        }
    }

    public static class Car {
        public int makeModelId;
        public String licensePlate;

        public Car(int makeModelId, String licensePlate) {
            this.makeModelId = makeModelId;
            this.licensePlate = licensePlate;
        }

        @Override
        public String toString() {
            return "Car{" +
                    ", makeModelId=" + makeModelId +
                    ", licensePlate='" + licensePlate + '\'' +
                    '}';
        }
    }

    public static class Technician {
        public String firstName;
        public String lastName;

        public Technician(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return "Technician{" +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }

    public static class Service {
        public String serviceName;
        public double cost;

        public Service(String serviceName, double cost) {
            this.serviceName = serviceName;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "Service{" +
                    ", serviceName='" + serviceName + '\'' +
                    ", cost=" + cost +
                    '}';
        }
    }

    // Function to get all distinct Makes
    public static List<String> getAllMakes(Connection connection) throws SQLException {
        List<String> makes = new ArrayList<>();
        String sql = "SELECT DISTINCT Make FROM MakeModel";
        try (ResultSet resultSet = executeQuery(connection, sql)) {
            while (resultSet.next()) {
                makes.add(resultSet.getString("Make"));
            }
        }
        return makes;
    }

    // Function to get all distinct Models
    public static List<String> getAllModels(Connection connection) throws SQLException {
        List<String> models = new ArrayList<>();
        String sql = "SELECT DISTINCT Model FROM MakeModel";
        try (ResultSet resultSet = executeQuery(connection, sql)) {
            while (resultSet.next()) {
                models.add(resultSet.getString("Model"));
            }
        }
        return models;
    }

    // Function to get Models for a specific Make
    public static List<String> getModelsByMake(Connection connection, String make) throws SQLException {
        List<String> models = new ArrayList<>();
        String sql = "SELECT Model FROM MakeModel WHERE Make = ?";
        try (ResultSet resultSet = executeQuery(connection, sql, make)) {
            while (resultSet.next()) {
                models.add(resultSet.getString("Model"));
            }
        }
        return models;
    }

// Function to get all Customers
public static List<String> getAllCustomers(Connection connection) throws SQLException {
    List<String> customers = new ArrayList<>();
    String sql = "SELECT FirstName, LastName FROM Customers";
    try (ResultSet resultSet = executeQuery(connection, sql)) {
    while (resultSet.next()) {
    customers.add(resultSet.getString("FirstName") + " " + resultSet.getString("LastName"));
    }
    }
    return customers;
    }
   
    // Function to get all Technicians
    public static List<String> getAllTechnicians(Connection connection) throws SQLException {
    List<String> technicians = new ArrayList<>();
    String sql = "SELECT FirstName, LastName FROM Technicians";
    try (ResultSet resultSet = executeQuery(connection, sql)) {
    while (resultSet.next()) {
    technicians.add(resultSet.getString("FirstName") + " " + resultSet.getString("LastName"));
    }
    }
    return technicians;
    }
    
// Function to get all Services
public static List<String> getAllServices(Connection connection) throws SQLException {
    List<String> services = new ArrayList<>();
    String sql = "SELECT ServiceName FROM Services";
    try (ResultSet resultSet = executeQuery(connection, sql)) {
    while (resultSet.next()) {
    services.add(resultSet.getString("ServiceName"));
    }
    }
    return services;
    }
    
    
    public static List<String> getAllCars(Connection connection) throws SQLException {
        List<String> cars = new ArrayList<>();
        String sql = "SELECT MM.Make, MM.Model, C.LicensePlate " +
                     "FROM Cars C JOIN MakeModel MM ON C.MakeModel_ID = MM.MM_ID";
        try (ResultSet resultSet = executeQuery(connection, sql)) {
            while (resultSet.next()) {
                String carDetails = resultSet.getString("Make") + " " +
                                    resultSet.getString("Model") + " (" +
                                    resultSet.getString("LicensePlate") + ")";
                cars.add(carDetails);
            }
        }
        return cars;
    }
    // Function to link a car to a customer
public static void linkCarToCustomer(Connection connection, int customerId, int carId) throws SQLException {
    String sql = "INSERT INTO CustomerCars (Customer_ID, Car_ID) VALUES (?, ?)";
    executeUpdate(connection, sql, customerId, carId);
}

// Function to get customer ID by full name
public static int getCustomerIdByName(Connection connection, String fullName) throws SQLException {
    String[] nameParts = fullName.split(" ");
    if (nameParts.length < 2) {
        throw new SQLException("Invalid customer name format");
    }
    String firstName = nameParts[0];
    String lastName = nameParts[1];
    
    String sql = "SELECT CUST_ID FROM Customers WHERE FirstName = ? AND LastName = ?";
    ResultSet resultSet = executeQuery(connection, sql, firstName, lastName);
    if (resultSet.next()) {
        return resultSet.getInt("CUST_ID");
    } else {
        throw new SQLException("Customer not found: " + fullName);
    }
}


// Function to link a service to a technician
public static void linkServiceToTechnician(Connection connection, int technicianId, int serviceId) throws SQLException {
    String sql = "INSERT INTO TechsServices (TECH_ID, SERVICE_ID) VALUES (?, ?)";
    executeUpdate(connection, sql, technicianId, serviceId);
}

// Function to get technician ID by name
public static int getTechnicianIdByName(Connection connection, String fullName) throws SQLException {
    String[] nameParts = fullName.split(" ");
    if (nameParts.length < 2) {
        throw new SQLException("Invalid technician name format");
    }
    String firstName = nameParts[0];
    String lastName = nameParts[1];
    
    String sql = "SELECT TECH_ID FROM Technicians WHERE FirstName = ? AND LastName = ?";
    ResultSet resultSet = executeQuery(connection, sql, firstName, lastName);
    if (resultSet.next()) {
        return resultSet.getInt("TECH_ID");
    } else {
        throw new SQLException("Technician not found: " + fullName);
    }
}

// Function to get service ID by name
public static int getServiceIdByName(Connection connection, String serviceName) throws SQLException {
    String sql = "SELECT SERVICE_ID FROM Services WHERE ServiceName = ?";
    ResultSet resultSet = executeQuery(connection, sql, serviceName);
    if (resultSet.next()) {
        return resultSet.getInt("SERVICE_ID");
    } else {
        throw new SQLException("Service not found: " + serviceName);
    }
}

// Function to create an appointment
public static void createAppointment(Connection connection, int customerCarId, String appointmentDate, String appointmentTime) throws SQLException {
    // Convert string date to SQL Date format
    String[] dateParts = appointmentDate.split("/");
    int day = Integer.parseInt(dateParts[0]);
    int month = Integer.parseInt(dateParts[1]);
    int year = 2024; // Assuming current year
    
    // Format date for SQL (YYYY-MM-DD)
    String sqlDateStr = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    
    String sql = "INSERT INTO CustomerAppointmentDateTime (CUST_CAR_ID, AppointmentDate, AppointmentTime) VALUES (?, ?, ?)";
    executeUpdate(connection, sql, customerCarId, sqlDateStr, appointmentTime);
}


// Function to create a complete appointment (including all relationships)
public static int createCompleteAppointment(Connection connection, int customerId, int carId, 
                                         int technicianId, int serviceId,
                                         String appointmentDate, String appointmentTime) throws SQLException {
    
    // 1. Ensure car is linked to customer in CustomerCars table
    int customerCarId = getOrCreateCustomerCar(connection, customerId, carId);
    
    // 2. Ensure technician is linked to service in TechsServices table
    int techServiceId = getOrCreateTechService(connection, technicianId, serviceId);
    
    // 3. Create appointment date/time entry
    String sql = "INSERT INTO CustomerAppointmentDateTime (CUST_CAR_ID, AppointmentDate, AppointmentTime) VALUES (?, ?, ?)";
    PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"APPOINTMENT_DATE_TIME_ID"});
    
    // Parse date components
    String[] dateParts = appointmentDate.split("/");
    int day = Integer.parseInt(dateParts[0]);
    int month = Integer.parseInt(dateParts[1]);
    
    // Format for SQL Server (YYYY-MM-DD)
    String formattedDate = "2024-" + 
                          (month < 10 ? "0" + month : month) + "-" + 
                          (day < 10 ? "0" + day : day);
    
    stmt.setInt(1, customerCarId);
    stmt.setString(2, formattedDate);
    stmt.setString(3, appointmentTime);
    stmt.executeUpdate();
    
    ResultSet rs = stmt.getGeneratedKeys();
    int appointmentDateTimeId = 0;
    if (rs.next()) {
        appointmentDateTimeId = rs.getInt(1);
    } else {
        throw new SQLException("Failed to get ID for new appointment datetime");
    }
    
    // 4. Finally link the date/time to the tech-service in CustomerAppointmentService
    String linkSql = "INSERT INTO CustomerAppointmentService (CAR_DATE_TIME_ID, TECHNICAN_SERIVCE_ID) VALUES (?, ?)";
    PreparedStatement linkStmt = connection.prepareStatement(linkSql);
    linkStmt.setInt(1, appointmentDateTimeId);
    linkStmt.setInt(2, techServiceId);
    linkStmt.executeUpdate();
    
    return appointmentDateTimeId;
}


// Enhanced version of getAllAppointments that properly joins all the tables
public static List<Map<String, String>> getAllAppointments(Connection connection) throws SQLException {
    List<Map<String, String>> appointments = new ArrayList<>();
    
    String sql = 
        "SELECT " +
        "    cadt.APPOINTMENT_DATE_TIME_ID, " +
        "    c.FirstName AS CustomerFirstName, " +
        "    c.LastName AS CustomerLastName, " +
        "    mm.Make, " +
        "    mm.Model, " +
        "    cars.LicensePlate, " +
        "    t.FirstName AS TechFirstName, " +
        "    t.LastName AS TechLastName, " +
        "    s.ServiceName, " +
        "    s.Cost AS ServiceCost, " +
        "    CONVERT(VARCHAR, cadt.AppointmentDate, 103) AS FormattedDate, " +
        "    cadt.AppointmentTime " +
        "FROM CustomerAppointmentDateTime cadt " +
        "JOIN CustomerCars cc ON cadt.CUST_CAR_ID = cc.CUSTCAR_ID " +
        "JOIN Customers c ON cc.Customer_ID = c.CUST_ID " +
        "JOIN Cars cars ON cc.Car_ID = cars.CAR_ID " +
        "JOIN MakeModel mm ON cars.MakeModel_ID = mm.MM_ID " +
        "JOIN CustomerAppointmentService cas ON cadt.APPOINTMENT_DATE_TIME_ID = cas.CAR_DATE_TIME_ID " +
        "JOIN TechsServices ts ON cas.TECHNICAN_SERIVCE_ID = ts.TECHSERVICE_ID " +
        "JOIN Technicians t ON ts.TECH_ID = t.TECH_ID " +
        "JOIN Services s ON ts.SERVICE_ID = s.SERVICE_ID " +
        "ORDER BY cadt.AppointmentDate, cadt.AppointmentTime";
    
    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            Map<String, String> appointment = new HashMap<>();
            appointment.put("id", rs.getString("APPOINTMENT_DATE_TIME_ID"));
            appointment.put("customerName", rs.getString("CustomerFirstName") + " " + rs.getString("CustomerLastName"));
            appointment.put("carDetails", rs.getString("Make") + " " + rs.getString("Model") + " (" + rs.getString("LicensePlate") + ")");
            appointment.put("technicianName", rs.getString("TechFirstName") + " " + rs.getString("TechLastName"));
            appointment.put("serviceName", rs.getString("ServiceName"));
            appointment.put("serviceCost", "$" + rs.getDouble("ServiceCost"));
            appointment.put("date", rs.getString("FormattedDate"));
            appointment.put("time", formatMilitaryTime(rs.getString("AppointmentTime")));
            
            appointments.add(appointment);
        }
    }
    
    return appointments;
}

// Helper function to format time as military time without leading zeros on hours
public static String formatMilitaryTime(String timeString) {
    if (timeString == null || timeString.isEmpty()) {
        return timeString;
    }
    
    try {
        // Parse the time (assuming it's in a standard format like "HH:MM:SS" or "HH:MM")
        String[] timeParts = timeString.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        
        // Format as military time without leading zeros on hours
        return hours + ":" + (minutes < 10 ? "0" + minutes : minutes);
    } catch (Exception e) {
        // If any parsing error occurs, return the original time
        return timeString;
    }
}

// Helper function to get car ID by details string with improved error handling
public static int getCarIdByDetails(Connection connection, String carDetails) throws SQLException {
    try {
        // Parse car details string like "Toyota Camry (ABC-123)"
        int openParenIndex = carDetails.indexOf('(');
        int closeParenIndex = carDetails.indexOf(')');
        
        if (openParenIndex == -1 || closeParenIndex == -1) {
            throw new SQLException("Invalid car details format: " + carDetails);
        }
        
        String licensePlate = carDetails.substring(openParenIndex + 1, closeParenIndex);
        
        String sql = "SELECT CAR_ID FROM Cars WHERE LicensePlate = ?";
        ResultSet resultSet = executeQuery(connection, sql, licensePlate);
        if (resultSet.next()) {
            return resultSet.getInt("CAR_ID");
        } else {
            throw new SQLException("Car not found with license plate: " + licensePlate);
        }
    } catch (Exception e) {
        throw new SQLException("Error parsing car details: " + carDetails + ". " + e.getMessage());
    }
}
// Make sure these helper methods are public and accessible in your SQLFunctions class

// Helper function to get or create a CustomerCar entry
public static int getOrCreateCustomerCar(Connection connection, int customerId, int carId) throws SQLException {
    // First check if this relationship already exists
    String checkSql = "SELECT CUSTCAR_ID FROM CustomerCars WHERE Customer_ID = ? AND Car_ID = ?";
    PreparedStatement checkStmt = connection.prepareStatement(checkSql);
    checkStmt.setInt(1, customerId);
    checkStmt.setInt(2, carId);
    
    ResultSet rs = checkStmt.executeQuery();
    if (rs.next()) {
        return rs.getInt("CUSTCAR_ID");
    } else {
        // Create new relationship
        String insertSql = "INSERT INTO CustomerCars (Customer_ID, Car_ID) VALUES (?, ?)";
        PreparedStatement insertStmt = connection.prepareStatement(insertSql, new String[]{"CUSTCAR_ID"});
        insertStmt.setInt(1, customerId);
        insertStmt.setInt(2, carId);
        insertStmt.executeUpdate();
        
        ResultSet keys = insertStmt.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        } else {
            throw new SQLException("Failed to get ID for new customer-car relationship");
        }
    }
}

// Helper function to get or create a TechService entry
public static int getOrCreateTechService(Connection connection, int technicianId, int serviceId) throws SQLException {
    // First check if this relationship already exists
    String checkSql = "SELECT TECHSERVICE_ID FROM TechsServices WHERE TECH_ID = ? AND SERVICE_ID = ?";
    PreparedStatement checkStmt = connection.prepareStatement(checkSql);
    checkStmt.setInt(1, technicianId);
    checkStmt.setInt(2, serviceId);
    
    ResultSet rs = checkStmt.executeQuery();
    if (rs.next()) {
        return rs.getInt("TECHSERVICE_ID");
    } else {
        // Create new relationship
        String insertSql = "INSERT INTO TechsServices (TECH_ID, SERVICE_ID) VALUES (?, ?)";
        PreparedStatement insertStmt = connection.prepareStatement(insertSql, new String[]{"TECHSERVICE_ID"});
        insertStmt.setInt(1, technicianId);
        insertStmt.setInt(2, serviceId);
        insertStmt.executeUpdate();
        
        ResultSet keys = insertStmt.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        } else {
            throw new SQLException("Failed to get ID for new technician-service relationship");
        }
    }
}
        
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("✅ Successfully connected to the database!");

            // Example usage of add functions (already present)
            addCustomer(connection, "John", "Doe", "123-456-7890");
            addCar(connection, "Toyota", "Camry", "ABC-123");
            addTechnician(connection, "Jane", "Smith");
            addService(connection, "Wheel Alignment", 75.00);

            System.out.println("✅ Added new records!");

            // Example usage of get functions
            Customer customer = getCustomer(connection, 1); // Assuming ID 1 exists
            if (customer != null) {
                System.out.println("Retrieved Customer: " + customer);
            }

            Car car = getCar(connection, 1);
            if (car != null) {
                System.out.println("Retrieved Car: " + car);
            }

            Technician technician = getTechnician(connection, 1);
            if (technician != null) {
                System.out.println("Retrieved Technician: " + technician);
            }

            Service service = getService(connection, 1);
            if (service != null) {
                System.out.println("Retrieved Service: " + service);
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error:");
            e.printStackTrace();
        }
    }
}