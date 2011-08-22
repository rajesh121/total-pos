package totalpos;

/**
 *
 * @author Saúl Hidalgo
 */
public class BankPOS {
    private String id;
    private String descripcion;
    private String lotes;

    public BankPOS(String id, String descripcion, String lotes) {
        this.id = id;
        this.descripcion = descripcion;
        this.lotes = lotes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getId() {
        return id;
    }

    public String getLotes() {
        return lotes;
    }

}
