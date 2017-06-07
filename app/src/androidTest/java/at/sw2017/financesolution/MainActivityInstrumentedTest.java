package at.sw2017.financesolution;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;

import com.github.clans.fab.FloatingActionButton;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.util.Calendar;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Created by JK on 26.04.17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new
            ActivityTestRule<>(MainActivity.class);


    // -- Main Layout Tests --
    @Test
    public void testTabLayoutExists() throws Exception {
        onView(withId(R.id.tab_layout)).check(matches(isDisplayed()));
    }

    // -- Home Fragment Tests --
    @Test
    public void testHomeTabExists() throws Exception {
        Matcher<View> matcher = allOf(withText("Home"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).check(matches(isDisplayed()));
    }

    @Test
    public void testHomeTabFragmentIsDisplayed() throws Exception {
        Matcher<View> matcher = allOf(withText("Home"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());
        sleep(500);
        onView(withId(R.id.frag_home_balance_text)).check(matches(isDisplayed()));
    }


    // -- Transaction Fragment Tests --
    @Test
    public void testTransactionsTabExists() throws Exception {
        Matcher<View> matcher = allOf(withText("Transactions"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).check(matches(isDisplayed()));
    }
/*
    @Test
    public void testTransactionsTabFragmentIsDisplayed() throws Exception {
        Matcher<View> matcher = allOf(withText("Transactions"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());
        sleep(500);
        onView(withId(R.layout.fragment_transaction)).check(matches(isDisplayed()));
        //onView(withId(R.layout.fragment_reports)).check(matches(isDisplayed()));
    }
*/


    @Test
    public void testFloatingActionButtonExists() throws Exception {
        onView(withId(R.id.floating_action_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testFloatingActionButtonTransctionsExists() throws Exception {
        onView(allOf(isDescendantOfA(withId(R.id.floating_action_button)), not(withId(R.id.floating_action_button_categories)),
                not(withId(R.id.floating_action_button_transactions)),
                not(withId(R.id.floating_action_button_reminders)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                instanceOf(FloatingActionButton.class))).perform(click());
        onView(withId(R.id.floating_action_button_transactions)).check(matches(isDisplayed()));
    }

    @Test
    public void testFloatingActionButtonTransactionsClick() throws Exception {
        onView(allOf(isDescendantOfA(withId(R.id.floating_action_button)), not(withId(R.id.floating_action_button_categories)),
                not(withId(R.id.floating_action_button_transactions)),
                not(withId(R.id.floating_action_button_reminders)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                instanceOf(FloatingActionButton.class))).perform(click());
        onView(withId(R.id.floating_action_button_transactions)).perform(click());
        onView(withId(R.id.add_transaction_activity_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testFloatingActionButtonRemindersExists() throws Exception {
        onView(allOf(isDescendantOfA(withId(R.id.floating_action_button)), not(withId(R.id.floating_action_button_categories)),
                not(withId(R.id.floating_action_button_transactions)),
                not(withId(R.id.floating_action_button_reminders)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                instanceOf(FloatingActionButton.class))).perform(click());
        onView(withId(R.id.floating_action_button_reminders)).check(matches(isDisplayed()));
    }

    @Test
    public void testFloatingActionButtonRemindersClick() throws Exception {
        onView(allOf(isDescendantOfA(withId(R.id.floating_action_button)), not(withId(R.id.floating_action_button_categories)),
                not(withId(R.id.floating_action_button_transactions)),
                not(withId(R.id.floating_action_button_reminders)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                instanceOf(FloatingActionButton.class))).perform(click());
        onView(withId(R.id.floating_action_button_reminders)).perform(click());
        onView(withId(R.id.activity_add_reminder)).check(matches(isDisplayed()));
    }

    @Test
    public void testFloatingActionButtonCategoriesExists() throws Exception {
        onView(allOf(isDescendantOfA(withId(R.id.floating_action_button)), not(withId(R.id.floating_action_button_categories)),
                not(withId(R.id.floating_action_button_transactions)),
                not(withId(R.id.floating_action_button_reminders)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                instanceOf(FloatingActionButton.class))).perform(click());
        onView(withId(R.id.floating_action_button_categories)).check(matches(isDisplayed()));
    }

    @Test
    public void testFloatingActionButtonCategoriesClick() throws Exception {
        onView(allOf(isDescendantOfA(withId(R.id.floating_action_button)), not(withId(R.id.floating_action_button_categories)),
                not(withId(R.id.floating_action_button_transactions)),
                not(withId(R.id.floating_action_button_reminders)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                instanceOf(FloatingActionButton.class))).perform(click());
        onView(withId(R.id.floating_action_button_categories)).perform(click());
        //onView(withId(R.id.add_category_activity_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testTransactionsListExists() throws Exception {
        Matcher<View> matcher = allOf(withText("Transactions"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());
        sleep(500);
        onView(withId(R.id.transaction_list_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testTransactionsListSearchFieldExists() throws Exception {
        Matcher<View> matcher = allOf(withText("Transactions"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());
        sleep(500);
        onView(withId(R.id.transaction_search_field)).check(matches(isDisplayed()));
    }

    // -- Reports Fragment Tests --
    @Test
    public void testReportsTabExists() throws Exception {
        Matcher<View> matcher = allOf(withText("Reports"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).check(matches(isDisplayed()));
    }

    /*
    @Test
    public void testReportsTabFragmentIsDisplayed() throws Exception {
        Matcher<View> matcher = allOf(withText("Reports"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());

        onView(withId(R.layout.fragment_report)).check(matches(isDisplayed()));
    }
*/
    @Test
    public void testPieChartExists() throws Exception {
        Matcher<View> matcher = allOf(withText("Reports"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());

        onView(withId(R.id.chartExpensesPie))      // withId(R.id.my_view) is a ViewMatcher
                .perform(click())               // click() is a ViewAction
                .check(matches(isDisplayed())); // matches(isDisplayed()) is a ViewAssertion
    }

    @Test
    public void testPieChartWithContentCreation() throws Exception {
        // 1. Click floating action button to create a transaction
        onView(allOf(isDescendantOfA(withId(R.id.floating_action_button)), not(withId(R.id.floating_action_button_categories)),
                not(withId(R.id.floating_action_button_transactions)),
                not(withId(R.id.floating_action_button_reminders)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                instanceOf(FloatingActionButton.class))).perform(click());

        onView(withId(R.id.floating_action_button_transactions)).perform(click());

        // 2    Create Transaction
        // 2.1  Description
        final String transactionText = "Pizza & Beer";
        onView(withId(R.id.editDescription)).perform(typeText(transactionText), closeSoftKeyboard());
        onView(withId(R.id.editDescription))
                .check(matches(withText(transactionText)));

        // 2.2  Date
        onView(withId(R.id.editDate)).perform(click());
        int year = 1990;
        int monthOfYear = 10;
        int dayOfMonth = 15;
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withText("OK")).perform(click());

        // 2.3  Set category
        String selectionText = "Food & Drinks";
        onView(withId(R.id.spinnerCategory)).perform(click());
        onData(hasToString(startsWith("Food"))).perform(click());

        // 2.4  Set amount
        String amount = "25.99";
        onView(withId(R.id.editAmount)).perform(typeText(amount), closeSoftKeyboard());

        // 2.5  Click save button
        onView(withId(R.id.buttonSave)).perform(click());

        // 2.6  Switch to reports page
        Matcher<View> matcher = allOf(withText("Reports"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());
    }


    @Test
    public void addAndReopenAndEditTransaction() throws Exception {

        final String description = "12TestDescription34";
        final String amount = "6543.21";
        final String category = "Entertainment";
        final String newDescription = "newDescription";

        final int year = 2015;
        final int monthOfYear = 9;
        final int dayOfMonth = 20;

        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear-1, dayOfMonth); // month in calendar starts with 0
        final String expectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());

        // open activity
        onView(allOf(isDescendantOfA(withId(R.id.floating_action_button)), Matchers.not(withId(R.id.floating_action_button_categories)),
                Matchers.not(withId(R.id.floating_action_button_transactions)),
                Matchers.not(withId(R.id.floating_action_button_reminders)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                instanceOf(FloatingActionButton.class))).perform(click());
        onView(withId(R.id.floating_action_button_transactions)).perform(click());

        // go back
        Espresso.pressBack();


        // open again
        onView(allOf(isDescendantOfA(withId(R.id.floating_action_button)), Matchers.not(withId(R.id.floating_action_button_categories)),
                Matchers.not(withId(R.id.floating_action_button_transactions)),
                Matchers.not(withId(R.id.floating_action_button_reminders)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                instanceOf(FloatingActionButton.class))).perform(click());
        onView(withId(R.id.floating_action_button_transactions)).perform(click());

        // fill fields
        // description
        onView(withId(R.id.editDescription)).perform(typeText(description), closeSoftKeyboard());

        // start photo and cancel
        //onView(withId(R.id.photoView)).perform(click());
        //Espresso.pressBack();

        // category
        onView(withId(R.id.spinnerCategory)).perform(click());
        onData(hasToString(startsWith("Entertainment"))).perform(click());

        // amount
        onView(withId(R.id.editAmount)).perform(typeText(amount), closeSoftKeyboard());

        // date
        onView(withId(R.id.editDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withText("OK")).perform(click());


        // goback again
        Espresso.pressBack(); // close activity

        // open again
        onView(allOf(isDescendantOfA(withId(R.id.floating_action_button)), Matchers.not(withId(R.id.floating_action_button_categories)),
                Matchers.not(withId(R.id.floating_action_button_transactions)),
                Matchers.not(withId(R.id.floating_action_button_reminders)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                instanceOf(FloatingActionButton.class))).perform(click());
        onView(withId(R.id.floating_action_button_transactions)).perform(click());

        // description
        onView(withId(R.id.editDescription)).perform(typeText(description), closeSoftKeyboard());

        // category
        onView(withId(R.id.spinnerCategory)).perform(click());
        onData(hasToString(startsWith("Entertainment"))).perform(click());

        // amount
        onView(withId(R.id.editAmount)).perform(typeText(amount), closeSoftKeyboard());

        // date
        onView(withId(R.id.editDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withText("OK")).perform(click());

        // save
        onView(withId(R.id.buttonSave)).perform(click());

        // reopen
        onData(anything())
                .inAdapterView(allOf(withId(R.id.frag_home_transaction_listview), isCompletelyDisplayed()))
                .atPosition(0).perform(click());

        // check entries
        onView(withId(R.id.editDescription)).check(matches(withText(description)));
        onView(withId(R.id.editAmount)).check(matches(withText(amount))); // TODO: uncomment after getamount decimal fix
        onView(withId(R.id.spinnerCategory)).check(matches(withSpinnerText(containsString(category))));
        onView(withId(R.id.editDate)).check(matches(withText(expectedDate)));

        // update description
        onView(withId(R.id.editDescription)).perform(typeText(newDescription), closeSoftKeyboard());
        onView(withId(R.id.buttonSave)).perform(click());

    }

}
