/*
 * ManageUser.java
 *
 * Created on 18-jul-2011, 15:26:57
 */

package totalpos;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author shidalgo
 */
public class ManageUser extends javax.swing.JDialog {

    private List<User> users;
    private List<Profile> profiles;
    private int selectedUser = -1;
    public String newUserId = null;
    public boolean isOk = false;

    /** Creates new form ManageUser
     * @param parent Padre de la nueva ventana.
     * @param modal Indica el estilo del Frame.
     */
    public ManageUser(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        updateAll();


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        loginText = new javax.swing.JTextField();
        nombreText = new javax.swing.JTextField();
        apellidoText = new javax.swing.JTextField();
        roleCombo = new javax.swing.JComboBox();
        bloqueadoCheck = new javax.swing.JCheckBox();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        newUserButton = new javax.swing.JButton();
        newPassword = new javax.swing.JPasswordField();
        newPassword2 = new javax.swing.JPasswordField();
        mustChangePasswordCheck = new javax.swing.JCheckBox();
        canChangePasswordCheck = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        userList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Constants.appName);

        labelTitle.setFont(new java.awt.Font("Courier New", 1, 18));
        labelTitle.setText("Usuarios");
        labelTitle.setName("labelTitle"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel4.setFont(new java.awt.Font("Courier New", 0, 11));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel4.setText("Usuario");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Courier New", 0, 11));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel5.setText("Nombre");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setFont(new java.awt.Font("Courier New", 0, 11));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel6.setText("Apellido");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel9.setFont(new java.awt.Font("Courier New", 0, 11));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel9.setText("Perfil");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Courier New", 0, 11));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel2.setText("Contraseña");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Courier New", 0, 11));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel3.setText("Contraseña de nuevo");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel1.setForeground(new java.awt.Color(255, 51, 0));
        jLabel1.setText("*");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel7.setForeground(new java.awt.Color(255, 51, 0));
        jLabel7.setText("*");
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setForeground(new java.awt.Color(255, 51, 0));
        jLabel8.setText("*");
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel10.setForeground(new java.awt.Color(255, 51, 0));
        jLabel10.setText("*");
        jLabel10.setName("jLabel10"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel1))
                        .addComponent(jLabel7))
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(92, 92, 92)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addContainerGap(235, Short.MAX_VALUE))
        );

        jPanel3.setName("jPanel3"); // NOI18N

        loginText.setEditable(false);
        loginText.setName("loginText"); // NOI18N

        nombreText.setName("nombreText"); // NOI18N
        nombreText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombreTextKeyReleased(evt);
            }
        });

        apellidoText.setName("apellidoText"); // NOI18N
        apellidoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                apellidoTextKeyReleased(evt);
            }
        });

        roleCombo.setName("roleCombo"); // NOI18N

        bloqueadoCheck.setText("Bloqueado");
        bloqueadoCheck.setName("bloqueadoCheck"); // NOI18N
        bloqueadoCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bloqueadoCheckActionPerformed(evt);
            }
        });

        saveButton.setText("Guardar");
        saveButton.setName("saveButton"); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cerrar");
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        newUserButton.setText("Nuevo Usuario");
        newUserButton.setName("newUserButton"); // NOI18N
        newUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUserButtonActionPerformed(evt);
            }
        });

        newPassword.setName("newPassword"); // NOI18N

        newPassword2.setName("newPassword2"); // NOI18N

        mustChangePasswordCheck.setText("Debe cambiar contraseña al iniciar sesión por primera vez.");
        mustChangePasswordCheck.setName("mustChangePasswordCheck"); // NOI18N

        canChangePasswordCheck.setText("Puede cambiar contraseña.");
        canChangePasswordCheck.setName("canChangePasswordCheck"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(canChangePasswordCheck)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(mustChangePasswordCheck)
                            .addContainerGap())
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(newUserButton, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                                .addComponent(bloqueadoCheck, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                                .addComponent(roleCombo, 0, 387, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap())
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(newPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(newPassword2, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(nombreText, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                            .addGap(12, 12, 12))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(apellidoText, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(loginText, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                            .addContainerGap()))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loginText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombreText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(apellidoText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87)
                .addComponent(roleCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bloqueadoCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mustChangePasswordCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(canChangePasswordCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(newUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        userList.setFont(new java.awt.Font("Courier New", 0, 11));
        userList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        userList.setName("userList"); // NOI18N
        userList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                userListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(userList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelTitle))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(labelTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void userListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_userListValueChanged
        selectedUser = userList.getSelectedIndex();

        if ( selectedUser == -1 ){
            userList.setSelectedIndex(0);
            selectedUser = 0;
        }

        User u = users.get(selectedUser);
        loginText.setText(u.getLogin());
        nombreText.setText(u.getNombre());
        apellidoText.setText(u.getApellido());
        roleCombo.setSelectedIndex(profiles.indexOf(new Profile(u.getPerfil(),null)));
        bloqueadoCheck.setSelected(u.getBloqueado());
        mustChangePasswordCheck.setSelected(u.getDebeCambiarPassword());
        canChangePasswordCheck.setSelected(u.getPuedeCambiarPassword());
        newPassword.setText("");
        newPassword2.setText("");
        
    }//GEN-LAST:event_userListValueChanged

    private void bloqueadoCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bloqueadoCheckActionPerformed
        
    }//GEN-LAST:event_bloqueadoCheckActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {

            if ( !newPassword.getText().equals(newPassword2.getText()) ){
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Las contraseñas deben ser iguales.");
                msg.show(this);
                return;
            }

            if ( newPassword.getText().isEmpty() ){
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "La contraseña no puede ser vacía.");
                msg.show(this);
                return;
            }

            ConnectionDrivers.changeProperties(loginText.getText(), nombreText.getText(), apellidoText.getText(), roleCombo.getSelectedItem().toString(), bloqueadoCheck.isSelected(), canChangePasswordCheck.isSelected() , mustChangePasswordCheck.isSelected());
            ConnectionDrivers.setPassword(loginText.getText(), Shared.hashPassword(newPassword.getText()));
            MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Guardado satisfactoriamente");
            msg.show(this);

            newUserId = loginText.getText();
            updateAll();
        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
            msg.show(this);
        }catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas al cambiar perfil.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void newUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUserButtonActionPerformed
        NewUser nu = new NewUser(this, true);
        if ( nu.isOk ){
            Shared.centerFrame(nu);
            nu.setVisible(true);
            updateAll();
        }
        
    }//GEN-LAST:event_newUserButtonActionPerformed

    private void nombreTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreTextKeyReleased
        if ( nombreText.getText().length() > 15 ){
            nombreText.setText(nombreText.getText().substring(0, 15));
        }
    }//GEN-LAST:event_nombreTextKeyReleased

    private void apellidoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_apellidoTextKeyReleased
        if ( apellidoText.getText().length() > 15 ){
            apellidoText.setText(apellidoText.getText().substring(0, 15));
        }
    }//GEN-LAST:event_apellidoTextKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apellidoText;
    private javax.swing.JCheckBox bloqueadoCheck;
    private javax.swing.JCheckBox canChangePasswordCheck;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JTextField loginText;
    private javax.swing.JCheckBox mustChangePasswordCheck;
    private javax.swing.JPasswordField newPassword;
    private javax.swing.JPasswordField newPassword2;
    private javax.swing.JButton newUserButton;
    private javax.swing.JTextField nombreText;
    private javax.swing.JComboBox roleCombo;
    private javax.swing.JButton saveButton;
    private javax.swing.JList userList;
    // End of variables declaration//GEN-END:variables

    private void updateUsers() throws SQLException {
        try{
            users = ConnectionDrivers.listUsers();
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas al listar los usuarios.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
    }

    private void updateList() {
        String l[] = new String[users.size()];
        int i = 0;
        for (User u : users) {
            l[i++] = u.getLogin();
        }

        
        userList.setListData(l);
    }

    private void updateAll() {
        try {
            updateProfiles();
            updateUsers();
            updateList();

            if ( newUserId == null ) {
                userList.setSelectedIndex(0);
            }
            else{
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getLogin().equals(newUserId)) {
                        userList.setSelectedIndex(i);
                        newUserId = null;
                        break;
                    }
                }
            }
            isOk = true;
        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
            msg.show(this);
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas al listar usuarios.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
    }

    private void updateProfiles() throws SQLException, Exception {
        profiles = Shared.updateProfiles(roleCombo,false);
    }

}
