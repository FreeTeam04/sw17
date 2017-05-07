package at.sw2017.financesolution;

/**
 * Created by Hannes on 07.05.2017.
 */

public class Category {
    private String name;
    private int id;

    public Category(String categoryName, int dbID)
    {
        this.name = categoryName;
        this.id = dbID;
    }

    public int getDBID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }
}
