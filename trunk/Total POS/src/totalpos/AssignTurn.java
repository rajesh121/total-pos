/*
 * AssignTurn.java
 *
 * Created on 03-ago-2011, 8:45:06
 */

package totalpos;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Saúl Hidalgo
 */
public class AssignTurn extends javax.swing.JInternalFrame {

    private List<Turn> turns;
    private List<PointOfSale> poses;

    /** Creates new form AssignTurn */
    public AssignTurn() {
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

        titleLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        turnCombo = new javax.swing.JComboBox();
        posCombo = new javax.swing.JComboBox();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Asignar Turnos");

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 24));
        titleLabel.setText("Asignar Turnos");
        titleLabel.setName("titleLabel"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel1.setText("Turno");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel2.setText("Caja");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setName("jLabel2"); // NOI18N

        turnCombo.setName("turnCombo"); // NOI18N

        posCombo.setName("posCombo"); // NOI18N

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
                    .addComponent(titleLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(posCombo, 0, 224, Short.MAX_VALUE)
                            .addComponent(turnCombo, 0, 224, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(turnCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(posCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(acceptButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        doIt();
    }//GEN-LAST:event_acceptButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox posCombo;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JComboBox turnCombo;
    // End of variables declaration//GEN-END:variables

    private void doIt() {
        try {
            Assign a = new Assign(turns.get(turnCombo.getSelectedIndex()).getIdentificador(), poses.get(posCombo.getSelectedIndex()).getId(), null, true);
            ConnectionDrivers.createAssign(a);
            MessageBox msb = new MessageBox(MessageBox.SGN_SUCCESS, "Guardado satisfactoriamente");
            msb.show(this);

            setVisible(false);
            this.dispose();
        } catch (NumberFormatException ex){
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Error en el monto de la caja.");
            msb.show(this);
        } catch (SQLException ex) {
            if ( ex.getMessage().matches(Constants.isDataRepeated) ){
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Asignación ya existente o algún turno está solapado. Intente otro.");
                msb.show(this);
            }else{
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            }
        }
    }

    private void updateAll() {
        try {
            turns = ConnectionDrivers.listTurns();
            poses = ConnectionDrivers.listPointOfSales(true);

            for (Turn t : turns) {
                turnCombo.addItem( "(" + t.getIdentificador() + ") " + Constants.sdfHour.format(t.getInicio()) + " -> " + Constants.sdfHour.format(t.getFin()));
            }

            for (PointOfSale pointOfSale : poses) {
                // Should it be in the driver?? Maybe :-o
                if ( pointOfSale.isEnabled() ){
                    posCombo.addItem(pointOfSale.getId());
                }
            }

        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
            msg.show(this);
            this.dispose();
            Shared.reload();
        }
    }

}
