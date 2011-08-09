package totalpos;

import com.sun.jna.Library;
import com.sun.jna.ptr.IntByReference;

/**
 *
 * @author shidalgo
 */
public interface FiscalDriver extends Library{

    public boolean OpenFpctrl(String str);
    public boolean UploadStatusCmd(IntByReference status, IntByReference err, String cmd, String fileAdd);
    public boolean CloseFpctrl();
    public boolean CheckFprinter();
    public boolean ReadFpStatus(IntByReference status, IntByReference err);
    public boolean SendCmd(IntByReference status, IntByReference err, String cmd);
    public boolean SendNCmd(IntByReference status, IntByReference err, String buff);
    public boolean SendFileCmd(IntByReference status, IntByReference err, String fileAdd);
    public boolean UploadReportCmd(IntByReference status, IntByReference err, String cmd, String fileAdd);

}
