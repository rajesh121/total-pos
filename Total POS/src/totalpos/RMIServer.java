package totalpos;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.RemoteStub;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shidalgo
 */
public class RMIServer {

    private static int port = 1099;
    private static String rmiServer = "127.0.0.1";
    private static String rmiServiceName = "TotalPosRMI";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println(Shared.now() + " Iniciando RMIServer...");
            RMISocketFactory.setSocketFactory(new FixedPortRMISocketFactory());
            LocateRegistry.createRegistry(Constants.rmiPort);
            Services s = new ServiceImplement();
            UnicastRemoteObject.exportObject(s);
            Naming.rebind("rmi://" + rmiServer + ":" + Constants.rmiPort + "/" + rmiServiceName, s);
            System.out.println(Shared.now() + " Iniciado!");
        }  catch (MalformedURLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
