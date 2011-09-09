/*
 * Login.java
 *
 * Created on 08-jul-2011, 12:36:33
 */

package totalpos;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
/**
 *
 * @author Saul Hidalgo
 */
public class Login extends JFrame {

    /** Creates new form Login */
    public Login() {
        initComponents();

        wallpaper = new Bottom((new ImageIcon(getClass().getResource("/totalpos/resources/Fondo-Inicio.jpg"))).getImage());
        loginText = new JTextField();
        userLabel = new JLabel();
        passwordText = new JPasswordField();
        passwordLabel = new JLabel();

        GroupLayout myBottomLayout = new GroupLayout(wallpaper);
        wallpaper.setLayout(myBottomLayout);
        myBottomLayout.setHorizontalGroup(
            myBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(myBottomLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(myBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(passwordLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userLabel, GroupLayout.Alignment.TRAILING))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(myBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(loginText)
                    .addComponent(passwordText, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(413, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(Constants.appName);

        userLabel.setFont(new Font("Courier New", 0, 12));
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userLabel.setIcon(new ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        userLabel.setText("Usuario");
        userLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        userLabel.setIconTextGap(0);

        passwordText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                passwordTextActionPerformed(evt);
            }
        });

        passwordLabel.setFont(new Font("Courier New", 0, 12));
        passwordLabel.setIcon(new ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        passwordLabel.setText("Contraseña");
        passwordLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        myBottomLayout.setVerticalGroup(
            myBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, myBottomLayout.createSequentialGroup()
                .addContainerGap(396, Short.MAX_VALUE)
                .addGroup(myBottomLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(userLabel)
                    .addComponent(loginText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(myBottomLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(wallpaper, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(wallpaper, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();

        wallpaper.setVisible(true);

    }

    private void passwordTextActionPerformed(ActionEvent evt) {
        try {
            passwordText.setEnabled(false);
            if ( !ConnectionDrivers.existsUser(loginText.getText().trim()) ){
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Usuario no existe");
                msg.show(this);
                passwordText.setEnabled(true);
                return;
            }

            if ( ConnectionDrivers.isLocked(loginText.getText().trim()) ){
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Usuario bloqueado");
                msg.show(this);
                passwordText.setEnabled(true);
                return;
            }
            ConnectionDrivers.login(loginText.getText(), passwordText.getPassword());

            User u = Shared.giveUser(ConnectionDrivers.listUsers(), loginText.getText());
            if ( u.getDebeCambiarPassword() ){
                ChangePassword cp = new ChangePassword(this, true, u);
                Shared.centerFrame(cp);
                cp.setVisible(true);
                if ( !cp.isOk ){
                    MessageBox msg = new MessageBox(MessageBox.SGN_IMPORTANT, "Debes cambiar el password. Intenta de nuevo.");
                    msg.show(this);
                    passwordText.setEnabled(true);
                    return;
                }
            }
            Shared.userInsertedPasswordOk(loginText.getText());
            UpdateClock uc = new UpdateClock();
            Shared.setScreenSaver(uc);

            if ( Constants.isPos ){

                List<Assign> as = ConnectionDrivers.listAssignsTurnPosRightNow();
                boolean toContinue = false;

                Assign a = null;
                for (Assign assign : as) {
                    if ( assign.getPos().equals(Constants.myId) && assign.isOpen() ){
                        toContinue = true;
                        a = assign;
                        break; // for performance ...  =D!
                    }
                }

                // Don't check assignments when you're offline
                if ( !toContinue && !Shared.isOffline ){
                    MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "No hay asignación para esta caja el día de hoy.");
                    msg.show(this);
                    passwordText.setEnabled(true);
                    return;
                }else if ( Shared.isOffline ){
                    //TODO What day should I choose
                    a = new Assign("offline", Constants.myId, java.sql.Date.valueOf(Constants.sdfDay2DB.format(Calendar.getInstance().getTime())), true);
                }

                uc.start(); //Start the screensaver xDD
                Shared.setUser(u);
                MainRetailWindows mrw = new MainRetailWindows(u, a);
                if ( mrw.isOk ){
                    Shared.setMyMainWindows(mrw);
                    Shared.centerFrame(mrw);
                    mrw.setVisible(true);
                }
            }else{
                uc.start(); //Same here
                Shared.setUser(u);
                MainWindows mw = new MainWindows(u);
                Shared.setMyMainWindows(mw);
                Shared.centerFrame(mw);
                mw.setVisible(true);
            }
            this.setVisible(false);
            dispose();
        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
            msg.show(this);
            passwordText.setEnabled(true);
        } catch (Exception ex) {
            passwordText.setEnabled(true);
            String kindErr = "";
            if ( Constants.wrongPasswordMsg.equals(ex.getMessage()) ) {
                kindErr = Constants.wrongPasswordMsg;
            }

            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, kindErr);
            msg.show(this);

            if ( ex.getMessage().equals(Constants.wrongPasswordMsg) ){
                try {
                    Shared.userTrying(loginText.getText());
                } catch (Exception ex1) {
                    msg = new MessageBox(MessageBox.SGN_DANGER,
                                (ex1.getMessage().equals(Constants.userLocked)? Constants.userLocked :"Error."),
                                ex1);
                    msg.show(null);
                    this.dispose();
                    Shared.reload();
                }
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Constants.appName);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 710, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 522, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private Bottom wallpaper;

    private JLabel userLabel;
    private JLabel passwordLabel;
    private JTextField loginText;
    private JPasswordField passwordText;
}
