import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class NewGUI{

    public static void main(String[] args) {
        JFrame frame = new JFrame("Service Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new GridLayout(0, 1));

        DefaultComboBoxModel<String> customerModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> technicianModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> makeModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> modelModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> serviceModel = new DefaultComboBoxModel<>();

        HashMap<String, ArrayList<String>> customerCars = new HashMap<>();
        HashMap<String, ArrayList<String>> technicianServices = new HashMap<>();
        ArrayList<String> appointmentList = new ArrayList<>();

        try (Connection conn = SQLFunctions.getConnection()) {
            // Load data from the database
            loadCustomers(conn, customerModel, customerCars);
            loadTechnicians(conn, technicianModel, technicianServices);
            loadMakes(conn, makeModel);
            loadModels(conn, modelModel);
            loadServices(conn, serviceModel);

        } catch (SQLException e) {
            handleDatabaseError(frame, e);
        }

        // Add Customer Button (Modified to take First and Last Name)
        JButton addCustomerButton = new JButton("Add Customer");
        addCustomerButton.addActionListener(e -> addCustomer(frame));
        frame.add(addCustomerButton);

        // Add Technician Button (Modified to take First and Last Name)
        JButton addTechnicianButton = new JButton("Add Technician");
        addTechnicianButton.addActionListener(e -> addTechnician(frame));
        frame.add(addTechnicianButton);

        // Add Car Button (Retained from the 1st file with some refactoring)
        JButton addCarButton = new JButton("Add Car");
        addCarButton.addActionListener(e -> addCar(frame, makeModel, modelModel));
        frame.add(addCarButton);

        // Add Service Button (Retained from the 1st file)
        JButton addServiceButton = new JButton("Add Service");
        addServiceButton.addActionListener(e -> addService(frame));
        frame.add(addServiceButton);

        // Associate Car to Customer (Refactored for clarity)
        JButton linkCarButton = new JButton("Associate Car to Customer");
        linkCarButton.addActionListener(e -> linkCarToCustomer(frame, customerModel));
        frame.add(linkCarButton);

        // Associate Service to Technician (Refactored for clarity)
        JButton assignServiceButton = new JButton("Associate Service to Technician");
        assignServiceButton.addActionListener(e -> assignServiceToTechnician(frame, technicianModel, serviceModel));
        frame.add(assignServiceButton);

        // Create Appointment Button (Mostly from 1st file, with slight UI adjustments)
        JButton createAppointmentButton = new JButton("Create Appointment");
        createAppointmentButton.addActionListener(e -> createAppointment(frame, customerModel, makeModel, modelModel, technicianModel, serviceModel));
        frame.add(createAppointmentButton);

        // View All Appointments (Retained from the 1st file)
        JButton viewAppointmentsButton = new JButton("View All Appointments");
        viewAppointmentsButton.addActionListener(e -> viewAppointments(frame));
        frame.add(viewAppointmentsButton);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        frame.add(exitButton);

        frame.setVisible(true);
    }

    // --- Helper Methods ---

    private static void loadCustomers(Connection conn, DefaultComboBoxModel<String> customerModel, HashMap<String, ArrayList<String>> customerCars) throws SQLException {
        List<String> customers = SQLFunctions.getAllCustomers(conn);
        for (String customer : customers) {
            customerModel.addElement(customer);
            customerCars.put(customer, new ArrayList<>());
        }
    }

    private static void loadTechnicians(Connection conn, DefaultComboBoxModel<String> technicianModel, HashMap<String, ArrayList<String>> technicianServices) throws SQLException {
        List<String> technicians = SQLFunctions.getAllTechnicians(conn);
        for (String technician : technicians) {
            technicianModel.addElement(technician);
            technicianServices.put(technician, new ArrayList<>());
        }
    }

    private static void loadMakes(Connection conn, DefaultComboBoxModel<String> makeModel) throws SQLException {
        List<String> makes = SQLFunctions.getAllMakes(conn);
        for (String make : makes) {
            makeModel.addElement(make);
        }
    }

    private static void loadModels(Connection conn, DefaultComboBoxModel<String> modelModel) throws SQLException {
        List<String> models = SQLFunctions.getAllModels(conn);
        for (String model : models) {
            modelModel.addElement(model);
        }
    }

    private static void loadServices(Connection conn, DefaultComboBoxModel<String> serviceModel) throws SQLException {
        List<String> services = SQLFunctions.getAllServices(conn);
        for (String service : services) {
            serviceModel.addElement(service);
        }
    }

    private static void handleDatabaseError(JFrame frame, SQLException e) {
        JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    private static void addCustomer(JFrame frame) {
        String customerFirstName = JOptionPane.showInputDialog(frame, "Enter Customer First Name:");
        if (customerFirstName == null) return;

        String customerLastName = JOptionPane.showInputDialog(frame, "Enter Customer Last Name:");
        if (customerLastName == null) return;

        String customerPhone = JOptionPane.showInputDialog(frame, "Enter Customer Phone Number:");
        if (customerPhone == null) return;

        try (Connection conn = SQLFunctions.getConnection()) {
            SQLFunctions.addCustomer(conn, customerFirstName, customerLastName, customerPhone);
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) ((JComboBox<?>) frame.getContentPane().getComponent(0)).getModel();
            model.addElement(customerFirstName + " " + customerLastName);
            JOptionPane.showMessageDialog(frame, "Customer Added: " + customerFirstName + " " + customerLastName + ", " + customerPhone);
        } catch (SQLException ex) {
            handleDatabaseError(frame, ex);
        }
    }

    private static void addTechnician(JFrame frame) {
        String firstName = JOptionPane.showInputDialog(frame, "Enter Technician First Name:");
        if (firstName == null) return;

        String lastName = JOptionPane.showInputDialog(frame, "Enter Technician Last Name:");
        if (lastName == null) return;

        try (Connection conn = SQLFunctions.getConnection()) {
            SQLFunctions.addTechnician(conn, firstName, lastName);
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) ((JComboBox<?>) frame.getContentPane().getComponent(1)).getModel();
            model.addElement(firstName + " " + lastName);
            JOptionPane.showMessageDialog(frame, "Technician Added: " + firstName + " " + lastName);
        } catch (SQLException ex) {
            handleDatabaseError(frame, ex);
        }
    }

    private static void addCar(JFrame frame, DefaultComboBoxModel<String> makeModel, DefaultComboBoxModel<String> modelModel) {
        JComboBox<String> makeDropdown = new JComboBox<>(makeModel);
        DefaultComboBoxModel<String> availableModelsModel = new DefaultComboBoxModel<>();
        JComboBox<String> modelDropdown = new JComboBox<>(availableModelsModel);
        JTextField licensePlateField = new JTextField();

        makeDropdown.addActionListener(event -> {
            String selectedMake = (String) makeDropdown.getSelectedItem();
            availableModelsModel.removeAllElements();
            try (Connection conn = SQLFunctions.getConnection()) {
                List<String> models = SQLFunctions.getModelsByMake(conn, selectedMake);
                for (String model : models) {
                    availableModelsModel.addElement(model);
                }
            } catch (SQLException ex) {
                handleDatabaseError(frame, ex);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Car Make:"));
        panel.add(makeDropdown);
        panel.add(new JLabel("Car Model:"));
        panel.add(modelDropdown);
        panel.add(new JLabel("License Plate:"));
        panel.add(licensePlateField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Car", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String selectedMake = (String) makeDropdown.getSelectedItem();
            String selectedModel = (String) modelDropdown.getSelectedItem();
            String licensePlate = licensePlateField.getText();

            if (selectedMake != null && selectedModel != null && !licensePlate.isEmpty()) {
                try (Connection conn = SQLFunctions.getConnection()) {
                    SQLFunctions.addCar(conn, selectedMake, selectedModel, licensePlate);
                    JOptionPane.showMessageDialog(frame, "Car Added!");
                } catch (SQLException ex) {
                    handleDatabaseError(frame, ex);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Make, Model, and License Plate are required!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void addService(JFrame frame) {
        JTextField serviceNameField = new JTextField();
        JTextField serviceCostField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Service Name:"));
        panel.add(serviceNameField);
        panel.add(new JLabel("Service Cost:"));
        panel.add(serviceCostField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Service", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String serviceName = serviceNameField.getText();
            String serviceCostText = serviceCostField.getText();

            if (!serviceName.isEmpty() && !serviceCostText.isEmpty()) {
                try {
                    double serviceCost = Double.parseDouble(serviceCostText);
                    try (Connection conn = SQLFunctions.getConnection()) {
                        SQLFunctions.addService(conn, serviceName, serviceCost);
                        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) ((JComboBox<?>) frame.getContentPane().getComponent(2)).getModel();
                        model.addElement(serviceName);
                        JOptionPane.showMessageDialog(frame, "Service Added!");
                    } catch (SQLException ex) {
                        handleDatabaseError(frame, ex);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid cost format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Service name and cost required!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void linkCarToCustomer(JFrame frame, DefaultComboBoxModel<String> customerModel) {
        JComboBox<String> customerDropdown = new JComboBox<>(customerModel);
        DefaultComboBoxModel<String> carModel = new DefaultComboBoxModel<>();
        try (Connection conn = SQLFunctions.getConnection()) {
            List<String> cars = SQLFunctions.getAllCars(conn);
            for (String car : cars) {
                carModel.addElement(car);
            }
        } catch (SQLException ex) {
            handleDatabaseError(frame, ex);
            return; // Important: Exit if there's a DB error
        }
        JComboBox<String> carDropdown = new JComboBox<>(carModel);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Customer:"));
        panel.add(customerDropdown);
        panel.add(new JLabel("Select Car:"));
        panel.add(carDropdown);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Link Car to Customer", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String selectedCustomer = (String) customerDropdown.getSelectedItem();
            String selectedCar = (String) carDropdown.getSelectedItem();
            if (selectedCustomer != null && selectedCar != null) {
                try (Connection conn = SQLFunctions.getConnection()) {
                    int customerId = SQLFunctions.getCustomerIdByName(conn, selectedCustomer);
                    int carId = SQLFunctions.getCarIdByDetails(conn, selectedCar);

                    SQLFunctions.linkCarToCustomer(conn, customerId, carId);
                    JOptionPane.showMessageDialog(frame, "Car Linked to Customer!");
                } catch (SQLException ex) {
                    handleDatabaseError(frame, ex);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Select both Customer and Car!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void assignServiceToTechnician(JFrame frame, DefaultComboBoxModel<String> technicianModel, DefaultComboBoxModel<String> serviceModel) {
        JComboBox<String> technicianDropdown = new JComboBox<>(technicianModel);
        JComboBox<String> serviceDropdown = new JComboBox<>(serviceModel);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Technician:"));
        panel.add(technicianDropdown);
        panel.add(new JLabel("Select Service:"));
        panel.add(serviceDropdown);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Assign Service to Technician", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String selectedTechnician = (String) technicianDropdown.getSelectedItem();
            String selectedService = (String) serviceDropdown.getSelectedItem();
            if (selectedTechnician != null && selectedService != null) {
                try (Connection conn = SQLFunctions.getConnection()) {
                    int technicianId = SQLFunctions.getTechnicianIdByName(conn, selectedTechnician);
                    int serviceId = SQLFunctions.getServiceIdByName(conn, selectedService);

                    SQLFunctions.linkServiceToTechnician(conn, technicianId, serviceId);
                    JOptionPane.showMessageDialog(frame, "Service Assigned to Technician!");
                } catch (SQLException ex) {
                    handleDatabaseError(frame, ex);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Select both Technician and Service!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void createAppointment(JFrame frame, DefaultComboBoxModel<String> customerModel, DefaultComboBoxModel<String> makeModel, DefaultComboBoxModel<String> modelModel, DefaultComboBoxModel<String> technicianModel, DefaultComboBoxModel<String> serviceModel) {
        try (Connection conn = SQLFunctions.getConnection()) {
            // 1. Date & Time Selection (with improved UI)
            String[] timeSlots = {"09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                    "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM"};
            JComboBox<String> timeDropdown = new JComboBox<>(timeSlots);

            String[] months = {
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
            };
            JComboBox<String> monthsDropdown = new JComboBox<>(months);

            String[] appointmentDays = {
                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                    "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
            };
            JComboBox<String> dateDropdown = new JComboBox<>(appointmentDays);

            String[] years = {"2024"};
            JComboBox<String> yearDropdown = new JComboBox<>(years);

            JPanel dateTimePanel = new JPanel(new GridLayout(0, 1));
            dateTimePanel.add(new JLabel("Select Date:"));
            JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            datePanel.add(new JLabel("Month:"));
            datePanel.add(monthsDropdown);
            datePanel.add(new JLabel("  Day:"));
            datePanel.add(dateDropdown);
            datePanel.add(new JLabel("  Year:"));
            datePanel.add(yearDropdown);
            dateTimePanel.add(datePanel);
            dateTimePanel.add(new JLabel("Select Time:"));
            dateTimePanel.add(timeDropdown);

            int dateTimeResult = JOptionPane.showConfirmDialog(
                    frame, dateTimePanel, "Pick Date and Time", JOptionPane.OK_CANCEL_OPTION);

            if (dateTimeResult != JOptionPane.OK_OPTION) return;

            String selectedTime = (String) timeDropdown.getSelectedItem();
            String selectedDay = (String) dateDropdown.getSelectedItem();
            String selectedMonth = String.valueOf(monthsDropdown.getSelectedIndex() + 1); // Convert month name to number
            String selectedYear = (String) yearDropdown.getSelectedItem();

            if (selectedTime == null || selectedDay == null || selectedMonth == null || selectedYear == null) {
                JOptionPane.showMessageDialog(
                        frame, "Date and time must be selected!",
                        "Validation Error", JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Format date as Month Day, Year for display
            String monthName = months[Integer.parseInt(selectedMonth) - 1];
            String appointmentDate = monthName + " " + selectedDay + ", " + selectedYear;

            // 2. Customer Selection
            String[] customers = new String[customerModel.getSize()];
            for (int i = 0; i < customerModel.getSize(); i++) {
                customers[i] = customerModel.getElementAt(i);
            }

            String selectedCustomer = (String) JOptionPane.showInputDialog(
                    frame, "Select Customer:", "Create Appointment",
                    JOptionPane.QUESTION_MESSAGE, null, customers, null
            );

            if (selectedCustomer == null) return;
            int customerId = SQLFunctions.getCustomerIdByName(conn, selectedCustomer);

            // 3. Car Selection (Get from DB)
            String carQuery =
                    "SELECT mm.Make, mm.Model, c.LicensePlate, c.CAR_ID " +
                            "FROM CustomerCars cc " +
                            "JOIN Cars c ON cc.Car_ID = c.CAR_ID " +
                            "JOIN MakeModel mm ON c.MakeModel_ID = mm.MM_ID " +
                            "WHERE cc.Customer_ID = ?";

            PreparedStatement carStmt = conn.prepareStatement(carQuery);
            carStmt.setInt(1, customerId);
            ResultSet carRs = carStmt.executeQuery();

            ArrayList<String> carsList = new ArrayList<>();
            Map<String, Integer> carIdMap = new HashMap<>(); // To store car IDs by display name

            while (carRs.next()) {
                String carDetails = carRs.getString("Make") + " " +
                        carRs.getString("Model") + " (" +
                        carRs.getString("LicensePlate") + ")";
                carsList.add(carDetails);
                carIdMap.put(carDetails, carRs.getInt("CAR_ID"));
            }

            if (carsList.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "No cars found for this customer. Please add a car first.",
                        "No Cars Available", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create array for the dropdown
            String[] carArray = carsList.toArray(new String[0]);
            String selectedCar = (String) JOptionPane.showInputDialog(
                    frame, "Select Car:", "Create Appointment",
                    JOptionPane.QUESTION_MESSAGE, null, carArray, null
            );

            if (selectedCar == null) return;
            int carId = carIdMap.get(selectedCar);

            // 4. Service and Technician selection
            // Get all available services
            List<String> allServices = SQLFunctions.getAllServices(conn);
            if (allServices.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "No services available. Please add services first.",
                        "No Services", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create a list to store selected services and their technicians
            List<Map<String, Object>> selectedServicesTechnicians = new ArrayList<>();
            boolean addingServices = true;

            // 4a. Service and Technician selection loop
            while (addingServices) {
                // Display services that haven't been selected yet
                List<String> availableServices = new ArrayList<>(allServices);
                for (Map<String, Object> entry : selectedServicesTechnicians) {
                    availableServices.remove(entry.get("service"));
                }

                if (availableServices.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "All services have been selected.",
                            "Services Complete", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }

                // Select a service
                String[] serviceArray = availableServices.toArray(new String[0]);
                String selectedService = (String) JOptionPane.showInputDialog(
                        frame, "Select Service:", "Create Appointment",
                        JOptionPane.QUESTION_MESSAGE, null, serviceArray, null
                );

                if (selectedService == null && selectedServicesTechnicians.isEmpty()) {
                    // User canceled without selecting any services
                    return;
                } else if (selectedService == null) {
                    // User is done selecting services
                    break;
                }

                // Get service ID
                int serviceId = SQLFunctions.getServiceIdByName(conn, selectedService);

                // Get technicians who can perform this service
                String techQuery =
                        "SELECT t.TECH_ID, t.FirstName, t.LastName " +
                                "FROM TechsServices ts " +
                                "JOIN Technicians t ON ts.TECH_ID = t.TECH_ID " +
                                "WHERE ts.SERVICE_ID = ?";

                PreparedStatement techStmt = conn.prepareStatement(techQuery);
                techStmt.setInt(1, serviceId);
                ResultSet techRs = techStmt.executeQuery();

                ArrayList<String> techsList = new ArrayList<>();
                Map<String, Integer> techIdMap = new HashMap<>();

                while (techRs.next()) {
                    String techName = techRs.getString("FirstName") + " " + techRs.getString("LastName");
                    techsList.add(techName);
                    techIdMap.put(techName, techRs.getInt("TECH_ID"));
                }

                if (techsList.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "No technicians available for " + selectedService + ". Please assign technicians to this service.",
                            "No Technicians", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                // Select a technician for this service
                String[] techArray = techsList.toArray(new String[0]);
                String selectedTechnician = (String) JOptionPane.showInputDialog(
                        frame, "Select Technician for " + selectedService + ":", "Create Appointment",
                        JOptionPane.QUESTION_MESSAGE, null, techArray, null
                );

                if (selectedTechnician == null) {
                    // User canceled technician selection
                    continue;
                }

                int technicianId = techIdMap.get(selectedTechnician);

                // Add to our selected services
                Map<String, Object> serviceEntry = new HashMap<>();
                serviceEntry.put("service", selectedService);
                serviceEntry.put("serviceId", serviceId);
                serviceEntry.put("technician", selectedTechnician);
                serviceEntry.put("technicianId", technicianId);
                selectedServicesTechnicians.add(serviceEntry);

                // Ask if they want to add more services
                int addMore = JOptionPane.showConfirmDialog(
                        frame, "Add another service to this appointment?",
                        "Add More Services", JOptionPane.YES_NO_OPTION
                );
                addingServices = (addMore == JOptionPane.YES_OPTION);
            }

            // Make sure at least one service was selected
            if (selectedServicesTechnicians.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "You must select at least one service for the appointment.",
                        "No Services Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create summary panel to show all selections
            JPanel summaryPanel = new JPanel(new GridLayout(0, 1));
            summaryPanel.add(new JLabel("Selected Date: " + appointmentDate));
            summaryPanel.add(new JLabel("Selected Time: " + selectedTime));
            summaryPanel.add(new JLabel("Selected Customer: " + selectedCustomer));
            summaryPanel.add(new JLabel("Selected Car: " + selectedCar));

            // Show selected services and technicians
            summaryPanel.add(new JLabel("Selected Services:"));
            for (Map<String, Object> entry : selectedServicesTechnicians) {
                summaryPanel.add(new JLabel("• " + entry.get("service") + " - Technician: " + entry.get("technician")));
            }

            int confirmResult = JOptionPane.showConfirmDialog(
                    frame, summaryPanel, "Confirm Appointment", JOptionPane.OK_CANCEL_OPTION
            );

            if (confirmResult == JOptionPane.OK_OPTION) {
                // Create customer-car relationship if it doesn't exist
                int customerCarId = SQLFunctions.getOrCreateCustomerCar(conn, customerId, carId);

                // Begin transaction
                conn.setAutoCommit(false);
                try {
                    // Create appointment date/time entry
                    String dateTimeSql = "INSERT INTO CustomerAppointmentDateTime (CUST_CAR_ID, AppointmentDate, AppointmentTime) VALUES (?, ?, ?)";

                    // Format date for SQL Server (YYYY-MM-DD)
                    String formattedDate = selectedYear + "-" +
                            (Integer.parseInt(selectedMonth) < 10 ? "0" + selectedMonth : selectedMonth) + "-" +
                            (Integer.parseInt(selectedDay) < 10 ? "0" + selectedDay : selectedDay);

                    PreparedStatement dateTimeStmt = conn.prepareStatement(dateTimeSql, new String[]{"APPOINTMENT_DATE_TIME_ID"});
                    dateTimeStmt.setInt(1, customerCarId);
                    dateTimeStmt.setString(2, formattedDate);
                    dateTimeStmt.setString(3, selectedTime);
                    dateTimeStmt.executeUpdate();

                    ResultSet rs = dateTimeStmt.getGeneratedKeys();
                    int appointmentDateTimeId = 0;
                    if (rs.next()) {
                        appointmentDateTimeId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to get ID for new appointment datetime");
                    }

                    // For each service, create the appointment service entry
                    for (Map<String, Object> serviceEntry : selectedServicesTechnicians) {
                        int techId = (int) serviceEntry.get("technicianId");
                        int servId = (int) serviceEntry.get("serviceId");

                        // Get or create tech-service relationship
                        int techServiceId = SQLFunctions.getOrCreateTechService(conn, techId, servId);

                        // Create appointment service entry
                        String serviceSql = "INSERT INTO CustomerAppointmentService (CAR_DATE_TIME_ID, TECHNICAN_SERIVCE_ID) VALUES (?, ?)";
                        PreparedStatement serviceStmt = conn.prepareStatement(serviceSql);
                        serviceStmt.setInt(1, appointmentDateTimeId);
                        serviceStmt.setInt(2, techServiceId);
                        serviceStmt.executeUpdate();
                    }

                    // Commit transaction
                    conn.commit();

                    // Build details for confirmation message
                    StringBuilder detailsBuilder = new StringBuilder();
                    detailsBuilder.append("Date: ").append(appointmentDate).append("\n");
                    detailsBuilder.append("Time: ").append(selectedTime).append("\n");
                    detailsBuilder.append("Customer: ").append(selectedCustomer).append("\n");
                    detailsBuilder.append("Car: ").append(selectedCar).append("\n");
                    detailsBuilder.append("Services:\n");

                    double totalCost = 0;
                    for (Map<String, Object> entry : selectedServicesTechnicians) {
                        String serviceName = (String) entry.get("service");
                        String techName = (String) entry.get("technician");

                        // Get service cost
                        String costQuery = "SELECT Cost FROM Services WHERE ServiceName = ?";
                        PreparedStatement costStmt = conn.prepareStatement(costQuery);
                        costStmt.setString(1, serviceName);
                        ResultSet costRs = costStmt.executeQuery();

                        double serviceCost = 0;
                        if (costRs.next()) {
                            serviceCost = costRs.getDouble("Cost");
                            totalCost += serviceCost;
                        }

                        detailsBuilder.append("• ").append(serviceName)
                                .append(" - $").append(String.format("%.2f", serviceCost))
                                .append(" (Technician: ").append(techName).append(")\n");
                    }

                    detailsBuilder.append("\nTotal Cost: $").append(String.format("%.2f", totalCost));

                    // Show confirmation
                    JOptionPane.showMessageDialog(
                            frame, "Appointment Created Successfully!\n\n" + detailsBuilder.toString(),
                            "Appointment Created", JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (SQLException ex) {
                    conn.rollback();
                    throw ex;
                } finally {
                    conn.setAutoCommit(true);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    frame, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private static void viewAppointments(JFrame frame) {
        try (Connection conn = SQLFunctions.getConnection()) {
            List<Map<String, String>> dbAppointments = SQLFunctions.getAllAppointments(conn);

            if (dbAppointments.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No appointments found in the database.");
                return;
            }

            // Create a table model for displaying appointments
            String[] columnNames = {"ID", "Customer", "Car", "Technician", "Service", "Cost", "Date", "Time"};
            Object[][] data = new Object[dbAppointments.size()][columnNames.length];

            for (int i = 0; i < dbAppointments.size(); i++) {
                Map<String, String> appointment = dbAppointments.get(i);
                data[i][0] = appointment.get("id");
                data[i][1] = appointment.get("customerName");
                data[i][2] = appointment.get("carDetails");
                data[i][3] = appointment.get("technicianName");
                data[i][4] = appointment.get("serviceName");
                data[i][5] = appointment.get("serviceCost");
                data[i][6] = appointment.get("date");
                data[i][7] = appointment.get("time");
            }

            JTable table = new JTable(data, columnNames);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID column
            table.getColumnModel().getColumn(5).setPreferredWidth(60);  // Cost column
            table.getColumnModel().getColumn(6).setPreferredWidth(80);  // Date column
            table.getColumnModel().getColumn(7).setPreferredWidth(60);  // Time column

            // Make the table sortable
            table.setAutoCreateRowSorter(true);

            // Set default sort by date then time
            RowSorter<? extends TableModel> sorter = table.getRowSorter();
            if (sorter instanceof TableRowSorter) {
                List<RowSorter.SortKey> sortKeys = new ArrayList<>();
                // Sort by date (column 6) first, then by time (column 7)
                sortKeys.add(new RowSorter.SortKey(6, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(7, SortOrder.ASCENDING));
                ((TableRowSorter<? extends TableModel>) sorter).setSortKeys(sortKeys);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(900, 400));

            // Create panel with table and buttons
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();

            // Add reset sort button
            JButton resetSortButton = new JButton("Sort by Date & Time");
            resetSortButton.addActionListener(event -> {
                RowSorter<? extends TableModel> currentSorter = table.getRowSorter();
                if (currentSorter instanceof TableRowSorter) {
                    List<RowSorter.SortKey> sortKeys = new ArrayList<>();
                    sortKeys.add(new RowSorter.SortKey(6, SortOrder.ASCENDING));
                    sortKeys.add(new RowSorter.SortKey(7, SortOrder.ASCENDING));
                    ((TableRowSorter<? extends TableModel>) currentSorter).setSortKeys(sortKeys);
                }
            });
            buttonPanel.add(resetSortButton);

            tablePanel.add(buttonPanel, BorderLayout.SOUTH);

            // Show in a dialog
            JDialog dialog = new JDialog(frame, "All Appointments", true);
            dialog.setContentPane(tablePanel);
            dialog.pack();
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    frame, "Error retrieving appointments: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}