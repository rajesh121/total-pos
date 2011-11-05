package totalpos;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author shidalgo
 */
public class Cost implements Serializable {
    private Date date;
    private Double quant;

    public Cost(Date date, Double quant) {
        this.date = date;
        this.quant = quant;
    }

    public Date getDate() {
        return date;
    }

    public Double getQuant() {
        return quant;
    }

    public Cost newest(Cost o){
        if ( getDate().before(o.getDate()) ){
            return o;
        }
        return this;
    }

    @Override
    public String toString() {
        return quant + "";
    }

}
