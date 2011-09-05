package totalpos;

import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 *
 * @author Saul Hidalgo.
 */
public class Constants {
    public static final String companyName = "Grupo Total 99";
    public static final String appName = "Total POS";

    public static final int numberConnection = 5;

    /**
     * TODO: This must be in a configuration file.
     */
    public static final String dbHost = "localhost";
    public static final String dbName = "gt99";
    public static final String dbUser = "root";
    public static final String dbPassword = "123456789";
    public static final String defaultUser = "Invitado";


    public static final String mirrorDbHost = "localhost";
    //is This redundant??

    public static final String mirrorDbName = "gt99mirror";
    public static final String mirrordbUser = "root";
    public static final String mirrordbPassword = "123456789";

    public static String[] tablesToMirrorAtBegin = {"articulo","usuario","codigo_de_barras","costo",
        "precio","tipo_de_usuario","tipo_de_usuario_puede","asigna","configuracion","nodo","punto_de_venta","cliente"};
    public static String[] tablesToMirrorAtDay = {"articulo","precio"};
    public static String[] tablesToCleanMirror = {"factura","factura_contiene","nota_de_credito","nota_de_credito_contiene"};

    public static final String wrongPasswordMsg = "Contraseña errónea.";
    public static final String userLocked = "El usuario ha sido bloqueado.";
    public static final String dataRepeated = "Esta intentando ingresar valores repetidos";
    public static final String isDataRepeated = "Duplicate entry \'[^\\\']*\' for key \'[^\\\']*\'";
    public static final String duplicatedMsg = "Duplicate entry \'GenericTable\' for key \'Generic Key\'";

    public static SimpleDateFormat sdfHour = new SimpleDateFormat("h:mm a");
    public static SimpleDateFormat sdfDay = new SimpleDateFormat("d MMM yyyy");
    public static SimpleDateFormat sdfDay2DB = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat sdfDateHour = new SimpleDateFormat("dd-MM-yyyy / h:mm:ss a");
    public static DecimalFormat df = new DecimalFormat("#0.00");
    public static DecimalFormat df2int = new DecimalFormat("#00.###");

    public static final boolean isPos = true;

    public static int secondsToCheckTurn = 30;
    public static int secondsToUpdateCountdown = 10;
    public static int secondsToUpdateMirror = 15;

    /**
     * TODO: This must be in the local database!!!
     */
    public static String myId = "02";
    public static String tmpFileName = "tmp.data";

    public static Font font = new Font("Courier New", 0, 12);
    public static final Color transparent = new Color(0, true);
    public static final Color lightBlue = new Color(150,150,255);
    public static final Color lightGreen = new Color(150,255,150);

    public static final Double minimumCash = 150.0;
    public static final String[] kindOfBPOS={"Debito","Credito","Ambas"};

    public static final String reportFolder = "./reports";
    public static final StyleBuilder boldStyle = stl.style().bold();
    public static final StyleBuilder boldCenteredStyle = stl.style(boldStyle)
	                                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
    public static final StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
	                                    .setBorder(stl.pen1Point())
	                                    .setBackgroundColor(Color.LIGHT_GRAY);

    public static final StyleBuilder titleStyle = stl.style(boldCenteredStyle)
                             .setVerticalAlignment(VerticalAlignment.MIDDLE)
                             .setFontSize(15); 
}
