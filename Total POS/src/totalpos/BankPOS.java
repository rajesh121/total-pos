package totalpos;

/**
 *
 * @author Saúl Hidalgo
 */
public class BankPOS {
    private String id;
    private String descripcion;
    private String lot;
    private String posId;
    private String kind;

    public BankPOS(String id, String descripcion, String lot, String posId, String kind) {
        this.id = id;
        this.descripcion = descripcion;
        this.lot = lot;
        this.posId = posId;
        this.kind = kind;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }

    public String getLot() {
        return lot;
    }

    public String getPosId() {
        return posId;
    }
}
