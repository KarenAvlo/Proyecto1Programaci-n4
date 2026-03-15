-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: bolsa_empleo
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `caracteristica`
--

DROP TABLE IF EXISTS `caracteristica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caracteristica` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `id_padre` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_padre` (`id_padre`),
  CONSTRAINT `caracteristica_ibfk_1` FOREIGN KEY (`id_padre`) REFERENCES `caracteristica` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caracteristica`
--

LOCK TABLES `caracteristica` WRITE;
/*!40000 ALTER TABLE `caracteristica` DISABLE KEYS */;
/*!40000 ALTER TABLE `caracteristica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empresa`
--

DROP TABLE IF EXISTS `empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empresa` (
  `email` varchar(100) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `localizacion` varchar(200) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `descripcion` tinytext,
  PRIMARY KEY (`email`),
  CONSTRAINT `fk_empresa_usuario` FOREIGN KEY (`email`) REFERENCES `usuario` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empresa`
--

LOCK TABLES `empresa` WRITE;
/*!40000 ALTER TABLE `empresa` DISABLE KEYS */;
INSERT INTO `empresa` VALUES ('contacto@intel.com','Intel Costa Rica','Heredia, Belén','2211-0000','Empresa líder en tecnología y microprocesadores.');
/*!40000 ALTER TABLE `empresa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oferente`
--

DROP TABLE IF EXISTS `oferente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oferente` (
  `cedula` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `nacionalidad` varchar(50) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `residencia` varchar(200) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `curriculo_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cedula`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `fk_oferente_usuario` FOREIGN KEY (`email`) REFERENCES `usuario` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oferente`
--

LOCK TABLES `oferente` WRITE;
/*!40000 ALTER TABLE `oferente` DISABLE KEYS */;
INSERT INTO `oferente` VALUES ('1-1111-1111','Juan','Pérez','Costarricense','8888-8888','San José, Centro','juan@gmail.com','cv_juan.pdf');
/*!40000 ALTER TABLE `oferente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oferente_caracteristica`
--

DROP TABLE IF EXISTS `oferente_caracteristica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oferente_caracteristica` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cedula_oferente` varchar(20) DEFAULT NULL,
  `id_caracteristica` int DEFAULT NULL,
  `nivel` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cedula_oferente` (`cedula_oferente`),
  KEY `id_caracteristica` (`id_caracteristica`),
  CONSTRAINT `oferente_caracteristica_ibfk_1` FOREIGN KEY (`cedula_oferente`) REFERENCES `oferente` (`cedula`),
  CONSTRAINT `oferente_caracteristica_ibfk_2` FOREIGN KEY (`id_caracteristica`) REFERENCES `caracteristica` (`id`),
  CONSTRAINT `oferente_caracteristica_chk_1` CHECK ((`nivel` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oferente_caracteristica`
--

LOCK TABLES `oferente_caracteristica` WRITE;
/*!40000 ALTER TABLE `oferente_caracteristica` DISABLE KEYS */;
/*!40000 ALTER TABLE `oferente_caracteristica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `puesto`
--

DROP TABLE IF EXISTS `puesto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `puesto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email_empresa` varchar(100) DEFAULT NULL,
  `descripcion` tinytext NOT NULL,
  `salario_ofrecido` decimal(10,2) DEFAULT NULL,
  `tipo_publicacion` varchar(10) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT '1',
  `fecha_publicacion` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_puesto_empresa` (`email_empresa`),
  CONSTRAINT `fk_puesto_empresa` FOREIGN KEY (`email_empresa`) REFERENCES `empresa` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puesto`
--

LOCK TABLES `puesto` WRITE;
/*!40000 ALTER TABLE `puesto` DISABLE KEYS */;
/*!40000 ALTER TABLE `puesto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `puesto_caracteristica`
--

DROP TABLE IF EXISTS `puesto_caracteristica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `puesto_caracteristica` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_puesto` int DEFAULT NULL,
  `id_caracteristica` int DEFAULT NULL,
  `nivel_deseado` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_puesto` (`id_puesto`),
  KEY `id_caracteristica` (`id_caracteristica`),
  CONSTRAINT `puesto_caracteristica_ibfk_1` FOREIGN KEY (`id_puesto`) REFERENCES `puesto` (`id`),
  CONSTRAINT `puesto_caracteristica_ibfk_2` FOREIGN KEY (`id_caracteristica`) REFERENCES `caracteristica` (`id`),
  CONSTRAINT `puesto_caracteristica_chk_1` CHECK ((`nivel_deseado` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puesto_caracteristica`
--

LOCK TABLES `puesto_caracteristica` WRITE;
/*!40000 ALTER TABLE `puesto_caracteristica` DISABLE KEYS */;
/*!40000 ALTER TABLE `puesto_caracteristica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `email` varchar(100) NOT NULL,
  `clave` varchar(100) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `estado` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('admin@bolsa.com','$2a$10$T3w0bruOgfwJZAskCY/0w.0ZPM42TcooWLQvLjBz5OqXt/OBFF.ae','ADMIN',1),('contacto@intel.com','123','EMPRESA',1),('juan@gmail.com','123','OFERENTE',1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-15 17:16:21
