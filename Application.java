import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Technician");
            System.out.println("3. Add Car to Customer");
            System.out.println("4. Add Service to Technician");
            System.out.println("5. Create Appointment");
            System.out.println("6. View Appointments");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add Customer
                    System.out.println("Enter Customer Name:");
                    String customerName = scanner.nextLine();

                    System.out.println("Enter Customer Phone Number:");
                    String customerPhone = scanner.nextLine();

                    Customer customer = new Customer();
                    customer.setName(customerName);
                    customer.setPhoneNumber(customerPhone);

                    DAOManager.CustomerDAO.addCustomer(customer);
                    System.out.println("Customer added successfully!");
                    break;

                case 2:
                    // Add Technician
                    System.out.println("Enter Technician Name:");
                    String technicianName = scanner.nextLine();

                    Technician technician = new Technician();
                    technician.setName(technicianName);

                    DAOManager.TechnicianDAO.addTechnician(technician);
                    System.out.println("Technician added successfully!");
                    break;

                case 3:
                    // Add Car to Customer
                    List<Customer> customers = DAOManager.CustomerDAO.getAllCustomers();
                    if (customers.isEmpty()) {
                        System.out.println("No customers available. Add a customer first.");
                        break;
                    }

                    System.out.println("Select a Customer:");
                    for (Customer c : customers) {
                        System.out.println(c.getId() + ". " + c.getName());
                    }
                    int customerId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Customer selectedCustomer = customers.stream()
                            .filter(c -> c.getId() == customerId)
                            .findFirst()
                            .orElse(null);

                    if (selectedCustomer == null) {
                        System.out.println("Invalid customer ID!");
                        break;
                    }

                    System.out.println("Enter Car Make:");
                    String carMake = scanner.nextLine();

                    System.out.println("Enter Car Model:");
                    String carModel = scanner.nextLine();

                    System.out.println("Enter License Plate:");
                    String licensePlate = scanner.nextLine();

                    Car car = new Car();
                    car.setMake(carMake);
                    car.setModel(carModel);
                    car.setLicensePlate(licensePlate);

                    DAOManager.CarDAO.addCar(car, selectedCustomer.getId());
                    System.out.println("Car added successfully to customer: " + selectedCustomer.getName());
                    break;

                case 4:
                    // Add Service to Technician
                    List<Technician> technicians = DAOManager.TechnicianDAO.getAllTechnicians();
                    if (technicians.isEmpty()) {
                        System.out.println("No technicians available. Add a technician first.");
                        break;
                    }

                    System.out.println("Select a Technician:");
                    for (Technician t : technicians) {
                        System.out.println(t.getId() + ". " + t.getName());
                    }
                    int technicianId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Technician selectedTechnician = technicians.stream()
                            .filter(t -> t.getId() == technicianId)
                            .findFirst()
                            .orElse(null);

                    if (selectedTechnician == null) {
                        System.out.println("Invalid technician ID!");
                        break;
                    }

                    System.out.println("Enter Service Name:");
                    String serviceName = scanner.nextLine();

                    DAOManager.ServiceDAO.addService(technicianId, serviceName);
                    System.out.println("Service added successfully to technician: " + selectedTechnician.getName());
                    break;

                case 5:
                    // Create Appointment
                    customers = DAOManager.CustomerDAO.getAllCustomers();
                    technicians = DAOManager.TechnicianDAO.getAllTechnicians();
                    if (customers.isEmpty() || technicians.isEmpty()) {
                        System.out.println("You need to add customers and technicians first!");
                        break;
                    }

                    // Select Customer
                    System.out.println("Select a Customer:");
                    for (Customer c : customers) {
                        System.out.println(c.getId() + ". " + c.getName());
                    }
                    customerId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    selectedCustomer = customers.stream()
                            .filter(c -> c.getId() == customerId)
                            .findFirst()
                            .orElse(null);

                    if (selectedCustomer == null) {
                        System.out.println("Invalid customer ID!");
                        break;
                    }

                    // Select Car
                    List<Car> cars = DAOManager.CarDAO.getCarsByCustomerId(selectedCustomer.getId());
                    if (cars.isEmpty()) {
                        System.out.println("No cars available for this customer. Add a car first.");
                        break;
                    }

                    System.out.println("Select a Car:");
                    for (Car c : cars) {
                        System.out.println(c.getId() + ". " + c.getMake() + " " + c.getModel() + " (" + c.getLicensePlate() + ")");
                    }
                    int carId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Car selectedCar = cars.stream()
                            .filter(c -> c.getId() == carId)
                            .findFirst()
                            .orElse(null);

                    if (selectedCar == null) {
                        System.out.println("Invalid car ID!");
                        break;
                    }

                    // Select Technician
                    System.out.println("Select a Technician:");
                    for (Technician t : technicians) {
                        System.out.println(t.getId() + ". " + t.getName());
                    }
                    technicianId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    selectedTechnician = technicians.stream()
                            .filter(t -> t.getId() == technicianId)
                            .findFirst()
                            .orElse(null);

                    if (selectedTechnician == null) {
                        System.out.println("Invalid technician ID!");
                        break;
                    }

                    // Select Service
                    List<String> services = DAOManager.ServiceDAO.getServicesByTechnicianId(selectedTechnician.getId());
                    if (services.isEmpty()) {
                        System.out.println("No services available for this technician. Add a service first.");
                        break;
                    }

                    System.out.println("Select a Service:");
                    for (int i = 0; i < services.size(); i++) {
                        System.out.println((i + 1) + ". " + services.get(i));
                    }
                    int serviceIndex = scanner.nextInt() - 1;
                    scanner.nextLine(); // Consume newline

                    if (serviceIndex < 0 || serviceIndex >= services.size()) {
                        System.out.println("Invalid service selection!");
                        break;
                    }
                    serviceName = services.get(serviceIndex);

                    // Enter Appointment Time and Day
                    System.out.println("Enter Appointment Day:");
                    String day = scanner.nextLine();

                    System.out.println("Enter Appointment Time:");
                    String time = scanner.nextLine();

                    // Create Appointment
                    int serviceId = DAOManager.ServiceDAO.getServiceId(selectedTechnician.getId(), serviceName);

                    Appointment appointment = new Appointment();
                    appointment.setCustomerId(selectedCustomer.getId());
                    appointment.setCarId(selectedCar.getId());
                    appointment.setTechnicianId(selectedTechnician.getId());
                    appointment.setServiceId(serviceId);
                    appointment.setAppointmentDay(day);
                    appointment.setAppointmentTime(time);

                    DAOManager.AppointmentDAO.addAppointment(appointment);
                    System.out.println("Appointment created successfully!");
                    break;

                case 6:
                    // View Appointments
                    List<Appointment> appointments = DAOManager.AppointmentDAO.getAllAppointments();
                    if (appointments.isEmpty()) {
                        System.out.println("No appointments found!");
                    } else {
                        for (Appointment a : appointments) {
                            System.out.println("Appointment ID: " + a.getId());
                            System.out.println("Customer: " + a.getCustomerName());
                            System.out.println("Car: " + a.getCarMake() + " " + a.getCarModel() + " (" + a.getLicensePlate() + ")");
                            System.out.println("Technician: " + a.getTechnicianName());
                            System.out.println("Service: " + a.getServiceName());
                            System.out.println("Day: " + a.getAppointmentDay());
                            System.out.println("Time: " + a.getAppointmentTime());
                            System.out.println("-------------------------");
                        }
                    }
                    break;

                case 7:
                    // Exit
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}