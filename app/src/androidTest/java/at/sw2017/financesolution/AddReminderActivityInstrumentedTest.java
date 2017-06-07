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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by jk on 03.06.17.
 */

@RunWith(AndroidJUnit4.class)
public class AddReminderActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<AddReminderActivity> mActivityRule = new
            ActivityTestRule<>(AddReminderActivity.class);

    @Test
    public void testAddReminderActivityExists() throws Exception {
        onView(withId(R.id.activity_add_reminder)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddReminderEditTitleExists() throws Exception {
        onView(withId(R.id.edittext_add_reminder_title)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddReminderEditAmountExists() throws Exception {
        onView(withId(R.id.edittext_add_reminder_amount)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddReminderEditDateExists() throws Exception {
        onView(withId(R.id.edittext_add_reminder_date)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddReminderEditTimeExists() throws Exception {
        onView(withId(R.id.edittext_add_reminder_time)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddReminderButtonSaveExists() throws Exception {
        onView(withId(R.id.button_save_reminder)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddReminderButtonSaveClick() throws Exception {
        onView(withId(R.id.button_save_reminder)).perform(click());
    }

    @Test
    public void testAddReminderEditTitleInput() throws Exception {
        final String TEXT = "Reminder Title";
        onView(withId(R.id.edittext_add_reminder_title)).perform(typeText(TEXT), closeSoftKeyboard());
        onView(withId(R.id.edittext_add_reminder_title)).check(matches(withText(TEXT)));
    }

    @Test
    public void testAddReminderEditAmountInput() throws Exception {
        final String TEXT = "100";
        onView(withId(R.id.edittext_add_reminder_title)).perform(typeText(TEXT), closeSoftKeyboard());
        onView(withId(R.id.edittext_add_reminder_title)).check(matches(withText(TEXT)));
    }
}
