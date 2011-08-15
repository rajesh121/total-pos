package totalpos;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import java.io.File;
import java.io.FileNotFoundException;
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

        if ( !items.isEmpty() ){
            int line = 1;
            if ( client != null && !client.getId().isEmpty() ){
                printer.SendCmd(a, b, "i0" + ( line++ ) + "CI/RIF: " + client.getId());
                if ( b.getValue() != 0 ){
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
                }
                if ( !client.getName().trim().isEmpty() ){
                    printer.SendCmd(a, b, "i0" + ( line++ ) + "Nombre: " + client.getName());
                    if ( b.getValue() != 0 ){
                        throw new Exception(Shared.getErrMapping().get(b.getValue()));
                    }
                }
                if ( !client.getPhone().trim().isEmpty() ){
                    printer.SendCmd(a, b, "i0" + ( line++ ) + "Telefono: " + client.getPhone());
                    if ( b.getValue() != 0 ){
                        throw new Exception(Shared.getErrMapping().get(b.getValue()));
                    }
                }
                if ( !client.getAddress().trim().isEmpty() ){
                    printer.SendCmd(a, b, "i0" + ( line++ ) + "Direccion: " + client.getAddress());
                    if ( b.getValue() != 0 ){
                        throw new Exception(Shared.getErrMapping().get(b.getValue()));
                    }
                }
                printer.SendCmd(a, b, "i0" + ( line++ ) + "Correlativo: " + ticketId);
                if ( b.getValue() != 0 ){
                    throw new Exception(Shared.getErrMapping().get(b.getValue()));
                }

                
            }
            printer.SendCmd(a, b, "i0" + ( line++ ) + "Vendedor: " + u.getNombre());
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
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
            printer.SendCmd(a, b, "4");
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }
            printer.SendCmd(a, b, "101");
            if ( b.getValue() != 0 ){
                throw new Exception(Shared.getErrMapping().get(b.getValue()));
            }
            
        }
        printer.CloseFpctrl();
    }
    
}
