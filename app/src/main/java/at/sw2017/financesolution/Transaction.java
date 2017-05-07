package at.sw2017.financesolution;

import java.util.Date;

public class Transaction {
    private int id;
    private Category category;
    private Date creationDate;
    private String description = "";
    private double amount = 0.0;

    public Transaction(Date date, Category category, String description, double amount) {
        this.creationDate = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    public Date getDate() { return creationDate; }

    public double getAmount()
    {
        return amount;
    }

    public String getDescription() {return this.description; }

    public Category getCategory() { return this.category; }

}
