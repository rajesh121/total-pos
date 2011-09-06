package totalpos;

import java.sql.SQLException;
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
        splash.changeStatus("Inicializando configuración de base de datos...", 45);
        try {
            ConnectionDrivers.initializeConfig();
        } catch (SQLException ex) {
            ConnectionDrivers.reinitializeOffline();
            try {
                ConnectionDrivers.initializeConfig();
            } catch (SQLException ex1) {
                MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Problemas con la base de datos para el modo-offline",ex1);
                msb.show(splash);
                System.exit(0);
            }
            SellWithoutStock sws = new SellWithoutStock(splash, true, "Trabajar Modo Fuera de Línea","workOffline");
            Shared.centerFrame(sws);
            sws.setVisible(true);
            if ( !sws.authorized ){
                MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "No se ha autorizado a trabajar fuera de línea. No se puede continuar.",ex);
                msb.show(splash);
                System.exit(0);
            }
            Shared.isOffline = true;
        }
        splash.changeStatus("Inicializando configuración general...", 45);
        Shared.initialize();

        if ( !Shared.isOffline ){
            try {
                splash.changeStatus("Sincronizando inventario ...", 60);
                ConnectionDrivers.updateStock();
                for (String table : Constants.tablesToCleanMirror) {
                    splash.changeStatus("Cargando tabla " + table + "...", 65);
                    ConnectionDrivers.cleanMirror(table);
                }
                for (String table : Constants.tablesToMirrorAtBegin) {
                    splash.changeStatus("Replicando tabla " + table + "...", 75);
                    ConnectionDrivers.mirrorTable(table);
                }
            } catch (Exception ex) {
                System.out.println("ex = " + ex.getMessage());
            }
        }

        splash.changeStatus("Creando ventana de login...", 85);
        Login login = new Login();
        Shared.centerFrame(login);
        login.setExtendedState(login.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        login.setVisible(true);

        splash.setVisible(false);

    }
}
