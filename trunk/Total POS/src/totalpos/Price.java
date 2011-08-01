package totalpos;

import java.text.DecimalFormat;
import java.util.Date;

/**
 *
 * @author shidalgo
 */
public class Price {
    private Date date;
    private Double quant;

    public Price(Date date, Double quant) {
        this.date = date;
        this.quant = quant;
    }

    public Price(Price p){
        this.date = p.getDate();
        this.quant = p.getQuant();
    }

    public Date getDate() {
        return date;
    }

    public Double getQuant() {
        return quant;
    }

    public Price newest(Price o){
        if ( getDate().before(o.getDate()) ){
            return o;
        }
        return this;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(quant) + " Bsf";
    }

}
