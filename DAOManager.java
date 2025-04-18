import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOManager {

    // AppointmentDAO
    public static class AppointmentDAO {
        public static List<Appointment> getAllAppointments() {
            List<Appointment> appointments = new ArrayList<>();
            String query = """
                SELECT 
                    a.id AS AppointmentID,
                    c.name AS CustomerName,
                    car.make AS CarMake,
                    car.model AS CarModel,
                    car.license_plate AS LicensePlate,
                    t.name AS TechnicianName,
                    s.service_name AS ServiceName,
                    a.appointment_day AS AppointmentDay,
                    a.appointment_time AS AppointmentTime
                FROM Appointments a
                JOIN Customers c ON a.customer_id = c.id
                JOIN Cars car ON a.car_id = car.id
                JOIN Technicians t ON a.technician_id = t.id
                JOIN Services s ON a.service_id = s.id;
            """;

            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setId(resultSet.getInt("AppointmentID"));
                    appointment.setCustomerName(resultSet.getString("CustomerName"));
                    appointment.setCarMake(resultSet.getString("CarMake"));
                    appointment.setCarModel(resultSet.getString("CarModel"));
                    appointment.setLicensePlate(resultSet.getString("LicensePlate"));
                    appointment.setTechnicianName(resultSet.getString("TechnicianName"));
                    appointment.setServiceName(resultSet.getString("ServiceName"));
                    appointment.setAppointmentDay(resultSet.getString("AppointmentDay"));
                    appointment.setAppointmentTime(resultSet.getString("AppointmentTime"));
                    appointments.add(appointment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return appointments;
        }

        public static void addAppointment(Appointment appointment) {
            String query = """
                INSERT INTO Appointments (customer_id, car_id, technician_id, service_id, appointment_day, appointment_time)
                VALUES (?, ?, ?, ?, ?, ?);
            """;

            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, appointment.getCustomerId());
                statement.setInt(2, appointment.getCarId());
                statement.setInt(3, appointment.getTechnicianId());
                statement.setInt(4, appointment.getServiceId());
                statement.setString(5, appointment.getAppointmentDay());
                statement.setString(6, appointment.getAppointmentTime());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // CustomerDAO
    public static class CustomerDAO {
        public static List<Customer> getAllCustomers() {
            List<Customer> customers = new ArrayList<>();
            String query = "SELECT * FROM Customers";

            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setId(resultSet.getInt("id"));
                    customer.setName(resultSet.getString("name"));
                    customer.setPhoneNumber(resultSet.getString("phone_number"));
                    customers.add(customer);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return customers;
        }

        public static void addCustomer(Customer customer) {
            String query = "INSERT INTO Customers (name, phone_number) VALUES (?, ?)";

            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, customer.getName());
                statement.setString(2, customer.getPhoneNumber());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // CarDAO
    public static class CarDAO {
        public static List<Car> getCarsByCustomerId(int customerId) {
            List<Car> cars = new ArrayList<>();
            String query = "SELECT * FROM Cars WHERE customer_id = ?";

            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, customerId);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Car car = new Car();
                    car.setId(resultSet.getInt("id"));
                    car.setMake(resultSet.getString("make"));
                    car.setModel(resultSet.getString("model"));
                    car.setLicensePlate(resultSet.getString("license_plate"));
                    cars.add(car);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return cars;
        }

        public static void addCar(Car car, int customerId) {
            String query = "INSERT INTO Cars (customer_id, make, model, license_plate) VALUES (?, ?, ?, ?)";

            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, customerId);
                statement.setString(2, car.getMake());
                statement.setString(3, car.getModel());
                statement.setString(4, car.getLicensePlate());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // TechnicianDAO
    public static class TechnicianDAO {
        public static List<Technician> getAllTechnicians() {
            List<Technician> technicians = new ArrayList<>();
            String query = "SELECT * FROM Technicians";

            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Technician technician = new Technician();
                    technician.setId(resultSet.getInt("id"));
                    technician.setName(resultSet.getString("name"));
                    technicians.add(technician);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return technicians;
        }

        public static void addTechnician(Technician technician) {
            String query = "INSERT INTO Technicians (name) VALUES (?)";

            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, technician.getName());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ServiceDAO
    public static class ServiceDAO {
        public static List<String> getServicesByTechnicianId(int technicianId) {
            List<String> services = new ArrayList<>();
            String query = "SELECT service_name FROM Services WHERE technician_id = ?";

            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, technicianId);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    services.add(resultSet.getString("service_name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return services;
        }

        public static int getServiceId(int technicianId, String serviceName) {
            String query = "SELECT id FROM Services WHERE technician_id = ? AND service_name = ?";

            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, technicianId);
                statement.setString(2, serviceName);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1; // Return -1 if the service is not found
        }

        public static void addService(int technicianId, String serviceName) {
            String query = "INSERT INTO Services (technician_id, service_name) VALUES (?, ?)";

            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, technicianId);
                statement.setString(2, serviceName);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}