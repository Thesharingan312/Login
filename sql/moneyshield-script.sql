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
-- Temporary view structure for view `budget_vs_actual`
--

DROP TABLE IF EXISTS `budget_vs_actual`;
/*!50001 DROP VIEW IF EXISTS `budget_vs_actual`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `budget_vs_actual` AS SELECT 
 1 AS `first_name`,
 1 AS `last_name`,
 1 AS `category`,
 1 AS `year`,
 1 AS `month`,
 1 AS `budget_amount`,
 1 AS `actual_amount`,
 1 AS `difference`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `budgets`
--

DROP TABLE IF EXISTS `budgets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `budgets` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `category_id` int NOT NULL,
  `year` int NOT NULL,
  `month` int NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `notes` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `budgets_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `budgets_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `budgets`
--

LOCK TABLES `budgets` WRITE;
/*!40000 ALTER TABLE `budgets` DISABLE KEYS */;
INSERT INTO `budgets` VALUES (1,4,1,2025,1,1000.00,'January monthly budget for Emmanuel','2025-05-13 05:30:26'),(2,5,1,2025,1,1200.00,'January monthly budget for Franyi','2025-05-13 05:30:26'),(3,6,1,2025,1,1500.00,'January monthly budget for Ydalina','2025-05-13 05:30:26'),(4,4,1,2025,0,12000.00,'Annual budget for Emmanuel','2025-05-13 05:30:26'),(5,5,1,2025,0,14000.00,'Annual budget for Franyi','2025-05-13 05:30:26'),(6,6,1,2025,0,16000.00,'Annual budget for Ydalina','2025-05-13 05:30:26'),(7,4,2,2025,1,300.00,'January food budget for Emmanuel','2025-05-13 05:30:26'),(8,5,3,2025,1,500.00,'January housing budget for Franyi','2025-05-13 05:30:26'),(9,6,4,2025,1,200.00,'January health budget for Ydalina','2025-05-13 05:30:26');
/*!40000 ALTER TABLE `budgets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (15,'Childcare'),(11,'Education'),(14,'Entertainment'),(2,'Food'),(1,'General'),(8,'Groceries'),(4,'Health'),(3,'Housing'),(13,'Insurance'),(10,'Investments'),(6,'Leisure'),(16,'Others'),(9,'Savings'),(7,'Technology'),(5,'Transport'),(12,'Utilities');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profiles`
--

DROP TABLE IF EXISTS `profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profiles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES (1,'admin'),(2,'user');
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `saving_types`
--

DROP TABLE IF EXISTS `saving_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `saving_types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `saving_types`
--

LOCK TABLES `saving_types` WRITE;
/*!40000 ALTER TABLE `saving_types` DISABLE KEYS */;
INSERT INTO `saving_types` VALUES (2,'extra'),(1,'fixed');
/*!40000 ALTER TABLE `saving_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `savings`
--

DROP TABLE IF EXISTS `savings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `savings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `type_id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `notes` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `savings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `savings_ibfk_3` FOREIGN KEY (`type_id`) REFERENCES `saving_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `savings`
--

LOCK TABLES `savings` WRITE;
/*!40000 ALTER TABLE `savings` DISABLE KEYS */;
INSERT INTO `savings` VALUES (1,4,1,'Emergency Fund',100.00,'Monthly saving for emergencies','2025-05-13 05:30:26'),(2,4,2,'Vacation Fund',50.00,'Extra saving for summer trip','2025-05-13 05:30:26'),(3,5,1,'Tech Upgrade',200.00,'Monthly saving for new laptop','2025-05-13 05:30:26'),(4,5,2,'Birthday Gift',30.00,'Gift for friend','2025-05-13 05:30:26'),(5,6,1,'Medical Fund',300.00,'Reserved for health emergencies','2025-05-13 05:30:26'),(6,6,2,'Business Expansion',100.00,'Saved for marketing','2025-05-13 05:30:26');
/*!40000 ALTER TABLE `savings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `savings_summary`
--

DROP TABLE IF EXISTS `savings_summary`;
/*!50001 DROP VIEW IF EXISTS `savings_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `savings_summary` AS SELECT 
 1 AS `first_name`,
 1 AS `last_name`,
 1 AS `saving_type`,
 1 AS `saving_name`,
 1 AS `amount`,
 1 AS `notes`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `transaction_types`
--

DROP TABLE IF EXISTS `transaction_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_types`
--

LOCK TABLES `transaction_types` WRITE;
/*!40000 ALTER TABLE `transaction_types` DISABLE KEYS */;
INSERT INTO `transaction_types` VALUES (2,'expense'),(1,'income');
/*!40000 ALTER TABLE `transaction_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `type_id` int NOT NULL,
  `category_id` int DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL,
  `description` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `type_id` (`type_id`),
  KEY `fk_transaction_category` (`category_id`),
  CONSTRAINT `fk_transaction_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`type_id`) REFERENCES `transaction_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,1,2,NULL,50.00,'Dinner at restaurant','2025-05-13 05:30:26'),(2,1,1,NULL,1200.00,'Monthly paycheck','2025-05-13 05:30:26'),(3,1,2,NULL,30.00,'Taxi to work','2025-05-13 05:30:26'),(4,2,1,NULL,1500.00,'Monthly payment','2025-05-13 05:30:26'),(5,2,2,NULL,100.00,'Supermarket shopping','2025-05-13 05:30:26'),(6,2,2,NULL,20.00,'Cinema','2025-05-13 05:30:26'),(7,3,1,NULL,2000.00,'Web project','2025-05-13 05:30:26'),(8,3,2,NULL,150.00,'New headphones','2025-05-13 05:30:26'),(9,4,1,NULL,1500.00,'January salary','2025-05-13 05:30:26'),(10,4,2,NULL,200.00,'January rent','2025-05-13 05:30:26'),(11,5,1,NULL,1600.00,'Web project payment','2025-05-13 05:30:26'),(12,5,2,NULL,100.00,'Electricity bill','2025-05-13 05:30:26'),(13,6,1,NULL,1700.00,'January profits','2025-05-13 05:30:26'),(14,6,2,NULL,300.00,'Dental appointment','2025-05-13 05:30:26');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `profile_id` int DEFAULT NULL,
  `base_budget` decimal(10,2) DEFAULT '0.00',
  `base_saving` decimal(10,2) DEFAULT '0.00',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `profile_id` (`profile_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Juan','Pérez','juan@perez.com','2830049',2,500.00,100.00,'2025-05-13 05:30:26'),(2,'Ana','López','ana@lopez.com','9221040',2,600.00,150.00,'2025-05-13 05:30:26'),(3,'Carlos','García','carlos@garcia.com','4781441',1,700.00,200.00,'2025-05-13 05:30:26'),(4,'Emmanuel','Pallares','emmanuel@moneyshield.com','1234567',1,500.00,100.00,'2025-05-13 05:30:26'),(5,'Franyi','López','franyi@moneyshield.com','2345678',2,600.00,150.00,'2025-05-13 05:30:26'),(6,'Ydalina','Tejada','ydalina@moneyshield.com','3456789',2,700.00,200.00,'2025-05-13 05:30:26'),(7,'Rosa','Meltrozo','roza.meltrozo@gmail.com','7062217',2,800.00,150.00,'2025-05-13 07:47:39');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'moneyshield'
--

--
-- Dumping routines for database 'moneyshield'
--
/*!50003 DROP PROCEDURE IF EXISTS `categorize_transactions` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `categorize_transactions`()
BEGIN
    -- Update transactions without category to use Others category (ID 16)
    UPDATE transactions SET category_id = 16 WHERE category_id IS NULL;
    
    -- You could add more logic here to automatically categorize based on description keywords
    -- For example:
    UPDATE transactions SET category_id = 2 WHERE category_id IS NULL 
        AND (description LIKE '%restaurant%' OR description LIKE '%food%' OR description LIKE '%meal%');
    
    UPDATE transactions SET category_id = 3 WHERE category_id IS NULL 
        AND (description LIKE '%rent%' OR description LIKE '%house%' OR description LIKE '%apartment%');
    
    UPDATE transactions SET category_id = 4 WHERE category_id IS NULL 
        AND (description LIKE '%doctor%' OR description LIKE '%medicine%' OR description LIKE '%dental%' OR description LIKE '%health%');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `budget_vs_actual`
--

/*!50001 DROP VIEW IF EXISTS `budget_vs_actual`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `budget_vs_actual` AS select `u`.`first_name` AS `first_name`,`u`.`last_name` AS `last_name`,`c`.`name` AS `category`,`b`.`year` AS `year`,`b`.`month` AS `month`,`b`.`total_amount` AS `budget_amount`,coalesce(sum(`t`.`amount`),0) AS `actual_amount`,(`b`.`total_amount` - coalesce(sum(`t`.`amount`),0)) AS `difference` from (((`budgets` `b` join `users` `u` on((`b`.`user_id` = `u`.`id`))) join `categories` `c` on((`b`.`category_id` = `c`.`id`))) left join `transactions` `t` on(((`t`.`user_id` = `u`.`id`) and (`t`.`category_id` = `c`.`id`) and (`t`.`type_id` = 2)))) group by `u`.`first_name`,`u`.`last_name`,`c`.`name`,`b`.`year`,`b`.`month`,`b`.`total_amount` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `savings_summary`
--

/*!50001 DROP VIEW IF EXISTS `savings_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `savings_summary` AS select `u`.`first_name` AS `first_name`,`u`.`last_name` AS `last_name`,`st`.`name` AS `saving_type`,`s`.`name` AS `saving_name`,`s`.`amount` AS `amount`,`s`.`notes` AS `notes` from ((`savings` `s` join `users` `u` on((`s`.`user_id` = `u`.`id`))) join `saving_types` `st` on((`s`.`type_id` = `st`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-03 12:01:36
