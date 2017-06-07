package at.sw2017.financesolution;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by Peter on 02-Jun-17.
 */


@RunWith(AndroidJUnit4.class)
public class AddTransactionActivityPhotoInstrumentedTest {
/*
    @Rule
    public ActivityTestRule<AddTransactionActivity> mActivityRule = new
            ActivityTestRule<>(AddTransactionActivity.class);
*/

    @Rule
    public IntentsTestRule mIntentsRule = new IntentsTestRule<>(AddTransactionActivity.class);

    @Before
    public void stubCameraIntent() {

        Instrumentation.ActivityResult result = createImageCaptureStub();

        // intent stub
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);
    }

    @Test
    public void testTakePhoto() throws Exception {

        onView(withId(R.id.photoView)).check(matches(isDisplayed()));

        onView(withId(R.id.photoView)).perform(click());

        //intended(hasAction(equalTo(MediaStore.ACTION_IMAGE_CAPTURE)));
        // how to compare (cropped) images
    }

    private Instrumentation.ActivityResult createImageCaptureStub() {
        // Put the drawable in a bundle.
        Bundle bundle = new Bundle();
        bundle.putParcelable("TEST_PIC_KEY", BitmapFactory.decodeResource(
                mIntentsRule.getActivity().getResources(), R.drawable.ic_shopping_basket_black_24dp));
        Bitmap b;

        // Create the Intent that will include the bundle.
        Intent resultData = new Intent();
        resultData.putExtras(bundle);

        // Create the ActivityResult with the Intent.
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

}