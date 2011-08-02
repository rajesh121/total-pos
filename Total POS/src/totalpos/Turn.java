package totalpos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author shidalgo
 */
public class Turn {
    private String username;
    private String pos;
    private Double cash;
    private boolean abierto;
    private String day;

    public Turn(String username, String pos, Double cash, boolean abierto) {
        this.username = username;
        this.pos = pos;
        this.cash = cash;
        this.abierto = abierto;
    }

    public Turn(String username, String pos, Double cash, boolean abierto, String day) {
        this.username = username;
        this.pos = pos;
        this.cash = cash;
        this.abierto = abierto;
        this.day = day;
    }

    public boolean isAbierto() {
        return abierto;
    }

    public Double getCash() {
        return cash;
    }

    public String getDay() {
        return day;
    }

    public String getPos() {
        return pos;
    }

    public String getUsername() {
        return username;
    }

}
