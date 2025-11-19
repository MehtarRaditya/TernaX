-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: ternax
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
-- Table structure for table `ayam`
--

DROP TABLE IF EXISTS `ayam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ayam` (
  `id` int NOT NULL,
  `butir_telur_harian` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ayam`
--

LOCK TABLES `ayam` WRITE;
/*!40000 ALTER TABLE `ayam` DISABLE KEYS */;
/*!40000 ALTER TABLE `ayam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bangun_kandang`
--

DROP TABLE IF EXISTS `bangun_kandang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bangun_kandang` (
  `id` int NOT NULL,
  `kapasitas_awal` tinyint DEFAULT NULL,
  `luas_awal` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bangun_kandang`
--

LOCK TABLES `bangun_kandang` WRITE;
/*!40000 ALTER TABLE `bangun_kandang` DISABLE KEYS */;
/*!40000 ALTER TABLE `bangun_kandang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `beli_hewan`
--

DROP TABLE IF EXISTS `beli_hewan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beli_hewan` (
  `id` int NOT NULL,
  `jenis` varchar(10) NOT NULL,
  `usia_bulan_dibeli` smallint NOT NULL,
  `berat_dibeli` decimal(7,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beli_hewan`
--

LOCK TABLES `beli_hewan` WRITE;
/*!40000 ALTER TABLE `beli_hewan` DISABLE KEYS */;
/*!40000 ALTER TABLE `beli_hewan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `beli_konsumsi`
--

DROP TABLE IF EXISTS `beli_konsumsi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beli_konsumsi` (
  `id` int NOT NULL,
  `jenis` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beli_konsumsi`
--

LOCK TABLES `beli_konsumsi` WRITE;
/*!40000 ALTER TABLE `beli_konsumsi` DISABLE KEYS */;
/*!40000 ALTER TABLE `beli_konsumsi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `budget`
--

DROP TABLE IF EXISTS `budget`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `budget` (
  `id` int NOT NULL,
  `nama` varchar(100) NOT NULL,
  `deskripsi` text,
  `jenis` varchar(45) NOT NULL,
  `uang` varchar(45) NOT NULL,
  `pelaku` int NOT NULL,
  `waktu` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `budget`
--

LOCK TABLES `budget` WRITE;
/*!40000 ALTER TABLE `budget` DISABLE KEYS */;
/*!40000 ALTER TABLE `budget` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `daging`
--

DROP TABLE IF EXISTS `daging`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `daging` (
  `id` int NOT NULL,
  `jenis` varchar(45) NOT NULL,
  `berat_stok` float NOT NULL,
  `berat_terjual` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daging`
--

LOCK TABLES `daging` WRITE;
/*!40000 ALTER TABLE `daging` DISABLE KEYS */;
/*!40000 ALTER TABLE `daging` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gaji`
--

DROP TABLE IF EXISTS `gaji`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gaji` (
  `id` varchar(45) NOT NULL,
  `karyawan` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gaji`
--

LOCK TABLES `gaji` WRITE;
/*!40000 ALTER TABLE `gaji` DISABLE KEYS */;
/*!40000 ALTER TABLE `gaji` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hewan`
--

DROP TABLE IF EXISTS `hewan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hewan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `jenis` varchar(10) NOT NULL,
  `berat` decimal(7,2) NOT NULL,
  `usia_bulan` smallint NOT NULL,
  `kelamin` varchar(15) NOT NULL,
  `pemilik` varchar(100) NOT NULL,
  `kondisi` varchar(30) NOT NULL,
  `penyakit` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hewan`
--

LOCK TABLES `hewan` WRITE;
/*!40000 ALTER TABLE `hewan` DISABLE KEYS */;
/*!40000 ALTER TABLE `hewan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jual_hewan`
--

DROP TABLE IF EXISTS `jual_hewan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jual_hewan` (
  `id` int NOT NULL,
  `berat_dijual` decimal(7,2) NOT NULL,
  `usia_bulan_dijual` smallint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jual_hewan`
--

LOCK TABLES `jual_hewan` WRITE;
/*!40000 ALTER TABLE `jual_hewan` DISABLE KEYS */;
/*!40000 ALTER TABLE `jual_hewan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jual_produk`
--

DROP TABLE IF EXISTS `jual_produk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jual_produk` (
  `id` int NOT NULL,
  `jumlah` float NOT NULL,
  `produk` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jual_produk`
--

LOCK TABLES `jual_produk` WRITE;
/*!40000 ALTER TABLE `jual_produk` DISABLE KEYS */;
/*!40000 ALTER TABLE `jual_produk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kandang`
--

DROP TABLE IF EXISTS `kandang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kandang` (
  `id` int NOT NULL,
  `jenis` varchar(20) NOT NULL,
  `luas` int NOT NULL,
  `kapasitas` smallint NOT NULL,
  `terpakai` smallint NOT NULL,
  `sisa` smallint NOT NULL,
  `tanggal_dibangun` date NOT NULL,
  `tanggal_terakhir_dibersihkan` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kandang`
--

LOCK TABLES `kandang` WRITE;
/*!40000 ALTER TABLE `kandang` DISABLE KEYS */;
/*!40000 ALTER TABLE `kandang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `karyawan`
--

DROP TABLE IF EXISTS `karyawan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `karyawan` (
  `id` int NOT NULL,
  `nama` varchar(100) NOT NULL,
  `akun` varchar(50) NOT NULL,
  `password` varchar(32) NOT NULL,
  `role` varchar(15) NOT NULL,
  `gaji` int NOT NULL,
  `tanggal_rekrut` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `karyawan`
--

LOCK TABLES `karyawan` WRITE;
/*!40000 ALTER TABLE `karyawan` DISABLE KEYS */;
/*!40000 ALTER TABLE `karyawan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `konsumsi`
--

DROP TABLE IF EXISTS `konsumsi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `konsumsi` (
  `id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `konsumsi`
--

LOCK TABLES `konsumsi` WRITE;
/*!40000 ALTER TABLE `konsumsi` DISABLE KEYS */;
/*!40000 ALTER TABLE `konsumsi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produk`
--

DROP TABLE IF EXISTS `produk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produk` (
  `id` int NOT NULL AUTO_INCREMENT,
  `jenis` varchar(20) NOT NULL,
  `kualitas` varchar(20) NOT NULL,
  `tanggal_diperoleh` date NOT NULL,
  `peternak` int NOT NULL,
  `pemeriksa` int DEFAULT NULL,
  `status_kelayakan` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produk`
--

LOCK TABLES `produk` WRITE;
/*!40000 ALTER TABLE `produk` DISABLE KEYS */;
/*!40000 ALTER TABLE `produk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sapi`
--

DROP TABLE IF EXISTS `sapi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sapi` (
  `id` int NOT NULL,
  `liter_susu_harian` smallint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sapi`
--

LOCK TABLES `sapi` WRITE;
/*!40000 ALTER TABLE `sapi` DISABLE KEYS */;
/*!40000 ALTER TABLE `sapi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `susu`
--

DROP TABLE IF EXISTS `susu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `susu` (
  `id` int NOT NULL,
  `liter_stok` smallint NOT NULL,
  `liter_terjual` smallint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `susu`
--

LOCK TABLES `susu` WRITE;
/*!40000 ALTER TABLE `susu` DISABLE KEYS */;
/*!40000 ALTER TABLE `susu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `telur`
--

DROP TABLE IF EXISTS `telur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `telur` (
  `id` int NOT NULL,
  `butir_stok` float NOT NULL,
  `butir_terjual` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `telur`
--

LOCK TABLES `telur` WRITE;
/*!40000 ALTER TABLE `telur` DISABLE KEYS */;
/*!40000 ALTER TABLE `telur` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tingkat_kandang`
--

DROP TABLE IF EXISTS `tingkat_kandang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tingkat_kandang` (
  `id` int NOT NULL,
  `kapasitas_sebelum` int NOT NULL,
  `kapasitas_sesudah` int NOT NULL,
  `luas_sebelum` int NOT NULL,
  `luas_sesudah` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tingkat_kandang`
--

LOCK TABLES `tingkat_kandang` WRITE;
/*!40000 ALTER TABLE `tingkat_kandang` DISABLE KEYS */;
/*!40000 ALTER TABLE `tingkat_kandang` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-18 10:24:40
