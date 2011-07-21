/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewUser.java
 *
 * Created on 19-jul-2011, 9:44:40
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shidalgo
 */
public class NewUser extends javax.swing.JDialog {

    private ManageUser parent;

    /** Creates new form NewUser
     * @param parent
     * @param modal
     */
    public NewUser(ManageUser parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        try {
            this.parent = parent;
            Shared.updateProfiles(roleCombo, true);
            roleCombo.getModel();
        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
            msg.show(this);
        }  catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Error al crear usuario.",ex);
            msb.show(this);
            Shared.reload();
        }


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        loginText = new javax.swing.JTextField();
        roleCombo = new javax.swing.JComboBox();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Constants.appName);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel1.setText("Introduzca el nuevo login");
        jLabel1.setName("jLabel1"); // NOI18N

        loginText.setName("loginText"); // NOI18N
        loginText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                loginTextKeyPressed(evt);
            }
        });

        roleCombo.setName("roleCombo"); // NOI18N

        cancelButton.setText("Cancelar");
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        acceptButton.setText("Aceptar");
        acceptButton.setName("acceptButton"); // NOI18N
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(loginText, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(roleCombo, javax.swing.GroupLayout.Alignment.TRAILING, 0, 231, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(acceptButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roleCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(acceptButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loginTextKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            doit();
        }else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            closeWindows();
        }
    }//GEN-LAST:event_loginTextKeyPressed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        closeWindows();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        doit();
    }//GEN-LAST:event_acceptButtonActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField loginText;
    private javax.swing.JComboBox roleCombo;
    // End of variables declaration//GEN-END:variables

    private void closeWindows() {
        this.setVisible(false);
        dispose();
    }

    private void doit(){
        try {
            if (loginText.getText().isEmpty()) {
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "El login no puede ser vacío.");
                msg.show(this);
                return;
            }
            if (roleCombo.getSelectedIndex() == 0) {
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Debe elegir el perfil.");
                msg.show(this);
                return;
            }
            ConnectionDrivers.createUser(loginText.getText() , (String)roleCombo.getSelectedItem());
            MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Usuario creado satisfactoriamente.");
            msg.show(this);
            parent.newUserId = loginText.getText();
            closeWindows();
        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Error con la base de datos.", ex);
            msg.show(this);
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Error al crear el nuevo usuario.",ex);
            msb.show(this);
            Shared.reload();
        }
    }

}
