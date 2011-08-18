package totalpos;

import java.sql.Date;

/**
 *
 * @author Saúl Hidalgo
 */
public class Assign {
    private String turn;
    private String pos;
    private Date date;
    private boolean open;

    public Assign(String turn, String pos, Date date, boolean open) {
        this.turn = turn;
        this.pos = pos;
        this.date = date;
        this.open = open;
    }
    public Date getDate() {
        return date;
    }

    public boolean isOpen() {
        return open;
    }

    public String getPos() {
        return pos;
    }

    public String getTurn() {
        return turn;
    }
}
