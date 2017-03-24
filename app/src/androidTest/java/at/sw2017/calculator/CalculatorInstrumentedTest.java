package at.sw2017.calculator;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.intent.IntentCallback;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CalculatorInstrumentedTest {

    @Rule
    public ActivityTestRule<Calculator> mActivityRule = new
            ActivityTestRule<>(Calculator.class);

    @Test
    public void testButtons() throws Exception {
        for (int i = 0; i <= 9; i++) {
            onView(withText(Integer.toString(i))).perform(click());
        }
        onView(withText("+")).perform(click());
        onView(withText("-")).perform(click());
        onView(withText("*")).perform(click());
        onView(withText("/")).perform(click());
        onView(withText("=")).perform(click());
        onView(withText("C")).perform(click());
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("at.sw2017.calculator", appContext.getPackageName());
    }

    @Test
    public void testInputField() {
        for (int i = 9; i >= 0; i--) {
            onView(withText(Integer.toString(i))).perform(click());
        }

        onView(withText("9876543210")).check(matches(isDisplayed()));
    }
}
