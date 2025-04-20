class Date {
    private int day;
    private int month;
    private int year;
   
  
    public Date(int day, int month) {
    this.day = day;
    this.month = month;
    year = 2024; // Or get the current year if needed
    }
   
  
    public int getDay() {
    return day;
    }
   
  
    public void setDay(int day) {
    this.day = day;
    }
   
  
    public int getMonth() {
    return month;
    }
   
  
    public void setMonth(int month) {
    this.month = month;
    }
   
  
    public int getYear() {
    return year;
    }
   
  
    public String toString() {
    return day + "/" + month + "/" + year;
    }
   }
   
  
   class Appointment {
    private int id;
    private int customerId;
    private int carId;
    private int technicianId;
    private int serviceId;
    private String customerName;
    private String carMake;
    private String carModel;
    private String licensePlate;
    private String technicianName;
    private String serviceName;
    private Date appointmentDate;
    private String appointmentTime;
   
  
    // Getters and Setters (all of them)
    public int getId() {
    return id;
    }
   
  
    public void setId(int id) {
    this.id = id;
    }
   
  
    public int getCustomerId() {
    return customerId;
    }
   
  
    public void setCustomerId(int customerId) {
    this.customerId = customerId;
    }
   
  
    public int getCarId() {
    return carId;
    }
   
  
    public void setCarId(int carId) {
    this.carId = carId;
    }
   
  
    public int getTechnicianId() {
    return technicianId;
    }
   
  
    public void setTechnicianId(int technicianId) {
    this.technicianId = technicianId;
    }
   
  
    public int getServiceId() {
    return serviceId;
    }
   
  
    public void setServiceId(int serviceId) {
    this.serviceId = serviceId;
    }
   
  
    public String getCustomerName() {
    return customerName;
    }
   
  
    public void setCustomerName(String customerName) {
    this.customerName = customerName;
    }
   
  
    public String getCarMake() {
    return carMake;
    }
   
  
    public void setCarMake(String carMake) {
    this.carMake = carMake;
    }
   
  
    public String getCarModel() {
    return carModel;
    }
   
  
    public void setCarModel(String carModel) {
    this.carModel = carModel;
    }
   
  
    public String getLicensePlate() {
    return licensePlate;
    }
   
  
    public void setLicensePlate(String licensePlate) {
    this.licensePlate = licensePlate;
    }
   
  
    public String getTechnicianName() {
    return technicianName;
    }
   
  
    public void setTechnicianName(String technicianName) {
    this.technicianName = technicianName;
    }
   
  
    public String getServiceName() {
    return serviceName;
    }
   
  
    public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
    }
   
  
    public Date getAppointmentDate() {
    return appointmentDate;
    }
   
  
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
   
  
    public String getAppointmentTime() {
    return appointmentTime;
    }
   
  
    public void setAppointmentTime(String appointmentTime) {
    this.appointmentTime = appointmentTime;
    }
   }