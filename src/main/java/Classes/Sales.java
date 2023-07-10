package Classes;

import java.util.ArrayList;

public class Sales {
 public int saleId;
 public String saleCode;
 public String clientName;
 public ArrayList<String> productName;
 public ArrayList<Double> productPrice;
 public int saleQuantity;
 public double totalSale;
 public String saleDate;
 public String employeeName;

 public Sales(int saleId, String saleCode, String clientName, ArrayList<String> productName, ArrayList<Double> productPrice, int saleQuantity, double totalSale, String saleDate, String employeeName) {
  this.saleId = saleId;
  this.saleCode = saleCode;
  this.clientName = clientName;
  this.productName = productName;
  this.productPrice = productPrice;
  this.saleQuantity = saleQuantity;
  this.totalSale = totalSale;
  this.saleDate = saleDate;
  this.employeeName = employeeName;
 }
 
 public int getSaleId() {
  return saleId;
 }

 public void setSaleId(int saleId) {
  this.saleId = saleId;
 }

 public String getSaleCode() {
  return saleCode;
 }

 public void setSaleCode(String saleCode) {
  this.saleCode = saleCode;
 }

 public String getClientName() {
  return clientName;
 }

 public void setClientName(String clientName) {
  this.clientName = clientName;
 }

 public ArrayList<String> getProductName() {
  return productName;
 }

 public void setProductName(ArrayList<String> productName) {
  this.productName = productName;
 }

 public ArrayList<Double> getProductPrice() {
  return productPrice;
 }

 public void setProductPrice(ArrayList<Double> productPrice) {
  this.productPrice = productPrice;
 }

 public int getSaleQuantity() {
  return saleQuantity;
 }

 public void setSaleQuantity(int saleQuantity) {
  this.saleQuantity = saleQuantity;
 }

 public double getTotalSale() {
  return totalSale;
 }

 public void setTotalSale(double totalSale) {
  this.totalSale = totalSale;
 }

 public String getSaleDate() {
  return saleDate;
 }

 public void setSaleDate(String saleDate) {
  this.saleDate = saleDate;
 }

 public String getEmployeeName() {
  return employeeName;
 }

 public void setNombreEmpleado(String employeeName) {
  this.employeeName = employeeName;
 }
}
