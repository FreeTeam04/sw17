package at.sw2017.financesolution;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by JK on 26.04.17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new
            ActivityTestRule<>(MainActivity.class);

    @Test
    public void testTabLayout() throws Exception {
        onView(withId(R.id.tab_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testHomeTabExists() throws Exception {
        Matcher<View> matcher = allOf(withText("Home"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());
    }

    @Test
    public void testTranasactionsTabExists() throws Exception {
        Matcher<View> matcher = allOf(withText("Transactions"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());
    }

    @Test
    public void testReportsTabExists() throws Exception {
        Matcher<View> matcher = allOf(withText("Reports"),
                isDescendantOfA(withId(R.id.tab_layout)));
        onView(matcher).perform(click());
        onView(matcher).check(matches(not(isDisplayed())));
    }

    @Test
    public void testButtons() throws Exception {

        /*onView(withText("Add Transaction")).perform(click());
        for (int i = 1; i <= 9; i++) {
            onView(withText(Integer.toString(i))).perform(click());
        }

        onView(withId(R.id.button0)).perform(click());

        onView(withText("+")).perform(click());
        onView(withText("-")).perform(click());
        onView(withText("*")).perform(click());
        onView(withText("/")).perform(click());
        onView(withText("=")).perform(click());
        onView(withText("C")).perform(click());*/

    }

}
