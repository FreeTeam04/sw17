package at.sw2017.financesolution.models;

import java.util.Date;

public class Transaction {
    private long id;
    private Category category;
    private Date creationDate;
    private String description = "";
    private double amount = 0.0;
    private long categoryID;
    private String photoPath = "";

    public Transaction()
    {
    }

    public Transaction(Date date, Category category, String description, double amount, String photoPath) {
        this.creationDate = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.photoPath = photoPath;
    }

    public void setPhotoPath(String photoPath) {this.photoPath = photoPath;}

    public String getPhotoPath() {return photoPath;}

    public void setDate(Date date) {this.creationDate = date; }

    public Date getDate() { return creationDate; }

    public void setAmount(double amount) {this.amount = amount; }

    public double getAmount()
    {
        return amount;
    }

    public void setDescription(String description) { this.description = description; }

    public String getDescription() {return this.description; }

    public void setCategory(Category category) {this.category = category; }

    public Category getCategory() { return this.category; }

    public void setCategoryID(long id) { this.categoryID = id; }

    public long getCategoryID() { return this.categoryID; }

    public void setId(long id) {this.id = id; }

    public long getId() {return this.id; }

}
