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
        Shared.setProcessingWindows(Shared.getProcessingWindows()+1);
        Shared.createLockFile();
        w.doIt();
        return null;
    }

    @Override
    protected void done() {
        Shared.setProcessingWindows(Shared.getProcessingWindows()-1);
        if ( Shared.getProcessingWindows() == 0 ){
            Shared.removeLockFile();
        }
        w.close();
    }
}
