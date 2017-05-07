package at.sw2017.financesolution;

import android.util.Log;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hannes on 26.04.17.
 */

public interface FinanceDataConnector {

    ArrayList<Transaction> getAllTransactions();

    ArrayList<Category> getAllCategories();

    boolean createOrOpenDatabase();

    void addTransaction(Transaction transaction);

    void removeTransaction(Transaction transaction);

    Date convertDBDateToDate(String ISO8601Date);

    String convertDateToDBDate(Date date);

    void setDatadirectory(String dataDir);

}
