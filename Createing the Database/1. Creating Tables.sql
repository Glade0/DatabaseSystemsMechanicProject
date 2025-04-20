CREATE TABLE Customers /*Information for Customers*/
(
    CUST_ID INT IDENTITY(1,1) PRIMARY KEY, /*Primary Key + Identity (auto incrementing the ID)*/
    FirstName NVARCHAR(50), LastName NVARCHAR(50), PhoneNumber NVARCHAR(50)
);

Create Table MakeModel 
/*Composite Table for Make and Models*/
/*Splitting Make and Model doesn't make sense to me as models are based on makes.*/
(
	 MM_ID INT IDENTITY(1,1) PRIMARY KEY, Make NVARCHAR(50), Model NVARCHAR(50),
)


CREATE TABLE Cars /*Table for Cars*/
(
    CAR_ID INT IDENTITY(1,1) PRIMARY KEY, MakeModel_ID INT, LicensePlate NVARCHAR(50) /*FK*/
    CONSTRAINT MakeModelFK FOREIGN KEY (MakeModel_ID) REFERENCES MakeModel(MM_ID)
);


CREATE TABLE CustomerCars /*Tables that connect the customers and cars.*/
(
    CUSTCAR_ID INT IDENTITY(1,1) PRIMARY KEY, Customer_ID INT /*FK*/, Car_ID INT /*FK*/,
    CONSTRAINT CustomerIDFK FOREIGN KEY (Customer_ID) 
	REFERENCES Customers(CUST_ID),
    CONSTRAINT CAR_ID_FK FOREIGN KEY (Car_ID) 
	REFERENCES Cars(CAR_ID)
);

	Create Table [Services] 
	/*Table for the services and their cost*/
	/*Might have to do special formatting when referencing*/
	(
		SERVICE_ID INT IDENTITY(1,1) PRIMARY KEY, /*Primary Key + Identity (auto incrementing the ID)*/
		ServiceName NVARCHAR(50),
		Cost int
	)

	create table Technicians /*Tables for the Technicans*/
	(
		TECH_ID INT IDENTITY(1,1) PRIMARY KEY, /*Primary Key + Identity (auto incrementing the ID)*/
		FirstName NVARCHAR(50),
        LastName NVARCHAR(50),
	)

	Create Table TechsServices 
	/*The Table for the services the technicans can provide, linking them*/
	(
		TECHSERVICE_ID INT IDENTITY(1,1) PRIMARY KEY,SERVICE_ID int /*FK*/, TECH_ID int /*FK*/
		CONSTRAINT FK_ServiceTechsServices FOREIGN KEY (SERVICE_ID)
		REFERENCES [Services] (SERVICE_ID),
		CONSTRAINT FK_TechnicianTechsServices FOREIGN KEY (TECH_ID)
		REFERENCES Technicians (TECH_ID)
	)

CREATE TABLE CustomerAppointmentDateTime /*Table with the appointment date and time info*/
(
    APPOINTMENT_DATE_TIME_ID INT IDENTITY(1,1) PRIMARY KEY,
    CUST_CAR_ID INT /*FK*/,
    AppointmentDate DATE,
    AppointmentTime TIME,
    CONSTRAINT APPOINTMENTCUSTCAR FOREIGN KEY (CUST_CAR_ID) REFERENCES CustomerCars(CUSTCAR_ID)
);

CREATE TABLE CustomerAppointmentService /*Table that lists the service that will happen during and appointment.*/
(
    APPOINTMENT_SERVICE_ID INT IDENTITY(1,1) PRIMARY KEY,
    CAR_DATE_TIME_ID INT /*FK*/,TECHNICAN_SERIVCE_ID INT /*FK*/,
    CONSTRAINT CARINFO FOREIGN KEY (CAR_DATE_TIME_ID) REFERENCES CustomerAppointmentDateTime (APPOINTMENT_DATE_TIME_ID),
    CONSTRAINT TECHNICIANSERVICE FOREIGN KEY (TECHNICAN_SERIVCE_ID) REFERENCES TechsServices (TECHSERVICE_ID),
);