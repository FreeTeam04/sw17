package at.sw2017.financesolution;

import org.junit.Test;

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
    public void checkFinanceDataConnectorAvailable()
    {
        assertNotNull(FinanceDataConnectorImpl.getInstance());
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
        financeDataMock.setupPseudoTransactionsCase2(); // empty list

        FinanceSolutionsCalculator calculator = new FinanceSolutionsCalculator(financeDataMock);

        assertEquals(calculator.getCurrentBalance(), 50.0, 0.0001);

    }
}