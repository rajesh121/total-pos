/*
 * MainWindows.java
 *
 * Created on 08-jul-2011, 11:55:51
 */

package totalpos;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author Saúl Hidalgo
 */
public class MainWindows extends javax.swing.JFrame {

    private TreeMap< String , Set<Character> > mnemonics = new TreeMap<String, Set<Character> >();
    private Image wallpaper = new ImageIcon(getClass().getResource("/totalpos/resources/Fondo-Tramado.jpg")).getImage();
    MdiPanel mdiPanel = new MdiPanel(wallpaper);

    /** Creates new form MainWindows
     * @param user 
     */
    public MainWindows(User user) {
        initComponents();

        mdiPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        mdiPanel.setFocusable(true);
        mdiPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mdiPanelMouseMoved(evt);
            }

            private void mdiPanelMouseMoved(MouseEvent evt) {
                Shared.getScreenSaver().actioned();
                //TODO FIX IT!
                //mdiPanel.requestFocus();
            }
        });
        mdiPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
                    logout();
                }
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mdiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(mdiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        mdiPanel.setVisible(true);
        getContentPane().add(mdiPanel);

        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        createMenu(menuBar,"root");


        if ( !Shared.getConfig().containsKey("storeName") ){
            CreateShop cs = new CreateShop();
            if ( cs.isOk ){
                mdiPanel.add(cs);
                cs.setVisible(true);
            }else{
                MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "No se pudo crear la tienda. No se puede continuar.");
                msb.show(null);
            }
        }
    }

    private void createMenu(JComponent menu, String root){
        try {
            List<Edge> edges = ConnectionDrivers.listEdgesAllowed(root, Shared.getUser().getPerfil());

            for (int i = 0; i < edges.size(); i++) {

                Edge ed = edges.get(i);
                JComponent child = null;
                if ( ed.getFuncion().equals("menu") ){
                    child = new JMenu(new AppUserAction(ed,this));
                }else{
                    child = new JMenuItem(new AppUserAction(ed,this));
                }
                child.setFont(new Font("Courier New", 0, 12));
                child.setFocusable(false);
                menu.add(child);
                createMenu(child, ed.getId());
            }

        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
            msg.show(this);
        } catch (Exception ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, ex.getMessage(), ex);
            msg.show(this);
            this.dispose();
            Shared.reload();
        }
    }

    private int giveMeMnemonic(String nameMenu, String name){
        for ( char c : name.toCharArray() ) {
            c = Character.toLowerCase(c);
            if (!( mnemonics.containsKey(nameMenu) && mnemonics.get(nameMenu).contains(c) )) {
                if ( !mnemonics.containsKey(nameMenu) ){
                    mnemonics.put(nameMenu, new TreeSet<Character>());
                }
                mnemonics.get(nameMenu).add(c);
                return Character.toLowerCase(c)-'a';
            }
        }
        return 0;
    }

    private class AppUserAction extends AbstractAction {

        private Edge ed;
        private MainWindows mainWindows;

        private AppUserAction(Edge ed, MainWindows aThis) {
            this.ed = ed;
            this.mainWindows = aThis;

            putValue(Action.NAME, ed.getNombre());
            putValue(Action.MNEMONIC_KEY,
                    new Integer(java.awt.event.KeyEvent.VK_A+ giveMeMnemonic(ed.getPredecesor(), ed.getNombre()) ));
        }

        public void actionPerformed(ActionEvent evt) {
            if ( ed.getFuncion().equals("addProfile") ){
                CreateProfile cp = new CreateProfile();
                mdiPanel.add(cp);
                cp.setVisible(true);
            }else if ( ed.getFuncion().equals("searchProfile") ) {
                SearchProfile sp = new SearchProfile();
                if ( sp.isOk ){
                    mdiPanel.add(sp);
                    sp.setVisible(true);
                }
            }else if ( ed.getFuncion().equals("manageUser") ){
                ManageUser mu = new ManageUser();
                if ( mu.isOk ){
                    mdiPanel.add(mu);
                    mu.setVisible(true);
                }
            } else if ( ed.getFuncion().equals("changeIdleTime") ){
                ChangeIdleTime cit = new ChangeIdleTime();
                if ( cit.isOk ){
                    mdiPanel.add(cit);
                    cit.setVisible(true);
                }
            } else if ( ed.getFuncion().equals("manageItem") ) {
                ManageItem mi = new ManageItem();
                if( mi.isOk ){
                    mdiPanel.add(mi);
                    mi.setVisible(true);
                }
            } else if ( ed.getFuncion().equals("listTurns") ) {
                ListTurnsForm ct = new ListTurnsForm();
                if( ct.isOk ){
                    mdiPanel.add(ct);
                    ct.setVisible(true);
                }
            } else if ( ed.getFuncion().equals("createStore") ) {
                CreateShop cs = new CreateShop();
                if( cs.isOk ){
                    mdiPanel.add(cs);
                    cs.setVisible(true);
                }
            } else if ( ed.getFuncion().equals("listPos") ) {
                ListPOS cp = new ListPOS();
                if( cp.isOk ){
                    mdiPanel.add(cp);
                    cp.setVisible(true);
                }
            } else if ( ed.getFuncion().equals("listTurnsAssigned") ) {
                ListTurnsAssigned lta = new ListTurnsAssigned();
                if( lta.isOk ){
                    mdiPanel.add(lta);
                    lta.setVisible(true);
                }
            } else if ( ed.getFuncion().equals("managePOSBank") ) {
                ManageBank lta = new ManageBank();
                if( lta.isOk ){
                    mdiPanel.add(lta);
                    lta.setVisible(true);
                }
            } else if ( ed.getFuncion().equals("listReports") ) {
                ReportsForm rf = new ReportsForm();
                mdiPanel.add(rf);
                rf.setVisible(true);
            } else if ( ed.getFuncion().equals("expenses") ) {
                AddExpenses ae = new AddExpenses();
                mdiPanel.add(ae);
                ae.setVisible(true);
            } else if ( ed.getFuncion().equals("exit") ) {
                logout();
            } else if (ed.getFuncion().isEmpty()) {
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Función no implementada aún");
                msg.show(mainWindows);
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

        jPanel1 = new javax.swing.JPanel();
        esc2exit = new javax.swing.JLabel();
        whatTimeIsIt = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Shared.getUser().getLogin() + " @ " + Constants.appName);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setFocusable(false);
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanel1MouseMoved(evt);
            }
        });
        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel1KeyPressed(evt);
            }
        });

        esc2exit.setFont(new java.awt.Font("Courier New", 0, 11));
        esc2exit.setText("ESC = Salir.");
        esc2exit.setFocusable(false);
        esc2exit.setName("esc2exit"); // NOI18N
        esc2exit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                esc2exitKeyPressed(evt);
            }
        });

        whatTimeIsIt.setFocusable(false);
        whatTimeIsIt.setName("whatTimeIsIt"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(esc2exit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 497, Short.MAX_VALUE)
                .addComponent(whatTimeIsIt, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(esc2exit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(whatTimeIsIt, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N
        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(608, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            logout();
        }
    }//GEN-LAST:event_formKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Shared.setUser(null);
    }//GEN-LAST:event_formWindowClosing

    private void jPanel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_jPanel1MouseMoved

    private void jPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            logout();
        }
    }//GEN-LAST:event_jPanel1KeyPressed

    private void esc2exitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_esc2exitKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            logout();
        }
    }//GEN-LAST:event_esc2exitKeyPressed

    private void logout(){

        Object[] options = {"Si","No"};
        int n = JOptionPane.showOptionDialog(this,"¿Desea salir del sistema?",
                Constants.appName,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if ( n == 0 ){
            Login l = new Login();
            Shared.centerFrame(l);
            Shared.maximize(l);
            l.setVisible(true);
            Shared.setUser(null);

            setVisible(false);
            dispose();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel esc2exit;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenuBar menuBar;
    public javax.swing.JLabel whatTimeIsIt;
    // End of variables declaration//GEN-END:variables

}
