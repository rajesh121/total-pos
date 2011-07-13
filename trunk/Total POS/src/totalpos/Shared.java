package totalpos;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Saul Hidalgo
 */
public class Shared {


    protected static void centerFrame(javax.swing.JFrame frame){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        frame.setLocation(x, y);
    }

    protected static String hashPassword(String x){
        try {
            MessageDigest d = MessageDigest.getInstance("SHA-1");
            d.reset();
            d.update(x.getBytes());
            return d.digest().toString();

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Shared.class.getName()).log(Level.SEVERE, null, ex);
            return x;
        }
    }

    protected static boolean login(String user, String password){
        return true;
    }

}
