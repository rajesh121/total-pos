/*
 * AddCard2Pay.java
 *
 * Created on 23-ago-2011, 10:22:50
 */

package totalpos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Saúl Hidalgo
 */
public class AddCard2Pay extends javax.swing.JDialog {

    private String reason;
    SpecifyPaymentForm myParent;
    List<BankPOS> bpos = new ArrayList<BankPOS>();
    public boolean isOk = false;
    
    /** Creates new form AddCard2Pay */
    public AddCard2Pay(SpecifyPaymentForm parent, boolean modal, String reasonI) {
        super(parent, modal);
        try {
            initComponents();
            reason = reasonI;
            myParent = parent;
            acceptButton.setMnemonic('A');
            cancelButton.setMnemonic('C');
            modifyLot.setMnemonic('M');
            updateAll();
            isOk = true;
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Error en la base de datos.",ex);
            msb.show(this);
            return;
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

        moneyLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        acceptButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        moneyField = new javax.swing.JTextField();
        posLabel = new javax.swing.JLabel();
        bposCombo = new java.awt.Choice();
        modifyLot = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agregar Forma de Pago");

        moneyLabel.setText("Dinero");
        moneyLabel.setName("moneyLabel"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Courier New", 1, 18));
        jLabel1.setText("Agregar Forma de Pago");
        jLabel1.setName("jLabel1"); // NOI18N

        acceptButton.setText("Aceptar");
        acceptButton.setFocusable(false);
        acceptButton.setName("acceptButton"); // NOI18N
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancelar");
        cancelButton.setFocusable(false);
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        moneyField.setName("moneyField"); // NOI18N

        posLabel.setText("Punto De Venta");
        posLabel.setName("posLabel"); // NOI18N

        bposCombo.setName("bposCombo"); // NOI18N

        modifyLot.setText("Cambiar Lote");
        modifyLot.setFocusable(false);
        modifyLot.setName("modifyLot"); // NOI18N
        modifyLot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyLotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(posLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(moneyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(modifyLot, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(acceptButton, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(moneyField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                            .addComponent(bposCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(moneyLabel)
                    .addComponent(moneyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(posLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bposCombo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(acceptButton)
                    .addComponent(modifyLot))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateAll() throws SQLException{
        bposCombo.removeAll();
        bpos = ConnectionDrivers.listBPos(Constants.myId, reason);
        for (BankPOS bankPOS : bpos) {
            bposCombo.add(bankPOS.getId() + " - " + bankPOS.getLot() );
        }
    }

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        Double d = .0;
        try{
            d = Double.parseDouble(moneyField.getText().replace(',', '.'));
        }catch ( NumberFormatException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Monto incorrecto");
            msb.show(this);
            return;
        }
        Double minimun = .0;
        try{
            minimun = Double.parseDouble(Shared.getConfig(reason));
        }catch( NumberFormatException ex ){
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Error en la configuración del monto mínimo para tarjetas de tipo: "
                    + reason + ". Se asume mínimo 1 Bsf");
            msb.show(this);
            minimun = 1.0;
        }
        if ( d < minimun ){
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Monto incorrecto. Debe ser al menos " + minimun + " Bsf.");
            msb.show(this);
            return;
        }
        if ( bposCombo.getSelectedIndex() == -1 ){
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Debe seleccionar el punto de venta!");
            msb.show(this);
            return;
        }
        myParent.add(reason,d,bpos.get(bposCombo.getSelectedIndex()));
        this.dispose();
}//GEN-LAST:event_acceptButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
}//GEN-LAST:event_cancelButtonActionPerformed

    private void modifyLotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyLotActionPerformed
        if ( bposCombo.getSelectedIndex() == -1 ){
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Debe seleccionar el punto de venta!");
            msb.show(this);
            return;
        }
        BankPOS bp = bpos.get(bposCombo.getSelectedIndex());
        String id = JOptionPane.showInputDialog(this, "Nuevo Lote", bp.getLot());
        if ( id != null && !id.isEmpty() ){
            try {
                ConnectionDrivers.changeLot(bp.getId(), id);
                updateAll();
            } catch (SQLException ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            }
        }
    }//GEN-LAST:event_modifyLotActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private java.awt.Choice bposCombo;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton modifyLot;
    private javax.swing.JTextField moneyField;
    private javax.swing.JLabel moneyLabel;
    private javax.swing.JLabel posLabel;
    // End of variables declaration//GEN-END:variables

}
