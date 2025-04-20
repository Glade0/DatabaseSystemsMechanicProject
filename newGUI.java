 import java.awt.*;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.HashMap;
 import javax.swing.*;
 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 import java.util.List;
 import java.util.Map;
 

 public class newGUI {
 

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
  // Load Customers
  List<String> customers = SQLFunctions.getAllCustomers(conn);
  for (String customer : customers) {
  customerModel.addElement(customer);
  customerCars.put(customer, new ArrayList<>());
  }
 

  // Load Technicians
  List<String> technicians = SQLFunctions.getAllTechnicians(conn);
  for (String technician : technicians) {
  technicianModel.addElement(technician);
  technicianServices.put(technician, new ArrayList<>());
  }
 

  // Load Makes
  List<String> makes = SQLFunctions.getAllMakes(conn);
  for (String make : makes) {
  makeModel.addElement(make);
  }
 

  // Load Models
  List<String> models = SQLFunctions.getAllModels(conn);
  for (String model : models) {
  modelModel.addElement(model);
  }
 

  // Load Services
  List<String> services = SQLFunctions.getAllServices(conn);
  for (String service : services) {
  serviceModel.addElement(service);
  }
  } catch (SQLException e) {
  JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
  e.printStackTrace();
  }
 
    // Add Customer Button
    JButton addCustomerButton = new JButton("Add Customer");
    addCustomerButton.addActionListener(e -> {
   String customerFirstName = JOptionPane.showInputDialog(frame, "Enter Customer First Name:"); // Get first name
   String customerLastName = JOptionPane.showInputDialog(frame, "Enter Customer Last Name:"); // Get last name
   String customerPhone = JOptionPane.showInputDialog(frame, "Enter Customer Phone Number:");
   if (customerFirstName != null && customerLastName != null && customerPhone != null) { //Check for both names
   try (Connection conn = SQLFunctions.getConnection()) {
   SQLFunctions.addCustomer(conn, customerFirstName, customerLastName, customerPhone); // Call addCustomer with first and last name
   customerModel.addElement(customerFirstName + " " + customerLastName); // Add to dropdown (full name)
   customerCars.put(customerFirstName + " " + customerLastName, new ArrayList<>()); // Initialize car list for the customer
   JOptionPane.showMessageDialog(frame, "Customer Added: " + customerFirstName + " " + customerLastName + ", " + customerPhone); //Display full name
   } catch (SQLException ex) {
   JOptionPane.showMessageDialog(frame, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
   ex.printStackTrace();
   }
   }
    });
    frame.add(addCustomerButton);

  // Add Car Button
  JButton addCarButton = new JButton("Add Car");
  addCarButton.addActionListener(e -> {
      JComboBox<String> makeDropdown = new JComboBox<>(makeModel);
      DefaultComboBoxModel<String> availableModelsModel = new DefaultComboBoxModel<>();
      JComboBox<String> modelDropdown = new JComboBox<>(availableModelsModel);
      JTextField licensePlateField = new JTextField();
      //when a make is selected, update the model dropdown
makeDropdown.addActionListener(event -> {
 String selectedMake = (String) makeDropdown.getSelectedItem();
 availableModelsModel.removeAllElements();
 try (Connection conn = SQLFunctions.getConnection()) {
  List<String> models = SQLFunctions.getModelsByMake(conn, selectedMake);
  for (String model : models) {
   availableModelsModel.addElement(model);
  }
 } catch (SQLException ex) {
  JOptionPane.showMessageDialog(frame, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
  ex.printStackTrace();
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


                  //makeModel.addElement(selectedMake); //dont add to the dropdowns
                  //modelModel.addElement(selectedModel);


                  JOptionPane.showMessageDialog(frame, "Car Added!");
              } catch (SQLException ex) {
                  JOptionPane.showMessageDialog(frame, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                  ex.printStackTrace();
              }
          } else {
              JOptionPane.showMessageDialog(frame, "Make, Model, and License Plate are required!", "Error", JOptionPane.ERROR_MESSAGE);
          }
      }
  });
  frame.add(addCarButton);

 // Add Service Button
 JButton addServiceButton = new JButton("Add Service");
 addServiceButton.addActionListener(e -> {
JTextField serviceNameField = new JTextField();
JTextField serviceCostField = new JTextField(); // Added cost field
JPanel panel = new JPanel(new GridLayout(0, 1));
panel.add(new JLabel("Service Name:"));
panel.add(serviceNameField);
panel.add(new JLabel("Service Cost:")); // Added cost label
panel.add(serviceCostField);


int result = JOptionPane.showConfirmDialog(frame, panel, "Add Service", JOptionPane.OK_CANCEL_OPTION);
if (result == JOptionPane.OK_OPTION) {
String serviceName = serviceNameField.getText();
String serviceCostText = serviceCostField.getText(); // Get cost as text


if (!serviceName.isEmpty() && !serviceCostText.isEmpty()) {
try {
double serviceCost = Double.parseDouble(serviceCostText); // Parse cost to double
try (Connection conn = SQLFunctions.getConnection()) {
SQLFunctions.addService(conn, serviceName, serviceCost);


serviceModel.addElement(serviceName);


JOptionPane.showMessageDialog(frame, "Service Added!");
} catch (SQLException ex) {
JOptionPane.showMessageDialog(frame, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
ex.printStackTrace();
}
} catch (NumberFormatException ex) {
JOptionPane.showMessageDialog(frame, "Invalid cost format!", "Error", JOptionPane.ERROR_MESSAGE);
}
} else {
JOptionPane.showMessageDialog(frame, "Service name and cost required!", "Error", JOptionPane.ERROR_MESSAGE);
}
}
 });
 frame.add(addServiceButton);

 

  // Add Technician Button
  JButton addTechnicianButton = new JButton("Add Technician");
  addTechnicianButton.addActionListener(e -> {
      JTextField firstNameField = new JTextField();
      JTextField lastNameField = new JTextField();
      JPanel panel = new JPanel(new GridLayout(0, 1));
      panel.add(new JLabel("First Name:"));
      panel.add(firstNameField);
      panel.add(new JLabel("Last Name:"));
      panel.add(lastNameField);
      
      int result = JOptionPane.showConfirmDialog(frame, panel, "Add Technician", JOptionPane.OK_CANCEL_OPTION);
      if (result == JOptionPane.OK_OPTION) {
          String firstName = firstNameField.getText();
          String lastName = lastNameField.getText();
          
          if (!firstName.isEmpty() && !lastName.isEmpty()) {
              try (Connection conn = SQLFunctions.getConnection()) {
                  SQLFunctions.addTechnician(conn, firstName, lastName);
                  String fullName = firstName + " " + lastName;
                  technicianModel.addElement(fullName);
                  technicianServices.put(fullName, new ArrayList<>()); // Keep this for UI consistency
                  JOptionPane.showMessageDialog(frame, "Technician Added: " + fullName);
              } catch (SQLException ex) {
                  JOptionPane.showMessageDialog(frame, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                  ex.printStackTrace();
              }
          } else {
              JOptionPane.showMessageDialog(frame, "Both first and last name are required!", "Error", JOptionPane.ERROR_MESSAGE);
          }
      }
  });
  frame.add(addTechnicianButton);
 

// Replace the linkCarButton action listener with this:
JButton linkCarButton = new JButton("Associate Car to Customer");
linkCarButton.addActionListener(e -> {
    JComboBox<String> customerDropdown = new JComboBox<>(customerModel);
    DefaultComboBoxModel<String> carModel = new DefaultComboBoxModel<>();
    try (Connection conn = SQLFunctions.getConnection()) {
        List<String> cars = SQLFunctions.getAllCars(conn);
        for (String car : cars) {
            carModel.addElement(car);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(frame, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
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
                
                // Keep the HashMap update for UI consistency
                customerCars.get(selectedCustomer).add(selectedCar);
                
                JOptionPane.showMessageDialog(frame, "Car Linked to Customer!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select both Customer and Car!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});
frame.add(linkCarButton);

  // Replace the assignServiceButton action listener with this:
JButton assignServiceButton = new JButton("Associate Service to Technician");
assignServiceButton.addActionListener(e -> {
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
                
                // Keep the HashMap update for UI consistency
                technicianServices.get(selectedTechnician).add(selectedService);
                
                JOptionPane.showMessageDialog(frame, "Service Assigned to Technician!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Select both Technician and Service!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});
  frame.add(assignServiceButton);
 

  JButton createAppointmentButton = new JButton("Create Appointment");
  createAppointmentButton.addActionListener(e -> {
      try (Connection conn = SQLFunctions.getConnection()) {
          // 1. Date & Time Selection (moved to be first)
          String[] timeSlots = { 
              "09:00", "10:00", "11:00", "12:00", "13:00", 
              "14:00", "15:00", "16:00", "17:00" 
          };
          JComboBox<String> timeDropdown = new JComboBox<>(timeSlots);
          
          String[] appointmentDays = { 
              "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", 
              "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", 
              "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" 
          };
          String[] appointmentMonths = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
          
          JComboBox<String> dateDropdown = new JComboBox<>(appointmentDays);
          JComboBox<String> monthsDropdown = new JComboBox<>(appointmentMonths);
          
          // Create panel for date/time selection
          JPanel dateTimePanel = new JPanel(new GridLayout(0, 1));
          dateTimePanel.add(new JLabel("Select Date (Day/Month):"));
          
          JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
          datePanel.add(dateDropdown);
          datePanel.add(new JLabel("/"));
          datePanel.add(monthsDropdown);
          dateTimePanel.add(datePanel);
          
          dateTimePanel.add(new JLabel("Select Time:"));
          dateTimePanel.add(timeDropdown);
          
          int dateTimeResult = JOptionPane.showConfirmDialog(
              frame, dateTimePanel, "Pick Date and Time", JOptionPane.OK_CANCEL_OPTION
          );
          
          if (dateTimeResult != JOptionPane.OK_OPTION) return;
          
          String selectedTime = (String) timeDropdown.getSelectedItem();
          String selectedDay = (String) dateDropdown.getSelectedItem();
          String selectedMonth = (String) monthsDropdown.getSelectedItem();
          
          if (selectedTime == null || selectedDay == null || selectedMonth == null) {
              JOptionPane.showMessageDialog(
                  frame, "Date and time must be selected!", 
                  "Validation Error", JOptionPane.ERROR_MESSAGE
              );
              return;
          }
          
          // Format date as day/month for display
          String appointmentDate = selectedDay + "/" + selectedMonth;
          
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
                  
                  // Parse date components
                  // Format for SQL Server (YYYY-MM-DD)
                  String formattedDate = "2024-" + 
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
  });
  frame.add(createAppointmentButton);
 
//View all appointments
  // Replace the viewAppointmentsButton action listener with this improved version:

JButton viewAppointmentsButton = new JButton("View All Appointments");
viewAppointmentsButton.addActionListener(e -> {
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
        
        // Make the table sortable (optional)
        table.setAutoCreateRowSorter(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        

        
        // Create panel with table and buttons
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
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
});
  frame.add(viewAppointmentsButton);
 

  JButton exitButton = new JButton("Exit");
  exitButton.addActionListener(e -> System.exit(0));
  frame.add(exitButton);
 

  frame.setVisible(true);
  }
 }