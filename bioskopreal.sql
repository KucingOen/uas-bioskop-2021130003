-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for bioskopreal
CREATE DATABASE IF NOT EXISTS `bioskopreal` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bioskopreal`;

-- Dumping structure for table bioskopreal.film
CREATE TABLE IF NOT EXISTS `film` (
  `id` int NOT NULL AUTO_INCREMENT,
  `judul` varchar(255) NOT NULL,
  `genre` varchar(255) NOT NULL,
  `harga` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bioskopreal.film: ~3 rows (approximately)
INSERT INTO `film` (`id`, `judul`, `genre`, `harga`) VALUES
	(1, 'The Marvels', 'Action', 30000.00),
	(2, 'Inception', 'Sci-fi', 35000.00),
	(3, 'Naruto the Movie', 'Anime', 40000.00);

-- Dumping structure for table bioskopreal.kursi
CREATE TABLE IF NOT EXISTS `kursi` (
  `id` int NOT NULL AUTO_INCREMENT,
  `film_id` int DEFAULT NULL,
  `nama_kursi` varchar(5) NOT NULL,
  `tersedia` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `film_id` (`film_id`),
  CONSTRAINT `kursi_ibfk_1` FOREIGN KEY (`film_id`) REFERENCES `film` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bioskopreal.kursi: ~120 rows (approximately)
INSERT INTO `kursi` (`id`, `film_id`, `nama_kursi`, `tersedia`) VALUES
	(1, 1, 'A1', 0),
	(2, 1, 'A2', 0),
	(3, 1, 'A3', 1),
	(4, 1, 'A4', 1),
	(5, 1, 'A5', 1),
	(6, 1, 'A6', 1),
	(7, 1, 'A7', 1),
	(8, 1, 'A8', 1),
	(9, 1, 'B1', 1),
	(10, 1, 'B2', 1),
	(11, 1, 'B3', 1),
	(12, 1, 'B4', 1),
	(13, 1, 'B5', 1),
	(14, 1, 'B6', 1),
	(15, 1, 'B7', 1),
	(16, 1, 'B8', 1),
	(17, 1, 'C1', 1),
	(18, 1, 'C2', 1),
	(19, 1, 'C3', 1),
	(20, 1, 'C4', 1),
	(21, 1, 'C5', 1),
	(22, 1, 'C6', 1),
	(23, 1, 'C7', 1),
	(24, 1, 'C8', 1),
	(25, 1, 'D1', 1),
	(26, 1, 'D2', 1),
	(27, 1, 'D3', 1),
	(28, 1, 'D4', 1),
	(29, 1, 'D5', 1),
	(30, 1, 'D6', 1),
	(31, 1, 'D7', 1),
	(32, 1, 'D8', 1),
	(33, 1, 'E1', 1),
	(34, 1, 'E2', 1),
	(35, 1, 'E3', 1),
	(36, 1, 'E4', 1),
	(37, 1, 'E5', 1),
	(38, 1, 'E6', 1),
	(39, 1, 'E7', 1),
	(40, 1, 'E8', 1),
	(41, 2, 'A1', 1),
	(42, 2, 'A2', 1),
	(43, 2, 'A3', 1),
	(44, 2, 'A4', 1),
	(45, 2, 'A5', 1),
	(46, 2, 'A6', 1),
	(47, 2, 'A7', 1),
	(48, 2, 'A8', 1),
	(49, 2, 'B1', 1),
	(50, 2, 'B2', 1),
	(51, 2, 'B3', 1),
	(52, 2, 'B4', 1),
	(53, 2, 'B5', 1),
	(54, 2, 'B6', 1),
	(55, 2, 'B7', 1),
	(56, 2, 'B8', 1),
	(57, 2, 'C1', 1),
	(58, 2, 'C2', 1),
	(59, 2, 'C3', 1),
	(60, 2, 'C4', 1),
	(61, 2, 'C5', 1),
	(62, 2, 'C6', 1),
	(63, 2, 'C7', 1),
	(64, 2, 'C8', 1),
	(65, 2, 'D1', 1),
	(66, 2, 'D2', 1),
	(67, 2, 'D3', 1),
	(68, 2, 'D4', 1),
	(69, 2, 'D5', 1),
	(70, 2, 'D6', 1),
	(71, 2, 'D7', 1),
	(72, 2, 'D8', 1),
	(73, 2, 'E1', 1),
	(74, 2, 'E2', 1),
	(75, 2, 'E3', 1),
	(76, 2, 'E4', 1),
	(77, 2, 'E5', 1),
	(78, 2, 'E6', 1),
	(79, 2, 'E7', 1),
	(80, 2, 'E8', 1),
	(81, 3, 'A1', 0),
	(82, 3, 'A2', 1),
	(83, 3, 'A3', 1),
	(84, 3, 'A4', 1),
	(85, 3, 'A5', 1),
	(86, 3, 'A6', 1),
	(87, 3, 'A7', 1),
	(88, 3, 'A8', 1),
	(89, 3, 'B1', 1),
	(90, 3, 'B2', 1),
	(91, 3, 'B3', 1),
	(92, 3, 'B4', 1),
	(93, 3, 'B5', 1),
	(94, 3, 'B6', 1),
	(95, 3, 'B7', 1),
	(96, 3, 'B8', 1),
	(97, 3, 'C1', 1),
	(98, 3, 'C2', 1),
	(99, 3, 'C3', 1),
	(100, 3, 'C4', 1),
	(101, 3, 'C5', 1),
	(102, 3, 'C6', 1),
	(103, 3, 'C7', 1),
	(104, 3, 'C8', 1),
	(105, 3, 'D1', 1),
	(106, 3, 'D2', 1),
	(107, 3, 'D3', 1),
	(108, 3, 'D4', 1),
	(109, 3, 'D5', 1),
	(110, 3, 'D6', 1),
	(111, 3, 'D7', 1),
	(112, 3, 'D8', 1),
	(113, 3, 'E1', 1),
	(114, 3, 'E2', 1),
	(115, 3, 'E3', 1),
	(116, 3, 'E4', 1),
	(117, 3, 'E5', 1),
	(118, 3, 'E6', 1),
	(119, 3, 'E7', 1),
	(120, 3, 'E8', 1);

-- Dumping structure for table bioskopreal.riwayat_pembelian
CREATE TABLE IF NOT EXISTS `riwayat_pembelian` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `film_id` int DEFAULT NULL,
  `kursi` varchar(255) DEFAULT NULL,
  `harga` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `film_id` (`film_id`),
  CONSTRAINT `riwayat_pembelian_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `riwayat_pembelian_ibfk_2` FOREIGN KEY (`film_id`) REFERENCES `film` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bioskopreal.riwayat_pembelian: ~3 rows (approximately)
INSERT INTO `riwayat_pembelian` (`id`, `user_id`, `film_id`, `kursi`, `harga`) VALUES
	(5, 1, 1, 'A1', 30000),
	(6, 1, 1, 'A2', 30000),
	(7, 3, 3, 'A1', 55000);

-- Dumping structure for table bioskopreal.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bioskopreal.users: ~3 rows (approximately)
INSERT INTO `users` (`id`, `username`, `password`, `role`) VALUES
	(1, 'user', 'user', 'pembeli'),
	(2, 'admin', 'admin', 'admin'),
	(3, 'anton', 'anton', 'pembeli');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
