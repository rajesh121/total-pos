package totalpos;

/**
 *
 * @author Saúl Hidalgo
 */
public class Item2Receipt {
    private Item item;
    private Integer quant;

    public Item2Receipt(Item item, Integer quant) {
        this.item = item;
        this.quant = quant;
    }

    public Item getItem() {
        return item;
    }

    public Integer getQuant() {
        return quant;
    }

}
