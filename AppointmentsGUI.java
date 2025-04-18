import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AppointmentsGUI {
    private JFrame frame;
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> customerComboBox;
    private JComboBox<String> carComboBox;
    private JComboBox<String> technicianComboBox;
    private JComboBox<String> serviceComboBox;
    private JComboBox<String> dayComboBox;
    private JComboBox<String> timeComboBox;
    private JButton createAppointmentButton;
    private JButton refreshButton;

    public AppointmentsGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Appointment Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Table for displaying appointments
        String[] columnNames = {"ID", "Customer", "Car", "License Plate", "Technician", "Service", "Day", "Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        appointmentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel for creating appointments
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        // Customer Selection
        inputPanel.add(new JLabel("Select Customer:"));
        customerComboBox = new JComboBox<>();
        updateCustomerComboBox();
        customerComboBox.addActionListener(e -> updateCarComboBox());
        inputPanel.add(customerComboBox);

        // Car Selection
        inputPanel.add(new JLabel("Select Car:"));
        carComboBox = new JComboBox<>();
        updateCarComboBox();
        inputPanel.add(carComboBox);

        // Technician Selection
        inputPanel.add(new JLabel("Select Technician:"));
        technicianComboBox = new JComboBox<>();
        updateTechnicianComboBox();
        technicianComboBox.addActionListener(e -> updateServiceComboBox());
        inputPanel.add(technicianComboBox);

        // Service Selection
        inputPanel.add(new JLabel("Select Service:"));
        serviceComboBox = new JComboBox<>();
        updateServiceComboBox();
        inputPanel.add(serviceComboBox);

        // Appointment Day Selection
        inputPanel.add(new JLabel("Select Appointment Day:"));
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        dayComboBox = new JComboBox<>(days);
        inputPanel.add(dayComboBox);

        // Appointment Time Selection
        inputPanel.add(new JLabel("Select Appointment Time:"));
        String[] times = {"9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM"};
        timeComboBox = new JComboBox<>(times);
        inputPanel.add(timeComboBox);

        // Create Appointment Button
        createAppointmentButton = new JButton("Create Appointment");
        createAppointmentButton.addActionListener(e -> createAppointment());
        inputPanel.add(createAppointmentButton);

        // Refresh Button
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> updateAppointmentsTable());
        inputPanel.add(refreshButton);

        frame.add(inputPanel, BorderLayout.SOUTH);

        // Load initial data
        updateAppointmentsTable();

        frame.setVisible(true);
    }

    private void updateCustomerComboBox() {
        customerComboBox.removeAllItems();
        List<Customer> customers = DAOManager.CustomerDAO.getAllCustomers();
        for (Customer customer : customers) {
            customerComboBox.addItem(customer.getId() + ": " + customer.getName());
        }
    }

    private void updateCarComboBox() {
        carComboBox.removeAllItems();
        int selectedCustomerIndex = customerComboBox.getSelectedIndex();
        if (selectedCustomerIndex >= 0) {
            int customerId = Integer.parseInt(customerComboBox.getSelectedItem().toString().split(":")[0]);
            List<Car> cars = DAOManager.CarDAO.getCarsByCustomerId(customerId);
            for (Car car : cars) {
                carComboBox.addItem(car.getId() + ": " + car.getMake() + " " + car.getModel() + " (" + car.getLicensePlate() + ")");
            }
        }
    }

    private void updateTechnicianComboBox() {
        technicianComboBox.removeAllItems();
        List<Technician> technicians = DAOManager.TechnicianDAO.getAllTechnicians();
        for (Technician technician : technicians) {
            technicianComboBox.addItem(technician.getId() + ": " + technician.getName());
        }
    }

    private void updateServiceComboBox() {
        serviceComboBox.removeAllItems();
        int selectedTechnicianIndex = technicianComboBox.getSelectedIndex();
        if (selectedTechnicianIndex >= 0) {
            int technicianId = Integer.parseInt(technicianComboBox.getSelectedItem().toString().split(":")[0]);
            List<String> services = DAOManager.ServiceDAO.getServicesByTechnicianId(technicianId);
            for (String service : services) {
                serviceComboBox.addItem(service);
            }
        }
    }

    private void updateAppointmentsTable() {
        List<Appointment> appointments = DAOManager.AppointmentDAO.getAllAppointments();

        // Clear the table
        tableModel.setRowCount(0);

        // Add rows to the table
        for (Appointment appointment : appointments) {
            tableModel.addRow(new Object[]{
                appointment.getId(),
                appointment.getCustomerName(),
                appointment.getCarMake() + " " + appointment.getCarModel(),
                appointment.getLicensePlate(),
                appointment.getTechnicianName(),
                appointment.getServiceName(),
                appointment.getAppointmentDay(),
                appointment.getAppointmentTime()
            });
        }
    }

    private void createAppointment() {
        int selectedCustomerIndex = customerComboBox.getSelectedIndex();
        int selectedCarIndex = carComboBox.getSelectedIndex();
        int selectedTechnicianIndex = technicianComboBox.getSelectedIndex();
        int selectedServiceIndex = serviceComboBox.getSelectedIndex();

        if (selectedCustomerIndex < 0 || selectedCarIndex < 0 || selectedTechnicianIndex < 0 || selectedServiceIndex < 0) {
            JOptionPane.showMessageDialog(frame, "Please select all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int customerId = Integer.parseInt(customerComboBox.getSelectedItem().toString().split(":")[0]);
        int carId = Integer.parseInt(carComboBox.getSelectedItem().toString().split(":")[0]);
        int technicianId = Integer.parseInt(technicianComboBox.getSelectedItem().toString().split(":")[0]);
        String serviceName = serviceComboBox.getSelectedItem().toString();
        String day = dayComboBox.getSelectedItem().toString();
        String time = timeComboBox.getSelectedItem().toString();

        int serviceId = DAOManager.ServiceDAO.getServiceId(technicianId, serviceName);

        // Create the appointment
        Appointment appointment = new Appointment();
        appointment.setCustomerId(customerId);
        appointment.setCarId(carId);
        appointment.setTechnicianId(technicianId);
        appointment.setServiceId(serviceId);
        appointment.setAppointmentDay(day);
        appointment.setAppointmentTime(time);

        // Add to database
        DAOManager.AppointmentDAO.addAppointment(appointment);

        // Update the table
        updateAppointmentsTable();
        JOptionPane.showMessageDialog(frame, "Appointment created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new AppointmentsGUI();
    }
}