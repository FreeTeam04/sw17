package at.sw2017.financesolution;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by joe on 06.06.17.
 */

@RunWith(AndroidJUnit4.class)
public class ReminderActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<ReminderActivity> mActivityRule = new
            ActivityTestRule<>(ReminderActivity.class);

    @Test
    public void testSearchFieldExists() throws Exception {
        onView(withId(R.id.reminder_search_field)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddReminderEditSearchFieldInput() throws Exception {
        final String TEXT = "Some search";
        onView(withId(R.id.reminder_search_field)).perform(typeText(TEXT), closeSoftKeyboard());
        onView(withId(R.id.reminder_search_field)).check(matches(withText(TEXT)));
    }

    @Test
    public void testListViewExists() throws Exception {
        onView(withId(R.id.reminder_list_view)).check(matches(isDisplayed()));
    }
}