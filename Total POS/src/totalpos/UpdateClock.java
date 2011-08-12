package totalpos;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        int checkTurn = 1;
        while(Shared.getUser() != null ){
            ++checkTurn;
            checkTurn %= Constants.secondToCheckTurn;

            if ( checkTurn == 0 && Shared.getMyMainWindows() instanceof MainRetailWindows ){
                try {
                    List<Assign> as = ConnectionDrivers.listAssignsTurnPosRightNow();
                    boolean toContinue = false;
                    for (Assign assign : as) {
                        if (assign.getPos().equals(Constants.myId)) {
                            toContinue = true;
                            break; // for performance ...  =D!
                        }
                    }
                    if (!toContinue) {
                        MessageBox msg = new MessageBox(MessageBox.SGN_IMPORTANT, "El turno de venta se ha vencido.");
                        msg.show(null);
                        Shared.reload();
                        break;
                    }
                } catch (SQLException ex) {
                    MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos");
                    msg.show(null);
                    Shared.reload();
                    break;
                }
            }

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
                    break;
                }
            }

            Thread thisThread = Thread.currentThread();

            try {
                thisThread.sleep(1000);
            } catch (InterruptedException ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problema desconocido",ex);
                msb.show(null);
                Shared.reload();
                break;
            }

            Date d = new Date(Calendar.getInstance().getTimeInMillis());

            if ( Shared.getMyMainWindows() instanceof MainWindows ){
                MainWindows m = (MainWindows)Shared.getMyMainWindows();
                m.whatTimeIsIt.setText(Constants.sdfDateHour.format(d));
            } else if ( Shared.getMyMainWindows() instanceof MainRetailWindows ){
                MainRetailWindows m = (MainRetailWindows)Shared.getMyMainWindows();
                m.whatTimeIsIt.setText(Constants.sdfDateHour.format(d));
            }

        }
    }

}
