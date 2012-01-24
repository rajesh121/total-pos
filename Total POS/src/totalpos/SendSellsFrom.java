/*
 * SendSellsFrom.java
 *
 * Created on 17-ene-2012, 15:16:43
 */

package totalpos;

import java.awt.Window;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author shidalgo
 */
public class SendSellsFrom extends javax.swing.JInternalFrame implements Doer{

    protected Working workingFrame;

    /** Creates new form SendSellsFrom */
    public SendSellsFrom() {
        initComponents();
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
        jtf = new javax.swing.JTextField();
        acceptButton = new javax.swing.JButton();
        chooseDay = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Enviar ventas desde...");
        setResizable(false);

        jLabel1.setText("Enviar ventas desde...");
        jLabel1.setName("jLabel1"); // NOI18N

        jtf.setEditable(false);
        jtf.setName("jtf"); // NOI18N

        acceptButton.setText("Aceptar");
        acceptButton.setName("acceptButton"); // NOI18N
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        chooseDay.setText("Elegir día");
        chooseDay.setName("chooseDay"); // NOI18N
        chooseDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseDayActionPerformed(evt);
            }
        });

        closeButton.setText("Cerrar");
        closeButton.setName("closeButton"); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtf, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(chooseDay, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeButton, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chooseDay)
                    .addComponent(acceptButton)
                    .addComponent(closeButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chooseDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseDayActionPerformed
        ChooseDate cal = new ChooseDate(Constants.appName,(JComponent)jtf,false);
        ((MainWindows)Shared.getMyMainWindows()).mdiPanel.add(cal);
        cal.setVisible(true);
        System.out.println("text Field = " + jtf.getText());
    }//GEN-LAST:event_chooseDayActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        workingFrame = new Working((Window) Shared.getMyMainWindows());

        WaitSplash ws = new WaitSplash(this);

        Shared.centerFrame(workingFrame);
        workingFrame.setVisible(true);
        ws.execute();
    }//GEN-LAST:event_acceptButtonActionPerformed

    @Override
    public void close() {
        workingFrame.setVisible(false);
        
    }

    @Override
    public void doIt() {
        if ( !jtf.getText().isEmpty() ){
            String[] s = jtf.getText().split("-");
            Calendar calendar = new GregorianCalendar(Integer.parseInt(s[0]), Integer.parseInt(s[1])-1, Integer.parseInt(s[2]));
            Calendar calendarCurTime = Calendar.getInstance();
            System.out.println("calendarCurTime.getTimeInMillis() - calendar.getTimeInMillis() = " + (calendarCurTime.getTimeInMillis() - calendar.getTimeInMillis()));
            if ( calendarCurTime.getTimeInMillis() - calendar.getTimeInMillis() > 0 ){
                try {
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH)+1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    String myDay = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
                    System.out.println("MyDay = " + myDay);
                    while (ConnectionDrivers.previousClosed(myDay) && !(year == calendarCurTime.get(Calendar.YEAR) &&
                            month ==  calendarCurTime.get(Calendar.MONTH) && day ==  calendarCurTime.get(Calendar.DAY_OF_MONTH)
                            )) {
                        Shared.sendSells(myDay, null, "");
                        System.out.println("Enviar dia " + myDay + " | " + calendarCurTime.get(Calendar.YEAR) + " . " +
                               calendarCurTime.get(Calendar.MONTH) + " | " + calendarCurTime.get(Calendar.DAY_OF_MONTH));
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH)+1;
                        day = calendar.get(Calendar.DAY_OF_MONTH );
                        myDay = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
                    }
                    System.out.println("MyDay = " + myDay);
                } catch (IOException ex) {
                    Logger.getLogger(SendSellsFrom.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(SendSellsFrom.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton chooseDay;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jtf;
    // End of variables declaration//GEN-END:variables

}
