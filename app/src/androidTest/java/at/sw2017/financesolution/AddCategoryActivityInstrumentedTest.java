package at.sw2017.financesolution;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.INotificationSideChannel;
import android.widget.DatePicker;
import android.widget.EditText;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import at.sw2017.financesolution.models.Category;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

/**
 * Created by fabiowallner on 09/05/2017.
 */
@RunWith(AndroidJUnit4.class)
public class AddCategoryActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<AddCategoryActivity> mActivityRule = new
            ActivityTestRule<>(AddCategoryActivity.class);

    // Test if UI elements are displayed
    @Test
    public void testNameTextFieldExists() throws Exception {
        onView(withId(R.id.editName)).check(matches(isDisplayed()));
    }

    @Test
    public void testSaveButtonExists() throws Exception {
        onView(withId(R.id.buttonSave)).check(matches(isDisplayed()));
    }

    // Test Input
    @Test
    public void testNameInput() throws Exception {

        final String TEXT = "Test Category Text!";

        // write description text
        onView(withId(R.id.editName))
                .perform(typeText(TEXT), closeSoftKeyboard());

        // check description text
        onView(withId(R.id.editName))
                .check(matches(withText(TEXT)));
    }

    @Test
    public void testInvalidDescriptionInput() throws Exception {

        onView(withId(R.id.buttonSave)).perform(click());
        onView(withText("No name")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testCategoryCreation() throws Exception {
        final String TEXT = "testcategory1";

        // write description text
        onView(withId(R.id.editName))
                .perform(typeText(TEXT), closeSoftKeyboard());
        onView(withId(R.id.buttonSave)).perform(click());

        Context appContext = mActivityRule.getActivity().getApplicationContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        FinanceDataConnectorImpl fdcl = (FinanceDataConnectorImpl)fdc;
        ArrayList<Category> categories = fdcl.getAllCategories();

        assertEquals(TEXT,categories.get(categories.size() - 1).getName());
    }

    @Test
    public void testCategoryEdit() throws Exception {
        final String TEXT = "testcategory1";
        final String TEXT2 = "testcategory2";

        // write description text
        onView(withId(R.id.editName))
                .perform(typeText(TEXT), closeSoftKeyboard());
        onView(withId(R.id.buttonSave)).perform(click());

        Context appContext = mActivityRule.getActivity().getApplicationContext();
        FinanceDataConnector fdc = FinanceDataConnectorImpl.getInstance(appContext);
        FinanceDataConnectorImpl fdcl = (FinanceDataConnectorImpl)fdc;
        ArrayList<Category> categories = fdcl.getAllCategories();

        assertEquals(TEXT,categories.get(categories.size() - 1).getName());

        Intent editTransactionIntent = new Intent(appContext, AddTransactionActivity.class);
        editTransactionIntent.putExtra("EDIT", categories.get(categories.size() - 1).getDBID());
        mActivityRule.launchActivity(editTransactionIntent);

        onView(withId(R.id.editName))
                .check(matches(withText(TEXT)));

        onView(withId(R.id.editName))
                .perform(typeText(TEXT2), closeSoftKeyboard());

        onView(withId(R.id.buttonSave)).perform(click());

        categories = fdcl.getAllCategories();
        assertEquals(TEXT + TEXT2,categories.get(categories.size() - 1).getName());

    }
}

