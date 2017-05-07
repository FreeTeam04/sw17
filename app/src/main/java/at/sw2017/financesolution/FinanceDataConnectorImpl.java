package at.sw2017.financesolution;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FinanceDataConnectorImpl implements FinanceDataConnector {

    private String databaseName = "FinanceDB";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE = "transaction_date";
    private static final String KEY_DESCRIPTION = "description";

    private String dataDirectory = "";

    public void setDatadirectory(String dataDir)
    {
        this.dataDirectory = dataDir;
    }
    // CREATE TABLE t(x INTEGER, y, z, PRIMARY KEY(x ASC));
    private String createStatementCategory = "CREATE TABLE IF NOT EXISTS Categories(" +
    KEY_ID + " INTEGER, " + KEY_NAME + " TEXT, PRIMARY KEY(" + KEY_ID + " ASC));";

    private String createStatementTransaction = "CREATE TABLE IF NOT EXISTS Transactions(" +
            KEY_ID + " INTEGER, " +
            KEY_CATEGORY_ID + " INTEGER, " +
            KEY_DATE + " TEXT, " +
            KEY_AMOUNT + " DECIMAL(10,5), " +
            KEY_DESCRIPTION + " TEXT, " +
            "PRIMARY KEY(" + KEY_ID + " ASC), " +
            " FOREIGN KEY ("+KEY_CATEGORY_ID+") REFERENCES Categories("+ KEY_ID + "));";


    private SQLiteDatabase database;

    private static FinanceDataConnector instance;

    private FinanceDataConnectorImpl() {
    }

    public static FinanceDataConnector getInstance() {
        if (instance == null) {
            instance = new FinanceDataConnectorImpl();
        }

        return instance;
    }


    @Override
    public void addTransaction(Transaction transaction)
    {
        String query = "INSERT INTO Transactions(" + KEY_CATEGORY_ID + "," + KEY_DATE + "," +
                KEY_AMOUNT + "," + KEY_DESCRIPTION + ") VALUES (" + transaction.getCategory().getDBID() + ","
                + "'" + this.convertDateToDBDate(transaction.getDate()) +"'," + transaction.getAmount() + ",'" +
                transaction.getDescription() + "');";

        this.database.beginTransaction();

        try {
            this.database.execSQL(query);
            this.database.setTransactionSuccessful();
        } catch (SQLException sqlEx) {
            Log.d("FinanaceDataConnection", sqlEx.getMessage());
        } finally {
            this.database.endTransaction();
        }

    }

    @Override
    public void removeTransaction(Transaction transaction) {

        // rawQuery("SELECT id, name FROM people WHERE name = ? AND id = ?", new String[] {"David", "2"});


    }

    public Date convertDBDateToDate(String ISO8601Date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        try {
            Date d = sdf.parse(ISO8601Date);
            return d;
        } catch (ParseException ex) {
            Log.d("FinanceDataConnector", ex.getMessage() + "\n\n" + ex.getStackTrace());
        }
        return null;
    }

    public String convertDateToDBDate(Date date)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return df.format(date);
    }

    @Override
    public ArrayList<Transaction> getAllTransactions() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public ArrayList<Category> getAllCategories() {
        String queryAllCategories = "SELECT * FROM Categories;";
        String args[] = {};
        this.database.rawQuery(queryAllCategories,args);
        return null;
    }

    @Override
    public boolean createOrOpenDatabase() {

        database = SQLiteDatabase.openOrCreateDatabase(this.dataDirectory + "/" + databaseName, null);

        // Check tables exist
        database.execSQL(createStatementCategory);
        database.execSQL(createStatementTransaction);

        // TEXT as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
        return database.isOpen();
    }




}
