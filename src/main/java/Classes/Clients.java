package Classes;

public class Clients {
 public int clientId;
 public String clientName;
 public String clientLastName;
 public String DUI;
 
 public Clients(int clientId, String clientName, String clientLastName, String DUI) {
  this.clientId = clientId;
  this.clientName = clientName;
  this.clientLastName = clientLastName;
  this.DUI = DUI;
 }
 
 public int getClientId() {
  return clientId;
 }

 public void setClientId(int clientId) {
  this.clientId = clientId;
 }

 public String getClientName() {
  return clientName;
 }

 public void setClientName(String clientName) {
  this.clientName = clientName;
 }

 public String getClientLastName() {
  return clientLastName;
 }

 public void setClientLastName(String clientLastName) {
  this.clientLastName = clientLastName;
 }

 public String getDUI() {
  return DUI;
 }

 public void setDUI(String DUI) {
  this.DUI = DUI;
 }
}
