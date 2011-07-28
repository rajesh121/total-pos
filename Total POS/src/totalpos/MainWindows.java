/*
 * MainWindows.java
 *
 * Created on 08-jul-2011, 11:55:51
 */

package totalpos;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 *
 * @author shidalgo
 */
public class MainWindows extends javax.swing.JFrame {

    private User user;
    private TreeMap< String , Set<Character> > mnemonics = new TreeMap<String, Set<Character> >();

    /** Creates new form MainWindows
     * @param user 
     */
    public MainWindows(User user) {
        initComponents();
        this.user = user;

        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        createMenu(menuBar,"root");
        //showLast();
    }

    private void createMenu(JComponent menu, String root){
        try {
            List<Edge> edges = ConnectionDrivers.listEdgesAllowed(root, user.getPerfil());

            for (int i = 0; i < edges.size(); i++) {

                Edge ed = edges.get(i);
                JComponent child = null;
                if ( ed.getFuncion().equals("menu") ){
                    child = new JMenu(new AppUserAction(ed,this));
                }else{
                    child = new JMenuItem(new AppUserAction(ed,this));
                }
                child.setFont(new Font("Courier New", 0, 12));
                menu.add(child);
                createMenu(child, ed.getId());
            }

            
            /*btn.setText("Salir");
            btn.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logout();
                }
            });
            jPeople.add(btn);*/

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
            } else if ( ed.getFuncion().equals("changeIdleTime") ){
                ChangeIdleTime cit = new ChangeIdleTime(mainWindows, true);
                if ( cit.isOk ){
                    Shared.centerFrame(cit);
                    cit.setVisible(true);
                }
            } else if ( ed.getFuncion().equals("manageItem") ) {
                ManageItem mi = new ManageItem(mainWindows, true);
                if( mi.isOk ){
                    Shared.centerFrame(mi);
                    mi.setVisible(true);
                }
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

        menuBar = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Constants.appName);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        menuBar.setName("menuBar"); // NOI18N
        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 752, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 623, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            logout();
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
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

}
