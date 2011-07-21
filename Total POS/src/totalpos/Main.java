package totalpos;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Saul Hidalgo.
 */
public class Main {

    protected static StartSplash splash;

    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        splash = new StartSplash();
        Shared.centerFrame(splash);
        splash.setVisible(true);
        
        splash.changeStatus("Conectado a base de datos...", 30);
        ConnectionDrivers.initialize();
        splash.changeStatus("Inicializando configuración...", 45);
        ConnectionDrivers.initializeConfig();

        splash.changeStatus("Creando ventana de login...", 60);
        Login login = new Login();
        Shared.centerFrame(login);
        login.setExtendedState(login.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        login.setVisible(true);

        splash.setVisible(false);

    }
}
