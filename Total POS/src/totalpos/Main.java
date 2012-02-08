package totalpos;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Saul Hidalgo.
 */
public class Main {

    protected static StartSplash splash;

    public static void main(String[] args) {        
        splash = new StartSplash();
        splash.changeStatus("Leyendo archivo de configuración...", 10);
        try {
            if ( Constants.isPos ){
                new ServerSocket(Constants.lockingPort);
            }
        } catch (IOException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Ya existe otra instancia de Total Pos en esta computadora! No se puede continuar");
            msb.show(splash);
            System.exit(0);
        }
        Shared.centerFrame(splash);
        splash.setVisible(true);

        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

        if ( Shared.isLockFile() ){
            MessageBox msb = new MessageBox(MessageBox.SGN_NOTICE, "Total Pos ha sido cerrado repentinamente mientras trabajaba. Se pudo haber perdido información.");
            msb.show(splash);
            Shared.removeLockFile();
        }
        
        try {
            Shared.loadFileConfig();
        } catch (IOException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Error al leer el archivo de configuración. No se puede continuar",ex);
            msb.show(splash);
            System.exit(0);
        }
        splash.changeStatus("Verificando variables básicas...", 20);
        for (String var : Constants.var2check) {
            if ( Shared.getFileConfig(var) == null ){
                MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "La variable " + var + " es obligatoria. No se puede continuar");
                msb.show(splash);
                System.exit(0);
            }
        }
        
        splash.changeStatus("Conectado a base de datos...", 30);
        ConnectionDrivers.initialize();

        // TODO UNCOMMENT THIS
        /*if ( !Constants.isPos ){
            splash.changeStatus("Actualizando bancos y gastos desde SAP", 40);
            Shared.updateExpensesAndBanks();
        }*/

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
                ConnectionDrivers.updateMoney();
                for (String table : Constants.tablesToCleanMirror) {
                    splash.changeStatus("Cargando tabla " + table + "...", 65);
                    ConnectionDrivers.cleanMirror(table);
                }
                for (String table : Constants.tablesToMirrorAtBegin) {
                    splash.changeStatus("Replicando tabla " + table + "...", 75);
                    ConnectionDrivers.mirrorTableFastMode(table);
                }
            } catch (Exception ex) {
                System.out.println("ex = " + ex.getMessage());
            }
        }

        splash.changeStatus("Creando ventana de login...", 95);
        Login login = new Login();
        Shared.centerFrame(login);
        login.setExtendedState(login.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        login.setVisible(true);

        splash.setVisible(false);
        
    }
}
