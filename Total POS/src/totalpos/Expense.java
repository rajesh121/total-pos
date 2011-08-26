package totalpos;

/**
 *
 * @author Saúl Hidalgo
 */
public class Expense {
    private String concept;
    private Double quant;

    public Expense(String concept, Double quant) {
        this.concept = concept;
        this.quant = quant;
    }

    public String getConcept() {
        return concept;
    }

    public Double getQuant() {
        return quant;
    }
}
