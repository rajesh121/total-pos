package totalpos;

import java.sql.Date;

/**
 *
 * @author Saúl Hidalgo
 */
public class Deposit {
    private String bank;
    private String formId;
    private String cataport;
    private Double quant;

    public Deposit(String bank, String formId, String cataport, Double quant) {
        this.bank = bank;
        this.formId = formId;
        this.cataport = cataport;
        this.quant = quant;
    }

    public String getBank() {
        return bank;
    }

    public String getCataport() {
        return cataport;
    }

    public String getFormId() {
        return formId;
    }

    public Double getQuant() {
        return quant;
    }

}
