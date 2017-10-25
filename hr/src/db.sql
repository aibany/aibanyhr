-- MySQL dump 10.13  Distrib 5.7.19, for osx10.12 (x86_64)
--
-- Host: localhost    Database: aeaihr
-- ------------------------------------------------------
-- Server version	5.7.19

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

/*
 * create database aeaihr default character set utf8 collate utf8_general_ci;
 * create user 'libo'@'%' identified by 'libo888';
 *  grant all privileges on aeaihr.* to 'libo';
 */

--
-- Table structure for table `SYS_LOG`
--

DROP TABLE IF EXISTS `SYS_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SYS_LOG` (
  `ID` char(36) DEFAULT NULL,
  `OPER_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `IP_ADDTRESS` varchar(32) DEFAULT NULL,
  `USER_ID` varchar(32) DEFAULT NULL,
  `USER_NAME` varchar(32) DEFAULT NULL,
  `FUNC_NAME` varchar(64) DEFAULT NULL,
  `ACTION_TYPE` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SYS_LOG`
--

LOCK TABLES `SYS_LOG` WRITE;
/*!40000 ALTER TABLE `SYS_LOG` DISABLE KEYS */;
INSERT INTO `SYS_LOG` VALUES ('7A6F6BF3-22C0-4769-9B80-33936BB11E80','2017-08-16 08:32:36','0:0:0:0:0:0:0:1','admin','管理员','退出系统','logout'),('E9CD6A9B-EBEB-4981-A60C-5C5D14084019','2017-08-16 08:32:39','0:0:0:0:0:0:0:1','admin','管理员','账号密码登录','login'),('DB41F411-E2A7-4F99-AFC3-AA15CD6600D5','2017-08-16 08:33:30','0:0:0:0:0:0:0:1','admin','管理员','退出系统','logout'),('8587CC1F-7D18-4A2A-BAAF-F591D823DAED','2017-08-16 08:33:34','0:0:0:0:0:0:0:1','admin','管理员','账号密码登录','login'),('95CFA80C-3F0B-415B-9A5A-8288F016D135','2017-08-16 08:34:45','0:0:0:0:0:0:0:1','admin','管理员','退出系统','logout'),('637CFA60-907A-493B-9AB7-FCD1B12607C9','2017-08-16 08:34:48','0:0:0:0:0:0:0:1','admin','管理员','账号密码登录','login');
/*!40000 ALTER TABLE `SYS_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_attendance`
--

DROP TABLE IF EXISTS `hr_attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_attendance` (
  `ATD_ID` char(36) NOT NULL,
  `ATD_DATE` varchar(32) DEFAULT NULL,
  `USER_ID` char(36) DEFAULT NULL,
  `ATD_IN_TIME` datetime DEFAULT NULL,
  `ATD_IN_PLACE` varchar(32) DEFAULT NULL,
  `ATD_IN_HOUSE` varchar(32) DEFAULT '夜班',
  `ATD_IN_COORDINATE` varchar(128) DEFAULT NULL,
  `ATD_OUT_TIME` datetime DEFAULT NULL,
  `ATD_OUT_PLACE` varchar(32) DEFAULT NULL,
  `ATD_OUT_HOUSE` varchar(32) DEFAULT NULL,
  `ATD_OUT_COORDINATE` varchar(128) DEFAULT NULL,
  `ATD_OVERTIME` decimal(8,1) DEFAULT '0.0',
  PRIMARY KEY (`ATD_ID`),
  KEY `index_query` (`ATD_DATE`,`ATD_IN_TIME`,`ATD_OUT_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_attendance`
--

LOCK TABLES `hr_attendance` WRITE;
/*!40000 ALTER TABLE `hr_attendance` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_education`
--

DROP TABLE IF EXISTS `hr_education`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_education` (
  `EDU_ID` char(36) NOT NULL,
  `EMP_ID` char(36) DEFAULT NULL,
  `EDU_IN_TIME` date DEFAULT NULL,
  `EDU_OUT_TIME` date DEFAULT NULL,
  `EDU_EDUCATION` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`EDU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_education`
--

LOCK TABLES `hr_education` WRITE;
/*!40000 ALTER TABLE `hr_education` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_education` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_employee`
--

DROP TABLE IF EXISTS `hr_employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_employee` (
  `EMP_ID` char(36) NOT NULL,
  `EMP_CODE` varchar(32) DEFAULT NULL,
  `EMP_NAME` varchar(32) DEFAULT NULL,
  `EMP_SEX` char(1) DEFAULT NULL,
  `EMP_BIRTHDAY` date DEFAULT NULL,
  `EMP_NATIONAL` varchar(32) DEFAULT NULL,
  `EMP_PARTY` char(2) DEFAULT NULL,
  `EMP_ID_NUMBER` varchar(32) DEFAULT NULL,
  `EMP_TEL` varchar(32) DEFAULT NULL,
  `EMP_NATIVE_PLACE` varchar(32) DEFAULT NULL,
  `EMP_MARITAL_STATUS` varchar(32) DEFAULT NULL,
  `EMP_REFERENCE_TIME` date DEFAULT NULL,
  `EMP_NOW_DEPT` char(36) DEFAULT NULL,
  `EMP_NOW_JOB` varchar(32) DEFAULT NULL,
  `EMP_INDUCTION_TIME` date DEFAULT NULL,
  `EMP_EMAIL` varchar(32) DEFAULT NULL,
  `EMP_REGULAR_TIME` date DEFAULT NULL,
  `EMP_EDUCATION` varchar(32) DEFAULT NULL,
  `EMP_STATE` varchar(32) DEFAULT NULL,
  `EMP_BASIC` decimal(8,2) DEFAULT NULL,
  `EMP_PERFORMANCE` decimal(8,2) DEFAULT NULL,
  `EMP_SUBSIDY` decimal(8,2) DEFAULT NULL,
  `EMP_WORK_STATE` varchar(32) DEFAULT NULL,
  `EMP_DIMISSION_TIME` date DEFAULT NULL,
  `EMP_TAX` decimal(8,2) DEFAULT '0.00',
  `EMP_INSURE` decimal(8,2) DEFAULT '0.00',
  `EMP_ANNUAL_LEAVE_DAYS` varchar(5) DEFAULT NULL,
  `EMP_ALLOWANCE` decimal(8,2) DEFAULT '0.00',
  `EMP_PARTICIPATE_SALARY` varchar(32) DEFAULT NULL,
  `EMP_AWARD_ALLDAYS` decimal(8,2) DEFAULT '0.00',
  `EMP_ALLOWANCE_MIDROOM` decimal(8,2) DEFAULT '0.00',
  `EMP_ALLOWANCE_NIGHT` decimal(8,2) DEFAULT '0.00',
  `EMP_ALLOWANCE_OTHER` decimal(8,2) DEFAULT '0.00',
  `EMP_FUND_HOUSE` decimal(8,2) DEFAULT '0.00',
  `EMP_EMERG_MAN` varchar(32) DEFAULT '',
  `EMP_EMERG_PHONE` varchar(32) DEFAULT '',
  `EMP_ALLOWANCE_YEAR` decimal(8,2) DEFAULT '0.00',
  `EMP_BANK_CARD` varchar(32) DEFAULT '',
  `EMP_BANK_CARDINFO` varchar(128) DEFAULT '',
  PRIMARY KEY (`EMP_ID`),
  KEY `index_query` (`EMP_CODE`,`EMP_NAME`,`EMP_STATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_employee`
--

LOCK TABLES `hr_employee` WRITE;
/*!40000 ALTER TABLE `hr_employee` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_evection`
--

DROP TABLE IF EXISTS `hr_evection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_evection` (
  `EVE_ID` char(36) NOT NULL,
  `EVE_APPLY_USER` char(36) DEFAULT NULL,
  `EVE_TOGETHER` varchar(32) DEFAULT NULL,
  `EVE_START_TIME` datetime DEFAULT NULL,
  `EVE_OVER_TIME` date DEFAULT NULL,
  `EVE_REIMBURSEMENT_TIME` datetime DEFAULT NULL,
  `EVE_DAYS` int(11) DEFAULT NULL,
  `EVE_SUBSIDY` decimal(6,2) DEFAULT NULL,
  `EVE_APPROVE_USER` char(36) DEFAULT NULL,
  `EVE_APPROVE_TIME` date DEFAULT NULL,
  `APP_RESULT` varchar(32) DEFAULT NULL,
  `EVE_APPROVE_OPINION` varchar(256) DEFAULT NULL,
  `EVE_TOTAL_MONEY` decimal(6,2) DEFAULT NULL,
  `EVE_REASON` varchar(256) DEFAULT NULL,
  `STATE` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`EVE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_evection`
--

LOCK TABLES `hr_evection` WRITE;
/*!40000 ALTER TABLE `hr_evection` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_evection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_expenses`
--

DROP TABLE IF EXISTS `hr_expenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_expenses` (
  `EXPE_ID` char(36) NOT NULL,
  `EVE_ID` char(36) DEFAULT NULL,
  `EXPE_DEPART` varchar(32) DEFAULT NULL,
  `EXPE_DESTINATION` varchar(32) DEFAULT NULL,
  `EXPE_DEPART_TIME` datetime DEFAULT NULL,
  `EXPE_COMEBACK_TIME` date DEFAULT NULL,
  `EXPE_TRANSPORTATION_WAY` varchar(32) DEFAULT NULL,
  `EXPE_TRANSPORTATION_FEE` decimal(6,2) DEFAULT NULL,
  `EXPE_HOTEL` decimal(6,2) DEFAULT NULL,
  `EXPE_OTHER` decimal(6,2) DEFAULT NULL,
  `EXPE_REMARKS` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`EXPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_expenses`
--

LOCK TABLES `hr_expenses` WRITE;
/*!40000 ALTER TABLE `hr_expenses` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_expenses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_experience`
--

DROP TABLE IF EXISTS `hr_experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_experience` (
  `EXP_ID` char(36) NOT NULL,
  `EMP_ID` char(36) DEFAULT NULL,
  `EXP_IN_TIME` date DEFAULT NULL,
  `EXP_OUT_TIME` date DEFAULT NULL,
  `EXP_EXPERIENCE` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`EXP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_experience`
--

LOCK TABLES `hr_experience` WRITE;
/*!40000 ALTER TABLE `hr_experience` DISABLE KEYS */;
INSERT INTO `hr_experience` VALUES ('135CD682-8EF9-4A46-A81F-4829270C7057','82F48D66-7026-46B9-96A9-2A94EBFD70D0','2017-03-31','2017-03-25','');
/*!40000 ALTER TABLE `hr_experience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_leave`
--

DROP TABLE IF EXISTS `hr_leave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_leave` (
  `LEA_ID` char(36) NOT NULL,
  `USER_ID` char(36) DEFAULT NULL,
  `LEA_DATE` datetime DEFAULT NULL,
  `LEA_SDATE` date DEFAULT NULL,
  `LEA_EDATE` date DEFAULT NULL,
  `LEA_DAYS` varchar(32) DEFAULT NULL,
  `LEA_TYPE` varchar(12) DEFAULT NULL,
  `LEA_CAUSE` varchar(256) DEFAULT NULL,
  `STATE` varchar(32) DEFAULT NULL,
  `LEA_APPOVER` char(36) DEFAULT NULL,
  `LEA_APP_TIME` datetime DEFAULT NULL,
  `LEA_APP_OPINION` varchar(256) DEFAULT NULL,
  `APP_RESULT` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`LEA_ID`),
  KEY `index_query` (`LEA_DATE`,`LEA_SDATE`,`LEA_EDATE`,`STATE`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_leave`
--

LOCK TABLES `hr_leave` WRITE;
/*!40000 ALTER TABLE `hr_leave` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_leave` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_leave_record`
--

DROP TABLE IF EXISTS `hr_leave_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_leave_record` (
  `REC_ID` varchar(36) NOT NULL,
  `EMP_CODE` varchar(36) DEFAULT NULL,
  `EMP_NAME` varchar(64) DEFAULT NULL,
  `EMP_JOB` varchar(128) DEFAULT NULL,
  `LEAVE_RESON` varchar(1024) DEFAULT NULL,
  `OP_TIME` datetime DEFAULT NULL,
  `IN_TIME` datetime DEFAULT NULL,
  `OP_NAME` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`REC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_leave_record`
--

LOCK TABLES `hr_leave_record` WRITE;
/*!40000 ALTER TABLE `hr_leave_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_leave_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_location`
--

DROP TABLE IF EXISTS `hr_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_location` (
  `LOCAT_ID` char(36) NOT NULL,
  `USER_ID` char(36) DEFAULT NULL,
  `LOCAT_TIME` datetime DEFAULT NULL,
  `LOCAT_PLACE` varchar(64) DEFAULT NULL,
  `LOCAT_HOUSE` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`LOCAT_ID`),
  KEY `index_query` (`USER_ID`,`LOCAT_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_location`
--

LOCK TABLES `hr_location` WRITE;
/*!40000 ALTER TABLE `hr_location` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_salary`
--

DROP TABLE IF EXISTS `hr_salary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_salary` (
  `SAL_ID` char(36) NOT NULL,
  `SAL_USER` char(36) DEFAULT NULL,
  `SAL_YEAR` varchar(32) DEFAULT NULL,
  `SAL_MONTH` varchar(32) DEFAULT NULL,
  `SAL_TOTAL` decimal(8,2) DEFAULT NULL,
  `SAL_ACTUAL` decimal(8,2) DEFAULT NULL,
  `SAL_VALID_DAYS` decimal(3,1) DEFAULT NULL,
  `SAL_WORK_DAYS` decimal(3,1) DEFAULT NULL,
  `SAL_BASIC` decimal(8,2) DEFAULT NULL,
  `SAL_OVERTIME_NORMAL` int(11) DEFAULT '0',
  `SAL_OVERTIME_WEEKEND` int(11) DEFAULT '0',
  `SAL_OVERTIME_HOLIDAY` int(11) DEFAULT '0',
  `SAL_OVERTIME` decimal(10,2) DEFAULT '0.00',
  `SAL_AWARD_ALLDAYS` decimal(5,2) DEFAULT NULL,
  `SAL_ALLOWANCE_MIDROOM` decimal(5,2) DEFAULT NULL,
  `SAL_ALLOWANCE_NIGHT` decimal(5,2) DEFAULT NULL,
  `SAL_ALLOWANCE_OTHER` decimal(5,2) DEFAULT NULL,
  `SAL_PERFORMANCE` decimal(8,2) DEFAULT NULL,
  `SAL_SUBSIDY` decimal(8,2) DEFAULT NULL,
  `SAL_ALLOWANCE` decimal(8,2) DEFAULT NULL,
  `SAL_ALLOWANCE_OUT` decimal(8,2) DEFAULT NULL,
  `SAL_ALLOWANCE_AIR` decimal(4,2) DEFAULT NULL,
  `SAL_ALLOWANCE_ALL` decimal(5,2) DEFAULT NULL,
  `SAL_ALLOWANCE_YEAR` decimal(8,2) DEFAULT NULL,
  `SAL_ALLOWANCE_RECORD` decimal(5,2) DEFAULT NULL,
  `SAL_ALLOWANCE_RECORD_WHAT` varchar(256) DEFAULT NULL,
  `SAL_DEDUCT_NOWORKDAYS` decimal(5,2) DEFAULT NULL,
  `SAL_DEDUCT_NOWORK` decimal(5,2) DEFAULT NULL,
  `SAL_DEDUCT_RECORD_WHAT` varchar(256) DEFAULT NULL,
  `SAL_DEDUCT_EAT` decimal(5,2) DEFAULT NULL,
  `SAL_DEDUCT_SIGNCARD` decimal(5,2) DEFAULT NULL,
  `SAL_DEDUCT_LEAVE` decimal(5,2) DEFAULT NULL,
  `SAL_INSURE` decimal(5,2) DEFAULT NULL,
  `SAL_FUND_HOUSE` decimal(5,2) DEFAULT NULL,
  `SAL_FUND_ILL` decimal(5,2) DEFAULT NULL,
  `SAL_DEDUCT_BUYCARD` decimal(5,2) DEFAULT NULL,
  `SAL_DEDUCT_LATER` decimal(5,2) DEFAULT NULL,
  `SAL_TAX` decimal(5,2) DEFAULT NULL,
  `SAL_REMARKS` varchar(512) DEFAULT NULL,
  `SAL_STATE` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`SAL_ID`),
  KEY `index_query` (`SAL_USER`,`SAL_STATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_salary`
--

LOCK TABLES `hr_salary` WRITE;
/*!40000 ALTER TABLE `hr_salary` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_salary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_salrecord`
--

DROP TABLE IF EXISTS `hr_salrecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_salrecord` (
  `REC_ID` varchar(36) NOT NULL,
  `SAL_CODE` varchar(36) DEFAULT NULL,
  `SAL_NAME` varchar(64) DEFAULT NULL,
  `REC_TYPE` varchar(64) DEFAULT NULL,
  `OLD_VALUE` varchar(64) DEFAULT NULL,
  `NEW_VALUE` varchar(64) DEFAULT NULL,
  `OP_TIME` datetime DEFAULT NULL,
  `OP_NAME` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`REC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_salrecord`
--

LOCK TABLES `hr_salrecord` WRITE;
/*!40000 ALTER TABLE `hr_salrecord` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_salrecord` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_valid`
--

DROP TABLE IF EXISTS `hr_valid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_valid` (
  `VALID_YEAR` varchar(32) NOT NULL,
  `VALID_MONTH` varchar(32) NOT NULL,
  `VALID_DAYS` decimal(3,1) DEFAULT NULL,
  PRIMARY KEY (`VALID_YEAR`,`VALID_MONTH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_valid`
--

LOCK TABLES `hr_valid` WRITE;
/*!40000 ALTER TABLE `hr_valid` DISABLE KEYS */;
INSERT INTO `hr_valid` VALUES ('2017','01',22.0),('2017','02',22.0),('2017','04',19.0),('2017','05',21.0),('2017','06',22.0);
/*!40000 ALTER TABLE `hr_valid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_work_overtime`
--

DROP TABLE IF EXISTS `hr_work_overtime`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_work_overtime` (
  `WOT_ID` char(36) NOT NULL,
  `USER_ID` char(36) DEFAULT NULL,
  `WOT_DATE` varchar(32) DEFAULT NULL,
  `WOT_PARTICIPANT` varchar(32) DEFAULT NULL,
  `WOT_PLACE` varchar(32) DEFAULT NULL,
  `WOT_DESC` varchar(256) DEFAULT NULL,
  `WOT_OVERTIME_DATE` date DEFAULT NULL,
  `WOT_TIME` varchar(32) DEFAULT NULL,
  `WOT_APPROVER` char(36) DEFAULT NULL,
  `WOT_APP_TIME` datetime DEFAULT NULL,
  `APP_RESULT` varchar(32) DEFAULT NULL,
  `WOT_APP_OPINION` varchar(256) DEFAULT NULL,
  `STATE` varchar(32) DEFAULT NULL,
  `WOT_TIME_COMPANY` varchar(32) DEFAULT NULL,
  `WOT_DAYTYPE` varchar(12) DEFAULT '工作日',
  `WOT_CALC_HOURS` decimal(5,2) DEFAULT '0.00',
  PRIMARY KEY (`WOT_ID`),
  KEY `index_query` (`USER_ID`,`WOT_DATE`,`STATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_work_overtime`
--

LOCK TABLES `hr_work_overtime` WRITE;
/*!40000 ALTER TABLE `hr_work_overtime` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_work_overtime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_work_performance`
--

DROP TABLE IF EXISTS `hr_work_performance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hr_work_performance` (
  `PER_ID` char(36) NOT NULL,
  `EMP_ID` char(36) DEFAULT NULL,
  `PER_IN_TIME` date DEFAULT NULL,
  `PER_NOW_TIME` date DEFAULT NULL,
  `PER_WORK_PERFORMANCE` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`PER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_work_performance`
--

LOCK TABLES `hr_work_performance` WRITE;
/*!40000 ALTER TABLE `hr_work_performance` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_work_performance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_group`
--

DROP TABLE IF EXISTS `security_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_group` (
  `GRP_ID` varchar(36) NOT NULL,
  `GRP_CODE` varchar(32) DEFAULT NULL,
  `GRP_NAME` varchar(32) DEFAULT NULL,
  `GRP_PID` varchar(36) DEFAULT NULL,
  `GRP_DESC` varchar(128) DEFAULT NULL,
  `GRP_STATE` varchar(1) DEFAULT NULL,
  `GRP_SORT` int(11) DEFAULT NULL,
  PRIMARY KEY (`GRP_ID`),
  KEY `index_query` (`GRP_CODE`,`GRP_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_group`
--

LOCK TABLES `security_group` WRITE;
/*!40000 ALTER TABLE `security_group` DISABLE KEYS */;
INSERT INTO `security_group` VALUES ('00000000-0000-0000-00000000000000000','Root','公司集团','','制造部','1',0),('0E12268C-BD14-4FCE-A7C0-A8A5D55ED842','ADM','行政部','00000000-0000-0000-00000000000000000','','1',7),('3435DD86-53B8-41DC-BE10-0463428D18D6','DEV','研发部','00000000-0000-0000-00000000000000000','','1',5),('42FE2AE3-DBA0-498B-86BC-05E35D123A3E','SALE','营销部','00000000-0000-0000-00000000000000000','','1',6),('72A9E7B1-5B47-4822-ACA0-A8CB1EE78506','TECH','技术部','00000000-0000-0000-00000000000000000','','1',4),('FDB148EF-9146-4675-AB8E-0CE85164B67A','HR','人力部','00000000-0000-0000-00000000000000000','','1',3);
/*!40000 ALTER TABLE `security_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_group_auth`
--

DROP TABLE IF EXISTS `security_group_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_group_auth` (
  `GRP_AUTH_ID` varchar(36) NOT NULL,
  `GRP_ID` varchar(36) DEFAULT NULL,
  `RES_TYPE` varchar(32) DEFAULT NULL,
  `RES_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`GRP_AUTH_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_group_auth`
--

LOCK TABLES `security_group_auth` WRITE;
/*!40000 ALTER TABLE `security_group_auth` DISABLE KEYS */;
INSERT INTO `security_group_auth` VALUES ('25DA0120-EC1F-4C80-BCFF-C1DD27FD0EAA','FDB148EF-9146-4675-AB8E-0CE85164B67A','Menu','AD7CE5A0-39C4-4B43-B243-D056BBDF9332'),('93EF8C63-845C-4CE0-9AF7-EA00FDEAEBFD','FDB148EF-9146-4675-AB8E-0CE85164B67A','Menu','00000000-0000-0000-00000000000000000');
/*!40000 ALTER TABLE `security_group_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_role`
--

DROP TABLE IF EXISTS `security_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_role` (
  `ROLE_ID` varchar(36) NOT NULL,
  `ROLE_CODE` varchar(32) DEFAULT NULL,
  `ROLE_NAME` varchar(32) DEFAULT NULL,
  `ROLE_PID` varchar(36) DEFAULT NULL,
  `ROLE_DESC` varchar(128) DEFAULT NULL,
  `ROLE_STATE` varchar(32) DEFAULT NULL,
  `ROLE_SORT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`),
  KEY `index_query` (`ROLE_CODE`,`ROLE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_role`
--

LOCK TABLES `security_role` WRITE;
/*!40000 ALTER TABLE `security_role` DISABLE KEYS */;
INSERT INTO `security_role` VALUES ('00000000-0000-0000-00000000000000000','System','系统角色',NULL,NULL,'1',NULL),('61729215-7F01-417B-A7A9-C4B4E0B9A0A3','EMPLOYEE','普通职员','00000000-0000-0000-00000000000000000','','1',4),('7506D7DA-8E26-49AE-AF5E-40E8912172A4','HR_MASTER','人力负责人','00000000-0000-0000-00000000000000000','','1',5),('895A1379-0B71-4390-955D-92A49ABABDC0','SALARY_MASTER','薪资负责人','00000000-0000-0000-00000000000000000','','1',7),('D7CEA7EC-8CC9-42A3-8125-1FAE15661A48','APPROVE','业务核准人','00000000-0000-0000-00000000000000000','','1',6);
/*!40000 ALTER TABLE `security_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_role_auth`
--

DROP TABLE IF EXISTS `security_role_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_role_auth` (
  `ROLE_AUTH_ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) DEFAULT NULL,
  `RES_TYPE` varchar(32) DEFAULT NULL,
  `RES_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ROLE_AUTH_ID`),
  KEY `index_query` (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_role_auth`
--

LOCK TABLES `security_role_auth` WRITE;
/*!40000 ALTER TABLE `security_role_auth` DISABLE KEYS */;
/*!40000 ALTER TABLE `security_role_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_role_group_rel`
--

DROP TABLE IF EXISTS `security_role_group_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_role_group_rel` (
  `GRP_ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`GRP_ID`,`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_role_group_rel`
--

LOCK TABLES `security_role_group_rel` WRITE;
/*!40000 ALTER TABLE `security_role_group_rel` DISABLE KEYS */;
INSERT INTO `security_role_group_rel` VALUES ('00000000-0000-0000-00000000000000000','00000000-0000-0000-00000000000000000'),('21E50836-912E-4DB8-82CB-9B32C8A44C9F','00000000-0000-0000-00000000000000000'),('21E50836-912E-4DB8-82CB-9B32C8A44C9F','25788B54-CE0A-4137-8890-EFA4F0DE06B6'),('23F5915B-C8F1-4BA5-AED9-7CE50B11D5F4','00000000-0000-0000-00000000000000000'),('23F5915B-C8F1-4BA5-AED9-7CE50B11D5F4','25788B54-CE0A-4137-8890-EFA4F0DE06B6'),('315F898C-A008-4F77-BAB0-4FDF935F7B1F','00000000-0000-0000-00000000000000000'),('315F898C-A008-4F77-BAB0-4FDF935F7B1F','25788B54-CE0A-4137-8890-EFA4F0DE06B6'),('BBD420A2-68AE-49C2-B3D8-78DC166F511F','00000000-0000-0000-00000000000000000');
/*!40000 ALTER TABLE `security_role_group_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_user`
--

DROP TABLE IF EXISTS `security_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_user` (
  `USER_ID` varchar(36) NOT NULL,
  `USER_CODE` varchar(32) DEFAULT NULL,
  `USER_NAME` varchar(32) DEFAULT NULL,
  `USER_PWD` varchar(32) DEFAULT NULL,
  `USER_SEX` varchar(1) DEFAULT NULL,
  `USER_DESC` varchar(128) DEFAULT NULL,
  `USER_STATE` varchar(32) DEFAULT NULL,
  `USER_SORT` int(11) DEFAULT NULL,
  `USER_MAIL` varchar(64) DEFAULT NULL,
  `USER_PHONE` varchar(64) DEFAULT NULL,
  `USER_WX_OPENID` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  KEY `index_query` (`USER_CODE`,`USER_NAME`,`USER_WX_OPENID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_user`
--

LOCK TABLES `security_user` WRITE;
/*!40000 ALTER TABLE `security_user` DISABLE KEYS */;
INSERT INTO `security_user` VALUES ('7DE6ED51-3F4B-4BE6-84A6-17BC6186CC24','admin','管理员','8762EB814817CC8DCBB3FB5C5FCD52E0','M','内置账户，勿删！！','1',1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `security_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_user_auth`
--

DROP TABLE IF EXISTS `security_user_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_user_auth` (
  `USER_AUTH_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(36) DEFAULT NULL,
  `RES_TYPE` varchar(32) DEFAULT NULL,
  `RES_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`USER_AUTH_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_user_auth`
--

LOCK TABLES `security_user_auth` WRITE;
/*!40000 ALTER TABLE `security_user_auth` DISABLE KEYS */;
/*!40000 ALTER TABLE `security_user_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_user_group_rel`
--

DROP TABLE IF EXISTS `security_user_group_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_user_group_rel` (
  `GRP_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`GRP_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_user_group_rel`
--

LOCK TABLES `security_user_group_rel` WRITE;
/*!40000 ALTER TABLE `security_user_group_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `security_user_group_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_user_role_rel`
--

DROP TABLE IF EXISTS `security_user_role_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_user_role_rel` (
  `ROLE_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_user_role_rel`
--

LOCK TABLES `security_user_role_rel` WRITE;
/*!40000 ALTER TABLE `security_user_role_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `security_user_role_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_codelist`
--

DROP TABLE IF EXISTS `sys_codelist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_codelist` (
  `TYPE_ID` varchar(32) NOT NULL,
  `CODE_ID` varchar(32) NOT NULL,
  `CODE_NAME` varchar(32) DEFAULT NULL,
  `CODE_DESC` varchar(128) DEFAULT NULL,
  `CODE_SORT` int(11) DEFAULT NULL,
  `CODE_FLAG` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`TYPE_ID`,`CODE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_codelist`
--

LOCK TABLES `sys_codelist` WRITE;
/*!40000 ALTER TABLE `sys_codelist` DISABLE KEYS */;
INSERT INTO `sys_codelist` VALUES ('APP_RESULT','NO','不同意','',2,'1'),('APP_RESULT','YES','同意','',1,'1'),('AuthedHandlerId','Bottom','BottomHandler','',1,'1'),('AuthedHandlerId','Building','BuildingHandler','',1,'1'),('AuthedHandlerId','Homepage','HomepageHandler','',1,'1'),('AuthedHandlerId','Logo','LogoHandler','',1,'1'),('AuthedHandlerId','MainWin','MainWinHandler','',1,'1'),('AuthedHandlerId','MenuTree','MenuTreeHandler','',1,'1'),('AuthedHandlerId','Navigater','NavigaterHandler','',1,'1'),('BOOL_DEFINE','N','否','',2,'1'),('BOOL_DEFINE','Y','是','',1,'1'),('CLUE_SOURCE','0','邮件推广','',1,'1'),('CLUE_SOURCE','1','电话推广','',2,'1'),('CLUE_SOURCE','2','网上搜索','',3,'1'),('CLUE_SOURCE','3','朋友介绍','',4,'1'),('CLUE_SOURCE','4','线下拜访','',5,'1'),('CLUE_SOURCE','5','其他','',6,'1'),('CLUE_STATE','0','初始化','',1,'1'),('CLUE_STATE','1','已分配','',2,'1'),('CLUE_STATE','2','已认领','',3,'1'),('CLUE_STATE','3','已搁置','',4,'1'),('CLUE_STATE','4','已关闭','',5,'1'),('CODE_TYPE_GROUP','app_code_define','应用编码','null',1,'1'),('CODE_TYPE_GROUP','sys_code_define','系统编码','系统编码123a1',3,'1'),('CUST_INDUSTRY','0','计算机/互联网/通信/电子','',1,'1'),('CUST_INDUSTRY','1','会计/金融/银行/保险','',2,'1'),('CUST_INDUSTRY','10','政府/非赢利机构/其他','',12,'1'),('CUST_INDUSTRY','11','餐饮/娱乐','',11,'1'),('CUST_INDUSTRY','2','贸易/消费/制造/营运','',3,'1'),('CUST_INDUSTRY','3','制药/医疗','',4,'1'),('CUST_INDUSTRY','4','广告/媒体','',5,'1'),('CUST_INDUSTRY','5','房地产/建筑','',6,'1'),('CUST_INDUSTRY','6','专业服务/教育/培训','',7,'1'),('CUST_INDUSTRY','7','服务业','',8,'1'),('CUST_INDUSTRY','8','物流/运输','',9,'1'),('CUST_INDUSTRY','9','能源/原材料','',10,'1'),('CUST_LEVEL','KEY','重点','',1,'1'),('CUST_LEVEL','NON_PRIORITY','非优先','',3,'1'),('CUST_LEVEL','ORDINARY','普通','',2,'1'),('CUST_NATURE','0','未知','',1,'1'),('CUST_NATURE','1','外资(欧美)','',2,'1'),('CUST_NATURE','10','非营利机构','',11,'1'),('CUST_NATURE','11','其他性质','',12,'1'),('CUST_NATURE','2','外资(非欧美)','',3,'1'),('CUST_NATURE','3','合资','',4,'1'),('CUST_NATURE','4','国企','',5,'1'),('CUST_NATURE','5','民营公司','',6,'1'),('CUST_NATURE','6','国内上市公司','',7,'1'),('CUST_NATURE','7','外企代表处','',8,'1'),('CUST_NATURE','8','政府机关','',9,'1'),('CUST_NATURE','9','事业单位','',10,'1'),('CUST_PROGRESS_STATE','BUSINESS_NEGOTIATION','商务洽谈','',3,'1'),('CUST_PROGRESS_STATE','DETERMINED_INTENTION','确定意向','',2,'1'),('CUST_PROGRESS_STATE','PRELIMINARY','初步进展','',1,'1'),('CUST_PROGRESS_STATE','SIGNING_DEAL','签约成交','',4,'1'),('CUST_PROGRESS_STATE','STAGNANT_LOSS','停滞流失','',5,'1'),('CUST_SCALE','1000-5000','1000-5000人','',5,'1'),('CUST_SCALE','10000','10000人以上','',7,'1'),('CUST_SCALE','150-500','150-500人','',3,'1'),('CUST_SCALE','50','少于50人','',1,'1'),('CUST_SCALE','50-150','50-150人','',2,'1'),('CUST_SCALE','500-1000','500-1000人','',4,'1'),('CUST_SCALE','5000-10000','5000-10000人','',6,'1'),('CUST_STATE','Confirm','确认','',3,'1'),('CUST_STATE','init','初始化','',1,'1'),('CUST_STATE','Postpone','暂缓','',4,'1'),('CUST_STATE','Submit','提交','',2,'1'),('CUST_VISIT_CATEGORY','FOLLOW_CUST','跟进客户','',1,'1'),('CUST_VISIT_CATEGORY','PRO_CUST','潜在客户','',1,'1'),('EMP_EDUCATION','0','初中','',1,'1'),('EMP_EDUCATION','1','高中','',2,'1'),('EMP_EDUCATION','2','中技','',3,'1'),('EMP_EDUCATION','3','中专','',4,'1'),('EMP_EDUCATION','4','大专','',5,'1'),('EMP_EDUCATION','5','本科','',6,'1'),('EMP_EDUCATION','6','MBA','',7,'1'),('EMP_EDUCATION','7','硕士','',8,'1'),('EMP_EDUCATION','8','博士','',9,'1'),('EMP_EDUCATION','9','其他','',10,'1'),('EMP_MARITAL_STATUS','0','未婚','',1,'1'),('EMP_MARITAL_STATUS','1','已婚','',2,'1'),('EMP_MARITAL_STATUS','2','离异','',3,'1'),('EMP_PARTY','0','群众','',1,'1'),('EMP_PARTY','1','党员','',2,'1'),('EMP_STATE','approved','已核准','',2,'1'),('EMP_STATE','drafe','草稿','',1,'1'),('EXPE_TRANSPORTATION_WAY','0','汽车','',1,'1'),('EXPE_TRANSPORTATION_WAY','1','火车','',2,'1'),('EXPE_TRANSPORTATION_WAY','2','轮船','',3,'1'),('EXPE_TRANSPORTATION_WAY','3','飞机','',4,'1'),('FUNCTION_TYPE','funcmenu','功能菜单','',1,'1'),('FUNCTION_TYPE','funcnode','功能节点','',1,'1'),('GRP_STATE','0','无效','',1,'1'),('GRP_STATE','1','有效','',2,'1'),('HANDLER_TYPE','MAIN','主处理器','',1,'1'),('HANDLER_TYPE','OTHER','其他处理器','',2,'1'),('LEA_TYPE','0','病假','',1,'1'),('LEA_TYPE','1','事假','',2,'1'),('LEA_TYPE','2','年假','',3,'1'),('MENUTREE_CASCADE','0','关闭','',1,'1'),('MENUTREE_CASCADE','1','展开','',2,'1'),('OPER_CTR_TYPE','disableMode','不能操作','',2,'1'),('OPER_CTR_TYPE','hiddenMode','隐藏按钮','',1,'1'),('OPP_CONCERN_PRODUCT','infoKnow','信息知识门户','',5,'1'),('OPP_CONCERN_PRODUCT','interDataCenter','综合数据中心','',6,'1'),('OPP_CONCERN_PRODUCT','mobileOffice','移动办公平台','',3,'1'),('OPP_CONCERN_PRODUCT','processIntegration','业务流程集成','',1,'1'),('OPP_CONCERN_PRODUCT','SOA','SOA综合集成','',4,'1'),('OPP_CONCERN_PRODUCT','systemDocking','异构系统对接','',0,'1'),('OPP_LEVEL','INTERMEDIATE','中','',2,'1'),('OPP_LEVEL','STRONG','强','',3,'1'),('OPP_LEVEL','WEAK','弱','',1,'1'),('OPP_STATE','0','初始化','',1,'1'),('OPP_STATE','1','已提交','',2,'1'),('OPP_STATE','2','已确认','',3,'1'),('OPP_STATE','3','已关闭','',4,'1'),('ORDER_STATE','0','初始化','',1,'1'),('ORDER_STATE','1','已确认','',3,'1'),('ORG_CLASSIFICATION','ENTERPRISE_ENTITY','企业实体','',1,'1'),('ORG_CLASSIFICATION','SOFTWARE_AGENTS','软件代理商','',3,'1'),('ORG_CLASSIFICATION','SOFTWARE_DEVELOPERS','软件开发商','',2,'1'),('ORG_CLASSIFICATION','SYSTEM_INTEGRATOR','系统集成商','',4,'1'),('ORG_CLASSIFICATION','UNKNOWN','未知','',5,'1'),('ORG_LABELS','1','无人接听','',1,'1'),('ORG_LABELS','2','号码信息不符','',2,'1'),('ORG_LABELS','3','空号','',3,'1'),('ORG_LABELS','4','暂停服务','',4,'1'),('ORG_LABELS','5','暂无需求','',5,'1'),('ORG_LABELS','6','后续联系','',6,'1'),('ORG_LABELS','7','态度恶劣','',7,'1'),('ORG_SOURCES','INFO_ACQUIRE','信息采集','',2,'1'),('ORG_SOURCES','RECRUIT_ACQUIRE','招聘采集','',3,'1'),('ORG_SOURCES','WEB_SEARCH','网络搜索','',1,'1'),('ORG_TYPE','0','未知','',1,'1'),('ORG_TYPE','1','外资（欧美）','',2,'1'),('ORG_TYPE','10','非营利机构','',11,'1'),('ORG_TYPE','11','其他性质','',12,'1'),('ORG_TYPE','2','外资（非欧美）','',3,'1'),('ORG_TYPE','3','合资','',4,'1'),('ORG_TYPE','4','国企','',5,'1'),('ORG_TYPE','5','民营公司','',6,'1'),('ORG_TYPE','6','国内上市公司','',7,'1'),('ORG_TYPE','7','外企代表处','',8,'1'),('ORG_TYPE','8','政府机关','',9,'1'),('ORG_TYPE','9','事业单位','',10,'1'),('PER_SEX','F','女','',3,'1'),('PER_SEX','M','男','',2,'1'),('PER_SEX','UNKONW','未知','',1,'1'),('PER_STATE','0','有效','',1,'1'),('PER_STATE','1','无效','',2,'1'),('POSITION_TYPE','dummy_postion','虚拟岗位','null',1,'1'),('POSITION_TYPE','real_postion','实际岗位','null',1,'1'),('PROVINCE','AnHui','安徽','',13,'1'),('PROVINCE','AoMen','澳门','',33,'1'),('PROVINCE','BeiJing','北京','',1,'1'),('PROVINCE','ChongQing','重庆','',4,'1'),('PROVINCE','FuJian','福建','',14,'1'),('PROVINCE','GanSu','甘肃','',28,'1'),('PROVINCE','GuangDong','广东','',20,'1'),('PROVINCE','GuangXi','广西','',21,'1'),('PROVINCE','GuiZhou','贵州','',24,'1'),('PROVINCE','HaiNan','海南','',22,'1'),('PROVINCE','HeBei','河北','',5,'1'),('PROVINCE','HeiLongJiang','黑龙江','',10,'1'),('PROVINCE','HeNan','河南','',17,'1'),('PROVINCE','HuBei','湖北','',18,'1'),('PROVINCE','HuNan','湖南','',19,'1'),('PROVINCE','JiangSu','江苏','',11,'1'),('PROVINCE','JiangXi','江西','',15,'1'),('PROVINCE','JiLin','吉林','',9,'1'),('PROVINCE','LiaoNing','辽宁','',8,'1'),('PROVINCE','NeiMengGu','内蒙古','',7,'1'),('PROVINCE','NingXia','宁夏','',30,'1'),('PROVINCE','QingHai','青海','',29,'1'),('PROVINCE','SanXi','陕西','',27,'1'),('PROVINCE','ShanDong','山东','',16,'1'),('PROVINCE','ShangHai','上海','',2,'1'),('PROVINCE','ShanXi','山西','',6,'1'),('PROVINCE','SiChuan','四川','',23,'1'),('PROVINCE','TaiWan','台湾','',34,'1'),('PROVINCE','TianJin','天津','',3,'1'),('PROVINCE','XiangGang','香港','',32,'1'),('PROVINCE','XinJiang','新疆','',31,'1'),('PROVINCE','XiZang','西藏','',26,'1'),('PROVINCE','YunNan','云南','',25,'1'),('PROVINCE','ZheJiang','浙江','',12,'1'),('RES_TYPE','IMAGE','图片文件','',2,'1'),('RES_TYPE','ISO','镜像文件','',1,'1'),('RES_TYPE','VIDEO','视频文件','',3,'1'),('SAL_OVERTIME_CALC','周末','20','',2,'1'),('SAL_OVERTIME_CALC','普通','15','',1,'1'),('SAL_OVERTIME_CALC','节假日','25','',3,'1'),('SAL_STATE','0','初始化','',1,'1'),('SAL_STATE','1','已核准','',2,'1'),('STATE','approved','已核准','',3,'1'),('STATE','drafe','草稿','',1,'1'),('STATE','submitted','已提交','',2,'1'),('SYS_VALID_TYPE','0','无效','null',2,'1'),('SYS_VALID_TYPE','1','有效','null',1,'1'),('TASK_CLASS','ColdCalls','陌生拜访','',1,'1'),('TASK_CLASS','FollowUp','意向跟进','',2,'1'),('TASK_FOLLOW_STATUS','HaveFollowUp','已跟进','',2,'1'),('TASK_FOLLOW_STATUS','NoFollowUp','未跟进','',1,'1'),('TASK_REVIEW_STATE','ConfirmPlan','确认计划','',3,'1'),('TASK_REVIEW_STATE','ConfirmSummary','确认总结','',5,'1'),('TASK_REVIEW_STATE','Init','初始化','',1,'1'),('TASK_REVIEW_STATE','SubmitPlan','提交计划','',2,'1'),('TASK_REVIEW_STATE','SubmitSummary','提交总结','',4,'1'),('UNIT_TYPE','dept','部门','',10,'1'),('UNIT_TYPE','org','机构','',20,'1'),('UNIT_TYPE','post','岗位','',30,'1'),('USER_SEX','F','女','',2,'1'),('USER_SEX','M','男','null',1,'1'),('VISIT_EFFECT','Follow_up','可跟进','',3,'1'),('VISIT_EFFECT','Have_intention','有意向','',1,'1'),('VISIT_EFFECT','Unwanted','不需要','',2,'1'),('VISIT_TYPE','0','预约拜访','',1,'1'),('VISIT_TYPE','1','电话拜访','',3,'1'),('VISIT_TYPE','2','邮件拜访','',4,'1'),('VISIT_TYPE','3','陌生拜访','',2,'1'),('WOT_TIME','0.5','0.5','',1,'1'),('WOT_TIME','1.0','1.0','',2,'1'),('WOT_TIME','1.5','1.5','',3,'1'),('WOT_TIME','2.0','2.0','',4,'1'),('WOT_TIME','2.5','2.5','',5,'1'),('WOT_TIME','3.0','3.0','',6,'1'),('WOT_TIME','3.5','3.5','',7,'1'),('WOT_TIME','4.0','4.0','',8,'1'),('WOT_TIME','4.5','4.5','',9,'1'),('WOT_TIME','5.0','5.0','',10,'1'),('WOT_TIME_COMPANY','day','天','',2,'1'),('WOT_TIME_COMPANY','hour','小时','',1,'1');
/*!40000 ALTER TABLE `sys_codelist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_codetype`
--

DROP TABLE IF EXISTS `sys_codetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_codetype` (
  `TYPE_ID` varchar(32) NOT NULL,
  `TYPE_NAME` varchar(32) DEFAULT NULL,
  `TYPE_GROUP` varchar(32) DEFAULT NULL,
  `TYPE_DESC` varchar(128) DEFAULT NULL,
  `IS_CACHED` char(1) DEFAULT NULL,
  `IS_UNITEADMIN` char(1) DEFAULT NULL,
  `IS_EDITABLE` char(1) DEFAULT NULL,
  `LEGNTT_LIMIT` varchar(6) DEFAULT NULL,
  `CHARACTER_LIMIT` char(1) DEFAULT NULL,
  `EXTEND_SQL` char(1) DEFAULT NULL,
  `SQL_BODY` varchar(512) DEFAULT NULL,
  `SQL_COND` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_codetype`
--

LOCK TABLES `sys_codetype` WRITE;
/*!40000 ALTER TABLE `sys_codetype` DISABLE KEYS */;
INSERT INTO `sys_codetype` VALUES ('APP_RESULT','核准結果','sys_code_define','','Y','Y','Y','10','',NULL,NULL,NULL),('BOOL_DEFINE','布尔定义','sys_code_define','','Y','Y','Y','1','C','','',''),('CODE_TYPE_GROUP','编码类型分组','app_code_define','编码类型分组',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('EMP_EDUCATION','学历','sys_code_define',NULL,'Y','Y','Y','',NULL,NULL,NULL,NULL),('EMP_MARITAL_STATUS','婚姻状况','sys_code_define',NULL,'Y','Y','Y','',NULL,NULL,NULL,NULL),('EMP_PARTY','政治面貌','sys_code_define',NULL,'Y','Y','Y','',NULL,NULL,NULL,NULL),('EMP_STATE','基本信息状态','sys_code_define',NULL,'Y','Y','Y','',NULL,NULL,NULL,NULL),('EXPE_TRANSPORTATION_WAY','交通方式','sys_code_define',NULL,'Y','Y','Y','',NULL,NULL,NULL,NULL),('FUNCTION_TYPE','功能类型','sys_code_define',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('HANDLER_TYPE','控制器类型','sys_code_define','','N','Y','Y','32','C',NULL,NULL,NULL),('LEA_TYPE','请假类型','sys_code_define',NULL,'Y','Y','Y','',NULL,NULL,NULL,NULL),('MENUTREE_CASCADE','是否展开','sys_code_define','','Y','Y','Y','1','N','N','',''),('OPER_CTR_TYPE','操作控制类型','sys_code_define',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('POSITION_TYPE','岗位类型','app_code_define',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('RES_TYPE','资源类型','sys_code_define','','N','Y','Y','16','C',NULL,NULL,NULL),('SAL_OVERTIME_CALC','加班费','app_code_define','','Y','Y','Y','20','','N','',''),('SAL_STATE','薪资状态','sys_code_define',NULL,'Y','Y','Y','',NULL,NULL,NULL,NULL),('STATE','状态','sys_code_define',NULL,'Y','Y','Y','',NULL,NULL,NULL,NULL),('SYS_VALID_TYPE','有效标识符','app_code_define',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('UNIT_TYPE','单位类型','app_code_define',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('USER_SEX','性别类型','sys_code_define','','N','Y','Y','16','C',NULL,NULL,NULL),('WOT_TIME','加班时间','sys_code_define',NULL,'Y','Y','Y','',NULL,NULL,NULL,NULL),('WOT_TIME_COMPANY','加班时长单位','sys_code_define','','Y','Y','Y','20','','N','','');
/*!40000 ALTER TABLE `sys_codetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_function`
--

DROP TABLE IF EXISTS `sys_function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_function` (
  `FUNC_ID` varchar(36) NOT NULL,
  `FUNC_NAME` varchar(64) DEFAULT NULL,
  `FUNC_TYPE` varchar(32) DEFAULT NULL,
  `MAIN_HANDLER` varchar(36) DEFAULT NULL,
  `FUNC_PID` varchar(36) DEFAULT NULL,
  `FUNC_STATE` char(1) DEFAULT NULL,
  `FUNC_SORT` int(11) DEFAULT NULL,
  `FUNC_DESC` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`FUNC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_function`
--

LOCK TABLES `sys_function` WRITE;
/*!40000 ALTER TABLE `sys_function` DISABLE KEYS */;
INSERT INTO `sys_function` VALUES ('00000000-0000-0000-00000000000000000','人力资源系统','funcmenu',NULL,NULL,'1',NULL,NULL),('00000000-0000-0000-00000000000000001','系统管理','funcmenu','','00000000-0000-0000-00000000000000000','1',100,''),('49F49293-5270-4C72-B7EE-02562627BF03','基本信息','funcnode','4F7D78BE-CEF2-4B71-9F43-EEF3ABADC5A1','AD7CE5A0-39C4-4B43-B243-D056BBDF9332','1',1,''),('4AFE24C0-D10F-4B1C-BC11-4ADCBBA72689','离职管理','funcnode','A747DD63-6189-474A-B8EA-D264536C7E7F','AD7CE5A0-39C4-4B43-B243-D056BBDF9332','1',9,''),('5FDEE3AB-6D32-4C5F-AD65-EF1EE5FFBAE6','附件管理','funcnode','1F617665-FC8B-4E8C-ABE4-540C363A17A8','00000000-0000-0000-00000000000000001','1',7,''),('67BA273A-DD31-48D0-B78C-1D60D5316074','系统日志','funcnode','494DF09B-7573-4CCA-85C1-97F4DC58C86B','00000000-0000-0000-00000000000000001','1',6,NULL),('692B0D37-2E66-4E82-92B4-E59BCF76EE76','编码管理','funcnode','B4FE5722-9EA6-47D8-8770-D999A3F6A354','00000000-0000-0000-00000000000000001','1',5,NULL),('697BF588-ED62-43BB-B30E-3A7E40E7F1F1','薪资管理','funcnode','0248868D-BBBE-4F96-B7C5-FAF4E1556570','AD7CE5A0-39C4-4B43-B243-D056BBDF9332','1',6,''),('6FDE7748-A30D-4386-999E-600B79D472D5','定位查看','funcnode','34BD49C5-0BA6-4DAC-A96C-B2B426127C28','AD7CE5A0-39C4-4B43-B243-D056BBDF9332','1',10,''),('6FDE7748-A30D-4386-999E-600B79D472D6','晋升调薪','funcnode','34BD49C5-0BA6-4DAC-A96C-B2B426127C29','AD7CE5A0-39C4-4B43-B243-D056BBDF9332','1',7,''),('8C84B439-2788-4608-89C4-8F5AA076D124','组织机构','funcnode','439949F0-C6B7-49FF-8ED1-2A1B5062E7B9','00000000-0000-0000-00000000000000001','1',1,NULL),('99EAA89A-59E6-4866-9980-21164DA571F2','考勤管理','funcnode','67CF818A-7AB6-43CB-BDD5-A57B5FC2C512','AD7CE5A0-39C4-4B43-B243-D056BBDF9332','1',2,''),('A0334956-426E-4E49-831B-EB00E37285FD','编码类型','funcnode','9A16D554-F989-438A-B92D-C8C8AC6BF9B8','00000000-0000-0000-00000000000000001','1',4,NULL),('AD7CE5A0-39C4-4B43-B243-D056BBDF9332','业务功能','funcmenu','','00000000-0000-0000-00000000000000000','1',99,''),('B29FEBDB-F5D9-41AE-8C2D-451A00B2C51F','请假申请','funcnode','642BCD38-28C1-4FF1-A194-535BFAB10679','AD7CE5A0-39C4-4B43-B243-D056BBDF9332','1',4,''),('C977BC31-C78F-4B16-B0C6-769783E46A06','功能管理','funcnode','46C52D33-8797-4251-951F-F7CA23C76BD7','00000000-0000-0000-00000000000000001','1',3,NULL),('D3582A2A-3173-4F92-B1AD-2F999A2CBE18','修改密码','funcnode','88882DB9-967F-430E-BA9C-D0BBBBD2BD0C','00000000-0000-0000-00000000000000001','1',8,''),('DFE8BE4C-4024-4A7B-8DF2-630003832AE9','角色管理','funcnode','0CE03AD4-FF29-4FDB-8FEE-DA8AA38B649F','00000000-0000-0000-00000000000000001','1',2,NULL),('F8C4DC47-8F58-4420-8FC4-4DB1CE35C0F6','加班申请','funcnode','7782AD69-8FDC-4F5D-8510-36FD8D3BF249','AD7CE5A0-39C4-4B43-B243-D056BBDF9332','1',3,'');
/*!40000 ALTER TABLE `sys_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_handler`
--

DROP TABLE IF EXISTS `sys_handler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_handler` (
  `HANLER_ID` varchar(36) NOT NULL,
  `HANLER_CODE` varchar(64) DEFAULT NULL,
  `HANLER_TYPE` varchar(32) DEFAULT NULL,
  `HANLER_URL` varchar(128) DEFAULT NULL,
  `FUNC_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`HANLER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_handler`
--

LOCK TABLES `sys_handler` WRITE;
/*!40000 ALTER TABLE `sys_handler` DISABLE KEYS */;
INSERT INTO `sys_handler` VALUES ('0248868D-BBBE-4F96-B7C5-FAF4E1556570','HrSalaryManageList','MAIN',NULL,'697BF588-ED62-43BB-B30E-3A7E40E7F1F1'),('05CBEDD2-E00C-4383-B5BA-F117BAB217E5','HrEducationEditBox','OTHER','','49F49293-5270-4C72-B7EE-02562627BF03'),('0CE03AD4-FF29-4FDB-8FEE-DA8AA38B649F','SecurityRoleTreeManage','MAIN','','DFE8BE4C-4024-4A7B-8DF2-630003832AE9'),('145920A1-3843-419E-A633-5C3AC2AFC454','HrSalaryManageEdit','OTHER','','697BF588-ED62-43BB-B30E-3A7E40E7F1F1'),('1F617665-FC8B-4E8C-ABE4-540C363A17A8','WcmGeneralGroup8ContentList','MAIN',NULL,'5FDEE3AB-6D32-4C5F-AD65-EF1EE5FFBAE6'),('27BCFFDE-DD30-4659-8EAB-D0D848A39DB8','DeptTreeSelect','OTHER','','49F49293-5270-4C72-B7EE-02562627BF03'),('34BD49C5-0BA6-4DAC-A96C-B2B426127C28','HrLocationManageList','MAIN',NULL,'6FDE7748-A30D-4386-999E-600B79D472D5'),('34BD49C5-0BA6-4DAC-A96C-B2B426127C29','HrSalRecordManageList','MAIN',NULL,'6FDE7748-A30D-4386-999E-600B79D472D6'),('3844F972-311A-459A-8B9A-B39DFA526B6C','HrValidDays','OTHER','','697BF588-ED62-43BB-B30E-3A7E40E7F1F1'),('439949F0-C6B7-49FF-8ED1-2A1B5062E7B9','SecurityGroupList','MAIN','','8C84B439-2788-4608-89C4-8F5AA076D124'),('46C52D33-8797-4251-951F-F7CA23C76BD7','FunctionTreeManage','MAIN',NULL,'C977BC31-C78F-4B16-B0C6-769783E46A06'),('4878413D-5A54-464A-8ECE-59E78D43F2D1','HrLeaveManageEdit','OTHER','','B29FEBDB-F5D9-41AE-8C2D-451A00B2C51F'),('494DF09B-7573-4CCA-85C1-97F4DC58C86B','SysLogQueryList','MAIN',NULL,'67BA273A-DD31-48D0-B78C-1D60D5316074'),('4F0BC221-A2D0-4500-907A-EC116B48AC74','HrWorkOvertimeManageEdit','OTHER','','F8C4DC47-8F58-4420-8FC4-4DB1CE35C0F6'),('4F7D78BE-CEF2-4B71-9F43-EEF3ABADC5A1','HrEmployeeManageList','MAIN',NULL,'49F49293-5270-4C72-B7EE-02562627BF03'),('62EA8181-C838-4E46-9A2E-82E94D866EFF','UserListSelectList','OTHER','','49F49293-5270-4C72-B7EE-02562627BF03'),('642BCD38-28C1-4FF1-A194-535BFAB10679','HrLeaveManageList','MAIN',NULL,'B29FEBDB-F5D9-41AE-8C2D-451A00B2C51F'),('67CF818A-7AB6-43CB-BDD5-A57B5FC2C512','HrAttendanceManageList','MAIN',NULL,'99EAA89A-59E6-4866-9980-21164DA571F2'),('7782AD69-8FDC-4F5D-8510-36FD8D3BF249','HrWorkOvertimeManageList','MAIN',NULL,'F8C4DC47-8F58-4420-8FC4-4DB1CE35C0F6'),('7D03AFCE-974A-45B8-B163-34002C7CE07B','HrExperienceEditBox','OTHER','','49F49293-5270-4C72-B7EE-02562627BF03'),('86C0B8E5-92FE-4FB7-ACBF-1C63243B5D30','HrWorkPerformanceEditBox','OTHER','','49F49293-5270-4C72-B7EE-02562627BF03'),('88882DB9-967F-430E-BA9C-D0BBBBD2BD0C','ModifyPassword','MAIN',NULL,'D3582A2A-3173-4F92-B1AD-2F999A2CBE18'),('89E26739-44ED-4BF5-9CA1-D950052F07A9','UserListSelectList','OTHER','','B29FEBDB-F5D9-41AE-8C2D-451A00B2C51F'),('98733F5B-A599-41A9-B135-33F480BDE062','HrEmployeeManageEdit','OTHER','','49F49293-5270-4C72-B7EE-02562627BF03'),('9A16D554-F989-438A-B92D-C8C8AC6BF9B8','CodeTypeManageList','MAIN',NULL,'A0334956-426E-4E49-831B-EB00E37285FD'),('A2E1E8E7-EAC9-45F7-AAEE-D08C89BDEE0A','UserListSelectList','OTHER','','F8C4DC47-8F58-4420-8FC4-4DB1CE35C0F6'),('A747DD63-6189-474A-B8EA-D264536C7E7F','HrLeaveRecordManageList','MAIN','','4AFE24C0-D10F-4B1C-BC11-4ADCBBA72689'),('A9846F89-E5FA-4820-9913-0CF1BB71F4D1','HrAttendanceManageEdit','OTHER','','99EAA89A-59E6-4866-9980-21164DA571F2'),('B4FE5722-9EA6-47D8-8770-D999A3F6A354','CodeListManageList','MAIN',NULL,'692B0D37-2E66-4E82-92B4-E59BCF76EE76');
/*!40000 ALTER TABLE `sys_handler` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_onlinecount`
--

DROP TABLE IF EXISTS `sys_onlinecount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_onlinecount` (
  `IPADDRRESS` varchar(64) NOT NULL,
  `ONLINECOUNT` int(11) DEFAULT NULL,
  PRIMARY KEY (`IPADDRRESS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_onlinecount`
--

LOCK TABLES `sys_onlinecount` WRITE;
/*!40000 ALTER TABLE `sys_onlinecount` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_onlinecount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_operation`
--

DROP TABLE IF EXISTS `sys_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_operation` (
  `OPER_ID` char(36) NOT NULL,
  `HANLER_ID` varchar(36) DEFAULT NULL,
  `OPER_CODE` varchar(64) DEFAULT NULL,
  `OPER_NAME` varchar(64) DEFAULT NULL,
  `OPER_ACTIONTPYE` varchar(64) DEFAULT NULL,
  `OPER_SORT` int(11) DEFAULT NULL,
  PRIMARY KEY (`OPER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_operation`
--

LOCK TABLES `sys_operation` WRITE;
/*!40000 ALTER TABLE `sys_operation` DISABLE KEYS */;
INSERT INTO `sys_operation` VALUES ('022E5A78-2D0B-4C27-8C92-512FE6C89636','4F0BC221-A2D0-4500-907A-EC116B48AC74','save','保存','save',1),('04925782-D1DD-4799-874D-DA89C09C5E93','4F7D78BE-CEF2-4B71-9F43-EEF3ABADC5A1','edit','编辑','edit',2),('04D84DDC-B3C1-45F2-B1C4-C9CD986A6EC7','67CF818A-7AB6-43CB-BDD5-A57B5FC2C512','signOut','签退','signOut',2),('0F283527-C213-4E9F-BAA2-59F0E38CED48','642BCD38-28C1-4FF1-A194-535BFAB10679','detail','查看','detail',4),('104EB1B4-AF84-42B7-9649-EEDE8726F17A','4F0BC221-A2D0-4500-907A-EC116B48AC74','resubmit','反提交','resubmit',3),('1C1A3EA8-FC9C-4856-B372-0DC6D394512E','7782AD69-8FDC-4F5D-8510-36FD8D3BF249','edit','编辑','edit',2),('22CF4305-08D1-476F-B17D-556F36AE58BF','98733F5B-A599-41A9-B135-33F480BDE062','revokeApproval','反核准','revokeApproval',4),('270A2E3A-8649-46A8-9B95-EB022090D78D','4F0BC221-A2D0-4500-907A-EC116B48AC74','submit','提交','submit',2),('2734D16B-B147-4CA6-8E0C-09BB2B996B9C','86C0B8E5-92FE-4FB7-ACBF-1C63243B5D30','close','关闭','close',3),('2AEDB46D-6764-4967-9536-692B968CD46B','27BCFFDE-DD30-4659-8EAB-D0D848A39DB8','confirm','确认','confirm',1),('302FF309-B104-48B0-810B-2E8F0709F2B5','7782AD69-8FDC-4F5D-8510-36FD8D3BF249','revokeApproval','反核准','revokeApproval',4),('35FE49D8-CB7D-415F-9A86-FF5C04FEFFC5','7D03AFCE-974A-45B8-B163-34002C7CE07B','save','保存','save',2),('3C2BBF3E-FF53-4913-8688-6158B5418812','7D03AFCE-974A-45B8-B163-34002C7CE07B','close','关闭','close',3),('3DB35A08-1211-4FDB-A3F2-CF31515F0366','7782AD69-8FDC-4F5D-8510-36FD8D3BF249','delete','删除','delete',6),('3EC41FB8-ED98-41F3-B86D-F7473AEDD002','494DF09B-7573-4CCA-85C1-97F4DC58C86B','viewDetail','查看','viewDetail',1),('4233DC59-89E9-450B-A72A-F58A4711BAFF','145920A1-3843-419E-A633-5C3AC2AFC454','edit','编辑','edit',1),('42F79115-9E98-4F60-90FC-5468996C9496','4F7D78BE-CEF2-4B71-9F43-EEF3ABADC5A1','detail','查看','detail',6),('4330D785-FF0D-474D-AE29-D759B4BD8DF2','4F7D78BE-CEF2-4B71-9F43-EEF3ABADC5A1','revokeApproval','反核准','revokeApproval',5),('43A4596F-D7D6-4F25-AED4-C88896161FC1','98733F5B-A599-41A9-B135-33F480BDE062','back','返回','back',5),('460573D2-CB7C-4C2B-A43C-D7E08017A9C7','05CBEDD2-E00C-4383-B5BA-F117BAB217E5','save','保存','save',2),('48D4510E-8B9C-4869-801E-0481B79B3BCD','4F0BC221-A2D0-4500-907A-EC116B48AC74','revokeApproval','反核准','revokeApproval',5),('49B5A311-DBF1-41D7-ADCC-4731912E314B','145920A1-3843-419E-A633-5C3AC2AFC454','revokeApproval','反核准','revokeApproval',4),('4C025775-4C79-4B88-9D52-DC35DF77D3D2','4878413D-5A54-464A-8ECE-59E78D43F2D1','resubmit','反提交','resubmit',3),('5501533A-5F00-490D-9559-E2AFF4DB7EE0','98733F5B-A599-41A9-B135-33F480BDE062','save','保存','save',2),('57E4949A-6801-4F14-82FE-36D92E26BACF','0248868D-BBBE-4F96-B7C5-FAF4E1556570','revokeApproval','反核准','revokeApproval',4),('5DA79901-A8BF-4650-A46A-F4440F7DAEF9','7782AD69-8FDC-4F5D-8510-36FD8D3BF249','approve','核准','approve',3),('5F9CCA75-94A7-43D0-A0F1-5E36869F75B0','0248868D-BBBE-4F96-B7C5-FAF4E1556570','approve','核准','approve',3),('60AB9AE6-D835-4B6E-99FF-EF10148F2884','A2E1E8E7-EAC9-45F7-AAEE-D08C89BDEE0A','choice','选择','choice',1),('65F0D7A5-A5A4-4E43-A276-3A002A334DE1','642BCD38-28C1-4FF1-A194-535BFAB10679','approve','核准','approve',3),('69C9DD89-FD32-40E7-BE7A-FBC979071558','0248868D-BBBE-4F96-B7C5-FAF4E1556570','delete','删除','delete',6),('6BCA573E-A0C2-43BD-961C-BEE957DB2BA7','3844F972-311A-459A-8B9A-B39DFA526B6C','save','保存','save',1),('6BF7A157-D333-4F40-8612-F61D3FD4D258','494DF09B-7573-4CCA-85C1-97F4DC58C86B','refreshImgBtn','刷新','refresh',2),('6EEDC6CB-123D-4311-BD86-956C821D54E2','642BCD38-28C1-4FF1-A194-535BFAB10679','delete','删除','delete',5),('7295E472-0ABB-48F1-A696-68E8F8835CF8','4878413D-5A54-464A-8ECE-59E78D43F2D1','save','保存','save',1),('7321224A-C052-431B-B48C-C12DFD4292DA','4F0BC221-A2D0-4500-907A-EC116B48AC74','approve','核准','approve',4),('745C43D0-441D-4568-BFC0-10E5436C31C5','67CF818A-7AB6-43CB-BDD5-A57B5FC2C512','signIn','签到','signIn',1),('817C7748-7267-4327-A5BC-094F17598734','A2E1E8E7-EAC9-45F7-AAEE-D08C89BDEE0A','close','关闭','close',2),('882E1437-1B3B-4BB0-8D09-56A97491B105','3844F972-311A-459A-8B9A-B39DFA526B6C','close','关闭','close',2),('88651356-FC4E-43EA-BDEF-09BA37B61C9B','0248868D-BBBE-4F96-B7C5-FAF4E1556570','validDays','设置有效天数','validDays',5),('898A6526-F0B2-4C74-A5FB-E7EC00949FE7','98733F5B-A599-41A9-B135-33F480BDE062','approve','核准','approve',3),('8B1FE359-E708-466B-9324-DD003477D418','0248868D-BBBE-4F96-B7C5-FAF4E1556570','summary','汇总','summary',1),('8F66A121-3CCD-4EC5-8E50-A160193ACE64','145920A1-3843-419E-A633-5C3AC2AFC454','approve','核准','approve',3),('95259C3E-BE55-4E42-ACBA-2031770FD14D','4878413D-5A54-464A-8ECE-59E78D43F2D1','submit','提交','submit',2),('99F81EE3-55DB-4C05-A3E1-E6867AE5F447','86C0B8E5-92FE-4FB7-ACBF-1C63243B5D30','edit','编辑','edit',1),('9F66B841-0922-4012-8D5A-063F7FC7719A','4878413D-5A54-464A-8ECE-59E78D43F2D1','back','返回','back',5),('A86CFB0E-B6EB-41A4-9B92-4A3833487D72','98733F5B-A599-41A9-B135-33F480BDE062','edit','编辑','edit',1),('ADC2DE07-FDC6-40AB-A215-730D87325CE8','0248868D-BBBE-4F96-B7C5-FAF4E1556570','detail','查看','detail',7),('B1B9439B-7905-4128-A4DD-60EAC2A6BFFD','A9846F89-E5FA-4820-9913-0CF1BB71F4D1','confirm','确认','confirm',1),('B2D8182E-B201-4858-B52D-8197064E6CCE','7782AD69-8FDC-4F5D-8510-36FD8D3BF249','apply','申请','apply',1),('B5F84B18-BB64-4F14-9511-B232A44894F1','67CF818A-7AB6-43CB-BDD5-A57B5FC2C512','exportWord','导出word','exportWord',3),('B6F4ADFE-3BCC-4D84-B4CC-1A4991A0556B','4F7D78BE-CEF2-4B71-9F43-EEF3ABADC5A1','delete','删除','delete',7),('BE9FE91B-F66A-4CE1-A5AC-DA36B22B3065','86C0B8E5-92FE-4FB7-ACBF-1C63243B5D30','save','保存','save',2),('BF43D760-8731-4D1C-A54A-6024E09682DD','05CBEDD2-E00C-4383-B5BA-F117BAB217E5','close','关闭','close',3),('C275E1CE-3977-4899-BC18-1DA376A83E4B','0248868D-BBBE-4F96-B7C5-FAF4E1556570','edit','编辑','edit',2),('C477EF88-C1E7-4908-81EA-770E31240780','145920A1-3843-419E-A633-5C3AC2AFC454','save','保存','save',2),('C953BDA3-52AC-49D2-A39B-C7A822F95A4C','4878413D-5A54-464A-8ECE-59E78D43F2D1','approve','核准','approve',4),('D2CD85A9-1A48-4373-9496-376F5402B858','67CF818A-7AB6-43CB-BDD5-A57B5FC2C512','exportPdf','导出pdf','exportPdf',4),('D88643B1-1076-40D7-B7F1-CE3504E2AA85','4F7D78BE-CEF2-4B71-9F43-EEF3ABADC5A1','create','新增','create',1),('DF5E4931-A578-41AD-A79A-6503EAB4DC83','642BCD38-28C1-4FF1-A194-535BFAB10679','edit','编辑','edit',2),('E33BCEC7-9BB5-4CF9-8FCD-76CE7DD0B684','7782AD69-8FDC-4F5D-8510-36FD8D3BF249','detail','查看','detail',5),('E74F0296-3814-41BA-AC28-2302FB41306F','4F0BC221-A2D0-4500-907A-EC116B48AC74','back','返回','back',6),('EEA03EB1-F621-4DAA-AC94-44BFF0872FB7','05CBEDD2-E00C-4383-B5BA-F117BAB217E5','edit','编辑','edit',1),('F25C0F1E-6180-48FA-83D4-711DABEFD4A9','145920A1-3843-419E-A633-5C3AC2AFC454','back','返回','back',5),('F6A1F60A-C579-40C9-B804-B0800F01B9A0','A9846F89-E5FA-4820-9913-0CF1BB71F4D1','close','关闭','close',2),('FB7B15C0-9D21-4AFA-8918-28DFDAC25FA1','7D03AFCE-974A-45B8-B163-34002C7CE07B','edit','编辑','edit',1),('FC086415-FACB-4530-B38A-F993ABE59337','27BCFFDE-DD30-4659-8EAB-D0D848A39DB8','close','关闭','close',2),('FE224D4E-381B-4DC6-82EA-B7F2C70F4012','642BCD38-28C1-4FF1-A194-535BFAB10679','apply','申请','apply',1),('FFA6D0CD-ED98-4C51-BBDC-A3367BE0B594','4F7D78BE-CEF2-4B71-9F43-EEF3ABADC5A1','approve','核准','approve',3);
/*!40000 ALTER TABLE `sys_operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wcm_general_group`
--

DROP TABLE IF EXISTS `wcm_general_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wcm_general_group` (
  `GRP_ID` varchar(36) NOT NULL,
  `GRP_NAME` varchar(64) DEFAULT NULL,
  `GRP_PID` varchar(36) DEFAULT NULL,
  `GRP_ORDERNO` int(11) DEFAULT NULL,
  `GRP_IS_SYSTEM` varchar(32) DEFAULT NULL,
  `GRP_RES_TYPE_DESC` varchar(32) DEFAULT NULL,
  `GRP_RES_TYPE_EXTS` varchar(128) DEFAULT NULL,
  `GRP_RES_SIZE_LIMIT` varchar(32) DEFAULT NULL,
  `GRP_DESC` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`GRP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wcm_general_group`
--

LOCK TABLES `wcm_general_group` WRITE;
/*!40000 ALTER TABLE `wcm_general_group` DISABLE KEYS */;
INSERT INTO `wcm_general_group` VALUES ('77777777-7777-7777-7777-777777777777','附件目录','',NULL,'','','','',''),('A6018D88-8345-46EE-A452-CE362FAC72E2','视频文件','77777777-7777-7777-7777-777777777777',2,'Y','视频','*.mp4;*.3gp;*.wmv;*.avi;*.rm;*.rmvb;*.flv','100MB',''),('CF35D1E6-102E-428A-B39C-0072D491D5B1','业务附件','77777777-7777-7777-7777-777777777777',1,'Y','所有资源','*.*','2MB','');
/*!40000 ALTER TABLE `wcm_general_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wcm_general_resource`
--

DROP TABLE IF EXISTS `wcm_general_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wcm_general_resource` (
  `RES_ID` varchar(36) NOT NULL,
  `GRP_ID` varchar(36) DEFAULT NULL,
  `RES_NAME` varchar(64) DEFAULT NULL,
  `RES_SHAREABLE` varchar(32) DEFAULT NULL,
  `RES_LOCATION` varchar(256) DEFAULT NULL,
  `RES_SIZE` varchar(64) DEFAULT NULL,
  `RES_SUFFIX` varchar(32) DEFAULT NULL,
  `RES_DESCRIPTION` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`RES_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wcm_general_resource`
--

LOCK TABLES `wcm_general_resource` WRITE;
/*!40000 ALTER TABLE `wcm_general_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `wcm_general_resource` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-16 16:35:27
