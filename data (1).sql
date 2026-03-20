-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bolsa_empleo
-- ------------------------------------------------------
-- Server version	9.1.0

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
  KEY `FKbdrwmh3uny8wvw16s3hbfn77j` (`id_padre`),
  CONSTRAINT `FKbdrwmh3uny8wvw16s3hbfn77j` FOREIGN KEY (`id_padre`) REFERENCES `caracteristica` (`id`)
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
  `descripcion` tinytext,
  `localizacion` varchar(200) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`email`),
  CONSTRAINT `FK71acyfjo59o7kv40dhbgycek5` FOREIGN KEY (`email`) REFERENCES `usuario` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empresa`
--

LOCK TABLES `empresa` WRITE;
/*!40000 ALTER TABLE `empresa` DISABLE KEYS */;
INSERT INTO `empresa` VALUES ('Empresa líder en tecnología','San José, CR','Tech Solutions','7777-7777','empresa@bolsa.com'),(NULL,NULL,'Patitos.SA',NULL,'patitos@bolsa.com');
/*!40000 ALTER TABLE `empresa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oferente`
--

DROP TABLE IF EXISTS `oferente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oferente` (
  `apellido` varchar(50) NOT NULL,
  `cedula` varchar(20) NOT NULL,
  `curriculo_path` varchar(255) DEFAULT NULL,
  `nacionalidad` varchar(50) DEFAULT NULL,
  `nombre` varchar(50) NOT NULL,
  `residencia` varchar(200) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`email`),
  CONSTRAINT `FKt10ux63r3upqr00a0erdjy2n1` FOREIGN KEY (`email`) REFERENCES `usuario` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oferente`
--

LOCK TABLES `oferente` WRITE;
/*!40000 ALTER TABLE `oferente` DISABLE KEYS */;
INSERT INTO `oferente` VALUES ('Pérez','1-1111-1111',NULL,NULL,'Juan',NULL,'8888-8888','oferente@bolsa.com');
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
  `nivel` int DEFAULT NULL,
  `cedula_oferente` varchar(100) DEFAULT NULL,
  `id_caracteristica` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3f3ptp7vf9bmc2rcvapxikddg` (`cedula_oferente`),
  KEY `FKt3gyf7wxawxwwmgncaecvtbq1` (`id_caracteristica`),
  CONSTRAINT `FK3f3ptp7vf9bmc2rcvapxikddg` FOREIGN KEY (`cedula_oferente`) REFERENCES `oferente` (`email`),
  CONSTRAINT `FKt3gyf7wxawxwwmgncaecvtbq1` FOREIGN KEY (`id_caracteristica`) REFERENCES `caracteristica` (`id`)
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
  `activo` bit(1) DEFAULT b'1',
  `descripcion` tinytext NOT NULL,
  `fecha_publicacion` datetime(6) DEFAULT NULL,
  `salario_ofrecido` decimal(10,2) DEFAULT NULL,
  `tipo_publicacion` varchar(10) DEFAULT NULL,
  `email_empresa` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8gc6101u3st65blyth8n6divq` (`email_empresa`),
  CONSTRAINT `FK8gc6101u3st65blyth8n6divq` FOREIGN KEY (`email_empresa`) REFERENCES `empresa` (`email`)
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
  `nivel_deseado` int DEFAULT NULL,
  `id_caracteristica` int DEFAULT NULL,
  `id_puesto` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2b8nqkwlt4pxsn5ywhkivbnru` (`id_caracteristica`),
  KEY `FKmk53vgh9vgoeynckqkmygup7b` (`id_puesto`),
  CONSTRAINT `FK2b8nqkwlt4pxsn5ywhkivbnru` FOREIGN KEY (`id_caracteristica`) REFERENCES `caracteristica` (`id`),
  CONSTRAINT `FKmk53vgh9vgoeynckqkmygup7b` FOREIGN KEY (`id_puesto`) REFERENCES `puesto` (`id`)
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
  `estado` bit(1) DEFAULT b'0',
  `tipo` varchar(20) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('admin@bolsa.com','$2a$10$8mOF5zF/a0pNeB41XEz01uQeFKieLwiz0gRGlFZgznXj7eJEONT5W',_binary '','ADMIN'),('empresa@bolsa.com','$2a$10$lV7kdGP82fmCsvS05ziJpOXP8PbsADPYIyUVo3Zoajs2Lig8KQWaK',_binary '','EMPRESA'),('oferente@bolsa.com','$2a$10$3MhBW/X3wyBoL3zhHsNCa.ADyYpT/1AeC2PnvUh6h2pq6X/TL3Ll.',_binary '','OFERENTE'),('patitos@bolsa.com','$2a$10$TVda7SkTl00BtwVWgcLdAelYhYJbcuGUFVv6AlOVPVKW.ZR5rF5EC',_binary '','EMPRESA');
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

-- Dump completed on 2026-03-19 19:19:36
