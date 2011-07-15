package totalpos;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Saul Hidalgo
 */
public class Shared {


    protected static void centerFrame(javax.swing.JFrame frame){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        frame.setLocation(x, y);
    }

    protected static void maximize(JFrame frame){
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    protected static String hashPassword(String x){
        return x.hashCode() + "";
    }

    static void centerFrame(JDialog dialog) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = dialog.getSize().width;
        int h = dialog.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        dialog.setLocation(x, y);
    }

}
