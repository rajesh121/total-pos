/*
 * AddReport.java
 *
 * Created on 02-sep-2011, 12:41:00
 */

package totalpos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JInternalFrame;

/**
 *
 * @author Saúl Hidalgo
 */
public class AddReport extends JInternalFrame {

    /** Creates new form AddReport */
    protected AddReport() {
        initComponents();
        titleLabel.setText("Agregar Reporte");
        titleField.setText("");
        columnField.setText("");
        parametersField.setText("");
        showNumbersCheckBox.setSelected(true);
        nameField.setText("");
        queryField.setText("");
    }

    /** Creates new form AddReport
     * @param report 
     */
    protected AddReport(Report report) {
        initComponents();
        titleLabel.setText("Modificar Reporte");
        titleField.setText(report.getTitle());
        columnField.setText(report.getColumns());
        parametersField.setText(report.getParameters());
        showNumbersCheckBox.setSelected(report.isShowNumbers());
        nameField.setText(report.getFile().getName());
        queryField.setText(report.getQuery());
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
        jLabel2 = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        columnField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        showNumbersCheckBox = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        parametersField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        queryField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();
        verticalLabel = new javax.swing.JLabel();
        orientationCombo = new javax.swing.JComboBox();

        setClosable(true);
        setIconifiable(true);
        setTitle("Agregar Reporte");

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        titleLabel.setText("Título Acá");
        titleLabel.setName("titleLabel"); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel2.setText("Título");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setName("jLabel2"); // NOI18N

        titleField.setText("jTextField1");
        titleField.setName("titleField"); // NOI18N

        columnField.setText("jTextField1");
        columnField.setName("columnField"); // NOI18N

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel3.setText("Columnas");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel4.setText("Núm de páginas");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel4.setName("jLabel4"); // NOI18N

        showNumbersCheckBox.setName("showNumbersCheckBox"); // NOI18N

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel5.setText("Parámetros");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel5.setName("jLabel5"); // NOI18N

        parametersField.setText("jTextField1");
        parametersField.setName("parametersField"); // NOI18N

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel6.setText("Consulta");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel6.setName("jLabel6"); // NOI18N

        queryField.setText("jTextField1");
        queryField.setName("queryField"); // NOI18N

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel7.setText("Nombre");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel7.setName("jLabel7"); // NOI18N

        nameField.setText("jTextField1");
        nameField.setName("nameField"); // NOI18N

        cancelButton.setText("Cancelar");
        cancelButton.setFocusable(false);
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        acceptButton.setText("Aceptar");
        acceptButton.setFocusable(false);
        acceptButton.setName("acceptButton"); // NOI18N
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        verticalLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        verticalLabel.setText("Orientación");
        verticalLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        verticalLabel.setName("verticalLabel"); // NOI18N

        orientationCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Vertical", "Horizontal" }));
        orientationCombo.setName("orientationCombo"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(titleField, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(columnField, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(showNumbersCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(parametersField, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(queryField, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(verticalLabel, 0, 0, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(orientationCombo, 0, 453, Short.MAX_VALUE)
                            .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(columnField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(showNumbersCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(parametersField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(queryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(verticalLabel)
                    .addComponent(orientationCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(acceptButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        if ( !nameField.getText().isEmpty() ){
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(Constants.reportFolder+"/"+nameField.getText()));
                out.write("Title==" + titleField.getText()+"\n");
                out.write("Columns=="+columnField.getText()+"\n");
                out.write("ShowNumbers=="+(showNumbersCheckBox.isSelected()?"True":"False")+"\n");
                out.write("Parameters=="+parametersField.getText()+"\n");
                out.write("SQL=="+queryField.getText()+"\n");
                out.write("Vertical=="+(orientationCombo.getSelectedIndex()!=0?"True":"False")+"\n");
                out.close();

                MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Guardado correctamente");
                msg.show(this);
                this.dispose();
            } catch (IOException ex) {
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Error al intentar escribir el archivo. No fue guardado.",ex);
                msg.show(this);
            }
            
        }else{
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "El nombre del archivo es inválido.");
            msg.show(this);
        }
    }//GEN-LAST:event_acceptButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField columnField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField nameField;
    private javax.swing.JComboBox orientationCombo;
    private javax.swing.JTextField parametersField;
    private javax.swing.JTextField queryField;
    private javax.swing.JCheckBox showNumbersCheckBox;
    private javax.swing.JTextField titleField;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel verticalLabel;
    // End of variables declaration//GEN-END:variables

}
