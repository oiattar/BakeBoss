package com.example.oi156f.bakeboss;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Test for the StepDetailFragment
 */
@RunWith(AndroidJUnit4.class)
public class StepDetailActivityBasicTest {

    @Rule public ActivityTestRule<StepDetailActivity> mActivityRule
            = new ActivityTestRule<>(StepDetailActivity.class);

    @Test
    public void GoToNextStep() {
        onView(withId(R.id.next_step_button)).perform(click());

    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.oi156f.bakeboss", appContext.getPackageName());
    }
}
