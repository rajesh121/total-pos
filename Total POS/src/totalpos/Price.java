package totalpos;

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
}
