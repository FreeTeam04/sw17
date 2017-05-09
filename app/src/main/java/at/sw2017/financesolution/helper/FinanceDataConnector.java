package at.sw2017.financesolution.helper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import at.sw2017.financesolution.models.Category;
import at.sw2017.financesolution.models.Transaction;

/**
 * Created by hannes on 26.04.17.
 */

public interface FinanceDataConnector {

    ArrayList<Transaction> getAllTransactions();

    ArrayList<Category> getAllCategories();

    void createInitialCategories();

    // void addTransaction(Transaction transaction);

    void removeTransaction(Transaction transaction);

    ArrayList<Transaction> getLastTransactions(int number);

    Date convertDBDateToDate(String ISO8601Date);

    String convertDateToDBDate(Date date);

    // void setDatadirectory(String dataDir);

}
