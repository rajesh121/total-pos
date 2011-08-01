/*
 * MainRetailWindows.java
 *
 * Created on 29-jul-2011, 15:41:13
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    /** Creates new form MainRetailWindows
     * @param parent
     * @param modal
     * @param u
     */
    public MainRetailWindows(User u) {
        initComponents();
        user = u;
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        updateAll();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    protected void updateAll(){
        descriptionLabel.setText("Bievenido a Mundo Total");
        currentPrice.setText("");
        subTotalLabel.setText("0.00 Bsf");
        items = new ArrayList<Item>();

        updateTable();
        
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
        subTotalLabel = new javax.swing.JLabel();
        logoLabel = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Constants.appName);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setName("jPanel1"); // NOI18N

        descriptionLabel.setFont(new java.awt.Font("Courier New", 0, 24)); // NOI18N
        descriptionLabel.setForeground(new java.awt.Color(255, 255, 255));
        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setName("descriptionLabel"); // NOI18N

        currentPrice.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        currentPrice.setForeground(new java.awt.Color(255, 255, 255));
        currentPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        currentPrice.setName("currentPrice"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                    .addComponent(currentPrice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(currentPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        gridTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Descripción", "Descuento", "Cantidad", "Precio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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
            .addGap(0, 448, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setName("jPanel5"); // NOI18N

        subTotalLabel.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        subTotalLabel.setForeground(new java.awt.Color(255, 255, 255));
        subTotalLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        subTotalLabel.setName("subTotalLabel"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subTotalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subTotalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(barcodeField, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barcodeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        logoLabel.setName("logoLabel"); // NOI18N

        javax.swing.GroupLayout logoLabelLayout = new javax.swing.GroupLayout(logoLabel);
        logoLabel.setLayout(logoLabelLayout);
        logoLabelLayout.setHorizontalGroup(
            logoLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 575, Short.MAX_VALUE)
        );
        logoLabelLayout.setVerticalGroup(
            logoLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );

        imageLabel.setName("imageLabel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                    .addComponent(logoLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void barcodeFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barcodeFieldKeyReleased
        
    }//GEN-LAST:event_barcodeFieldKeyReleased

    private void barcodeFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barcodeFieldKeyPressed
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
                if ( itemC.size() != 1 ){
                    System.out.println(itemC.size());
                    for (Item item : itemC) {
                        System.out.println(item);
                    }
                }
                assert( itemC.size() == 1 );
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
            if ( gridTable.getSelectedRow() != -1 ){

                items.remove(gridTable.getSelectedRow());

                DefaultTableModel model = (DefaultTableModel) gridTable.getModel();
                model.removeRow(gridTable.getSelectedRow());

                if ( items.isEmpty() ){
                    updateAll();
                }else{
                    gridTable.setRowSelectionInterval( model.getRowCount()-1, model.getRowCount()-1);

                    updateCurrentItem();
                    updateSubTotal();
                    cleanForNewItem();
                }
            }else{
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Debe seleccionar un artículo.");
                msb.show(this);
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel logoLabel;
    private javax.swing.JLabel subTotalLabel;
    // End of variables declaration//GEN-END:variables

    private void logout(){
        if ( JOptionPane.showConfirmDialog(MainWindows.mw, "¿Está seguro que desea cerrar sesión?") == 0 ){
            Login l = new Login();
            Shared.centerFrame(l);
            Shared.maximize(l);
            l.setVisible(true);
            ConnectionDrivers.user = null;

            setVisible(false);
            dispose();
        }
    }

    private void addItem(Item get) {
        DefaultTableModel model = (DefaultTableModel) gridTable.getModel();

        String s[] = {get.getDescription(),get.getDescuento(), quant+"" , get.getLastPrice().toString()};
        model.addRow(s);

        gridTable.setRowSelectionInterval( model.getRowCount()-1, model.getRowCount()-1);
        
        items.add(get);
        
    }

    private void cleanForNewItem(){
        quant = 1;
        barcodeField.setText("");
    }

    private void updateSubTotal() {
        DecimalFormat df = new DecimalFormat("#.00");
        double subT = .0;
        for (Item item : items) {
            subT += item.getLastPrice().getQuant();
        }
        subTotalLabel.setText(df.format(subT) + " Bsf");
    }

}
