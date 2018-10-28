package com.pherodev.killddl;

import android.content.Context;
import android.content.Intent;

import com.pherodev.killddl.activities.CategoryActivity;
import com.pherodev.killddl.activities.TasksActivity;
import com.pherodev.killddl.models.Category;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.UUID;

import androidx.test.rule.ActivityTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
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
    public String uniqueCategoryTitle = UUID.randomUUID().toString();

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = categoryActivityRule.getActivity().getApplicationContext();
        assertEquals("com.pherodev.killddl", appContext.getPackageName());
    }

    @Test
    public void createCategoryItem() {
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.fab_input_category_complete)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(uniqueCategoryTitle), closeSoftKeyboard());
        onView(withId(R.id.fab_input_category_complete)).perform(click());
        onView(withId(R.id.fab_input_category_complete)).check(doesNotExist());
        onView(withId(R.id.recycler_view_categories)).check(matches(hasDescendant(withText(uniqueCategoryTitle))));
    }

    @Test
    public void emptyCategoryItem() {
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.fab_input_category_complete)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.fab_input_category_complete)).perform(click());
        // Rather than check if the error message appears, check that your activity hasn't finished
        onView(withId(R.id.fab_input_category_complete)).check(matches(isDisplayed()));
    }

    @Test
    public void excessiveTitleCategoryItem() {
        onView(withId(R.id.fab_create_category)).perform(click());
        StringBuilder excessiveLengthSB = new StringBuilder();
        for (int i = 0; i < 5; i++)
            excessiveLengthSB.append("qwertyuiopasdfghjklzxcvbnm");
        onView(withId(R.id.fab_input_category_complete)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(excessiveLengthSB.toString()), closeSoftKeyboard());
        onView(withId(R.id.fab_input_category_complete)).perform(click());
        // Rather than check if the error message appears, check that your activity hasn't finished
        onView(withId(R.id.fab_input_category_complete)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteCategoryItem() {
        categoryActivityRule.getActivity().programmaticallyDeleteLastCategoryEntry();
        onView(withText(uniqueCategoryTitle)).check(doesNotExist());
    }

    // Must be run after deleteCategoryItem()
    @Test
    public void userCancelsCreateCategoryItem() {
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(uniqueCategoryTitle), closeSoftKeyboard());
        pressBack();
        // Otherwise this fails
        onView(withText(uniqueCategoryTitle)).check(doesNotExist());
    }

    @Test
    public void launchTasksActivity() {
        // Create a Category through UI to reach TasksActivity
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(uniqueCategoryTitle), closeSoftKeyboard());
        onView(withId(R.id.fab_input_category_complete)).perform(click());
        categoryActivityRule.getActivity().programmaticallyLaunchTasksActivityWithLatestCategory();
        onView(withId(R.id.fab_create_task)).check(matches(isDisplayed()));
        // This is going to clutter up CategoryActivity without delete
        categoryActivityRule.getActivity().programmaticallyDeleteLastCategoryEntry();
        pressBack();
        onView(withText(uniqueCategoryTitle)).check(doesNotExist());
    }

}
