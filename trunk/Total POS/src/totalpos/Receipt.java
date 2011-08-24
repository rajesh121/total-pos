package totalpos;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author Saúl Hidalgo xD
 */
public class Receipt {
    private String internId;
    private String status;
    private Date creationDate;
    private Date printingDate;
    private String clientId;
    private Double totalWithoutIva;
    private Double totalWithIva;
    private Double globalDiscount;
    private Double iva;
    private String fiscalPrinter;
    private String fiscalNumber;
    private String zReportId;
    private String userCodeId;
    private Integer numberItems;
    private List<Item2Receipt> items;
    private String turn;

    public Receipt(String internId, String status, Date creationDate,
            Date printingDate, String clientId, Double totalWithoutIva,
            Double totalWithIva, Double globalDiscount, Double iva,
            String fiscalPrinter, String fiscalNumber, String zReportId,
            String userCodeId, Integer numberItems, List<Item2Receipt> items,
            String turn) {
        this.internId = internId;
        this.status = status;
        this.creationDate = creationDate;
        this.printingDate = printingDate;
        this.clientId = clientId;
        this.totalWithoutIva = totalWithoutIva;
        this.totalWithIva = totalWithIva;
        this.globalDiscount = globalDiscount;
        this.iva = iva;
        this.fiscalPrinter = fiscalPrinter;
        this.fiscalNumber = fiscalNumber;
        this.zReportId = zReportId;
        this.userCodeId = userCodeId;
        this.numberItems = numberItems;
        this.items = items;
        this.turn = turn;
    }

    public String getClientId() {
        return clientId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getFiscalNumber() {
        return fiscalNumber;
    }

    public String getFiscalPrinter() {
        return fiscalPrinter;
    }

    public Double getGlobalDiscount() {
        return globalDiscount;
    }

    public String getInternId() {
        return internId;
    }

    public List<Item2Receipt> getItems() {
        return items;
    }

    public Double getIva() {
        return iva;
    }

    public Integer getNumberItems() {
        return numberItems;
    }

    public Date getPrintingDate() {
        return printingDate;
    }

    public String getStatus() {
        return status;
    }

    public Double getTotalWithIva() {
        return totalWithIva;
    }

    public Double getTotalWithoutIva() {
        return totalWithoutIva;
    }

    public String getUserCodeId() {
        return userCodeId;
    }

    public String getzReportId() {
        return zReportId;
    }

    public String getTurn() {
        return turn;
    }

}
