/*
 * MainRetailWindows.java
 *
 * Created on 29-jul-2011, 15:41:13
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author shidalgo
 */
public class MainRetailWindows extends javax.swing.JFrame {

    private User user;
    protected int quant = 1;
    private List<Item> items;
    private String actualId;
    FiscalPrinter printer;
    public boolean isOk = false;

    /** Creates new form MainRetailWindows
     * @param u
     */
    public MainRetailWindows(User u) {
        try {
            initComponents();
            this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            user = u;
            printer = new FiscalPrinter();
            updateAll();
            if ( !checkPrinter() ){
                MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "La impresora no coincide con la registrada en el sistema. No se puede continuar");
                msb.show(null);
                this.dispose();
                Shared.reload();
            }else{
                isOk = true;
            }
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
            msb.show(null);
            this.dispose();
            Shared.reload();
        } catch (FileNotFoundException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas en la comunicación con la impresora.",ex);
            msb.show(null);
            this.dispose();
            Shared.reload();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    protected void updateAll(){
        try {
            descriptionLabel.setText("Bievenido a Mundo Total");
            currentPrice.setText("");
            ivaLabelResult.setText("0.00 Bsf");
            ivaLabelResult1.setText("0.00 Bsf");
            subTotalLabelResult.setText("0.00 Bsf");
            items = new ArrayList<Item>();
            imageLabel.setVisible(false);

            updateTable();

            actualId = nextId();
            ConnectionDrivers.createReceipt(actualId, user.getLogin());
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas al creando el recibo.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
    }

    protected void updateTable(){
        DefaultTableModel model = (DefaultTableModel) gridTable.getModel();
        model.setRowCount(0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        descriptionLabel = new javax.swing.JLabel();
        currentPrice = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        gridTable = new javax.swing.JTable();
        barcodeField = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        ivaLabel = new javax.swing.JLabel();
        ivaLabelResult = new javax.swing.JLabel();
        subTotalLabelResult = new javax.swing.JLabel();
        subTotalLabel = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        ivaLabelResult1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Constants.appName);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setName("jPanel1"); // NOI18N

        descriptionLabel.setFont(new java.awt.Font("Courier New", 0, 24));
        descriptionLabel.setForeground(new java.awt.Color(255, 255, 255));
        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setName("descriptionLabel"); // NOI18N

        currentPrice.setFont(new java.awt.Font("Courier New", 1, 24));
        currentPrice.setForeground(new java.awt.Color(255, 255, 255));
        currentPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        currentPrice.setName("currentPrice"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(currentPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        gridTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Descripción", "Descuento", "Precio", "Iva", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        gridTable.setFocusable(false);
        gridTable.setName("gridTable"); // NOI18N
        gridTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(gridTable);
        gridTable.getColumnModel().getColumn(0).setPreferredWidth(300);

        barcodeField.setName("barcodeField"); // NOI18N
        barcodeField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                barcodeFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                barcodeFieldKeyReleased(evt);
            }
        });

        jPanel4.setName("jPanel4"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 514, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setName("jPanel5"); // NOI18N

        ivaLabel.setFont(new java.awt.Font("Courier New", 1, 24));
        ivaLabel.setForeground(new java.awt.Color(255, 255, 255));
        ivaLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ivaLabel.setText("Iva:");
        ivaLabel.setName("ivaLabel"); // NOI18N

        ivaLabelResult.setFont(new java.awt.Font("Courier New", 1, 24));
        ivaLabelResult.setForeground(new java.awt.Color(255, 255, 255));
        ivaLabelResult.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ivaLabelResult.setName("ivaLabelResult"); // NOI18N

        subTotalLabelResult.setFont(new java.awt.Font("Courier New", 1, 24));
        subTotalLabelResult.setForeground(new java.awt.Color(255, 255, 255));
        subTotalLabelResult.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        subTotalLabelResult.setName("subTotalLabelResult"); // NOI18N

        subTotalLabel.setFont(new java.awt.Font("Courier New", 1, 24));
        subTotalLabel.setForeground(new java.awt.Color(255, 255, 255));
        subTotalLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        subTotalLabel.setText("SubTotal:");
        subTotalLabel.setName("subTotalLabel"); // NOI18N

        totalLabel.setFont(new java.awt.Font("Courier New", 1, 24));
        totalLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalLabel.setText("Total:");
        totalLabel.setName("totalLabel"); // NOI18N

        ivaLabelResult1.setFont(new java.awt.Font("Courier New", 1, 24));
        ivaLabelResult1.setForeground(new java.awt.Color(255, 255, 255));
        ivaLabelResult1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ivaLabelResult1.setName("ivaLabelResult1"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subTotalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ivaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ivaLabelResult, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(subTotalLabelResult, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(totalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ivaLabelResult1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subTotalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subTotalLabelResult, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(ivaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(ivaLabelResult, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ivaLabelResult1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                    .addComponent(barcodeField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barcodeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Imagen"));
        jPanel3.setName("jPanel3"); // NOI18N

        imageLabel.setName("imageLabel"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void barcodeFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barcodeFieldKeyReleased
        
    }//GEN-LAST:event_barcodeFieldKeyReleased

    private void barcodeFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barcodeFieldKeyPressed
        Shared.getScreenSaver().actioned();
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            logout();
            return;
        }else if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            try {
                List<Item> itemC = ConnectionDrivers.listItems(barcodeField.getText(), "", "", "");
                if ( itemC.isEmpty() ){
                    MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Artículo no existe!");
                    msb.show(this);
                    cleanForNewItem();
                    return;
                }
                assert( itemC.size() == 1 );
                if ( itemC.get(0).isStatus() ){
                    MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Artículo bloqueado. No puede ser facturado!");
                    msb.show(this);
                    cleanForNewItem();
                    return;
                }
                addItem(itemC.get(0));
                updateCurrentItem();
                cleanForNewItem();
                updateSubTotal();
                
            } catch (SQLException ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
                msb.show(this);
            } catch (Exception ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas al listar artículos.",ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            }
        } else if ( evt.getKeyCode() == KeyEvent.VK_DOWN ){
            if ( gridTable.getSelectedRow() == gridTable.getModel().getRowCount() - 1 ){
                return;
            }
            gridTable.setRowSelectionInterval(gridTable.getSelectedRow()+1, gridTable.getSelectedRow()+1);
            updateCurrentItem();
        } else if ( evt.getKeyCode() == KeyEvent.VK_UP ){
            if ( gridTable.getSelectedRow() <= 0 ){
                return;
            }
            gridTable.setRowSelectionInterval(gridTable.getSelectedRow()-1, gridTable.getSelectedRow()-1);
            updateCurrentItem();
        }else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE ){
            Object[] options = {"Si",
                    "No"};
            int n = JOptionPane.showOptionDialog(this,
                "¿Desea eliminar el artículo?",
                Constants.appName,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

            if ( n == 0 ){
                deleteItem();
            }
        } else if ( evt.getKeyCode() == KeyEvent.VK_F10 ){
            if ( items.isEmpty() ){
                return;
            }
            Object[] options = {"Si",
                    "No"};
            int n = JOptionPane.showOptionDialog(this,
                "¿Desea eliminar el pedido?",
                Constants.appName,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
            if ( n == 0 ){
                deleteCurrent();
            }
        } else if ( evt.getKeyCode() == KeyEvent.VK_F11 ){
            toWait();
            updateAll();
        } else if ( evt.getKeyCode() == KeyEvent.VK_F12 ){
            ListIdleReceipts lir = new ListIdleReceipts(this, true);
            if ( lir.isOk ){
                Shared.centerFrame(lir);
                lir.setVisible(true);
            }
        }
    }//GEN-LAST:event_barcodeFieldKeyPressed

    private void updateCurrentItem(){
        Item i = items.get(gridTable.getSelectedRow());
        descriptionLabel.setText(i.getDescription());
        currentPrice.setText(i.getLastPrice().toString());
        Shared.loadPhoto(imageLabel, i.getImageAddr());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField barcodeField;
    private javax.swing.JLabel currentPrice;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTable gridTable;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel ivaLabel;
    private javax.swing.JLabel ivaLabelResult;
    private javax.swing.JLabel ivaLabelResult1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel subTotalLabel;
    private javax.swing.JLabel subTotalLabelResult;
    private javax.swing.JLabel totalLabel;
    // End of variables declaration//GEN-END:variables

    private void logout(){
        if ( JOptionPane.showConfirmDialog( (Shared.getMyMainWindows()) , "¿Está seguro que desea cerrar sesión?") == 0 ){
            Login l = new Login();
            Shared.centerFrame(l);
            Shared.maximize(l);
            l.setVisible(true);
            Shared.setUser(null);

            setVisible(false);
            dispose();
        }
    }

    private void addItem(Item get) {
        try {
            ConnectionDrivers.addItem2Receipt(actualId, get);
            DefaultTableModel model = (DefaultTableModel) gridTable.getModel();

            String[] s = {get.getDescription(), get.getDescuento(), get.getLastPrice().toString(), get.getLastPrice().getIva().toString(), get.getLastPrice().plusIva().toString()};
            model.addRow(s);
            gridTable.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
            items.add(get);
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
            msb.show(this);
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas al agregar el artículo",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
        
    }

    private void cleanForNewItem(){
        quant = 1;
        barcodeField.setText("");
    }

    private void updateSubTotal() {
        DecimalFormat df = new DecimalFormat("#.00");
        double subT = .0 , ivaT = .0 , total = .0;
        for (Item item : items) {
            subT += item.getLastPrice().getQuant();
            ivaT += item.getLastPrice().getIva().getQuant();
            total += item.getLastPrice().plusIva().getQuant();
        }
        subTotalLabelResult.setText(df.format(subT) + " Bsf");
        ivaLabelResult.setText(df.format(ivaT) + " Bsf");
        ivaLabelResult1.setText(df.format(total) + " Bsf");
    }

    private String nextId(){
        try {
            int rightNow = ConnectionDrivers.lastReceiptToday();

            Date d = ConnectionDrivers.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(d)+ Constants.myId + rightNow;
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
            return "";
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas al listar calcular el siguiente código de factura.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
            return "";
        }
        
    }

    private void deleteItem() {
        if ( gridTable.getSelectedRow() != -1){
            try {
                ConnectionDrivers.deleteItem2Receipt(actualId, items.get(gridTable.getSelectedRow()));

                items.remove(gridTable.getSelectedRow());
                DefaultTableModel model = (DefaultTableModel) gridTable.getModel();
                model.removeRow(gridTable.getSelectedRow());
                if (items.isEmpty()) {
                    try {
                        ConnectionDrivers.cancelReceipt(actualId);
                        MessageBox msb = new MessageBox(MessageBox.SGN_SUCCESS, "Pedido anulado.");
                        msb.show(this);
                        updateAll();
                    } catch (SQLException ex) {
                        MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
                        msb.show(this);
                        this.dispose();
                        Shared.reload();
                    }
                } else {
                    gridTable.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
                    updateCurrentItem();
                    updateSubTotal();
                    cleanForNewItem();
                }
            } catch (SQLException ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            } catch (Exception ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas al eliminar artículo de la factura.",ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            }
        }else{
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Debe seleccionar un artículo.");
            msb.show(this);
        }
    }

    private void toWait() {
        try {
            if ( !items.isEmpty() ){
                ConnectionDrivers.putToIdle(actualId);
                MessageBox msb = new MessageBox(MessageBox.SGN_SUCCESS, "Pedido puesto en espera.");
                msb.show(this);
            }
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
    }

    public void loadThisReceipt(Receipt r){
        if ( !r.getItems().isEmpty() ){
            actualId = r.getInternId();
            DefaultTableModel model = (DefaultTableModel) gridTable.getModel();

            for (Item item : r.getItems()) {
                String[] s = {item.getDescription(), item.getDescuento(), item.getLastPrice().toString(), item.getLastPrice().getIva().toString(), item.getLastPrice().plusIva().toString()};
                model.addRow(s);
                items.add(item);
            }
            gridTable.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
            updateCurrentItem();
            updateSubTotal();
        }
    }

    private boolean checkPrinter() throws SQLException, FileNotFoundException {
        String p = ConnectionDrivers.getMyPrinter();
        return printer.isTheSame(p);
    }

    public void deleteCurrent(){
        while( !items.isEmpty() ){
            deleteItem();
        }
    }
}