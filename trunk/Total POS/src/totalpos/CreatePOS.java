/*
 * CreatePOS.java
 *
 * Created on 03-ago-2011, 16:30:54
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;

/**
 *
 * @author Saúl Hidalgo
 */
public class CreatePOS extends javax.swing.JInternalFrame {

    private boolean modify; // To save code xDD.

    /** Creates new form CreatePOS */
    public CreatePOS() {
        initComponents();
    }

    public CreatePOS(PointOfSale pos) {
        initComponents();
        modify = true;
        titleLabel.setText("Modificar Caja");
        numberField.setText(pos.getId());
        fiscalPrinterField.setText(pos.getPrinter());
        locateField.setText(pos.getDescription());

        numberField.setEditable(false);
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
        numberLabel = new javax.swing.JLabel();
        locateLabel = new javax.swing.JLabel();
        printerLabel = new javax.swing.JLabel();
        numberField = new javax.swing.JTextField();
        locateField = new javax.swing.JTextField();
        fiscalPrinterField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Crear Caja");

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 24));
        titleLabel.setText("Crear Caja");
        titleLabel.setName("titleLabel"); // NOI18N

        numberLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        numberLabel.setText("Número *");
        numberLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        numberLabel.setName("numberLabel"); // NOI18N

        locateLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        locateLabel.setText("Ubicación");
        locateLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        locateLabel.setName("locateLabel"); // NOI18N

        printerLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        printerLabel.setText("Impresora Fiscal");
        printerLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printerLabel.setName("printerLabel"); // NOI18N

        numberField.setName("numberField"); // NOI18N

        locateField.setName("locateField"); // NOI18N

        fiscalPrinterField.setName("fiscalPrinterField"); // NOI18N
        fiscalPrinterField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fiscalPrinterFieldActionPerformed(evt);
            }
        });
        fiscalPrinterField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fiscalPrinterFieldKeyPressed(evt);
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
                    .addComponent(titleLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(numberLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(locateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(printerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fiscalPrinterField, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                            .addComponent(locateField, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                            .addComponent(numberField, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(numberLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(locateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(printerLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(numberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(locateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fiscalPrinterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        doIt();
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void fiscalPrinterFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fiscalPrinterFieldActionPerformed
        
    }//GEN-LAST:event_fiscalPrinterFieldActionPerformed

    private void fiscalPrinterFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fiscalPrinterFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            doIt();
        }
    }//GEN-LAST:event_fiscalPrinterFieldKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField fiscalPrinterField;
    private javax.swing.JTextField locateField;
    private javax.swing.JLabel locateLabel;
    private javax.swing.JTextField numberField;
    private javax.swing.JLabel numberLabel;
    private javax.swing.JLabel printerLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

    private void doIt() {
        try {
            if ( numberField.getText().isEmpty() ){
                MessageBox msb = new MessageBox(MessageBox.SGN_SUCCESS, "El número no puede ser vacío");
                msb.show(this);
                return;
            }
            if ( modify ){
                ConnectionDrivers.modifyPos(numberField.getText(), locateField.getText(), fiscalPrinterField.getText());
            }else{
                ConnectionDrivers.createPos(numberField.getText(), locateField.getText(), fiscalPrinterField.getText());
            }
            MessageBox msb = new MessageBox(MessageBox.SGN_SUCCESS, "Guardado satisfactoriamente.");
            msb.show(this);
            this.setVisible(false);
            this.dispose();
        } catch (SQLException ex) {
            if ( ex.getMessage().matches(Constants.isDataRepeated) ){
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Perfil ya existente. Intente otro.");
                msb.show(this);
            }else{
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            }
        }
    }

}