/*
 * ManageClient.java
 *
 * Created on 11-ago-2011, 10:09:43
 */

package totalpos;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Saúl Hidalgo
 */
public class ManageClient extends javax.swing.JDialog {

    private boolean found = false;
    private MainRetailWindows parent;
    private boolean modified = false;
    public boolean isOk = false;

    /** Creates new form ManageClient */
    public ManageClient(Frame parent, boolean modal, Client c) {
        super(parent, modal);
        initComponents();
        this.parent = (MainRetailWindows) parent;
        modifyClient.setVisible(false);
        if ( c != null ){
            try {
                searchIt(c.getId());
                if (found) {
                    idField.setText(c.getId());
                }
                isOk = true;
            } catch (SQLException ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            }
        }
        isOk = true;
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
        cirifLabel = new javax.swing.JLabel();
        Nombre = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        nameField = new javax.swing.JTextField();
        phoneField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        modifyClient = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        comboKind = new javax.swing.JComboBox();
        addressField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Clientes");
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        titleLabel.setText("Clientes");
        titleLabel.setName("titleLabel"); // NOI18N

        cirifLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        cirifLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        cirifLabel.setText("R.I.F.");
        cirifLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cirifLabel.setName("cirifLabel"); // NOI18N

        Nombre.setFont(new java.awt.Font("Courier New", 0, 12));
        Nombre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        Nombre.setText("Nombre");
        Nombre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Nombre.setName("Nombre"); // NOI18N

        addressLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        addressLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        addressLabel.setText("Ciudad");
        addressLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addressLabel.setName("addressLabel"); // NOI18N

        phoneLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        phoneLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        phoneLabel.setText("Teléfono");
        phoneLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        phoneLabel.setName("phoneLabel"); // NOI18N

        idField.setFont(new java.awt.Font("Courier New", 0, 12));
        idField.setName("idField"); // NOI18N
        idField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idFieldActionPerformed(evt);
            }
        });
        idField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                idFieldFocusLost(evt);
            }
        });
        idField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                idFieldKeyReleased(evt);
            }
        });

        nameField.setFont(new java.awt.Font("Courier New", 0, 12));
        nameField.setName("nameField"); // NOI18N
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameFieldFocusGained(evt);
            }
        });
        nameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameFieldKeyReleased(evt);
            }
        });

        phoneField.setFont(new java.awt.Font("Courier New", 0, 12));
        phoneField.setName("phoneField"); // NOI18N
        phoneField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                phoneFieldKeyPressed(evt);
            }
        });

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

        jLabel1.setText("* = Campo Obligatorio");
        jLabel1.setName("jLabel1"); // NOI18N

        modifyClient.setText("Modificar");
        modifyClient.setName("modifyClient"); // NOI18N
        modifyClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyClientActionPerformed(evt);
            }
        });

        jLabel9.setText("*");
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText("*");
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText("*");
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel12.setText("*");
        jLabel12.setName("jLabel12"); // NOI18N

        comboKind.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "V", "G", "J" }));
        comboKind.setName("comboKind"); // NOI18N
        comboKind.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboKindKeyPressed(evt);
            }
        });

        addressField.setName("addressField"); // NOI18N
        addressField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                addressFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(cirifLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel9))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(phoneLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                                        .addComponent(Nombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel10)))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(addressLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12)))
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(modifyClient, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(nameField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                            .addComponent(phoneField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(comboKind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(idField, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
                            .addComponent(addressField, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cirifLabel)
                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(comboKind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Nombre)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneLabel)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressLabel)
                    .addComponent(jLabel12)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(acceptButton)
                    .addComponent(jLabel1)
                    .addComponent(modifyClient)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void searchIt(String clientId) throws SQLException{
        List<Client> clients = ConnectionDrivers.listClients(clientId);
        if ( clients.isEmpty() ){
            nameField.setText("");
            phoneField.setText("");
            addressField.setText("");
            nameField.setEditable(true);
            phoneField.setEditable(true);
            addressField.setEditable(true);
            modifyClient.setVisible(false);
            found = false;
        }else if ( clients.size() == 1 ){
            Client c = clients.get(0);
            nameField.setText(c.getName());
            phoneField.setText(c.getPhone());
            addressField.setText(c.getAddress());
            nameField.setEditable(false);
            phoneField.setEditable(false);
            addressField.setEditable(false);
            found = true;
            modifyClient.setVisible(true);
        }else{
            // This section shouldn't be reached.
            // There are 2 clients with the same RIF.
            found = false;
            assert( false );
        }
    }

    private void idFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idFieldFocusLost
        
    }//GEN-LAST:event_idFieldFocusLost

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        doIt();
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void idFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idFieldKeyPressed
        Shared.getScreenSaver().actioned();
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            nameField.requestFocus();
        }else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }
    }//GEN-LAST:event_idFieldKeyPressed

    private void nameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameFieldKeyPressed
        Shared.getScreenSaver().actioned();
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }else if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            phoneField.requestFocus();
        }
    }//GEN-LAST:event_nameFieldKeyPressed

    private void phoneFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phoneFieldKeyPressed
        Shared.getScreenSaver().actioned();
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }else if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            addressField.requestFocus();
        }
    }//GEN-LAST:event_phoneFieldKeyPressed

    private void modifyClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyClientActionPerformed
        modified = true;
        nameField.setEditable(true);
        phoneField.setEditable(true);
        addressField.setEditable(true);
        nameField.requestFocus();
    }//GEN-LAST:event_modifyClientActionPerformed

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameFieldActionPerformed

    private void nameFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldFocusGained
        if ( validateRif() ){
            try {
                searchIt(comboKind.getSelectedItem().toString() + idField.getText());
            } catch (SQLException ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            }
        }
    }//GEN-LAST:event_nameFieldFocusGained

    private void idFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idFieldKeyReleased
        if ( idField.getText().length() > 9 ){
            idField.setText(idField.getText().substring(0, 9));
        }

        String t = idField.getText().toString();
        idField.setText("");
        for (char d : t.toCharArray()) {
            if ( Character.isDigit(d) ){
                idField.setText(idField.getText() + d);
            }
        }

    }//GEN-LAST:event_idFieldKeyReleased

    private void comboKindKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboKindKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }else if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            idField.requestFocus();
        }
    }//GEN-LAST:event_comboKindKeyPressed

    private void idFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idFieldActionPerformed

    private void addressFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addressFieldKeyReleased

        if ( addressField.getText().length() > 30 ){
            addressField.setText(addressField.getText().substring(0,30));
        }

        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }else if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            acceptButton.requestFocus();
        }
    }//GEN-LAST:event_addressFieldKeyReleased

    private void nameFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameFieldKeyReleased

        if ( idField.getText().length() > 9 ){
            idField.setText(idField.getText().substring(0, 9));
        }
        if ( nameField.getText().length() > 35 ){
            nameField.setText(nameField.getText().substring(0,35));
        }
    }//GEN-LAST:event_nameFieldKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Nombre;
    private javax.swing.JButton acceptButton;
    private javax.swing.JTextField addressField;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel cirifLabel;
    private javax.swing.JComboBox comboKind;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton modifyClient;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField phoneField;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

    private boolean validateRif(){
        idField.setText(idField.getText().toUpperCase());
        String text = idField.getText();
        if ( !text.matches("([0-9]+)")){
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Especifique el rif!!");
            idField.requestFocus();
            msb.show(this);
            return false;
        }
        idField.setText(("00000000" + text).substring(text.length()-1));
        return true;
    }

    private void doIt() {
        Shared.getScreenSaver().actioned();

        if ( ! validateRif() ){
            idField.setSelectionStart(0);
            idField.setSelectionEnd(idField.getText().length());
            idField.requestFocus();
            return;
        }
        if ( nameField.getText().isEmpty() || addressField.getText().isEmpty()
                || phoneField.getText().isEmpty() ){
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Todos los campos son obligatorios");
            msb.show(this);
            return;
        }

        Client myClient = new Client(comboKind.getSelectedItem().toString() + idField.getText(),
                nameField.getText(), addressField.getText(), phoneField.getText());

        try {
            if ( !found ){
                ConnectionDrivers.createClient(myClient);
            } else if ( modified ){
                ConnectionDrivers.modifyClient(myClient);
            }
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
        parent.setClient(myClient.getId().isEmpty()?null:myClient);
        this.dispose();
    }
    
}
