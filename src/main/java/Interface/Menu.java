package Interface;

import Classes.Clients;
import Classes.Employees;
import Classes.Inventory;
import Classes.Sales;
import Config.ConexionBD;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public final class Menu extends javax.swing.JFrame { 
 Connection connection;
 Statement statement;
 ResultSet resultSet;
 
 Clients clients;
 Employees employees;
 Inventory inventory;
 Sales sales;
 
 ArrayList<String> productsName = new ArrayList<>();
 ArrayList<Double> productsPrice = new ArrayList<>();
         
public Menu() throws ClassNotFoundException, SQLException {
 initComponents();
 connection = ConexionBD.conexionBD();
 showClients();
 showEmployees();
 showInventory();
 showSales();
 hour();
 
 lbl_date.setText(date());
 lbl_User.setVisible(false);
 
 UIManager.put("OptionPane.yesButtonText", "Sí");
 UIManager.put("OptionPane.noButtonText", "No");
 
 setImageButton(btn_searchClient, "/Icons/search.png");
 setImageButton(btn_clientReport, "/Icons/print.png");
 setImageButton(btn_searchEmployee, "/Icons/search.png");
 setImageButton(btn_employeeReport, "/Icons/print.png");
 setImageButton(btn_searchProduct, "/Icons/search.png");
 setImageButton(btn_productReport, "/Icons/print.png");
 setImageButton(btn_searchSales, "/Icons/search.png");
 setImageButton(btn_salesReport, "/Icons/print.png");

 setLocationRelativeTo(null);
 setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
}

public void setImageLabel(JLabel jLabel, String root) {
 ImageIcon image = new ImageIcon(getClass().getResource(root));
 Icon icon = new ImageIcon(image.getImage().getScaledInstance(jLabel.getWidth(), jLabel.getHeight(), Image.SCALE_DEFAULT));
 jLabel.setIcon(icon);
 this.repaint();
}

public void setImageButton(JButton jButton, String root) {
 ImageIcon image = new ImageIcon(getClass().getResource(root));
 Icon icon = new ImageIcon(image.getImage().getScaledInstance(jButton.getWidth()/2, jButton.getHeight()/2, Image.SCALE_DEFAULT));
 jButton.setIcon(icon);
 this.repaint();
}

private static String hours() {
 Date date = new Date();
 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a");
 
 return simpleDateFormat.format(date);
}

private void hour() {
 Timer timer;
 
 timer = new Timer(1000, (ActionEvent evt) -> {
  lbl_hour.setText(hours());
 });
 
 timer.start();
}

private static String date() {
 Date date = new Date();
 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
 
 return simpleDateFormat.format(date);
}

private void closeWindow() {
 int choice = JOptionPane.showConfirmDialog(this, "¿Estás seguro/a de que quieres salir?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
     
 if (choice == JOptionPane.YES_OPTION) {
  this.setVisible(false);
  Login login = new Login();
  login.setVisible(true);
 }
}

/* CRUD de clientes */

public void showClients() {
 String[] titles = {"Código", "Nombre de cliente", "DUI"};
 String[] clientsData = new String[3];
 DefaultTableModel defaultTableModel = new DefaultTableModel(null, titles);
 String clientsQuery = "select idCliente, concat(nombreCliente, ' ', apellidoCliente) as 'nombreCliente', DUI from clientes";
 
 try {
  statement = connection.createStatement();
  resultSet = statement.executeQuery(clientsQuery);
  
  while(resultSet.next()) {
   clientsData[0] = resultSet.getString("idCliente");
   clientsData[1] = resultSet.getString("nombreCliente");
   clientsData[2] = resultSet.getString("DUI");
   
   defaultTableModel.addRow(clientsData);
   int lastId = Integer.parseInt(resultSet.getString("idCliente"))+1;
   txtClientId.setText(""+lastId);
  }
  
  tbl_clients.setModel(defaultTableModel);
 } catch(SQLException ex) {
  System.out.println("Error al mostrar datos de cliente, error: "+ex.getMessage());
 } finally {
  ConexionBD.closeResources(null, statement, resultSet);
 }
}

public Clients setClients() {
 String clientName = txtClientName.getText();
 String clientLastName = txtClientLastName.getText();
 String DUI = txtDUIClient.getText();
 
 clients = new Clients(0, clientName, clientLastName, DUI);
 
 return clients;
}

private void deleteClients() {
 try {
  int selectedRow = tbl_clients.getSelectedRow();
  int clientId = (int) tbl_clients.getValueAt(selectedRow, 0);
  String deleteClient = "delete from clientes where idCliente = ?";
  
  try (PreparedStatement preparedStatement = connection.prepareStatement(deleteClient)) {
   preparedStatement.setInt(1, clientId);
   int delete = preparedStatement.executeUpdate();
   
   if(delete >= 0) {
    JOptionPane.showMessageDialog(null, "Cliente eliminado del registro", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
   }
  }
 } catch (SQLException ex) {
  JOptionPane.showMessageDialog(null, "Error al eliminar cliente del registro", "Error", JOptionPane.ERROR_MESSAGE);
  System.out.println("Error al eliminar cliente, error: " + ex.getMessage());
 } finally {
  ConexionBD.closeResources(connection, null, null);
 }
}

private void searchClients() {
 String[] titles = {"Código", "Nombre de cliente", "DUI"};
 String[] clientsData = new String[3];
 DefaultTableModel defaultTableModel = new DefaultTableModel(null, titles);
 String clientsQuery = "select idCliente, concat(nombreCliente, ' ', apellidoCliente) as 'nombreCliente', DUI from clientes where nombreCliente like '%"+txtSearchClient.getText()+"%'";
 
 try {
  statement = connection.createStatement();
  resultSet = statement.executeQuery(clientsQuery);
  
  while(resultSet.next()) {
   clientsData[0] = resultSet.getString("idCliente");
   clientsData[1] = resultSet.getString("nombreCliente");
   clientsData[2] = resultSet.getString("DUI");
   
   defaultTableModel.addRow(clientsData);
  }
  
  tbl_clients.setModel(defaultTableModel);
 } catch(SQLException ex) {
  System.out.println("Error al mostrar datos de cliente, error: "+ex.getMessage());
 } finally {
  ConexionBD.closeResources(null, statement, resultSet);
 }
}

public void clearClientsData() {
 txtClientName.setText("");
 txtClientLastName.setText("");
 txtDUIClient.setText("");
 txtSearchClient.setText("");
}

/* CRUD de empleados */

public void showEmployees() {
 String[] titles = {"Código", "Nombre de empleado", "DUI"};
 String[] employeesData = new String[3];
 DefaultTableModel defaultTableModel = new DefaultTableModel(null, titles);
 String employeesQuery = "select idEmpleado, concat(nombreEmpleado, ' ', apellidoEmpleado) as 'nombreEmpleado', DUI from empleados";
 
 try {
  statement = connection.createStatement();
  resultSet = statement.executeQuery(employeesQuery);
  
  while(resultSet.next()) {
   employeesData[0] = resultSet.getString("idEmpleado");
   employeesData[1] = resultSet.getString("nombreEmpleado");
   employeesData[2] = resultSet.getString("DUI");
   
   defaultTableModel.addRow(employeesData);
   int lastId = Integer.parseInt(resultSet.getString("idEmpleado"))+1;
   txtEmployeeId.setText(""+lastId);
  }
  
  tbl_employees.setModel(defaultTableModel);
 } catch(SQLException ex) {
  System.out.println("Error al mostrar datos de empleado, error: "+ex.getMessage());
 } finally {
  ConexionBD.closeResources(null, statement, resultSet);
 }
}

public Employees setEmployees() {
 String employeeName = txtEmployeeName.getText();
 String employeeLastName = txtEmployeeLastName.getText();
 String DUI = txtEmployeeDUI.getText();
 String[] userName = txtEmployeeName.getText().split(" ");
 String[] userLastName = txtEmployeeLastName.getText().split(" ");
 String user = userName[0]+" "+userLastName[0];
 int userType = 0;
 
 if(rbtn_administrator.isSelected()) {
  userType = 2;
 } else {
  userType = 1;
 }
 
 employees = new Employees(0, employeeName, employeeLastName, DUI, user, userType);
 
 return employees;
}

private void deleteEmployees() {
 try {
  int selectedRow = tbl_employees.getSelectedRow();
  int employeeId = (int) tbl_employees.getValueAt(selectedRow, 0);
  String deleteEmployee = "delete from empleados where idEmpleado = ?";
  
  try (PreparedStatement preparedStatement = connection.prepareStatement(deleteEmployee)) {
   preparedStatement.setInt(1, employeeId);
   int delete = preparedStatement.executeUpdate();
   
   if(delete >= 0) {
    JOptionPane.showMessageDialog(null, "Empleado eliminado del registro", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
   }
  }
  ConexionBD.closeResources(connection, null, null);
 } catch (SQLException ex) {
  JOptionPane.showMessageDialog(null, "Error al eliminar empleado del registro", "Error", JOptionPane.ERROR_MESSAGE);
  System.out.println("Error al eliminar empleado, error: " + ex.getMessage());
 }
}

public void searchEmployees() {
 String[] titles = {"Código", "Nombre de empleado", "DUI"};
 String[] employeesData = new String[3];
 DefaultTableModel defaultTableModel = new DefaultTableModel(null, titles);
 String employeesQuery = "select idEmpleado, concat(nombreEmpleado, ' ', apellidoEmpleado) as 'nombreEmpleado', DUI from empleados where nombreEmpleado like '%"+txtSearchEmployee.getText()+"%'";
 
 try {
  statement = connection.createStatement();
  resultSet = statement.executeQuery(employeesQuery);
  
  while(resultSet.next()) {
   employeesData[0] = resultSet.getString("idEmpleado");
   employeesData[1] = resultSet.getString("nombreEmpleado");
   employeesData[2] = resultSet.getString("DUI");
   
   defaultTableModel.addRow(employeesData);
  }
  
  tbl_employees.setModel(defaultTableModel);
 } catch(SQLException ex) {
  System.out.println("Error al mostrar datos de empleado, error: "+ex.getMessage());
 } finally {
  ConexionBD.closeResources(null, statement, resultSet);
 }
}

public void clearEmployeesData() {
 txtEmployeeName.setText("");
 txtEmployeeLastName.setText("");
 txtEmployeeDUI.setText("");
 txtSearchEmployee.setText("");
}

/* CRUD de productos */

public void showInventory() {
 String[] titles = {"", "Código", "Producto", "Precio", "Proveedor", "Material", "Stock"};
 String[] productsData = new String[7];
 DefaultTableModel defaultTableModel = new DefaultTableModel(null, titles);
 String productsQuery = "select idProducto, codigoProducto, nombreProducto, precioProducto, nombreProveedor, materialProducto, stockProducto from productos";
 
 try {
  statement = connection.createStatement();
  resultSet = statement.executeQuery(productsQuery);
  
  Set<String> productsNames = new HashSet<>();
  
  while(resultSet.next()) {
   productsData[0] = resultSet.getString("idProducto");
   productsData[1] = resultSet.getString("codigoProducto");
   productsData[2] = resultSet.getString("nombreProducto");
   productsData[3] = "$" + resultSet.getString("precioProducto");
   productsData[4] = resultSet.getString("nombreProveedor");
   productsData[5] = resultSet.getString("materialProducto");
   productsData[6] = resultSet.getString("stockProducto");
   
   defaultTableModel.addRow(productsData);
   int lastId = Integer.parseInt(resultSet.getString("idProducto"))+1;
   txtProductId.setText(""+lastId);
   
  productsNames.add(resultSet.getString("nombreProducto"));
  }
  
  productsNames.forEach(productName -> {
   cbSalesProductName.addItem(productName);
  });
  
  tbl_products.setModel(defaultTableModel);
  tbl_products.getColumnModel().getColumn(0).setMinWidth(0);
  tbl_products.getColumnModel().getColumn(0).setMaxWidth(0);
 } catch(SQLException ex) {
  System.out.println("Error al mostrar datos de inventario, error: "+ex.getMessage());
 } finally {
  ConexionBD.closeResources(null, statement, resultSet);
 }
}

public Inventory setInventory() {
 String productCode = txtProductCode.getText();
 String productName = txtProductName.getText();
 double productPrice = Double.parseDouble(txtProductPrice.getText());
 String provideerName = txtProvideerName.getText();
 String productMaterial = txtMaterial.getText();
 int productStock = Integer.parseInt(txtStock.getText());
 
 inventory = new Inventory(0, productCode, productName, productPrice, provideerName, productMaterial, productStock);
 
 return inventory;
}

private void deleteInventory() {
 try {
  int selectedRow = tbl_products.getSelectedRow();
  int productId = (int) tbl_products.getValueAt(selectedRow, 0);
  String deleteInventory = "delete from productos where codigoProducto = ?";
  
  try (PreparedStatement preparedStatement = connection.prepareStatement(deleteInventory)) {
   preparedStatement.setInt(1, productId);
   int delete = preparedStatement.executeUpdate();
   
   if(delete >= 0) {
    JOptionPane.showMessageDialog(null, "Producto eliminado del registro", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
   }
  }
  ConexionBD.closeResources(connection, null, null);
 } catch (SQLException ex) {
  JOptionPane.showMessageDialog(null, "Error al eliminar producto del registro", "Error", JOptionPane.ERROR_MESSAGE);
  System.out.println("Error al eliminar producto, error: " + ex.getMessage());
 }
}

public void searchInventory() {
 String[] titles = {"", "Código", "Producto", "Precio", "Proveedor", "Material", "Stock"};
 String[] productsData = new String[7];
 DefaultTableModel defaultTableModel = new DefaultTableModel(null, titles);
 String productsQuery = "select idProducto, codigoProducto, nombreProducto, precioProducto, nombreProveedor, materialProducto, stockProducto from productos where nombreProducto like '%"+txtSearchProduct.getText()+"%'";
 
 try {
  statement = connection.createStatement();
  resultSet = statement.executeQuery(productsQuery);
  
  while(resultSet.next()) {
   productsData[0] = resultSet.getString("idProducto");
   productsData[1] = resultSet.getString("codigoProducto");
   productsData[2] = resultSet.getString("nombreProducto");
   productsData[3] = "$" + resultSet.getString("precioProducto");
   productsData[4] = resultSet.getString("nombreProveedor");
   productsData[5] = resultSet.getString("materialProducto");
   productsData[6] = resultSet.getString("stockProducto");
   
   defaultTableModel.addRow(productsData);
  }
  
  tbl_products.setModel(defaultTableModel);
  tbl_products.getColumnModel().getColumn(0).setMinWidth(0);
  tbl_products.getColumnModel().getColumn(0).setMaxWidth(0);
 } catch(SQLException ex) {
  System.out.println("Error al mostrar datos de empleado, error: "+ex.getMessage());
 } finally {
  ConexionBD.closeResources(null, statement, resultSet);
 }
}

public void clearInventoryData() {
 txtProductCode.setText("");
 txtProductName.setText("");
 txtProvideerName.setText("");
 txtMaterial.setText("");
 txtProductPrice.setText("");
}

/* CRUD de ventas */

public void showSales() {
 String[] titles = {"", "Código", "Cliente", "Producto", "Precio", "Cantidad", "Total", "Fecha de Venta", "Facturado"};
 String[] salesData = new String[9];
 DefaultTableModel defaultTableModel = new DefaultTableModel(null, titles);
 String salesQuery = "select idVenta, codigoVenta, nombreCliente, nombreProducto, precioProducto, cantidadVenta, totalVenta, fechaVenta, nombreEmpleado from ventas";
 
 try {
  statement = connection.createStatement();
  resultSet = statement.executeQuery(salesQuery);
  
  Set<String> clientsName = new HashSet<>();
  
  while(resultSet.next()) {
   salesData[0] = resultSet.getString("idVenta");
   salesData[1] = resultSet.getString("codigoVenta");
   salesData[2] = resultSet.getString("nombreCliente");
   salesData[3] = resultSet.getString("nombreProducto");
   salesData[4] = "$" + resultSet.getString("precioProducto");
   salesData[5] = resultSet.getString("cantidadVenta");
   salesData[6] = "$" + resultSet.getString("totalVenta");
   salesData[7] = resultSet.getString("fechaVenta");
   salesData[8] = resultSet.getString("nombreEmpleado");
   
   defaultTableModel.addRow(salesData);
   int lastId = Integer.parseInt(resultSet.getString("idVenta"))+1;
   txtSalesId.setText(""+lastId);
   
   clientsName.add(resultSet.getString("nombreCliente"));
  }
  
  clientsName.forEach(clientName -> {
   cbClientName.addItem(clientName);
  });
  
  getClientDUI();
  
  tbl_sales.setModel(defaultTableModel);
  tbl_sales.getColumnModel().getColumn(0).setMinWidth(0);
  tbl_sales.getColumnModel().getColumn(0).setMaxWidth(0);
 } catch(SQLException ex) {
  System.out.println("Error al mostrar datos de ventas, error: "+ex.getMessage());
 } finally {
  ConexionBD.closeResources(null, statement, resultSet);
 }
}

public Sales setSales() {
 String saleCode = String.format("%010d", Integer.parseInt(txtSalesId.getText()));
 String clientName = cbClientName.getSelectedItem().toString();
 String productName = cbSalesProductName.getSelectedItem().toString();
 double productPrice = Double.parseDouble(txtSalesPrice.getText());
 int saleQuantity = Integer.parseInt(txtSalesQuantity.getText());
 double totalSale = calculateTotalSale(txtSalesQuantity.getText(), txtSalesPrice.getText());
 String saleDate = "";
 String employeeName = lbl_username.getText();
 
 sales = new Sales(0, saleCode, clientName, productsName, productsPrice, saleQuantity, totalSale, saleDate, employeeName);
 
 return sales;
}

private void deleteSale() {
 try {
  int selectedRow = tbl_sales.getSelectedRow();
  int saleId = (int) tbl_sales.getValueAt(selectedRow, 0);
  String deleteSale = "delete from productos where codigoProducto = ?";
  
  try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSale)) {
   preparedStatement.setInt(1, saleId);
   int delete = preparedStatement.executeUpdate();
   
   if(delete >= 0) {
    JOptionPane.showMessageDialog(null, "Venta eliminada del registro", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
   }
  }
  ConexionBD.closeResources(connection, null, null);
 } catch (SQLException ex) {
  JOptionPane.showMessageDialog(null, "Error al eliminar venta del registro", "Error", JOptionPane.ERROR_MESSAGE);
  System.out.println("Error al eliminar venta, error: " + ex.getMessage());
 }
}

public void searchSales() {
 String[] titles = {"", "Código", "Cliente", "Producto", "Precio", "Cantidad", "Total", "Fecha de Venta", "Facturado"};
 String[] salesData = new String[9];
 DefaultTableModel defaultTableModel = new DefaultTableModel(null, titles);
 String parameter = null;
 
 parameter = (cbFilter.getSelectedItem() == "Cliente") ? "nombreCliente" : (cbFilter.getSelectedItem() == "Producto") ? "nombreProducto" : 
 (cbFilter.getSelectedItem() == "Empleado") ? "nombreEmpleado" : (cbFilter.getSelectedItem() == "Fecha") ? "fechaVenta" : null;
 
 String salesQuery = "select idVenta, codigoVenta, nombreCliente, nombreProducto, precioProducto, cantidadVenta, totalVenta, fechaVenta, nombreEmpleado from ventas where "+parameter+" like '%"+txtSearchSales.getText()+"%'";
 
 try {
  statement = connection.createStatement();
  resultSet = statement.executeQuery(salesQuery);
  
  Set<String> clientsName = new HashSet<>();
  
  while(resultSet.next()) {
   salesData[0] = resultSet.getString("idVenta");
   salesData[1] = resultSet.getString("codigoVenta");
   salesData[2] = resultSet.getString("nombreCliente");
   salesData[3] = resultSet.getString("nombreProducto");
   salesData[4] = "$" + resultSet.getString("precioProducto");
   salesData[5] = resultSet.getString("cantidadVenta");
   salesData[6] = "$" + resultSet.getString("totalVenta");
   salesData[7] = resultSet.getString("fechaVenta");
   salesData[8] = resultSet.getString("nombreEmpleado");
   
   defaultTableModel.addRow(salesData);
   cbClientName.removeAllItems();
   clientsName.add(resultSet.getString("nombreCliente"));
  }
  
  clientsName.forEach(clientName -> {
   cbClientName.addItem(clientName);
  });
  
  tbl_sales.setModel(defaultTableModel);
  tbl_sales.getColumnModel().getColumn(0).setMinWidth(0);
  tbl_sales.getColumnModel().getColumn(0).setMaxWidth(0);
 } catch(SQLException ex) {
  System.out.println("Error al mostrar datos de ventas, error: "+ex.getMessage());
 } finally {
  ConexionBD.closeResources(null, statement, resultSet);
 }
}

public void getClientDUI() {
 String getClientDUI = "select DUI from clientes where concat(nombreCliente, ' ', apellidoCliente) = '"+cbClientName.getSelectedItem().toString()+"'";
 
 try {
  statement = connection.createStatement();
  resultSet = statement.executeQuery(getClientDUI);
  
  if(resultSet.next()) {
   txtSalesClientDUI.setText(resultSet.getString("dui"));
  }
 } catch(SQLException ex) {
  System.out.println("Error al buscar DUI, error: "+ex.getMessage());
 }
}

public void printAndSaveSale() {
 SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
 String fileName = dateFormat.format(new Date());
 String desktop = System.getProperty("user.home") + File.separator + "Desktop";
 File file = new File(desktop, fileName + ".pdf");
 
 try {
  FileOutputStream outputStream = new FileOutputStream(file);
  Document document = new Document();
  PdfWriter writer = PdfWriter.getInstance(document, outputStream);
  
  document.open();
  PdfContentByte pdfContentByte = writer.getDirectContent();
  PdfTemplate pdfTemplate = pdfContentByte.createTemplate(PageSize.A4.getWidth(), PageSize.A4.getHeight());
  Graphics2D graphics2D = pdfTemplate.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight());

  //Imprimir el contenido del panel en el gráfico
  pnl_ticket.printAll(graphics2D);
  graphics2D.dispose();
  pdfContentByte.addTemplate(pdfTemplate, 0, 0);
  
  document.close();
  outputStream.close();
  
  Sales newSale = null;
  newSale = setSales();
  ConexionBD.saveSale(newSale);
  clearSaleData();
  showSales();
 } catch (DocumentException | HeadlessException | IOException ex) {
  JOptionPane.showMessageDialog(null, "Error al imprimir ticket, error: " + ex.getMessage(), "Error de impresión", JOptionPane.ERROR_MESSAGE);
  System.out.println("Error al imprimir ticket, error: " + ex.getMessage());
 }
}

public Double calculateTotalSale(String quantity, String productPrice) {
 int quantityInt = Integer.parseInt(quantity);
 double productPriceDouble = Double.parseDouble(productPrice);
 
 return quantityInt*productPriceDouble;
}

public void clearSaleData() {
 txtSalesId.setText("");
 txtSalesClientDUI.setText("        - ");
 cbSalesProductName.setSelectedItem(0);
 txtSalesQuantity.setText("");
 txtSalesPrice.setText("");
 txtTotalSale.setText("");
 
 lbl_ClientNameSale.setText(cbClientName.getSelectedItem().toString());
 lbl_ClientDUISale.setText("0000xxxx-x");
 lbl_SaleDate.setText("00/00/0000");
 lbl_SalesProductName.setText("Nombre de producto");
 lbl_SalesQuantity.setText("0");
 lbl_SalesPrice.setText("$0.00");
 lbl_TotalSale.setText("$0.00");
}

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_salir = new javax.swing.JButton();
        btn_clientes = new javax.swing.JButton();
        btn_empleados = new javax.swing.JButton();
        btn_inventario = new javax.swing.JButton();
        btn_ayuda = new javax.swing.JButton();
        btn_ventas = new javax.swing.JButton();
        lbl_role = new javax.swing.JLabel();
        lbl_username = new javax.swing.JLabel();
        lbl_logo = new javax.swing.JLabel();
        pnl_menu = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lbl_hour = new javax.swing.JLabel();
        lbl_date = new javax.swing.JLabel();
        lbl_principal = new javax.swing.JLabel();
        pnl_clientes = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_clients = new javax.swing.JTable();
        btn_deleteClient = new javax.swing.JButton();
        btn_saveClient = new javax.swing.JButton();
        btn_cancelClient = new javax.swing.JButton();
        btn_editClient = new javax.swing.JButton();
        txtClientId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtClientName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtClientLastName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtSearchClient = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btn_searchClient = new javax.swing.JButton();
        txtDUIClient = new javax.swing.JFormattedTextField();
        btn_clientReport = new javax.swing.JButton();
        pnl_empleados = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_employees = new javax.swing.JTable();
        btn_deleteEmployee = new javax.swing.JButton();
        btn_saveEmployee = new javax.swing.JButton();
        btn_cancelEmployee = new javax.swing.JButton();
        btn_editEmployee = new javax.swing.JButton();
        txtEmployeeId = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtEmployeeName = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtEmployeeLastName = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtSearchEmployee = new javax.swing.JTextField();
        btn_searchEmployee = new javax.swing.JButton();
        txtEmployeeDUI = new javax.swing.JFormattedTextField();
        btn_employeeReport = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        rbtn_user = new javax.swing.JRadioButton();
        rbtn_administrator = new javax.swing.JRadioButton();
        lbl_User = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        pnl_inventario = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_products = new javax.swing.JTable();
        btn_deleteProduct = new javax.swing.JButton();
        btn_saveProduct = new javax.swing.JButton();
        btn_cancelProduct = new javax.swing.JButton();
        btn_editProduct = new javax.swing.JButton();
        txtProductId = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtProvideerName = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtSearchProduct = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        btn_searchProduct = new javax.swing.JButton();
        btn_productReport = new javax.swing.JButton();
        txtMaterial = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txtProductPrice = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        pnl_ventas = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_sales = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        pnl_ticket = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        lbl_ClientNameSale = new javax.swing.JLabel();
        lbl_ClientDUISale = new javax.swing.JLabel();
        lbl_SaleDate = new javax.swing.JLabel();
        lbl_SalesProductName = new javax.swing.JLabel();
        lbl_SalesPrice = new javax.swing.JLabel();
        lbl_TotalSale = new javax.swing.JLabel();
        lbl_SalesQuantity = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        btn_deleteSale = new javax.swing.JButton();
        btn_saveSale = new javax.swing.JButton();
        btn_cancelSale = new javax.swing.JButton();
        btn_editSale = new javax.swing.JButton();
        txtSalesId = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtSearchSales = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        btn_searchSales = new javax.swing.JButton();
        txtSalesClientDUI = new javax.swing.JFormattedTextField();
        btn_salesReport = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtSalesQuantity = new javax.swing.JTextField();
        txtSalesPrice = new javax.swing.JTextField();
        cbFilter = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        txtTotalSale = new javax.swing.JTextField();
        cbClientName = new javax.swing.JComboBox<>();
        btn_salesAddProduct = new javax.swing.JButton();
        cbSalesProductName = new javax.swing.JComboBox<>();
        pnl_ayuda = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        lbl_clients = new javax.swing.JLabel();
        lbl_menu = new javax.swing.JLabel();
        lbl_employees = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        lbl_inventory = new javax.swing.JLabel();
        lbl_sales = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        lbl_login = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        pnl_salesData = new javax.swing.JTextPane();
        jLabel56 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        pnl_inventoryData = new javax.swing.JTextPane();
        jLabel57 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        pnl_employeesData = new javax.swing.JTextPane();
        jLabel58 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        pnl_clientsData = new javax.swing.JTextPane();
        jLabel59 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        pnl_menuData = new javax.swing.JTextPane();
        lbl_loginData = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_salir.setForeground(new java.awt.Color(0, 0, 0));
        btn_salir.setText("Salir");
        btn_salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salirActionPerformed(evt);
            }
        });
        getContentPane().add(btn_salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, 130, 50));

        btn_clientes.setForeground(new java.awt.Color(0, 0, 0));
        btn_clientes.setText("Clientes");
        btn_clientes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_clientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clientesActionPerformed(evt);
            }
        });
        getContentPane().add(btn_clientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 130, 50));

        btn_empleados.setForeground(new java.awt.Color(0, 0, 0));
        btn_empleados.setText("Empleados");
        btn_empleados.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_empleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_empleadosActionPerformed(evt);
            }
        });
        getContentPane().add(btn_empleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 130, 50));

        btn_inventario.setForeground(new java.awt.Color(0, 0, 0));
        btn_inventario.setText("Inventario");
        btn_inventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_inventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_inventarioActionPerformed(evt);
            }
        });
        getContentPane().add(btn_inventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 130, 50));

        btn_ayuda.setForeground(new java.awt.Color(0, 0, 0));
        btn_ayuda.setText("Ayuda");
        btn_ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ayuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ayudaActionPerformed(evt);
            }
        });
        getContentPane().add(btn_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 130, 50));

        btn_ventas.setForeground(new java.awt.Color(0, 0, 0));
        btn_ventas.setText("Ventas");
        btn_ventas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ventas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ventasActionPerformed(evt);
            }
        });
        getContentPane().add(btn_ventas, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 130, 50));

        lbl_role.setForeground(new java.awt.Color(255, 255, 255));
        lbl_role.setText("Rol: Administrador");
        getContentPane().add(lbl_role, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 130, -1));

        lbl_username.setForeground(new java.awt.Color(255, 255, 255));
        lbl_username.setText("David Cruz");
        getContentPane().add(lbl_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 140, -1));

        lbl_logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/admin.png"))); // NOI18N
        lbl_logo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_logo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbl_logo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_logoMouseClicked(evt);
            }
        });
        getContentPane().add(lbl_logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 130, 110));

        pnl_menu.setBackground(new java.awt.Color(255, 255, 255));
        pnl_menu.setForeground(new java.awt.Color(255, 255, 255));
        pnl_menu.setToolTipText("");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Mueblería \"El Serrucho\"");
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Hour:");

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Date:");

        lbl_hour.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lbl_hour.setForeground(new java.awt.Color(0, 0, 0));
        lbl_hour.setText("00:00");

        lbl_date.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lbl_date.setForeground(new java.awt.Color(0, 0, 0));
        lbl_date.setText("00/00/0000");

        lbl_principal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_principal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/principal.png"))); // NOI18N

        javax.swing.GroupLayout pnl_menuLayout = new javax.swing.GroupLayout(pnl_menu);
        pnl_menu.setLayout(pnl_menuLayout);
        pnl_menuLayout.setHorizontalGroup(
            pnl_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_menuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_principal, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_menuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnl_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbl_hour, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(4, 4, 4)))
                .addContainerGap())
        );
        pnl_menuLayout.setVerticalGroup(
            pnl_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_menuLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbl_principal, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addGroup(pnl_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lbl_hour))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lbl_date))
                .addContainerGap())
        );

        getContentPane().add(pnl_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 750, 560));

        pnl_clientes.setBackground(new java.awt.Color(255, 255, 255));
        pnl_clientes.setForeground(new java.awt.Color(255, 255, 255));
        pnl_clientes.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Clientes");

        jTabbedPane1.setForeground(new java.awt.Color(0, 0, 0));

        tbl_clients.setForeground(new java.awt.Color(0, 0, 0));
        tbl_clients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_clients.setFocusable(false);
        tbl_clients = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        jScrollPane1.setViewportView(tbl_clients);

        jTabbedPane1.addTab("Clientes", jScrollPane1);

        btn_deleteClient.setForeground(new java.awt.Color(0, 0, 0));
        btn_deleteClient.setText("Eliminar");
        btn_deleteClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_deleteClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteClientActionPerformed(evt);
            }
        });

        btn_saveClient.setForeground(new java.awt.Color(0, 0, 0));
        btn_saveClient.setText("Guardar");
        btn_saveClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_saveClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveClientActionPerformed(evt);
            }
        });

        btn_cancelClient.setForeground(new java.awt.Color(0, 0, 0));
        btn_cancelClient.setText("Cancelar");
        btn_cancelClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cancelClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelClientActionPerformed(evt);
            }
        });

        btn_editClient.setForeground(new java.awt.Color(0, 0, 0));
        btn_editClient.setText("Editar");
        btn_editClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_editClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editClientActionPerformed(evt);
            }
        });

        txtClientId.setEditable(false);
        txtClientId.setForeground(new java.awt.Color(0, 0, 0));
        txtClientId.setEnabled(false);

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Código de cliente:");

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Nombre de cliente:");

        txtClientName.setForeground(new java.awt.Color(0, 0, 0));

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Apellido de cliente:");

        txtClientLastName.setForeground(new java.awt.Color(0, 0, 0));

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("DUI de cliente:");

        txtSearchClient.setForeground(new java.awt.Color(0, 0, 0));
        txtSearchClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchClientKeyReleased(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Buscar cliente:");

        btn_searchClient.setForeground(new java.awt.Color(0, 0, 0));
        btn_searchClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search.png"))); // NOI18N
        btn_searchClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_searchClient.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_searchClient.setMargin(new java.awt.Insets(0, 14, 2, 14));
        btn_searchClient.setMaximumSize(new java.awt.Dimension(72, 24));
        btn_searchClient.setMinimumSize(new java.awt.Dimension(72, 24));
        btn_searchClient.setPreferredSize(new java.awt.Dimension(72, 24));
        btn_searchClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchClientActionPerformed(evt);
            }
        });

        txtDUIClient.setFormatterFactory(new javax.swing.JFormattedTextField.AbstractFormatterFactory() {
            public javax.swing.JFormattedTextField.AbstractFormatter
            getFormatter(javax.swing.JFormattedTextField textFormater) {
                try {
                    return new javax.swing.text.MaskFormatter("########-#");
                } catch(java.text.ParseException ex) {
                    System.out.println("Error al formatear DUI de cliente, error: "+ex.getMessage());
                }

                return null;
            }
        });

        btn_clientReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_clientReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clientReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_clientesLayout = new javax.swing.GroupLayout(pnl_clientes);
        pnl_clientes.setLayout(pnl_clientesLayout);
        pnl_clientesLayout.setHorizontalGroup(
            pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_clientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_clientesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtClientLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_clientesLayout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addComponent(btn_deleteClient, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addComponent(btn_editClient, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl_clientesLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtClientId, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_clientesLayout.createSequentialGroup()
                        .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnl_clientesLayout.createSequentialGroup()
                                .addComponent(txtSearchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_searchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_clientReport, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(367, 367, 367))
                            .addGroup(pnl_clientesLayout.createSequentialGroup()
                                .addComponent(txtClientName, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addGap(31, 31, 31)
                                .addComponent(txtDUIClient, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_clientesLayout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(btn_saveClient, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(620, Short.MAX_VALUE)))
            .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_clientesLayout.createSequentialGroup()
                    .addContainerGap(620, Short.MAX_VALUE)
                    .addComponent(btn_cancelClient, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(16, 16, 16)))
        );
        pnl_clientesLayout.setVerticalGroup(
            pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_clientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(txtClientLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txtClientId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7)
                    .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtClientName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDUIClient))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_searchClient, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearchClient, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clientReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_deleteClient, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_editClient, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_clientesLayout.createSequentialGroup()
                    .addContainerGap(516, Short.MAX_VALUE)
                    .addComponent(btn_saveClient, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(pnl_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_clientesLayout.createSequentialGroup()
                    .addContainerGap(516, Short.MAX_VALUE)
                    .addComponent(btn_cancelClient, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        getContentPane().add(pnl_clientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 750, 560));

        pnl_empleados.setBackground(new java.awt.Color(255, 255, 255));
        pnl_empleados.setForeground(new java.awt.Color(255, 255, 255));
        pnl_empleados.setToolTipText("");

        jTabbedPane2.setForeground(new java.awt.Color(0, 0, 0));

        tbl_employees.setForeground(new java.awt.Color(0, 0, 0));
        tbl_employees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_employees.setFocusable(false);
        tbl_employees = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        jScrollPane2.setViewportView(tbl_employees);

        jTabbedPane2.addTab("Empleados", jScrollPane2);

        btn_deleteEmployee.setForeground(new java.awt.Color(0, 0, 0));
        btn_deleteEmployee.setText("Eliminar");
        btn_deleteEmployee.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_deleteEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteEmployeeActionPerformed(evt);
            }
        });

        btn_saveEmployee.setForeground(new java.awt.Color(0, 0, 0));
        btn_saveEmployee.setText("Guardar");
        btn_saveEmployee.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_saveEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveEmployeeActionPerformed(evt);
            }
        });

        btn_cancelEmployee.setForeground(new java.awt.Color(0, 0, 0));
        btn_cancelEmployee.setText("Cancelar");
        btn_cancelEmployee.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cancelEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelEmployeeActionPerformed(evt);
            }
        });

        btn_editEmployee.setForeground(new java.awt.Color(0, 0, 0));
        btn_editEmployee.setText("Editar");
        btn_editEmployee.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_editEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editEmployeeActionPerformed(evt);
            }
        });

        txtEmployeeId.setEditable(false);
        txtEmployeeId.setForeground(new java.awt.Color(0, 0, 0));
        txtEmployeeId.setEnabled(false);

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Código de empleado:");

        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Nombre de empleado:");

        txtEmployeeName.setForeground(new java.awt.Color(0, 0, 0));

        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Apellido de empleado:");

        txtEmployeeLastName.setForeground(new java.awt.Color(0, 0, 0));

        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("DUI de empleado:");

        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("Buscar empleado:");

        txtSearchEmployee.setForeground(new java.awt.Color(0, 0, 0));
        txtSearchEmployee.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchEmployeeKeyReleased(evt);
            }
        });

        btn_searchEmployee.setForeground(new java.awt.Color(0, 0, 0));
        btn_searchEmployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search.png"))); // NOI18N
        btn_searchEmployee.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_searchEmployee.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_searchEmployee.setMargin(new java.awt.Insets(0, 14, 2, 14));
        btn_searchEmployee.setMaximumSize(new java.awt.Dimension(72, 24));
        btn_searchEmployee.setMinimumSize(new java.awt.Dimension(72, 24));
        btn_searchEmployee.setPreferredSize(new java.awt.Dimension(72, 24));
        btn_searchEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchEmployeeActionPerformed(evt);
            }
        });

        txtEmployeeDUI.setFormatterFactory(new javax.swing.JFormattedTextField.AbstractFormatterFactory() {
            public javax.swing.JFormattedTextField.AbstractFormatter
            getFormatter(javax.swing.JFormattedTextField textFormater) {
                try {
                    return new javax.swing.text.MaskFormatter("########-#");
                } catch(java.text.ParseException ex) {
                    System.out.println("Error al formatear DUI de cliente, error: "+ex.getMessage());
                }

                return null;
            }
        });

        btn_employeeReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_employeeReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_employeeReportActionPerformed(evt);
            }
        });

        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("Tipo de usuario:");

        rbtn_user.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_user.setForeground(new java.awt.Color(0, 0, 0));
        rbtn_user.setText("Usuario");
        rbtn_user.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtn_user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_userActionPerformed(evt);
            }
        });

        rbtn_administrator.setBackground(new java.awt.Color(255, 255, 255));
        rbtn_administrator.setForeground(new java.awt.Color(0, 0, 0));
        rbtn_administrator.setText("Administrador");
        rbtn_administrator.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtn_administrator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_administratorActionPerformed(evt);
            }
        });

        lbl_User.setForeground(new java.awt.Color(0, 0, 0));
        lbl_User.setText("Usuario");

        jLabel31.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 0));
        jLabel31.setText("Empleados");

        javax.swing.GroupLayout pnl_empleadosLayout = new javax.swing.GroupLayout(pnl_empleados);
        pnl_empleados.setLayout(pnl_empleadosLayout);
        pnl_empleadosLayout.setHorizontalGroup(
            pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_empleadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2)
                    .addGroup(pnl_empleadosLayout.createSequentialGroup()
                        .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_empleadosLayout.createSequentialGroup()
                                .addGap(214, 214, 214)
                                .addComponent(btn_deleteEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(94, 94, 94)
                                .addComponent(btn_editEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_empleadosLayout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(41, 41, 41)
                                .addComponent(rbtn_user)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbtn_administrator))
                            .addComponent(jLabel31)
                            .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(pnl_empleadosLayout.createSequentialGroup()
                                    .addComponent(btn_searchEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btn_employeeReport, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnl_empleadosLayout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addGap(223, 223, 223)
                                    .addComponent(lbl_User))))
                        .addGap(0, 202, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_empleadosLayout.createSequentialGroup()
                        .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_empleadosLayout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(pnl_empleadosLayout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(27, 27, 27)))
                        .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtSearchEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_empleadosLayout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(pnl_empleadosLayout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(31, 31, 31)))
                        .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmployeeDUI, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmployeeLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_empleadosLayout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(btn_saveEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(620, Short.MAX_VALUE)))
            .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_empleadosLayout.createSequentialGroup()
                    .addContainerGap(620, Short.MAX_VALUE)
                    .addComponent(btn_cancelEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(16, 16, 16)))
        );
        pnl_empleadosLayout.setVerticalGroup(
            pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_empleadosLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(txtEmployeeLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(txtEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_User)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEmployeeDUI)
                        .addComponent(jLabel15)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbtn_user, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rbtn_administrator, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btn_searchEmployee, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSearchEmployee, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16))
                    .addComponent(btn_employeeReport, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_deleteEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_editEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_empleadosLayout.createSequentialGroup()
                    .addContainerGap(516, Short.MAX_VALUE)
                    .addComponent(btn_saveEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(pnl_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_empleadosLayout.createSequentialGroup()
                    .addContainerGap(516, Short.MAX_VALUE)
                    .addComponent(btn_cancelEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        getContentPane().add(pnl_empleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 750, 560));

        pnl_inventario.setBackground(new java.awt.Color(255, 255, 255));
        pnl_inventario.setForeground(new java.awt.Color(255, 255, 255));
        pnl_inventario.setToolTipText("");

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("Inventario");

        jTabbedPane3.setForeground(new java.awt.Color(0, 0, 0));

        tbl_products.setForeground(new java.awt.Color(0, 0, 0));
        tbl_products.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_products.setFocusable(false);
        tbl_products = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        jScrollPane3.setViewportView(tbl_products);

        jTabbedPane3.addTab("Productos", jScrollPane3);

        btn_deleteProduct.setForeground(new java.awt.Color(0, 0, 0));
        btn_deleteProduct.setText("Eliminar");
        btn_deleteProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_deleteProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteProductActionPerformed(evt);
            }
        });

        btn_saveProduct.setForeground(new java.awt.Color(0, 0, 0));
        btn_saveProduct.setText("Guardar");
        btn_saveProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_saveProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveProductActionPerformed(evt);
            }
        });

        btn_cancelProduct.setForeground(new java.awt.Color(0, 0, 0));
        btn_cancelProduct.setText("Cancelar");
        btn_cancelProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cancelProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelProductActionPerformed(evt);
            }
        });

        btn_editProduct.setForeground(new java.awt.Color(0, 0, 0));
        btn_editProduct.setText("Editar");
        btn_editProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_editProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editProductActionPerformed(evt);
            }
        });

        txtProductId.setEditable(false);
        txtProductId.setForeground(new java.awt.Color(0, 0, 0));
        txtProductId.setEnabled(false);

        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("Índice de producto:");

        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("Nombre de producto:");

        txtProductName.setForeground(new java.awt.Color(0, 0, 0));

        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("Proveedor:");

        txtProvideerName.setForeground(new java.awt.Color(0, 0, 0));

        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("Material:");

        txtSearchProduct.setForeground(new java.awt.Color(0, 0, 0));
        txtSearchProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchProductKeyReleased(evt);
            }
        });

        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("Buscar producto:");

        btn_searchProduct.setForeground(new java.awt.Color(0, 0, 0));
        btn_searchProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search.png"))); // NOI18N
        btn_searchProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_searchProduct.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_searchProduct.setMargin(new java.awt.Insets(0, 14, 2, 14));
        btn_searchProduct.setMaximumSize(new java.awt.Dimension(72, 24));
        btn_searchProduct.setMinimumSize(new java.awt.Dimension(72, 24));
        btn_searchProduct.setPreferredSize(new java.awt.Dimension(72, 24));
        btn_searchProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchProductActionPerformed(evt);
            }
        });

        btn_productReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_productReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_productReportActionPerformed(evt);
            }
        });

        txtMaterial.setForeground(new java.awt.Color(0, 0, 0));

        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("Código del producto:");

        txtProductCode.setEditable(false);
        txtProductCode.setForeground(new java.awt.Color(0, 0, 0));

        jLabel46.setForeground(new java.awt.Color(0, 0, 0));
        jLabel46.setText("Precio:");

        txtProductPrice.setForeground(new java.awt.Color(0, 0, 0));

        jLabel45.setForeground(new java.awt.Color(0, 0, 0));
        jLabel45.setText("Stock:");

        javax.swing.GroupLayout pnl_inventarioLayout = new javax.swing.GroupLayout(pnl_inventario);
        pnl_inventario.setLayout(pnl_inventarioLayout);
        pnl_inventarioLayout.setHorizontalGroup(
            pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_inventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane3)
                    .addGroup(pnl_inventarioLayout.createSequentialGroup()
                        .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_inventarioLayout.createSequentialGroup()
                                .addGap(214, 214, 214)
                                .addComponent(btn_deleteProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(94, 94, 94)
                                .addComponent(btn_editProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnl_inventarioLayout.createSequentialGroup()
                        .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_inventarioLayout.createSequentialGroup()
                                .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtProductName, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                    .addComponent(txtProductId, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                    .addComponent(txtProductCode))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pnl_inventarioLayout.createSequentialGroup()
                                .addComponent(txtSearchProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_searchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_productReport, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(112, 112, 112)))
                        .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_inventarioLayout.createSequentialGroup()
                                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36))
                            .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(pnl_inventarioLayout.createSequentialGroup()
                                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(2, 2, 2))
                                .addGroup(pnl_inventarioLayout.createSequentialGroup()
                                    .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_inventarioLayout.createSequentialGroup()
                                            .addComponent(jLabel21)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(pnl_inventarioLayout.createSequentialGroup()
                                            .addComponent(jLabel22)
                                            .addGap(20, 20, 20)))
                                    .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtProvideerName, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
            .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_inventarioLayout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(btn_saveProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(620, Short.MAX_VALUE)))
            .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_inventarioLayout.createSequentialGroup()
                    .addContainerGap(620, Short.MAX_VALUE)
                    .addComponent(btn_cancelProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(16, 16, 16)))
        );
        pnl_inventarioLayout.setVerticalGroup(
            pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_inventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(txtProvideerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(txtProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel22)
                    .addComponent(txtMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46)
                    .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_inventarioLayout.createSequentialGroup()
                        .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_searchProduct, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSearchProduct, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_productReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23))
                        .addGap(18, 18, 18)
                        .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_deleteProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_editProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnl_inventarioLayout.createSequentialGroup()
                        .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel45))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_inventarioLayout.createSequentialGroup()
                    .addContainerGap(516, Short.MAX_VALUE)
                    .addComponent(btn_saveProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(pnl_inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_inventarioLayout.createSequentialGroup()
                    .addContainerGap(516, Short.MAX_VALUE)
                    .addComponent(btn_cancelProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        getContentPane().add(pnl_inventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 750, 560));

        pnl_ventas.setBackground(new java.awt.Color(255, 255, 255));
        pnl_ventas.setForeground(new java.awt.Color(255, 255, 255));
        pnl_ventas.setToolTipText("");

        jLabel25.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setText("Ventas");

        jTabbedPane4.setForeground(new java.awt.Color(0, 0, 0));
        jTabbedPane4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane4MouseClicked(evt);
            }
        });

        tbl_sales.setForeground(new java.awt.Color(0, 0, 0));
        tbl_sales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_sales.setFocusable(false);
        tbl_sales = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        jScrollPane4.setViewportView(tbl_sales);

        jTabbedPane4.addTab("Ventas realizadas", jScrollPane4);

        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pnl_ticket.setBackground(new java.awt.Color(255, 255, 255));

        jLabel33.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Comprobante de compra");

        jLabel34.setText("Cliente:");

        jLabel35.setText("DUI:");

        jLabel36.setText("Fecha de compra:");

        jLabel37.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel37.setText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("Producto(s):");

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Precio:");

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("Cantidad:");

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("Total:");

        lbl_ClientNameSale.setText("Nombre de cliente");

        lbl_ClientDUISale.setText("0000xxxx-x");

        lbl_SaleDate.setText("00/00/0000");

        lbl_SalesProductName.setText("Nombre de producto");

        lbl_SalesPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_SalesPrice.setText("$0.00");

        lbl_TotalSale.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TotalSale.setText("$0.00");

        lbl_SalesQuantity.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_SalesQuantity.setText("0");

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Mueblería \"El Serrucho\" S.A de C.V");

        jLabel43.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Serruchamos madera, no tu bolsillo");

        javax.swing.GroupLayout pnl_ticketLayout = new javax.swing.GroupLayout(pnl_ticket);
        pnl_ticket.setLayout(pnl_ticketLayout);
        pnl_ticketLayout.setHorizontalGroup(
            pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ticketLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addGroup(pnl_ticketLayout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_SaleDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(90, 90, 90))
                    .addGroup(pnl_ticketLayout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_ClientNameSale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnl_ticketLayout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addGap(25, 25, 25)
                        .addComponent(lbl_ClientDUISale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnl_ticketLayout.createSequentialGroup()
                        .addGroup(pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_SalesProductName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel40)
                            .addComponent(lbl_SalesQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_SalesPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_TotalSale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnl_ticketLayout.setVerticalGroup(
            pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ticketLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(lbl_ClientNameSale))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(lbl_ClientDUISale))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(lbl_SaleDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel40)
                    .addComponent(jLabel39)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_ticketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_SalesProductName)
                    .addComponent(lbl_SalesQuantity)
                    .addComponent(lbl_SalesPrice)
                    .addComponent(lbl_TotalSale))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel43)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(191, Short.MAX_VALUE)
                .addComponent(pnl_ticket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(183, 183, 183))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_ticket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane6.setViewportView(jPanel1);

        jTabbedPane4.addTab("Vista previa - Ticket", jScrollPane6);

        btn_deleteSale.setForeground(new java.awt.Color(0, 0, 0));
        btn_deleteSale.setText("Eliminar");
        btn_deleteSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_deleteSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteSaleActionPerformed(evt);
            }
        });

        btn_saveSale.setForeground(new java.awt.Color(0, 0, 0));
        btn_saveSale.setText("Guardar");
        btn_saveSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_saveSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveSaleActionPerformed(evt);
            }
        });

        btn_cancelSale.setForeground(new java.awt.Color(0, 0, 0));
        btn_cancelSale.setText("Cancelar");
        btn_cancelSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cancelSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelSaleActionPerformed(evt);
            }
        });

        btn_editSale.setForeground(new java.awt.Color(0, 0, 0));
        btn_editSale.setText("Editar");
        btn_editSale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_editSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editSaleActionPerformed(evt);
            }
        });

        txtSalesId.setEditable(false);
        txtSalesId.setForeground(new java.awt.Color(0, 0, 0));
        txtSalesId.setEnabled(false);

        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("No. de venta:");

        jLabel27.setForeground(new java.awt.Color(0, 0, 0));
        jLabel27.setText("Nombre de cliente:");

        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setText("Producto:");

        jLabel29.setForeground(new java.awt.Color(0, 0, 0));
        jLabel29.setText("DUI de cliente:");

        txtSearchSales.setForeground(new java.awt.Color(0, 0, 0));
        txtSearchSales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSalesKeyReleased(evt);
            }
        });

        jLabel30.setForeground(new java.awt.Color(0, 0, 0));
        jLabel30.setText("Buscar venta:");

        btn_searchSales.setForeground(new java.awt.Color(0, 0, 0));
        btn_searchSales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search.png"))); // NOI18N
        btn_searchSales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_searchSales.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_searchSales.setMargin(new java.awt.Insets(0, 14, 2, 14));
        btn_searchSales.setMaximumSize(new java.awt.Dimension(72, 24));
        btn_searchSales.setMinimumSize(new java.awt.Dimension(72, 24));
        btn_searchSales.setPreferredSize(new java.awt.Dimension(72, 24));
        btn_searchSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchSalesActionPerformed(evt);
            }
        });

        txtSalesClientDUI.setEditable(false);
        txtSalesClientDUI.setFormatterFactory(new javax.swing.JFormattedTextField.AbstractFormatterFactory() {
            public javax.swing.JFormattedTextField.AbstractFormatter
            getFormatter(javax.swing.JFormattedTextField textFormater) {
                try {
                    return new javax.swing.text.MaskFormatter("########-#");
                } catch(java.text.ParseException ex) {
                    System.out.println("Error al formatear DUI de cliente, error: "+ex.getMessage());
                }

                return null;
            }
        });
        txtSalesClientDUI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalesClientDUIActionPerformed(evt);
            }
        });

        btn_salesReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_salesReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salesReportActionPerformed(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Cantidad:");

        jLabel32.setForeground(new java.awt.Color(0, 0, 0));
        jLabel32.setText("Precio:");

        txtSalesQuantity.setForeground(new java.awt.Color(0, 0, 0));
        txtSalesQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSalesQuantityKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSalesQuantityKeyTyped(evt);
            }
        });

        txtSalesPrice.setForeground(new java.awt.Color(0, 0, 0));
        txtSalesPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSalesPriceKeyReleased(evt);
            }
        });

        cbFilter.setForeground(new java.awt.Color(0, 0, 0));
        cbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Cliente", "Producto", "Empleado", "Fecha" }));
        cbFilter.setSelectedItem("Seleccionar");
        cbFilter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel44.setForeground(new java.awt.Color(0, 0, 0));
        jLabel44.setText("Total:");

        txtTotalSale.setEditable(false);

        cbClientName.setForeground(new java.awt.Color(0, 0, 0));
        cbClientName.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cbClientName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbClientNameItemStateChanged(evt);
            }
        });

        btn_salesAddProduct.setText("Añadir");
        btn_salesAddProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_salesAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salesAddProductActionPerformed(evt);
            }
        });

        cbSalesProductName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbSalesProductNameItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout pnl_ventasLayout = new javax.swing.GroupLayout(pnl_ventas);
        pnl_ventas.setLayout(pnl_ventasLayout);
        pnl_ventasLayout.setHorizontalGroup(
            pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ventasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_ventasLayout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_ventasLayout.createSequentialGroup()
                        .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnl_ventasLayout.createSequentialGroup()
                                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel30))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnl_ventasLayout.createSequentialGroup()
                                        .addComponent(txtSearchSales)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_searchSales, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_salesReport, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(245, 245, 245))
                                    .addGroup(pnl_ventasLayout.createSequentialGroup()
                                        .addComponent(cbClientName, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(145, 145, 145)
                                        .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ventasLayout.createSequentialGroup()
                                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(181, 181, 181))
                                            .addGroup(pnl_ventasLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jLabel28)
                                                .addGap(33, 33, 33)
                                                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtSalesQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                                    .addComponent(cbSalesProductName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                            .addGroup(pnl_ventasLayout.createSequentialGroup()
                                                .addGap(2, 2, 2)
                                                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel32)
                                                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_ventasLayout.createSequentialGroup()
                                                        .addComponent(txtTotalSale)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btn_salesAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(txtSalesPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(2, 2, 2)))))
                                .addGap(10, 10, 10))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ventasLayout.createSequentialGroup()
                                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ventasLayout.createSequentialGroup()
                                        .addComponent(jLabel29)
                                        .addGap(31, 31, 31)
                                        .addComponent(txtSalesClientDUI, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ventasLayout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addGap(39, 39, 39)
                                        .addComponent(txtSalesId, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(16, 16, 16))
                    .addGroup(pnl_ventasLayout.createSequentialGroup()
                        .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(26, 26, 26))
                    .addGroup(pnl_ventasLayout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addComponent(btn_deleteSale, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addComponent(btn_editSale, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_cancelSale, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35))))
            .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_ventasLayout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(btn_saveSale, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(620, Short.MAX_VALUE)))
        );
        pnl_ventasLayout.setVerticalGroup(
            pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ventasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtSalesId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(cbSalesProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel8)
                    .addComponent(txtSalesQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbClientName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtSalesClientDUI)
                    .addComponent(txtSalesPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalSale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44)
                    .addComponent(btn_salesAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_ventasLayout.createSequentialGroup()
                        .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_searchSales, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                            .addComponent(btn_salesReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_ventasLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtSearchSales, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_deleteSale, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_editSale, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_cancelSale, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnl_ventasLayout.createSequentialGroup()
                        .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
            .addGroup(pnl_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_ventasLayout.createSequentialGroup()
                    .addContainerGap(516, Short.MAX_VALUE)
                    .addComponent(btn_saveSale, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        getContentPane().add(pnl_ventas, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 750, 560));

        pnl_ayuda.setBackground(new java.awt.Color(255, 255, 255));
        pnl_ayuda.setForeground(new java.awt.Color(255, 255, 255));
        pnl_ayuda.setToolTipText("");

        jLabel47.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(0, 0, 0));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel47.setText("Centro de ayuda");
        jLabel47.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jScrollPane5.setBackground(java.awt.Color.white);
        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane5.setPreferredSize(new java.awt.Dimension(1550, 1850));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel48.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 0, 0));
        jLabel48.setText("Menu Principal");

        jLabel49.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 0, 0));
        jLabel49.setText("Clientes");

        jLabel51.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 0, 0));
        jLabel51.setText("Empleados");

        lbl_clients.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Screenshots/2.png"))); // NOI18N

        lbl_menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Screenshots/1.png"))); // NOI18N

        lbl_employees.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Screenshots/3.png"))); // NOI18N

        jLabel52.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 0, 0));
        jLabel52.setText("Inventario");

        lbl_inventory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Screenshots/4.png"))); // NOI18N

        lbl_sales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Screenshots/5.png"))); // NOI18N

        jLabel53.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 0, 0));
        jLabel53.setText("Ventas");

        lbl_login.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Screenshots/10.png"))); // NOI18N

        jLabel55.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 0, 0));
        jLabel55.setText("Login");

        jLabel54.setForeground(new java.awt.Color(0, 0, 0));
        jLabel54.setText("En el formulario para registrar ventas puedes realizar las siguientes acciones:");

        pnl_salesData.setEditable(false);
        pnl_salesData.setBackground(new java.awt.Color(255, 255, 255));
        pnl_salesData.setBorder(null);
        pnl_salesData.setForeground(new java.awt.Color(0, 0, 0));
        pnl_salesData.setText("1. Puedes registrar y eliminar los datos de una venta realizada.\n2. Puedes visualizar un registro detallado acerca de las ventas realizadas.\n3. Puedes buscar el detalle de alguna venta realizada.\n4. Puedes generar un reporte detallado acerca de las ventas realizadas.");
        jScrollPane7.setViewportView(pnl_salesData);

        jLabel56.setForeground(new java.awt.Color(0, 0, 0));
        jLabel56.setText("En el formulario de Inventario puedes realizar las siguientes acciones:");

        pnl_inventoryData.setEditable(false);
        pnl_inventoryData.setBackground(new java.awt.Color(255, 255, 255));
        pnl_inventoryData.setBorder(null);
        pnl_inventoryData.setForeground(new java.awt.Color(0, 0, 0));
        pnl_inventoryData.setText("1. Puedes registrar, eliminar y/o actualizar los datos de un producto.\n2. Puedes visualizar un registro detallado acerca de los productos que posee la mueblería.\n3. Puedes buscar el detalle de algún producto.\n4. Puedes generar un reporte detallado acerca de los productos registrados.");
        jScrollPane8.setViewportView(pnl_inventoryData);

        jLabel57.setForeground(new java.awt.Color(0, 0, 0));
        jLabel57.setText("En el formulario de Clientes puedes realizar las siguientes acciones:");

        pnl_employeesData.setEditable(false);
        pnl_employeesData.setBackground(new java.awt.Color(255, 255, 255));
        pnl_employeesData.setBorder(null);
        pnl_employeesData.setForeground(new java.awt.Color(0, 0, 0));
        pnl_employeesData.setText("1. Puedes registrar, eliminar y/o actualizar los datos de un empleado.\n2. Puedes visualizar un registro detallado acerca de los empleados registrados en el sistema.\n3. Puedes buscar el detalle de algún empleado.\n4. Se asigna automáticamente un usuario en función del 1er nombre y 1er apellido del empleado.\n5. Puedes asignar el nivel de acceso de un usuario \"Administrador\" o \"Usuario\"\n6. Puedes generar un reporte detallado acerca de los empleados registrados.");
        jScrollPane9.setViewportView(pnl_employeesData);

        jLabel58.setForeground(new java.awt.Color(0, 0, 0));
        jLabel58.setText("En el formulario de Empleados puedes realizar las siguientes acciones:");

        pnl_clientsData.setEditable(false);
        pnl_clientsData.setBackground(new java.awt.Color(255, 255, 255));
        pnl_clientsData.setBorder(null);
        pnl_clientsData.setForeground(new java.awt.Color(0, 0, 0));
        pnl_clientsData.setText("1. Puedes registrar, eliminar y/o actualizar los datos de un cliente.\n2. Puedes visualizar un registro detallado acerca de los clientes que posee la mueblería.\n3. Puedes buscar el detalle de algún cliente.\n4. Puedes generar un reporte detallado acerca de los clientes registrados.");
        jScrollPane10.setViewportView(pnl_clientsData);

        jLabel59.setForeground(new java.awt.Color(0, 0, 0));
        jLabel59.setText("En el menu principal puedes realizar las siguientes acciones:");

        pnl_menuData.setEditable(false);
        pnl_menuData.setBackground(new java.awt.Color(255, 255, 255));
        pnl_menuData.setBorder(null);
        pnl_menuData.setForeground(new java.awt.Color(0, 0, 0));
        pnl_menuData.setText("1. Visualizar los botones de acceso a formularios.\n2. Puedes visualizar la fecha y hora actuales cada que desees.\n3. Cuentas con diversos botones de acceso: \"Clientes\", \"Empleados\", \"Inventario\", \"Ventas\", \"Ayuda\", \"Salir\"\n4. Si deseas salir del sistema, se te hará la consulta sobre si realmente quieres salir del sistema, a lo cual deberás responder con \"Sí\" o \"No\"");
        jScrollPane11.setViewportView(pnl_menuData);

        lbl_loginData.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lbl_loginData.setForeground(new java.awt.Color(0, 0, 0));
        lbl_loginData.setText("En el caso de que cierres tu sesión en el sistema, serás redirigido al login.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(746, 746, 746))
                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54)
                            .addComponent(jLabel52)
                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel51)
                            .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lbl_loginData, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
                                .addComponent(lbl_sales, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lbl_login, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
                                .addComponent(jLabel58, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_employees, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lbl_inventory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbl_clients, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 685, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel48)
                                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(183, 183, 183))
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(lbl_menu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_menu, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_clients, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_employees, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_inventory, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_sales, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_login, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_loginData, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane5.setViewportView(jPanel2);

        javax.swing.GroupLayout pnl_ayudaLayout = new javax.swing.GroupLayout(pnl_ayuda);
        pnl_ayuda.setLayout(pnl_ayudaLayout);
        pnl_ayudaLayout.setHorizontalGroup(
            pnl_ayudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ayudaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_ayudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnl_ayudaLayout.setVerticalGroup(
            pnl_ayudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ayudaLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 2288, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(pnl_ayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 750, 560));

        jLabel1.setBackground(new java.awt.Color(36, 46, 66));
        jLabel1.setForeground(new java.awt.Color(5, 176, 255));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 562));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
     closeWindow();
    }//GEN-LAST:event_btn_salirActionPerformed

    private void btn_ventasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ventasActionPerformed
     pnl_menu.setVisible(false);
     pnl_clientes.setVisible(false);
     pnl_empleados.setVisible(false);
     pnl_inventario.setVisible(false);
     pnl_ventas.setVisible(true);
     pnl_ayuda.setVisible(false);
     showSales();
    }//GEN-LAST:event_btn_ventasActionPerformed

    private void btn_inventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_inventarioActionPerformed
     pnl_menu.setVisible(false);
     pnl_clientes.setVisible(false);
     pnl_empleados.setVisible(false);
     pnl_inventario.setVisible(true);
     pnl_ventas.setVisible(false);
     pnl_ayuda.setVisible(false);
     showInventory();
    }//GEN-LAST:event_btn_inventarioActionPerformed

    private void btn_clientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clientesActionPerformed
     pnl_menu.setVisible(false);
     pnl_clientes.setVisible(true);
     pnl_empleados.setVisible(false);
     pnl_inventario.setVisible(false);
     pnl_ventas.setVisible(false);
     pnl_ayuda.setVisible(false);
     showClients();
    }//GEN-LAST:event_btn_clientesActionPerformed

    private void btn_empleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_empleadosActionPerformed
     pnl_menu.setVisible(false);
     pnl_clientes.setVisible(false);
     pnl_empleados.setVisible(true);
     pnl_inventario.setVisible(false);
     pnl_ventas.setVisible(false);
     pnl_ayuda.setVisible(false);
     showEmployees();
    }//GEN-LAST:event_btn_empleadosActionPerformed

    private void lbl_logoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_logoMouseClicked
     pnl_menu.setVisible(true);
     pnl_clientes.setVisible(false);
     pnl_empleados.setVisible(false);
     pnl_inventario.setVisible(false);
     pnl_ventas.setVisible(false);
     pnl_ayuda.setVisible(false);
    }//GEN-LAST:event_lbl_logoMouseClicked

    private void btn_saveClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveClientActionPerformed
     if(txtClientName.getText().isEmpty() || txtClientLastName.getText().isEmpty() || txtDUIClient.getText().equals("        - ")) {
      JOptionPane.showMessageDialog(null, "Completa todos los campos antes de guardar", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
     } else {   
      Clients newClient = null;
      newClient = setClients();
     
      ConexionBD.saveClient(newClient);
      clearClientsData();
      showClients();
     }
    }//GEN-LAST:event_btn_saveClientActionPerformed

    private void btn_deleteClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteClientActionPerformed
     int choice = JOptionPane.showConfirmDialog(this, "¿Desea eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
     
     if (choice == JOptionPane.YES_OPTION) {
      deleteClients();
      clearClientsData();
      showClients();
     }
    }//GEN-LAST:event_btn_deleteClientActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
     closeWindow();
    }//GEN-LAST:event_formWindowClosing

    private void btn_editClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editClientActionPerformed
     String idCliente;
     String nombreClienteCompleto;
     String[] nombresCliente;
     String nombreCliente;
     String apellidoCliente;
     String DUI;
     
     if(btn_editClient.getText().equals("Editar")) {
      int selectedRow = tbl_clients.getSelectedRow();
      
      if(selectedRow >= 0) {
       idCliente = tbl_clients.getValueAt(selectedRow, 0).toString();
       nombreClienteCompleto = tbl_clients.getValueAt(selectedRow, 1).toString();
       nombresCliente = nombreClienteCompleto.split(" ");
       nombreCliente = nombresCliente[0] + " " + nombresCliente[1];
       apellidoCliente = nombresCliente[2] + " " + nombresCliente[3];
       DUI = tbl_clients.getValueAt(selectedRow, 2).toString();
       
       txtClientId.setText(String.valueOf(idCliente));
       txtClientName.setText(nombreCliente);
       txtClientLastName.setText(apellidoCliente);
       txtDUIClient.setText(DUI);
       
       btn_editClient.setText("Actualizar");
      } else {
       JOptionPane.showMessageDialog(null, "Para actualizar un registro, seleccione uno previamente", "Error", JOptionPane.INFORMATION_MESSAGE);
       btn_editClient.setText("Editar");
      }
     } else {
      idCliente = txtClientId.getText();
      nombreCliente = txtClientName.getText();
      apellidoCliente = txtClientLastName.getText();
      DUI = txtDUIClient.getText();
      
      clients = new Clients(Integer.parseInt(idCliente), nombreCliente, apellidoCliente, DUI);
      ConexionBD.updateClient(clients);
      clearClientsData();
      showClients();
      btn_editClient.setText("Editar");
     }
    }//GEN-LAST:event_btn_editClientActionPerformed

    private void btn_cancelClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelClientActionPerformed
     int choice = JOptionPane.showConfirmDialog(this, "¿Desea cancelar las acciones sobre este registro?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
     
     if (choice == JOptionPane.YES_OPTION) {
      clearClientsData();
      showClients();
      btn_editClient.setText("Editar");
     }
    }//GEN-LAST:event_btn_cancelClientActionPerformed

    private void btn_deleteEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteEmployeeActionPerformed
     int choice = JOptionPane.showConfirmDialog(this, "¿Desea eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
     
     if (choice == JOptionPane.YES_OPTION) {
      deleteEmployees();
      clearEmployeesData();
      showEmployees();
     }
    }//GEN-LAST:event_btn_deleteEmployeeActionPerformed

    private void btn_saveEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveEmployeeActionPerformed
     if(txtEmployeeName.getText().isEmpty() || txtEmployeeLastName.getText().isEmpty() || txtEmployeeDUI.getText().equals("        - ") || (!rbtn_administrator.isSelected() && !rbtn_user.isSelected())) {
      JOptionPane.showMessageDialog(null, "Completa todos los campos antes de guardar", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
     } else {
      Employees newEmployee = null;
      newEmployee = setEmployees();
     
      ConexionBD.saveEmployee(newEmployee);
      clearEmployeesData();
      showEmployees();
     }
    }//GEN-LAST:event_btn_saveEmployeeActionPerformed

    private void btn_cancelEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelEmployeeActionPerformed
     int choice = JOptionPane.showConfirmDialog(this, "¿Desea cancelar las acciones sobre este registro?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
     
     if (choice == JOptionPane.YES_OPTION) {
      clearEmployeesData();
      showEmployees();
      btn_editEmployee.setText("Editar");
     }
    }//GEN-LAST:event_btn_cancelEmployeeActionPerformed

    private void btn_editEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editEmployeeActionPerformed
     String employeeId;
     String employeeFullName;
     String[] employeeNames;
     String employeeName;
     String employeeLastName;
     String DUI;
     String user;
     int userType;
     
     if(btn_editEmployee.getText().equals("Editar")) {
      int selectedRow = tbl_employees.getSelectedRow();
      
      if(selectedRow >= 0) {
       employeeId = tbl_employees.getValueAt(selectedRow, 0).toString();
       employeeFullName = tbl_employees.getValueAt(selectedRow, 1).toString();
       employeeNames = employeeFullName.split(" ");
       employeeName = employeeNames[0] + " " + employeeNames[1];
       employeeLastName = employeeNames[2] + " " + employeeNames[3];
       DUI = tbl_employees.getValueAt(selectedRow, 2).toString();
       user = employeeNames[0]+" "+employeeNames[2];
       
       txtEmployeeId.setText(String.valueOf(employeeId));
       txtEmployeeName.setText(employeeName);
       txtEmployeeLastName.setText(employeeLastName);
       txtEmployeeDUI.setText(DUI);
       lbl_User.setText(user);
       
       btn_editEmployee.setText("Actualizar");
      } else {
       JOptionPane.showMessageDialog(null, "Para actualizar un registro, seleccione uno previamente", "Error", JOptionPane.INFORMATION_MESSAGE);
       btn_editEmployee.setText("Editar");
      }
     } else {
      employeeId = txtEmployeeId.getText();
      employeeName = txtEmployeeName.getText();
      employeeLastName = txtEmployeeLastName.getText();
      DUI = txtEmployeeDUI.getText();
      user = lbl_User.getText();
      userType = rbtn_administrator.isSelected() ? 2 : 1;
      
      employees = new Employees(Integer.parseInt(employeeId), employeeName, employeeLastName, DUI, user, userType);
      ConexionBD.updateEmployee(employees);
      clearEmployeesData();
      showEmployees();
      btn_editEmployee.setText("Editar");
     }
    }//GEN-LAST:event_btn_editEmployeeActionPerformed

    private void btn_searchClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchClientActionPerformed
     searchClients();
    }//GEN-LAST:event_btn_searchClientActionPerformed

    private void txtSearchClientKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchClientKeyReleased
     if(txtSearchClient.getText().isEmpty()) {
      showClients();
     }
    }//GEN-LAST:event_txtSearchClientKeyReleased

    private void btn_searchEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchEmployeeActionPerformed
     searchEmployees();
    }//GEN-LAST:event_btn_searchEmployeeActionPerformed

    private void txtSearchEmployeeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchEmployeeKeyReleased
     if(txtSearchEmployee.getText().isEmpty()) {
      showEmployees();
     }
    }//GEN-LAST:event_txtSearchEmployeeKeyReleased

    private void rbtn_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtn_userActionPerformed
     if(rbtn_user.isSelected()) {
      rbtn_administrator.setSelected(false);
     }
    }//GEN-LAST:event_rbtn_userActionPerformed

    private void rbtn_administratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtn_administratorActionPerformed
     if(rbtn_administrator.isSelected()) {
      rbtn_user.setSelected(false);
     }
    }//GEN-LAST:event_rbtn_administratorActionPerformed

    private void btn_deleteProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteProductActionPerformed
     int choice = JOptionPane.showConfirmDialog(this, "¿Desea eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
     
     if (choice == JOptionPane.YES_OPTION) {
      deleteInventory();
      clearInventoryData();
      showInventory();
     }
    }//GEN-LAST:event_btn_deleteProductActionPerformed

    private void btn_saveProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveProductActionPerformed
     if(txtProductName.getText().isEmpty() || txtProvideerName.getText().isEmpty() || txtMaterial.getText().isEmpty()) {
      JOptionPane.showMessageDialog(null, "Completa todos los campos antes de guardar", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
     } else {
      Inventory newInventory = null;
      newInventory = setInventory();
     
      ConexionBD.saveInventory(newInventory);
      clearInventoryData();
      showInventory();
     }
    }//GEN-LAST:event_btn_saveProductActionPerformed

    private void btn_cancelProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelProductActionPerformed
     int choice = JOptionPane.showConfirmDialog(this, "¿Desea cancelar las acciones sobre este registro?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
     
     if (choice == JOptionPane.YES_OPTION) {
      clearInventoryData();
      showInventory();
      btn_editProduct.setText("Editar");
     }
    }//GEN-LAST:event_btn_cancelProductActionPerformed

    private void btn_editProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editProductActionPerformed
     String productId = null;
     String productCode;
     String productName;
     String productPrice;
     String provideerName;
     String productMaterial;
     String productStock;
     
     if(btn_editProduct.getText().equals("Editar")) {
      int selectedRow = tbl_products.getSelectedRow();
      
      if(selectedRow >= 0) {
       productId = tbl_products.getValueAt(selectedRow, 0).toString();
       productCode = tbl_products.getValueAt(selectedRow, 1).toString();
       productName = tbl_products.getValueAt(selectedRow, 2).toString();
       productPrice = tbl_products.getValueAt(selectedRow, 3).toString();
       provideerName = tbl_products.getValueAt(selectedRow, 4).toString();
       productMaterial = tbl_products.getValueAt(selectedRow, 5).toString();
       productStock = tbl_products.getValueAt(selectedRow, 6).toString();
       
       txtProductId.setText(productId);
       txtProductCode.setText(String.valueOf(productCode));
       txtProductName.setText(productName);
       txtProductPrice.setText(productPrice);
       txtProvideerName.setText(provideerName);
       txtMaterial.setText(productMaterial);
       txtStock.setText(productStock);
       
       btn_editProduct.setText("Actualizar");
      } else {
       JOptionPane.showMessageDialog(null, "Para actualizar un registro, seleccione uno previamente", "Error", JOptionPane.INFORMATION_MESSAGE);
       btn_editProduct.setText("Editar");
      }
     } else {
      productId = txtProductId.getText();
      productCode = txtProductCode.getText();
      productName = txtProductName.getText();
      productPrice = txtProductPrice.getText();
      provideerName = txtProvideerName.getText();
      productMaterial = txtMaterial.getText();
      productStock = txtStock.getText();
      
      inventory = new Inventory(Integer.parseInt(productId), productCode, productName, Double.parseDouble(productPrice), provideerName, productMaterial, Integer.parseInt(productStock));
      ConexionBD.updateInventory(inventory);
      clearInventoryData();
      showInventory();
      btn_editProduct.setText("Editar");
     }
    }//GEN-LAST:event_btn_editProductActionPerformed

    private void txtSearchProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchProductKeyReleased
     if(txtSearchProduct.getText().isEmpty()) {
      showInventory();
     }
    }//GEN-LAST:event_txtSearchProductKeyReleased

    private void btn_searchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchProductActionPerformed
     searchInventory();
    }//GEN-LAST:event_btn_searchProductActionPerformed

    private void btn_deleteSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteSaleActionPerformed
     int choice = JOptionPane.showConfirmDialog(this, "¿Desea eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
     
     if (choice == JOptionPane.YES_OPTION) {
      deleteSale();
      clearSaleData();
      showSales();
     }
    }//GEN-LAST:event_btn_deleteSaleActionPerformed

    private void btn_saveSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveSaleActionPerformed
     if(txtSalesClientDUI.getText().equals("        - ") || cbSalesProductName.getSelectedItem().toString().isEmpty() || txtSalesQuantity.getText().isEmpty() || txtSalesPrice.getText().isEmpty()) {
      JOptionPane.showMessageDialog(null, "Completa todos los campos antes de guardar", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
     } else {
      printAndSaveSale();
     }
    }//GEN-LAST:event_btn_saveSaleActionPerformed

    private void btn_cancelSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelSaleActionPerformed
     int choice = JOptionPane.showConfirmDialog(this, "¿Desea cancelar las acciones sobre este registro?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
     
     if (choice == JOptionPane.YES_OPTION) {
      clearSaleData();
      showSales();
      btn_editSale.setText("Editar");
     }
    }//GEN-LAST:event_btn_cancelSaleActionPerformed

    private void btn_editSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editSaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_editSaleActionPerformed

    private void txtSearchSalesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSalesKeyReleased
     if(txtSearchSales.getText().isEmpty()) {
      showSales();
     }
    }//GEN-LAST:event_txtSearchSalesKeyReleased

    private void btn_searchSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchSalesActionPerformed
     if(!txtSearchSales.getText().isEmpty()) {
      searchSales();
     } else {
      JOptionPane.showMessageDialog(null, "Introduzca un dato para realizar la búsqueda", "Error de búsqueda", JOptionPane.ERROR_MESSAGE);
     }
    }//GEN-LAST:event_btn_searchSalesActionPerformed

    private void txtSalesClientDUIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalesClientDUIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalesClientDUIActionPerformed

    private void btn_salesReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salesReportActionPerformed
     ConexionBD.generateSalesReport();
    }//GEN-LAST:event_btn_salesReportActionPerformed

    private void jTabbedPane4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane4MouseClicked

    }//GEN-LAST:event_jTabbedPane4MouseClicked

    private void btn_clientReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clientReportActionPerformed
     ConexionBD.generateClientsReport();
    }//GEN-LAST:event_btn_clientReportActionPerformed

    private void btn_employeeReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_employeeReportActionPerformed
     ConexionBD.generateEmployeesReport();
    }//GEN-LAST:event_btn_employeeReportActionPerformed

    private void btn_productReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_productReportActionPerformed
     ConexionBD.generateInventoryReport();
    }//GEN-LAST:event_btn_productReportActionPerformed

    private void txtSalesQuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalesQuantityKeyReleased
     lbl_SalesQuantity.setText(txtSalesQuantity.getText());
     lbl_SalesPrice.setText("$"+txtSalesPrice.getText());
     
     if(!txtSalesQuantity.getText().isEmpty() && !txtSalesPrice.getText().isEmpty()) {
      txtTotalSale.setText((calculateTotalSale(txtSalesQuantity.getText(), txtSalesPrice.getText())).toString());
      lbl_TotalSale.setText("$"+(calculateTotalSale(txtSalesQuantity.getText(), txtSalesPrice.getText())).toString());
     } else {
      calculateTotalSale("0", "0");
      txtTotalSale.setText("0.00");
      lbl_SalesQuantity.setText("0");
      lbl_SalesPrice.setText("$0.00");
      lbl_TotalSale.setText("$0.00");
     }
    }//GEN-LAST:event_txtSalesQuantityKeyReleased

    private void txtSalesPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalesPriceKeyReleased
     lbl_SalesQuantity.setText(txtSalesQuantity.getText());
     lbl_SalesPrice.setText("$"+txtSalesPrice.getText());
     
     if(!txtSalesQuantity.getText().isEmpty() && !txtSalesPrice.getText().isEmpty()) {
      txtTotalSale.setText((calculateTotalSale(txtSalesQuantity.getText(), txtSalesPrice.getText())).toString());
      lbl_TotalSale.setText("$"+(calculateTotalSale(txtSalesQuantity.getText(), txtSalesPrice.getText())).toString());
     } else {
      calculateTotalSale("0", "0");
      txtTotalSale.setText("0.00");
      lbl_SalesQuantity.setText("0");
      lbl_SalesPrice.setText("$0.00");
      lbl_TotalSale.setText("$0.00");
     }
    }//GEN-LAST:event_txtSalesPriceKeyReleased

    private void cbClientNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbClientNameItemStateChanged
     getClientDUI();
     lbl_ClientNameSale.setText(cbClientName.getSelectedItem().toString());
     lbl_ClientDUISale.setText(txtSalesClientDUI.getText());
    }//GEN-LAST:event_cbClientNameItemStateChanged

    private void btn_ayudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ayudaActionPerformed
     pnl_menu.setVisible(false);
     pnl_clientes.setVisible(false);
     pnl_empleados.setVisible(false);
     pnl_inventario.setVisible(false);
     pnl_ventas.setVisible(false);
     pnl_ayuda.setVisible(true);
    }//GEN-LAST:event_btn_ayudaActionPerformed

    private void btn_salesAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salesAddProductActionPerformed
     if(!cbSalesProductName.getSelectedItem().toString().isEmpty()) {
      productsName.add(cbSalesProductName.getSelectedItem().toString());
      productsPrice.add(Double.parseDouble(txtSalesPrice.getText()));
     }
    }//GEN-LAST:event_btn_salesAddProductActionPerformed

    private void txtSalesQuantityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalesQuantityKeyTyped
     char character = evt.getKeyChar();
     
     if(!Character.isDigit(character)) {
      evt.consume(); //Ignorar el carácter no numérico
     }
    }//GEN-LAST:event_txtSalesQuantityKeyTyped

    private void cbSalesProductNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSalesProductNameItemStateChanged
     lbl_SalesProductName.setText(cbSalesProductName.getSelectedItem().toString());
    }//GEN-LAST:event_cbSalesProductNameItemStateChanged

 public static void main(String args[]) {
  java.awt.EventQueue.invokeLater(new Runnable() {
  
  @Override
  public void run() {
   try {
    new Menu().setVisible(true);
   } catch (ClassNotFoundException | SQLException ex) {
    System.out.println("Error al cargar menu, error: "+ex.getMessage());
   }
  }
  });
 }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_ayuda;
    private javax.swing.JButton btn_cancelClient;
    private javax.swing.JButton btn_cancelEmployee;
    private javax.swing.JButton btn_cancelProduct;
    private javax.swing.JButton btn_cancelSale;
    private javax.swing.JButton btn_clientReport;
    private javax.swing.JButton btn_clientes;
    private javax.swing.JButton btn_deleteClient;
    private javax.swing.JButton btn_deleteEmployee;
    private javax.swing.JButton btn_deleteProduct;
    private javax.swing.JButton btn_deleteSale;
    private javax.swing.JButton btn_editClient;
    private javax.swing.JButton btn_editEmployee;
    private javax.swing.JButton btn_editProduct;
    private javax.swing.JButton btn_editSale;
    public javax.swing.JButton btn_empleados;
    private javax.swing.JButton btn_employeeReport;
    private javax.swing.JButton btn_inventario;
    private javax.swing.JButton btn_productReport;
    private javax.swing.JButton btn_salesAddProduct;
    private javax.swing.JButton btn_salesReport;
    private javax.swing.JButton btn_salir;
    private javax.swing.JButton btn_saveClient;
    private javax.swing.JButton btn_saveEmployee;
    private javax.swing.JButton btn_saveProduct;
    private javax.swing.JButton btn_saveSale;
    private javax.swing.JButton btn_searchClient;
    private javax.swing.JButton btn_searchEmployee;
    private javax.swing.JButton btn_searchProduct;
    private javax.swing.JButton btn_searchSales;
    private javax.swing.JButton btn_ventas;
    private javax.swing.JComboBox<String> cbClientName;
    private javax.swing.JComboBox<String> cbFilter;
    private javax.swing.JComboBox<String> cbSalesProductName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JLabel lbl_ClientDUISale;
    private javax.swing.JLabel lbl_ClientNameSale;
    private javax.swing.JLabel lbl_SaleDate;
    private javax.swing.JLabel lbl_SalesPrice;
    private javax.swing.JLabel lbl_SalesProductName;
    private javax.swing.JLabel lbl_SalesQuantity;
    private javax.swing.JLabel lbl_TotalSale;
    private javax.swing.JLabel lbl_User;
    public javax.swing.JLabel lbl_clients;
    private javax.swing.JLabel lbl_date;
    public javax.swing.JLabel lbl_employees;
    private javax.swing.JLabel lbl_hour;
    public javax.swing.JLabel lbl_inventory;
    public javax.swing.JLabel lbl_login;
    public javax.swing.JLabel lbl_loginData;
    public javax.swing.JLabel lbl_logo;
    public javax.swing.JLabel lbl_menu;
    public javax.swing.JLabel lbl_principal;
    public javax.swing.JLabel lbl_role;
    public javax.swing.JLabel lbl_sales;
    public javax.swing.JLabel lbl_username;
    private javax.swing.JPanel pnl_ayuda;
    private javax.swing.JPanel pnl_clientes;
    public javax.swing.JTextPane pnl_clientsData;
    private javax.swing.JPanel pnl_empleados;
    public javax.swing.JTextPane pnl_employeesData;
    private javax.swing.JPanel pnl_inventario;
    public javax.swing.JTextPane pnl_inventoryData;
    private javax.swing.JPanel pnl_menu;
    public javax.swing.JTextPane pnl_menuData;
    public javax.swing.JTextPane pnl_salesData;
    private javax.swing.JPanel pnl_ticket;
    private javax.swing.JPanel pnl_ventas;
    private javax.swing.JRadioButton rbtn_administrator;
    private javax.swing.JRadioButton rbtn_user;
    private javax.swing.JTable tbl_clients;
    private javax.swing.JTable tbl_employees;
    private javax.swing.JTable tbl_products;
    private javax.swing.JTable tbl_sales;
    private javax.swing.JTextField txtClientId;
    private javax.swing.JTextField txtClientLastName;
    private javax.swing.JTextField txtClientName;
    private javax.swing.JFormattedTextField txtDUIClient;
    private javax.swing.JFormattedTextField txtEmployeeDUI;
    private javax.swing.JTextField txtEmployeeId;
    private javax.swing.JTextField txtEmployeeLastName;
    private javax.swing.JTextField txtEmployeeName;
    private javax.swing.JTextField txtMaterial;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtProductId;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtProductPrice;
    private javax.swing.JTextField txtProvideerName;
    private javax.swing.JFormattedTextField txtSalesClientDUI;
    private javax.swing.JTextField txtSalesId;
    private javax.swing.JTextField txtSalesPrice;
    private javax.swing.JTextField txtSalesQuantity;
    private javax.swing.JTextField txtSearchClient;
    private javax.swing.JTextField txtSearchEmployee;
    private javax.swing.JTextField txtSearchProduct;
    private javax.swing.JTextField txtSearchSales;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtTotalSale;
    // End of variables declaration//GEN-END:variables
}
