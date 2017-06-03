package at.sw2017.financesolution;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

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


}
