CREATE DATABASE MM_NEGO;

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
plate varchar(10),
vehicle_type varchar(30),
brand varchar(20),
model varchar(20),
foreign key (plate) references MM_CUSTOMER(plate)
);

-- Inserts para MM_PROVIDERS
INSERT INTO MM_PROVIDERS (codProv, name, dateIni, dateEnd, SwiAct) VALUES
('CAX', 'La Caixa', '2024/01/10', '2024/11/19', true),
('ING', 'Banco ING', '2023/05/23', '2024/05/23', true),
('COL', 'COLONIA', '2022/04/07', '2024/03/18', true),
('BBVA', 'Banco BBVA', '2024/03/13', '2025/01/10', false);

-- Inserts para MM_TRANSLATIONS
INSERT INTO MM_TRANSLATIONS (codProv, internalCode, externalCode, dateIni, dateEnd) VALUES
('CAX', 'C/', 'Car', '2024/03/13', NULL),
('CAX','AV', 'Aven', '2024/03/13', NULL),
('CAX','PL', 'Plz', '2024/03/13', NULL),
('ING','C/', 'Calle', '2024/03/13', NULL),
('ING','AV', 'Avenida', '2024/03/13', NULL),
('ING','PL', 'P.', '2024/03/13', NULL),
('COL','C/', 'Carrer', '2024/03/13', NULL),
('COL','AV', 'Avenguda', '2024/03/13', NULL),
('COL','PL', 'Plaça', '2024/03/13', NULL),
('BBVA','C/', 'Camino', '2024/03/13', NULL),
('BBVA','AV', 'Avend', '2024/03/13', NULL),
('BBVA','PL', 'P/', '2024/03/13', NULL);
