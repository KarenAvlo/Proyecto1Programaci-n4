USE bolsa_empleo;

-- 1. LIMPIEZA INICIAL (Opcional, para no duplicar datos si corres el script varias veces)
DELETE FROM usuario;
DELETE FROM caracteristica;

-- 2. USUARIOS INICIALES (Clave '123' para todos por ahora)
-- Un administrador (acceso total)
INSERT INTO usuario (email, clave, tipo, estado) VALUES ('admin@bolsa.com', '123', 'ADMIN', 1);

-- Una empresa ya aprobada
INSERT INTO usuario (email, clave, tipo, estado) VALUES ('contacto@intel.com', '123', 'EMPRESA', 1);
INSERT INTO empresa (email, nombre, localizacion, telefono, descripcion) 
VALUES ('contacto@intel.com', 'Intel Costa Rica', 'Heredia, Belén', '2211-0000', 'Empresa líder en tecnología y microprocesadores.');

-- Un oferente ya aprobado
INSERT INTO usuario (email, clave, tipo, estado) VALUES ('juan@gmail.com', '123', 'OFERENTE', 1);
INSERT INTO oferente (cedula, nombre, primer_apellido, nacionalidad, telefono, residencia, email, curriculo_path)
VALUES ('1-1111-1111', 'Juan', 'Pérez', 'Costarricense', '8888-8888', 'San José, Centro', 'juan@gmail.com', 'cv_juan.pdf');


-- 3. CARACTERÍSTICAS (Jerarquía según el PDF)
-- Categorías principales (Padres)
INSERT INTO caracteristica (id, nombre, id_padre) VALUES (1, 'Lenguajes de Programación', NULL);
INSERT INTO caracteristica (id, nombre, id_padre) VALUES (2, 'Bases de Datos', NULL);
INSERT INTO caracteristica (id, nombre, id_padre) VALUES (3, 'Tecnologías Web', NULL);

-- Subcategorías (Hijos)
-- Lenguajes
INSERT INTO caracteristica (nombre, id_padre) VALUES ('Java', 1);
INSERT INTO caracteristica (nombre, id_padre) VALUES ('Python', 1);
INSERT INTO caracteristica (nombre, id_padre) VALUES ('C#', 1);

-- Bases de Datos
INSERT INTO caracteristica (nombre, id_padre) VALUES ('MySQL', 2);
INSERT INTO caracteristica (nombre, id_padre) VALUES ('PostgreSQL', 2);

-- Tecnologías Web
INSERT INTO caracteristica (nombre, id_padre) VALUES ('HTML5 & CSS3', 3);
INSERT INTO caracteristica (nombre, id_padre) VALUES ('Spring Boot', 3);


-- 4. UN PUESTO DE PRUEBA (Para ver el Top 5)
INSERT INTO puesto (email_empresa, descripcion, salario_ofrecido, tipo_publicacion, activo)
VALUES ('contacto@intel.com', 'Desarrollador Junior Java', 950000.00, 'PUBLICA', 1);