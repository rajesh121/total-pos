package totalpos;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;

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
            MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "No se encontró el driver.");
            msb.show(Main.splash);
            return false;
        }
        
    }

}
