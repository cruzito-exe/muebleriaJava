package Interface;

import Classes.Employees;
import Config.ConexionBD;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {
 Connection connection;
 PreparedStatement preparedStatement;
 ResultSet resultSet;
 
 String role = null;
 
 Employees employees;
 Menu menu;
 
 public Login() {
  initComponents();
  employees = new Classes.Employees(0, null, null, null, null, 0);
  this.setLocationRelativeTo(null);
 }
 
@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_login = new javax.swing.JButton();
        txtUsername = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbl_clickHerePassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        chb_mostrarContraseña = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setBackground(new java.awt.Color(255, 255, 255));
        setFont(new java.awt.Font("Fira Code", 0, 10)); // NOI18N
        setName("login"); // NOI18N
        setResizable(false);

        btn_login.setForeground(new java.awt.Color(0, 0, 0));
        btn_login.setText("Login");
        btn_login.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });

        txtUsername.setForeground(new java.awt.Color(0, 0, 0));
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsernameKeyPressed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Username");

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Password");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("LOGIN");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Forgot your password?");

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("You don't have an account?");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Click here");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        lbl_clickHerePassword.setForeground(new java.awt.Color(0, 0, 0));
        lbl_clickHerePassword.setText("Click here");
        lbl_clickHerePassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        txtPassword.setForeground(new java.awt.Color(0, 0, 0));
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });

        chb_mostrarContraseña.setForeground(new java.awt.Color(0, 0, 0));
        chb_mostrarContraseña.setText("Show password");
        chb_mostrarContraseña.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chb_mostrarContraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chb_mostrarContraseñaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(331, 331, 331)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_clickHerePassword))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .addComponent(txtUsername)
                            .addComponent(chb_mostrarContraseña))))
                .addContainerGap(253, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(353, 353, 353))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chb_mostrarContraseña)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_login)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lbl_clickHerePassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(87, 87, 87))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

 public Employees login(String user, String dui) {
  String login = "select * from empleados where usuario = ? and DUI = ?";

  try {
   menu = new Menu();
   connection = ConexionBD.conexionBD();
   preparedStatement = connection.prepareStatement(login);
   preparedStatement.setString(1, user);
   preparedStatement.setString(2, dui);
   resultSet = preparedStatement.executeQuery();

   if(resultSet.next()) {
    employees.setEmployeeId(resultSet.getInt("idEmpleado"));
    employees.setUser(resultSet.getString("usuario"));
    employees.setUserType(resultSet.getInt("TipoUsuario"));
    
    if(employees.getUserType() == 2) {
     setAdminInterface();
    } else {
     setUserInterface();
    }
    
    JOptionPane.showMessageDialog(null, "Bienvenido/a " + user);
    menu.setVisible(true);
    dispose();
    
    menu.lbl_username.setText(user);
    menu.lbl_role.setText(role);
    
    return employees;
   } else {
    JOptionPane.showMessageDialog(null, "Credenciales inválidas. Por favor, intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
   }
  } catch(ClassNotFoundException | SQLException ex) {
   JOptionPane.showMessageDialog(null, "Ha ocurrido un error al iniciar sesión, error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
   System.out.println("Error al iniciar sesión, error: " + ex.getMessage());
  } finally {
   ConexionBD.closeResources(connection, preparedStatement, resultSet);
  }

  return null;
}
 
private void setAdminInterface() {
 setInterface("Rol: Administrador", "/Icons/admin.png", "/Screenshots/1.png", "/Screenshots/2.png", "/Screenshots/3.png", "/Screenshots/4.png", "/Screenshots/5.png", "/Screenshots/10.png", "/Icons/principal.png", true);
}

private void setUserInterface() {
 setInterface("Rol: Empleado", "/Icons/user.png", "/Screenshots/6.png", "/Screenshots/7.png", "/Screenshots/7.png", "/Screenshots/8.png", "/Screenshots/9.png", "/Screenshots/10.png", "/Icons/principal.png", false);
}

private void setInterface(String role, String logoImagePath, String menuImagePath, String clientsImagePath, String employeesImagePath, String inventoryImagePath, String salesImagePath, String loginImagePath, String principalImagePath, boolean enableEmployees) {
 this.role = role;
 menu.setImageLabel(menu.lbl_logo, logoImagePath);
 menu.setImageLabel(menu.lbl_menu, menuImagePath);
 menu.setImageLabel(menu.lbl_clients, clientsImagePath);
 menu.setImageLabel(menu.lbl_employees, employeesImagePath);
 menu.setImageLabel(menu.lbl_inventory, inventoryImagePath);
 menu.setImageLabel(menu.lbl_sales, salesImagePath);
 menu.setImageLabel(menu.lbl_login, loginImagePath);
 menu.setImageLabel(menu.lbl_principal, principalImagePath);
 menu.btn_empleados.setEnabled(enableEmployees);
}

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
     String username = txtUsername.getText();
     String password = txtPassword.getText();

     if(username.isEmpty() || password.isEmpty()) {
      JOptionPane.showMessageDialog(null, "Por favor, ingrese el usuario y la contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
     } else {
      login(username, password);
     }
    }//GEN-LAST:event_btn_loginActionPerformed

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
     if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
      String username = txtUsername.getText();
      String password = txtPassword.getText();
      
      if(username.isEmpty() || password.isEmpty()) {
       JOptionPane.showMessageDialog(null, "Por favor, ingrese el usuario y la contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
      } else {
       login(username, password);
      }
     }
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void chb_mostrarContraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chb_mostrarContraseñaActionPerformed
     if(chb_mostrarContraseña.isSelected()) {
      txtPassword.setEchoChar((char)0);
     } else {
      txtPassword.setEchoChar('\u2022');
     }
    }//GEN-LAST:event_chb_mostrarContraseñaActionPerformed

    private void txtUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyPressed
     if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
      String username = txtUsername.getText();
      String password = txtPassword.getText();
      
      if(username.isEmpty() || password.isEmpty()) {
       JOptionPane.showMessageDialog(null, "Por favor, ingrese el usuario y la contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
      } else {
       login(username, password);
      }
     }
    }//GEN-LAST:event_txtUsernameKeyPressed

    public static void main(String args[]) {
     java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
       new Login().setVisible(true);
      }
     });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_login;
    private javax.swing.JCheckBox chb_mostrarContraseña;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lbl_clickHerePassword;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
