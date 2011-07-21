/*
 * MainWindows.java
 *
 * Created on 08-jul-2011, 11:55:51
 */

package totalpos;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 *
 * @author shidalgo
 */
public class MainWindows extends javax.swing.JFrame {

    private User user;
    protected List<JFlowPanel> navegatorStack;

    /** Creates new form MainWindows
     * @param user 
     */
    public MainWindows(User user) {
        initComponents();
        this.user = user;
        navegatorStack = new LinkedList<JFlowPanel>();

        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        navegatorStack.add(createMenu(new Edge("root", "root", null, "")));
        showLast();
    }

    private JFlowPanel createMenu(Edge root){
        try {
            List<Edge> edges = ConnectionDrivers.listEdgesAllowed(root.getId(), user.getPerfil());

            scrollPanel.getViewport().setView(null);
            JFlowPanel jPeople = new JFlowPanel();
            jPeople.applyComponentOrientation(getComponentOrientation());

            for (int i = 0; i < edges.size(); i++) {

                Edge ed = edges.get(i);

                JButton btn = new JButton(new AppUserAction(ed,this));
                btn.applyComponentOrientation(getComponentOrientation());
                btn.setFocusPainted(false);
                btn.setFocusable(false);
                btn.setRequestFocusEnabled(false);
                btn.setHorizontalAlignment(SwingConstants.CENTER);
                btn.setMaximumSize(new Dimension(150, 50));
                btn.setPreferredSize(new Dimension(150, 50));
                btn.setMinimumSize(new Dimension(150, 50));

                jPeople.add(btn);
            }
            JButton btn = new JButton();
            btn.applyComponentOrientation(getComponentOrientation());
            btn.setFocusPainted(false);
            btn.setFocusable(false);
            btn.setRequestFocusEnabled(false);
            btn.setHorizontalAlignment(SwingConstants.CENTER);
            btn.setMaximumSize(new Dimension(150, 50));
            btn.setPreferredSize(new Dimension(150, 50));
            btn.setMinimumSize(new Dimension(150, 50));

            if ( ! root.getId().equals("root")){
                btn.setText("Atras");
                btn.addActionListener(new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        navegatorStack.remove(navegatorStack.size()-1);
                        showLast();
                    }
                });
                jPeople.add(btn);
            }else{
                btn.setText("Salir");
                btn.addActionListener(new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        logout();
                    }
                });
                jPeople.add(btn);
            }

            return jPeople;

        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Error con la base de datos.", ex);
            msg.show(this);
            return null;
        } catch (Exception ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Error", ex);
            msg.show(this);
            Shared.reload();
            return null;
        }
    }

    private void showLast(){
        scrollPanel.getViewport().setView(navegatorStack.get(navegatorStack.size()-1));
    }

    private class AppUserAction extends AbstractAction {

        private Edge ed;
        private MainWindows mainWindows;

        private AppUserAction(Edge ed, MainWindows aThis) {
            this.ed = ed;
            this.mainWindows = aThis;

            putValue(Action.NAME, ed.getNombre());
            //putValue(Action.MNEMONIC_KEY,  new Integer(java.awt.event.KeyEvent.VK_A));
        }

        public void actionPerformed(ActionEvent evt) {
            if ( ed.getFuncion().equals("addProfile") ){
                CreateProfile cp = new CreateProfile(mainWindows, enabled);
                Shared.centerFrame(cp);
                cp.setVisible(true);
            }else if ( ed.getFuncion().equals("searchProfile") ) {
                SearchProfile sp = new SearchProfile(mainWindows, enabled);
                if ( sp.isOk ){
                    Shared.centerFrame(sp);
                    sp.setVisible(true);
                }
            }else if ( ed.getFuncion().equals("manageUser") ){
                ManageUser mu = new ManageUser(mainWindows, true);
                if ( mu.isOk ){
                    Shared.centerFrame(mu);
                    mu.setVisible(true);
                }
            } else if (ed.getFuncion().isEmpty()) {
                JFlowPanel t = createMenu(ed);
                if ( t != null ){
                    mainWindows.navegatorStack.add(t);
                    mainWindows.showLast();
                }
            }else{
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Funcion desconocida " + ed.getFuncion());
                msb.show(mainWindows);
            }
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

        scrollPanel = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Constants.appName);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        scrollPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel.setName("scrollPanel"); // NOI18N
        scrollPanel.setPreferredSize(new java.awt.Dimension(510, 118));
        scrollPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scrollPanelKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void scrollPanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scrollPanelKeyPressed

    }//GEN-LAST:event_scrollPanelKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            if ( navegatorStack.size() > 1 ){
                navegatorStack.remove(navegatorStack.size()-1);
                showLast();
            }else{
                logout();
            }
        }
    }//GEN-LAST:event_formKeyPressed

    private void logout(){
        if ( JOptionPane.showConfirmDialog(null, "¿Está seguro que desea cerrar sesión?") == 0 ){
            Login l = new Login();
            Shared.centerFrame(l);
            Shared.maximize(l);
            l.setVisible(true);
            ConnectionDrivers.username = null;

            setVisible(false);
            dispose();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPanel;
    // End of variables declaration//GEN-END:variables

}
