# Proyecto para la Academia Soluciones Informáticas.

## Actualmente solo funciona para la tabla horarios y alumnos:

#### TABLAS PARA MYSQL

CREATE TABLE `academia`.`horario`(  
`id` int (11) AUTO_INCREMENT,  
`horainicio` varchar (150) DEFAULT NULL,  
`horafin` varchar (150) DEFAULT NULL, 
`curso` varchar (150) DEFAULT NULL,   
`instructor` varchar (150) DEFAULT NULL,
PRIMARY KEY (`id`)
);

CREATE TABLE `academia`.`alumnos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `apellido` VARCHAR(45) NULL,
  `nombre` VARCHAR(45) NULL,
  `telefono` VARCHAR(45) NULL,
  `otro` VARCHAR(45) NULL,
  `comunietica` VARCHAR(45) NULL,
  `sexo` VARCHAR(45) NULL,
  `fechanac` DATE NULL,
  `lugarnac` VARCHAR(45) NULL,
  `papependiente` VARCHAR(45) NULL,
  `niveleducativo` VARCHAR(45) NULL,
  `dpi` VARCHAR(45) NULL,
  `extendida` VARCHAR(45) NULL,
  `direccion` VARCHAR(45) NULL,
  `municipio` VARCHAR(45) NULL,
  `departamento` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  
  
  #### TABLAS PARA POSTGRESQL
  
  create table horario (
   id SERIAL NOT NULL,
   	horainicio VARCHAR(40) not null,
   	horafin VARCHAR(40) not null,
	curso VARCHAR(40) not null,
	instructor VARCHAR(40) not null,
   constraint PK_horario primary key (ID)
);
  
  create table alumnos (
   id SERIAL NOT NULL,
   	apellido VARCHAR(40) not null,
   	nombre VARCHAR(40) not null,
	telefono VARCHAR(40) not null,
	otro VARCHAR(40) not null,
	comunietica VARCHAR(40) not null,
	sexo VARCHAR(40) not null,
   	fechanac DATE not null,
	lugarnac VARCHAR(40) not null,
	papependiente VARCHAR(40) not null,
	niveleducativo VARCHAR(40) not null,
	dpi VARCHAR(40) not null,
	extendida VARCHAR(40) not null,
	direccion VARCHAR(40) not null,
	municipio VARCHAR(40) not null,
	departamento VARCHAR(40) not null,
   constraint PK_alumnos primary key (ID)
);

### Link para el script de la base de datos.

https://gist.github.com/E-Samayoa/263f30a60170e580d45eba717ffc202a

Nota: La base de datos no está adaptada en su totalidad al esquema de Spring.
