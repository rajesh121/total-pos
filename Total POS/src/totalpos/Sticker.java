package totalpos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

/**
 *
 * @author shidalgo
 */
public class Sticker {

    private static int offset = 420;
    private static int separations[] = {165,175,175,135,185,25,25,240,222,25};

    private String barCode, mark, description, price;

    public Sticker(String barCode, String mark, String description, String price) {
        this.barCode = barCode;
        this.mark = mark;
        this.description = description;
        this.price = price;
    }

    public void print(int n){
        try {
            String description2 = "";
            if (description.length() > 34) {
                description2 = description.substring(34);
                description = description.substring(0, 34);
            }
            if (description2.length() > 34) {
                description2 = description2.substring(0, 34);
            }
            if (mark.length() > 15) {
                mark = mark.substring(0, 15);
            }
            if (price.length() > 5) {
                price = price.substring(0, 5);
            }
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date d = new Date();
            String date = dateFormat.format(d);
            n = (n + 1) / 2;
            PrintService psZebra = PrintServiceLookup.lookupDefaultPrintService();
            DocPrintJob job = psZebra.createPrintJob();
            String buff =
                "N\n" +
                "A" + separations[0] +",0,0,1,1,1,N,\"Grupo Total 99 C.A.\"\n"+
                "A" + separations[1] + ",15,0,1,1,1,N,\"RIF: J-31150187-8\"\n"+
                "A" + separations[2] + ",30,0,5,1,1,N,\""+price +"\"\n"+
                "A" + separations[3] + ",45,0,3,1,1,N,\"Bsf\"\n"+
                "A" + separations[4] + ",80,0,3,1,1,N,\""+mark+"\"\n"+
                "A" + separations[5] + ",100,0,1,1,1,N,\""+description+"\"\n"+
                "A" + separations[6] + ",115,0,1,1,1,N,\""+description2+"\"\n"+
                "A" + separations[7] + ",140,0,1,1,1,N,\""+date + "\"\n"+
                "A" + separations[8] + ",160,0,1,1,1,N,\""+barCode +"\"\n"+
                "B" + separations[9] + ",130,0,1,1,2,100,N,\"" + barCode + "\"\n"+

                "A" + (offset+separations[0]) +",0,0,1,1,1,N,\"Grupo Total 99 C.A.\"\n"+
                "A" + (offset+separations[1]) + ",15,0,1,1,1,N,\"RIF: J-31150187-8\"\n"+
                "A" + (offset+separations[2]) + ",30,0,5,1,1,N,\""+price +"\"\n"+
                "A" + (offset+separations[3]) + ",45,0,3,1,1,N,\"Bsf\"\n"+
                "A" + (offset+separations[4]) + ",80,0,3,1,1,N,\""+mark+"\"\n"+
                "A" + (offset+separations[5]) + ",100,0,1,1,1,N,\""+description+"\"\n"+
                "A" + (offset+separations[6]) + ",115,0,1,1,1,N,\""+description2+"\"\n"+
                "A" + (offset+separations[7]) + ",140,0,1,1,1,N,\""+date + "\"\n"+
                "A" + (offset+separations[8]) + ",160,0,1,1,1,N,\""+barCode +"\"\n"+
                "B" + (offset+separations[9]) + ",130,0,1,1,2,100,N,\"" + barCode + "\"\n"+
                "P" + n + "\n";

            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(buff.getBytes(), flavor, null);
            job.print(doc, null);
        } catch (PrintException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la impresora.",ex);
            msb.show(MainWindows.mw);
        }
    }

}
