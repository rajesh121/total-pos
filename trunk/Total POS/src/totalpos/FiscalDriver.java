package totalpos;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;

/**
 *
 * @author Saúl Hidalgo
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
    public int OpenPort(byte SerialPort , byte compBps);
    public int ClosePort();
    public int NewDoc(int TipDoc, byte[] NameCliente , byte[] IDCliente , byte[] IDCajero , byte[] IDMemPrn , NativeLong NVoucher , int DiaFct, int MesFct , int AnoFct, int HHFct, int MMFct, int SSFct, int AMPMFct, int DiaDev, int MesDev, int AnoDev, int HHDev, int MMDev, int SSDev, int AMPMDev);
    public int CloseDoc(double FPago1, double FPago2, double FPago3, double FPago4, double FPago5, double FPago6, double DscGlobal, double RcgGlobal, byte BarcodeLong, byte barcodeTipo, String barcodeData);
    public int RptX(byte DiaRptX , byte mesRptX , byte anoRptX , byte HHRptX, byte MMRptX, byte SSRptX, byte AMPMRptX , String IDCajero);
    public int RptZ(byte DiaRptX , byte mesRptX , byte anoRptX , byte HHRptX, byte MMRptX, byte SSRptX, byte AMPMRptX , String IDCajero);
    public int PrintTextNoFiscal(int TFonts, String TextNoFiscal, int NumLine, byte buffer);
    public int GenRptZ(byte DiaRptZ , byte MesRptZ, byte AnoRptZ, byte HHRptZ , byte MMRptZ , byte SSRptZ , byte AMPMRptZ);
}
