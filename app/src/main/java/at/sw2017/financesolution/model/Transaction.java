package at.sw2017.financesolution.model;

import java.util.Date;

/**
 * Created by fabiowallner on 25/04/2017.
 */

public class Transaction {

    int id;
    int category_id;
    Date date;
    String description;
    double amount;

    public Transaction() {

    }

    public Transaction(int category_id, Date date, String description, double amount) {
        this.category_id = category_id;
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
