package totalpos;

import javax.swing.JFrame;

/**
 *
 * @author Saul Hidalgo.
 */
public class Main {

    protected static StartSplash splash;

    public static void main(String[] args) {
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
