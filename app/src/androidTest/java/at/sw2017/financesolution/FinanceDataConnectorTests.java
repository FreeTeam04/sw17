package at.sw2017.financesolution;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;
import at.sw2017.financesolution.models.Category;
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
