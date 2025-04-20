-- Insert data into Customers
INSERT INTO Customers (FirstName, LastName, PhoneNumber) VALUES
('Jacob', 'Smith', 5701234567), -- Added NULL for phone number as it was not provided.
('Sherry', 'Jones', 5702345678),
('Jennifer', 'Johnson', 5703456789);

-- Insert data into MakeModel
INSERT INTO MakeModel (Make, Model) VALUES
('Honda', 'Civic'),  -- Added 'Unknown' as Make is not provided
('Honda', 'Accord'),
('Honda', 'CR-V'),
('Toyota', 'Camry'),
('Toyota', 'Prius'),
('Toyota', 'Sienna'),
('Ford', 'Focus'),
('Ford', 'Explorer'),
('Ford', 'F-150');

-- Insert data into Cars.  We'll assume a way to link license plates to MakeModel.  Using 'Unknown' for now.
INSERT INTO Cars (MakeModel_ID, LicensePlate)
VALUES
(1, 'KW123'), -- Jacob Smith - KW123
(2, '9H277'), -- Jacob Smith - 9H277
(4, '2022Y6'), -- Sherry Jones - 2022Y6
(7, 'S3E246'), -- Jennifer Johnson - S3E246
(8, 'D68G67'), -- Jennifer Johnson - D68G67
(9, 'KQ963'); -- Jennifer Johnson - KQ963


-- Insert data into CustomerCars
INSERT INTO CustomerCars (Customer_ID, Car_ID) VALUES
(1, 1), -- Jacob Smith - KW123
(1, 2), -- Jacob Smith - 9H277
(2, 3), -- Sherry Jones - 2022Y6
(3, 4), -- Jennifer Johnson - S3E246
(3, 5), -- Jennifer Johnson - D68G67
(3, 6); -- Jennifer Johnson - KQ963

-- Insert data into Services
INSERT INTO [Services] (ServiceName, Cost) VALUES
('Oil Change', 49.99),
('Tire Rotation', 10.50),
('Brake Pads', 125.99),
('Inspection', 59.00),
('Replace brake line', 200.00); -- added service

-- Insert data into Technicians
INSERT INTO Technicians (FirstName, LastName) VALUES
('Daniel', 'Hogan'),
('Margery', 'Hope'),
('Charlie', 'Cole'),
('Joe', 'Smith');

-- Insert data into TechsServices
INSERT INTO TechsServices (SERVICE_ID, TECH_ID) VALUES
(1, 1), -- Daniel Hogan - Oil Change
(3, 1), -- Daniel Hogan - Brake Pads
(1, 2), -- Margery Hope - Oil Change
(2, 2), -- Margery Hope - Tire Rotation
(4, 2), -- Margery Hope - Inspection
(3, 2), -- Margery Hope - Brake Pads
(5, 3),  -- Charlie Cole - Replace brake line
(1, 4);  -- Joe Smith - Oil Change

-- Insert data into CustomerAppointmentDateTime
INSERT INTO CustomerAppointmentDateTime (CUST_CAR_ID, AppointmentDate, AppointmentTime) VALUES
(5, '05-03-2025', '11:00'), -- Jennifer Johnson - D68G67 - 5/3/2024 11am
(3, '05-03-2025', '13:00'), -- Sherry Jones - 2022Y6 - 5/3/2024 1pm
(2, '05-03-2025', '14:00'),  -- Jacob Smith - 9H277 - 5/3/2024 2pm
(4, '05-04-2025', '11:00');  -- Jennifer Johnson - S3E246 - 5/4/2024 11am

-- Insert data into CustomerAppointmentService
INSERT INTO CustomerAppointmentService (CAR_DATE_TIME_ID, TECHNICAN_SERIVCE_ID) VALUES
(1, 2), -- Jennifer Johnson D68G67 5/3/2024 11am  Daniel Hogan – Brake Pads
(1, 1), -- Jennifer Johnson D68G67 5/3/2024 11am Margery Hope – Oil Change
(1, 4), -- Jennifer Johnson D68G67 5/3/2024 11am Margery Hope – Tire Rotation
(2, 4), -- Sherry Jones 2022Y6 5/3/2024 1pm Margery Hope – Inspection
(2, 1), -- Sherry Jones 2022Y6 5/3/2024 1pm Daniel Hogan – Oil Change
(2, 2),  -- Sherry Jones 2022Y6 5/3/2024 1pm Margery Hope – Tire Rotation
(2, 3),  -- Sherry Jones 2022Y6 5/3/2024 1pm Daniel Hogan – Brake Pads
(3, 1), -- Jacob Smith 9H277 5/3/2024 2pm Daniel Hogan – Oil Change
(3, 2), -- Jacob Smith 9H277 5/3/2024 2pm Margery Hope – Tire Rotation
(3, 3), -- Jacob Smith 9H277 5/3/2024 2pm Daniel Hogan – Brake Pads
(3, 4), -- Jacob Smith 9H277 5/3/2024 2pm Margery Hope – Inspection
(3,5),  -- Jacob Smith 9H277 5/3/2024 2pm Daniel Hogan - replace brake line
(4, 1), -- Jennifer Johnson S3E246 5/4/2024 11am Daniel Hogan – Oil Change
(4, 3),  -- Jennifer Johnson S3E246 5/4/2024 11am Daniel Hogan – Brake Pads
(4,7);  -- Jennifer Johnson S3E246 5/4/2024 11am Charlie Cole - replace brake line