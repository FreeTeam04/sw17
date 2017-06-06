package at.sw2017.financesolution.helper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import at.sw2017.financesolution.models.Category;
import at.sw2017.financesolution.models.Reminder;
import at.sw2017.financesolution.models.Transaction;

/**
 * Created by hannes on 26.04.17.
 */

public interface FinanceDataConnector {

    boolean clearDatabaseContent();

    long createCategory(Category category);

    long createTransaction(Transaction transaction);

    long updateTransaction(Transaction transaction);

    long createReminder(Reminder reminder);

    long updateReminder(Reminder reminder);

    ArrayList<Transaction> getAllTransactions();

    ArrayList<Category> getAllCategories();

    ArrayList<Reminder> getAllReminders();

    void createInitialCategories();

    // void addTransaction(Transaction transaction);

    void removeTransaction(Transaction transaction);

    void removeReminder(Reminder reminder);

    Transaction getTransaction(long transaction_id);

    Reminder getReminder(long reminder_id);

    Category getCategory(long category_id);

    ArrayList<Transaction> getLastTransactions(int number);

    Date convertDBDateToDate(String ISO8601Date);

    String convertDateToDBDate(Date date);

    // void setDatadirectory(String dataDir);

    Map<String, Float> getSpendingPerCategoryForCurrentMonth();

    Map<String, Float> getSpendingPerCategoryForCurrentYear();

}
