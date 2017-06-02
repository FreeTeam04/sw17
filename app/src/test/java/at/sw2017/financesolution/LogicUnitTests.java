package at.sw2017.financesolution;

import android.content.Context;
import android.test.RenamingDelegatingContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;

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
        assertEquals(4, 2 + 2);
    }

}