package totalpos;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private String lastReceipt = null;

    public FiscalPrinter() {
        printer = (FiscalDriver) Native.loadLibrary("tfhkaif", FiscalDriver.class);;
    }

     public boolean checkPrinter() throws SQLException, FileNotFoundException, Exception {
        String p = ConnectionDrivers.getMyPrinter();
        return isTheSame(p);
    }

    private void calculateSerial() throws Exception{
        boolean ansT;
        isOk = false;
        IntByReference a = new IntByReference();
        IntByReference b = new IntByReference();
        ansT = printer.OpenFpctrl("COM1");
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
        String s = sc.next().substring(66, 76);

        printerSerial = s;
        sc.close();
        file.delete();
        printer.CloseFpctrl();
    }

    public boolean isTheSame(String serial) throws Exception{
        return getSerial().equals(serial);
    }

    public void printTicket(List<Item2Receipt> items , Client client, Double globalDiscount, String ticketId, User u , List<PayForm> pfs) throws Exception{
        isOk = false;
        IntByReference a = new IntByReference();
        IntByReference b = new IntByReference();
        printer.OpenFpctrl("COM1");

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
            buffer.add("i0" + ( line++ ) + "Caja: " + Constants.myId);

            for (String bu : buffer) {
                printer.SendCmd(a, b, bu);
                if ( b.getValue() != 0 ){
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
                }
            }

            for (Item2Receipt item2r : items) {
                Item item = item2r.getItem();
                printer.SendCmd(a, b, "!" + ( Shared.formatDoubleToPrint(item.getLastPrice().getQuant()) ) +
                        Shared.formatQuantToPrint(item2r.getQuant()+.0) + item.getDescription().substring(0, Math.min(item.getDescription().length(), 38)));
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
            printer.SendCmd(a, b, "3");
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }

            printer.SendCmd(a, b, "y" + ticketId);// + Shared.formatDoubleToPrintDiscount(globalDiscount));
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }

            if ( globalDiscount != null && globalDiscount > 0 ){
                printer.SendCmd(a, b, "p-" + Shared.formatDoubleToPrintDiscount(globalDiscount));
                if ( b.getValue() != 0 ){
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
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
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
                }
            }
        }
        printer.UploadStatusCmd(a, b, "S1", Constants.tmpFileName);
        if ( b.getValue() != 0 ){
            throw new Exception(Shared.getErrMapping().get(b.getValue()));
        }

        File file = new File(Constants.tmpFileName);

        Scanner sc = new Scanner(file);

        boolean ansT = sc.hasNext();
        assert(ansT);
        String s = sc.next().substring(21, 29);

        lastReceipt = s;
        sc.close();
        file.delete();
        printer.CloseFpctrl();
        isOk = true;
    }

    public void extractMoney(User u, String boss, Double quant) throws Exception{
        isOk = false;
        IntByReference a = new IntByReference();
        IntByReference b = new IntByReference();
        printer.OpenFpctrl("COM1");

        printer.SendCmd(a, b, "9001" + Shared.formatDoubleToPrint(quant) );
        if ( b.getValue() != 0 ){
            throw new Exception(Shared.getErrMapping().get(b.getValue()));
        }

        List<String> buffer = new ArrayList<String>();
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
    }

    public void reportExtraction() throws Exception{
        isOk = false;
        IntByReference a = new IntByReference();
        IntByReference b = new IntByReference();
        printer.OpenFpctrl("COM1");

        printer.SendCmd(a, b, "t");
        if ( b.getValue() != 0 ){
            throw new Exception(Shared.getErrMapping().get(b.getValue()));
        }

        printer.CloseFpctrl();
        isOk = true;
    }

    public String getZ() throws Exception{
        if ( z == null  ){
            isOk = false;
            IntByReference a = new IntByReference();
            IntByReference b = new IntByReference();
            printer.OpenFpctrl("COM1");

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
    }

    public String getSerial() throws Exception{
        if ( printerSerial == null ){
            calculateSerial();
        }
        return printerSerial;
    }

    public String getLastFiscalNumber(){
        return lastReceipt;
    }

    public void printCreditNote(List<Item2Receipt> items, String ticketId, String myId, User u ) throws Exception{
        isOk = false;
        IntByReference a = new IntByReference();
        IntByReference b = new IntByReference();
        printer.OpenFpctrl("COM1");

        List<String> buffer = new ArrayList<String>();

        if ( !items.isEmpty() ){
            int line = 1;
            buffer.add("i0" + ( line++ ) + "Correlativo: " + myId);
            buffer.add("i0" + ( line++ ) + "Factura: " + ticketId);
            buffer.add("i0" + ( line++ ) + "Usuario: " + u.getNombre());

            for (String bu : buffer) {
                printer.SendCmd(a, b, bu);
                if ( b.getValue() != 0 ){
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
                }
            }

            for (Item2Receipt item2r : items) {
                Item item = item2r.getItem();
                printer.SendCmd(a, b, "d1" + ( Shared.formatDoubleToPrint(item.getLastPrice().getQuant()) ) +
                        Shared.formatQuantToPrint(item2r.getQuant()+.0) + item.getDescription().substring(0, Math.min(item.getDescription().length(), 38)));
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
            printer.SendCmd(a, b, "3");
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }
            printer.SendCmd(a, b, "f01000000000000");
        }

        printer.UploadStatusCmd(a, b, "S1", Constants.tmpFileName);
        if ( b.getValue() != 0 ){
            throw new Exception(Shared.getErrMapping().get(b.getValue()));
        }

        File file = new File(Constants.tmpFileName);

        Scanner sc = new Scanner(file);

        boolean ansT = sc.hasNext();
        assert(ansT);
        String s = sc.next().substring(21, 29);

        lastReceipt = s;
        sc.close();
        file.delete();
        printer.CloseFpctrl();
        isOk = true;
    }

    void report(String r){
        isOk = false;
        IntByReference a = new IntByReference();
        IntByReference b = new IntByReference();
        printer.OpenFpctrl("COM1");
        printer.SendCmd(a, b, "I0"+r);
        printer.CloseFpctrl();
        isOk = true;
    }

    public void forceClose(){
        printer.CloseFpctrl();
    }
    
}
