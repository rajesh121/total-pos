/*
 * SearchItem.java
 *
 * Created on 11-oct-2011, 8:19:01
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
public class SearchItem extends javax.swing.JDialog {

    List<Item> items = new ArrayList<Item>();
    public boolean isOk = false;
    MainRetailWindows parent;

    /** Creates new form SearchItem */
    public SearchItem(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        try {
            initComponents();
            updateAll();
            this.parent = (MainRetailWindows)parent;
            isOk = true;
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
            msb.show(Shared.getMyMainWindows());
            this.dispose();
            Shared.reload();
        }
    }

    void updateAll() throws SQLException{
        items = ConnectionDrivers.listItemsByModel(modelField.getText());

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setRowCount(0);

        for (Item item : items) {
            String s[] = {item.getCode(),item.getDescription(),item.getMark(),item.getSector(),
                item.getModel(),item.getDescuento()+"",item.getLastPrice().plusIva().toString(),item.getMainBarcode()};
            model.addRow(s);
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
        filterFrame = new javax.swing.JPanel();
        modelLabel = new javax.swing.JLabel();
        modelField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        imageFrame = new javax.swing.JPanel();
        imagePanel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Buscar Articulo");
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        titleLabel.setText("Buscar Artículo");
        titleLabel.setName("titleLabel"); // NOI18N

        filterFrame.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Courier New", 0, 12))); // NOI18N
        filterFrame.setName("filterFrame"); // NOI18N

        modelLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        modelLabel.setText("Modelo");
        modelLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        modelLabel.setName("modelLabel"); // NOI18N

        modelField.setName("modelField"); // NOI18N
        modelField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                modelFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                modelFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout filterFrameLayout = new javax.swing.GroupLayout(filterFrame);
        filterFrame.setLayout(filterFrameLayout);
        filterFrameLayout.setHorizontalGroup(
            filterFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(modelLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modelField, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                .addContainerGap())
        );
        filterFrameLayout.setVerticalGroup(
            filterFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterFrameLayout.createSequentialGroup()
                .addGroup(filterFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modelLabel)
                    .addComponent(modelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setFocusable(false);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Descripcion", "Marca", "Sector", "Modelo", "Descuento", "Precio", "Codigo Barras"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setFocusable(false);
        table.setName("table"); // NOI18N
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(6).setPreferredWidth(50);

        imageFrame.setBorder(javax.swing.BorderFactory.createTitledBorder("Foto"));
        imageFrame.setFocusable(false);
        imageFrame.setName("imageFrame"); // NOI18N

        imagePanel.setName("imagePanel"); // NOI18N

        javax.swing.GroupLayout imageFrameLayout = new javax.swing.GroupLayout(imageFrame);
        imageFrame.setLayout(imageFrameLayout);
        imageFrameLayout.setHorizontalGroup(
            imageFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
        );
        imageFrameLayout.setVerticalGroup(
            imageFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel1.setText("F5 / Cargar");
        jLabel1.setFocusable(false);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel2.setText("ESC / Salir");
        jLabel2.setFocusable(false);
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel3.setText("Enter / Actualizar");
        jLabel3.setFocusable(false);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel)
                    .addComponent(filterFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(imageFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(imageFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(filterFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void modelFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_modelFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            try {
                updateAll();
            } catch (SQLException ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            }
        } else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        } else if ( evt.getKeyCode() == KeyEvent.VK_DOWN ){
            if ( table.getSelectedRow() == table.getModel().getRowCount() - 1 ){
                return;
            }
            table.setRowSelectionInterval(table.getSelectedRow()+1, table.getSelectedRow()+1);
            Shared.checkVisibility(table);
        } else if ( evt.getKeyCode() == KeyEvent.VK_UP ){
            if ( table.getSelectedRow() <= 0 ){
                return;
            }
            table.setRowSelectionInterval(table.getSelectedRow()-1, table.getSelectedRow()-1);
            Shared.checkVisibility(table);
        } else if ( evt.getKeyCode() == KeyEvent.VK_F5 ){
            if ( table.getSelectedRow() != -1 ){
                this.dispose();
                parent.loadItem((String) table.getValueAt(table.getSelectedRow(), 7));
            }
        }
    }//GEN-LAST:event_modelFieldKeyPressed

    private void modelFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_modelFieldKeyReleased
        if ( table.getSelectedRow() != -1 ){
            Item i = items.get(table.getSelectedRow());
            Shared.loadPhoto(imagePanel,i.getImageAddr(),Math.max(imagePanel.getWidth(),1),getHeight()-150);
        }
    }//GEN-LAST:event_modelFieldKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel filterFrame;
    private javax.swing.JPanel imageFrame;
    private javax.swing.JLabel imagePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField modelField;
    private javax.swing.JLabel modelLabel;
    private javax.swing.JTable table;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

}
