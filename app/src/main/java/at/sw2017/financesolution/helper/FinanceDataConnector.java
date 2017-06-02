package at.sw2017.financesolution.helper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import at.sw2017.financesolution.models.Category;
import at.sw2017.financesolution.models.Transaction;

/**
 * Created by hannes on 26.04.17.
 */

public interface FinanceDataConnector {

    long createCategory(Category category);

    long createTransaction(Transaction transaction);

    long updateTransaction(Transaction transaction);

    ArrayList<Transaction> getAllTransactions();

    ArrayList<Category> getAllCategories();

    void createInitialCategories();

    // void addTransaction(Transaction transaction);

    void removeTransaction(Transaction transaction);

    Transaction getTransaction(long transaction_id);

    Category getCategory(long category_id);

    ArrayList<Transaction> getLastTransactions(int number);

    Date convertDBDateToDate(String ISO8601Date);

    String convertDateToDBDate(Date date);

    // void setDatadirectory(String dataDir);

    Map<String, Float> getSpendingPerCategoryForCurrentMonth();

    Map<String, Float> getSpendingPerCategoryForCurrentYear();

}
