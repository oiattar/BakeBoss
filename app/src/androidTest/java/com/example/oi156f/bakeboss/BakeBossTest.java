package com.example.oi156f.bakeboss;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Test for the StepDetailFragment
 */
@RunWith(AndroidJUnit4.class)
public class BakeBossTest {

    @Rule public ActivityTestRule<RecipeListActivity> mActivityRule
            = new ActivityTestRule<>(RecipeListActivity.class);


    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void verifyRecipeListFragment() {
        onView(withRecyclerView(R.id.recipe_list).atPosition(0))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(withRecyclerView(R.id.recipe_list).atPosition(1))
                .check(matches(hasDescendant(withText("Brownies"))));
        onView(withRecyclerView(R.id.recipe_list).atPosition(2))
                .check(matches(hasDescendant(withText("Yellow Cake"))));
        onView(withId(R.id.recipe_list)).perform(scrollToPosition(3));
        onView(withRecyclerView(R.id.recipe_list).atPosition(3))
                .check(matches(hasDescendant(withText("Cheesecake"))));
    }

    @Test
    public void verifyRecipeDetailFragment() {
        // click second recipe "Brownies"
        onView(withRecyclerView(R.id.recipe_list).atPosition(1)).perform(click());

        // verify Brownies details are displayed
        onView(withId(R.id.ingredients_title)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_list)).check(matches(withListSize(10)));
        onView(withId(R.id.steps_title)).check(matches(isDisplayed()));
        onView(withId(R.id.steps_list)).check(matches(withListSize(10)));
        onView(withText("Brownies")).check(matches(isDisplayed()));
    }

    @Test
    public void verifyStepDetailFragment() {
        // click second recipe "Brownies"
        onView(withRecyclerView(R.id.recipe_list).atPosition(1)).perform(click());

        // click second step in Brownies steps list
        onData(anything()).inAdapterView(withId(R.id.steps_list))
                .atPosition(1)
                .perform(click());

        // verify the second step details are displayed, image instead of video
        onView(withId(R.id.step_title)).check(matches(withText("Starting prep")));
        onView(withId(R.id.step_instruction)).check(matches(withText(containsString("Preheat the oven"))));
        onView(withId(R.id.step_image)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.video_frame)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        // go to previous step
        onView(withId(R.id.previous_step_button)).perform(click());

        // verify "Previous Step" button is disabled because it is the first step
        onView(withId(R.id.previous_step_button)).check(matches(not(isEnabled())));

        // verify first step details are displayed with video visible
        onView(withId(R.id.step_title)).check(matches(withText("Recipe Introduction")));
        onView(withId(R.id.step_instruction)).check(matches(withText(containsString("Recipe Introduction"))));
        onView(withId(R.id.step_image)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.video_frame)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View> () {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView) view).getChildCount () == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have " + size + " items");
            }
        };
    }
}
