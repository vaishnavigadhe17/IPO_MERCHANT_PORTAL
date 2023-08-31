-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 20, 2023 at 08:19 AM
-- Server version: 5.1.41
-- PHP Version: 5.3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `ipo-portal`
--
CREATE DATABASE `ipo-portal` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `ipo-portal`;

-- --------------------------------------------------------

--
-- Table structure for table `admin_details`
--

CREATE TABLE IF NOT EXISTS `admin_details` (
  `Admin_Id` int(11) NOT NULL,
  `Admin_Name` varchar(250) NOT NULL,
  `Admin_Password` varchar(250) NOT NULL,
  PRIMARY KEY (`Admin_Id`),
  KEY `Admin_Id` (`Admin_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin_details`
--

INSERT INTO `admin_details` (`Admin_Id`, `Admin_Name`, `Admin_Password`) VALUES
(1, 'admin', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `category_details`
--

CREATE TABLE IF NOT EXISTS `category_details` (
  `Category_Id` int(11) NOT NULL AUTO_INCREMENT,
  `Category_Code` varchar(6) NOT NULL,
  `Category_Name` varchar(250) NOT NULL,
  `Admin_Id` int(11) NOT NULL,
  `Recorded_Date` date NOT NULL,
  PRIMARY KEY (`Category_Id`),
  KEY `Category_Id` (`Category_Id`),
  KEY `Admin_Id` (`Admin_Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `category_details`
--

INSERT INTO `category_details` (`Category_Id`, `Category_Code`, `Category_Name`, `Admin_Id`, `Recorded_Date`) VALUES
(1, 'MC', 'Merchant Creation', 1, '2023-08-15'),
(2, 'ME', 'Merchant Enquiry', 1, '2023-08-15'),
(3, 'MA', 'Merchant Approval', 1, '2023-08-15');

-- --------------------------------------------------------

--
-- Table structure for table `merchant_details`
--

CREATE TABLE IF NOT EXISTS `merchant_details` (
  `Merchant_Id` int(11) NOT NULL AUTO_INCREMENT,
  `User_Id` int(11) NOT NULL,
  `Upload_Id` int(11) NOT NULL,
  `Name` varchar(250) NOT NULL,
  `Marketing_Name` varchar(250) NOT NULL,
  `Address` text NOT NULL,
  `City` varchar(250) NOT NULL,
  `State` varchar(250) NOT NULL,
  `Pincode` varchar(10) NOT NULL,
  `Mobile_No` varchar(15) DEFAULT NULL,
  `Landline_No` varchar(15) DEFAULT NULL,
  `Director` varchar(250) NOT NULL,
  `Category` varchar(250) DEFAULT NULL,
  `MCC` varchar(250) DEFAULT NULL,
  `MCC_Desc` varchar(250) DEFAULT NULL,
  `Type_Of_Integration` varchar(250) NOT NULL,
  `VPA` varchar(250) NOT NULL,
  `GST_Number` varchar(250) NOT NULL,
  `PAN` varchar(250) NOT NULL,
  `Merchant_Official_Id` varchar(250) DEFAULT NULL,
  `Merchant_Website` varchar(250) DEFAULT NULL,
  `Bank_Name` varchar(250) DEFAULT NULL,
  `IFSC_Code` varchar(10) NOT NULL,
  `Nodal_Account_No` varchar(20) NOT NULL,
  `Pool_Account_No` varchar(20) DEFAULT NULL,
  `Pool_IFSC_Code` varchar(10) DEFAULT NULL,
  `SYM` varchar(250) DEFAULT NULL,
  `Status` varchar(250) NOT NULL,
  `Reason` text,
  `Recorded_Date` datetime NOT NULL,
  PRIMARY KEY (`Merchant_Id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `merchant_details`
--

INSERT INTO `merchant_details` (`Merchant_Id`, `User_Id`, `Upload_Id`, `Name`, `Marketing_Name`, `Address`, `City`, `State`, `Pincode`, `Mobile_No`, `Landline_No`, `Director`, `Category`, `MCC`, `MCC_Desc`, `Type_Of_Integration`, `VPA`, `GST_Number`, `PAN`, `Merchant_Official_Id`, `Merchant_Website`, `Bank_Name`, `IFSC_Code`, `Nodal_Account_No`, `Pool_Account_No`, `Pool_IFSC_Code`, `SYM`, `Status`, `Reason`, `Recorded_Date`) VALUES
(1, 2, 1, 'MAAGH ADVERTISING AND MARKETING SERVICES LIMITED', 'MAAGH ADVERTISING AND MARKETING SERVICES LIMITED', '101 near Dmart New Link Road, Andheri West ', 'Mumbai', 'Maharashtra', '400051', '7664531811', '22 4603 2341', 'Azharuddin R Nomani', 'IPO', '6211', 'Broking', 'Web collect', 'nomani@axisbank', '27AAICM6541FT1', 'SRTRP8542D', 'dummymerchant@info.in', 'www.dummywebsite.com', 'Axis bank', '', '', '', '', 'MAAGHAD', 'Approved', '', '2023-08-20 00:00:00'),
(2, 2, 3, 'java', 'java', 'java', 'java', 'java', '607002', '', '', 'java', '', 'java', 'java', 'java', 'java@axisbank', 'test23132321321', 'java', '', '', '', 'java', 'java', '', 'java', '', 'Uploaded', NULL, '2023-08-20 00:00:00'),
(3, 2, 4, 'Car', 'Car', 'Car', 'Car', 'Car', '607002', '', '', 'Car', '', 'Car', 'Car', 'Car', 'javaNSE@axisbank', 'Car', 'Car', '', '', '', 'Car', 'Car', '', 'Car', '', 'Rejected', 'Incorrect record, upload after correcting it.', '2023-08-20 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `merchant_update_details`
--

CREATE TABLE IF NOT EXISTS `merchant_update_details` (
  `Merchant_Update_Id` int(11) NOT NULL AUTO_INCREMENT,
  `Merchant_Id` int(11) NOT NULL,
  `Requested_User_Id` int(11) DEFAULT NULL,
  `Reason` text,
  `Approver_User_Id` int(11) DEFAULT NULL,
  `Status` varchar(250) NOT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Recorded_Date` datetime NOT NULL,
  PRIMARY KEY (`Merchant_Update_Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `merchant_update_details`
--

INSERT INTO `merchant_update_details` (`Merchant_Update_Id`, `Merchant_Id`, `Requested_User_Id`, `Reason`, `Approver_User_Id`, `Status`, `Updated_Date`, `Recorded_Date`) VALUES
(1, 3, 1, 'Update the VPA and other mandatory field and up[load once again.', 2, 'Approved', '2023-08-20 00:00:00', '2023-08-20 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `upload_details`
--

CREATE TABLE IF NOT EXISTS `upload_details` (
  `Upload_Id` int(11) NOT NULL AUTO_INCREMENT,
  `User_Id` int(11) NOT NULL,
  `File_Name` varchar(250) NOT NULL,
  `File_Extension` varchar(10) NOT NULL,
  `Message` text NOT NULL,
  `Status` tinyint(1) NOT NULL,
  `Recorded_Date` datetime NOT NULL,
  PRIMARY KEY (`Upload_Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `upload_details`
--

INSERT INTO `upload_details` (`Upload_Id`, `User_Id`, `File_Name`, `File_Extension`, `Message`, `Status`, `Recorded_Date`) VALUES
(1, 2, 'TBL_MERCH_ONBOARD_DTLS', 'xlsx', 'Uploaded the file successfully!', 1, '2023-08-20 00:00:00'),
(2, 2, 'TBL_MERCH_ONBOARD_DTLS _1', 'xlsx', 'The Uploaded file, have some existing records.', 0, '2023-08-20 00:00:00'),
(3, 2, '-', '-', 'Uploaded By Merchant Creation Form', 1, '2023-08-20 00:00:00'),
(4, 2, '-', '-', 'Uploaded By Merchant Creation Form', 1, '2023-08-20 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `user_details`
--

CREATE TABLE IF NOT EXISTS `user_details` (
  `User_Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(250) NOT NULL,
  `Phoneno` bigint(15) NOT NULL,
  `Email_Id` varchar(250) NOT NULL,
  `Address` text NOT NULL,
  `Location` int(11) NOT NULL,
  `Category_Id` int(11) NOT NULL,
  `Username` varchar(250) NOT NULL,
  `Password` varchar(250) NOT NULL,
  `Recorded_Date` date NOT NULL,
  PRIMARY KEY (`User_Id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=100000 ;

--
-- Dumping data for table `user_details`
--

INSERT INTO `user_details` (`User_Id`, `Name`, `Phoneno`, `Email_Id`, `Address`, `Location`, `Category_Id`, `Username`, `Password`, `Recorded_Date`) VALUES
(2, 'kiruba', 9043963074, 'kirubakarans2009@gmail.com', 'chennai', 607002, 1, 'kiruba', 'kiruba', '2023-08-15'),
(3, 'hari', 8043963075, 'hari@gmail.com', 'chennai', 607002, 2, 'hari', 'hari', '2023-08-15'),
(4, 'venkat', 7843963076, 'venkat@gmail.com', 'Chennai', 607002, 3, 'venkat', 'venkat', '2023-08-15');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
