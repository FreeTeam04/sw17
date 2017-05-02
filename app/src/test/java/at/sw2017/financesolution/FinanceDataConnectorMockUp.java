package at.sw2017.financesolution;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hannes on 26.04.17.
 */

public class FinanceDataConnectorMockUp implements FinanceDataConnector {


    private ArrayList<Transaction> transactions = null;

    public void setTransactions(ArrayList<Transaction> transactions)
    {
        this.transactions = transactions;
    }

    public void setupPseudoTransactionsCase1() // mock empty transaction list
    {
        transactions = new ArrayList<Transaction>();
    }

    public void setupPseudoTransactionsCase2()
    {
        Calendar cal = new GregorianCalendar();
        cal.set(2017, 1,1);
        Transaction transaction1 = new Transaction(1, cal.getTime(), 1, "pseudo",100.0);

        cal.set(2017, 4,17);
        Transaction transaction2 = new Transaction(2, cal.getTime(), 1, "pseudo2",-50.0);

        this.transactions = new ArrayList<Transaction>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        
    }

    @Override
    public ArrayList<Transaction> getAllTransactions() {
        return this.transactions;
    }
}
