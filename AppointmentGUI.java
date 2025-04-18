import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AppointmentGUI {
    private JFrame frame;
    private JComboBox<String> customerComboBox;
    private JComboBox<String> carComboBox;
    private JComboBox<String> technicianComboBox;
    private JComboBox<String> dayComboBox;
    private JComboBox<String> timeComboBox;
    private JButton createAppointmentButton;

    private List<Customer> customers;
    private List<Technician> technicians;

    public AppointmentGUI(List<Customer> customers, List<Technician> technicians) {
        this.customers = customers;
        this.technicians = technicians;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Create Appointment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2, 10, 10));

        // Customer Selection
        frame.add(new JLabel("Select Customer:"));
        customerComboBox = new JComboBox<>();
        for (Customer customer : customers) {
            customerComboBox.addItem(customer.getId() + ": " + customer.getName());
        }
        customerComboBox.addActionListener(e -> updateCarComboBox());
        frame.add(customerComboBox);

        // Car Selection
        frame.add(new JLabel("Select Car:"));
        carComboBox = new JComboBox<>();
        updateCarComboBox(); // Populate cars for the first customer
        frame.add(carComboBox);

        // Technician Selection
        frame.add(new JLabel("Select Technician:"));
        technicianComboBox = new JComboBox<>();
        for (Technician technician : technicians) {
            technicianComboBox.addItem(technician.getId() + ": " + technician.getName());
        }
        frame.add(technicianComboBox);

        // Appointment Day Selection
        frame.add(new JLabel("Select Appointment Day:"));
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        dayComboBox = new JComboBox<>(days);
        frame.add(dayComboBox);

        // Appointment Time Selection
        frame.add(new JLabel("Select Appointment Time:"));
        String[] times = {"9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM"};
        timeComboBox = new JComboBox<>(times);
        frame.add(timeComboBox);

        // Create Appointment Button
        createAppointmentButton = new JButton("Create Appointment");
        createAppointmentButton.addActionListener(e -> createAppointment());
        frame.add(createAppointmentButton);

        frame.setVisible(true);
    }

    private void updateCarComboBox() {
        carComboBox.removeAllItems();
        int selectedCustomerIndex = customerComboBox.getSelectedIndex();
        if (selectedCustomerIndex >= 0) {
            Customer selectedCustomer = customers.get(selectedCustomerIndex);
            for (Car car : selectedCustomer.getCars()) {
                carComboBox.addItem(car.getId() + ": " + car.getMake() + " " + car.getModel());
            }
        }
    }

    private void createAppointment() {
        int selectedCustomerIndex = customerComboBox.getSelectedIndex();
        int selectedCarIndex = carComboBox.getSelectedIndex();
        int selectedTechnicianIndex = technicianComboBox.getSelectedIndex();

        if (selectedCustomerIndex < 0 || selectedCarIndex < 0 || selectedTechnicianIndex < 0) {
            JOptionPane.showMessageDialog(frame, "Please select all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer selectedCustomer = customers.get(selectedCustomerIndex);
        Car selectedCar = selectedCustomer.getCars().get(selectedCarIndex);
        Technician selectedTechnician = technicians.get(selectedTechnicianIndex);

        String day = (String) dayComboBox.getSelectedItem();
        String time = (String) timeComboBox.getSelectedItem();

        // Create the appointment
        Appointment appointment = new Appointment();
        appointment.setId(selectedCustomer.getCars().size() + 1); // Example ID
        appointment.setCustomer(selectedCustomer);
        appointment.setCar(selectedCar);
        appointment.setTechnician(selectedTechnician);
        appointment.setDay(day);
        appointment.setTime(time);

        JOptionPane.showMessageDialog(frame, "Appointment created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Example data
        List<Customer> customers = ExampleData.getCustomers();
        List<Technician> technicians = ExampleData.getTechnicians();

        new AppointmentGUI(customers, technicians);
    }
}