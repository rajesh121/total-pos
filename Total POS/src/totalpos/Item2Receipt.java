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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item2Receipt other = (Item2Receipt) obj;
        if (this.item != other.item && (this.item == null || !this.item.equals(other.item))) {
            return false;
        }
        return true;
    }
}
