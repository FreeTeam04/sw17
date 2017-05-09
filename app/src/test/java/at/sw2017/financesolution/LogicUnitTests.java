package at.sw2017.financesolution;

import android.content.Context;
import android.test.RenamingDelegatingContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LogicUnitTests {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void calculateBalanceTest_EmptyList() throws Exception {

        FinanceDataConnectorMockUp financeDataMock = new FinanceDataConnectorMockUp();
        financeDataMock.setupPseudoTransactionsCase1(); // empty list

        FinanceSolutionsCalculator calculator = new FinanceSolutionsCalculator(financeDataMock);

        assertEquals(calculator.getCurrentBalance(), 0.0, 0.0001);

    }

    @Test
    public void calculateBalanceTest_2() throws Exception {

        FinanceDataConnectorMockUp financeDataMock = new FinanceDataConnectorMockUp();
        financeDataMock.setupPseudoTransactionsCase2(); // 2 transactions +100, -50

        FinanceSolutionsCalculator calculator = new FinanceSolutionsCalculator(financeDataMock);

        assertEquals(calculator.getCurrentBalance(), 50.0, 0.0001);

    }


    // TODO: Fix this test about checking if a database connection was created correctly
    @RunWith(JUnit4.class)
    public class testDatabaseConnection {

        // Mock Context (TODO fixme)
        Context mMockContext;

        @Before
        public void setUp() {
           //  mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        }

        // FinanceDataConnector dbConnection = FinanceDataConnectorImpl.getInstance(mMockContext);



    }

}