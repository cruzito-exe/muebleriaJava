-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 10, 2023 at 11:58 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `systemtest`
--

-- --------------------------------------------------------

--
-- Table structure for table `clientes`
--

CREATE TABLE `clientes` (
  `idCliente` int(11) NOT NULL,
  `nombreCliente` varchar(255) NOT NULL,
  `apellidoCliente` varchar(255) NOT NULL,
  `DUI` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `clientes`
--

INSERT INTO `clientes` (`idCliente`, `nombreCliente`, `apellidoCliente`, `DUI`) VALUES
(1, 'David Alejandro', 'Cruz Cruz', '06364093-2'),
(4, 'Madelyn Daniela', 'Ortíz Genovez', '12345678-9'),
(5, 'asdfasd asasa', 'asdas asa', '1111111111'),
(6, 'sdfsdfsd sdfZz', 'sdfsdfsd dsfsdfsazx', '22222222-2');

-- --------------------------------------------------------

--
-- Table structure for table `empleados`
--

CREATE TABLE `empleados` (
  `idEmpleado` int(11) NOT NULL,
  `nombreEmpleado` varchar(255) NOT NULL,
  `apellidoEmpleado` varchar(255) NOT NULL,
  `DUI` varchar(10) NOT NULL,
  `usuario` varchar(255) NOT NULL,
  `tipoUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `empleados`
--

INSERT INTO `empleados` (`idEmpleado`, `nombreEmpleado`, `apellidoEmpleado`, `DUI`, `usuario`, `tipoUsuario`) VALUES
(1, 'David', 'Cruz', '06364093-2', 'David Cruz', 2),
(2, 'Alejandro Antonio', 'Cruz Cruz', '02319203-1', 'Alejandro Cruz', 1),
(6, 'asdasdas', 'qweqweqw', '22222222-2', 'null', 0),
(7, 'Juan Pedro', 'Lopez Perez', '99999999-9', 'Juan Lopez', 2),
(16, 'Daniel Alexander', 'Torres Alfaro', '01929018-2', 'Daniel Torres', 1),
(17, 'asdasd asdas', 'asdasd asdsa', '12121212-1', 'asdasd asdasd', 1);

-- --------------------------------------------------------

--
-- Table structure for table `productos`
--

CREATE TABLE `productos` (
  `idProducto` int(11) NOT NULL,
  `codigoProducto` varchar(12) NOT NULL,
  `nombreProducto` varchar(255) NOT NULL,
  `precioProducto` double(5,2) NOT NULL,
  `nombreProveedor` varchar(255) NOT NULL,
  `materialProducto` varchar(255) NOT NULL,
  `stockProducto` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `productos`
--

INSERT INTO `productos` (`idProducto`, `codigoProducto`, `nombreProducto`, `precioProducto`, `nombreProveedor`, `materialProducto`, `stockProducto`) VALUES
(1, '00000000001', 'Silla de Arce', 24.25, 'Serrería \"El árbol alegre\"', 'Madera de Arce, Pintura Blanca, Barniz', 50),
(4, '00000000002', 'Banquito', 22.40, 'Serrería \"El árbol alegre\"', 'Madera de caoba, pintura verde, barniz', 72);

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

CREATE TABLE `usuarios` (
  `idUsuario` int(11) NOT NULL,
  `usuario` varchar(255) NOT NULL,
  `password` varchar(50) NOT NULL,
  `tipoUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ventas`
--

CREATE TABLE `ventas` (
  `idVenta` int(11) NOT NULL,
  `codigoVenta` varchar(12) NOT NULL,
  `nombreCliente` varchar(255) NOT NULL,
  `nombreProducto` varchar(255) NOT NULL,
  `precioProducto` varchar(255) NOT NULL,
  `cantidadVenta` int(11) NOT NULL,
  `totalVenta` double(5,2) NOT NULL,
  `fechaVenta` varchar(10) NOT NULL,
  `nombreEmpleado` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `ventas`
--

INSERT INTO `ventas` (`idVenta`, `codigoVenta`, `nombreCliente`, `nombreProducto`, `precioProducto`, `cantidadVenta`, `totalVenta`, `fechaVenta`, `nombreEmpleado`) VALUES
(1, '0000000001', 'David Alejandro Cruz Cruz', 'Silla de Roble', '24.00', 1, 24.00, '2023-07-09', 'David Cruz'),
(2, '0000000002', 'Madelyn Daniela Ortíz Genovez', 'Silla de Roble', '24.00', 2, 49.00, '2023-07-09', 'Alejandro Cruz'),
(3, '0000000003', 'David Alejandro Cruz Cruz', 'Silla de Arce', '12.00', 2, 24.00, '2023-07-10', 'David Cruz'),
(4, '0000000004', 'David Alejandro Cruz Cruz', 'Silla de Arce', '15.00', 2, 30.00, '2023-07-10', 'David Cruz'),
(5, '0000000005', 'Madelyn Daniela Ortíz Genovez', 'Banco de Abeto', '15.00', 3, 45.00, '2023-07-10', 'David Cruz'),
(6, '0000000006', 'Madelyn Daniela Ortíz Genovez', 'Banco de Abeto', '15.00', 2, 30.00, '2023-07-10', 'David Cruz'),
(7, '0000000007', 'David Alejandro Cruz Cruz', 'adasd', '12.00', 2, 24.00, '2023-07-10', 'David Cruz'),
(8, '0000000008', 'David Alejandro Cruz Cruz', 'Silla de arce,Silla de roble', '15.00', 2, 30.00, '2023-07-10', 'David Cruz'),
(9, '0000000009', 'David Alejandro Cruz Cruz', 'Silla de Arce,Banquito', '21.23', 3, 63.69, '2023-07-10', 'David Cruz'),
(10, '0000000010', 'David Alejandro Cruz Cruz', 'Silla de Arce,Banquito,Silla de Arce,Silla de Arce', '21.00', 2, 42.00, '2023-07-10', 'David Cruz');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`idCliente`);

--
-- Indexes for table `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`idEmpleado`);

--
-- Indexes for table `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`idProducto`);

--
-- Indexes for table `ventas`
--
ALTER TABLE `ventas`
  ADD PRIMARY KEY (`idVenta`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `clientes`
--
ALTER TABLE `clientes`
  MODIFY `idCliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `empleados`
--
ALTER TABLE `empleados`
  MODIFY `idEmpleado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `productos`
--
ALTER TABLE `productos`
  MODIFY `idProducto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `ventas`
--
ALTER TABLE `ventas`
  MODIFY `idVenta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
