
package totalpos;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import srvSap.ObjectFactory;
import srvSap.ZSDSCABDEV;
import srvSap.ZSDSCABFACT;
import srvSap.ZSDSPOSDEV;
import srvSap.ZSDSPOSFACT;

/**
 *
 * @author Saúl Hidalgo
 */
public class ReceiptSap {
    List<Receipt> receipts = new LinkedList<Receipt>();
    private String id = Constants.maximunId; // is it really INF??
    private String maxFiscalId = Constants.minimunId;
    private String minFiscalId = Constants.maximunId;
    private String z = "";
    private String kind = "";
    private String printerId;
    private String client = "";

    private static ObjectFactory of = Constants.of;

    public ReceiptSap() {
    }

    public void add(Receipt r){
        receipts.add(r);
        if ( r.getInternId().compareTo(id) < 0 ){
            id = r.getInternId();
        }

        if ( Integer.parseInt( r.getFiscalNumber() ) > Integer.parseInt(maxFiscalId) ){
            maxFiscalId = r.getFiscalNumber();
        }

        if ( Integer.parseInt( r.getFiscalNumber() ) < Integer.parseInt(minFiscalId) ){
            minFiscalId = r.getFiscalNumber();
        }

        kind = r.getClientId().equals("Contado")?"1":"2";
        client = r.getClientId().equals("Contado")?"":r.getClientId();
        z = r.getzReportId();
        printerId = r.getFiscalPrinter();
    }

    public int getSize(){
        return receipts.size();
    }

    public ZSDSCABFACT getHeaderF(String myDay){
        ZSDSCABFACT ans = new ZSDSCABFACT();
        String idF = "F" + id;

        String range = minFiscalId + "-" + maxFiscalId;

        System.out.println("MANDT\tFKDAT\tVBELN\tZTIPV\tKUNNR\tRANGO\tREPOZ\tIMPRE\tWAERS\tWERKS");
        ans.setMANDT(of.createZSDSCABDEVMANDT(Constants.mant));
        System.out.print(Constants.mant + "\t");
        ans.setFKDAT(of.createZSDSPOSDEVFKDAT(myDay.replace("-", "")));
        System.out.print(myDay.replace("-", "") + "\t");
        ans.setVBELN(of.createZSDSCABDEVVBELN(idF));
        System.out.print(idF + "\t");
        ans.setZTIPV(of.createZSDSCABDEVZTIPV(kind));
        System.out.print(kind + "\t");
        ans.setKUNNR(of.createZSDSCABDEVKUNNR(client));
        System.out.print(client + "\t");
        ans.setRANGO(of.createZSDSCABDEVRANGO(range));
        System.out.print(range + "\t");
        ans.setREPOZ(of.createZSDSCABDEVREPOZ(z));
        System.out.print(z + "\t");
        ans.setIMPRE(of.createZSDSCABDEVIMPRE(printerId));
        System.out.print(printerId + "\t");
        ans.setWAERS(of.createZSDSCABDEVWAERS(Constants.waerks));
        System.out.print(Constants.waerks + "\t");
        ans.setWERKS(of.createZSDSCABDEVWERKS(Constants.storePrefix+Shared.getConfig("storeName")));
        System.out.print(Constants.storePrefix+Shared.getConfig("storeName") + "\n");
        return ans;
    }

    public ZSDSCABDEV getHeader(String myDay){
        ZSDSCABDEV ans = new ZSDSCABDEV();
        String idF = "D" + id;

        String range = minFiscalId + "-" + maxFiscalId;

        System.out.println("MANDT\tFKDAT\tVBELN\tZTIPV\tKUNNR\tRANGO\tREPOZ\tIMPRE\tWAERS\tWERKS");
        ans.setMANDT(of.createZSDSCABDEVMANDT(Constants.mant));
        System.out.print(Constants.mant + "\t");
        ans.setFKDAT(of.createZSDSPOSDEVFKDAT(myDay.replace("-", "")));
        System.out.print(myDay.replace("-", "") + "\t");
        ans.setVBELN(of.createZSDSCABDEVVBELN(idF));
        System.out.print(idF + "\t");
        ans.setZTIPV(of.createZSDSCABDEVZTIPV(kind));
        System.out.print(kind + "\t");
        ans.setKUNNR(of.createZSDSCABDEVKUNNR(client));
        System.out.print(client + "\t");
        ans.setRANGO(of.createZSDSCABDEVRANGO(range));
        System.out.print(range + "\t");
        ans.setREPOZ(of.createZSDSCABDEVREPOZ(z));
        System.out.print(z + "\t");
        ans.setIMPRE(of.createZSDSCABDEVIMPRE(printerId));
        System.out.print(printerId + "\t");
        ans.setWAERS(of.createZSDSCABDEVWAERS(Constants.waerks));
        System.out.print(Constants.waerks + "\t");
        ans.setWERKS(of.createZSDSCABDEVWERKS(Constants.storePrefix+Shared.getConfig("storeName")));
        System.out.print(Constants.storePrefix+Shared.getConfig("storeName") + "\n");
        return ans;
    }

    public List<ZSDSPOSDEV> getDetails(String myDay){
        List<ZSDSPOSDEV> ans = new LinkedList<ZSDSPOSDEV>();
        
        /**
         * I am not proud about this code xDDD
         * But, it is simply fast!
         */

        TreeMap<String,String> units = new TreeMap<String, String>();
        TreeMap<String,Integer> quant = new TreeMap<String, Integer>();
        TreeMap<String,Double> price = new TreeMap<String, Double>();
        TreeMap<String,Double> disc = new TreeMap<String, Double>();
        for (Receipt receipt : receipts) {
            for (Item2Receipt item2Receipt : receipt.getItems()) {
                if ( item2Receipt.getQuant() > 0 ){
                    String idd = item2Receipt.getItem().getCode();
                    if ( !units.containsKey(idd) ){
                        units.put(idd, item2Receipt.getItem().getSellUnits());
                        quant.put(idd, item2Receipt.getQuant());
                        price.put(idd, item2Receipt.getItem().getLastPrice().getQuant());
                        disc.put(idd, (item2Receipt.getItem().getDescuento()/100.0)*item2Receipt.getItem().getLastPrice().getQuant());
                    }else{
                        quant.put(idd, quant.get(idd) + item2Receipt.getQuant());
                    }
                }
            }
        }
        System.out.println("MANDT\tFKDAT\tVBELN\tPOSNR\tEAN11\tKWMENG\tVRKME\tCHARG\tKBETP\tKBETD\tPERNR\tWERKS");
        int position = 1;
        for (Iterator<String> it = (units.keySet().iterator()); it.hasNext();) {
            String idd = it.next();
            ZSDSPOSDEV zdpd = new ZSDSPOSDEV();
            zdpd.setMANDT(of.createZSDSPOSDEVMANDT(Constants.mant));
            System.out.print(Constants.mant + "\t");
            zdpd.setFKDAT(of.createZSDSPOSDEVFKDAT(myDay.replace("-", "")));
            System.out.print(myDay.replace("-", "") + "\t");
            zdpd.setVBELN(of.createZSDSPOSDEVVBELN("D" + id ));
            System.out.print("D" + id + "\t");
            zdpd.setPOSNR(of.createZSDSPOSDEVPOSNR(Constants.df2intSAP.format(position++)));
            System.out.print(Constants.df2intSAP.format(position-1) + "\t");
            zdpd.setEAN11(of.createZSDSPOSDEVEAN11(idd));
            System.out.print(idd + "\t");
            zdpd.setKWMENG(new BigDecimal(quant.get(idd)));
            System.out.print(quant.get(idd) + "\t");
            zdpd.setVRKME(of.createZSDSPOSDEVVRKME(units.get(idd)));
            System.out.print(units.get(idd) + "\t");
            zdpd.setCHARG(of.createZSDSPOSDEVCHARG(""));
            System.out.print("" + "\t");
            zdpd.setKBETP(new BigDecimal(price.get(idd)));
            System.out.print(price.get(idd) + "\t");
            zdpd.setKBETD(new BigDecimal(disc.get(idd)));
            System.out.print(disc.get(idd) + "\t");
            zdpd.setPERNR(of.createZSDSPOSDEVPERNR("999999"));
            System.out.print("999999" + "\t");
            zdpd.setWERKS(of.createZSDSPOSDEVWERKS(Constants.storePrefix+Shared.getConfig("storeName")));
            System.out.print(Constants.storePrefix+Shared.getConfig("storeName") + "\n");
            ans.add(zdpd);
        }
        return ans;
    }

    public List<ZSDSPOSFACT> getDetailsF(String myDay){
        List<ZSDSPOSFACT> ans = new LinkedList<ZSDSPOSFACT>();

        /**
         * I am not proud about this code xDDD
         * But, it is simply fast!
         */

        TreeMap<String,String> units = new TreeMap<String, String>();
        TreeMap<String,Integer> quant = new TreeMap<String, Integer>();
        TreeMap<String,Double> price = new TreeMap<String, Double>();
        TreeMap<String,Double> disc = new TreeMap<String, Double>();
        Double gDisc = .0;
        for (Receipt receipt : receipts) {
            for (Item2Receipt item2Receipt : receipt.getItems()) {
                if ( item2Receipt.getQuant() > 0 ){
                    String idd = item2Receipt.getItem().getCode();
                    if ( !units.containsKey(idd) ){
                        units.put(idd, item2Receipt.getItem().getSellUnits());
                        quant.put(idd, item2Receipt.getQuant() );
                        price.put(idd, item2Receipt.getItem().getLastPrice().getQuant());
                        disc.put(idd, (item2Receipt.getItem().getDescuento()/100.0)*item2Receipt.getItem().getLastPrice().getQuant());

                    }else{
                        quant.put(idd, quant.get(idd) + item2Receipt.getQuant());
                    }
                }
            }
            gDisc = receipt.getGlobalDiscount();
        }

        System.out.println("MANDT\tFKDAT\tVBELN\tPOSNR\tEAN11\tKWMENG\tVRKME\tCHARG\tKBETP\tKBETD\tPERNR\tWERKS");
        int position = 1;
        for (Iterator<String> it = (units.keySet().iterator()); it.hasNext();) {
            String idd = it.next();
            ZSDSPOSFACT zdpd = new ZSDSPOSFACT();
            zdpd.setMANDT(of.createZSDSPOSDEVMANDT(Constants.mant));
            System.out.print(Constants.mant + "\t");
            zdpd.setFKDAT(of.createZSDSPOSDEVFKDAT(myDay.replace("-", "")));
            System.out.print(myDay.replace("-", "") + "\t");
            zdpd.setVBELN(of.createZSDSPOSDEVVBELN("F" + id ));
            System.out.print("F" + id + "\t");
            zdpd.setPOSNR(of.createZSDSPOSDEVPOSNR(Constants.df2intSAP.format(position++)));
            System.out.print(Constants.df2intSAP.format(position-1) + "\t");
            zdpd.setEAN11(of.createZSDSPOSDEVEAN11(idd));
            System.out.print(idd + "\t");
            zdpd.setKWMENG(new BigDecimal(quant.get(idd)));
            System.out.print(quant.get(idd) + "\t");
            zdpd.setVRKME(of.createZSDSPOSDEVVRKME(units.get(idd)));
            System.out.print(units.get(idd) + "\t");
            zdpd.setCHARG(of.createZSDSPOSDEVCHARG(""));
            System.out.print("" + "\t");
            zdpd.setKBETP(new BigDecimal(price.get(idd)));
            System.out.print(price.get(idd) + "\t");
            zdpd.setKBETD(new BigDecimal(disc.get(idd)+gDisc*(price.get(idd)-disc.get(idd))));
            System.out.print((disc.get(idd)+gDisc*(price.get(idd)-disc.get(idd))) + "\t");
            zdpd.setPERNR(of.createZSDSPOSDEVPERNR("999999"));
            System.out.print("999999" + "\t");
            zdpd.setWERKS(of.createZSDSCABDEVWERKS(Constants.storePrefix+Shared.getConfig("storeName")));
            System.out.print(Constants.storePrefix+Shared.getConfig("storeName") + "\n");
            ans.add(zdpd);
        }
        return ans;
    }

}
