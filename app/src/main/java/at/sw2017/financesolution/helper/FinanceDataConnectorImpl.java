package at.sw2017.financesolution.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.sw2017.financesolution.models.Category;
import at.sw2017.financesolution.models.Transaction;

public class FinanceDataConnectorImpl extends SQLiteOpenHelper implements FinanceDataConnector {

    // Logcat tag
    private static final String LOG = "FinanceDataConnector";

    // Database Version
    private static final int DATABASE_VERSION = 3;

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
    private static final String KEY_PHOTO_PATH = "photo";
    

    // CREATE TABLE t(x INTEGER, y, z, PRIMARY KEY(x ASC));
    private String createStatementCategory = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "(" +
    KEY_ID + " INTEGER, " + KEY_NAME + " TEXT, PRIMARY KEY(" + KEY_ID + " ASC));";

    private String createStatementTransaction = "CREATE TABLE IF NOT EXISTS "+ TABLE_TRANSACTIONS + "(" +
            KEY_ID + " INTEGER, " +
            KEY_CATEGORY_ID + " INTEGER, " +
            KEY_DATE + " TEXT, " +
            KEY_AMOUNT + " DECIMAL(10,5), " +
            KEY_DESCRIPTION + " TEXT, " +
            KEY_PHOTO_PATH + " TEXT, "  +
            "PRIMARY KEY(" + KEY_ID + " ASC), " +
            " FOREIGN KEY ("+KEY_CATEGORY_ID+") REFERENCES Categories("+ KEY_ID + "));";


    // Delete table content statements
    private String deleteTransactionsContent = "DELETE FROM " + TABLE_TRANSACTIONS + ";";
    private String deleteCategoriesContent = "DELETE FROM " + TABLE_CATEGORIES + ";";


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


    @Override
    public boolean clearDatabaseContent() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(deleteTransactionsContent);
            db.close();
            db = this.getWritableDatabase();
            db.execSQL(deleteCategoriesContent);
            db.close();
        } catch (SQLiteException sqLiteExcpetion) {
            Log.d(LOG, sqLiteExcpetion.getMessage());
            return false;
        }
        return true;
    }

    public long createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());

        // insert row
        long category_id = db.insert(TABLE_CATEGORIES, null, values);

        return category_id;
    }

    public long createTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, transaction.getDescription());
        values.put(KEY_AMOUNT, transaction.getAmount());
        values.put(KEY_DATE, convertDateToDBDate(transaction.getDate()));
        values.put(KEY_CATEGORY_ID, transaction.getCategoryID());
        values.put(KEY_PHOTO_PATH, transaction.getPhotoPath());
        // insert row
        long transaction_id = db.insert(TABLE_TRANSACTIONS, null, values);

        return transaction_id;
    }

    @Override
    public long updateTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, transaction.getDescription());
        values.put(KEY_AMOUNT, transaction.getAmount());
        values.put(KEY_DATE, convertDateToDBDate(transaction.getDate()));
        values.put(KEY_CATEGORY_ID, transaction.getCategoryID());
        values.put(KEY_PHOTO_PATH, transaction.getPhotoPath());

        // Update row
        // https://stackoverflow.com/a/18390848
        long transaction_id = db.update(TABLE_TRANSACTIONS, values, "id="+transaction.getId(), null);

        return transaction_id;
    }

    @Override
    public Transaction getTransaction(long transaction_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE "
                + KEY_ID + " = " + transaction_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        //Category category = new Category();
        //category = getCategory(c.getLong(c.getColumnIndex(KEY_CATEGORY_ID)));

        Transaction ts = new Transaction();
        ts.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        ts.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
        ts.setAmount(c.getInt(c.getColumnIndex(KEY_AMOUNT)));
        //ts.setCategory(category);
        ts.setCategoryID(c.getInt(c.getColumnIndex(KEY_CATEGORY_ID)));
        ts.setDate(convertDBDateToDate(c.getString(c.getColumnIndex(KEY_DATE))));
        ts.setPhotoPath(c.getString(c.getColumnIndex(KEY_PHOTO_PATH)));

        return ts;
    }

    @Override
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
    public Map<String, Float> getSpendingPerCategoryForCurrentMonth() {

        Map<String, Float> spendingsInMonth = new HashMap<>();

        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);

        ArrayList<Transaction> transactions =  getTransactionsOfMonth(month);

        for(int i = 0; i < transactions.size(); i++)
        {
            Category category = transactions.get(i).getCategory();

            if(spendingsInMonth.containsKey(category.getName()))
            {
                Float spending = spendingsInMonth.get(category.getName());
                Float amount = Float.valueOf((float)transactions.get(i).getAmount());
                if (amount < 0) // if it is a spending
                {
                    spending += amount;
                    spendingsInMonth.put(category.getName(),spending);
                }
            }
            else
            {
                Float spending = Float.valueOf((float)transactions.get(i).getAmount());
                spendingsInMonth.put(category.getName(),spending);
            }
        }

        return spendingsInMonth;
    }

    @Override
    public Map<String, Float> getSpendingPerCategoryForCurrentYear() {
        Map<String, Float> spendingsInYear = new HashMap<>();

        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);

        ArrayList<Transaction> transactions =  getTransactionsOfYear(year);

        for(int i = 0; i < transactions.size(); i++)
        {
            String category = transactions.get(i).getCategory().getName();

            if(spendingsInYear.containsKey(category))
            {
                Float spending = spendingsInYear.get(category);
                Float amount = Float.valueOf((float)transactions.get(i).getAmount());
                if (amount < 0) // if it is a spending
                {
                    spending += amount;
                    spendingsInYear.put(category,spending);
                }
            }
            else
            {
                Float spending = Float.valueOf((float)transactions.get(i).getAmount());
                spendingsInYear.put(category,spending);
            }
        }

        return spendingsInYear;
    }

    @Override
    public ArrayList<Transaction> getAllTransactions() {
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + ";";
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);

        ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();
        cursor.moveToNext();
        while(!cursor.isAfterLast()) {
            Transaction transaction = new Transaction();
            transaction.setDate(convertDBDateToDate(cursor.getString(cursor.getColumnIndex(KEY_DATE))));
            transaction.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
            transaction.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
            transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT)));
            transaction.setCategoryID(cursor.getLong(cursor.getColumnIndex(KEY_CATEGORY_ID)));
            transaction.setPhotoPath(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_PATH)));

            Category category = getCategory(transaction.getCategoryID());
            transaction.setCategory(category);

            transactionsList.add(transaction);
            cursor.moveToNext();
        }

        return transactionsList;
    }

    public ArrayList<Transaction> getTransactionsOfMonth(int month) {
        ArrayList<Transaction> results = new ArrayList<>();
        ArrayList<Transaction> transactions = getAllTransactions();
        Calendar calBefore = Calendar.getInstance();
        Calendar calAfter  = Calendar.getInstance();

        for (Transaction t : transactions) {
            calBefore.set(Calendar.MONTH, month);
            calBefore.set(Calendar.DAY_OF_YEAR, 1);
            calBefore.add(Calendar.DAY_OF_YEAR, -1);

            calAfter.set(Calendar.MONTH, month+1);
            calAfter.set(Calendar.DAY_OF_YEAR, 1);

            if (t.getDate().after(calBefore.getTime()) && t.getDate().before(calAfter.getTime())) {
                results.add(t);
            }
        }
        return results;
    }

    public ArrayList<Transaction> getTransactionsOfYear(int year) {
        ArrayList<Transaction> results = new ArrayList<>();
        ArrayList<Transaction> transactions = getAllTransactions();

        Calendar calBefore = Calendar.getInstance();
        Calendar calAfter  = Calendar.getInstance();

        calBefore.set(Calendar.YEAR, year);
        calBefore.set(Calendar.DAY_OF_YEAR, 1);
        calBefore.add(Calendar.DAY_OF_YEAR, -1);

        calAfter.set(Calendar.YEAR, year+1);
        calAfter.set(Calendar.DAY_OF_YEAR, 1);

        for (Transaction t : transactions) {
            if (t.getDate().after(calBefore.getTime()) && t.getDate().before(calAfter.getTime())) {
                results.add(t);
            }
        }
        return results;
    }

    public ArrayList<Transaction> getLastTransactions(int number) {

        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " LIMIT " + number + ";";
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);

        ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();
        cursor.moveToNext();
        while(!cursor.isAfterLast()) {
            Transaction transaction = new Transaction();
            transaction.setDate(convertDBDateToDate(cursor.getString(cursor.getColumnIndex(KEY_DATE))));
            transaction.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
            transaction.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
            transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT)));
            transaction.setCategoryID(cursor.getLong(cursor.getColumnIndex(KEY_CATEGORY_ID)));
            transaction.setPhotoPath(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_PATH)));

            Category category = getCategory(transaction.getCategoryID());
            transaction.setCategory(category);

            transactionsList.add(transaction);
            cursor.moveToNext();
        }

        return transactionsList;
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

        ArrayList<Category> categoriesList = new ArrayList<Category>();
        cursor.moveToNext();
        while(!cursor.isAfterLast()) {
            Category newCategory = new Category();
            newCategory.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            newCategory.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));

            categoriesList.add(newCategory);
            cursor.moveToNext();
        }

        return categoriesList;
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
