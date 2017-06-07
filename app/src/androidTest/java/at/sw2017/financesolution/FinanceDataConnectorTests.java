package at.sw2017.financesolution;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContext;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;
import at.sw2017.financesolution.models.Category;
import at.sw2017.financesolution.models.Reminder;
import at.sw2017.financesolution.models.Transaction;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FinanceDataConnectorTests {

    // Test 1: Check database is createable
    // Test 2: Check database content is deleteable
    
    @Test
    public void checkDatabaseRetrieveable() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);

        assertNotNull(fdc);
    }

    @Test
    public void checkClearDatabaseReturnValue() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        boolean retValue = fdc.clearDatabaseContent();
        assertEquals(true, retValue);
    }

    @Test
    public void checkDatabaseClearDeletesTransactions()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);

        fdc.clearDatabaseContent();

        List<Transaction> transactions = fdc.getAllTransactions();
        assertEquals(0, transactions.size());
    }

    @Test
    public void checkDatabaseClearDeletesReminders()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);

        fdc.clearDatabaseContent();

        List<Reminder> reminders = fdc.getAllReminders();
        assertEquals(0, reminders.size());
    }

    /*
    @Test
    public void checkDatabaseClearException()
    {
        Context appContext = new MockContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);

        boolean success = fdc.clearDatabaseContent();

        assertFalse(success);

    }
    */
    @Test
    public void checkDatabaseOnCreate()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        FinanceDataConnectorImpl fdcl = (FinanceDataConnectorImpl)fdc;
        SQLiteDatabase db = fdcl.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Transactions");
        db.execSQL("DROP TABLE IF EXISTS Categories");
        db.execSQL("DROP TABLE IF EXISTS Reminders");
        fdcl.onCreate(db);
        boolean found = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'Transactions'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                found = true;
            }
            cursor.close();
        }

        assertTrue(found);

    }

    public void checkDatabaseOnUpgrade()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        FinanceDataConnectorImpl fdcl = (FinanceDataConnectorImpl)fdc;
        SQLiteDatabase db = fdcl.getWritableDatabase();
        fdcl.onUpgrade(db, 1,2);
        boolean found = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'Transactions'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                found = true;
            }
            cursor.close();
        }

        assertTrue(found);

    }

    @Test
    public void checkDatabaseClearDeletesCategories()
    {
        final int initialCategoryCount = 7;

        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);

        fdc.clearDatabaseContent();

        List<Category> categories = fdc.getAllCategories();

        // compare with initial category count since initial categories will always be recreated
        assertEquals(initialCategoryCount, categories.size());
    }

    @Test
    public void checkInitialCategoriesAreCreated()
    {
        List<Category> initialCategories =  Arrays.asList(
                new Category("Transportation", 1),
                new Category("Traveling", 2),
                new Category("Housing & Living", 3),
                new Category("Food & Drinks", 4),
                new Category("Clothing", 5),
                new Category("Entertainment", 6),
                new Category("Hobbies", 7));

        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        fdc.clearDatabaseContent();
        List<Category> existingCategories = fdc.getAllCategories();

        for(int i = 0; i < initialCategories.size(); i++)
        {
            boolean found = false;

            for (int j = 0; j < existingCategories.size(); j++)
            {
                if (initialCategories.get(i).getDBID() == existingCategories.get(j).getDBID() &&
                    initialCategories.get(i).getName().compareTo(existingCategories.get(j).getName()) == 0)
                {
                    found = true;
                }
            }
            assertEquals(true, found);
        }
    }

    @Test
    public void checkCreateCategory()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        fdc.clearDatabaseContent();

        Category newCategory = new Category();
        String newCategoryName = "TestCategory";
        newCategory.setName(newCategoryName);
        newCategory.setId(fdc.createCategory(newCategory));

        Category category = fdc.getCategory(newCategory.getDBID());

        assertNotNull(category);
        assertEquals(category.getDBID(), newCategory.getDBID());
        assertEquals(category.getName(), newCategory.getName());
    }

    @Test
    public void checkUpdateCategory()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        fdc.clearDatabaseContent();

        Category newCategory = new Category();
        String newCategoryName = "TestCategory";
        newCategory.setName(newCategoryName);
        newCategory.setId(fdc.createCategory(newCategory));

        Category category = fdc.getCategory(newCategory.getDBID());

        assertNotNull(category);
        assertEquals(category.getDBID(), newCategory.getDBID());
        assertEquals(category.getName(), newCategory.getName());

        newCategory.setName("TestCategory2");

        fdc.updateCategory(newCategory);
        category = fdc.getCategory(newCategory.getDBID());

        assertNotNull(category);
        assertEquals(category.getDBID(), newCategory.getDBID());
        assertEquals(category.getName(), newCategory.getName());
    }

    @Test
    public void checkCreateReminder()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        fdc.clearDatabaseContent();

        Reminder reminder = new Reminder(new Date(), "TestReminder", 12.34);
        reminder.setId(fdc.createReminder(reminder));

        Reminder reminderFromDb = fdc.getReminder(reminder.getId());

        assertNotNull(reminderFromDb);
        assertEquals(reminder.getId(), reminderFromDb.getId());
        assertEquals(reminder.getTitle(), reminderFromDb.getTitle());
    }

    @Test
    public void checkEditReminder()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        fdc.clearDatabaseContent();

        Reminder reminder = new Reminder(new Date(), "TestReminder", 12.34);
        reminder.setId(fdc.createReminder(reminder));

        Reminder reminderFromDb = fdc.getReminder(reminder.getId());

        reminderFromDb.setTitle("TestReminder edited");
        fdc.updateReminder(reminderFromDb);

        Reminder reminderFromDb2 = fdc.getReminder(reminderFromDb.getId());

        assertNotNull(reminderFromDb2);
        assertEquals(reminderFromDb.getId(), reminderFromDb2.getId());
        assertEquals(reminderFromDb.getTitle(), reminderFromDb2.getTitle());
    }

    @Test
    public void checkDeleteReminder()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        fdc.clearDatabaseContent();

        Reminder reminder = new Reminder(new Date(), "TestReminder", 12.34);
        reminder.setId(fdc.createReminder(reminder));

        fdc.removeReminder(reminder);
        Reminder reminderFromDb = new Reminder();

        try {
            reminderFromDb = fdc.getReminder(reminder.getId());
        }
        catch(CursorIndexOutOfBoundsException ex) {
            reminderFromDb = null;
        }

        assertNull(reminderFromDb);
    }

    @Test
    public void checkRemoveTransaction()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        Transaction transaction = new Transaction();
        boolean success = fdc.removeTransaction(transaction);
        assertTrue(success);
    }

    @Test
    public void checkDateParseException()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        Date date = fdc.convertDBDateToDate("test");
        assertNull(date);
    }

    @Test
    public void checkCreateCategoryNullParams()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        fdc.clearDatabaseContent();

        Category newCategory = new Category();
        String newCategoryName = null;
        newCategory.setName(null);
        newCategory.setId(fdc.createCategory(newCategory));


        Category category = fdc.getCategory(newCategory.getDBID());
        assertNotNull(category);

        // assertNotNull(category);
        // assertEquals(category.getDBID(), newCategory.getDBID());
        // assertEquals(category.getName(), newCategory.getName());
    }

    @Test
    public void checkGetSpendingPerCategoryCurrentYear()
    {
        // Delete all transactions
        // Create four transactions with current date from different categories
        // Check if amount and stuff matches

        // Transport, Taxi, -10
        // Transport, Bus, -5

        // Hobbies, Gaming Keyboard, -33.0
        // Hobbies, Preisgeld eSport, 100.0€

        Context appContext = InstrumentationRegistry.getTargetContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        fdc.clearDatabaseContent();
        fdc.createInitialCategories();

        Category transportCat = fdc.getCategory((long)1);
        Category hobbiesCat = fdc.getCategory((long)7);

        Date date = new Date();

        Transaction transactionTaxi = new Transaction();
        transactionTaxi.setCategory(transportCat);
        transactionTaxi.setCategoryID(transportCat.getDBID());
        transactionTaxi.setDescription("Taxi");
        transactionTaxi.setAmount(-10.0);
        transactionTaxi.setDate(date);

        Transaction transactionBus = new Transaction();
        transactionBus.setCategory(transportCat);
        transactionBus.setCategoryID(transportCat.getDBID());
        transactionBus.setDescription("Bus");
        transactionBus.setAmount(-5.0);
        transactionBus.setDate(date);

        Transaction transactionKeyboard = new Transaction();
        transactionKeyboard.setCategory(hobbiesCat);
        transactionKeyboard.setCategoryID(hobbiesCat.getDBID());
        transactionKeyboard.setDescription("Gaming Keyboard");
        transactionKeyboard.setAmount(-33.0);
        transactionKeyboard.setDate(date);

        Transaction transactionPrice = new Transaction();
        transactionPrice.setCategory(hobbiesCat);
        transactionPrice.setCategoryID(hobbiesCat.getDBID());
        transactionPrice.setDescription("Price money eSport");
        transactionPrice.setAmount(100.0);
        transactionPrice.setDate(date);

        fdc.createTransaction(transactionTaxi);
        fdc.createTransaction(transactionBus);
        fdc.createTransaction(transactionKeyboard);
        fdc.createTransaction(transactionPrice);

        Map<String, Float> spendingData = fdc.getSpendingPerCategoryForCurrentYear();

        Float transportationSpending = spendingData.get("Transportation");
        Float hobbiesSpending = spendingData.get("Hobbies");

        assertEquals(transportationSpending, -15.0, 0.01);
        assertNotNull(hobbiesSpending);
    }
}
