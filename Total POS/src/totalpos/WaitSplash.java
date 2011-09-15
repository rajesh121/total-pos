package totalpos;

import javax.swing.SwingWorker;


/**
 *
 * @author Saúl Hidalgo
 */

public class WaitSplash extends SwingWorker<Void, Integer>{
    
    Doer w;

    WaitSplash(Doer aThis) {
        w = aThis;
    }

    @Override
    protected Void doInBackground(){
        w.doIt();
        return null;
    }

    @Override
    protected void done() {
        ;
        // It might be useful in a future =D!
    }
}
