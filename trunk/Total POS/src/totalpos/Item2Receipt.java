package totalpos;

/**
 *
 * @author Saúl Hidalgo
 */
public class Item2Receipt {
    private Item item;
    private Integer quant;
    private Integer antiQuant;

    public Item2Receipt(Item item, Integer quant, Integer antiQuant) {
        this.item = item;
        this.quant = quant;
        this.antiQuant = antiQuant;
    }

    public Integer getAntiQuant() {
        return antiQuant;
    }

    public Item getItem() {
        return item;
    }

    public Integer getQuant() {
        return quant;
    }

}
