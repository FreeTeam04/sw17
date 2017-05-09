package at.sw2017.financesolution;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;

/**
 * Created by fabiowallner on 09/05/2017.
 */
@RunWith(AndroidJUnit4.class)
public class AddTransactionActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<AddTransactionActivity> mActivityRule = new
            ActivityTestRule<>(AddTransactionActivity.class);


    @Test
    public void testSaveButton() throws Exception {
        onView(withText("Save")).perform(click());
    }
}

