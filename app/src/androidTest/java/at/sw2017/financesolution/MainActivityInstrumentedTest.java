package at.sw2017.financesolution;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.Test;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testTransactionsTabFragmentIsDisplayed() throws Exception {
        Matcher<View> matcher = allOf(withText("Transactions"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());
        sleep(500);
        onView(withId(R.layout.fragment_transaction)).check(matches(isDisplayed()));
        //onView(withId(R.layout.fragment_reports)).check(matches(isDisplayed()));
    }

    /*@Test
    public void testAddTransactionButtonExists() throws Exception {

        onView(withText("Add Transaction")).perform(click());
    }*/

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
        //onView(withId(R.id.add_reminder_activity_layout)).check(matches(isDisplayed()));
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
    public void testFloatingActionButtonRCategoriesClick() throws Exception {
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

    @Test
    public void testReportsTabFragmentIsDisplayed() throws Exception {
        Matcher<View> matcher = allOf(withText("Reports"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());

        onView(withId(R.layout.fragment_report)).check(matches(isDisplayed()));
    }

    @Test
    public void testPieChartExists() throws Exception {
        Matcher<View> matcher = allOf(withText("Reports"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());

        onView(withId(R.id.chartExpensesPie))      // withId(R.id.my_view) is a ViewMatcher
                .perform(click())               // click() is a ViewAction
                .check(matches(isDisplayed())); // matches(isDisplayed()) is a ViewAssertion
    }

}
