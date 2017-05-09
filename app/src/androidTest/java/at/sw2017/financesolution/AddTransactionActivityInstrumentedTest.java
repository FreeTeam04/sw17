package at.sw2017.financesolution;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by fabiowallner on 09/05/2017.
 */
@RunWith(AndroidJUnit4.class)
public class AddTransactionActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<AddTransactionActivity> mActivityRule = new
            ActivityTestRule<>(AddTransactionActivity.class);


    // Test if UI elements are displayed
    @Test
    public void testAddTransactionHeaderExists() throws Exception {
        onView(withId(R.id.add_transaction_header)).check(matches(isDisplayed()));
    }

    @Test
    public void testDescriptionTextFieldExists() throws Exception {
        onView(withId(R.id.editDescription)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerGategoryExists() throws Exception {
        onView(withId(R.id.spinnerGategory)).check(matches(isDisplayed()));
    }

    @Test
    public void testSignButtonExists() throws Exception {
        onView(withId(R.id.sign)).check(matches(isDisplayed()));
    }

    @Test
    public void testEditAmountExists() throws Exception {
        onView(withId(R.id.editAmount)).check(matches(isDisplayed()));
    }

    @Test
    public void testDatePickerExists() throws Exception {
        onView(withId(R.id.datepickerDate)).check(matches(isDisplayed()));
    }

    @Test
    public void testSaveButtonExists() throws Exception {
        onView(withId(R.id.buttonSave)).check(matches(isDisplayed()));
    }


    // Test Input
    @Test
    public void testDescriptionInput() throws Exception {

        final String TEXT = "Test Description Text!";

        // write description text
        onView(withId(R.id.editDescription))
                .perform(typeText(TEXT), closeSoftKeyboard());

        // check description text
        onView(withId(R.id.editDescription))
                .check(matches(withText(TEXT)));
    }

}

