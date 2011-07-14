package totalpos;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
        return x.hashCode() + "";
    }

    protected static boolean login(String user, String password){
        Connection c = null;
        PreparedStatement pstmt = null;
        try {
            c = ConnectionDrivers.cpds.getConnection();
            pstmt = c.prepareStatement("select * from usuario where login = ? and password = ? ");
            pstmt.setString(1, user);
            pstmt.setString(2, hashPassword(password));
            
            if ( ! pstmt.executeQuery().next() ){
                System.out.println("Mala contrasena");
                return false;
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Shared.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
