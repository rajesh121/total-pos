package totalpos;

import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import srvSapPackage.ObjectFactory;

/**
 *
 * @author Saul Hidalgo.
 */
public class Constants {
    protected static final String companyName = "Grupo Total 99";
    protected static final String appName = "Total POS";

    protected static final int numberConnection = 5;

    //protected static final String dbHost = "localhost";
    protected static final String dbName = "gt99";
    protected static final String dbUser = "root";
    protected static final String dbPassword = "123456789";
    protected static final String defaultUser = "Invitado";
    protected static final String configPassword = "Admingt.99Admingt.99";

    protected static final ObjectFactory of = new ObjectFactory();

    //protected static final String mirrorDbHost = "localhost";
    //is This redundant??

    protected static final String mirrorDbName = "gt99mirror";
    protected static final String mirrordbUser = "root";
    protected static final String mirrordbPassword = "123456789";

    protected static final String[] tablesToMirrorAtBegin = {"articulo","usuario","codigo_de_barras","costo",
        "precio","tipo_de_usuario","tipo_de_usuario_puede","asigna","configuracion","nodo","punto_de_venta","cliente"};
    protected static final String[] tablesToMirrorAtDay = {"articulo","precio"};
    protected static final String[] tablesToCleanMirror = {"forma_de_pago","factura_contiene","factura","nota_de_credito_contiene","nota_de_credito"};

    protected static final String[] var2check = {"Server","ServerMirror","myId","printerPort"};

    protected static final String scriptReplicateName = "replicate.bat";
    protected static final String rootDir = "./";
    protected static final String scriptMovementsName = "prepareMovements.bat";
    protected static final String scriptCreateFileReport = "createReport.bat";

    protected static final String wrongPasswordMsg = "Contraseña errónea.";
    protected static final String userLocked = "El usuario ha sido bloqueado.";
    protected static final String dataRepeated = "Esta intentando ingresar valores repetidos";
    protected static final String isDataRepeated = "Duplicate entry \'[^\\\']*\' for key \'[^\\\']*\'";
    protected static final String duplicatedMsg = "Duplicate entry \'GenericTable\' for key \'Generic Key\'";
    protected static final String errWithPrinter = "<html>Hubo un problema con la impresora.<br>"
                    + "Posibles causas:<br>" +
                    "--- Falta de papel. Verifique que la impresora está encendida y revise el papel.<br>"+
                    "--- Falla de comunicación: Verifique que la impresora está encendida y revise la conexión con la impresora<br></html>";

    protected static final SimpleDateFormat sdfHour = new SimpleDateFormat("h:mm a");
    protected static final SimpleDateFormat sdfHour2BK = new SimpleDateFormat("hh-mm-ss-a");
    protected static final SimpleDateFormat sdfDay = new SimpleDateFormat("d MMM yyyy");
    protected static final SimpleDateFormat sdfDay2DB = new SimpleDateFormat("yyyy-MM-dd");
    protected static final SimpleDateFormat sdfDateHour = new SimpleDateFormat("dd-MM-yyyy / h:mm:ss a");
    protected static final SimpleDateFormat sdfDay2SAP = new SimpleDateFormat("yyyyMMdd");
    protected static final DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy");
    protected static final DecimalFormat df = new DecimalFormat("#0.00");
    protected static final DecimalFormat df2z = new DecimalFormat("0000");
    protected static final DecimalFormat df2int = new DecimalFormat("#00.###");
    protected static final DecimalFormat df2intSAP = new DecimalFormat("#000000.###");

    protected static final String storePrefix = "";
    protected static final String waerks = "VEF";

    protected static final boolean isPos = false;

    protected static String maximunId = "9999999999999999999999999";
    protected static String minimunId = "";

    protected static final int secondsToCheckTurn = 30;
    protected static final int secondsToUpdateCountdown = 10;
    protected static final int secondsToUpdateMirror = 6000;
    protected static final int secondsToChangeMsg2Pos = 10;
    protected static final int secondsToShiftMsg = 1;
    protected static final int dbTimeout = 60000;

    protected static final int longReportTotals = 100;

    protected static final int maximumLenghtMsg2Pos = 50;

    protected static final boolean withFiscalPrinter = true;

    protected static final String tmpFileName = "tmp.data";

    protected static final Font font = new Font("Courier New", 0, 12);
    protected static final Color transparent = new Color(0, true);
    protected static final Color lightBlue = new Color(184,207,229);
    protected static final Color lightGreen = new Color(150,255,150);

    protected static final Double minimumCash = 150.0;
    protected static final String[] kindOfBPOS={"Debito","Credito","Ambas"};

    protected static final String reportFolder = "./reports";
    protected static final StyleBuilder boldStyle = stl.style().bold();
    protected static final StyleBuilder boldCenteredStyle = stl.style(boldStyle)
	                                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
    protected static final StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
	                                    .setBorder(stl.pen1Point())
	                                    .setBackgroundColor(Color.LIGHT_GRAY);

    protected static final StyleBuilder titleStyle = stl.style(boldCenteredStyle)
                             .setVerticalAlignment(VerticalAlignment.MIDDLE)
                             .setFontSize(15);

    protected static final String mant = "250";
    protected static final String genericBank = "BGENE";

    protected static final String addrForIncome = ".\\traslados\\";
    protected static final String photoPrefix = ".\\fotos\\";
    protected static final String backupDir = ".\\conf\\";
    protected static final String tmpDir = "C:\\WINDOWS\\Temp\\";

    protected static final String fileName4Income = "income.rar";
    protected static final String fileName4ReportRar = "report.rar";
    protected static final String fileName4ConfigN = "config";
    protected static final String fileName4ConfigRar = "config.rar";
    protected static final String fileName4Config = "config.txt";
    protected static final String scriptConfig = "config.bat";
    protected static final String fileName4Report = "report.txt";
    protected static final String[] forbiddenWords = {"delete","upgrade","insert","drop", "create"};
}
