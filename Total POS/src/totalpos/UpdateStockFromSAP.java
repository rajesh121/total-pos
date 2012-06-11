package totalpos;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JFrame;
import net.n3.nanoxml.XMLException;
import webservice.TotalPosWebService;
import webservice.TotalPosWebServiceService;

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
        workingFrame = new Working((JFrame) Shared.getMyMainWindows());
        WaitSplash ws = new WaitSplash(this);

        Shared.centerFrame(workingFrame);
        workingFrame.setVisible(true);

        ws.execute();
        //doIt();
    }

    private void updatePrices(Connection c, TotalPosWebService ws) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLException{
        String myDay = Shared.getConfig("lastPriceUpdate");
        String newPrices = ws.listNewPriceFromDate(myDay, Shared.getConfig("storePrefix")+Shared.getConfig("storeName"), Shared.getConfig("Z"));
        System.out.println("newPrices = " + newPrices);

        ConnectionDrivers.setPrices(c,newPrices);
        ConnectionDrivers.setLastUpdateNow();
    }

    private void updateFlagC(Connection c, TotalPosWebService ws) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLException{
        String myDay = ConnectionDrivers.getLastFlagc();

        String daysFlagc = ws.getFlagC(Shared.getConfig("storePrefix")+Shared.getConfig("storeName"), myDay);

        ConnectionDrivers.updateFlagc(daysFlagc, c);
    }
    
    @Override
    public void doIt() {

        Connection c = null;
        try {
            Shared.createBackup("articulo precio codigo_de_barras costo movimiento_inventario detalles_movimientos");

            TotalPosWebService ws = new TotalPosWebServiceService().getTotalPosWebServicePort();

            c = ConnectionDrivers.cpds.getConnection();
            c.setAutoCommit(false);

            if ( mode.equals("MM") ){

                String ansListMM = ws.listMM(ConnectionDrivers.getLastMM(), Shared.getConfig("storePrefix")+Shared.getConfig("storeName"));
                //String ansListMM = ws.listMM("4900458128");
                System.out.println(" ansListMM = " + ansListMM );

                String itemsNeeded = ConnectionDrivers.createNewMovement(c, ansListMM);
                ansListMM = null;
                System.out.println("itemsNeeded = " + itemsNeeded);
                ws.listDescriptionFromItems(itemsNeeded, Shared.getConfig("storePrefix")+Shared.getConfig("storeName"));

                String ansPricesDiscounts = ws.listPriceFromItemList(itemsNeeded , Shared.getConfig("Z") , Shared.getConfig("storePrefix")+Shared.getConfig("storeName"));

                System.out.println("ansPricesDiscounts = " + ansPricesDiscounts);

                ConnectionDrivers.setPrices(c, ansPricesDiscounts);

                // Update prices too
                updatePrices(c,ws);

                // flagsC
                updateFlagC(c, ws);

                // Descriptions
                updateDescriptions(c, ws);

                System.out.println("Listo!");
            }else if ( mode.equals("Prices")){
                updatePrices(c,ws);
            }else if ( mode.equals("initialStock") ){
                
                String ansListMM = ws.getInitialStockWithPrices(Shared.getConfig("storePrefix")+Shared.getConfig("storeName"), Shared.getConfig("Z"));
                System.out.println(" ansListMM = " + ansListMM );
                ConnectionDrivers.getInitialStock(c, ansListMM);

                ConnectionDrivers.disableInitialStock(c);

                System.out.println("Listo!");
            }else if ( mode.equals("profitWorkers")){
                System.out.println("Profit DB Name " + Shared.getConfig("profitDatabase"));
                String ans = ws.listEmployCode(Shared.getConfig("storeNameProfit"), Shared.getConfig("profitDatabase"));
                System.out.println("Ans = " + ans);
                ConnectionDrivers.updateEmployees(ans);
            }

            System.out.println("Haciendo el commit...");
            c.commit();
            System.out.println("Terminado commit Exitoso!");

            MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Actualizado!");
            msg.show(Shared.getMyMainWindows());
        } catch (Exception ex) {
            System.out.println("Comenzo la exception");
            try {
                System.out.println("Haciendo Rollback");
                c.rollback();
                System.out.println("Reversado!");
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Ha ocurrido un error. No se ha guardado ningun cambio.", ex);
                msg.show(Shared.getMyMainWindows());
            } catch (Exception ex1) {
                // We are in problems :(
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Ha ocurrido un error. No se ha guardado ningun cambio.", ex);
                msg.show(Shared.getMyMainWindows());
                System.out.println("Ha ocurrido un error. Haciendo Roll back..." + ex1.getMessage());
            }
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                System.out.println("Ha ocurrido un error cerrando la conexion. " + ex.getMessage());
            }
        }
    }

    @Override
    public void close() {
        workingFrame.setVisible(false);
    }

    private void updateDescriptions(Connection c, TotalPosWebService ws) {
        /*String myDay = ConnectionDrivers.getLastDescriptionUpdate();

        String daysFlagc = ws.getFlagC(Constants.storePrefix+Shared.getConfig("storeName"), myDay);

        ConnectionDrivers.updateFlagc(daysFlagc, c);*/
    }

}
