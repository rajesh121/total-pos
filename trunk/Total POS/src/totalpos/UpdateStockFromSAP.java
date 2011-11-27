package totalpos;

import java.awt.Window;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ws.WS;
import ws.WSService;

/**
 *
 * @author Saúl Hidalgo
 */
public class UpdateStockFromSAP implements Doer{

    public Working workingFrame;
    public String mode;

    public UpdateStockFromSAP(String mode) {
        this.mode = mode;
    }

    public void updateStockFromSAP() {
        workingFrame = new Working((Window) Shared.getMyMainWindows());
        WaitSplash ws = new WaitSplash(this);

        Shared.centerFrame(workingFrame);
        workingFrame.setVisible(true);

        ws.execute();
    }

    public void updatePricesFromSAP(){
        
    }

    @Override
    public void doIt() {
        try {
            Shared.createBackup("articulo precio codigo_de_barras costo movimiento_inventario detalles_movimientos");

            WS ws = new WSService().getWSPort();

            ws.initialize("2011-11-11", Constants.storePrefix+Shared.getConfig("storeName"));

            if ( mode.equals("MM") ){
                String ansListMM = ws.listMM(ConnectionDrivers.getLastMM());
                System.out.println(" ansListMM = " + ansListMM );

                String itemsNeeded = ConnectionDrivers.createNewMovement(ansListMM);
                System.out.println("itemsNeeded = " + itemsNeeded);
                String ansDescriptions = ws.listDescriptionFromItems(itemsNeeded);

                ConnectionDrivers.createItems(ansDescriptions);

                String ansPricesDiscounts = ws.listPriceFromItemList(itemsNeeded , Shared.getConfig("Z") , Constants.storePrefix+Shared.getConfig("storeName"));

                System.out.println("ansPricesDiscounts = " + ansPricesDiscounts);

                ConnectionDrivers.setPrices(ansPricesDiscounts);
                System.out.println("Listo!");
            }else if ( mode.equals("Prices")){
                String myDay = Shared.getConfig("lastPriceUpdate");
                String newPrices = ws.listNewPriceFromDate(myDay, Constants.storePrefix+Shared.getConfig("storeName"), Shared.getConfig("Z"));
                System.out.println("newPrices = " + newPrices);
                ConnectionDrivers.setPrices(newPrices);
                ConnectionDrivers.setLastUpdateNow();
            }


            MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Actualizado!");
            msg.show(Shared.getMyMainWindows());
        } catch (Exception ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Ha ocurrido un error." , ex);
            msg.show(Shared.getMyMainWindows());
        }
    }

    @Override
    public void close() {
        workingFrame.setVisible(false);
    }

}
