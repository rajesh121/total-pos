package totalpos;

import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ObjectFactory;

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
    //public static final String dbHost = "localhost";
    public static final String dbName = "gt99";
    public static final String dbUser = "root";
    public static final String dbPassword = "123456789";
    public static final String defaultUser = "Invitado";


    
    public static final ObjectFactory of = new ObjectFactory();

    //public static final String mirrorDbHost = "localhost";
    //is This redundant??

    public static final String mirrorDbName = "gt99mirror";
    public static final String mirrordbUser = "root";
    public static final String mirrordbPassword = "123456789";

    public static final String[] tablesToMirrorAtBegin = {"articulo","usuario","codigo_de_barras","costo",
        "precio","tipo_de_usuario","tipo_de_usuario_puede","asigna","configuracion","nodo","punto_de_venta","cliente"};
    public static final String[] tablesToMirrorAtDay = {"articulo","precio"};
    public static final String[] tablesToCleanMirror = {"factura_contiene","factura","nota_de_credito_contiene","nota_de_credito"};

    public static final String[] var2check = {"Server","ServerMirror","myId"};

    public static final String scriptName = "replicate.bat";
    public static final String rootDir = "./";

    public static final String wrongPasswordMsg = "Contraseña errónea.";
    public static final String userLocked = "El usuario ha sido bloqueado.";
    public static final String dataRepeated = "Esta intentando ingresar valores repetidos";
    public static final String isDataRepeated = "Duplicate entry \'[^\\\']*\' for key \'[^\\\']*\'";
    public static final String duplicatedMsg = "Duplicate entry \'GenericTable\' for key \'Generic Key\'";
    public static final String errWithPrinter = "<html>Hubo un problema con la impresora.<br>"
                    + "Posibles causas:<br>" +
                    "--- Falta de papel. Verifique que la impresora está encendida y revise el papel.<br>"+
                    "--- Falla de comunicación: Verifique que la impresora está encendida y revise la conexión con la impresora<br></html>";

    public static final SimpleDateFormat sdfHour = new SimpleDateFormat("h:mm a");
    public static final SimpleDateFormat sdfDay = new SimpleDateFormat("d MMM yyyy");
    public static final SimpleDateFormat sdfDay2DB = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat sdfDateHour = new SimpleDateFormat("dd-MM-yyyy / h:mm:ss a");
    public static final SimpleDateFormat sdfDay2SAP = new SimpleDateFormat("yyyyMMdd");
    public static final DecimalFormat df = new DecimalFormat("#0.00");
    public static final DecimalFormat df2z = new DecimalFormat("0000");
    public static final DecimalFormat df2int = new DecimalFormat("#00.###");
    public static final DecimalFormat df2intSAP = new DecimalFormat("#000000.###");

    public static final String storePrefix = "";
    public static final String waerks = "VEF";

    public static final boolean isPos = false;

    public static String maximunId = "9999999999999999999999999";
    public static String minimunId = "";

    public static final int secondsToCheckTurn = 30;
    public static final int secondsToUpdateCountdown = 10;
    public static final int secondsToUpdateMirror = 6000;
    public static final int secondsToChangeMsg2Pos = 10;
    public static final int secondsToShiftMsg = 1;
    public static final int dbTimeout = 60000;

    public static final int maximumLenghtMsg2Pos = 50;

    public static final boolean withFiscalPrinter = true;

    public static final String tmpFileName = "tmp.data";

    public static final Font font = new Font("Courier New", 0, 12);
    public static final Color transparent = new Color(0, true);
    public static final Color lightBlue = new Color(184,207,229);
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

    public static final String mant = "200";
    public static final String genericBank = "BGENE";

    public static final String addrForIncome = ".\\traslados\\";
}
