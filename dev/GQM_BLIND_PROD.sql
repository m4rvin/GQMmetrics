-- MySQL dump 10.13  Distrib 5.6.12, for Win64 (x86_64)
--
-- Host: localhost    Database: GQM_BLIND_PROD
-- ------------------------------------------------------
-- Server version	5.6.12-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `app_user`
--

DROP TABLE IF EXISTS `app_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_expired` bit(1) NOT NULL,
  `account_locked` bit(1) NOT NULL,
  `address` varchar(150) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `postal_code` varchar(15) DEFAULT NULL,
  `province` varchar(100) DEFAULT NULL,
  `credentials_expired` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `account_enabled` bit(1) DEFAULT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `password_hint` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_user`
--

LOCK TABLES `app_user` WRITE;
/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
INSERT INTO `app_user` VALUES (-3,'\0','\0','','Ney York','US','12345','NY','\0','admin@gqm-blind.com','','Administrator','God','a40546cc4fd6a12572828bb803380888ad1bfdab','Something about my sister.','','admin',1,'http://gqmblind.com'),(-2,'\0','\0','Via Filippo Serafini 5','Rome','IT','00173','RM','\0','nicovax7@gmail.com','','Marco','Vasselli','45bcf4bc05478909c504f47edf89633c1460dfd9','My first nick name.','','marco',1,'https://gqm-blind.googlecode.com'),(-1,'\0','\0','Via Virgilio 114','Albano Laziale','IT','00041','RM','\0','luca.mastrogiovanni@gmail.com','','Luca','Mastrogiovanni','41213f768a613c42733e956314b0cf4dc2502377','My first nick name.','','luca',1,'https://gqm-blind.googlecode.com'),(1,'\0','\0','Via Cola di Rienzo 1','Rome','IT','1234','RM','\0','pm1@gqm-blind.com','','Project','Manager 1','0dd1ebccc81d9710bd67cea5120e89790b0ff57d','PWD hint 1.','','Project Manager 1',1,'https://gqm-blind.googlecode.com'),(2,'\0','\0','Via Cola di Rienzo 2','Rome','IT','1234','RM','\0','pm2@gqm-blind.com','','Project','Manager 2','0cc44f0ce1ee2e600839ae16bdd188267faa3a63','PWD hint 1.','','Project Manager 2',1,'https://gqm-blind.googlecode.com'),(3,'\0','\0','Via Lemonia 1','Rome','IT','1234','RM','\0','pe1@gqm-blind.com','','Project','Employee 1','f07a16172ccf528353cb262727618f761dbc2283','PWD hint 1.','','Project Employee 1',1,'https://gqm-blind.googlecode.com'),(4,'\0','\0','Via Lemonia 2','Rome','IT','1234','RM','\0','pe2@gqm-blind.com','','Project','Employee 2','4bc3d71224d71173824729d9d281dbb07066a3fc','PWD hint 1.','','Project Employee 2',1,'https://gqm-blind.googlecode.com'),(5,'\0','\0','Via del Serafico 1','Rome','IT','1234','RM','\0','gqmm1@gqm-blind.com','','GQM','Member 1','4ea45af0e2d23d621ffb144183ca238ed2fd8416','PWD hint 1.','','GQM Member 1',1,'https://gqm-blind.googlecode.com'),(6,'\0','\0','Via del Serafico 2','Rome','IT','1234','RM','\0','gqmm2@gqm-blind.com','','GQM','Member 2','f0f8227d65524fd4a52c7f617db5a1273c36739f','PWD hint 1.','','GQM Member 2',1,'https://gqm-blind.googlecode.com'),(7,'\0','\0','Via del Serafico 3','Rome','IT','1234','RM','\0','gqmm3@gqm-blind.com','','GQM','Member 3','f6926493cca117930d7ffb7255909f204de2a6c2','PWD hint 1.','','GQM Member 3',1,'https://gqm-blind.googlecode.com'),(8,'\0','\0','Via del Serafico 4','Rome','IT','1234','RM','\0','gqmm4@gqm-blind.com','','GQM','Member 4','8a37caeb4384be43fbbe8e679da395cc3337f040','PWD hint 1.','','GQM Member 4',1,'https://gqm-blind.googlecode.com'),(9,'\0','\0','Via del Serafico 5','Rome','IT','1234','RM','\0','gqmm5@gqm-blind.com','','GQM','Member 5','72166ce00bff5ba9d74063f0471c750f5e4745d2','PWD hint 1.','','GQM Member 5',1,'https://gqm-blind.googlecode.com'),(10,'\0','\0','Via del Serafico 6','Rome','IT','1234','RM','\0','gqmm6@gqm-blind.com','','GQM','Member 6','bf26424ee33648ae6381ae8b69072b9c11860d2b','PWD hint 1.','','GQM Member 6',1,'https://gqm-blind.googlecode.com');
/*!40000 ALTER TABLE `app_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goal`
--

DROP TABLE IF EXISTS `goal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal` (
  `goal_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity` varchar(255) DEFAULT NULL,
  `constraints` varchar(255) DEFAULT NULL,
  `context` varchar(255) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `focus` varchar(255) DEFAULT NULL,
  `impact_of_variation` varchar(255) DEFAULT NULL,
  `interpretation_model` int(11) DEFAULT NULL,
  `magnitude` varchar(255) DEFAULT NULL,
  `object` varchar(255) DEFAULT NULL,
  `refinement` varchar(255) DEFAULT NULL,
  `relations` varchar(255) DEFAULT NULL,
  `scope` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `timeframe` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `viewpoint` varchar(255) DEFAULT NULL,
  `ge_id` bigint(20) DEFAULT NULL,
  `go_id` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `project_id` bigint(20) NOT NULL,
  `strategy_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`goal_id`),
  UNIQUE KEY `goal_id` (`goal_id`),
  KEY `FK21F33318496EA4` (`strategy_id`),
  KEY `FK21F3333A83F18` (`go_id`),
  KEY `FK21F333BA9B7810` (`project_id`),
  KEY `FK21F3333A3B362` (`ge_id`),
  KEY `FK21F333C0683AAD` (`parent_id`),
  CONSTRAINT `FK21F33318496EA4` FOREIGN KEY (`strategy_id`) REFERENCES `strategy` (`strategy_id`),
  CONSTRAINT `FK21F3333A3B362` FOREIGN KEY (`ge_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK21F3333A83F18` FOREIGN KEY (`go_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK21F333BA9B7810` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`),
  CONSTRAINT `FK21F333C0683AAD` FOREIGN KEY (`parent_id`) REFERENCES `goal` (`goal_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goal`
--

LOCK TABLES `goal` WRITE;
/*!40000 ALTER TABLE `goal` DISABLE KEYS */;
INSERT INTO `goal` VALUES (-7,NULL,NULL,'Test goal -7','Test goal -7','Test goal -7','Test goal -7',2,NULL,NULL,'',NULL,'Test goal -7','DRAFT','Test goal -7',NULL,'Test goal -7','Test goal -7',6,5,NULL,-1,-1),(-6,NULL,NULL,'Trenitalia company of the Lazio region','Number of railway','Quality perceived by the user (less waiting taim in the railway station)','Increase the number of trains during peak time',2,NULL,NULL,'',NULL,'It is necessary to increase the number of railway','ACCEPTED','Rail service',NULL,'Business Gal','This goal is seen from the point of view of the management',6,5,NULL,-1,-2),(-5,NULL,NULL,'Trenitalia company of the Lazio region','Level of cleanliness','Quality perceived by the user','Increase the cleaning of railway wagon that start from Albano and arrive to Roma',1,NULL,NULL,'',NULL,'It is necessary to increase the cleaning of railway wagon','PROPOSED','Rail service',NULL,'Business Gal','This goal is seen from the point of view of the user',6,5,NULL,-1,NULL),(-4,NULL,NULL,'Trenitalia company of the Lazio region','Quality of air conditioning','Quality perceived by the user','Increase the quality of travel between Albano and Roma',2,NULL,NULL,'Please change the scope',NULL,'It is necessary to improve the quality of air conditioning in the railway wagon','FOR_REVIEW','Rail service',NULL,'Business Gal','This goal is seen from the point of view of the user',6,5,NULL,-1,NULL),(-3,NULL,NULL,'Trenitalia company of the Lazio region','Quality of service','Quality perceived by the user','Increase the quality of the service during the travel between Albano and Roma',2,NULL,NULL,'',NULL,'It is necessary to increase the quelity of the service','APPROVED','Rail service',NULL,'Business Gal','This goal is seen from the point of view of the user',6,5,NULL,-1,-1),(-2,NULL,NULL,'Trenitalia company of the Lazio region','Test1','Quality perceived by the user','Reduce the travel time between Albano and Roma',2,NULL,NULL,'',NULL,'It is necessary to improve the service','PROPOSED','Rail service',NULL,'Business Gal','This goal is seen from the point of view of the user',6,5,NULL,-2,NULL),(-1,NULL,NULL,'Trenitalia company of the Lazio region','Travel time','Quality perceived by the user','Reduce the travel time between Albano and Roma',2,NULL,NULL,'',NULL,'It is necessary to improve the service','APPROVED','Rail service',NULL,'Business Gal','This goal is seen from the point of view of the user',6,5,NULL,-1,-1),(1,'Increase','Jiano price and functionality','AAA','Increase Customer Satisfaction','Customer satisfaction','AAA',2,'10% reduction in number of custmer complaints','Product Jiano',NULL,'Can conflict with development other goals','Web product Division, Jiano Project Manager','APPROVED',NULL,'6 months after software release','Business Goal','AAA',6,5,NULL,1,2),(2,'Decrease','Development cost and functionality','AAA','Decrease reported software bugs ','Reported software bugs','AAA',2,'Decrease reported bugs by 20%','System test process for Jiano',NULL,'Can conflict with development other goals','Web product Division, Jiano Project Manager','APPROVED',NULL,'6 months after software release (might check every week)','Software Goal','AAA',6,5,1,1,4),(3,NULL,NULL,'Web product Division','Decrease reported bugs by 20% compared to prior projects','20% bug slippage compared to prior projects','Increase in the time of development and release of the software',1,NULL,NULL,NULL,NULL,'Web product Division, Jiano Project Manager','APPROVED','System test process',NULL,'Measurement Goal','Test Manager',6,5,2,1,NULL),(4,NULL,NULL,'Web product Division, Research and Development Division','Analyze a pilot poject using a new system test process','Using the new pilot project there will must be a reduction of 20% compared to the previous version of the test process','build a prototype which could not be used in production environment',1,NULL,NULL,NULL,NULL,'Web product Division, Jiano Project Manager','APPROVED','System test process',NULL,'Measurement Goal','Test Manager',6,5,2,1,NULL);
/*!40000 ALTER TABLE `goal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goal_mmdm`
--

DROP TABLE IF EXISTS `goal_mmdm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_mmdm` (
  `goal_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`goal_id`,`user_id`),
  KEY `FK7B20DFD55096BFA4` (`goal_id`),
  KEY `FK7B20DFD5F503D155` (`user_id`),
  CONSTRAINT `FK7B20DFD55096BFA4` FOREIGN KEY (`goal_id`) REFERENCES `goal` (`goal_id`),
  CONSTRAINT `FK7B20DFD5F503D155` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goal_mmdm`
--

LOCK TABLES `goal_mmdm` WRITE;
/*!40000 ALTER TABLE `goal_mmdm` DISABLE KEYS */;
INSERT INTO `goal_mmdm` VALUES (-6,8),(-5,9),(-5,10),(-3,9),(-3,10),(1,9),(1,10),(3,9),(3,10),(4,9);
/*!40000 ALTER TABLE `goal_mmdm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goal_qs`
--

DROP TABLE IF EXISTS `goal_qs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_qs` (
  `goal_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`goal_id`,`user_id`),
  KEY `FKB9536CE5096BFA4` (`goal_id`),
  KEY `FKB9536CEF503D155` (`user_id`),
  CONSTRAINT `FKB9536CE5096BFA4` FOREIGN KEY (`goal_id`) REFERENCES `goal` (`goal_id`),
  CONSTRAINT `FKB9536CEF503D155` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goal_qs`
--

LOCK TABLES `goal_qs` WRITE;
/*!40000 ALTER TABLE `goal_qs` DISABLE KEYS */;
INSERT INTO `goal_qs` VALUES (-6,9),(-5,7),(-5,8),(-3,7),(-3,8),(1,7),(1,8),(3,7),(3,8),(4,7);
/*!40000 ALTER TABLE `goal_qs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goal_question`
--

DROP TABLE IF EXISTS `goal_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_question` (
  `refinement` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `goal_id` bigint(20) NOT NULL DEFAULT '0',
  `question_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`goal_id`,`question_id`),
  KEY `FK864933925096BFA4` (`goal_id`),
  KEY `FK8649339256B5C4` (`question_id`),
  CONSTRAINT `FK864933925096BFA4` FOREIGN KEY (`goal_id`) REFERENCES `goal` (`goal_id`),
  CONSTRAINT `FK8649339256B5C4` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goal_question`
--

LOCK TABLES `goal_question` WRITE;
/*!40000 ALTER TABLE `goal_question` DISABLE KEYS */;
INSERT INTO `goal_question` VALUES (NULL,'PROPOSED',-6,-3),(NULL,'PROPOSED',-5,-1),(NULL,'PROPOSED',-3,-1),(NULL,'APPROVED',3,1),(NULL,'APPROVED',3,2),(NULL,'APPROVED',3,3),(NULL,'APPROVED',3,4),(NULL,'APPROVED',3,5),(NULL,'APPROVED',4,1);
/*!40000 ALTER TABLE `goal_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goal_vote`
--

DROP TABLE IF EXISTS `goal_vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_vote` (
  `goal_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`goal_id`,`user_id`),
  KEY `FK7B2500965096BFA4` (`goal_id`),
  KEY `FK7B250096F503D155` (`user_id`),
  CONSTRAINT `FK7B2500965096BFA4` FOREIGN KEY (`goal_id`) REFERENCES `goal` (`goal_id`),
  CONSTRAINT `FK7B250096F503D155` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goal_vote`
--

LOCK TABLES `goal_vote` WRITE;
/*!40000 ALTER TABLE `goal_vote` DISABLE KEYS */;
INSERT INTO `goal_vote` VALUES (-2,2),(1,1),(1,2),(3,1),(3,2);
/*!40000 ALTER TABLE `goal_vote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measurement`
--

DROP TABLE IF EXISTS `measurement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measurement` (
  `measurement_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `collecting_d` date DEFAULT NULL,
  `collecting_t` varchar(255) DEFAULT NULL,
  `ts` datetime DEFAULT NULL,
  `value` double DEFAULT NULL,
  `measuremento_id` bigint(20) DEFAULT NULL,
  `metric_id` bigint(20) NOT NULL,
  PRIMARY KEY (`measurement_id`),
  KEY `FKF75C839C616367ED` (`measuremento_id`),
  KEY `FKF75C839CF24AD404` (`metric_id`),
  CONSTRAINT `FKF75C839C616367ED` FOREIGN KEY (`measuremento_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FKF75C839CF24AD404` FOREIGN KEY (`metric_id`) REFERENCES `metric` (`metric_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measurement`
--

LOCK TABLES `measurement` WRITE;
/*!40000 ALTER TABLE `measurement` DISABLE KEYS */;
INSERT INTO `measurement` VALUES (1,'2013-06-15','23:02:10','2013-06-22 23:59:20',12,9,2),(2,'2013-06-16','10:02:10','2013-06-22 23:59:20',13,9,3),(3,'2013-06-17','10:02:10','2013-06-22 23:59:20',9,9,3),(4,'2013-06-18','10:02:10','2013-06-22 23:59:20',9,9,3),(5,'2013-01-01','09:00:00','2013-06-22 23:59:20',500,9,5),(6,'2013-02-01','09:00:00','2013-06-22 23:59:20',400,9,5),(7,'2013-03-01','09:00:00','2013-06-22 23:59:20',300,9,5),(8,'2013-04-01','09:00:00','2013-06-22 23:59:20',200,9,5),(9,'2013-05-01','09:00:00','2013-06-22 23:59:20',150,9,5),(10,'2013-06-01','09:00:00','2013-06-22 23:59:20',130,9,5);
/*!40000 ALTER TABLE `measurement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metric`
--

DROP TABLE IF EXISTS `metric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metric` (
  `metric_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `actual_value` double DEFAULT NULL,
  `code` varchar(50) NOT NULL,
  `collecting_type` varchar(50) DEFAULT NULL,
  `hypothesis` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `operation` varchar(50) DEFAULT NULL,
  `satisfying_condition_peration` varchar(50) DEFAULT NULL,
  `satisfying_condition_value` double DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `metric_a_id` bigint(20) DEFAULT NULL,
  `metric_b_id` bigint(20) DEFAULT NULL,
  `mmdmo_id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `scale_id` bigint(20) DEFAULT NULL,
  `unit_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`metric_id`),
  KEY `FK892AE1D0ACE7D0E2` (`metric_a_id`),
  KEY `FK892AE1D0BA9B7810` (`project_id`),
  KEY `FK892AE1D06F88BFDA` (`mmdmo_id`),
  KEY `FK892AE1D026AAC3F0` (`scale_id`),
  KEY `FK892AE1D0ACE84541` (`metric_b_id`),
  KEY `FK892AE1D033F3DE04` (`unit_id`),
  CONSTRAINT `FK892AE1D026AAC3F0` FOREIGN KEY (`scale_id`) REFERENCES `scale` (`scale_id`),
  CONSTRAINT `FK892AE1D033F3DE04` FOREIGN KEY (`unit_id`) REFERENCES `unit` (`unit_id`),
  CONSTRAINT `FK892AE1D06F88BFDA` FOREIGN KEY (`mmdmo_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK892AE1D0ACE7D0E2` FOREIGN KEY (`metric_a_id`) REFERENCES `metric` (`metric_id`),
  CONSTRAINT `FK892AE1D0ACE84541` FOREIGN KEY (`metric_b_id`) REFERENCES `metric` (`metric_id`),
  CONSTRAINT `FK892AE1D0BA9B7810` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metric`
--

LOCK TABLES `metric` WRITE;
/*!40000 ALTER TABLE `metric` DISABLE KEYS */;
INSERT INTO `metric` VALUES (1,10,'DEVTIME','SINGLE_VALUE','264','Development time',NULL,'LESS_OR_EQUAL',260,'SUBJECTIVE',NULL,NULL,9,1,-4,-11),(2,100,'NBUGOLD','SINGLE_VALUE','Less or more 200','Number of bug on old system',NULL,NULL,NULL,'OBJECTIVE',NULL,NULL,9,1,-4,NULL),(3,12,'NBUGNEW','SINGLE_VALUE','Less then 150','Number of bug on new system',NULL,NULL,NULL,'OBJECTIVE',NULL,NULL,9,1,-4,NULL),(4,1,'RATIOBUG','SINGLE_VALUE','Less then 0.8','Ratio between ild bug and new bug','DIVISION','LESS_OR_EQUAL',0.8,'OBJECTIVE',2,3,9,1,-4,NULL),(5,200,'#FAULT','MULTIPLE_VALUE','Less then 0.8','# faults found in system test in current project',NULL,'GREATER_OR_EQUAL',190,'OBJECTIVE',NULL,NULL,9,1,-4,NULL);
/*!40000 ALTER TABLE `metric` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `project_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `po_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`project_id`),
  KEY `FK50C8E2F942712A1` (`po_id`),
  CONSTRAINT `FK50C8E2F942712A1` FOREIGN KEY (`po_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (-2,'Cineca is a non profit Consortium, made up of 56 Italian universities*, and 3 Institutions.','CINECA',1),(-1,'Trenitalia is the primary train operator in Italy','Trenitalia',1),(1,'Replacement of the management software of beds developed by Cineca. Cineca is a non profit Consortium, made up of 56 Italian universities, and 3 Institutions.','Application replacement',1);
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_gqm`
--

DROP TABLE IF EXISTS `project_gqm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_gqm` (
  `project_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  KEY `FKC7FFD69DBA9B7810` (`project_id`),
  KEY `FKC7FFD69DF503D155` (`user_id`),
  CONSTRAINT `FKC7FFD69DBA9B7810` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`),
  CONSTRAINT `FKC7FFD69DF503D155` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_gqm`
--

LOCK TABLES `project_gqm` WRITE;
/*!40000 ALTER TABLE `project_gqm` DISABLE KEYS */;
INSERT INTO `project_gqm` VALUES (-1,5),(-1,6),(-1,7),(-1,8),(-1,9),(-1,10),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10);
/*!40000 ALTER TABLE `project_gqm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_pm`
--

DROP TABLE IF EXISTS `project_pm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_pm` (
  `project_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  KEY `FK8A94A4E3BA9B7810` (`project_id`),
  KEY `FK8A94A4E3F503D155` (`user_id`),
  CONSTRAINT `FK8A94A4E3BA9B7810` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`),
  CONSTRAINT `FK8A94A4E3F503D155` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_pm`
--

LOCK TABLES `project_pm` WRITE;
/*!40000 ALTER TABLE `project_pm` DISABLE KEYS */;
INSERT INTO `project_pm` VALUES (-1,1),(-1,2),(1,1),(1,2);
/*!40000 ALTER TABLE `project_pm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_team`
--

DROP TABLE IF EXISTS `project_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_team` (
  `project_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  KEY `FK3800B7C3BA9B7810` (`project_id`),
  KEY `FK3800B7C3F503D155` (`user_id`),
  CONSTRAINT `FK3800B7C3BA9B7810` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`),
  CONSTRAINT `FK3800B7C3F503D155` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_team`
--

LOCK TABLES `project_team` WRITE;
/*!40000 ALTER TABLE `project_team` DISABLE KEYS */;
INSERT INTO `project_team` VALUES (-1,1),(-1,2),(-1,3),(-1,4),(1,1),(1,2),(1,3),(1,4);
/*!40000 ALTER TABLE `project_team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `question_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `text` varchar(255) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `qso_id` bigint(20) NOT NULL,
  PRIMARY KEY (`question_id`),
  UNIQUE KEY `question_id` (`question_id`),
  KEY `FKBE5CA006BA9B7810` (`project_id`),
  KEY `FKBE5CA006C524F753` (`qso_id`),
  CONSTRAINT `FKBE5CA006BA9B7810` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`),
  CONSTRAINT `FKBE5CA006C524F753` FOREIGN KEY (`qso_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (-5,'Temperature inside the wagon','how many degrees there are in the railwai wagon during the summer months?',-1,7),(-4,'Time left in station','How long is a train at the railway station?',-1,7),(-3,'Percentage of train with problem','What is the percentage of train whose involved problems?',-1,7),(-2,'Trip duration','How long a journey between Rome and Albano?',-2,8),(-1,'Number of train','How many trains run from 7 to 10?',-1,7),(1,'Number of bugs reported in the previous system','how many bugs were found in the old software?',1,7),(2,'Number of bugs reported in the Jiano system','how many bugs were found in the Jiano software?',1,7),(3,'Number of test developer','how many test developers work on the new version of the Jiano software?',1,7),(4,'Number of test developer in the old system','how many test developers worked on the the previous version of the software?',1,7),(5,'Weeks to implement new test process','how many weeks are required to implement a new test process?',1,7);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_metric`
--

DROP TABLE IF EXISTS `question_metric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question_metric` (
  `refinement` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `question_id` bigint(20) NOT NULL DEFAULT '0',
  `metric_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`metric_id`,`question_id`),
  KEY `FK6A19A829F24AD404` (`metric_id`),
  KEY `FK6A19A82956B5C4` (`question_id`),
  CONSTRAINT `FK6A19A82956B5C4` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`),
  CONSTRAINT `FK6A19A829F24AD404` FOREIGN KEY (`metric_id`) REFERENCES `metric` (`metric_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_metric`
--

LOCK TABLES `question_metric` WRITE;
/*!40000 ALTER TABLE `question_metric` DISABLE KEYS */;
INSERT INTO `question_metric` VALUES (NULL,'APPROVED',1,1),(NULL,'APPROVED',2,1),(NULL,'APPROVED',1,2),(NULL,'APPROVED',2,2),(NULL,'APPROVED',2,3),(NULL,'APPROVED',2,4),(NULL,'APPROVED',2,5);
/*!40000 ALTER TABLE `question_metric` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(64) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (-2,'Default role for all Users','ROLE_USER'),(-1,'Administrator role (can edit Users and create project.)','ROLE_ADMIN');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scale`
--

DROP TABLE IF EXISTS `scale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scale` (
  `scale_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) DEFAULT NULL,
  `examples` varchar(4000) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `operations` varchar(255) DEFAULT NULL,
  `type` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`scale_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scale`
--

LOCK TABLES `scale` WRITE;
/*!40000 ALTER TABLE `scale` DISABLE KEYS */;
INSERT INTO `scale` VALUES (-4,'Quantitative','=;!=;>;<;+;-;*;/','Ratio',NULL,'\n            	 Age (from 0  to 99 years)'),(-3,'Quantitative','=;!=;>;<;+;-','Interval',NULL,'\n            	Date (from 9999 BC to 2013 AD) /\n				Latitude (from +90° to −90°)'),(-2,'Qualitative','=;!=;>;<','Ordinal',NULL,'\n            	Dichotomous:     Health\n                            (healthy vs.\n                            sick),\n                         Truth\n                            (true vs.\n                            false),\n                         Beauty\n                            (beautiful vs.\n                            ugly)\n            	Non-dichotomous: Opinion\n                            (\'completely agree\'/\n                             \'mostly agree\'/\n                             \'mostly disagree\'/\n                             \'completely disagree\')'),(-1,'Qualitative','=;!=','Nominal',NULL,'Dichotomous: Gender (male vs. female), Non-dichotomous: Nationality (American/Chinese/etc)');
/*!40000 ALTER TABLE `scale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `strategy`
--

DROP TABLE IF EXISTS `strategy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `strategy` (
  `strategy_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `assumption` varchar(4000) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `so_id` bigint(20) NOT NULL,
  PRIMARY KEY (`strategy_id`),
  UNIQUE KEY `strategy_id` (`strategy_id`),
  KEY `FK6E6A0793BA9B7810` (`project_id`),
  KEY `FK6E6A07934515924` (`so_id`),
  CONSTRAINT `FK6E6A07934515924` FOREIGN KEY (`so_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK6E6A0793BA9B7810` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `strategy`
--

LOCK TABLES `strategy` WRITE;
/*!40000 ALTER TABLE `strategy` DISABLE KEYS */;
INSERT INTO `strategy` VALUES (-3,'For trains cleaner is necessary to increase the number of people who carry out cleaning.','increase the number of people who do the cleaning',-1,1),(-2,'To reduce the travel time is necessary to strengthen the trains. With faster trains travel time of each leg will be shorter','Increase the maximum speed of trains',-1,2),(-1,'For getting more customers in the insurance area, the quality of the customer interaction processes has to be improved.','Improve customer interaction process',-1,1),(1,'For cost reduction is necessary to improve the developlemt process','Cost reduction',1,1),(2,'For improve the quality of poduct it\'s possibile, for example, reduce the number of bug','Improve quality of product',1,1),(3,'The most experienced personnel produce code with fewer bugs','Improve the quality of development team',1,2),(4,'Increasing the test module and the functional test, you will produce software with fewer bugs','Improve the testing process',1,2);
/*!40000 ALTER TABLE `strategy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unit`
--

DROP TABLE IF EXISTS `unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unit` (
  `unit_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `base_unit` char(1) DEFAULT NULL,
  `multiples` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `physical` varchar(50) DEFAULT NULL,
  `symbol` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`unit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit`
--

LOCK TABLES `unit` WRITE;
/*!40000 ALTER TABLE `unit` DISABLE KEYS */;
INSERT INTO `unit` VALUES (-11,'F',86400,'Man-days','time','man-days'),(-10,'T',1,'Celsius','temperature','T'),(-9,'T',1,'Kelvin','temperature','K'),(-8,'F',3600,'Hour','time','hr'),(-7,'F',60,'Minute','time','m'),(-6,'F',1000,'Kilometre','lenght','km'),(-5,'T',1,'Second','time','s'),(-4,'T',1,'Kilogram','mass','kg'),(-3,'T',1,'Metre','lenght','m'),(-2,'T',1,'Lumen','luminous flux','lm'),(-1,'T',1,'Radial','angle','rad');
/*!40000 ALTER TABLE `unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK143BF46A4FD90D75` (`role_id`),
  KEY `FK143BF46AF503D155` (`user_id`),
  CONSTRAINT `FK143BF46A4FD90D75` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK143BF46AF503D155` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (-3,-2),(-2,-2),(-1,-2),(1,-2),(2,-2),(3,-2),(4,-2),(5,-2),(6,-2),(7,-2),(8,-2),(9,-2),(10,-2),(-3,-1),(-2,-1),(-1,-1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-07-05  8:34:13
