package totalpos;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Saul Hidalgo.
 */
public class Constants {
    public static final String companyName = "Grupo Total 99";
    public static final String appName = "Total POS";

    public static final int numberConnection = 5;

    public static final String dbHost = "192.168.0.34";
    public static final String dbName = "gt99testing";
    public static final String dbUser = "root";
    public static final String dbPassword = "123456789";
    public static final String defaultUser = "Invitado";

    public static final String wrongPasswordMsg = "Contraseña errónea.";
    public static final String userLocked = "El usuario ha sido bloqueado.";
    public static final String dataRepeated = "Esta intentando ingresar valores repetidos";
    public static final String isDataRepeated = "Duplicate entry \'[^\\\']*\' for key \'[^\\\']*\'";
    public static final String duplicatedMsg = "Duplicate entry \'GenericTable\' for key \'Generic Key\'";

    public static SimpleDateFormat sdfHour = new SimpleDateFormat("h:mm a");
    public static SimpleDateFormat sdfDay = new SimpleDateFormat("d MMM yyyy");
    public static SimpleDateFormat sdfDateHour = new SimpleDateFormat("dd-MM-yyyy / h:mm:ss a");
    public static DecimalFormat df = new DecimalFormat("#.00");

    public static final boolean isPos = true;

    public static int secondToCheckTurn = 30;

    /**
     * TODO: This must be in the local database!!!
     */
    public static String myId = "02";
    public static String tmpFileName = "tmp.data";

    public static Font font = new Font("Courier New", 0, 12);
    public static final Color transparent = new Color(0, true);
    public static final Color lightBlue = new Color(150,150,255);
    public static final Color lightGreen = new Color(150,255,150);
}
