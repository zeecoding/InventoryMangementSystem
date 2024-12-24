-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 08, 2024 at 08:15 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `inventory`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`, `role`) VALUES
(2, 'admin', 'admin123', 'admin'),
(3, 'ali', 'ali123', 'salesman');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `customer_id` int(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `brand` varchar(100) NOT NULL,
  `productName` varchar(100) NOT NULL,
  `quantity` int(100) NOT NULL,
  `price` double NOT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id`, `customer_id`, `type`, `brand`, `productName`, `quantity`, `price`, `date`) VALUES
(96, 1, 'Cosmetics', 'Dove', 'Moisturizing Cream', 3, 855, '2024-11-08'),
(97, 2, 'Drinks', 'Nestle', 'Pineapple Juice', 1, 250, '2024-11-08'),
(98, 2, 'Snacks', 'Lays', 'Cheeze', 2, 40, '2024-11-08'),
(99, 3, 'Snacks', 'Lays', 'Cheeze', 5, 100, '2024-11-08'),
(101, 4, 'Snacks', 'Lays', 'Cheeze', 2, 40, '2024-11-08'),
(102, 4, 'Drinks', 'Nestle', 'Pineapple Juice', 1, 250, '2024-11-08'),
(103, 5, 'Cosmetics', 'Dove', 'Moisturizing Cream', 2, 570, '2024-11-09'),
(104, 5, 'Drinks', 'Nestle', 'Pineapple Juice', 2, 500, '2024-11-09'),
(105, 5, 'Snacks', 'Lays', 'Cheeze', 1, 20, '2024-11-09'),
(106, 6, 'Cosmetics', 'Dove', 'Moisturizing Cream', 2, 570, '2024-11-09'),
(107, 7, 'Snacks', 'Lays', 'Cheeze', 5, 100, '2024-11-09'),
(108, 8, 'Cosmetics', 'Dove', 'Moisturizing Cream', 3, 855, '2024-11-11'),
(109, 8, 'Drinks', 'Nestle', 'Pineapple Juice', 2, 500, '2024-11-11'),
(110, 8, 'Snacks', 'Lays', 'Cheeze', 5, 100, '2024-11-11'),
(111, 9, 'Cosmetics', 'Dove', 'Moisturizing Cream', 2, 570, '2024-11-11'),
(112, 9, 'Snacks', 'Lays', 'Cheeze', 2, 40, '2024-11-11'),
(115, 10, 'Snacks', 'Lays', 'Cheeze', 4, 80, '2024-11-11'),
(116, 11, 'Cosmetics', 'Dove', 'Moisturizing Cream', 1, 285, '2024-11-11'),
(117, 12, 'Snacks', 'Lays', 'Cheeze', 5, 100, '2024-11-14'),
(120, 13, 'Snacks', 'Lays', 'Cheeze', 5, 100, '2024-11-19'),
(121, 13, 'Cosmetics', 'Dove', 'Moisturizing Cream', 2, 570, '2024-11-19'),
(122, 13, 'Drinks', 'Nestle', 'Pineapple Juice', 3, 750, '2024-11-19'),
(128, 14, 'Snacks', 'Nestle', 'Kitkat chocolate bar', 4, 202.04, '2024-12-03'),
(129, 14, 'Snacks', 'Pepsico', 'Cheetos Crunchy', 5, 19.25, '2024-12-03'),
(130, 14, 'Snacks', 'Mondelez', 'Oreo Cookies', 3, 13.5, '2024-12-03'),
(131, 15, 'Drinks', 'Nestle', 'Pineapple Juice', 2, 500, '2024-12-03');

-- --------------------------------------------------------

--
-- Table structure for table `customer_receipt`
--

CREATE TABLE `customer_receipt` (
  `id` int(11) NOT NULL,
  `customer_id` int(100) NOT NULL,
  `total` double NOT NULL,
  `paid` double NOT NULL,
  `balance` double NOT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer_receipt`
--

INSERT INTO `customer_receipt` (`id`, `customer_id`, `total`, `paid`, `balance`, `date`) VALUES
(7, 1, 855, 900, 45, '2024-11-08'),
(8, 2, 290, 300, 10, '2024-11-08'),
(9, 3, 100, 100, 0, '2024-11-08'),
(10, 4, 290, 300, 10, '2024-11-08'),
(11, 5, 1090, 1100, 10, '2024-11-09'),
(12, 6, 570, 600, 30, '2024-11-09'),
(13, 7, 100, 100, 0, '2024-11-09'),
(14, 8, 1455, 1500, 45, '2024-11-11'),
(15, 9, 610, 620, 10, '2024-11-11'),
(16, 10, 80, 100, 20, '2024-11-11'),
(17, 11, 285, 300, 15, '2024-11-11'),
(18, 12, 100, 100, 0, '2024-11-14'),
(19, 13, 1420, 1500, 80, '2024-11-19'),
(32, 14, 234.79, 240, 5.21, '2024-12-03'),
(33, 15, 500, 500, 0, '2024-12-03');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `product_id` int(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `brand` varchar(100) NOT NULL,
  `productName` varchar(100) NOT NULL,
  `price` double NOT NULL,
  `status` varchar(100) NOT NULL,
  `image` varchar(500) NOT NULL,
  `date` date DEFAULT NULL,
  `quantity` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `product_id`, `type`, `brand`, `productName`, `price`, `status`, `image`, `date`, `quantity`) VALUES
(3, 1, 'Cosmetics', 'Dove', 'Moisturizing Cream', 285, 'Available', 'C:\\\\Users\\\\Acer\\\\Downloads\\\\pexels-828860-2693646.jpg', '2024-12-03', 11),
(4, 2, 'Drinks', 'Nestle', 'Pineapple Juice', 250, 'Available', 'C:\\\\Users\\\\Acer\\\\Downloads\\\\images.jpeg', '2024-11-19', 17),
(6, 3, 'Crockery', 'National', 'Cooking Pan', 1000, 'Available', 'C:\\Users\\Acer\\Downloads\\download.jpeg', '2024-11-11', 5),
(7, 4, 'Snacks', 'Lays', 'Cheeze', 20, 'Available', 'C:\\Users\\Acer\\Downloads\\download (2).jpeg', '2024-11-19', 22),
(10, 5, 'Snacks', 'Nestle', 'Kitkat chocolate bar', 50.51, 'Available', 'src/resources/default.png', '2024-12-03', 6),
(11, 6, 'Snacks', 'Mondelez', 'Oreo Cookies', 4.5, 'Available', '', '2024-12-03', 24),
(12, 7, 'Snacks', 'Pepsico', 'Cheetos Crunchy', 3.85, 'Available', '', '2024-12-03', 5),
(13, 8, 'Drinks', 'Pepsico', 'Orange Jucice', 8.54, 'Available', 'src/resources/default.png', '2024-12-03', 10),
(14, 9, 'Snacks', 'Kellog', 'Pringles', 10.23, 'Available', '', '2024-12-03', 15),
(15, 10, 'Cosmetics', 'MAC', 'Ruby Woo lipstick', 385.2, 'Available', '', '2024-12-03', 15),
(16, 11, 'Cosmetics', 'NIvea', 'Mositurizer', 430, 'Available', '', '2024-12-03', 5),
(17, 12, 'Crockery', 'Corelle', 'Dinner Plate Set', 855, 'Available', '', '2024-12-03', 4),
(18, 13, 'Crockery', 'Pyrex', 'Mixing Bowls', 955.85, 'Available', '', '2024-12-03', 10),
(19, 14, 'Crockery', 'IKEA', '365+ Mug', 658, 'Available', '', '2024-12-03', 2),
(20, 15, 'Drinks', 'Monster', 'Energy Drink', 455, 'Available', '', '2024-12-03', 7),
(21, 16, 'Snacks', 'Mars', 'Chocolate Bar', 355, 'Available', '', '2024-12-03', 50),
(22, 17, 'Crockery', 'Royal Albert', 'Tea set', 658.9, 'Available', '', '2024-12-04', 6),
(23, 18, 'Drinks', 'CocaCola', 'Fanta', 185, 'Available', '', '2024-12-03', 50);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customer_receipt`
--
ALTER TABLE `customer_receipt`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=132;

--
-- AUTO_INCREMENT for table `customer_receipt`
--
ALTER TABLE `customer_receipt`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
