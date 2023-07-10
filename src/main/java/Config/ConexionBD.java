package Config;

import Classes.Clients;
import Classes.Employees;
import Classes.Inventory;
import Classes.Sales;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ConexionBD {
 static Statement statement;
 static ResultSet resultset;
 static PreparedStatement preparedStatement;
 static Connection connection = null;
 
 public static Connection conexionBD() throws ClassNotFoundException, SQLException {
  try {
   Class.forName("com.mysql.jdbc.Driver");
   connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/systemTest", "root", "");
   statement = connection.createStatement();
  } catch (SQLException ex) {
   System.out.println("Error al conectar con la base de datos, error: "+ex.getMessage());
  }
     
  return connection;
 }
 
 /* CRUD de clientes */

 public static void saveClient(Clients clients) {
  String saveClient = "insert into clientes (nombreCliente, apellidoCliente, DUI) values (?, ?, ?)";
  
  try {
   preparedStatement = connection.prepareStatement(saveClient);
   preparedStatement.setString(1, clients.getClientName());
   preparedStatement.setString(2, clients.getClientLastName());
   preparedStatement.setString(3, clients.getDUI());
   preparedStatement.executeUpdate();
   JOptionPane.showMessageDialog(null, "Cliente registrado exitosamente", "Cliente registrado", JOptionPane.INFORMATION_MESSAGE);
  } catch(SQLException ex) {
   JOptionPane.showMessageDialog(null, "Error al registrar cliente", "Error", JOptionPane.ERROR_MESSAGE);
  }
 }
 
 public static void updateClient(Clients clients) {
  String updateClient = "update clientes set nombreCliente = ?, apellidoCliente = ?, DUI = ? where idCliente = ?";
  
  try {
   preparedStatement = connection.prepareStatement(updateClient);
   preparedStatement.setString(1, clients.getClientName());
   preparedStatement.setString(2, clients.getClientLastName());
   preparedStatement.setString(3, clients.getDUI());
   preparedStatement.setInt(4, clients.getClientId());
   preparedStatement.executeUpdate();
   
   JOptionPane.showMessageDialog(null, "Datos de cliente actualizados exitosamente", "Datos actualizados", JOptionPane.INFORMATION_MESSAGE);
  } catch(SQLException ex) {
   JOptionPane.showMessageDialog(null, "Error al actualizar registro de cliente", "Error", JOptionPane.ERROR_MESSAGE);
  }
 }
 
 public static void generateClientsReport(){
  JasperReport jasperReport;
  JasperPrint jasperPrint;
  JasperViewer jasperViewer;
  String path = "src\\main\\java\\Reports\\Clients.jasper";
     
  try {
   jasperReport = (JasperReport) JRLoader.loadObjectFromFile(path);
   jasperPrint = JasperFillManager.fillReport(jasperReport, null, conexionBD());
   jasperViewer = new JasperViewer(jasperPrint, false);
   jasperViewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   jasperViewer.setVisible(true);
  } catch(ClassNotFoundException | SQLException | JRException ex) {
   JOptionPane.showMessageDialog(null, "Error al cargar reporte de clientes", "Error", JOptionPane.ERROR_MESSAGE);
   System.out.println("Error al cargar reporte de clientes, error: "+ex.getMessage());
  }
 }
 
 /* CRUD de empleados */
 
 public static void saveEmployee(Employees employees) {
  String saveEmployee = "insert into empleados (nombreEmpleado, apellidoEmpleado, DUI, usuario, tipoUsuario) values (?, ?, ?, ?, ?)";
  
  try {
   preparedStatement = connection.prepareStatement(saveEmployee);
   preparedStatement.setString(1, employees.getEmployeeName());
   preparedStatement.setString(2, employees.getEmployeeLastName());
   preparedStatement.setString(3, employees.getDUI());
   preparedStatement.setString(4, employees.getUser());
   preparedStatement.setInt(5, employees.getUserType());
   preparedStatement.executeUpdate();
   JOptionPane.showMessageDialog(null, "Empleado registrado exitosamente", "Empleado registrado", JOptionPane.INFORMATION_MESSAGE);
  } catch(SQLException ex) {
   JOptionPane.showMessageDialog(null, "Error al registrar empleado", "Error", JOptionPane.ERROR_MESSAGE);
  }
 }
 
 public static void updateEmployee(Employees employees) {
  String updateEmployee = "update empleados set nombreEmpleado = ?, apellidoEmpleado = ?, DUI = ?, usuario = ?, tipoUsuario = ? where idEmpleado = ?";
  
  try {
   preparedStatement = connection.prepareStatement(updateEmployee);
   preparedStatement.setString(1, employees.getEmployeeName());
   preparedStatement.setString(2, employees.getEmployeeLastName());
   preparedStatement.setString(3, employees.getDUI());
   preparedStatement.setString(4, employees.getUser());
   preparedStatement.setInt(5, employees.getUserType());
   preparedStatement.setInt(6, employees.getEmployeeId());
   preparedStatement.executeUpdate();
   
   JOptionPane.showMessageDialog(null, "Datos de empleado actualizados exitosamente", "Datos actualizados", JOptionPane.INFORMATION_MESSAGE);
  } catch(SQLException ex) {
   JOptionPane.showMessageDialog(null, "Error al actualizar registro de empleado", "Error", JOptionPane.ERROR_MESSAGE);
  }
 }
 
 public static void generateEmployeesReport(){
  JasperReport jasperReport;
  JasperPrint jasperPrint;
  JasperViewer jasperViewer;
  String path = "src\\main\\java\\Reports\\Employees.jasper";
     
  try {
   jasperReport = (JasperReport) JRLoader.loadObjectFromFile(path);
   jasperPrint = JasperFillManager.fillReport(jasperReport, null, conexionBD());
   jasperViewer = new JasperViewer(jasperPrint, false);
   jasperViewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   jasperViewer.setVisible(true);
  } catch(ClassNotFoundException | SQLException | JRException ex) {
   JOptionPane.showMessageDialog(null, "Error al cargar reporte de empleados", "Error", JOptionPane.ERROR_MESSAGE);
   System.out.println("Error al cargar reporte de empleados, error: "+ex.getMessage());
  }
 }
 
 /* CRUD de inventario */
 
 public static void saveInventory(Inventory inventory) {
  String saveInventory = "insert into productos (codigoProducto, nombreProducto, precioProducto, nombreProveedor, materialProducto, stockProducto) values (?, ?, ?, ?, ?, ?)";
  
  try {
   preparedStatement = connection.prepareStatement(saveInventory);
   preparedStatement.setString(1, inventory.getProductCode());
   preparedStatement.setString(2, inventory.getProductName());
   preparedStatement.setDouble(3, inventory.getProductPrice());
   preparedStatement.setString(4, inventory.getProvideerName());
   preparedStatement.setString(5, inventory.getProductMaterial());
   preparedStatement.setInt(6, inventory.getProductStock());
   preparedStatement.executeUpdate();
   JOptionPane.showMessageDialog(null, "Producto registrado exitosamente", "Producto registrado", JOptionPane.INFORMATION_MESSAGE);
  } catch(SQLException ex) {
   JOptionPane.showMessageDialog(null, "Error al registrar producto", "Error", JOptionPane.ERROR_MESSAGE);
  }
 }

 public static void updateInventory(Inventory inventory) {
  String updateEmployee = "update productos set codigoProducto = ?, nombreProducto = ?, precioProducto = ?, nombreProveedor = ?, materialProducto = ? where idProducto = ?";
  
  try {
   preparedStatement = connection.prepareStatement(updateEmployee);
   preparedStatement.setString(1, inventory.getProductCode());
   preparedStatement.setString(2, inventory.getProductName());
   preparedStatement.setDouble(3, inventory.getProductPrice());
   preparedStatement.setString(4, inventory.getProvideerName());
   preparedStatement.setString(5, inventory.getProductMaterial());
   preparedStatement.setInt(6, inventory.getProductStock());
   preparedStatement.setInt(7, inventory.getProductId());
   preparedStatement.executeUpdate();
   
   JOptionPane.showMessageDialog(null, "Datos de producto actualizados exitosamente", "Datos actualizados", JOptionPane.INFORMATION_MESSAGE);
  } catch(SQLException ex) {
   JOptionPane.showMessageDialog(null, "Error al actualizar registro de producto", "Error", JOptionPane.ERROR_MESSAGE);
   //System.out.println("Error al actualizar datos de producto, error: "+ex.getMessage());
  }
 }
 
 public static void generateInventoryReport(){
  JasperReport jasperReport;
  JasperPrint jasperPrint;
  JasperViewer jasperViewer;
  String path = "src\\main\\java\\Reports\\Inventory.jasper";
     
  try {
   jasperReport = (JasperReport) JRLoader.loadObjectFromFile(path);
   jasperPrint = JasperFillManager.fillReport(jasperReport, null, conexionBD());
   jasperViewer = new JasperViewer(jasperPrint, false);
   jasperViewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   jasperViewer.setVisible(true);
  } catch(ClassNotFoundException | SQLException | JRException ex) {
   JOptionPane.showMessageDialog(null, "Error al cargar reporte de inventario", "Error", JOptionPane.ERROR_MESSAGE);
   System.out.println("Error al cargar reporte de empleados, error: "+ex.getMessage());
  }
 }
 
 /* CRUD de ventas */
 
 public static void saveSale(Sales sales) {
  String saveSale = "insert into ventas (codigoVenta, nombreCliente, nombreProducto, precioProducto, cantidadVenta, totalVenta, fechaVenta, nombreEmpleado) values (?, ?, ?, ?, ?, ?, curdate(), ?)";
  ArrayList<String> productName = sales.getProductName();
  String getProductName = String.join(",", productName);
  
  ArrayList<Double> productPrice = sales.getProductPrice();
  String getproductPrice = String.join(", ", productPrice.toString());
  
  try {
   preparedStatement = connection.prepareStatement(saveSale);
   preparedStatement.setString(1, sales.getSaleCode());
   preparedStatement.setString(2, sales.getClientName());
   preparedStatement.setString(3, getProductName);
   preparedStatement.setString(4, getproductPrice);
   preparedStatement.setInt(5, sales.getSaleQuantity());
   preparedStatement.setDouble(6, sales.getTotalSale());
   preparedStatement.setString(7, sales.getEmployeeName());
   preparedStatement.executeUpdate();
   JOptionPane.showMessageDialog(null, "Venta registrada exitosamente", "Producto registrado", JOptionPane.INFORMATION_MESSAGE);
  } catch(SQLException ex) {
   JOptionPane.showMessageDialog(null, "Error al registrar venta", "Error", JOptionPane.ERROR_MESSAGE);
      System.out.println("Error al registrar venta, error: "+ex.getMessage());
  }
 }
 
 public static void generateSalesReport(){
  JasperReport jasperReport;
  JasperPrint jasperPrint;
  JasperViewer jasperViewer;
  String path = "src\\main\\java\\Reports\\Sales.jasper";
     
  try {
   jasperReport = (JasperReport) JRLoader.loadObjectFromFile(path);
   jasperPrint = JasperFillManager.fillReport(jasperReport, null, conexionBD());
   jasperViewer = new JasperViewer(jasperPrint, false);
   jasperViewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   jasperViewer.setVisible(true);
  } catch(ClassNotFoundException | SQLException | JRException ex) {
   JOptionPane.showMessageDialog(null, "Error al cargar reporte de ventas", "Error", JOptionPane.ERROR_MESSAGE);
   System.out.println("Error al cargar reporte de empleados, error: "+ex.getMessage());
  }
 }
}