package totalpos;

import java.awt.Window;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.jfree.ui.ExtensionFileFilter;

/**
 *
 * @author Saúl Hidalgo
 */
public class UpdateStock implements Doer{

    public Working workingFrame;

    public void updateStock(){
        workingFrame = new Working((Window) Shared.getMyMainWindows());
        WaitSplash ws = new WaitSplash(this);

        Shared.centerFrame(workingFrame);
        workingFrame.setVisible(true);

        ws.execute();
    }

    @Override
    public void doIt() {
        try {
            Shared.createBackup();

            JFileChooser jfc = new JFileChooser();
            FileFilter f = new ExtensionFileFilter("Traslados de Total Pos","pos");
            jfc.setFileFilter(f);
            int selection = jfc.showOpenDialog(Shared.getMyMainWindows());
            if ( selection == JFileChooser.APPROVE_OPTION ){
                Shared.prepareMovements( jfc.getSelectedFile());
                Shared.updateMovements();
                if ( Shared.isHadMovements() ){
                    MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Fue cargado el nuevo inventario satisfactoriamente!");
                    msg.show(Shared.getMyMainWindows());
                }else{
                    MessageBox msg = new MessageBox(MessageBox.SGN_WARNING, "La tienda no tuvo ningun movimiento asociado.");
                    msg.show(Shared.getMyMainWindows());
                }
            }
        } catch (Exception ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Ha ocurrido un error. Puede que haya elegido un archivo inválido." , ex);
            msg.show(Shared.getMyMainWindows());
        }
    }

    @Override
    public void close() {
        workingFrame.setVisible(false);
    }

}
