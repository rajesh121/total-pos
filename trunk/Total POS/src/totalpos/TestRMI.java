package totalpos;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shidalgo
 */
public class TestRMI {

    private static int port = 9090;
    private static String serverRmi = "localhost";
    private static String rmiServiceName = "TotalPosRMI";

    public static void main(String[] args) {
        /*try {
            Services service = (Services) Naming.lookup("rmi://" + serverRmi + ":" + port + "/" + rmiServiceName);
            service.initialize();
            System.out.println("Inicializado!");
            service.deleteDataFrom("20111025", "9015");
            System.out.println("Listo!");
        } catch (SQLException ex) {
            Logger.getLogger(TestRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(TestRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(TestRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(TestRMI.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}
