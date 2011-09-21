
package totalpos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ZSDSCABDEV;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ZSDSPOSDEV;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ObjectFactory;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ZSDSCABFACT;
import org.datacontract.schemas._2004._07.grupototalcapacomunicacion.ZSDSPOSFACT;

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

        if ( r.getFiscalNumber().compareTo(maxFiscalId) > 0 ){
            maxFiscalId = r.getFiscalNumber();
        }

        if ( r.getFiscalNumber().compareTo(minFiscalId) < 0 ){
            minFiscalId = r.getFiscalNumber();
        }

        kind = r.getClientId().equals("Contado")?"1":"2";
        client = r.getClientId().equals("Contado")?r.getClientId():"";
        z = r.getzReportId();
        printerId = r.getFiscalPrinter();
    }

    public int getSize(){
        return receipts.size();
    }

    public ZSDSCABFACT getHeaderF(){
        ZSDSCABFACT ans = new ZSDSCABFACT();
        String idF = "F" + id;

        String range = minFiscalId + "-" + maxFiscalId;

        ans.setMANDT(of.createZSDSCABDEVMANDT(Constants.mant));
        ans.setFKDAT(of.createZSDSPOSDEVFKDAT(Constants.sdfDay2SAP.format(new GregorianCalendar().getTime())));
        ans.setVBELN(of.createZSDSCABDEVVBELN(idF));
        ans.setZTIPV(of.createZSDSCABDEVZTIPV(kind));
        ans.setKUNNR(of.createZSDSCABDEVKUNNR(client));
        ans.setRANGO(of.createZSDSCABDEVRANGO(range));
        ans.setREPOZ(of.createZSDSCABDEVREPOZ(z));
        ans.setIMPRE(of.createZSDSCABDEVIMPRE(printerId));
        ans.setWAERS(of.createZSDSCABDEVWAERS(Constants.waerks));
        ans.setWERKS(of.createZSDSCABDEVWERKS(Constants.storePrefix+Shared.getConfig("storeName")));
        return ans;
    }

    public ZSDSCABDEV getHeader(){
        ZSDSCABDEV ans = new ZSDSCABDEV();
        String idF = "D" + id;

        String range = minFiscalId + "-" + maxFiscalId;

        ans.setMANDT(of.createZSDSCABDEVMANDT(Constants.mant));
        ans.setFKDAT(of.createZSDSPOSDEVFKDAT(Constants.sdfDay2SAP.format(new GregorianCalendar().getTime())));
        ans.setVBELN(of.createZSDSCABDEVVBELN(idF));
        ans.setZTIPV(of.createZSDSCABDEVZTIPV(kind));
        ans.setKUNNR(of.createZSDSCABDEVKUNNR(client));
        ans.setRANGO(of.createZSDSCABDEVRANGO(range));
        ans.setREPOZ(of.createZSDSCABDEVREPOZ(z));
        ans.setIMPRE(of.createZSDSCABDEVIMPRE(printerId));
        ans.setWAERS(of.createZSDSCABDEVWAERS(Constants.waerks));
        ans.setWERKS(of.createZSDSCABDEVWERKS(Constants.storePrefix+Shared.getConfig("storeName")));
        return ans;
    }

    public List<ZSDSPOSDEV> getDetails(){
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
                if ( item2Receipt.getQuant() - item2Receipt.getAntiQuant() > 0 ){
                    String idd = item2Receipt.getItem().getCode();
                    if ( !units.containsKey(idd) ){
                        units.put(idd, item2Receipt.getItem().getSellUnits());
                        quant.put(idd, item2Receipt.getQuant() - item2Receipt.getAntiQuant());
                        price.put(idd, item2Receipt.getItem().getLastPrice().getQuant());
                        disc.put(idd, item2Receipt.getItem().getDescuento());
                    }else{
                        quant.put(idd, quant.get(idd) + item2Receipt.getQuant() - item2Receipt.getAntiQuant());
                    }
                }
            }
        }
        int position = 1;
        for (Iterator<String> it = (units.keySet().iterator()); it.hasNext();) {
            String idd = it.next();
            ZSDSPOSDEV zdpd = new ZSDSPOSDEV();
            zdpd.setMANDT(of.createZSDSPOSDEVMANDT(Constants.mant));
            zdpd.setFKDAT(of.createZSDSPOSDEVFKDAT(Constants.sdfDay2SAP.format(new GregorianCalendar().getTime())));
            zdpd.setVBELN(of.createZSDSPOSDEVVBELN("D" + id ));
            zdpd.setPOSNR(of.createZSDSPOSDEVPOSNR(Constants.df2intSAP.format(position++)));
            zdpd.setEAN11(of.createZSDSPOSDEVEAN11(idd));
            zdpd.setKWMENG(new BigDecimal(quant.get(idd)));
            zdpd.setVRKME(of.createZSDSPOSDEVVRKME(units.get(idd)));
            zdpd.setCHARG(of.createZSDSPOSDEVCHARG(""));
            zdpd.setKBETP(new BigDecimal(price.get(idd)));
            zdpd.setKBETD(new BigDecimal(disc.get(idd)));
            zdpd.setPERNR(of.createZSDSPOSDEVPERNR("999999"));
            zdpd.setWERKS(of.createZSDSPOSDEVWERKS(Constants.storePrefix+Shared.getConfig("storeName")));
            ans.add(zdpd);
        }
        return ans;
    }

    public List<ZSDSPOSFACT> getDetailsF(){
        List<ZSDSPOSFACT> ans = new LinkedList<ZSDSPOSFACT>();

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
                if ( item2Receipt.getQuant() - item2Receipt.getAntiQuant() > 0 ){
                    String idd = item2Receipt.getItem().getCode();
                    if ( !units.containsKey(idd) ){
                        units.put(idd, item2Receipt.getItem().getSellUnits());
                        quant.put(idd, item2Receipt.getQuant() - item2Receipt.getAntiQuant());
                        price.put(idd, item2Receipt.getItem().getLastPrice().getQuant());
                        disc.put(idd, item2Receipt.getItem().getDescuento());
                    }else{
                        quant.put(idd, quant.get(idd) + item2Receipt.getQuant() - item2Receipt.getAntiQuant());
                    }
                }
            }
        }
        int position = 1;
        for (Iterator<String> it = (units.keySet().iterator()); it.hasNext();) {
            String idd = it.next();
            ZSDSPOSFACT zdpd = new ZSDSPOSFACT();
            zdpd.setMANDT(of.createZSDSPOSDEVMANDT(Constants.mant));
            zdpd.setFKDAT(of.createZSDSPOSDEVFKDAT(Constants.sdfDay2SAP.format(new GregorianCalendar().getTime())));
            zdpd.setVBELN(of.createZSDSPOSDEVVBELN("F" + id ));
            zdpd.setPOSNR(of.createZSDSPOSDEVPOSNR(Constants.df2intSAP.format(position++)));
            zdpd.setEAN11(of.createZSDSPOSDEVEAN11(idd));
            zdpd.setKWMENG(new BigDecimal(quant.get(idd)));
            zdpd.setVRKME(of.createZSDSPOSDEVVRKME(units.get(idd)));
            zdpd.setCHARG(of.createZSDSPOSDEVCHARG(""));
            zdpd.setKBETP(new BigDecimal(price.get(idd)));
            zdpd.setKBETD(new BigDecimal(disc.get(idd)));
            zdpd.setPERNR(of.createZSDSPOSDEVPERNR("999999"));
            zdpd.setWERKS(of.createZSDSCABDEVWERKS(Constants.storePrefix+Shared.getConfig("storeName")));
            ans.add(zdpd);
        }
        return ans;
    }

}
