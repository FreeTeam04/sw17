package at.sw2017.financesolution;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.models.Category;
import at.sw2017.financesolution.models.Transaction;

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
        Category category = new Category("Auto",1);

        Calendar cal = new GregorianCalendar();
        cal.set(2017, 1,1);

        Transaction transaction1 = new Transaction(cal.getTime(), category, "pseudo", 100.0);

        cal.set(2017, 4,17);
        Transaction transaction2 = new Transaction(cal.getTime(), category, "pseudo2", -50.0);

        this.transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
    }

    @Override
    public long createCategory(Category category) {
        return 0;
    }

    @Override
    public long createTransaction(Transaction transaction) {
        return 0;
    }

    @Override
    public long updateTransaction(Transaction transaction) {
        return 0;
    }

    @Override
    public Transaction getTransaction(long id) {
        return new Transaction();
    }

    @Override
    public ArrayList<Transaction> getAllTransactions() {
        return this.transactions;
    }

    @Override
    public ArrayList<Category> getAllCategories() {
        return null;
    }


    @Override
    public void removeTransaction(Transaction transaction) {

    }

    @Override
    public ArrayList<Transaction> getLastTransactions(int number) {
        return null;
    }

    @Override
    public void createInitialCategories() {

    }

    @Override
    public Date convertDBDateToDate(String ISO8601Date) {
        return null;
    }

    @Override
    public String convertDateToDBDate(Date date) {
        return null;
    }
    
}
