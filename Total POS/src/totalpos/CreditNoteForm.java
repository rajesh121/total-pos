/*
 * CreditNoteForm.java
 *
 * Created on 23-ago-2011, 8:44:40
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Saúl Hidalgo
 */
public class CreditNoteForm extends javax.swing.JDialog implements Doer{

    private Receipt receipt;
    private String actualId;
    private MainRetailWindows myParent;
    public boolean isOk = false;
    public boolean alreadyDone = false;
    public boolean totally = true;
    Client client = null;
    public Working workingFrame;

    /** Creates new form CreditNoteForm
     * @param parent
     * @param modal
     * @param r 
     */
    public CreditNoteForm(MainRetailWindows parent, boolean modal, Receipt r) {
        super(parent, modal);
        initComponents();
        try {
            receipt = r;
            myParent = parent;
            internIdField.setText(receipt.getInternId());
            fiscalNumberField.setText(receipt.getFiscalNumber());
            fiscalPrinterField.setText(receipt.getFiscalPrinter());
            totalField.setText(Constants.df.format(receipt.getTotalWithIva()));
            zReportField.setText(receipt.getzReportId());
            dateField.setText(Constants.sdfDateHour.format(receipt.getCreationDate()));
            List<Client> l = ConnectionDrivers.listClients(receipt.getClientId());

            // Client MUST be registred
            assert( l.size() >= 1);
            Client c = client = l.get(0);
            clientIDField.setText(c.getId());
            clientDescriptionField.setText(c.getAddress());
            clientNameField.setText(c.getName());
            clientPhoneField.setText(c.getPhone());
            updateAll();
            if ( totally ){
                MessageBox msb = new MessageBox(MessageBox.SGN_NOTICE, "Esta factura ha sido TOTALMENTE devuelta.");
                msb.show(this);
            } else if(alreadyDone){
                MessageBox msb = new MessageBox(MessageBox.SGN_NOTICE, "Esta factura ya tiene devoluciones parciales.");
                msb.show(this);
            }
            isOk = true;
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
    }

    public void updateAll(){
        //TODO Set status in a Constant File
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        if ( !receipt.getItems().isEmpty() && receipt.getStatus().equals("Facturada")){
            actualId = receipt.getInternId();
            model = (DefaultTableModel) table.getModel();

            for (Item2Receipt item2r : receipt.getItems()) {
                Item item = item2r.getItem();
                if ( item2r.getAntiQuant() != 0 ){
                    alreadyDone = true;
                }
                
                if ( item2r.getAntiQuant() != item2r.getQuant() ){
                    totally = false;
                }

                Object[] s = {"0",item2r.getQuant(),item2r.getAntiQuant(),item.getDescription(), item.getDescuento()+"", item.getLastPrice().toString(), item.getLastPrice().getIva().toString(), item.getLastPrice().plusIva().toString()};
                model.addRow(s);
            }
            table.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        codeLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        clientIDField = new javax.swing.JTextField();
        clientNameField = new javax.swing.JTextField();
        clientPhoneField = new javax.swing.JTextField();
        descriptionLabel = new javax.swing.JLabel();
        clientDescription = new javax.swing.JScrollPane();
        clientDescriptionField = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        internCode = new javax.swing.JLabel();
        internIdField = new javax.swing.JTextField();
        fiscalPrinter = new javax.swing.JLabel();
        fiscalPrinterField = new javax.swing.JTextField();
        fiscalNumber = new javax.swing.JLabel();
        fiscalNumberField = new javax.swing.JTextField();
        zReportField = new javax.swing.JTextField();
        zReport = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
        zReport1 = new javax.swing.JLabel();
        totalField = new javax.swing.JTextField();
        reverseALLButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nota de Crédito");

        jLabel1.setFont(new java.awt.Font("Courier New", 1, 18));
        jLabel1.setText("Crear Nota de Crédito");
        jLabel1.setName("jLabel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Devolver", "Cantidad", "Devuelto", "Descripción", "Descuento", "Precio", "IVA", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setName("table"); // NOI18N
        table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(table);

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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));
        jPanel1.setFocusable(false);
        jPanel1.setName("jPanel1"); // NOI18N

        codeLabel.setFont(new java.awt.Font("Courier New", 1, 12));
        codeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        codeLabel.setText("Código");
        codeLabel.setFocusable(false);
        codeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        codeLabel.setName("codeLabel"); // NOI18N

        nameLabel.setFont(new java.awt.Font("Courier New", 1, 12));
        nameLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        nameLabel.setText("Nombre");
        nameLabel.setFocusable(false);
        nameLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nameLabel.setName("nameLabel"); // NOI18N

        phoneLabel.setFont(new java.awt.Font("Courier New", 1, 12));
        phoneLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        phoneLabel.setText("Teléfono");
        phoneLabel.setFocusable(false);
        phoneLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        phoneLabel.setName("phoneLabel"); // NOI18N

        clientIDField.setEditable(false);
        clientIDField.setText("jTextField1");
        clientIDField.setFocusable(false);
        clientIDField.setName("clientIDField"); // NOI18N

        clientNameField.setEditable(false);
        clientNameField.setText("jTextField2");
        clientNameField.setFocusable(false);
        clientNameField.setName("clientNameField"); // NOI18N

        clientPhoneField.setEditable(false);
        clientPhoneField.setText("jTextField3");
        clientPhoneField.setFocusable(false);
        clientPhoneField.setName("clientPhoneField"); // NOI18N

        descriptionLabel.setFont(new java.awt.Font("Courier New", 1, 12));
        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 3x.jpg"))); // NOI18N
        descriptionLabel.setText("Dirección");
        descriptionLabel.setFocusable(false);
        descriptionLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setName("descriptionLabel"); // NOI18N

        clientDescription.setName("clientDescription"); // NOI18N

        clientDescriptionField.setColumns(20);
        clientDescriptionField.setEditable(false);
        clientDescriptionField.setRows(2);
        clientDescriptionField.setFocusable(false);
        clientDescriptionField.setName("clientDescriptionField"); // NOI18N
        clientDescription.setViewportView(clientDescriptionField);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(phoneLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(codeLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(clientPhoneField, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(clientNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(clientIDField, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clientDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(codeLabel)
                    .addComponent(clientIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descriptionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nameLabel)
                            .addComponent(clientNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(phoneLabel)
                            .addComponent(clientPhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(clientDescription, 0, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de Factura"));
        jPanel2.setFocusable(false);
        jPanel2.setName("jPanel2"); // NOI18N

        internCode.setFont(new java.awt.Font("Courier New", 1, 12));
        internCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        internCode.setText("Correlativo");
        internCode.setFocusable(false);
        internCode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        internCode.setName("internCode"); // NOI18N

        internIdField.setEditable(false);
        internIdField.setText("jTextField1");
        internIdField.setFocusable(false);
        internIdField.setName("internIdField"); // NOI18N

        fiscalPrinter.setFont(new java.awt.Font("Courier New", 1, 12));
        fiscalPrinter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        fiscalPrinter.setText("Impresora");
        fiscalPrinter.setFocusable(false);
        fiscalPrinter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fiscalPrinter.setName("fiscalPrinter"); // NOI18N

        fiscalPrinterField.setEditable(false);
        fiscalPrinterField.setText("jTextField1");
        fiscalPrinterField.setFocusable(false);
        fiscalPrinterField.setName("fiscalPrinterField"); // NOI18N

        fiscalNumber.setFont(new java.awt.Font("Courier New", 1, 12));
        fiscalNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        fiscalNumber.setText("Nro Fiscal");
        fiscalNumber.setFocusable(false);
        fiscalNumber.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fiscalNumber.setName("fiscalNumber"); // NOI18N

        fiscalNumberField.setEditable(false);
        fiscalNumberField.setText("jTextField1");
        fiscalNumberField.setFocusable(false);
        fiscalNumberField.setName("fiscalNumberField"); // NOI18N

        zReportField.setEditable(false);
        zReportField.setText("jTextField1");
        zReportField.setFocusable(false);
        zReportField.setName("zReportField"); // NOI18N

        zReport.setFont(new java.awt.Font("Courier New", 1, 12));
        zReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        zReport.setText("Reporte Z");
        zReport.setFocusable(false);
        zReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zReport.setName("zReport"); // NOI18N

        dateLabel.setFont(new java.awt.Font("Courier New", 1, 12));
        dateLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        dateLabel.setText("Momento");
        dateLabel.setFocusable(false);
        dateLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        dateLabel.setName("dateLabel"); // NOI18N

        dateField.setEditable(false);
        dateField.setText("jTextField1");
        dateField.setFocusable(false);
        dateField.setName("dateField"); // NOI18N

        zReport1.setFont(new java.awt.Font("Courier New", 1, 12));
        zReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        zReport1.setText("Total");
        zReport1.setFocusable(false);
        zReport1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zReport1.setName("zReport1"); // NOI18N

        totalField.setEditable(false);
        totalField.setText("jTextField1");
        totalField.setFocusable(false);
        totalField.setName("totalField"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(fiscalNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(internCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(internIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fiscalNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(fiscalPrinter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(zReport1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(zReport, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(totalField, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(fiscalPrinterField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(zReportField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dateField, fiscalNumberField, internIdField});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(internCode)
                            .addComponent(internIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fiscalPrinter))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(dateLabel)
                                        .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(zReport1)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fiscalNumber)
                                .addComponent(fiscalNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(fiscalPrinterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zReportField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(zReport))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        reverseALLButton.setText("Devolver todo");
        reverseALLButton.setName("reverseALLButton"); // NOI18N
        reverseALLButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reverseALLButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(reverseALLButton, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(acceptButton)
                    .addComponent(reverseALLButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        workingFrame = new Working(this);
        WaitSplash ws = new WaitSplash(this);

        Shared.centerFrame(workingFrame);
        workingFrame.setVisible(true);

        ws.execute();
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void tableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            workingFrame = new Working(this);
            WaitSplash ws = new WaitSplash(this);

            Shared.centerFrame(workingFrame);
            workingFrame.setVisible(true);

            ws.execute();
        } else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }
    }//GEN-LAST:event_tableKeyPressed

    private void reverseALLButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reverseALLButtonActionPerformed
        for (int i = 0; i < table.getRowCount(); ++i) {
            try{
                int antiQuantComplement = Integer.parseInt((table.getValueAt(i, 1)+""));
                int antiQuantComplementDone = Integer.parseInt((table.getValueAt(i, 2)+""));
                table.setValueAt((antiQuantComplement-antiQuantComplementDone)+"", i, 0);
            }catch ( NumberFormatException ex){
                MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Cantidad inválida! Item " + (i+1) + "!");
                msb.show(this);
                return;
            }
        }
    }//GEN-LAST:event_reverseALLButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane clientDescription;
    private javax.swing.JTextArea clientDescriptionField;
    private javax.swing.JTextField clientIDField;
    private javax.swing.JTextField clientNameField;
    private javax.swing.JTextField clientPhoneField;
    private javax.swing.JLabel codeLabel;
    private javax.swing.JTextField dateField;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JLabel fiscalNumber;
    private javax.swing.JTextField fiscalNumberField;
    private javax.swing.JLabel fiscalPrinter;
    private javax.swing.JTextField fiscalPrinterField;
    private javax.swing.JLabel internCode;
    private javax.swing.JTextField internIdField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JButton reverseALLButton;
    private javax.swing.JTable table;
    private javax.swing.JTextField totalField;
    private javax.swing.JLabel zReport;
    private javax.swing.JLabel zReport1;
    private javax.swing.JTextField zReportField;
    // End of variables declaration//GEN-END:variables

    public void close(){
        workingFrame.setVisible(false);
    }

    @Override
    public void doIt() {

        try {
            List<Item2Receipt> items = new ArrayList<Item2Receipt>();
            for (int i = 0; i < table.getRowCount(); ++i) {
                try{
                    // This part was a little annoying :-o!
                    // x2
                    int antiquant = Integer.parseInt(((table.getValueAt(i, 0)+"")));
                    int antiQuantComplement = Integer.parseInt((table.getValueAt(i, 1)+""));
                    int antiQuantComplementDone = Integer.parseInt((table.getValueAt(i, 2)+""));
                    int a = antiQuantComplement - antiQuantComplementDone;
                    if ( antiquant > a ){
                        MessageBox msb = null;
                        if ( a != 0 ){
                            msb = new MessageBox(MessageBox.SGN_CAUTION, "Debe seleccionar máximo " + (a) +" artículo(s) en el item " + (i+1) + "!");
                        }else{
                            msb = new MessageBox(MessageBox.SGN_CAUTION, "Ese producto ya fue devuelto!");
                        }
                        msb.show(this);
                        return;
                    }
                    if ( antiquant > 0 ) {
                        items.add(new Item2Receipt(receipt.getItems().get(i).getItem(),antiquant,0));
                    }
                }catch ( NumberFormatException ex){
                    MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Cantidad inválida! Item " + (i+1) + "!");
                    msb.show(this);
                    return;
                }
            }
            if (items.isEmpty()) {
                MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Debe seleccionar al menos un (1) artículo para devolver.");
                msb.show(this);
                return;
            }
            myParent.printer.printerSerial = null;
            if (!myParent.printer.checkPrinter()) {
                MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "La impresora no coincide con la registrada en el sistema. No se puede continuar");
                msb.show(null);
                return;
            }
            actualId = Shared.nextIdCN(0);
            ConnectionDrivers.createCreditNote(actualId, receipt.getInternId(), myParent.getUser().getLogin(), myParent.getAssign(), items);
            myParent.printer.printCreditNote(items, receipt.getInternId(), actualId, myParent.getUser(), client, receipt.getAlternativeID());

            ConnectionDrivers.setFiscalDataCN(actualId, myParent.printer.getSerial() , myParent.printer.getZ() , myParent.printer.getLastFiscalNumber());
            ConnectionDrivers.setPritingHour(actualId, "nota_de_credito");
            ConnectionDrivers.updateLastCN(myParent.printer.getLastFiscalNumber());

            MessageBox msb = new MessageBox(MessageBox.SGN_SUCCESS, "Recuerde no botar la factura ni la nota de crédito, ambas deben ser enviadas a la oficina principal.");
            msb.show(this);
            this.dispose();
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Problemas con la base de datos",ex);
            msb.show(this);
            myParent.dispose();
            Shared.reload();
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, Constants.errWithPrinter);
            msb.show(this);
            myParent.dispose();
            Shared.reload();
        } 
    }

}
