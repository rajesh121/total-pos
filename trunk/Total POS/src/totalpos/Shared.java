package totalpos;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLWriter;
import srvEntidades.BNKA;
import srvEntidades.DD07T;
import srvEntidades.IsrvEntidades;
import srvEntidades.SrvEntidades;
import ws.WS;
import ws.WSService;

/**
 *
 * @author Saul Hidalgo
 */
public class Shared {

    private static TreeMap<String,String> config = new TreeMap<String, String>();
    private static TreeMap<String,String> fileConfig = new TreeMap<String, String>();
    private static Component myMainWindows = null;
    private static TreeMap<String, Integer> tries = new TreeMap<String, Integer>();
    private static User user;
    private static UpdateClock screenSaver;
    private static TreeMap<Integer, String> errMapping = new TreeMap<Integer, String>();
    protected static TreeMap<Integer, String> ncrErrMapping = new TreeMap<Integer, String>();
    protected static boolean isOffline = false;
    private static TreeMap<String , Item > newItemMapping;
    private static boolean hadMovements;
    protected static List< XMLElement > itemsNeeded;
    private static int processingWindows = 0;

    protected static void initialize(){
        errMapping.put(new Integer(0), "No hay error");
        errMapping.put(new Integer(1), "Fin de entrega de papel");
        errMapping.put(new Integer(2), "Error mecánico con el papel");
        errMapping.put(new Integer(3), "Fin en la entrega de papel");
        errMapping.put(new Integer(80), "Comando Inválido");
        errMapping.put(new Integer(84), "Tasa Inválida");
        errMapping.put(new Integer(88), "Directivas Inválidas");
        errMapping.put(new Integer(92), "Comando Inválido");
        errMapping.put(new Integer(96), "Error fiscal");
        errMapping.put(new Integer(100), "Error de la memoria fiscal");
        errMapping.put(new Integer(108), "Memoria fiscal llena");
        errMapping.put(new Integer(112), "Buffer completo");
        errMapping.put(new Integer(128), "Error en la comunicación");
        errMapping.put(new Integer(137), "No hay respuesta");
        errMapping.put(new Integer(144), "Error LRC");
        errMapping.put(new Integer(145), "Error con el API");
        errMapping.put(new Integer(153), "Error al crear el archivo");
        ncrErrMapping.put(new Integer(1), "La fecha enviada en el comando es anterior a la ultima utilizada");
        ncrErrMapping.put(new Integer(2), "No hay transacciones por cancelar");
        ncrErrMapping.put(new Integer(3), "Error en operación aritmética");
        ncrErrMapping.put(new Integer(4), "Error en los parámetros del comando");
        ncrErrMapping.put(new Integer(5), "El parámetro límite de la configuración ha sido alcanzado");
        ncrErrMapping.put(new Integer(6), "Falta papel");
        ncrErrMapping.put(new Integer(7), "El comando no puede ser ejecutado, hay una transaccion pendiente");
        ncrErrMapping.put(new Integer(8), "Memoria fiscal vacía");
        ncrErrMapping.put(new Integer(9), "Memoria fiscal llena");
        ncrErrMapping.put(new Integer(10), "Error de comunicación con la impresora fiscal");
        ncrErrMapping.put(new Integer(11), "Impresora fiscal no autenticada");
        ncrErrMapping.put(new Integer(12), "El cheque no ha sido colocado o la información para imprimirse no ha sido enviada");
        ncrErrMapping.put(new Integer(13), "Comando previo en curso");
        ncrErrMapping.put(new Integer(14), "Hay un cheque en curso");
        ncrErrMapping.put(new Integer(16), "Error of leyendo o escribiendo memoria fiscal (CRC error)");
        ncrErrMapping.put(new Integer(249), "Memoria fiscal procesando transacciones");
        ncrErrMapping.put(new Integer(250), "Error en secuencia de paquetes");
        ncrErrMapping.put(new Integer(251), "Error en el contador de secuencia de paquetes");
        ncrErrMapping.put(new Integer(252), "Error en la cabecera de paquetes");
        ncrErrMapping.put(new Integer(253), "Error de paquetes CRC");
        ncrErrMapping.put(new Integer(254), "Time out de paquetes");
        ncrErrMapping.put(new Integer(255), "Número de comando inválido");
        ncrErrMapping.put(new Integer(301),"Error in Reception");
        ncrErrMapping.put(new Integer(302),"Error in Transmission");
        ncrErrMapping.put(new Integer(303),"Invalid command");
        ncrErrMapping.put(new Integer(304),"Repeated Packaged");
        ncrErrMapping.put(new Integer(305),"Fiscal memory does not respond");
        ncrErrMapping.put(new Integer(306),"Error Opening the Historical Transactions Log");
        ncrErrMapping.put(new Integer(307),"Error Recording in the Historical Transactions Log");
        ncrErrMapping.put(new Integer(308),"Invalid Type of Report");
        ncrErrMapping.put(new Integer(309),"TimeOut in Communication");
        ncrErrMapping.put(new Integer(310),"Invalid Date format");
        ncrErrMapping.put(new Integer(311),"Invalid Hour format");
        ncrErrMapping.put(new Integer(312),"Closing error at the Historical Transactions Log");
        ncrErrMapping.put(new Integer(313),"The Amount Cannot Be Zero");
        ncrErrMapping.put(new Integer(314),"The Price Cannot Be Zero");
        ncrErrMapping.put(new Integer(315),"The Amount Cannot to be Greater to 12 Digits");
        ncrErrMapping.put(new Integer(316),"The Price Cannot be Greater to 12 Digits");
        ncrErrMapping.put(new Integer(317),"Item ‘s Description Cannot Be Empty");
        ncrErrMapping.put(new Integer(318),"Global Surcharge , payment types and Discount of Documents Cannot to be Greater to 14 Digits");
        ncrErrMapping.put(new Integer(319),"Length of Client’s Name Cannot Be Greater to 106 Characters");
        ncrErrMapping.put(new Integer(320),"Invalid Operation Type");
        ncrErrMapping.put(new Integer(321),"Invalid IVA Type");
        ncrErrMapping.put(new Integer(322),"The Amount Cannot Be Zero");
        ncrErrMapping.put(new Integer(323),"The Amount Cannot to be Greater to 12 Digit");
        ncrErrMapping.put(new Integer(324),"Length of the Barcode Cannot Be Greater to 12 Digits");
        ncrErrMapping.put(new Integer(325),"Operation’s Description Cannot Be In Empty");
        ncrErrMapping.put(new Integer(326),"Length of Client’s ID Cannot Be Greater to 30 Characters");
        ncrErrMapping.put(new Integer(327),"The Printer is Not Online");
        ncrErrMapping.put(new Integer(328),"The Check’s Amount Cannot Be Zero");
        ncrErrMapping.put(new Integer(329),"The Maximum Length of nonFiscal Text Is 38 Characters");
        ncrErrMapping.put(new Integer(330),"The Check’s Amount Cannot be Greater to 12 Digits");
        ncrErrMapping.put(new Integer(331),"The Check ‘s Amount In Letters Exceeds the 120 Characters");
        ncrErrMapping.put(new Integer(332),"The Year‘s Validation Paramter is Optional and Only accepts Value 1");
        ncrErrMapping.put(new Integer(333),"The Type of Report by Intervals Z Must Be Only 0 = Month to Month or 1= Day to Day");
        ncrErrMapping.put(new Integer(334),"Invalid Line Number");
        ncrErrMapping.put(new Integer(335),"The Header ‘s Maximum Length is 38 Characters");
        ncrErrMapping.put(new Integer(336),"The Payment Types Maximum Length is 15 Characters");
        ncrErrMapping.put(new Integer(337),"The Products Item Maximum Length is 190 Characters");
        ncrErrMapping.put(new Integer(338),"Invalid IVA length [Max. 2 integers and 2 Decimal");
        ncrErrMapping.put(new Integer(339),"Client’s Name Cannot Be Empty");
        ncrErrMapping.put(new Integer(340),"Fiscal Memory ID Cannot Be Empty");
        ncrErrMapping.put(new Integer(341),"Document Number Cannot Be Zero or Empty");
        ncrErrMapping.put(new Integer(342),"The Initial Date Cannot Be Greater to the Final Date");
        ncrErrMapping.put(new Integer(343),"The Cancellations/Surcharges’ Descriptions Maximum Length Is 190 Characters");
        ncrErrMapping.put(new Integer(344),"Invalid Type of Check Process");
        ncrErrMapping.put(new Integer(345),"The IVA Amount Cannot Be Empty");
        ncrErrMapping.put(new Integer(346),"There is a Transaction In Process In the Fiscal Memory");
        ncrErrMapping.put(new Integer(347),"Pay to the Order of Cannot Be Empty");
        ncrErrMapping.put(new Integer(348),"Account Number Cannot Be Empty");
        ncrErrMapping.put(new Integer(349),"Account Number Cannot Be Alphanumeric");
        ncrErrMapping.put(new Integer(350),"Length of Account Number Must Be 10 Min and Max 72 Digits");
        ncrErrMapping.put(new Integer(351),"To Account’s title Cannot Be Empty");
        ncrErrMapping.put(new Integer(352),"Bank of Cuenta Cannot Be Empty");
        ncrErrMapping.put(new Integer(353),"Transactions Log Activated");
        ncrErrMapping.put(new Integer(354),"Historical Transactions Log Deactivated");
        ncrErrMapping.put(new Integer(355),"[Create|Delete] Transactions Log Error");
        ncrErrMapping.put(new Integer(356),"Historical Transactions Log Erased");
        ncrErrMapping.put(new Integer(357),"Historical Transactions Log does not exist in the PATH of the DLL");
        ncrErrMapping.put(new Integer(358),"Parameter Buffer of PrintTextNoFiscal 0 Unbuffered, 1 Buffered");
        ncrErrMapping.put(new Integer(359),"Invalid Type Document");
        ncrErrMapping.put(new Integer(360),"The Allowed Rank of Consultation of RptZ in Memory is from 1 to 1825");
        ncrErrMapping.put(new Integer(361),"Fiscal Memory ID Must Be of 10 Characters");
        ncrErrMapping.put(new Integer(362),"Invalid Type Fonts");
        ncrErrMapping.put(new Integer(363),"Length of Memory ID Cannot Be Greater to 10 Characters");
        ncrErrMapping.put(new Integer(364),"Length of Pay to Order of Cannot Be Greater to 70 Characters");
        ncrErrMapping.put(new Integer(365),"Length of Voucher Cannot Be Greater to 6 Characters");
        ncrErrMapping.put(new Integer(366),"Length of Cashier’s Name Cannot Be Greater to 20 Characters");
        ncrErrMapping.put(new Integer(367),"Length of Title’s Account Cannot Be Greater to 73 Characters");
        ncrErrMapping.put(new Integer(368),"Length of Bank’s Account Cannot Be Greater to 70 Characters");
        ncrErrMapping.put(new Integer(369),"The Serial Port is not Enabled");
        ncrErrMapping.put(new Integer(370),"SetMsgErr Config Error, Only allows 0=Disabled, 1=Enabled");
        ncrErrMapping.put(new Integer(371),"Function Win32 API Error");
        ncrErrMapping.put(new Integer(372),"Invalid Configuration Bps Value 0=9600 Bps, 1=19200 Bps");
        ncrErrMapping.put(new Integer(373),"Invalid Serial Port");
        ncrErrMapping.put(new Integer(374),"Bps Configuration Error");
        ncrErrMapping.put(new Integer(375),"Fiscal Memory fault at Internal Level or Electrical Energy");


    }

    protected static void centerFrame(javax.swing.JFrame frame){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        frame.setLocation(x, y);
    }

    protected static void maximize(JFrame frame){
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    protected static String hashPassword(String x){
        return x.hashCode() + "";
    }

    public static void centerFrame(JDialog dialog) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = dialog.getSize().width;
        int h = dialog.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        dialog.setLocation(x, y);
    }

    public static String booleanToAllowed(boolean b){
        return b?"Habilitado":"Deshabilitado";
    }

    public static User giveUser(List<User> l, String u){
        for (User user : l)
            if ( user.getLogin().equals(u.toLowerCase()) )
                return user;

        return null;
        
    }

    public static List<Profile> updateProfiles(JComboBox rCombo, boolean withEmpty) throws SQLException, Exception {

        List<Profile> profiles = ConnectionDrivers.listProfile("");

        DefaultComboBoxModel dfcbm = (DefaultComboBoxModel) rCombo.getModel();
        dfcbm.removeAllElements();

        if (withEmpty){
            profiles.add(0, new Profile("", ""));
        }

        for (Profile profile : profiles) {
            rCombo.addItem(profile.getId());
        }

        return profiles;
    }

    public static String nextId(int offset){
        try {
            return Shared.getConfig("storeName") + Shared.getFileConfig("myId")
                    + String.format((Shared.isOffline?"9%05d":"%06d"), ConnectionDrivers.lastReceipt()-offset+1);
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
            msb.show(Shared.getMyMainWindows());
            Shared.reload();
            return "";
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas al listar calcular el siguiente código de factura.",ex);
            msb.show(Shared.getMyMainWindows());
            Shared.reload();
            return "";
        }

    }

    public static String nextIdCN(int offset) throws SQLException{
        return Shared.getConfig("storeName") + Shared.getFileConfig("myId")
                + String.format((Shared.isOffline?"9%05d":"%06d"), ConnectionDrivers.lastCreditNote()-offset+1);
    }

    public static void userTrying(String l) throws Exception{
        if ( !tries.containsKey(l) ){
            getTries().put(l, new Integer(1));
        }else if ( getTries().get(l).compareTo(new Integer(1)) > 0 ){
            try {
                ConnectionDrivers.lockUser(l);
            } catch (SQLException ex1) {
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex1);
                msg.show(Shared.getMyMainWindows());
            }
            throw new Exception(Constants.userLocked);
        }else{
            getTries().put(l, new Integer(getTries().get(l) + 1));
        }
    }

    public static void userInsertedPasswordOk(String username){
        getTries().put(username, 0);
    }

    protected static void reload(){
        Login login = new Login();
        Shared.centerFrame(login);
        login.setExtendedState(login.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        if ( getMyMainWindows() instanceof MainWindows ){
            ((MainWindows)getMyMainWindows()).dispose();
        }else if ( getMyMainWindows() instanceof MainRetailWindows ) {
            ((MainRetailWindows)getMyMainWindows()).dispose();
        }
        login.setVisible(true);
        setUser(null);
    }

    protected static void loadFileConfig() throws FileNotFoundException, IOException{
        prepareFile(new File(Constants.rootDir + Constants.fileName4ConfigN), Constants.fileName4ConfigRar, "password4config", Constants.scriptConfig);
        File f = new File(Constants.tmpDir + Constants.fileName4Config);
        Scanner sc = new Scanner(f);
        int lineNumber = 1;
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String[] toks = line.split("==");
            if ( toks.length != 2 ){
                throw new FileNotFoundException("Error al leer la línea " + lineNumber);
            }
            fileConfig.put(toks[0], toks[1]);
            ++lineNumber;
        }
        sc.close();
        f.delete();

        // Backward compatible D=
        if ( !fileConfig.containsKey("printerDriver") ){
            fileConfig.put("printerDriver", "tfhkaif");
        }
    }

    public static String getFileConfig(String k){
        return fileConfig.get(k);
    }

    public static String getConfig(String k){
        return getConfig().get(k);
    }

    public static void loadPhoto(JLabel imageLabel , String addr, int x, int y){
        if ( addr != null ){
            ImageIcon image = new ImageIcon(addr);
            ImageIcon imageIcon = new ImageIcon(image.getImage().getScaledInstance( x, y, Image.SCALE_AREA_AVERAGING));
            imageLabel.setIcon(imageIcon);
            imageLabel.setVisible(true);
        }
    }

    public static double round(double value, int decimalPlace)
    {
      double power_of_ten = 1;
      double fudge_factor = 0.05;
      while (decimalPlace-- > 0) {
         power_of_ten *= 10.0d;
         fudge_factor /= 10.0d;
      }
      return Math.round((value + fudge_factor)* power_of_ten)  / power_of_ten;
    }

    /**
     * @return the config
     */
    public static TreeMap<String, String> getConfig() {
        return config;
    }

    /**
     * @param aConfig the config to set
     */
    public static void setConfig(TreeMap<String, String> aConfig) {
        config = aConfig;
    }

    /**
     * @return the myMainWindows
     */
    protected static Component getMyMainWindows() {
        return myMainWindows;
    }

    /**
     * @param aMyMainWindows the myMainWindows to set
     */
    protected static void setMyMainWindows(Component aMyMainWindows) {
        myMainWindows = aMyMainWindows;
    }

    /**
     * @return the tries
     */
    protected static TreeMap<String, Integer> getTries() {
        return tries;
    }

    /**
     * @param aTries the tries to set
     */
    protected static void setTries(TreeMap<String, Integer> aTries) {
        tries = aTries;
    }

    /**
     * @return the user
     */
    protected static User getUser() {
        return user;
    }

    /**
     * @param aUser the user to set
     */
    protected static void setUser(User aUser) {
        user = aUser;
    }

    public static UpdateClock getScreenSaver() {
        return screenSaver;
    }

    public static void setScreenSaver(UpdateClock screenSaver) {
        Shared.screenSaver = screenSaver;
    }

    public static Turn getTurn(List<Turn> l ,String turnId){
        for (Turn turn : l) {
            if ( turn.getIdentificador().equals(turnId))
                return turn;
        }
        return null;
    }

    /**
     * @return the errMapping
     */
    public static TreeMap<Integer, String> getErrMapping() {
        return errMapping;
    }

    /**
     * @param errMapping the errMapping to set
     */
    public static void setErrMapping(TreeMap<Integer, String> errMapping_) {
        errMapping = errMapping_;
    }

    public static String formatDoubleToPrint(Double d){
        DecimalFormat df = new DecimalFormat("00000000.00");
        return df.format(d).replaceAll(",", "");
    }

    public static String formatDoubleToSpecifyMoneyInPrinter(Double d){
        DecimalFormat df = new DecimalFormat("0000000000.00");
        return df.format(d).replaceAll(",", "");
    }

    public static String formatDoubleToPrintDiscount(Double d){
        DecimalFormat df = new DecimalFormat("00.00");
        return df.format(d*100.0).replaceAll(",", "");
    }

    public static String formatQuantToPrint(Double d){
        DecimalFormat df = new DecimalFormat("00000.000");
        return df.format(d).replaceAll(",", "");
    }

    public static Double getIva(){
        try{
            return Double.parseDouble(getConfig("iva"))*100.0;
        }catch (NumberFormatException nfe){
            return .0;
        }
    }

    public static void what2DoWithReceipt(MainRetailWindows myParent , Exception msg){
        try{
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, Constants.errWithPrinter , msg);
            msb.show(null);

            myParent.toWait();
            myParent.updateAll();
        } catch (SQLException ex1) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex1);
            msb.show(null);
        }
    }

    public static void parseDiscounts(String fileAdr) throws FileNotFoundException, SQLException{
        Scanner sc = new Scanner(new File(fileAdr));
        while (sc.hasNextLine()) {
            String[] toks = sc.nextLine().split("\t");
            ConnectionDrivers.updateDiscount(myTrim(toks[0]) , toks[8]);
        }
        sc.close();
    }

    public static String myTrim(String str){
        return str.substring(1, str.length()-1);
    }

    public static List<Item> parseItems(String fileAddr) throws FileNotFoundException, ParseException, IOException{
        newItemMapping = new TreeMap<String, Item>();
        List<Item> ans = new LinkedList<Item>();
        DataInputStream in = new DataInputStream(new FileInputStream(fileAddr));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = "";
        while ( (line = br.readLine()) != null ) {
            String[] toks = line.split("\t");
            
            Price p = new Price( null , Double.parseDouble(toks[35])/(getIva()/100.0+1.0));
            Cost c = new Cost(null, Double.parseDouble(toks[55]));
            List<Price> lp = new LinkedList<Price>();
            lp.add(p);
            List<Cost> lc = new LinkedList<Cost>();
            lc.add(c);
            List<String> barcodes = new LinkedList<String>();
            barcodes.add(myTrim(toks[9]));
            Item i = new Item(myTrim(toks[0]),
                    myTrim(toks[1]) , Constants.dateFormatter.parse(toks[2].split(" ")[0]) , myTrim(toks[4]),
                    "",  myTrim(toks[6]), myTrim(toks[9]), myTrim(toks[10]), myTrim(toks[14]),
                    myTrim(toks[15]), Integer.parseInt(toks[19].split("\\.")[0]), lp, lc, barcodes,
                    toks[85].equals("T"), Constants.photoPrefix + myTrim(toks[0]) + ".JPG", "0");
            ans.add(i);
            newItemMapping.put(i.getCode(), i);
        }
        in.close();
        return ans;
    }

    public static List<Movement> parseMovements(String fileAddrMain, String fileAddrDetails) throws FileNotFoundException, ParseException, IOException{
        List<Movement> ans = new LinkedList<Movement>();

        DataInputStream inM = new DataInputStream(new FileInputStream(fileAddrMain));
        DataInputStream inD = new DataInputStream(new FileInputStream(fileAddrDetails));
        BufferedReader brM = new BufferedReader(new InputStreamReader(inM));
        BufferedReader brD = new BufferedReader(new InputStreamReader(inD));

        TreeMap<String , List<ItemQuant> > t = new TreeMap<String, List<ItemQuant>>();

        String line;
        while ( (line = brD.readLine()) != null ) {
            String[] toks = line.split("\t");
            if ( !t.containsKey(toks[0]) ){
                t.put(toks[0], new LinkedList<ItemQuant>());
            }
            int mul = 1;
            if ( toks[2].equals("\"TSAL\"") ){
                mul = -1;
            }
            t.get(toks[0]).add(new ItemQuant(myTrim(toks[3]), ((int) Double.parseDouble(toks[4])) * mul ));
        }

        while( (line = brM.readLine()) != null ){
            String[] toks = line.split("\t");
            if ( myTrim(toks[25]).equals(Shared.getConfig("storeName")) ){
                hadMovements = true;
                Date dd = Constants.dateFormatter.parse(toks[1].split(" ")[0]);
                java.sql.Date ddsql = new java.sql.Date(dd.getYear(), dd.getMonth(), dd.getDate());
                Movement m = new Movement(toks[0], ddsql
                        , myTrim(toks[2]) , myTrim(toks[16]), myTrim(toks[28]), t.get(toks[0]));
                ans.add(m);
            }
        }

        inM.close();
        inD.close();
        return ans;
    }

    public static void updateExpensesAndBanks(){
        try {
            SrvEntidades srvEnt = new SrvEntidades();
            IsrvEntidades bHBIE = srvEnt.getBasicHttpBindingIsrvEntidades();
            List<BNKA> lbnka = bHBIE.obtenerBancosSap(Constants.mant).getBNKA();
            String banks = "";
            for (BNKA bnka : lbnka) {
                banks += "{" + bnka.getBANKL().getValue() + " - " + bnka.getBANKA().getValue() + "}";
            }
            ConnectionDrivers.updateConfig("banks", banks);
            List<DD07T> dD07T = bHBIE.obtenerTiposSap().getDD07T();
            String expenses = "";
            for (DD07T dd07t : dD07T) {
                try{
                    Integer.parseInt(dd07t.getDOMVALUEL().getValue());
                    expenses += "{" + dd07t.getDOMVALUEL().getValue() + " - " + dd07t.getDDTEXT().getValue() + "}";
                }catch(NumberFormatException e){
                    ;// Nothing to do. That is not a number
                }
            }
            ConnectionDrivers.updateConfig("expenses", expenses);
            
        } catch (SQLException ex) {
            System.err.println("Problemas actualizando a los bancos.");
        }
    }

    public static void updateMovements() throws FileNotFoundException, SQLException, ParseException, IOException{
        hadMovements = false;
        System.out.println("Parse Items");
        List<Item> items = parseItems(Constants.addrForIncome + "art.txt");
        System.out.println("Listo\nActualizar Items");
        ConnectionDrivers.updateItems(items);
        System.out.println("Actualizar Movimientos");
        List<Movement> movements = parseMovements(Constants.addrForIncome + "ajuste.txt", Constants.addrForIncome + "reng_aju.txt");
        ConnectionDrivers.updateMovements(movements, newItemMapping);
        parseDiscounts(Constants.addrForIncome + "descuen.txt");
    }

    public static boolean isHadMovements() {
        return hadMovements;
    }
    
    public static String formatIt(String msg1, String msg2){
        char[] spaces = new char[Constants.longReportTotals - msg1.length() - msg2.length()];
        Arrays.fill(spaces, ' ');
        return msg1 + new String(spaces) + msg2;
    }

    static void prepareMovements(File myRar) throws IOException {
        String cmd = "copy \"" + myRar.getAbsolutePath() + "\" \"" + Constants.addrForIncome + Constants.fileName4Income + "\"\n"+
                "cd \"" + Constants.addrForIncome + "\"\n" +
                "erase *.txt\n" +
                "\"C:\\Archivos de programa\\WinRAR\\unrar.exe\" e " + Constants.fileName4Income + "\n"+
                "erase " + Constants.fileName4Income + "\n";

        FileWriter fstream = new FileWriter(Constants.rootDir + Constants.scriptMovementsName);
        BufferedWriter out = new BufferedWriter(fstream);

        out.write(cmd);
        out.close();

        Process process = Runtime.getRuntime().exec(Constants.rootDir + Constants.scriptMovementsName);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        while ( br.readLine() != null) {
            ;
        }
        File f = new File(Constants.rootDir + Constants.scriptMovementsName);
        f.delete();

    }

    public static void createBackup(String table) throws IOException, SQLException{
        String cmd = "mysqldump -u " + Constants.dbUser + " -p" + Constants.dbPassword + " " + Constants.dbName + " " + table + " > " +
                Constants.backupDir + Constants.sdfDay2DB.format(Calendar.getInstance().getTime()) + "-" +
                Constants.sdfHour2BK.format((Calendar.getInstance().getTime()));

        FileWriter fstream = new FileWriter(Constants.tmpDir + Constants.scriptMovementsName);
        BufferedWriter out = new BufferedWriter(fstream);

        out.write(cmd);
        out.close();

        Process process = Runtime.getRuntime().exec(Constants.tmpDir + Constants.scriptMovementsName);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        while ( br.readLine() != null) {
            ;
        }
        File f = new File(Constants.tmpDir + Constants.scriptMovementsName);
        f.delete();
    }

    protected static void prepareFile(File myRar, String fileName, String configKey, String scriptFile) throws IOException {
        String pass = Shared.getConfig(configKey);
        if ( configKey.equals("password4config") ){
            pass = Constants.configPassword;
        }
        String cmd = "copy \"" + myRar.getAbsolutePath() + "\" \"" + Constants.tmpDir + fileName + "\"\n"+
                "cd \"" + Constants.tmpDir + "\"\n" +
                "\"C:\\Archivos de programa\\WinRAR\\unrar.exe\" -p" + pass + " e -y " + fileName + "\n"+
                "erase " + Constants.tmpDir + fileName + "\n";

        FileWriter fstream = new FileWriter(Constants.tmpDir + scriptFile);
        BufferedWriter out = new BufferedWriter(fstream);

        out.write(cmd);
        out.close();

        Process process = Runtime.getRuntime().exec(Constants.tmpDir + scriptFile);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        while ( br.readLine() != null) {
            ;
        }
        File f = new File(Constants.tmpDir + scriptFile);
        f.delete();

    }

    protected static void checkVisibility(JTable table) {
        Rectangle rect = table.getCellRect(table.getSelectedRow(), 0, true);
        table.scrollRectToVisible(rect);
    }

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public static int calculateReason(String bwart, String shkzg){
        int reason = 0;
        for (String ii : Constants.incomingItems) {
            if ( bwart.equals(ii) ){
                reason = 1;
            }
        }

        for (String oi : Constants.outcomingItems) {
            if ( bwart.equals(oi) ){
                reason = -1;
            }
        }

        boolean isEqual = false;
        for (String bMovement : Constants.bwartMovement) {
            if ( bwart.equals(bMovement) ){
                isEqual = true;
            }
        }
        if ( isEqual ){
            if ( shkzg.equals("H") ){
                reason = -1;
            }else if ( shkzg.equals("S") ){
                reason = 1;
            }
        }
        return reason;
    }

    public static String b2s(byte b[]) {
        // Converts C string to Java String
        int len = 0;
        while (len < b.length && b[len] != 0)
        ++len;
        return new String(b, 0, len);
    }

    protected static String now4backup(){
        return Constants.sdf4backup.format(Calendar.getInstance().getTime()) + ".rar";
    }

    protected static void createBackup() throws IOException, IllegalStateException, FTPIllegalReplyException, FTPException, FileNotFoundException, FTPDataTransferException, FTPAbortedException{
        String cmd = "mysqldump -u " + Constants.dbUser + " -p" + Constants.dbPassword + " " + Constants.dbName  +
                " > " + Constants.tmpDir + "Backup.sql";

        FileWriter fstream = new FileWriter(Constants.tmpDir + Constants.tmpScript);
        BufferedWriter out = new BufferedWriter(fstream);

        out.write(cmd);
        out.close();

        Process process = Runtime.getRuntime().exec(Constants.tmpDir + Constants.tmpScript);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        while ( br.readLine() != null) {
            ;
        }

        String fileName = now4backup();
        cmd = "\"C:\\Archivos de programa\\WinRAR\\Rar.exe\" a -m5 -ed " + Constants.tmpDir
                + fileName + " " + Constants.tmpDir + "Backup.sql";
        process = Runtime.getRuntime().exec(cmd);
        is = process.getInputStream();
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);

        while ( br.readLine() != null) {
            ;
        }

        FTPClient client = new FTPClient();
        client.connect(Constants.ftpBackupAddr);
        client.login(Constants.ftpBackupUser, Constants.ftpBackupPassword);
        client.changeDirectory("/" + Shared.getConfig("storeName"));
        File f = new File(Constants.tmpDir + fileName);
        client.upload(f);
        client.disconnect(false);

    }

    static void sendSells(String myDay, ClosingDay cd , String ansMoney ) throws SQLException, IOException {
        List< ReceiptSap > CreditNoteGroup = new LinkedList<ReceiptSap>();
        // CN
        List<Receipt> receipts = ConnectionDrivers.listOkCN(myDay);

        /*if ( receipts.isEmpty() ){
            MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "No se puede continuar, debe existir al menos una nota de crédito.");
            msg.show(this);
            return;
        }*/
        // TODO UNCOMMENT THIS
        ReceiptSap rs = new ReceiptSap(myDay);
        int previousId = -1;
        String previousCli = "Contado";
        for (Receipt receipt : receipts) {
            if ( receipt.getFiscalNumber().isEmpty() ){
                System.out.println("Error con la factura " + receipt.getInternId());
                continue;
            }
            if ( (previousId == -1 || previousId +1 == Integer.parseInt(receipt.getFiscalNumber() )
                    && receipt.getClientId().equals("Contado") && receipt.getClientId().equals(previousCli)) ){
                rs.add(receipt);
            }else{
                CreditNoteGroup.add(rs);
                rs = new ReceiptSap(myDay);
                rs.add(receipt);
            }
            previousId = Integer.parseInt(receipt.getFiscalNumber());
            previousCli = receipt.getClientId();
        }
        if ( rs.getSize() > 0 ){
            CreditNoteGroup.add(rs);
        }

        IXMLElement xmlCN = new XMLElement("Notas");

        for (ReceiptSap receiptSap : CreditNoteGroup) {
            IXMLElement child = xmlCN.createElement("CN");
            xmlCN.addChild(child);
            child.setAttribute("getId", receiptSap.getId());
            child.setAttribute("getKind", receiptSap.getKind());
            child.setAttribute("getClient", receiptSap.getClient());
            child.setAttribute("range", receiptSap.getMinFiscalId() + "-" + receiptSap.getMaxFiscalId());
            child.setAttribute("getZ", receiptSap.getZ());
            child.setAttribute("getPrinterId", receiptSap.getPrinterId());
            int position = 1;
            for (Receipt receipt : receiptSap.receipts) {
                for (Item2Receipt item2Receipt : receipt.getItems()) {
                    IXMLElement childchild = child.createElement("CND");
                    child.addChild(childchild);
                    childchild.setAttribute("id", "D" + receiptSap.getId());
                    childchild.setAttribute("position", Constants.df2intSAP.format(position++));
                    childchild.setAttribute("barcode", item2Receipt.getItem().getMainBarcode());
                    childchild.setAttribute("quant", item2Receipt.getQuant().toString());
                    childchild.setAttribute("sellUnits", item2Receipt.getItem().getSellUnits());
                    childchild.setAttribute("sellPrice", item2Receipt.getSellPrice()+"");
                    childchild.setAttribute("discount", (item2Receipt.getSellDiscount()/100.0)*item2Receipt.getSellPrice()+"");
                }

            }
            System.out.println("child = " + receiptSap.getMinFiscalId() + "-" + receiptSap.getMaxFiscalId());
        }
        List< ReceiptSap > receiptGroup = new LinkedList<ReceiptSap>();
        receipts = ConnectionDrivers.listOkReceipts(myDay);

        if ( receipts.isEmpty() ){
            MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "No se puede continuar, debe existir al menos una factura.");
            msg.show(cd);
            return;
        }
        rs = new ReceiptSap(myDay);
        previousId = -1;
        previousCli = "Contado";
        Double previousDis = -1.0;

        List<String> clients = new LinkedList<String>();

        for (Receipt receipt : receipts) {

            if ( !receipt.getClientId().equals("Contado") ){
                clients.add(receipt.getClientId());
            }

            if ( receipt.getFiscalNumber().isEmpty() ){
                System.out.println("Error con la factura " + receipt.getInternId());
                continue;
            }
            if ( (previousId == -1 || previousId +1 == Integer.parseInt(receipt.getFiscalNumber() ) &&
                    receipt.getClientId().equals("Contado") && receipt.getClientId().equals(previousCli)) &&
                    ( Math.abs(receipt.getGlobalDiscount() - previousDis) < Constants.exilon || previousDis == -1.0 )){
                rs.add(receipt);
            }else{
                receiptGroup.add(rs);
                rs = new ReceiptSap(myDay);
                rs.add(receipt);
            }
            previousId = Integer.parseInt(receipt.getFiscalNumber());
            previousCli = receipt.getClientId();
            previousDis = receipt.getGlobalDiscount();
        }
        if ( rs.getSize() > 0 ){
            receiptGroup.add(rs);
        }

        IXMLElement xmlRe = new XMLElement("Facturas");

        for (ReceiptSap receiptSap : receiptGroup) {
            IXMLElement child = xmlRe.createElement("Re");
            xmlRe.addChild(child);
            child.setAttribute("getId", receiptSap.getId());
            child.setAttribute("getKind", receiptSap.getKind());
            child.setAttribute("getClient", receiptSap.getClient());
            child.setAttribute("range", receiptSap.getMinFiscalId() + "-" + receiptSap.getMaxFiscalId());
            child.setAttribute("getZ", receiptSap.getZ());
            child.setAttribute("getPrinterId", receiptSap.getPrinterId());

            int position = 1;
            for (Receipt receipt : receiptSap.receipts) {
                Double gDisc = receipt.getGlobalDiscount();
                for (Item2Receipt item2Receipt : receipt.getItems()) {
                    IXMLElement childchild = child.createElement("CND");
                    child.addChild(childchild);
                    childchild.setAttribute("id", "F" + receiptSap.getId());
                    childchild.setAttribute("position", Constants.df2intSAP.format(position++));
                    childchild.setAttribute("barcode", item2Receipt.getItem().getMainBarcode());
                    childchild.setAttribute("quant", item2Receipt.getQuant().toString());
                    childchild.setAttribute("sellUnits", item2Receipt.getItem().getSellUnits());
                    childchild.setAttribute("sellPrice", item2Receipt.getSellPrice()+"");
                    Double tmpD = (item2Receipt.getSellDiscount()/100.0)*item2Receipt.getSellPrice();
                    childchild.setAttribute("discount", tmpD + gDisc*(item2Receipt.getSellDiscount()-tmpD) +"");
                }

            }
            System.out.println("child = " +receiptSap.getMinFiscalId() + "-" + receiptSap.getMaxFiscalId());
        }

        XMLElement clienXML = new XMLElement("Clientes");


        List<Object> clientC = new LinkedList<Object>();
        for (String c : clients) {
            Client cc = ConnectionDrivers.listClients(c).get(0);
            IXMLElement client = clienXML.createElement("C");
            client.setAttribute("ID", cc.getId());
            String tname = cc.getName();
            client.setAttribute("Name", tname.substring(0,Math.min(35, tname.length())));
            String tc = cc.getAddress() + " Tlf: " + cc.getPhone();
            client.setAttribute("Addr", (tc).substring(0, Math.min(30,tc.length())));
            clienXML.addChild(client);
        }

        String ansTP = "OK";
        System.out.println("Comienzo de envio");
        WS ws = new WSService().getWSPort();
        String ansI = ws.initialize(myDay, Shared.getConfig("storeName"));
        System.out.println("Inicializar = " + ansI);
        if ( !ansI.isEmpty() ) {
            ansTP = ansI;
        }
        ansI = ws.deleteDataFrom();
        System.out.println("Eliminar = " + ansI);
        if ( !ansI.isEmpty() ) {
            ansTP = ansI;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLWriter xmlw = new XMLWriter(baos);
        xmlw.write(xmlCN);
        ansI = ws.sendCreditNotes(baos.toString());
        System.out.println("Nota de Credito = " + ansI );
        if ( !ansI.isEmpty() ) {
            ansTP = ansI;
        }
        ByteArrayOutputStream baosF = new ByteArrayOutputStream();
        XMLWriter xmlwF = new XMLWriter(baosF);
        xmlwF.write(xmlRe);
        ansI =  ws.sendReceipts(baosF.toString());
        System.out.println("Facturas = " + ansI );
        if ( !ansI.isEmpty() ) {
            ansTP = ansI;
        }
        ByteArrayOutputStream baosC = new ByteArrayOutputStream();
        XMLWriter xmlwC = new XMLWriter(baosC);
        xmlwC.write(clienXML);
        ansI = ws.sendClients(baosC.toString());
        System.out.println("Clientes = " + ansI);
        if ( !ansI.isEmpty() ) {
            ansTP = ansI;
        }
        ansI = ws.createDummySeller();
        System.out.println("Vend = " + ansI );

        if ( !ansI.isEmpty() ) {
            ansTP = ansI;
        }

        //String ansTP = "";
        // UNCOMMENT THIS
        /*try{
            Shared.createBackup();
        }catch( Exception ex ){
            ansTP = "File Error";
        }*/

        String msgT = "<html><br>Cobranzas: " + ansMoney + "<br>Ventas: " + ansTP + " </html>" ;
        MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, msgT);
        msg.show(cd);
    }

    public static int getProcessingWindows() {
        return processingWindows;
    }

    public static void setProcessingWindows(int processingWindows) {
        Shared.processingWindows = processingWindows;
    }

    public static void createLockFile(){
        FileWriter fstream = null;
        try {
            fstream = new FileWriter(".lock");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("");
            out.close();
        } catch (IOException ex) {
            //Don't do anything!
        } finally {
            try {
                fstream.close();
            } catch (IOException ex) {
                //Don't do anything!
            }
        }
    }

    public static void removeLockFile(){
        File f = new File(".lock");
        f.delete();
    }

    public static boolean isLockFile(){
        File f = new File(".lock");
        return f.canRead();
    }
}
