package com.pherodev.killddl;

import android.content.Context;

import com.pherodev.killddl.activities.CategoryActivity;
import com.pherodev.killddl.models.Category;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CategoryActivityTest {

    @Rule
    public ActivityTestRule<CategoryActivity> categoryActivityRule =
            new ActivityTestRule<>(CategoryActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = categoryActivityRule.getActivity().getApplicationContext();
        assertEquals("com.pherodev.killddl", appContext.getPackageName());
    }

    @Test
    public void floatingActionButtonExists() {
        onView(withId(R.id.fab_create_category)).check(matches(isDisplayed()));
    }

    @Test
    public void floatingActionButtonClickLaunchesInput() {
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.edit_text_input_category_title)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_text_input_task_title)).check(doesNotExist());
    }

    @Test
    public void createCategoryItem() {
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText("Title 1"), closeSoftKeyboard());
        onView(withId(R.id.fab_input_category_complete)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_input_category_complete)).perform(click());
        onView(withId(R.id.fab_input_category_complete)).check(doesNotExist());
        onView(withId(R.id.recycler_view_categories)).check(matches(hasDescendant(withText("Title 1"))));
    }

}
