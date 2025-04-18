import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
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
}