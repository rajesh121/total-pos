package totalpos;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Saúl Hidalgo.
 */
public class FiscalPrinter {
    private FiscalDriver printer;
    public boolean isOk = false;
    public String printerSerial = null;
    private String z = null;
    public String lastReceipt = null;

    public FiscalPrinter() {
        if ( Constants.withFiscalPrinter ){
            printer = (FiscalDriver) Native.loadLibrary(Shared.getFileConfig("printerDriver"), FiscalDriver.class);
        }
    }

     public boolean checkPrinter() throws SQLException, FileNotFoundException, Exception {
        if ( !Constants.withFiscalPrinter ){
            return true;
        }
        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            // TODO MODIFY HERE!
            printerSerial = "ABC123456";
            lastReceipt = "0";
            return true;
        }else if ( Shared.getFileConfig("printerDriver").equals("tfhkaif") ){
            return isTheSame(ConnectionDrivers.getMyPrinter());
        }else{
            throw new Exception("Driver de impresora desconocido!");
        }
    }

    private void calculateSerial() throws Exception{

        if ( !Constants.withFiscalPrinter ){
            return;
        }

        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            // TODO CODE HERE =D!
            printerSerial = "ABC123456";
            lastReceipt = "0";
        }else if ( Shared.getFileConfig("printerDriver").equals("tfhkaif") ){
            boolean ansT;
            isOk = false;
            IntByReference a = new IntByReference();
            IntByReference b = new IntByReference();
            ansT = printer.OpenFpctrl(Shared.getFileConfig("printerPort"));
            if ( !ansT ){
                throw new Exception(Shared.getErrMapping().get(128));
            }

            ansT = printer.UploadStatusCmd(a, b, "S1", Constants.tmpFileName);
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }
            assert (ansT);

            File file = new File(Constants.tmpFileName);

            Scanner sc = new Scanner(file);

            ansT = sc.hasNext();
            assert(ansT);
            String line = sc.next();


            printerSerial = line.substring(66, 76);
            z = Constants.df2z.format(Integer.parseInt(line.substring(47, 51))+1);
            lastReceipt = line.substring(21,21+8);
            sc.close();
            file.delete();
            printer.CloseFpctrl();
        }else{
            throw new Exception("Driver de impresora desconocido!");
        }
    }

    public boolean isTheSame(String serial) throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return true;
        }
        return getSerial().equals(serial);
    }

    public void printTicket(List<Item2Receipt> items , Client client, Double globalDiscount, String ticketId, User u , List<PayForm> pfs) throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return;
        }
        boolean everythingCool = true;
        
        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            // TODO CODE HERE =D!
        }else if ( Shared.getFileConfig("printerDriver").equals("tfhkaif") ){
            isOk = false;
            IntByReference a = new IntByReference();
            IntByReference b = new IntByReference();
            printer.OpenFpctrl(Shared.getFileConfig("printerPort"));

            List<String> buffer = new ArrayList<String>();

            if ( !items.isEmpty() ){
                int line = 1;
                if ( client != null && !client.getId().isEmpty() ){
                    buffer.add("i0" + ( line++ ) + "RIF: " + client.getId());
                    if ( !client.getName().trim().isEmpty() ) {
                        buffer.add("i0" + (line++) + "Nombre: " + client.getName());
                    }
                    if ( !client.getPhone().trim().isEmpty() ) {
                        buffer.add("i0" + (line++) + "Telefono: " + client.getPhone());
                    }
                    if ( !client.getAddress().trim().isEmpty() ) {
                        buffer.add("i0" + (line++) + "Direccion: " + client.getAddress());
                    }
                }else{
                    buffer.add("i0" + (line++) + "Cliente: Contado");
                }
                buffer.add("i0" + ( line++ ) + "Correlativo: " + ticketId);
                buffer.add("i0" + ( line++ ) + "Caja: " + Shared.getFileConfig("myId"));

                for (String bu : buffer) {
                    printer.SendCmd(a, b, bu);
                    if ( b.getValue() != 0 ){
                        throw new Exception(Shared.getErrMapping().get(b.getValue()));
                    }
                }

                for (Item2Receipt item2r : items) {
                    Item item = item2r.getItem();
                    printer.SendCmd(a, b, "!" + ( Shared.formatDoubleToPrint(item.getLastPrice().getQuant()) ) +
                            Shared.formatQuantToPrint(item2r.getQuant()+.0) + item.getDescription().substring(0, Math.min(item.getDescription().length(), 37)));
                    if ( b.getValue() != 0 ){
                        throw new Exception(Shared.getErrMapping().get(b.getValue()));
                    }
                    if ( item.getDescuento() != .0 ){
                        Double finalDiscount = item.getDescuento();
                        if ( finalDiscount > .0 ){
                            printer.SendCmd(a, b, "p-"+Shared.formatDoubleToPrintDiscount(finalDiscount/100.0));
                            if ( b.getValue() != 0 ){
                                throw new Exception(Shared.getErrMapping().get(b.getValue()));
                            }
                        }
                    }
                }
                try{
                    printer.SendCmd(a, b, "3");
                    if ( b.getValue() != 0 ){
                        // Fiscal Number has been generated.
                        //System.out.println("RECUPERANDO NUMERO FISCAL!!");
                        lastReceipt = Integer.parseInt(ConnectionDrivers.getLastReceipt())+1+"";
                        everythingCool = false;
                    }

                    if ( everythingCool ){
                        printer.SendCmd(a, b, "y" + ticketId);// + Shared.formatDoubleToPrintDiscount(globalDiscount));
                        if ( b.getValue() != 0 ){
                            lastReceipt = Integer.parseInt(ConnectionDrivers.getLastReceipt())+1+"";
                            everythingCool = false;
                        }

                        if ( globalDiscount != null && globalDiscount > 0 ){
                            printer.SendCmd(a, b, "p-" + Shared.formatDoubleToPrintDiscount(globalDiscount));
                            if ( b.getValue() != 0 ){
                                lastReceipt = Integer.parseInt(ConnectionDrivers.getLastReceipt())+1+"";
                                everythingCool = false;
                            }
                        }
                        for (PayForm pf : pfs) {
                            //TODO Put it in the configuracion database.
                            /**
                             * Put it in the configuracion file.
                             */
                            String cmd = "01";
                            if ( pf.getFormWay().equals("Efectivo") ){
                                cmd = "01";
                            }else if ( pf.getFormWay().equals("Nota de Credito") ){
                                cmd = "02";
                            }else if ( pf.getFormWay().equals("Credito") ){
                                cmd = "09";
                            }else if ( pf.getFormWay().equals("Debito") ){
                                cmd = "10";
                            }
                            printer.SendCmd(a, b, "2" + cmd + Shared.formatDoubleToSpecifyMoneyInPrinter(pf.getQuant()));
                            if ( b.getValue() != 0 ){
                                lastReceipt = Integer.parseInt(ConnectionDrivers.getLastReceipt())+1+"";
                                everythingCool = false;
                            }
                        }
                    }
                }catch(Exception ex){
                    lastReceipt = Integer.parseInt(ConnectionDrivers.getLastReceipt())+1+"";
                    everythingCool = false;
                }

            }
            if ( everythingCool ){
                printer.UploadStatusCmd(a, b, "S1", Constants.tmpFileName);
                if ( b.getValue() != 0 ){
                    lastReceipt = Integer.parseInt(ConnectionDrivers.getLastReceipt())+1+"";
                    everythingCool = false;
                }
                if ( everythingCool ){
                    File file = new File(Constants.tmpFileName);

                    Scanner sc = new Scanner(file);

                    boolean ansT = sc.hasNext();
                    assert(ansT);
                    String s = sc.next().substring(21, 29);

                    lastReceipt = s;
                    sc.close();
                    file.delete();
                }
            }
            printer.CloseFpctrl();
            isOk = true;
        } else{
            throw new Exception("Driver de impresora desconocido!");
        }
    }

    public void extractMoney(User u, String boss, Double quant) throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return;
        }

        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            // TODO CODE HERE =D!

            isOk = false;
            int ans = printer.OpenPort(Byte.parseByte(Shared.getFileConfig("printerPort")), (byte)2);

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }
            
            Calendar calendar = GregorianCalendar.getInstance();
            Date dd = Constants.sdf4ncr.parse(ConnectionDrivers.getDate4NCR());
            calendar.setTime(dd);

            ans = printer.NewDoc(3, "SAUL".getBytes(), "123".getBytes(),
                    "Cajero".getBytes(), "".getBytes(), new NativeLong(0), (calendar.get(Calendar.DAY_OF_MONTH)),
                    (calendar.get(Calendar.MONTH)+1), 21, calendar.get(Calendar.HOUR+1)%12,
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                    ((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1) ,(calendar.get(Calendar.DAY_OF_MONTH)),
                    (calendar.get(Calendar.MONTH)+1), 21, calendar.get(Calendar.HOUR+1)%12,
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                    ((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1));

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }

            ans = printer.PrintTextNoFiscal(Constants.normalFont,"",1,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"         Extraer dinero de la Caja " + Shared.getFileConfig("myId"),2,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.bigFont,"          " + Constants.df.format(quant) + " Bs",1,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"",3,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"               Firma Cajera",4,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"",5,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"",6,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"            _____________________",7,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"",8,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"               Firma Encargado",9,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"",10,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"",11,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"            _____________________",12,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"",13,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"",14,(byte)0);

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }

            ans = printer.CloseDoc(.0, .0, .0, .0, .0, .0, .0, .0, (byte)0, (byte)0, "");

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }

            printer.ClosePort();
            isOk = true;
        }else if ( Shared.getFileConfig("printerDriver").equals("tfhkaif") ){

            isOk = false;
            IntByReference a = new IntByReference();
            IntByReference b = new IntByReference();
            printer.OpenFpctrl(Shared.getFileConfig("printerPort"));

            // TODO Bug in the printer?? Sometimes It prints 10K instead of Shared.formatDoubleToPrint(quant);
            /*printer.SendCmd(a, b, "9001" + Shared.formatDoubleToPrint(quant) );
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }*/

            List<String> buffer = new ArrayList<String>();
            buffer.add("800 Retiro de Efectivo  " +  Constants.df.format(quant));
            buffer.add("800 Caja " + Shared.getFileConfig("myId"));
            buffer.add("800 ");
            buffer.add("800                 _______________________");
            buffer.add("800                   Firma del encargado");
            buffer.add("800 ");
            buffer.add("800                 _______________________");
            buffer.add("810                     Firma del cajero");

             for (String bu : buffer) {
                printer.SendCmd(a, b, bu);
                if ( b.getValue() != 0 ){
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
                }
            }

            printer.CloseFpctrl();
            isOk = true;

        }else{
            throw new Exception("Driver de impresora desconocido!");
        }
    }

    public void reportExtraction() throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return;
        }

        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            // TODO CODE HERE =D!
        }else if ( Shared.getFileConfig("printerDriver").equals("tfhkaif") ){
            isOk = false;
            IntByReference a = new IntByReference();
            IntByReference b = new IntByReference();
            printer.OpenFpctrl(Shared.getFileConfig("printerPort"));

            printer.SendCmd(a, b, "t");
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }

            printer.CloseFpctrl();
            isOk = true;
        }else{
            throw new Exception("Driver de impresora desconocido!");
        }
    }

    public String getZ() throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return "";
        }

        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            // TODO CODE HERE =D!
            return "";
        }else if ( Shared.getFileConfig("printerDriver").equals("tfhkaif") ){
            if ( z == null  ){
                isOk = false;
                IntByReference a = new IntByReference();
                IntByReference b = new IntByReference();
                printer.OpenFpctrl(Shared.getFileConfig("printerPort"));

                printer.UploadReportCmd(a, b, "U0X", Constants.tmpFileName);
                if ( b.getValue() != 0 ){
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
                }

                File file = new File(Constants.tmpFileName);

                Scanner sc = new Scanner(file);

                sc.hasNext();
                String s = sc.next().substring(0, 4);


                sc.close();
                file.delete();
                printer.CloseFpctrl();
                isOk = true;
                z = s;
            }
            return z;
        }else{
            throw new Exception("Driver de impresora desconocido!");
        }
    }

    public String getSerial() throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return "";
        }

        if ( printerSerial == null ){
            calculateSerial();
        }
        return printerSerial;
    }

    public String getLastFiscalNumber(){
        if ( !Constants.withFiscalPrinter ){
            return "";
        }

        return lastReceipt;
    }

    public void printCreditNote(List<Item2Receipt> items, String ticketId, String myId, User u , Client client, String alternativeId) throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return;
        }
        boolean everythingCool = true;

        isOk = false;
        IntByReference a = new IntByReference();
        IntByReference b = new IntByReference();
        printer.OpenFpctrl(Shared.getFileConfig("printerPort"));

        List<String> buffer = new ArrayList<String>();

        if ( !items.isEmpty() ){
            int line = 1;

            if ( client != null && !client.getId().isEmpty() && !client.getId().equals("Contado") ){
                buffer.add("i0" + ( line++ ) + "RIF: " + client.getId());
                if ( !client.getName().trim().isEmpty() ) {
                    buffer.add("i0" + (line++) + "Nombre: " + client.getName());
                }
                if ( !client.getPhone().trim().isEmpty() ) {
                    buffer.add("i0" + (line++) + "Telefono: " + client.getPhone());
                }
                if ( !client.getAddress().trim().isEmpty() ) {
                    buffer.add("i0" + (line++) + "Direccion: " + client.getAddress());
                }
            }else{
                buffer.add("i0" + (line++) + "Cliente: Contado");
            }
            buffer.add("i0" + ( line++ ) + "Correlativo: " + myId);
            buffer.add("i0" + ( line++ ) + "Factura: " + ticketId);
            if ( !alternativeId.isEmpty() ){
                buffer.add("i0" + ( line++ ) + "Factura ID Temporal: " + alternativeId);
            }
            buffer.add("i0" + ( line++ ) + "Caja: " + Shared.getFileConfig("myId"));

            for (String bu : buffer) {
                printer.SendCmd(a, b, bu);
                if ( b.getValue() != 0 ){
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
                }
            }
            
            for (Item2Receipt item2r : items) {
                Item item = item2r.getItem();
                printer.SendCmd(a, b, "d1" + ( Shared.formatDoubleToPrint( item2r.getSellPrice() ) ) +
                        Shared.formatQuantToPrint(item2r.getQuant()+.0) + item.getDescription().substring(0, Math.min(item.getDescription().length(), 37)));
                if ( b.getValue() != 0 ){
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
                }
                if ( item2r.getSellDiscount() != .0 ){
                    Double finalDiscount = ConnectionDrivers.getDiscount( item.getCode(),ticketId );
                    if ( finalDiscount > .0 ){
                        printer.SendCmd(a, b, "p-"+Shared.formatDoubleToPrintDiscount(finalDiscount/100.0));
                        if ( b.getValue() != 0 ){
                            throw new Exception(Shared.getErrMapping().get(b.getValue()));
                        }
                    }
                }
            }
            try{
                printer.SendCmd(a, b, "3");
                if ( b.getValue() != 0 ){
                    // Fiscal Number has been generated.
                    //System.out.println("RECUPERANDO NUMERO FISCAL!!");
                    lastReceipt = Integer.parseInt(ConnectionDrivers.getLastCN())+1+"";
                    everythingCool = false;
                }

                if ( everythingCool ){
                    printer.SendCmd(a, b, "y" + ticketId);// + Shared.formatDoubleToPrintDiscount(globalDiscount));
                    if ( b.getValue() != 0 ){
                        lastReceipt = Integer.parseInt(ConnectionDrivers.getLastCN())+1+"";
                        everythingCool = false;
                    }
                    printer.SendCmd(a, b, "f11000000000000");
                }
            }catch(Exception ex){
                lastReceipt = Integer.parseInt(ConnectionDrivers.getLastCN())+1+"";
                everythingCool = false;
            }
        }

        try{
            if ( everythingCool ){
                printer.UploadReportCmd(a, b, "U0X", Constants.tmpFileName);
                if ( b.getValue() != 0 ){
                    lastReceipt = Integer.parseInt(ConnectionDrivers.getLastCN())+1+"";
                    everythingCool = false;
                }

                if ( everythingCool ){
                    File file = new File(Constants.tmpFileName);

                    Scanner sc = new Scanner(file);

                    boolean ansT = sc.hasNext();
                    assert(ansT);
                    String s = sc.next().substring(168);

                    lastReceipt = s;
                    sc.close();
                    file.delete();
                }
            }
        }catch(Exception ex){
            lastReceipt = Integer.parseInt(ConnectionDrivers.getLastCN())+1+"";
            everythingCool = false;
        }
        printer.CloseFpctrl();
        isOk = true;
    }

    void report(String r) throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return;
        }

        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){

            isOk = false;
            int ans = printer.OpenPort(Byte.parseByte(Shared.getFileConfig("printerPort")), (byte)2);

            Calendar calendar = GregorianCalendar.getInstance();
            Date dd = Constants.sdf4ncr.parse(ConnectionDrivers.getDate4NCR());
            calendar.setTime(dd);

            System.out.println(calendar.get(Calendar.DAY_OF_MONTH) + " " + (calendar.get(Calendar.MONTH)+1) + " " + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR) + " " + calendar.get(Calendar.MINUTE) + " " + calendar.get(Calendar.SECOND) + " " + ((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1));
            System.out.println((byte)calendar.get(Calendar.DAY_OF_MONTH) + " " + (byte)(calendar.get(Calendar.MONTH)+1) + " " + (byte)calendar.get(Calendar.YEAR) + " " + (byte)calendar.get(Calendar.HOUR) + " " + (byte)calendar.get(Calendar.MINUTE) + " " + (byte)calendar.get(Calendar.SECOND) + " " + (byte)((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1));

            if ( r.equals("X") ){
                ans = printer.RptX((byte)(calendar.get(Calendar.DAY_OF_MONTH)), (byte)(calendar.get(Calendar.MONTH)+1), (byte)21, (byte)(calendar.get(Calendar.HOUR+1)%12), (byte)calendar.get(Calendar.MINUTE), (byte)calendar.get(Calendar.SECOND), (byte)((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1), "Caja" + Shared.getFileConfig("myId"));
            }else{
                ans = printer.GenRptZ((byte)calendar.get(Calendar.DAY_OF_MONTH), (byte)(calendar.get(Calendar.MONTH)+1), (byte)21, (byte)(calendar.get(Calendar.HOUR+1)%12), (byte)calendar.get(Calendar.MINUTE), (byte)calendar.get(Calendar.SECOND), (byte)((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1));
            }

            printer.ClosePort();

            if ( ans != 0 ){
                System.out.println("ans = " + ans);
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }
            isOk = true;

            return;
        }else if ( Shared.getFileConfig("printerDriver").equals("tfhkaif") ){
            isOk = false;
            IntByReference a = new IntByReference();
            IntByReference b = new IntByReference();
            printer.OpenFpctrl(Shared.getFileConfig("printerPort"));
            printer.SendCmd(a, b, "I0"+r);
            printer.CloseFpctrl();
            isOk = true;
        }else{
            throw new Exception("Driver de impresora desconocido!");
        }
        
    }

    public void forceClose(){
        printer.CloseFpctrl();
    }

    void updateValues() throws Exception {
        if ( Constants.withFiscalPrinter ){
            return;
        }

        printer.OpenFpctrl(Shared.getFileConfig("printerPort"));
        IntByReference a = new IntByReference();
        IntByReference b = new IntByReference();
        printer.UploadStatusCmd(a, b, "S4", Constants.tmpFileName);
        if ( b.getValue() != 0 ){
            throw new Exception(Shared.getErrMapping().get(b.getValue()));
        }

        File file = new File(Constants.tmpFileName);

        Scanner sc = new Scanner(file);

        boolean ansT = sc.hasNext();
        assert(ansT);

        String line = sc.next();
        //System.out.println("Linea = " + line.substring(2, 2+10));
        Double cash = Double.parseDouble(line.substring(2+10*0, 2+10*1))/100.0;
        Double cn = Double.parseDouble(line.substring(2+10*1, 2+10*2))/100.0;
        Double credit = Double.parseDouble(line.substring(2+10*8, 2+10*9))/100.0;
        Double debit = Double.parseDouble(line.substring(2+10*9, 2+10*10))/100.0;
        ConnectionDrivers.updateFiscalNumbers(cash, cn, debit, credit);

        sc.close();
        file.delete();
        printer.UploadStatusCmd(a, b, "S1", Constants.tmpFileName);
        if ( b.getValue() != 0 ){
            throw new Exception(Shared.getErrMapping().get(b.getValue()));
        }
        file = new File(Constants.tmpFileName);
        sc = new Scanner(file);
        line = sc.next();
        //Double total = Double.parseDouble(line.substring(2+10*1,2+10*2))/100.0;
        //String lReceipt = line.substring(2+10*2, 2+10*2+8);
        //TODO IS IT A BUG?????????????
        String lReceipt = line.substring(2+10*2-1, 2+10*2+8-1);
        int quantReceiptsToday = Integer.parseInt(line.substring(2+10*2+8 , 2+10*2+8+4));

        sc.close();
        file.delete();

        String pZ = Constants.df2z.format(Integer.parseInt(z) - 1);
        printer.UploadReportCmd(a, b, "U0X", Constants.tmpFileName);
        if ( b.getValue() != 0 ){
            throw new Exception(Shared.getErrMapping().get(b.getValue()));
        }
        
        file = new File(Constants.tmpFileName);
        sc = new Scanner(file);

        line = sc.next();

        String lastCN = line.substring(168);
        //System.out.println("line = " + line );
        Double total = Double.parseDouble(line.substring(39,39+9))/100.0 - Double.parseDouble(line.substring(109, 109+9))/100.0;
        
        sc.close();
        file.delete();

        printer.UploadReportCmd(a, b, "U3A00" + pZ + "00" + z, Constants.tmpFileName);
        if ( b.getValue() != 0 ){
            throw new Exception(Shared.getErrMapping().get(b.getValue()));
        }

        file = new File(Constants.tmpFileName);
        sc = new Scanner(file);

        line = sc.next();

        String pLastCN = line.substring(168);
        int nNC = (Integer.parseInt(lastCN)-Integer.parseInt(pLastCN));
        ConnectionDrivers.updateTotalFromPrinter(total, z ,lReceipt,quantReceiptsToday,lastCN,nNC);

        printer.CloseFpctrl();
    }

    // Pre: updateValues()
    void printResumeZ() throws Exception{
        if ( Constants.withFiscalPrinter ){
            return;
        }

        isOk = false;
        IntByReference a = new IntByReference();
        IntByReference b = new IntByReference();
        printer.OpenFpctrl(Shared.getFileConfig("printerPort"));

        List<String> buffer = new ArrayList<String>();
        buffer.add("800 ");
        buffer.add("800 Resumen del Reporte Z Nro " + z);
        buffer.add("800 Impresora Fiscal Serial Nro " + printerSerial );
        buffer.add("800 ");
        buffer.add("800 Sucursal: " + Shared.getConfig("storeName"));
        buffer.add("800 Caja Nro: " + Shared.getFileConfig("myId"));
        buffer.add("800 Ult Factura:        " + ConnectionDrivers.getLastReceipt());
        buffer.add("800 Ult Nota de Credito " + ConnectionDrivers.getLastCN());
        buffer.add("800 Nro de Ventas:      " + ConnectionDrivers.getQuant(Shared.getFileConfig("myId"),"num_facturas"));
        buffer.add("800 Nro de N/C:         " + ConnectionDrivers.getQuant( Shared.getFileConfig("myId"),"numero_notas_credito"));
        buffer.add("800 ");
        buffer.add("800 Neto Ventas:         " + Constants.df.format(ConnectionDrivers.getTotalDeclaredPos(Shared.getFileConfig("myId"))));
        buffer.add("800 ");
        buffer.add("810 Fin de Resumen del Reporte Z Nro " + z);

         for (String bu : buffer) {
            printer.SendCmd(a, b, bu);
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }
        }

        printer.CloseFpctrl();
        isOk = true;
    }
    
}
