package totalpos;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import java.io.File;
import java.io.FileNotFoundException;
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

    public FiscalPrinter() {
        printer = (FiscalDriver) Native.loadLibrary("tfhkaif", FiscalDriver.class);;
    }

    public boolean isTheSame(String serial) throws FileNotFoundException, Exception{
        boolean ans , ansT;
        isOk = false;
        IntByReference a = new IntByReference();
        IntByReference b = new IntByReference();
        ansT = printer.OpenFpctrl("COM1");
        assert (ansT);

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
        ans = s.equals(serial);
        sc.close();
        file.delete();
        printer.CloseFpctrl();
        isOk = true;
        return ans;
    }

    public void printTicket(List<Item> items , Client client, Double globalDiscount, String ticketId, User u) throws Exception{
        isOk = false;
        IntByReference a = new IntByReference();
        IntByReference b = new IntByReference();
        printer.OpenFpctrl("COM1");

        List<String> buffer = new ArrayList<String>();

        if ( !items.isEmpty() ){
            int line = 1;
            if ( client != null && !client.getId().isEmpty() ){
                buffer.add("i0" + ( line++ ) + "CI/RIF: " + client.getId());
                if ( !client.getName().trim().isEmpty() ) {
                    buffer.add("i0" + (line++) + "Nombre: " + client.getName());
                }
                if ( !client.getName().trim().isEmpty() ) {
                    buffer.add("i0" + (line++) + "Nombre: " + client.getName());
                }
                if ( !client.getPhone().trim().isEmpty() ) {
                    buffer.add("i0" + (line++) + "Telefono: " + client.getPhone());
                }
                if ( !client.getAddress().trim().isEmpty() ) {
                    buffer.add("i0" + (line++) + "Direccion: " + client.getAddress());
                }
            }
            buffer.add("i0" + ( line++ ) + "Correlativo: " + ticketId);
            buffer.add("i0" + ( line++ ) + "Vendedor: " + u.getNombre());

            for (String bu : buffer) {
                printer.SendCmd(a, b, bu);
                if ( b.getValue() != 0 ){
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
                }
            }

            for (Item item : items) {
                printer.SendCmd(a, b, "!" + ( Shared.formatDoubleToPrint(item.getLastPrice().getQuant()) ) +
                        "00001000" + item.getDescription().substring(0, Math.min(item.getDescription().length(), 38)));
                if ( b.getValue() != 0 ){
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
                }
            }
            printer.SendCmd(a, b, "3");
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }
            printer.SendCmd(a, b, "101");
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }
            
        }
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
        printer.CloseFpctrl();
        printer.OpenFpctrl("COM1");

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
    
}
