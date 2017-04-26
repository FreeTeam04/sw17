package at.sw2017.financesolution;

import java.util.Date;

/**
 * Created by hannes on 26.04.17.
 */

public class Transaction {
    private int id;

    double amount = 0.0;

    public Transaction(int id, Date date, int category, String description, double amount)
    {

    }

    public double getAmount()
    {
        return amount;
    }


}
