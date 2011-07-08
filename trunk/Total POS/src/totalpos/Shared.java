package totalpos;

import java.awt.Dimension;
import java.awt.Toolkit;

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

}
