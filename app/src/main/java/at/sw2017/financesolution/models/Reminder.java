package at.sw2017.financesolution.models;

import java.util.Date;

/**
 * Created by joe on 06.06.17.
 */

public class Reminder {
    private long id;
    private Date date;
    private String title = "";
    private double amount = 0.0;

    public Reminder()
    {
    }

    public Reminder(Date date, String title, double amount) {
        this.date = date;
        this.title = title;
        this.amount = amount;
    }

    public void setDate(Date date) {this.date = date; }

    public Date getDate() { return this.date; }

    public void setAmount(double amount) {this.amount = amount; }

    public double getAmount()
    {
        return amount;
    }

    public void setTitle(String title) { this.title = title; }

    public String getTitle() {return this.title; }

    public void setId(long id) {this.id = id; }

    public long getId() {return this.id; }
}
