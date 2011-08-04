package totalpos;

import java.util.Date;

/**
 *
 * @author Saúl Hidalgo
 */
public class Assigns {
    private String turn;
    private String pos;
    private Date date;
    private boolean open;
    private double cash;
    private double creditCard;
    private double debitCard;

    public Assigns(String turn, String pos, Date date, boolean open, double cash, double creditCard, double debitCard) {
        this.turn = turn;
        this.pos = pos;
        this.date = date;
        this.open = open;
        this.cash = cash;
        this.creditCard = creditCard;
        this.debitCard = debitCard;
    }

    public double getCash() {
        return cash;
    }

    public double getCreditCard() {
        return creditCard;
    }

    public Date getDate() {
        return date;
    }

    public double getDebitCard() {
        return debitCard;
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
