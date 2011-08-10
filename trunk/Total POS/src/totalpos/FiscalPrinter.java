package totalpos;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author shidalgo
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
    
}
