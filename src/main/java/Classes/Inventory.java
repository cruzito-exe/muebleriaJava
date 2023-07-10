package Classes;

public class Inventory {
 public int productId;
 public String productCode;
 public String productName;
 public double productPrice;
 public String provideerName;
 public String productMaterial;
 public int productStock;

 public Inventory(int productId, String productCode, String productName, double productPrice, String provideerName, String productMaterial, int productStock) {
  this.productId = productId;
  this.productCode = productCode;
  this.productName = productName;
  this.productPrice = productPrice;
  this.provideerName = provideerName;
  this.productMaterial = productMaterial;
  this.productStock = productStock;
 }

 public int getProductId() {
  return productId;
 }

 public void setProductId(int productId) {
  this.productId = productId;
 }

 public String getProductCode() {
  return productCode;
 }

 public void setProductCode(String productCode) {
  this.productCode = productCode;
 }

 public String getProductName() {
  return productName;
 }

 public void setProductName(String productName) {
  this.productName = productName;
 }

 public double getProductPrice() {
  return productPrice;
 }

 public void setProductPrice(double productPrice) {
  this.productPrice = productPrice;
 }

 public String getProvideerName() {
  return provideerName;
 }

 public void setProvideerName(String provideerName) {
  this.provideerName = provideerName;
 }

 public String getProductMaterial() {
  return productMaterial;
 }

 public void setProductMaterial(String productMaterial) {
  this.productMaterial = productMaterial;
 }
 
 public int getProductStock() {
  return productStock;
 }

 public void setProductStock(int productStock) {
  this.productStock = productStock;
 }
}
