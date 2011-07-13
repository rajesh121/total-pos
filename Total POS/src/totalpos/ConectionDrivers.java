package totalpos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shidalgo
 */
public class ConectionDrivers {
    protected static Connection connections[];
    protected static Statement statements[];

    protected static boolean initialize(){
        connections = new Connection[Constants.numberConnection];
        for (int i = 0; i < connections.length; i++) {
            try {
                connections[i] = DriverManager.getConnection("jdbc:mysql://" + Constants.dbHost + "/" + Constants.dbName, Constants.dbUser, Constants.dbPassword);
                statements[i] = connections[i].createStatement();
                
            } catch (SQLException ex) {
                Logger.getLogger(ConectionDrivers.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

        }

        

        return true;
    }

}
