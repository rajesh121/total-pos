package totalpos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFrame;

/**
 *
 * @author shidalgo
 */
public class UpdateClock extends Thread{

    private long lastOperationTime;

    public UpdateClock() {
        lastOperationTime = Calendar.getInstance().getTimeInMillis();
    }

    public void actioned(){
        lastOperationTime = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public void run(){
        while(Shared.getUser() != null ){

            if ( Calendar.getInstance().getTimeInMillis() - lastOperationTime > Long.valueOf(Shared.getConfig("idleTime"))){

                MessageBox msg = new MessageBox(MessageBox.SGN_WARNING, "El sistema ha permanecido mucho tiempo sin uso. Requiere contraseña.");
                msg.show(Shared.getMyMainWindows());
                PasswordNeeded pn = new PasswordNeeded((JFrame)Shared.getMyMainWindows(), true, Shared.getUser());
                Shared.centerFrame(pn);
                pn.setVisible(true);
                if ( pn.isPasswordOk() ){
                    lastOperationTime = Calendar.getInstance().getTimeInMillis();
                }else{
                    Shared.reload();
                }
            }

            Thread thisThread = Thread.currentThread();

            try {
                thisThread.sleep(1000);
            } catch (InterruptedException ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problema desconocido",ex);
                msb.show(null);
            }

            Date d = new Date(Calendar.getInstance().getTimeInMillis());
            DateFormat dfm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            if ( Shared.getMyMainWindows() instanceof MainWindows ){
                MainWindows m = (MainWindows)Shared.getMyMainWindows();
                m.whatTimeIsIt.setText(dfm.format(d));
            }

        }
    }

}
