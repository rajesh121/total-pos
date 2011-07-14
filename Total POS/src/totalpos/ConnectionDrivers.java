package totalpos;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shidalgo
 */
public class ConnectionDrivers {

    protected static ComboPooledDataSource cpds ;
    
    protected static boolean initialize(){
        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            String sT = "jdbc:mysql://" + Constants.dbHost + "/" +
                            Constants.dbName;
            cpds.setJdbcUrl(sT);
            cpds.setUser(Constants.dbUser);
            cpds.setPassword(Constants.dbPassword);

            return true;

        } catch (PropertyVetoException ex) {
            Logger.getLogger(ConnectionDrivers.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }

}
