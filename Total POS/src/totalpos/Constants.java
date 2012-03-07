package totalpos;

import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import srvSap.ObjectFactory;

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
    protected static final String dbPassword = ".Gt99Administrador.";
    //protected static final String dbPassword = "123456789";
    protected static final String defaultUser = "Invitado";
    protected static final String configPassword = "Admingt.99Admingt.99";

    protected static final ObjectFactory of = new ObjectFactory();

    //protected static final String mirrorDbHost = "localhost";
    //is This redundant??

    protected static final String mirrorDbName = "gt99mirror";
    protected static final String mirrordbUser = "root";
    protected static final String mirrordbPassword = ".Gt99Administrador.";
    protected static final String ftpHost = "184.172.244.110";
    protected static final String ftpUser = "gt99";
    protected static final String ftpPass = "X)!<jeS-8.;";
    protected static final String ftpDir = "/www/gtotal99/datos/gt99";
    protected static final String ftpBackupAddr = "192.168.0.78";
    protected static final String ftpBackupUser = "tienda";
    protected static final String ftpBackupPassword = "Admingt.99Administrador";

    protected static final String[] tablesToMirrorAtBegin = {"articulo","usuario","codigo_de_barras","costo",
        "precio","tipo_de_usuario","tipo_de_usuario_puede","asigna","configuracion","nodo","punto_de_venta","cliente"};
    protected static final String[] tablesToMirrorAtDay = {"articulo","precio"};
    protected static final String[] tablesToCleanMirror = {"forma_de_pago","factura_contiene","factura","nota_de_credito_contiene","nota_de_credito"};

    //protected static final String[] var2check = {"Server","ServerMirror","myId","printerPort", "printerDriver"};
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
    protected static final SimpleDateFormat sdf4backup = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss-a");
    protected static final SimpleDateFormat sdf4ncr = new SimpleDateFormat("dd MM yyyy hh mm ss a");
    protected static final DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy");
    protected static final DecimalFormat df = new DecimalFormat("#0.00");
    protected static final DecimalFormat df2z = new DecimalFormat("0000");
    protected static final DecimalFormat df2int = new DecimalFormat("#00.###");
    protected static final DecimalFormat df2intSAP = new DecimalFormat("#000000.###");
    protected static final String minimunDate = "20080101";

    protected static final String storePrefix = "";
    protected static final String waerks = "VEF";

    protected static final boolean isPos = false;

    protected static final int ncrYearOffset = 0;

    protected static final byte nonfiscalDoc = 3;
    protected static final byte receipt = 0;
    protected static final byte creditNote = 1;
    protected static final int normalFont = 1;
    protected static final int bigFont = 49;
    protected static final int yearMod = 1;

    protected static String maximunId = "999999999";
    protected static String minimunId = "0";

    protected static final int secondsToCheckTurn = 30;
    protected static final int secondsToUpdateCountdown = 10;
    protected static final int secondsToUpdateMirror = 6000;
    protected static final int secondsToChangeMsg2Pos = 60;
    protected static final int secondsToShiftMsg = 1;
    protected static final int dbTimeout = 60000;

    protected static final int longReportTotals = 100;

    protected static final int maximumLenghtMsg2Pos = 60;

    protected static final boolean withFiscalPrinter = true;

    protected static final String tmpFileName = "tmp.data";

    protected static final Font font = new Font("Courier New", 0, 12);
    protected static final Color transparent = new Color(0, true);
    protected static final Color lightBlue = new Color(184,207,229);
    protected static final Color lightGreen = new Color(150,255,150);

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

    protected static final String mant = "400";
    protected static final String genericBank = "BGENE";
    protected static final String pernr = "999999";
    protected static final String vbeln = "999999";

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
    protected static final String tmpScript = "tmp.bat";
    protected static final String fileName4Report = "report.txt";
    protected static final String[] forbiddenWords = {"delete","upgrade","insert","drop", "create"};

    protected static final String[] outcomingItems = {"906" , "551" , "905" , "702" };
    protected static final String[] incomingItems = {"904" , "701" , "907"};
    protected static final String[] bwartMovement = {"309" , "302" , "301"};

    protected static double exilon = .0001;
    protected static double moneyExilon = 2.0;
    protected static double printerExilon = 4.0;
    protected static String changeQuant = "quant";
    protected static String changeReceipt = "cn";
    protected static String tmpFtpFileName = "file.rar";

    protected static int rmiPort = 9090;
    //protected static String serverRmi = "186.24.10.122";
    protected static String serverRmi = "190.78.226.145";
    //protected static String serverRmi = "192.168.0.16";
    protected static String rmiServiceName = "TotalPosRMI";

    protected static String serverSQLServerAdd = "192.168.0.237";
    //protected static String serverSQLServerAdd = "192.168.0.236";
    protected static String portSqlServer = "1433";
    protected static String dbRMIName = "P01";
    //protected static String dbRMIName = "D01";
    protected static String dbRMIUser = "p02";
    //protected static String dbRMIUser = "d02";
    protected static String dbHeader = "p01";
    //protected static String dbHeader = "d01";
    protected static String dbRMIPass = "adminsqlp03";
    //protected static String dbRMIPass = "adminsqld03";
    protected static Connection connection;

    protected static int maxNcrDescription = 24;

    protected static int lockingPort = 54320;
    static boolean justEmail = false;

    static String email = "ventasdiariasgt99";
    static String passEmail = "gt99server.99";

    protected static String version = "1.0.310";

    protected static int maximumFiscalNumber = 1000000;
    static int triesWithPrinter = 5;

    public static String halfDay = "13:30:00";
    static String halfHour = "00:30:00";
    static Double add2PayForm = .99;
}
