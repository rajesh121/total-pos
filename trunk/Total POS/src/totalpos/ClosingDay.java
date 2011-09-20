/*
 * ClosingDay.java
 *
 * Created on 26-ago-2011, 15:37:34
 */

package totalpos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ArrayOfZFISCOBRANZA;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ArrayOfZFISDATAFISCAL;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ArrayOfZSDSCABDEV;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ArrayOfZSDSCABFACT;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ArrayOfZSDSCLIENT;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ArrayOfZSDSPOSDEV;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ArrayOfZSDSPOSFACT;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ArrayOfZSDSVENDFACT;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ObjectFactory;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.Resultado;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ZFISCOBRANZA;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ZFISDATAFISCAL;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ZFISHISTENVIOS;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ZSDSCABDEV;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ZSDSCLIENT;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ZSDSVENDFACT;
import org.tempuri.IsrvSap;
import org.tempuri.SrvSap;

/**
 *
 * @author Saúl Hidalgo
 */
public class ClosingDay extends javax.swing.JInternalFrame {

    List<Expense> expenses;
    List<Deposit> deposits;
    Double totalInCard;
    Double totalInCash;
    Double totalExpenses;
    private ObjectFactory of = Constants.of;

    /** Creates new form ClosingDay */
    public ClosingDay() {
        try {
            initComponents();
            updateAll();
            MessageBox msg = new MessageBox(MessageBox.SGN_IMPORTANT, "El cierre de Caja aún está en desarrollo!!");
            msg.show(this);
        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
            msg.show(null);
            this.dispose();
            Shared.reload();
        }
    }

    private void updateDeposits() throws SQLException{
        deposits = ConnectionDrivers.listDepositsToday();
        DefaultTableModel model = (DefaultTableModel) depositTable.getModel();
        model.setRowCount(0);
        for (Deposit e : deposits) {
            String[] s = {e.getBank(),e.getFormId(),Constants.df.format(e.getQuant())};
            model.addRow(s);
        }
    }

    private void updateExpense() throws SQLException{
        expenses = ConnectionDrivers.listExpensesToday();
        DefaultTableModel model = (DefaultTableModel) ExpenseTable.getModel();
        model.setRowCount(0);
        for (Expense e : expenses) {
            String[] s = {e.getConcept(),Constants.df.format(e.getQuant()),e.getDescription()};
            model.addRow(s);
        }
    }

    public void updatePayFormWaysxPoses() throws SQLException{
        ConnectionDrivers.listFormWayXPosToday((DefaultTableModel) formWayxPoses.getModel());
    }

    public void updateFiscalZ() throws SQLException{
        ConnectionDrivers.listFiscalZ((DefaultTableModel) fiscalZ.getModel());
    }

    public void updatePayWayxPosesDetails() throws SQLException{
        ConnectionDrivers.listFormWayXPosesDetailToday((DefaultTableModel) payWayxPosTable.getModel());
    }

    public void updateBankTable() throws SQLException{
        ConnectionDrivers.listBankTable((DefaultTableModel) bankTable.getModel());
    }

    private void updateAll() throws SQLException{
        updateDeposits();
        updateExpense();
        updatePayFormWaysxPoses();
        updateFiscalZ();
        updatePayWayxPosesDetails();
        updateBankTable();
        totalCardsField.setText(Constants.df.format(totalInCard = ConnectionDrivers.getTotalCardsToday()));
        totalCashField.setText(Constants.df.format(totalInCash = ConnectionDrivers.getTotalCashToday()));
        totalTotalField.setText(Constants.df.format(totalInCard + totalInCash));
        expensesTodayField.setText(Constants.df.format(totalExpenses = ConnectionDrivers.getExpensesToday()));
        totalDeclaredField.setText(Constants.df.format(totalInCard + totalInCash - totalExpenses));
        expensesMinusDeclaredField.setText(Constants.df.format(totalInCard + totalInCash));
        netValue.setText(Constants.df.format( totalInCard + totalInCash - ( totalInCard + totalInCash - totalExpenses ) - (totalExpenses)));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        fiscalZ = new javax.swing.JTable();
        updateFiscalNumberslButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        depositTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        bankTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ExpenseTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        formWayxPoses = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        payWayxPosTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        totalCardsField = new javax.swing.JTextField();
        totalCashField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        totalTotalField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        expensesMinusDeclaredField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        totalDeclaredField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        expensesTodayField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        netValue = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        printAndSendButton = new javax.swing.JButton();
        noteField = new javax.swing.JTextField();
        noteLabel = new javax.swing.JLabel();

        jFileChooser1.setName("jFileChooser1"); // NOI18N

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Día Operativo");
        setVisible(true);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Zetas Fiscales"));
        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        fiscalZ.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Caja", "Serial", "Dinero Caja", "Zeta Fiscal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        fiscalZ.setName("fiscalZ"); // NOI18N
        fiscalZ.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        fiscalZ.getTableHeader().setReorderingAllowed(false);
        fiscalZ.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fiscalZFocusGained(evt);
            }
        });
        jScrollPane6.setViewportView(fiscalZ);
        fiscalZ.getColumnModel().getColumn(0).setPreferredWidth(40);

        updateFiscalNumberslButton.setText("Actualizar Zeta Fiscal de todos");
        updateFiscalNumberslButton.setName("updateFiscalNumberslButton"); // NOI18N
        updateFiscalNumberslButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateFiscalNumberslButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateFiscalNumberslButton, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updateFiscalNumberslButton)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Depósitos"));
        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        depositTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Banco", "Numero", "Monto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        depositTable.setName("depositTable"); // NOI18N
        depositTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        depositTable.getTableHeader().setReorderingAllowed(false);
        depositTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                depositTableFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(depositTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Banco y Lotes"));
        jPanel4.setName("jPanel4"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        bankTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Código", "Banco", "Lote", "Medio", "Declarado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bankTable.setName("bankTable"); // NOI18N
        bankTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        bankTable.getTableHeader().setReorderingAllowed(false);
        bankTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bankTableFocusGained(evt);
            }
        });
        jScrollPane5.setViewportView(bankTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Gastos"));
        jPanel5.setName("jPanel5"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        ExpenseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Concepto", "Monto", "Descripción"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ExpenseTable.setName("ExpenseTable"); // NOI18N
        ExpenseTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ExpenseTable.getTableHeader().setReorderingAllowed(false);
        ExpenseTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ExpenseTableFocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(ExpenseTable);
        ExpenseTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        ExpenseTable.getColumnModel().getColumn(1).setPreferredWidth(10);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Medios de Pago por el Cajero"));
        jPanel6.setName("jPanel6"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        formWayxPoses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cajero", "Efectivo", "Tarjeta", "NC"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        formWayxPoses.setName("formWayxPoses"); // NOI18N
        formWayxPoses.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        formWayxPoses.getTableHeader().setReorderingAllowed(false);
        formWayxPoses.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formWayxPosesFocusGained(evt);
            }
        });
        jScrollPane4.setViewportView(formWayxPoses);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Medios de Pago x Cajero"));
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        payWayxPosTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Cajero", "Medio", "Registrado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        payWayxPosTable.setName("payWayxPosTable"); // NOI18N
        payWayxPosTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        payWayxPosTable.getTableHeader().setReorderingAllowed(false);
        payWayxPosTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                payWayxPosTableFocusGained(evt);
            }
        });
        jScrollPane3.setViewportView(payWayxPosTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Resumen"));
        jPanel7.setName("jPanel7"); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel1.setText("Declarado Tarj");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel2.setText("Declarado Efec");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setName("jLabel2"); // NOI18N

        totalCardsField.setText("Falta");
        totalCardsField.setFocusable(false);
        totalCardsField.setName("totalCardsField"); // NOI18N

        totalCashField.setText("Falta");
        totalCashField.setFocusable(false);
        totalCashField.setName("totalCashField"); // NOI18N

        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel3.setName("jLabel3"); // NOI18N

        totalTotalField.setText("Falta");
        totalTotalField.setFocusable(false);
        totalTotalField.setName("totalTotalField"); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel4.setText("Total Declarado");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel4.setName("jLabel4"); // NOI18N

        expensesMinusDeclaredField.setText("Falta");
        expensesMinusDeclaredField.setFocusable(false);
        expensesMinusDeclaredField.setName("expensesMinusDeclaredField"); // NOI18N

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel5.setText("Facturado");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel5.setName("jLabel5"); // NOI18N

        totalDeclaredField.setText("Falta");
        totalDeclaredField.setFocusable(false);
        totalDeclaredField.setName("totalDeclaredField"); // NOI18N

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel6.setText("Declarado");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel6.setName("jLabel6"); // NOI18N

        expensesTodayField.setText("Falta");
        expensesTodayField.setFocusable(false);
        expensesTodayField.setName("expensesTodayField"); // NOI18N

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel7.setText("Gastos");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel8.setText("Neto");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel8.setName("jLabel8"); // NOI18N

        netValue.setText("Falta");
        netValue.setFocusable(false);
        netValue.setName("netValue"); // NOI18N

        jTextField8.setText("Falta");
        jTextField8.setFocusable(false);
        jTextField8.setName("jTextField8"); // NOI18N

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel9.setText("Monto NC");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel12.setText("Sobra Faltante");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel12.setName("jLabel12"); // NOI18N

        jTextField9.setText("Falta");
        jTextField9.setFocusable(false);
        jTextField9.setName("jTextField9"); // NOI18N

        cancelButton.setText("Cancelar");
        cancelButton.setFocusable(false);
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        printAndSendButton.setText("Imprimir y Cerrar");
        printAndSendButton.setFocusable(false);
        printAndSendButton.setName("printAndSendButton"); // NOI18N
        printAndSendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printAndSendButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(totalCardsField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(totalCashField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(expensesMinusDeclaredField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalDeclaredField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(expensesTodayField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(netValue, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(printAndSendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(totalCardsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalCashField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(expensesMinusDeclaredField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalDeclaredField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(expensesTodayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(netValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(printAndSendButton))
                .addContainerGap())
        );

        noteField.setName("noteField"); // NOI18N

        noteLabel.setText("Observaciones:");
        noteLabel.setName("noteLabel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(noteLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(noteField, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(noteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noteLabel))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void fiscalZFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fiscalZFocusGained
        try {
            updateAll();
        } catch (SQLException ex) {
            Logger.getLogger(ClosingDay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fiscalZFocusGained

    private void depositTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_depositTableFocusGained
        try {
            updateAll();
        } catch (SQLException ex) {
            Logger.getLogger(ClosingDay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_depositTableFocusGained

    private void ExpenseTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ExpenseTableFocusGained
        try {
            updateAll();
        } catch (SQLException ex) {
            Logger.getLogger(ClosingDay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ExpenseTableFocusGained

    private void payWayxPosTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_payWayxPosTableFocusGained
        try {
            updateAll();
        } catch (SQLException ex) {
            Logger.getLogger(ClosingDay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_payWayxPosTableFocusGained

    private void bankTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bankTableFocusGained
        try {
            updateAll();
        } catch (SQLException ex) {
            Logger.getLogger(ClosingDay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bankTableFocusGained

    private void formWayxPosesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formWayxPosesFocusGained
        try {
            updateAll();
        } catch (SQLException ex) {
            Logger.getLogger(ClosingDay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWayxPosesFocusGained

    private void updateFiscalNumberslButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateFiscalNumberslButtonActionPerformed
        try {
            ConnectionDrivers.markToUpdateFiscalNumbersToday();
        } catch (SQLException ex) {
            Logger.getLogger(ClosingDay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_updateFiscalNumberslButtonActionPerformed

    private void printAndSendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printAndSendButtonActionPerformed
        doIt();
    }//GEN-LAST:event_printAndSendButtonActionPerformed

    public void doIt(){
        new CreateClosingDayReport();

        try {
            SrvSap ss = new SrvSap();
            IsrvSap isrvs = ss.getBasicHttpBindingIsrvSap();
            ZFISHISTENVIOS zfhe = new ZFISHISTENVIOS();
            ArrayOfZFISCOBRANZA lzfc = new ArrayOfZFISCOBRANZA();
            ArrayOfZFISDATAFISCAL aozfdf = new ArrayOfZFISDATAFISCAL();
            zfhe.setMANDT(of.createZFISHISTENVIOSMANDT(Constants.mant));
            zfhe.setIDTIENDA(of.createZFISHISTENVIOSIDTIENDA(Constants.storePrefix + Shared.getConfig("storeName")));
            zfhe.setFECHAPROCESADO(of.createZFISHISTENVIOSFECHAPROCESADO(Constants.sdfDay2SAP.format(new GregorianCalendar().getTime())));
            zfhe.setTOTALVENTASDIA(new BigDecimal(totalInCard + totalInCash));
            zfhe.setOBSERVACIONES(of.createZFISHISTENVIOSOBSERVACIONES(noteField.getText()));
            zfhe.setMODIFICAR(of.createZFISHISTENVIOSMODIFICAR("N"));
            zfhe.setFONDOCAJA(BigDecimal.ZERO);
            fillBanks(lzfc.getZFISCOBRANZA());
            fillExpenses(lzfc.getZFISCOBRANZA());
            fillDeposits(lzfc.getZFISCOBRANZA());
            List<ZFISDATAFISCAL> zFISDATAFISCAL = aozfdf.getZFISDATAFISCAL();
            for (ZFISDATAFISCAL zfdf : ConnectionDrivers.getOperativeDays()) {
                zFISDATAFISCAL.add(zfdf);
            }
            Resultado sss = isrvs.sapInsertCobranza(lzfc, aozfdf, zfhe);
            System.out.println(sss.getCodigoError());

            MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Enviado correctamente!!");
            msg.show(this);

            ArrayOfZSDSCABDEV aozsdscd = new ArrayOfZSDSCABDEV();
            ArrayOfZSDSVENDFACT aozsdsvf = new ArrayOfZSDSVENDFACT();
            ArrayOfZSDSCABFACT aozsdscf = new ArrayOfZSDSCABFACT();
            ArrayOfZSDSPOSDEV aozsdspd = new ArrayOfZSDSPOSDEV();
            ArrayOfZSDSPOSFACT aozsdspf = new ArrayOfZSDSPOSFACT();
            ArrayOfZSDSCLIENT aozsdsc = new ArrayOfZSDSCLIENT();


            // CN
            List<Receipt> receipts = ConnectionDrivers.listOkCNToday();
            ReceiptSap rs = new ReceiptSap();
            int previousId = -1;
            for (Receipt receipt : receipts) {
                if ( receipt.getFiscalNumber().isEmpty() ){
                    System.out.println("Error con la factura " + receipt.getInternId());
                    continue;
                }
                if ( (previousId == -1 || previousId +1 == Integer.parseInt(receipt.getFiscalNumber() ) && receipt.getClientId().equals("Contado")) ){
                    rs.add(receipt);
                }else{
                    aozsdscd.getZSDSCABDEV().add(rs.getHeader());
                    aozsdspd.getZSDSPOSDEV().addAll(rs.getDetails());
                    rs = new ReceiptSap();
                    rs.add(receipt);
                }
                previousId = Integer.parseInt(receipt.getFiscalNumber());
            }
            if ( rs.getSize() > 0 ){
                aozsdscd.getZSDSCABDEV().add(rs.getHeader());
                aozsdspd.getZSDSPOSDEV().addAll(rs.getDetails());
            }

            receipts = ConnectionDrivers.listOkReceiptsToday();
            rs = new ReceiptSap();
            previousId = -1;
            for (Receipt receipt : receipts) {
                if ( receipt.getFiscalNumber().isEmpty() ){
                    System.out.println("Error con la factura " + receipt.getInternId());
                    continue;
                }
                if ( (previousId == -1 || previousId +1 == Integer.parseInt(receipt.getFiscalNumber() ) && receipt.getClientId().equals("Contado")) ){
                    rs.add(receipt);
                }else{
                    aozsdscf.getZSDSCABFACT().add(rs.getHeaderF());
                    aozsdspf.getZSDSPOSFACT().addAll(rs.getDetailsF());
                    rs = new ReceiptSap();
                    rs.add(receipt);
                }
                previousId = Integer.parseInt(receipt.getFiscalNumber());
            }
            if ( rs.getSize() > 0 ){
                aozsdscf.getZSDSCABFACT().add(rs.getHeaderF());
                aozsdspf.getZSDSPOSFACT().addAll(rs.getDetailsF());
            }
            
            ZSDSVENDFACT zsdsvfi = new ZSDSVENDFACT();
            zsdsvfi.setFKDAT(of.createZSDSVENDFACTFKDAT(Constants.sdfDay2SAP.format(new GregorianCalendar().getTime())));
            zsdsvfi.setMANDT(of.createZSDSVENDFACTMANDT(Constants.mant));
            zsdsvfi.setPARTNNUMB(of.createZSDSVENDFACTPARTNNUMB("999999"));
            zsdsvfi.setPARTNROLE(of.createZSDSVENDFACTPARTNROLE("999999"));
            zsdsvfi.setPOSNR(of.createZSDSVENDFACTPOSNR("000001"));
            zsdsvfi.setVBELN(of.createZSDSVENDFACTVBELN("999999"));
            zsdsvfi.setWERKS(of.createZSDSVENDFACTWERKS(Constants.storePrefix+Shared.getConfig("storeName")));

            aozsdsvf.getZSDSVENDFACT().add(zsdsvfi);

            ZSDSCLIENT cli = new ZSDSCLIENT();
            cli.setMANDT(of.createZSDSVENDFACTMANDT(Constants.mant));
            cli.setFKDAT(of.createZSDSVENDFACTFKDAT(Constants.sdfDay2SAP.format(new GregorianCalendar().getTime())));
            cli.setNAME1(of.createZSDSCLIENTNAME1("Saul Hidalgo"));
            cli.setKUNNR(of.createZSDSCLIENTKUNNR("J123456780"));
            cli.setWAERS(of.createZSDSCLIENTWAERS("VEF"));
            cli.setADRNR(of.createZSDSCLIENTADRNR(""));
            aozsdsc.getZSDSCLIENT().add(cli);
            sss = isrvs.sapInsertVentas(aozsdscd, aozsdscf, aozsdspd, aozsdspf, aozsdsc, aozsdsvf);
            System.out.println(sss.getCodigoError());
            System.out.println(sss.getMensaje().getValue());

        } catch (SQLException ex) {
            Logger.getLogger(ClosingDay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable ExpenseTable;
    private javax.swing.JTable bankTable;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTable depositTable;
    private javax.swing.JTextField expensesMinusDeclaredField;
    private javax.swing.JTextField expensesTodayField;
    private javax.swing.JTable fiscalZ;
    private javax.swing.JTable formWayxPoses;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextField netValue;
    private javax.swing.JTextField noteField;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JTable payWayxPosTable;
    private javax.swing.JButton printAndSendButton;
    private javax.swing.JTextField totalCardsField;
    private javax.swing.JTextField totalCashField;
    private javax.swing.JTextField totalDeclaredField;
    private javax.swing.JTextField totalTotalField;
    private javax.swing.JButton updateFiscalNumberslButton;
    // End of variables declaration//GEN-END:variables

    private void fillBanks(List<ZFISCOBRANZA> zFISCOBRANZA) {
        for ( int i = 0 ; i < bankTable.getRowCount() ; i++ ){
            ZFISCOBRANZA zfc = new ZFISCOBRANZA();
            zfc.setID(1);
            zfc.setMANDT(of.createZFISCOBRANZAMANDT(Constants.mant));
            zfc.setFECHA(of.createZFISCOBRANZAFECHA(Constants.sdfDay2SAP.format(new GregorianCalendar().getTime())));
            zfc.setWERKS(of.createZFISCOBRANZAWERKS(Constants.storePrefix + Shared.getConfig("storeName")));
            zfc.setWAERS(of.createZFISCOBRANZAWAERS(Constants.waerks));
            zfc.setSIMBO(of.createZFISCOBRANZASIMBO((String)bankTable.getValueAt(i, 0)));
            zfc.setMPAGO( of.createZFISCOBRANZAMPAGO( bankTable.getValueAt(i, 3).equals("Credito")?"B":"D" ) );
            zfc.setBPAGO(of.createZFISCOBRANZABPAGO((String)bankTable.getValueAt(i, 0)));
            zfc.setLOTE(of.createZFISCOBRANZALOTE((String)bankTable.getValueAt(i, 2)));
            zfc.setMONTO(new BigDecimal((String)bankTable.getValueAt(i, 4)));
            zfc.setITEMTEXT(of.createZFISCOBRANZAITEMTEXT("No hay Observaciones"));
            zFISCOBRANZA.add(zfc);
        }
    }

    private void fillExpenses(List<ZFISCOBRANZA> zFISCOBRANZA) {
        for ( int i = 0 ; i < ExpenseTable.getRowCount() ; i++ ){
            ZFISCOBRANZA zfc = new ZFISCOBRANZA();
            zfc.setID(1);
            zfc.setMANDT(of.createZFISCOBRANZAMANDT(Constants.mant));
            zfc.setFECHA(of.createZFISCOBRANZAFECHA(Constants.sdfDay2SAP.format(new GregorianCalendar().getTime())));
            zfc.setWERKS(of.createZFISCOBRANZAWERKS(Constants.storePrefix + Shared.getConfig("storeName")));
            zfc.setWAERS(of.createZFISCOBRANZAWAERS(Constants.waerks));
            zfc.setSIMBO(of.createZFISCOBRANZASIMBO(Constants.genericBank));
            String tmp = ExpenseTable.getValueAt(i, 0).toString().split("-")[0];
            zfc.setMPAGO( of.createZFISCOBRANZAMPAGO( tmp.substring(0, tmp.length() - 1) ) );
            zfc.setBPAGO(of.createZFISCOBRANZABPAGO(Constants.genericBank));
            zfc.setLOTE(of.createZFISCOBRANZALOTE(""));
            zfc.setMONTO(new BigDecimal(((String)ExpenseTable.getValueAt(i, 1)).replace(',', '.')));
            zfc.setITEMTEXT(of.createZFISCOBRANZAITEMTEXT((String)ExpenseTable.getValueAt(i, 2)));
            zFISCOBRANZA.add(zfc);
        }
    }

    private void fillDeposits(List<ZFISCOBRANZA> zFISCOBRANZA) {
        for ( int i = 0 ; i < depositTable.getRowCount() ; i++ ){
            ZFISCOBRANZA zfc = new ZFISCOBRANZA();
            zfc.setID(1);
            zfc.setMANDT(of.createZFISCOBRANZAMANDT(Constants.mant));
            zfc.setFECHA(of.createZFISCOBRANZAFECHA(Constants.sdfDay2SAP.format(new GregorianCalendar().getTime())));
            zfc.setWERKS(of.createZFISCOBRANZAWERKS(Constants.storePrefix + Shared.getConfig("storeName")));
            zfc.setWAERS(of.createZFISCOBRANZAWAERS(Constants.waerks));
            zfc.setSIMBO(of.createZFISCOBRANZASIMBO((String)depositTable.getValueAt(i, 0)));
            zfc.setMPAGO( of.createZFISCOBRANZAMPAGO( "E" ) );
            zfc.setBPAGO(of.createZFISCOBRANZABPAGO((String)depositTable.getValueAt(i, 0)));
            zfc.setLOTE(of.createZFISCOBRANZALOTE(""));
            zfc.setMONTO(new BigDecimal(((String)depositTable.getValueAt(i, 2)).replace(',','.')));
            zfc.setITEMTEXT(of.createZFISCOBRANZAITEMTEXT("No hay Observaciones"));
            zFISCOBRANZA.add(zfc);
        }
    }
}
