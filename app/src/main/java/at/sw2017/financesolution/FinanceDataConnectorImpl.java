package at.sw2017.financesolution;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FinanceDataConnectorImpl extends SQLiteOpenHelper implements FinanceDataConnector {

    // Logcat tag
    private static final String LOG = "FinanceDataConnector";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "FinanceDB";

    // Table Names
    private static final String TABLE_TRANSACTIONS = "Transactions";
    private static final String TABLE_CATEGORIES = "Categories";

    // Common column names
    private static final String KEY_ID = "id";

    // CATEGORIES Table - column names
    private static final String KEY_NAME = "name";

    // TRANSACTION Table - column names
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE = "date";
    private static final String KEY_DESCRIPTION = "description";

    private String dataDirectory = "";

    public void setDatadirectory(String dataDir)
    {
        this.dataDirectory = dataDir;
    }
    // CREATE TABLE t(x INTEGER, y, z, PRIMARY KEY(x ASC));
    private String createStatementCategory = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "(" +
    KEY_ID + " INTEGER, " + KEY_NAME + " TEXT, PRIMARY KEY(" + KEY_ID + " ASC));";

    private String createStatementTransaction = "CREATE TABLE IF NOT EXISTS "+ TABLE_TRANSACTIONS + "(" +
            KEY_ID + " INTEGER, " +
            KEY_CATEGORY_ID + " INTEGER, " +
            KEY_DATE + " TEXT, " +
            KEY_AMOUNT + " DECIMAL(10,5), " +
            KEY_DESCRIPTION + " TEXT, " +
            "PRIMARY KEY(" + KEY_ID + " ASC), " +
            " FOREIGN KEY ("+KEY_CATEGORY_ID+") REFERENCES Categories("+ KEY_ID + "));";


    private SQLiteDatabase database;

    private static FinanceDataConnector instance;


    private List<Category> initialCategories =  Arrays.asList(
        new Category("Transportation", 1),
        new Category("Traveling", 2),
        new Category("Housing & Living", 3),
        new Category("Food & Drinks", 4),
        new Category("Clothing", 5),
        new Category("Entertainment", 6),
        new Category("Hobbies", 7));





    public static FinanceDataConnector getInstance(Context context) {
        if (instance == null) {
            instance = new FinanceDataConnectorImpl(context);
        }

        return instance;
    }

    private FinanceDataConnectorImpl(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    public long createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());

        // insert row
        long category_id = db.insert(TABLE_CATEGORIES, null, values);

        return category_id;
    }

    public long createTransaction(Transaction transaction, long category_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, transaction.getDescription());
        values.put(KEY_AMOUNT, transaction.getAmount());
        values.put(KEY_DATE, convertDateToDBDate(transaction.getDate()));
        values.put(KEY_CATEGORY_ID, transaction.getCategoryID());

        // insert row
        long transaction_id = db.insert(TABLE_TRANSACTIONS, null, values);

        return transaction_id;
    }

    public Transaction getTransaction(long transaction_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE "
                + KEY_ID + " = " + transaction_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Transaction ts = new Transaction();
        ts.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        ts.setAmount(c.getInt(c.getColumnIndex(KEY_AMOUNT)));
        ts.setCategoryID(c.getInt(c.getColumnIndex(KEY_CATEGORY_ID)));
        ts.setDate(convertDBDateToDate(c.getString(c.getColumnIndex(KEY_DATE))));

        return ts;
    }

    public Category getCategory(long category_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE "
                + KEY_ID + " = " + category_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Category cg = new Category();
        cg.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        cg.setName((c.getString(c.getColumnIndex(KEY_NAME))));

        return cg;
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
    public void createInitialCategories(){

        SQLiteDatabase db = this.getWritableDatabase();
        String args[] = {};

        for (int i=0; i<initialCategories.size(); ++i) {
            String name = initialCategories.get(i).getName();
            long id = initialCategories.get(i).getDBID();

            String query = "INSERT INTO " + TABLE_CATEGORIES + "(" + KEY_ID + "," + KEY_NAME + ")" +
                    " SELECT " +  id + ", '" + name + "'" +
                    " WHERE NOT EXISTS(SELECT 1 FROM " + TABLE_CATEGORIES +
                    " WHERE " + KEY_ID +" = "+ id + ");";
            //db.rawQuery(query, args);
            db.execSQL(query);
        }
    }

    @Override
    public ArrayList<Category> getAllCategories() {

        createInitialCategories();

        String queryAllCategories = "SELECT * FROM Categories;";
        String args[] = {};
        Cursor cursor = this.getReadableDatabase().rawQuery(queryAllCategories, args);

        ArrayList<Category> al = new ArrayList<Category>();
        cursor.moveToNext();
        while(!cursor.isAfterLast()) {
            Category cat = new Category();
            cat.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            cat.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));

            al.add(cat);
            cursor.moveToNext();
        }

        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create database tables
        db.execSQL(createStatementCategory);
        db.execSQL(createStatementTransaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        // create new tables
        onCreate(db);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
