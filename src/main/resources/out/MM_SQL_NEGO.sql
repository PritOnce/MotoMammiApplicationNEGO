CREATE DATABASE MM_NEGO;
drop database mm_nego;
CREATE TABLE MM_PROVIDERS(
id int primary key auto_increment,
codProv varchar(100) unique,
name varchar(100),
dateIni date,
dateEnd date,
SwiAct boolean
);

CREATE TABLE MM_INTERFACE(
id int primary key auto_increment,
codExternal varchar(100), -- DNI CUSTOMER
codProv varchar(100),
contJson longtext,
creationDate timestamp,
lastUpdate timestamp,
createdBy varchar(100),
updateBy varchar(100),
codError varchar(20),
errorMessage longtext,
statusProccess varchar(10) default 'N',
operation varchar(20),
`resource` varchar(20),
foreign key (codProv) references MM_PROVIDERS(codProv)
);

CREATE TABLE MM_TRANSLATIONS(
id int primary key auto_increment,
codProv varchar(100),
internalCode varchar(100),
externalCode varchar(100),
dateIni date,
dateEnd date,
foreign key (codProv) references MM_PROVIDERS(codProv)
);

CREATE TABLE MM_CUSTOMER(
id int auto_increment primary key,
dni varchar(10) not null unique,
`name` varchar(50),
first_surname varchar(50),
last_surname varchar(50),
email varchar(100),
birth_date date, 
postal_code varchar(20),
street_type varchar(50),
city varchar(50),
`number` int,
phone varchar(50),
gender varchar(20),
license_type varchar(10),
plate varchar(10) unique
);

CREATE TABLE MM_VEHICLES(
id int auto_increment primary key,
plate varchar(10) unique,
vehicle_type varchar(30),
brand varchar(20),
model varchar(20),
dni_customer varchar(10)
);

CREATE TABLE MM_PARTS (
id INT PRIMARY KEY AUTO_INCREMENT,
dni varchar(10) not null,
claim_number VARCHAR(20) UNIQUE,
policy_number VARCHAR(20),
claim_date DATE,
`description` TEXT,
`status` VARCHAR(20),
amount double
);

CREATE TABLE MM_INVOICES (
id INT PRIMARY KEY AUTO_INCREMENT,
invoice_number VARCHAR(20) NOT NULL UNIQUE,
dni VARCHAR(10),
codProv VARCHAR(100),
plate VARCHAR(10),
issue_date DATE, -- Fecha en que se emite la factura.
cost DOUBLE, -- Coste del seguro.
send boolean default false,
FOREIGN KEY (dni) REFERENCES MM_CUSTOMER(dni),
FOREIGN KEY (codProv) REFERENCES MM_PROVIDERS(codProv),
FOREIGN KEY (plate) REFERENCES MM_VEHICLES(plate)
);

CREATE TABLE MM_PROCESS (
id INT PRIMARY KEY auto_increment,
process_name varchar(20),
status boolean default true
);

INSERT INTO MM_PROCESS(process_name, status)
values ('callProcessReadInfo', true),
('callIntegrateInfo', true),
('genInvoiceFile', true);

INSERT INTO MM_INVOICES (invoice_number, dni, codProv, plate, issue_date, cost, send)
VALUES ('INV001', '48827175U', 'CAX', '1369PAO', '2024-03-01', 4864.33, false),
       ('INV002', '04850673H', 'CAX', '4274FFX', '2024-05-02', 4511.68, false),
       ('INV003', '77471925M', 'CAX', '2874SCS', '2024-05-03', 1127.64, false),
       ('INV004', '00353733Y', 'CAX', '2130ONI', '2024-05-04', 2620.00, false),
       ('INV005', '39362662D', 'CAX', '2026WFG', '2024-04-05', 1026.03, false);

SELECT * FROM mm_nego.mm_parts;

SELECT c.dni AS dniCustomer,
       c.name AS nameCustomer,
       c.first_surname AS firstSurname,
       c.last_surname AS lastSurname,
       v.model AS modelVehicle,
       v.plate AS plateVehicle,
       i.cost AS cost,
       i.invoice_number AS invoiceNumber
FROM MM_CUSTOMER c
JOIN MM_VEHICLES v ON c.plate = v.plate
JOIN MM_INVOICES i ON i.plate = v.plate
WHERE DATE_FORMAT(i.issue_date, '%Y%m') = "2024-05";



-- Inserts para MM_PROVIDERS
INSERT INTO MM_PROVIDERS (codProv, name, dateIni, dateEnd, SwiAct) VALUES
('CAX', 'La Caixa', '2024/01/10', '2024/11/19', true),
('ING', 'Banco ING', '2023/05/23', '2024/05/30', true),
('COL', 'COLONIA', '2022/04/07', '2024/03/18', true),
('BBVA', 'Banco BBVA', '2024/03/13', '2025/01/10', false);

-- Inserts para MM_TRANSLATIONS
INSERT INTO MM_TRANSLATIONS (codProv, internalCode, externalCode, dateIni, dateEnd) VALUES
('CAX', 'C/', 'Car', '2024/03/13', '2099/12/31'),
('CAX','AV', 'Aven', '2024/03/13', '2099/12/31'),
('CAX','PL', 'Plz', '2024/03/13', '2099/12/31'),
('ING','C/', 'Calle', '2024/03/13', '2099/12/31'),
('ING','AV', 'Avenida', '2024/03/13', '2099/12/31'),
('ING','PL', 'P.', '2024/03/13', '2099/12/31'),
('COL','C/', 'Carrer', '2024/03/13', '2099/12/31'),
('COL','AV', 'Avenguda', '2024/03/13', '2099/12/31'),
('COL','PL', 'Plaça', '2024/03/13', '2099/12/31'),
('BBVA','C/', 'Camino', '2024/03/13', '2099/12/31'),
('BBVA','AV', 'Avend', '2024/03/13', '2099/12/31'),
('BBVA','PL', 'P/', '2024/03/13', '2099/12/31');

SELECT c.dni,c.nombre,c.ap1,v.model,v.plate, i.cost,i.invoice_number* 
FROM mm_nego.mm_invoices i, mm_nego.mm_customer c, mm_nego.mm_vehicles v ;
