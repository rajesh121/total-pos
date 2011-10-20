/*
 * ChangeQuantItems.java
 *
 * Created on 25-ago-2011, 8:38:56
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;

/**
 *
 * @author Saúl Hidalgo
 */
public class ChangeQuantItems extends javax.swing.JDialog {

    public boolean passOk = false;
    public String funct;
    /** Creates new form ChangeQuantItems */
    protected ChangeQuantItems(java.awt.Frame parent, boolean modal, int quant, String title, String function) {
        super(parent, modal);
        initComponents();
        funct = function;
        if ( function.equals(Constants.changeReceipt) ){
            titleLabel.setText(title);
        }else if ( function.equals(Constants.changeQuant) ){
            titleLabel.setText(title + " " + quant);
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

        titleLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        acceptButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        cirifLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        Nombre = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cantidad de Productos");

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        titleLabel.setText("Titulo");
        titleLabel.setName("titleLabel"); // NOI18N

        jLabel11.setText("* = Campo Obligatorio");
        jLabel11.setName("jLabel11"); // NOI18N

        acceptButton.setText("Aceptar");
        acceptButton.setName("acceptButton"); // NOI18N
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        jLabel9.setText("*");
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText("*");
        jLabel10.setName("jLabel10"); // NOI18N

        idField.setFont(new java.awt.Font("Courier New", 0, 12));
        idField.setName("idField"); // NOI18N
        idField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idFieldKeyPressed(evt);
            }
        });

        cirifLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        cirifLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        cirifLabel.setText("Usuario");
        cirifLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cirifLabel.setName("cirifLabel"); // NOI18N

        passwordField.setName("passwordField"); // NOI18N
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });
        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordFieldKeyPressed(evt);
            }
        });

        Nombre.setFont(new java.awt.Font("Courier New", 0, 12));
        Nombre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        Nombre.setText("Contraseña");
        Nombre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Nombre.setName("Nombre"); // NOI18N

        cancelButton.setText("Cancelar");
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                        .addComponent(acceptButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cirifLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(idField, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cirifLabel)
                    .addComponent(jLabel9)
                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(Nombre)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelButton)
                        .addComponent(acceptButton))
                    .addComponent(jLabel11))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        doIt();
}//GEN-LAST:event_acceptButtonActionPerformed

    private void idFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }else if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            passwordField.requestFocus();
        }
}//GEN-LAST:event_idFieldKeyPressed

    private void passwordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }
}//GEN-LAST:event_passwordFieldKeyPressed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
}//GEN-LAST:event_cancelButtonActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        doIt();
    }//GEN-LAST:event_passwordFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Nombre;
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel cirifLabel;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

    private void doIt() {
        if ( idField.getText().isEmpty() ){
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "El usuario es obligatorio");
            msg.show(this);
            return;
        }
        try{

            if ( !ConnectionDrivers.existsUser(idField.getText().trim()) ){
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Usuario no existe");
                msg.show(this);
                passwordField.setEnabled(true);
                return;
            }

            if ( ConnectionDrivers.isLocked(idField.getText().trim()) ){
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Usuario bloqueado");
                msg.show(this);
                passwordField.setEnabled(true);
                return;
            }

            ConnectionDrivers.login(idField.getText(), passwordField.getPassword());

            User u = Shared.giveUser(ConnectionDrivers.listUsers(), idField.getText());
            if ( (funct.equals(Constants.changeQuant) && ConnectionDrivers.isAllowed(u.getPerfil(), "setQuant")) ||
                    (funct.equals(Constants.changeReceipt) && ConnectionDrivers.isAllowed(u.getPerfil(), "createCN")) ){
                Shared.userInsertedPasswordOk(idField.getText());
                passOk = true;
            }else{
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "El usuario no tiene permisos para esa operación");
                msg.show(this);
            }
            this.dispose();

        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Problemas con la base de datos",ex);
            msg.show(this);
        } catch (Exception ex) {
            String kindErr = "";

            if ( Constants.wrongPasswordMsg.equals(ex.getMessage()) ) {
                kindErr = Constants.wrongPasswordMsg;
            }

            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, kindErr);
            msg.show(this);

            if ( ex.getMessage().equals(Constants.wrongPasswordMsg) ){
                try {
                    Shared.userTrying(idField.getText());
                } catch (Exception ex1) {
                    msg = new MessageBox(MessageBox.SGN_DANGER,
                                (ex1.getMessage().equals(Constants.userLocked)? Constants.userLocked :"Error."),
                                ex1);
                    msg.show(null);
                    this.dispose();
                    Shared.reload();
                }
            }

        }
    }

}
