package at.sw2017.financesolution.models;

/**
 * Created by Hannes on 07.05.2017.
 */

public class Category {
    private String name;
    private long id;

    public Category()
    {
    }

    public Category(String categoryName, int dbID)
    {
        this.name = categoryName;
        this.id = dbID;
    }

    public void setId(long id) {this.id = id; }

    public long getDBID()
    {
        return this.id;
    }

    public void setName(String name) {this.name = name; }

    public String getName()
    {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
