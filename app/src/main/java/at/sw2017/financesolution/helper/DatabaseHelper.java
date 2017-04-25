package at.sw2017.financesolution.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.sw2017.financesolution.model.Category;
import at.sw2017.financesolution.model.Transaction;

/**
 * Created by fabiowallner on 25/04/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "financeSolution";

    // Table Names
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String TABLE_CATEGORIES = "categories";

    // Common column names
    private static final String KEY_ID = "id";

    // TRANSACTION Table - column names
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_AMOUNT = "amount";

    // CATEGORIES Table - column names
    private static final String KEY_NAME = "name";

    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE "
            + TABLE_TRANSACTIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORY_ID
            + " INTEGER," + KEY_DATE + " DATE," + KEY_DESCRIPTION
            + " TEXT," + KEY_AMOUNT + " INTEGER" + ")";

    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_TRANSACTIONS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
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

    /*
     * Creating a category
    */
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
        values.put(KEY_DATE, getDateAsString(transaction.getDate()));
        values.put(KEY_CATEGORY_ID, transaction.getCategory_id());

        // insert row
        long transaction_id = db.insert(TABLE_TRANSACTIONS, null, values);

        return transaction_id;
    }

    /*
     * get single category
    */
    public Category getCategory(long category_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE "
                + KEY_ID + " = " + category_id;

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Category cg = new Category();
        cg.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        cg.setName((c.getString(c.getColumnIndex(KEY_NAME))));

        return cg;
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
        ts.setCategory_id(c.getInt(c.getColumnIndex(KEY_CATEGORY_ID)));
        ts.setDate(getDateFromString(c.getString(c.getColumnIndex(KEY_DATE))));

        return ts;
    }

    private String getDateAsString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    private Date getDateFromString(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date returnValue;
        try {
            returnValue = dateFormat.parse(date);
        }
        catch (ParseException ex) {
            returnValue = null;
        }
        return returnValue;
    }
}
