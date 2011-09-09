package totalpos;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
    protected static boolean isOffline = false;
    protected static StartSplash SharedSS;

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
            if ( user.getLogin().equals(u) )
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

    protected static void loadFileConfig() throws FileNotFoundException{
        Scanner sc = new Scanner(new File("config"));
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
    }

    public static String getFileConfig(String k){
        return fileConfig.get(k);
    }

    public static String getConfig(String k){
        return getConfig().get(k);
    }

    public static void loadPhoto(JLabel imageLabel , String addr, int x, int y){
        ImageIcon image = new ImageIcon(addr);
        ImageIcon imageIcon = new ImageIcon(image.getImage().getScaledInstance( x, y, Image.SCALE_AREA_AVERAGING));
        imageLabel.setIcon(imageIcon);
        imageLabel.setVisible(true);
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

    public static void what2DoWithReceipt(MainRetailWindows myParent , String msg){
        try{
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Hubo un problema con la impresora.\n"
                    + "Posibles causas: " +
                    "--- Falta de papel. Verifique que la impresora está encendida y revise el papel.                       \n"+
                    "--- Falla de comunicación: Verifique que la impresora está encendida y revise la conexión con la impresora");
            msb.show(null);

            myParent.toWait();
            myParent.updateAll();
        } catch (SQLException ex1) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex1);
            msb.show(null);
        }
    }

}
