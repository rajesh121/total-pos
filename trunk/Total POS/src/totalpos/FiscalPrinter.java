package totalpos;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

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
    public boolean isReceipt = false;

    public FiscalPrinter() {
        if ( Constants.withFiscalPrinter ){
            printer = (FiscalDriver) Native.loadLibrary(Shared.getFileConfig("printerDriver"), FiscalDriver.class);
        }
    }

    public boolean checkPrinter() throws SQLException, FileNotFoundException, Exception {
        if ( !Constants.withFiscalPrinter ){
            return true;
        }
        return isTheSame(ConnectionDrivers.getMyPrinter());
    }

    private void calculateSerial() throws Exception{

        if ( !Constants.withFiscalPrinter ){
            return;
        }

        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            // TODO CODE HERE =D!

            printer.ClosePort();

            int ans = printer.OpenPort(Byte.parseByte(Shared.getFileConfig("printerPort")), (byte)2);
            printer.CancelTransaction();

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }

            TQueryPrnStatus tqps = new TQueryPrnStatus();
            ans = printer.QueryPrnStatus(tqps);

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }
            
            printerSerial = Shared.b2s(tqps.PrnID);
            z = tqps.UltZ + "";

            TQueryPrnTransaction tqpt = new TQueryPrnTransaction();
            ans = printer.QueryPrnTransaction((byte)1, tqpt);

            if ( isReceipt ){
                lastReceipt = tqpt.VoucherVta + "";
            }else{
                lastReceipt = tqpt.VoucherDev + "";
            }
            System.out.println("LastReceipt = " + lastReceipt);

            ans = printer.ClosePort();

            if ( ans != 0 ){
                System.out.println(Shared.ncrErrMapping.get(ans));
            }
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

    // TODO Check difference between predicted and everythingCool
    public void printTicket(List<Item2Receipt> items , Client client, Double globalDiscount, String ticketId, User u , List<PayForm> pfs) throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return;
        }
        boolean everythingCool = true;
        boolean predicted = false;
        
        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            isOk = false;

            printer.ClosePort();
            int ans = printer.OpenPort(Byte.parseByte(Shared.getFileConfig("printerPort")), (byte)2);
            printer.OpenBox();
            printer.CancelTransaction();

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }
            
            Calendar calendar = GregorianCalendar.getInstance();
            Date dd = Constants.sdf4ncr.parse(ConnectionDrivers.getDate4NCR());
            calendar.setTime(dd);

            if ( client == null || client.getId() == null || client.getId().isEmpty() ){
                client = new Client("Contado", "Contado", "", "");
            }

            int hour = calendar.get(Calendar.HOUR) == 0?12:calendar.get(Calendar.HOUR);

            System.out.println("Anyo = " +  calendar.get(Calendar.YEAR)%100);
            System.out.println("Fecha enviada: " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/"
                    + (byte)(calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset)+ " " + hour + ":" + calendar.get(Calendar.MINUTE)
                    + ":" + calendar.get(Calendar.SECOND) + " " + ((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1));
            ans = printer.NewDoc(Constants.receipt, client.getName(), client.getId(),
                    Shared.getUser().getLogin() + " " + ticketId, "ABC", new NativeLong(0), (calendar.get(Calendar.DAY_OF_MONTH)),
                    //(calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, (calendar.get(Calendar.HOUR)+12)%13,
                    (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, hour,
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                    ((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1) ,(calendar.get(Calendar.DAY_OF_MONTH)),
                    (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, hour,
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                    ((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1));

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }
            Double subtotal = .0;
            int itemsQuant = 0;
            for (Item2Receipt item2r : items) {
                String d = item2r.getItem().getDescription();
                subtotal += item2r.getSellPrice()*(1.0 - item2r.getSellDiscount()/100.0);
                ans = printer.NewItem((byte)0, (byte)0, item2r.getQuant()+.0, Shared.round(item2r.getSellPrice(),2) , (item2r.getItem().getModel()+"-"+d).substring(0, Math.min(Constants.maxNcrDescription, d.length())));
                if ( ans != 0 ){
                    throw new Exception(Shared.ncrErrMapping.get(ans));
                }
                if ( Math.abs(item2r.getItem().getDescuento()) > Constants.exilon ){
                    ans = printer.OprDoc((byte)0, (byte)0, Shared.round((item2r.getQuant()+.0)*item2r.getSellPrice()*(item2r.getSellDiscount()/100.0),2), ((item2r.getSellDiscount())+"%").replace(',', '.'));
                }
                if ( ans != 0 ){
                    throw new Exception(Shared.ncrErrMapping.get(ans));
                }
                itemsQuant += item2r.getQuant();
            }

            TreeMap<String,Double> buff = new TreeMap<String , Double>();
            buff.put("Efectivo", .0);
            buff.put("Nota de Credito", .0);
            buff.put("Credito", .0);
            buff.put("Debito", .0);
            for (PayForm payForm : pfs) {
                buff.put( payForm.getFormWay() , buff.get(payForm.getFormWay()) + payForm.getQuant());
            }
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"                                   ",1,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"===================================",2,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"      CANTIDAD DE ARTICULOS " + itemsQuant,3,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont,"===================================",4,(byte)0);

            System.out.println("Código de barras 69");
            ans = printer.CloseDoc(buff.get("Efectivo"), buff.get("Nota de Credito"), buff.get("Credito"), buff.get("Debito"),
                    .0, .0, subtotal*globalDiscount, .0, (byte)12 , (byte)70, ticketId);

            System.out.println("ans Close Doc = " + ans);

            if ( ans != 0 && ans != 309){
                printer.CancelTransaction();
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }

            TQueryPrnTransaction tqpt = new TQueryPrnTransaction();
            ans = printer.QueryPrnTransaction((byte)1, tqpt);

            predicted = false;
            if ( ans != 0 || tqpt.VoucherVta < 0 || tqpt.VoucherVta > Constants.maximumFiscalNumber ){
                lastReceipt = Integer.parseInt(ConnectionDrivers.getLastReceipt())+1+"";
                predicted = true;
            }else{
                lastReceipt = tqpt.VoucherVta + "";
            }

            ans = printer.ClosePort();

            if ( predicted ){
                MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "La factura fue guardada satisfactoriamente!!");
                msb.show(null);
            }
            isOk = true;
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
                    // TODO OJO!
                    // " " EXENTO!
                    // "!" Tasa de Iva 1
                    printer.SendCmd(a, b, Shared.getConfig("tfhkaifKindTaxReceipt") + ( Shared.formatDoubleToPrint(item.getLastPrice().getQuant()) ) +
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
                        predicted = true;
                    }

                    if ( everythingCool ){
                        printer.SendCmd(a, b, "y" + ticketId);// + Shared.formatDoubleToPrintDiscount(globalDiscount));
                        if ( b.getValue() != 0 ){
                            lastReceipt = Integer.parseInt(ConnectionDrivers.getLastReceipt())+1+"";
                            everythingCool = false;
                            predicted = true;
                        }

                        if ( globalDiscount != null && globalDiscount > 0 ){
                            printer.SendCmd(a, b, "p-" + Shared.formatDoubleToPrintDiscount(globalDiscount));
                            if ( b.getValue() != 0 ){
                                lastReceipt = Integer.parseInt(ConnectionDrivers.getLastReceipt())+1+"";
                                everythingCool = false;
                                predicted = true;
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
                                predicted = true;
                            }
                        }
                    }
                }catch(Exception ex){
                    lastReceipt = Integer.parseInt(ConnectionDrivers.getLastReceipt())+1+"";
                    everythingCool = false;
                    predicted = true;
                }

            }
            if ( everythingCool ){
                printer.UploadStatusCmd(a, b, "S1", Constants.tmpFileName);
                if ( b.getValue() != 0 ){
                    lastReceipt = Integer.parseInt(ConnectionDrivers.getLastReceipt())+1+"";
                    everythingCool = false;
                    predicted = true;
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

            if ( predicted ){
                MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "La factura fue guardada satisfactoriamente!!");
                msb.show(null);
            }
        } else{
            throw new Exception("Driver de impresora desconocido!");
        }
    }

    public void extractMoney(User u, String boss, Double quant) throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return;
        }

        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            isOk = false;
            printer.ClosePort();
            int ans = printer.OpenPort(Byte.parseByte(Shared.getFileConfig("printerPort")), (byte)2);
            printer.CancelTransaction();

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }
            
            Calendar calendar = GregorianCalendar.getInstance();
            Date dd = Constants.sdf4ncr.parse(ConnectionDrivers.getDate4NCR());
            calendar.setTime(dd);

            int hour = calendar.get(Calendar.HOUR) == 0?12:calendar.get(Calendar.HOUR);

            ans = printer.NewDoc(Constants.nonfiscalDoc, "SAUL", "123",
                    Shared.getUser().getLogin(), "", new NativeLong(0), (calendar.get(Calendar.DAY_OF_MONTH)),
                    //(calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, (calendar.get(Calendar.HOUR)+12)%13,
                    (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, hour,
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                    ((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1) ,(calendar.get(Calendar.DAY_OF_MONTH)),
                    (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, hour,
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

            ans = printer.OpenBox();

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
            printer.OpenBox();

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
            printer.OpenBox();

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

        if ( z == null  ){
            if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
                printer.ClosePort();
                int ans = printer.OpenPort(Byte.parseByte(Shared.getFileConfig("printerPort")), (byte)2);
                printer.CancelTransaction();

                if ( ans != 0 ){
                    throw new Exception(Shared.ncrErrMapping.get(ans));
                }

                TQueryPrnStatus tqps = new TQueryPrnStatus();
                ans = printer.QueryPrnStatus(tqps);

                if ( ans != 0 ){
                    throw new Exception(Shared.ncrErrMapping.get(ans));
                }

                z = tqps.UltZ + "";
                
                printer.ClosePort();

            }else if ( Shared.getFileConfig("printerDriver").equals("tfhkaif") ){

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
            }else{
                throw new Exception("Driver de impresora desconocido!");
            }
        }
        return z;
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

    // TODO Check difference between predicted and everythingCool
    public void printCreditNote(List<Item2Receipt> items, String ticketId, String myId, User u , Client client, String alternativeId, String fiscalTicketId, String receiptPrinter , Timestamp printingHour) throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return;
        }

        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            isOk = false;

            System.out.println(printingHour);
            Date printingHourD = new Date(printingHour.getTime());
            printer.ClosePort();
            int ans = printer.OpenPort(Byte.parseByte(Shared.getFileConfig("printerPort")), (byte)2);
            printer.CancelTransaction();

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }
            ans = printer.OpenBox();

            Calendar calendar = GregorianCalendar.getInstance();
            Date dd = Constants.sdf4ncr.parse(ConnectionDrivers.getDate4NCR());
            calendar.setTime(dd);

            Calendar calendarCN = new GregorianCalendar();
            calendarCN.setTime(printingHourD);

            int hour = calendar.get(Calendar.HOUR) == 0?12:calendar.get(Calendar.HOUR);
            int hourCN = calendarCN.get(Calendar.HOUR) == 0?12:calendarCN.get(Calendar.HOUR);

            if ( client == null || client.getId() == null || client.getId().isEmpty() || client.getId().equals("Contado") ){
                client = new Client("Contado", "Contado", "", "");
            }

            System.out.println("Hora = " + hour + " _ " + hour);
            ans = printer.NewDoc(Constants.creditNote, client.getName(), client.getId(),
                    Shared.getUser().getLogin(), receiptPrinter, new NativeLong(Long.parseLong(fiscalTicketId)), (calendar.get(Calendar.DAY_OF_MONTH)),
                    //(calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, (calendar.get(Calendar.HOUR)+12)%13,
                    (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, hour,
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                    ((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1) ,(calendarCN.get(Calendar.DAY_OF_MONTH)),
                    (calendarCN.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, hourCN,
                    calendarCN.get(Calendar.MINUTE), calendarCN.get(Calendar.SECOND),
                    ((calendarCN.get(Calendar.AM_PM) == Calendar.AM)?0:1));

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }
            
            Double subtotal = .0;
            for (Item2Receipt item2r : items) {
                String d = item2r.getItem().getDescription();
                subtotal += item2r.getSellPrice()*(1.0 - item2r.getSellDiscount()/100.0);
                ans = printer.NewItem((byte)0, (byte)0, item2r.getQuant()+.0, Shared.round(item2r.getSellPrice(),2) , (item2r.getItem().getModel()+"-"+d).substring(0, Math.min(Constants.maxNcrDescription, d.length())));
                if ( ans != 0 ){
                    throw new Exception(Shared.ncrErrMapping.get(ans));
                }
                if ( Math.abs(item2r.getSellDiscount()) > Constants.exilon ){
                    ans = printer.OprDoc((byte)0, (byte)0, Shared.round((item2r.getQuant()+.0)*item2r.getSellPrice()*(item2r.getSellDiscount()/100.0),2), ((item2r.getSellDiscount())+"%").replace(',', '.'));
                }

                if ( ans != 0 ){
                    throw new Exception(Shared.ncrErrMapping.get(ans));
                }
            }

            TreeMap<String,Double> buff = new TreeMap<String , Double>();
            buff.put("Efectivo", .0);
            buff.put("Nota de Credito", .0);
            buff.put("Credito", .0);
            buff.put("Debito", .0);

            ans = printer.CloseDoc(.0, new Price(null, subtotal).plusIva().getQuant(), .0, .0,
                    .0, .0, .0, .0, (byte)12 , (byte)70, ticketId);

            if ( ans != 0 && ans != 309 ){
                printer.CancelTransaction();
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }

            TQueryPrnTransaction tqpt = new TQueryPrnTransaction();
            ans = printer.QueryPrnTransaction((byte)1, tqpt);

            System.out.println("Ans Query Prn = " + ans);
            boolean predicted = false;
            if ( ans != 0 || tqpt.VoucherDev < 0 || tqpt.VoucherDev > Constants.maximumFiscalNumber ){
                lastReceipt = Integer.parseInt(ConnectionDrivers.getLastCN())+1+"";
                predicted = true;
            }else{
                lastReceipt = tqpt.VoucherDev + "";
            }

            if ( predicted ){
                MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "La factura fue guardada satisfactoriamente!!");
                msb.show(null);
            }

            ans = printer.ClosePort();

            isOk = true;
        }else if ( Shared.getFileConfig("printerDriver").equals("tfhkaif") ){

            boolean everythingCool = true;
            boolean predicted = false;

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
                    // TODO OJO!!!!!!!!!!!!!!!!!!!
                    // "d0 -> exento.
                    // "d1 -> Tasa de iva 1
                    printer.SendCmd(a, b, "d" + Shared.getConfig("tfhkaifKindTaxCN") + ( Shared.formatDoubleToPrint( item2r.getSellPrice() ) ) +
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
                        predicted = true;
                    }

                    if ( everythingCool ){
                        printer.SendCmd(a, b, "y" + ticketId);// + Shared.formatDoubleToPrintDiscount(globalDiscount));
                        if ( b.getValue() != 0 ){
                            lastReceipt = Integer.parseInt(ConnectionDrivers.getLastCN())+1+"";
                            everythingCool = false;
                            predicted = true;
                        }
                        printer.SendCmd(a, b, "f11000000000000");
                    }
                }catch(Exception ex){
                    lastReceipt = Integer.parseInt(ConnectionDrivers.getLastCN())+1+"";
                    everythingCool = false;
                    predicted = true;
                }
            }

            try{
                if ( everythingCool ){
                    printer.UploadReportCmd(a, b, "U0X", Constants.tmpFileName);
                    if ( b.getValue() != 0 ){
                        lastReceipt = Integer.parseInt(ConnectionDrivers.getLastCN())+1+"";
                        everythingCool = false;
                        predicted = true;
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
                predicted = true;
            }
            printer.CloseFpctrl();
            isOk = true;

            if ( predicted ){
                MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "La nota de crédito fue guardada satisfactoriamente!!");
                msb.show(null);
            }
        }
    }

    void report(String r) throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return;
        }

        //System.out.println("Imprimir reporte " + r);
        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){

            isOk = false;
            printer.ClosePort();
            int ans = printer.OpenPort(Byte.parseByte(Shared.getFileConfig("printerPort")), (byte)2);
            printer.CancelTransaction();

            Calendar calendar = GregorianCalendar.getInstance();
            Date dd = Constants.sdf4ncr.parse(ConnectionDrivers.getDate4NCR());
            calendar.setTime(dd);
            int hour = calendar.get(Calendar.HOUR) == 0?12:calendar.get(Calendar.HOUR);

            System.out.println(calendar.get(Calendar.DAY_OF_MONTH) + " " + (calendar.get(Calendar.MONTH)+1) + " " + calendar.get(Calendar.YEAR)%100 + " " + hour + " " + calendar.get(Calendar.MINUTE) + " " + calendar.get(Calendar.SECOND) + " " + ((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1));
            System.out.println((byte)calendar.get(Calendar.DAY_OF_MONTH) + " " + (byte)(calendar.get(Calendar.MONTH)+1) + " " + (byte)calendar.get(Calendar.YEAR)%100 + " " + (byte)hour + " " + (byte)calendar.get(Calendar.MINUTE) + " " + (byte)calendar.get(Calendar.SECOND) + " " + (byte)((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1));

            if ( r.equals("X") ){
                //ans = printer.RptX((byte)(calendar.get(Calendar.DAY_OF_MONTH)), (byte)(calendar.get(Calendar.MONTH)+1), (byte)(calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset), (byte)((calendar.get(Calendar.HOUR)+12)%13), (byte)calendar.get(Calendar.MINUTE), (byte)calendar.get(Calendar.SECOND), (byte)((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1), "Caja" + Shared.getFileConfig("myId"));
                ans = printer.RptX((byte)(calendar.get(Calendar.DAY_OF_MONTH)), (byte)(calendar.get(Calendar.MONTH)+1), (byte)(calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset), (byte)(hour), (byte)calendar.get(Calendar.MINUTE), (byte)calendar.get(Calendar.SECOND), (byte)((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1), "Caja" + Shared.getFileConfig("myId"));
            }else{
                //ans = printer.GenRptZ((byte)calendar.get(Calendar.DAY_OF_MONTH), (byte)(calendar.get(Calendar.MONTH)+1), (byte)(calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset), (byte)((calendar.get(Calendar.HOUR)+12)%13), (byte)calendar.get(Calendar.MINUTE), (byte)calendar.get(Calendar.SECOND), (byte)((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1));
                ans = printer.GenRptZ((byte)calendar.get(Calendar.DAY_OF_MONTH), (byte)(calendar.get(Calendar.MONTH)+1), (byte)(calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset), (byte)(hour), (byte)calendar.get(Calendar.MINUTE), (byte)calendar.get(Calendar.SECOND), (byte)((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1));
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

    void updateValues(String day) throws Exception {
        if ( !Constants.withFiscalPrinter ){
            return;
        }

        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            isOk = false;
            printer.ClosePort();
            int ans = printer.OpenPort(Byte.parseByte(Shared.getFileConfig("printerPort")), (byte)2);
            printer.CancelTransaction();

            if ( ans != 0 ){
                System.out.println(Shared.ncrErrMapping.get(ans));
            }

            TQueryPrnStatus tqps = new TQueryPrnStatus();
            ans = printer.QueryPrnStatus(tqps);

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }

            z = tqps.UltZ + "";
            System.out.println("El Reporte Z es " + z);

            TQueryPrnTransaction tqpr = new TQueryPrnTransaction();
            ans = printer.QueryPrnTransaction((byte)1, tqpr);

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }

            System.out.println(" Pago 1 = " + Double.parseDouble(Shared.b2s(tqpr.FPago1))/100.0);

            ConnectionDrivers.updateFiscalNumbers(Double.parseDouble(Shared.b2s(tqpr.FPago1))/100.0,
                    Double.parseDouble(Shared.b2s(tqpr.FPago2))/100.0,
                    Double.parseDouble(Shared.b2s(tqpr.FPago4))/100.0,
                    Double.parseDouble(Shared.b2s(tqpr.FPago3))/100.0);

            System.out.println(" Venta A = " + Double.parseDouble(Shared.b2s(tqpr.VtaA))/100.0);
            System.out.println(" Dev A = " + Double.parseDouble(Shared.b2s(tqpr.DevA))/100.0);

            Double total = Double.parseDouble(Shared.b2s(tqpr.VtaA))/100.0 - Double.parseDouble(Shared.b2s(tqpr.DevA))/100.0;

            TQueryPrnMemory tqpm = new TQueryPrnMemory();
            ans = printer.QueryPrnMemory(tqps.UltZ, tqpm);
            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }

            System.out.println("Anterior ultima venta = " + Shared.b2s(tqpm.CounterLastVta) + " " + Shared.b2s(tqpm.CounterLastDev) );

            System.out.println("Todos los valores = ");
            System.out.println(" tqpm.CounterDev ="+ Shared.b2s(tqpm.CounterDev));
            System.out.println(" tqpm.CounterLastDev ="+ Shared.b2s(tqpm.CounterLastDev));
            System.out.println(" tqpm.CounterLastMemRptZ ="+ Shared.b2s(tqpm.CounterLastMemRptZ));
            System.out.println(" tqpm.CounterLastVta ="+ Shared.b2s(tqpm.CounterLastVta));
            System.out.println(" tqpm.DateTimeLastDev ="+ Shared.b2s(tqpm.DateTimeLastDev));
            /*System.out.println(" tqpm.CounterDev ="+ Shared.b2s(tqpm.));
            System.out.println(" tqpm.CounterDev ="+ Shared.b2s(tqpm.CounterDev));*/

            int myCounterLastVta = tqpr.VoucherVta;
            int myCounterLastDev = tqpr.VoucherDev;

            try{
                myCounterLastVta = Integer.parseInt(Shared.b2s(tqpm.CounterLastVta));
                myCounterLastDev = Integer.parseInt(Shared.b2s(tqpm.CounterLastDev));
            }catch(Exception ex){
                
            }


            ConnectionDrivers.updateTotalFromPrinter(total, z ,tqpr.VoucherVta+"",tqpr.VoucherVta-myCounterLastVta,tqpr.VoucherDev+"",tqpr.VoucherDev-myCounterLastDev, day);

            //System.out.println("Total = " + Double.parseDouble(Shared.b2s(tqpr.VtaA))/100.0 + " - " + Double.parseDouble(Shared.b2s(tqpr.DevA))/100.0);
            printer.ClosePort();
            System.out.println("Termino de actualizar valores. Ultima venta = " + tqpr.VoucherVta + " Ultima Devolucion = " + tqpr.VoucherDev);
            isOk = true;
        }else if ( Shared.getFileConfig("printerDriver").equals("tfhkaif") ){
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

            Double total = .0;
            if ( Double.parseDouble(Shared.getConfig("iva") ) <= .0 ){
                total = Double.parseDouble(line.substring(29,29+9))/100.0 - Double.parseDouble(line.substring(99, 99+9))/100.0;
            }else{
                total = Double.parseDouble(line.substring(39,39+9))/100.0 - Double.parseDouble(line.substring(109, 109+9))/100.0;
            }

            sc.close();
            file.delete();

            printer.UploadReportCmd(a, b, "U3A00" + pZ + "00" + z, Constants.tmpFileName);
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }

            file = new File(Constants.tmpFileName);
            sc = new Scanner(file);

            line = sc.next();

            String pLastCN = "0";//line.substring(168);
            int nNC = (Integer.parseInt(lastCN)-Integer.parseInt(pLastCN));
            ConnectionDrivers.updateTotalFromPrinter(total, z ,lReceipt,quantReceiptsToday,lastCN,nNC, day);

            printer.CloseFpctrl();
        }else{
            throw new Exception("Driver de impresora desconocido!");
        }
    }

    // Pre: updateValues()
    void printResumeZ(String day, String xoz) throws Exception{
        if ( !Constants.withFiscalPrinter ){
            return;
        }

        if ( Shared.getFileConfig("printerDriver").equals("PrnFiscalDLL32") ){
            isOk = false;
            printer.ClosePort();
            int ans = printer.OpenPort(Byte.parseByte(Shared.getFileConfig("printerPort")), (byte)2);
            printer.CancelTransaction();

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }

            Calendar calendar = GregorianCalendar.getInstance();
            Date dd = Constants.sdf4ncr.parse(ConnectionDrivers.getDate4NCR());
            calendar.setTime(dd);
            int hour = calendar.get(Calendar.HOUR) == 0?12:calendar.get(Calendar.HOUR);

            ans = printer.NewDoc(Constants.nonfiscalDoc, "SAUL", "123",
                    Shared.getUser().getLogin(), "", new NativeLong(0), (calendar.get(Calendar.DAY_OF_MONTH)),
                    //(calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, (calendar.get(Calendar.HOUR)+12)%13,
                    (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, hour,
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                    ((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1) ,(calendar.get(Calendar.DAY_OF_MONTH)),
                    (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)%100+Constants.ncrYearOffset, hour,
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                    ((calendar.get(Calendar.AM_PM) == Calendar.AM)?0:1));

            if ( ans != 0 ){
                throw new Exception(Shared.ncrErrMapping.get(ans));
            }

            ans = printer.PrintTextNoFiscal(Constants.normalFont,"",1,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," Resumen del Reporte " + xoz + "  Nro " + z ,2,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," Impresora Fiscal Serial Nro " + printerSerial,1,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," ",3,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," Sucursal: " + Shared.getConfig("storeName"),4,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," Caja Nro: " + Shared.getFileConfig("myId"),5,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," Ult Factura:        " + ConnectionDrivers.getLastReceipt(),6,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," Ult Nota de Credito " + ConnectionDrivers.getLastCN(),7,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," Nro de Facturas:      " + ConnectionDrivers.getQuant(Shared.getFileConfig("myId"),"num_facturas", day),8,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," Nro de N/C:         " + ConnectionDrivers.getQuant( Shared.getFileConfig("myId"),"numero_notas_credito", day),9,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," Neto Ventas:         " + Constants.df.format(ConnectionDrivers.getTotalDeclaredPos(Shared.getFileConfig("myId"), day)),11,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," ",12,(byte)0);
            ans = printer.PrintTextNoFiscal(Constants.normalFont," Fin de Resumen del Reporte Z Nro " + z,13,(byte)0);
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

            List<String> buffer = new ArrayList<String>();
            buffer.add("800 ");
            buffer.add("800 Resumen del Reporte Z Nro " + z);
            buffer.add("800 Impresora Fiscal Serial Nro " + printerSerial );
            buffer.add("800 ");
            buffer.add("800 Sucursal: " + Shared.getConfig("storeName"));
            buffer.add("800 Caja Nro: " + Shared.getFileConfig("myId"));
            buffer.add("800 Ult Factura:        " + ConnectionDrivers.getLastReceipt());
            buffer.add("800 Ult Nota de Credito " + ConnectionDrivers.getLastCN());
            buffer.add("800 Nro de Facturas:      " + ConnectionDrivers.getQuant(Shared.getFileConfig("myId"),"num_facturas", day));
            buffer.add("800 Nro de N/C:         " + ConnectionDrivers.getQuant( Shared.getFileConfig("myId"),"numero_notas_credito", day));
            buffer.add("800 ");
            buffer.add("800 Neto Ventas:         " + Constants.df.format(ConnectionDrivers.getTotalDeclaredPos(Shared.getFileConfig("myId"), day)));
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
        }else{
            throw new Exception("Driver de impresora desconocido!");
        }
    }

    private int compareComputerPrinter(double currentReceipt) throws SQLException, Exception {
        double computer = ConnectionDrivers.getSumTotalWithIva("curdate()","factura","Facturada", true , Shared.getFileConfig("myId"))
                - ConnectionDrivers.getSumTotalWithIva("curdate()","nota_de_credito","Nota",false, Shared.getFileConfig("myId") );

        updateValues("curdate()");

        double printer = ConnectionDrivers.getTotalPrinter("curdate()", Shared.getFileConfig("myId"));

        System.out.println(" Computer = " + (computer + currentReceipt));
        System.out.println(" Printer = " + printer );
        printer = ConnectionDrivers.getTotalPrinter("curdate()", Shared.getFileConfig("myId"));
        if ( Math.abs( computer + currentReceipt - printer ) < Constants.printerExilon ){
            return 0;
        }else if ( computer < printer ){
            return -1;
        }else{
            return 1;
        }
    }
    
}
