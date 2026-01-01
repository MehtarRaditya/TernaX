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
-- Table structure for table `detail_pembelian`
--

DROP TABLE IF EXISTS `detail_pembelian`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detail_pembelian` (
  `id_detail` int NOT NULL AUTO_INCREMENT,
  `id_pembelian` int NOT NULL,
  `id_konsumsi` int NOT NULL,
  `kuantitas` int NOT NULL,
  PRIMARY KEY (`id_detail`),
  KEY `fk_detail_pembelian` (`id_pembelian`),
  KEY `fk_detail_konsumsi` (`id_konsumsi`),
  CONSTRAINT `fk_detail_konsumsi` FOREIGN KEY (`id_konsumsi`) REFERENCES `konsumsi` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_detail_pembelian` FOREIGN KEY (`id_pembelian`) REFERENCES `pembelian` (`id_pembelian`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail_pembelian`
--

LOCK TABLES `detail_pembelian` WRITE;
/*!40000 ALTER TABLE `detail_pembelian` DISABLE KEYS */;
INSERT INTO `detail_pembelian` VALUES (1,1,9,12),(2,1,1,10),(3,2,3,22),(4,3,4,15),(5,3,7,13),(6,4,1,12),(7,5,3,12),(8,6,3,1),(9,7,3,12),(10,8,3,2),(11,9,3,12),(12,10,5,1),(13,11,4,4),(14,11,7,5),(15,12,4,5),(16,13,3,2);
/*!40000 ALTER TABLE `detail_pembelian` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detail_penjualan`
--

DROP TABLE IF EXISTS `detail_penjualan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detail_penjualan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_penjualan` int NOT NULL,
  `id_katalog` int NOT NULL,
  `harga_deal` decimal(10,2) NOT NULL,
  `kuantitas` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_penjualan` (`id_penjualan`),
  KEY `id_katalog` (`id_katalog`),
  CONSTRAINT `detail_penjualan_ibfk_1` FOREIGN KEY (`id_penjualan`) REFERENCES `penjualan` (`id`),
  CONSTRAINT `detail_penjualan_ibfk_2` FOREIGN KEY (`id_katalog`) REFERENCES `detail_produk` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail_penjualan`
--

LOCK TABLES `detail_penjualan` WRITE;
/*!40000 ALTER TABLE `detail_penjualan` DISABLE KEYS */;
INSERT INTO `detail_penjualan` VALUES (1,1,1,120000.00,5.00,600000.00),(2,1,2,15000.00,5.00,75000.00),(3,1,3,35000.00,2.00,70000.00),(4,1,4,1500.00,2.00,3000.00),(5,2,3,35000.00,4.00,140000.00),(6,3,4,1500.00,5.00,7500.00),(7,4,1,120000.00,10.00,1200000.00),(8,5,2,15000.00,4.00,60000.00),(9,5,1,120000.00,20.00,2400000.00),(10,6,2,15000.00,5.00,75000.00),(11,6,1,120000.00,8.00,960000.00);
/*!40000 ALTER TABLE `detail_penjualan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detail_produk`
--

DROP TABLE IF EXISTS `detail_produk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detail_produk` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_produk` int NOT NULL,
  `id_hewan` int NOT NULL,
  `tanggal_diperoleh` date NOT NULL,
  `kuantitas` decimal(5,2) NOT NULL,
  `status_kelayakan` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `pemeriksa` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_produk` (`id_produk`),
  KEY `id_hewan` (`id_hewan`),
  KEY `pemeriksa` (`pemeriksa`),
  CONSTRAINT `detail_produk_ibfk_1` FOREIGN KEY (`id_produk`) REFERENCES `produk` (`id`),
  CONSTRAINT `detail_produk_ibfk_2` FOREIGN KEY (`id_hewan`) REFERENCES `hewan` (`id`),
  CONSTRAINT `detail_produk_ibfk_3` FOREIGN KEY (`pemeriksa`) REFERENCES `karyawan` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail_produk`
--

LOCK TABLES `detail_produk` WRITE;
/*!40000 ALTER TABLE `detail_produk` DISABLE KEYS */;
INSERT INTO `detail_produk` VALUES (1,2,7,'2025-12-16',20.00,'Layak',123),(2,2,10,'2025-12-17',20.00,'Pending',123),(3,3,11,'2025-12-16',20.00,'Layak',123),(4,4,12,'2025-12-09',20.00,'Tidak Layak',123),(5,1,7,'2025-12-16',40.00,'Tidak Layak',123),(6,2,10,'2025-12-24',5.00,'Pending',123),(7,2,10,'2025-12-24',5.00,'Pending',123),(8,2,13,'2026-01-10',5.00,'Pending',123),(9,2,13,'2026-01-10',5.00,'Pending',123),(10,4,12,'2025-12-04',7.00,'Layak',123),(11,4,12,'2025-12-04',7.00,'Tidak Layak',123),(12,3,14,'2025-12-16',4.00,'Layak',123),(13,3,14,'2025-12-16',4.00,'Tidak Layak',123),(14,1,15,'2025-12-16',50.00,'Pending',123),(15,1,15,'2025-12-16',50.00,'Pending',123),(16,1,6,'2025-12-16',20.00,'Pending',123),(17,1,6,'2025-12-16',20.00,'Pending',123),(18,2,10,'2025-12-16',10.00,'Pending',123),(19,2,10,'2025-12-16',10.00,'Layak',123),(20,1,17,'2025-12-28',50.00,'Pending',123),(21,1,17,'2025-12-28',50.00,'Pending',123),(22,1,13,'2025-12-28',5.00,'Pending',123),(23,1,13,'2025-12-28',5.00,'Pending',123),(24,1,10,'2025-12-28',2.00,'Pending',123),(25,1,10,'2025-12-28',2.00,'Pending',123),(26,3,12,'2025-12-28',3.00,'Pending',123),(27,3,12,'2025-12-28',3.00,'Pending',123),(28,1,16,'2025-12-28',4.00,'Pending',123),(29,1,16,'2025-12-28',4.00,'Pending',123);
/*!40000 ALTER TABLE `detail_produk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hewan`
--

DROP TABLE IF EXISTS `hewan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hewan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `jenis` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  `kondisi` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `berat` decimal(7,2) NOT NULL,
  `usia_bulan` smallint NOT NULL,
  `kelamin` varchar(15) COLLATE utf8mb4_general_ci NOT NULL,
  `pemilik` int NOT NULL,
  `penyakit` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_hewan_karyawan_idx` (`pemilik`),
  CONSTRAINT `fk_hewan_karyawan` FOREIGN KEY (`pemilik`) REFERENCES `karyawan` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hewan`
--

LOCK TABLES `hewan` WRITE;
/*!40000 ALTER TABLE `hewan` DISABLE KEYS */;
INSERT INTO `hewan` VALUES (6,'Sapi','Mati',200.00,10,'Jantan',123,'Sehat'),(7,'Sapi','Mati',500.00,6,'Betina',123,'Sehat'),(10,'Sapi','Mati',16.00,20,'Betina',123,'Sehat'),(11,'Ayam','Mati',20.00,5,'Betina',123,'Sehat'),(12,'Ayam','Mati',15.00,6,'Betina',123,'Sehat'),(13,'Sapi','Mati',100.00,5,'Betina',123,'Sehat'),(14,'Ayam','Mati',3.00,3,'Jantan',123,'Sakit'),(15,'Sapi','Mati',200.00,5,'Jantan',123,'Sehat'),(16,'Sapi','Mati',100.00,2,'Jantan',123,'Sehat'),(17,'Sapi','Mati',130.00,10,'Jantan',123,'Sehat'),(18,'Sapi','Alive',150.00,5,'Jantan',123,'Sehat'),(19,'Ayam','Alive',3.40,2,'Betina',123,'Sehat'),(20,'Sapi','Alive',80.00,3,'Betina',123,'Sehat'),(21,'Ayam','Alive',5.00,6,'Jantan',123,'Sehat'),(22,'Sapi','Alive',70.00,3,'Betina',123,'Sehat');
/*!40000 ALTER TABLE `hewan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `karyawan`
--

DROP TABLE IF EXISTS `karyawan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `karyawan` (
  `id` int NOT NULL,
  `nama` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `akun` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(32) COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(15) COLLATE utf8mb4_general_ci NOT NULL,
  `gaji` int NOT NULL,
  `tanggal_rekrut` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `karyawan`
--

LOCK TABLES `karyawan` WRITE;
/*!40000 ALTER TABLE `karyawan` DISABLE KEYS */;
INSERT INTO `karyawan` VALUES (123,'Kink radit','kinkradit123','12345','Peternak',12000000,'2025-11-12'),(213,'raf','raf','raf123','Logistik',1000000,'2025-12-12');
/*!40000 ALTER TABLE `karyawan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `konsumsi`
--

DROP TABLE IF EXISTS `konsumsi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `konsumsi` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nama_konsumsi` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `tipe` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `stok` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `konsumsi`
--

LOCK TABLES `konsumsi` WRITE;
/*!40000 ALTER TABLE `konsumsi` DISABLE KEYS */;
INSERT INTO `konsumsi` VALUES (1,'Jagung','Pakan Ayam',12),(2,'Cacing','Pakan Ayam',0),(3,'Rumput Gajah','Pakan Sapi',45),(4,'Dedak','Pakan Sapi',24),(5,'Vitamin Larut Lemak','Vitamin',1),(6,'Vitamin B Kompleks','Vitamin',0),(7,'Vitamin C','Vitamin',18),(8,'Antibiotik','Obat',0),(9,'Antiparasit','Obat',12);
/*!40000 ALTER TABLE `konsumsi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pemakaian_konsumsi`
--

DROP TABLE IF EXISTS `pemakaian_konsumsi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pemakaian_konsumsi` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_hewan` int NOT NULL,
  `id_konsumsi` int NOT NULL,
  `id_karyawan` int NOT NULL,
  `kuantitas` int NOT NULL,
  `tanggal_waktu` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_pakai_hewan` (`id_hewan`),
  KEY `fk_pakai_konsumsi` (`id_konsumsi`),
  KEY `fk_pakai_karyawan` (`id_karyawan`),
  CONSTRAINT `fk_pakai_hewan` FOREIGN KEY (`id_hewan`) REFERENCES `hewan` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_pakai_karyawan` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_pakai_konsumsi` FOREIGN KEY (`id_konsumsi`) REFERENCES `konsumsi` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pemakaian_konsumsi`
--

LOCK TABLES `pemakaian_konsumsi` WRITE;
/*!40000 ALTER TABLE `pemakaian_konsumsi` DISABLE KEYS */;
INSERT INTO `pemakaian_konsumsi` VALUES (2,10,1,123,3,'2025-12-28 04:28:04'),(3,6,1,123,2,'2025-12-28 04:34:57'),(4,6,3,123,12,'2025-12-28 04:48:40'),(5,18,3,123,1,'2025-12-29 12:01:37'),(6,18,3,123,5,'2025-12-29 12:01:46');
/*!40000 ALTER TABLE `pemakaian_konsumsi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pembelian`
--

DROP TABLE IF EXISTS `pembelian`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pembelian` (
  `id_pembelian` int NOT NULL AUTO_INCREMENT,
  `tanggal_pembelian` date NOT NULL,
  `id_karyawan` int NOT NULL,
  PRIMARY KEY (`id_pembelian`),
  KEY `id_karyawan` (`id_karyawan`),
  CONSTRAINT `pembelian_ibfk_1` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pembelian`
--

LOCK TABLES `pembelian` WRITE;
/*!40000 ALTER TABLE `pembelian` DISABLE KEYS */;
INSERT INTO `pembelian` VALUES (1,'2025-12-27',123),(2,'2025-12-03',123),(3,'2025-12-04',123),(4,'2025-12-03',123),(5,'2025-12-03',123),(6,'2025-12-11',123),(7,'2025-12-04',123),(8,'2025-12-04',123),(9,'2025-12-04',123),(10,'2025-12-04',123),(11,'2025-12-04',123),(12,'2025-12-02',123),(13,'2025-12-03',123);
/*!40000 ALTER TABLE `pembelian` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `penjualan`
--

DROP TABLE IF EXISTS `penjualan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `penjualan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tanggal` datetime DEFAULT CURRENT_TIMESTAMP,
  `total_harga` decimal(10,2) NOT NULL,
  `uang_dibayar` decimal(10,2) NOT NULL,
  `kembalian` decimal(10,2) NOT NULL,
  `id_karyawan` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `penjualan`
--

LOCK TABLES `penjualan` WRITE;
/*!40000 ALTER TABLE `penjualan` DISABLE KEYS */;
INSERT INTO `penjualan` VALUES (1,'2025-12-16 06:52:47',748000.00,800000.00,52000.00,123),(2,'2025-12-16 06:56:05',140000.00,150000.00,10000.00,123),(3,'2025-12-16 09:50:57',7500.00,10000.00,2500.00,123),(4,'2025-12-16 10:05:13',1200000.00,1300000.00,100000.00,123),(5,'2025-12-16 11:07:28',2460000.00,2500000.00,40000.00,123),(6,'2025-12-28 05:39:27',1035000.00,1040000.00,5000.00,123);
/*!40000 ALTER TABLE `penjualan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produk`
--

DROP TABLE IF EXISTS `produk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produk` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nama_produk` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `satuan` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  `harga_satuan` decimal(10,2) NOT NULL,
  `total_stok` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produk`
--

LOCK TABLES `produk` WRITE;
/*!40000 ALTER TABLE `produk` DISABLE KEYS */;
INSERT INTO `produk` VALUES (1,'Daging Sapi Premium','Kg',120000.00,88.00),(2,'Susu Sapi Murni','Liter',15000.00,6.00),(3,'Daging Ayam Potong','Kg',35000.00,1.00),(4,'Telur Ayam Negeri','Butir',1500.00,0.00);
/*!40000 ALTER TABLE `produk` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-01 23:48:46
