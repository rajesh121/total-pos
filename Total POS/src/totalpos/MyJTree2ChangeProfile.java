package totalpos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author shidalgo
 */
public class MyJTree2ChangeProfile extends JTree implements ActionListener{
    private JPopupMenu popup;
    private JMenuItem menu;
    private String profileId;
    private ShowProfile parent;

    public MyJTree2ChangeProfile(DefaultMutableTreeNode dmtn, String profile, ShowProfile parent) {
        super(dmtn);

        popup = new JPopupMenu();
        menu = new JMenuItem("Habilitar");
        menu.addActionListener(this);
        menu.setActionCommand("enable");
        popup.add(menu);

        menu = new JMenuItem("Deshabilitar");
        menu.addActionListener(this);
        menu.setActionCommand("disable");
        popup.add(menu);

        popup.setOpaque(true);
        popup.setLightWeightPopupEnabled(true);

        profileId = profile;

        this.parent = parent;

        addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseReleased( MouseEvent e ) {
                    if ( e.isPopupTrigger()) {
                        popup.show( (JComponent)e.getSource(), e.getX(), e.getY() );
                    }
                }
            }
        );

    }

    public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode dmtn;
        TreePath path = this.getSelectionPath();
        dmtn = (DefaultMutableTreeNode) path.getLastPathComponent();
        String nodeStr = dmtn.getUserObject().toString();
        String ptn = "[^(]+\\(([^)]+)\\).+";
        Pattern p = Pattern.compile(ptn);
        Matcher m = p.matcher(nodeStr);
        if ( m.find() ){

            if ( e.getActionCommand().equals("enable") ){
                try {
                    ConnectionDrivers.enableMenuProfile(profileId, m.group(1));
                    MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Menu habilitado satisfactoriamente");
                    msg.show(this);
                } catch (SQLException ex) {
                    MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Error", ex);
                    msg.show(this);
                }
            }else if ( e.getActionCommand().equals("disable") ){
                try {
                    ConnectionDrivers.disableMenuProfile(profileId, m.group(1));
                    MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Menu deshabilitado satisfactoriamente");
                    msg.show(this);
                } catch (SQLException ex) {
                    MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Error", ex);
                    msg.show(this);
                }
            }
        }else{
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Error inesperado! Los cambios no fueron guardados.");
            msg.show(this);
        }
    }

    

}
