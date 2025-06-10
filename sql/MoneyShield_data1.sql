CREATE DATABASE  IF NOT EXISTS `moneyshield` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `moneyshield`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: moneyshield
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Dumping data for table `budget_categories`
--

LOCK TABLES `budget_categories` WRITE;
/*!40000 ALTER TABLE `budget_categories` DISABLE KEYS */;
INSERT INTO `budget_categories` VALUES (2,'Food'),(1,'General'),(4,'Health'),(3,'Housing'),(5,'Transport');
/*!40000 ALTER TABLE `budget_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `budgets`
--

LOCK TABLES `budgets` WRITE;
/*!40000 ALTER TABLE `budgets` DISABLE KEYS */;
INSERT INTO `budgets` VALUES (1,4,2025,1,1000.00,'January monthly budget for Emmanuel','2025-05-07 06:48:20'),(2,5,2025,1,1200.00,'January monthly budget for Franyi','2025-05-07 06:48:20'),(3,6,2025,1,1500.00,'January monthly budget for Ydalina','2025-05-07 06:48:20'),(4,4,2025,0,12000.00,'Annual budget for Emmanuel','2025-05-07 06:48:20'),(5,5,2025,0,14000.00,'Annual budget for Franyi','2025-05-07 06:48:20'),(6,6,2025,0,16000.00,'Annual budget for Ydalina','2025-05-07 06:48:20');
/*!40000 ALTER TABLE `budgets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES (1,'admin'),(2,'user'),(3,'admin'),(4,'user');
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `saving_types`
--

LOCK TABLES `saving_types` WRITE;
/*!40000 ALTER TABLE `saving_types` DISABLE KEYS */;
INSERT INTO `saving_types` VALUES (2,'extra'),(1,'fixed');
/*!40000 ALTER TABLE `saving_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `savings`
--

LOCK TABLES `savings` WRITE;
/*!40000 ALTER TABLE `savings` DISABLE KEYS */;
INSERT INTO `savings` VALUES (1,4,1,9,'Emergency Fund','fixed',100.00,'Monthly saving for emergencies','2025-05-07 06:48:20'),(2,4,4,9,'Vacation Fund','extra',50.00,'Extra saving for summer trip','2025-05-07 06:48:20'),(3,5,2,11,'Tech Upgrade','fixed',200.00,'Monthly saving for new laptop','2025-05-07 06:48:20'),(4,5,5,11,'Birthday Gift','extra',30.00,'Gift for friend','2025-05-07 06:48:20'),(5,6,3,13,'Medical Fund','fixed',300.00,'Reserved for health emergencies','2025-05-07 06:48:20'),(6,6,6,13,'Business Expansion','extra',100.00,'Saved for marketing','2025-05-07 06:48:20');
/*!40000 ALTER TABLE `savings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `transaction_types`
--

LOCK TABLES `transaction_types` WRITE;
/*!40000 ALTER TABLE `transaction_types` DISABLE KEYS */;
INSERT INTO `transaction_types` VALUES (2,'expense'),(1,'income');
/*!40000 ALTER TABLE `transaction_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,1,'expense',50.00,'Food','Dinner at restaurant','2025-05-07 06:48:20'),(2,1,'income',1200.00,'Salary','Monthly paycheck','2025-05-07 06:48:20'),(3,1,'expense',30.00,'Transport','Taxi to work','2025-05-07 06:48:20'),(4,2,'income',1500.00,'Salary','Monthly payment','2025-05-07 06:48:20'),(5,2,'expense',100.00,'Groceries','Supermarket shopping','2025-05-07 06:48:20'),(6,2,'expense',20.00,'Leisure','Cinema','2025-05-07 06:48:20'),(7,3,'income',2000.00,'Freelance','Web project','2025-05-07 06:48:20'),(8,3,'expense',150.00,'Technology','New headphones','2025-05-07 06:48:20'),(9,4,'income',1500.00,'Salary','January salary','2025-05-07 06:48:20'),(10,4,'expense',200.00,'Rent','January rent','2025-05-07 06:48:20'),(11,5,'income',1600.00,'Freelance','Web project payment','2025-05-07 06:48:20'),(12,5,'expense',100.00,'Utilities','Electricity bill','2025-05-07 06:48:20'),(13,6,'income',1700.00,'Business','January profits','2025-05-07 06:48:20'),(14,6,'expense',300.00,'Health','Dental appointment','2025-05-07 06:48:20');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Juan','Pérez','juan@perez.com','2830049',2,0.00,0.00),(2,'Ana','López','ana@lopez.com','9221040',2,0.00,0.00),(3,'Carlos','García','carlos@garcia.com','4781441',1,0.00,0.00),(4,'Emmanuel','Pallares','emmanuel@pallares.com','7062217',1,0.00,0.00),(5,'Franyi','López','franyi@lopez.com','2345678',2,0.00,0.00),(6,'Ydalina','Tejada','ydalina@tejada.com','3456789',2,0.00,0.00),(8,'Miguel','Bouza','migue@bouza.com','5264321',1,0.00,0.00),(10,'patricia','nicola','patri@nicola.com','5264321',2,0.00,0.00),(11,'manila','okcla','manila@okcla.com','5689453',2,0.00,0.00),(12,'Emmanuel','Pallares','emmanuel@moneyshield.com','1234567',1,500.00,100.00),(13,'Franyi','Lopez','Franyi@moneyshield.com','2345678',2,600.00,150.00),(14,'Ydalina','Tejada','ydalina@moneyshield.com','3456789',2,700.00,200.00);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'moneyshield'
--

--
-- Dumping routines for database 'moneyshield'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-13  9:12:49
