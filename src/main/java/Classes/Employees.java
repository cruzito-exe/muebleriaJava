package Classes;

public class Employees {
 public int employeeId;
 public String employeeName;
 public String employeeLastName;
 public String user;
 public String DUI;
 public int userType;
 
 public Employees(int employeeId, String employeeName, String employeeLastName, String DUI, String user, int userType) {
  this.employeeId = employeeId;
  this.employeeName = employeeName;
  this.employeeLastName = employeeLastName;
  this.user = user;
  this.DUI = DUI;
  this.userType = userType;
 }
 
 public int getEmployeeId() {
  return employeeId;
 }
 
 public void setEmployeeId(int employeeId) {
  this.employeeId = employeeId;
 }
 
 public String getEmployeeName() {
  return employeeName;
 }
 
 public void setEmployeeName(String employeeName) {
  this.employeeName = employeeName;
 }
 
 public String getEmployeeLastName() {
  return employeeLastName;
 }
 
 public void setEmployeeLastName(String employeeLastName) {
  this.employeeLastName = employeeLastName;
 }
 
 public String getUser() {
  return user;
 }
 
 public void setUser(String user) {
  this.user = user;
 }
 
 public String getDUI() {
  return DUI;
 }
 
 public void setDui(String DUI) {
  this.DUI = DUI;
 }
 
 public int getUserType() {
  return userType;
 }
 
 public void setUserType(int userType) {
  this.userType = userType;
 }
}
