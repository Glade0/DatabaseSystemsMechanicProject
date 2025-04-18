import java.util.ArrayList;
import java.util.List;

public class ExampleData {
    public static List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();

        Customer customer1 = new Customer();
        customer1.setId(1);
        customer1.setName("John Doe");
        customer1.setPhoneNumber("123-456-7890");

        Car car1 = new Car();
        car1.setId(1);
        car1.setMake("Toyota");
        car1.setModel("Camry");
        customer1.addCar(car1);

        Car car2 = new Car();
        car2.setId(2);
        car2.setMake("Honda");
        car2.setModel("Civic");
        customer1.addCar(car2);

        customers.add(customer1);

        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setName("Jane Smith");
        customer2.setPhoneNumber("987-654-3210");

        Car car3 = new Car();
        car3.setId(3);
        car3.setMake("Ford");
        car3.setModel("Focus");
        customer2.addCar(car3);

        customers.add(customer2);

        return customers;
    }

    public static List<Technician> getTechnicians() {
        List<Technician> technicians = new ArrayList<>();

        Technician technician1 = new Technician();
        technician1.setId(1);
        technician1.setName("Mike Johnson");
        technician1.addService("Oil Change");
        technician1.addService("Brake Inspection");

        Technician technician2 = new Technician();
        technician2.setId(2);
        technician2.setName("Sarah Lee");
        technician2.addService("Tire Rotation");
        technician2.addService("Engine Diagnostics");

        technicians.add(technician1);
        technicians.add(technician2);

        return technicians;
    }
}