CREATE DATABASE IF NOT EXISTS bolsa_empleo;
USE bolsa_empleo;

-- LIMPIEZA (Orden correcto por llaves foráneas)
DROP TABLE IF EXISTS puesto_caracteristica; -- NUEVA
DROP TABLE IF EXISTS oferente_caracteristica;
DROP TABLE IF EXISTS puesto;
DROP TABLE IF EXISTS oferente;
DROP TABLE IF EXISTS empresa;
DROP TABLE IF EXISTS caracteristica;
DROP TABLE IF EXISTS usuario;

-- 1. USUARIOS (Para el Login con correo y clave)
CREATE TABLE usuario (
    email VARCHAR(100) PRIMARY KEY,
    clave VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL, -- 'ADMIN', 'EMPRESA', 'OFERENTE'
    estado TINYINT(1) DEFAULT 0 -- Para que el Admin apruebe
);

-- 2. EMPRESAS (Datos: nombre, localización, tel, descripción)
CREATE TABLE empresa (
    email VARCHAR(100) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    localizacion VARCHAR(200),
    telefono VARCHAR(20),
    descripcion TEXT,
    CONSTRAINT fk_empresa_usuario FOREIGN KEY (email) REFERENCES usuario(email)
);

-- 3. OFERENTES (Para que la empresa pueda ver sus detalles y PDF)
CREATE TABLE oferente (
    cedula VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    nacionalidad VARCHAR(50),
    telefono VARCHAR(20),
    residencia VARCHAR(200),
    email VARCHAR(100) UNIQUE,
    curriculo_path VARCHAR(255), -- Para el PDF
    CONSTRAINT fk_oferente_usuario FOREIGN KEY (email) REFERENCES usuario(email)
);

-- 4. CARACTERISTICAS (Habilidades)
CREATE TABLE caracteristica (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_padre INT, 
    FOREIGN KEY (id_padre) REFERENCES caracteristica(id)
);

-- 5. PUESTOS (Descripción, salario, pública/privada, estado)
CREATE TABLE puesto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email_empresa VARCHAR(100),
    descripcion TEXT NOT NULL,
    salario_ofrecido DECIMAL(10,2),
    tipo_publicacion VARCHAR(10), -- 'PUBLICA' o 'PRIVADA'
    activo TINYINT(1) DEFAULT 1, -- Para "Desactivar un puesto"
    fecha_publicacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_puesto_empresa FOREIGN KEY (email_empresa) REFERENCES empresa(email)
);

-- 6. REQUISITOS DEL PUESTO (Característica y Nivel deseado)
-- Esto permite a la empresa indicar qué habilidades busca y en qué nivel
CREATE TABLE puesto_caracteristica (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_puesto INT,
    id_caracteristica INT,
    nivel_deseado INT CHECK (nivel_deseado BETWEEN 1 AND 5),
    FOREIGN KEY (id_puesto) REFERENCES puesto(id),
    FOREIGN KEY (id_caracteristica) REFERENCES caracteristica(id)
);

-- 7. HABILIDADES DEL OFERENTE
CREATE TABLE oferente_caracteristica (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cedula_oferente VARCHAR(20),
    id_caracteristica INT,
    nivel INT CHECK (nivel BETWEEN 1 AND 5),
    FOREIGN KEY (cedula_oferente) REFERENCES oferente(cedula),
    FOREIGN KEY (id_caracteristica) REFERENCES caracteristica(id)
);