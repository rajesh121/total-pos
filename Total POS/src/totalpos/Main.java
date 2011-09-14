package totalpos;

import com.microsoft.schemas._2003._10.serialization.ObjectFactory;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.xml.namespace.QName;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ArrayOfZFISCOBRANZA;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ArrayOfZFISDATAFISCAL;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ZFISHISTENVIOS;
import org.tempuri.IsrvSap;
import org.tempuri.SrvSap;

/**
 *
 * @author Saul Hidalgo.
 */
public class Main {

    protected static StartSplash splash;

    public static void main(String[] args) {
     /*   SrvSap ss = new SrvSap();
        ss.getBasicHttpBindingIsrvSap();
        ObjectFactory o = new ObjectFactory();

        ArrayOfZFISCOBRANZA aozfc = new ArrayOfZFISCOBRANZA();
        ArrayOfZFISDATAFISCAL aozfdf = new ArrayOfZFISDATAFISCAL();
 
        ZFISHISTENVIOS zfhe = new ZFISHISTENVIOS();

        zfhe.setMANDT(o.createString("200"));
        zfhe.setIDTIENDA(o.createString(Shared.getConfig().get("storeName")));
        zfhe.setMODIFICAR(o.createString("N"));
        zfhe.setOBSERVACIONES(o.createString("Sin observaciones xD"));
        zfhe.setTOTALVENTASDIA(new BigDecimal(500));
        zfhe.setFONDOCAJA(BigDecimal.ZERO);
        zfhe.setFECHAPROCESADO(o.createString("20120101"));

        //isrvs.sapInsertCobranza(aozfc, aozfdf, zfhe);
        */
        splash = new StartSplash();
        Shared.centerFrame(splash);
        splash.setVisible(true);
        splash.changeStatus("Leyendo archivo de configuración...", 10);
        
        try {
            Shared.loadFileConfig();
        } catch (FileNotFoundException ex) {
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
