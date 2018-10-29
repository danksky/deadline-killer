package com.pherodev.killddl;

import android.content.Context;
import android.content.Intent;
import android.widget.DatePicker;

import com.pherodev.killddl.activities.CategoryActivity;
import com.pherodev.killddl.activities.TasksActivity;
import com.pherodev.killddl.models.Category;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.StringContains.containsString;


import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
     Opening (1)
        - Open right task
     Adding (3)
        - Add with good input
         - Add with no date
         - Add with no title
     Editing (3)
         - Edit with good input
         - Edit with no date
         - Edit with no title
     Deleting (1)
        - Delete

 Total: 1 + 3 + 3 + 1 = 8

 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TasksActivityTest {

    // Set up a TasksActivity with a categoryId for testing
    @Rule
    public ActivityTestRule<CategoryActivity> categoryActivityRule =
            new ActivityTestRule<>(CategoryActivity.class);
    public String uniqueCategoryTitle = UUID.randomUUID().toString();
    public String uniqueTaskTitle = UUID.randomUUID().toString();
    public String uniqueTaskDescription = UUID.randomUUID().toString();

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = categoryActivityRule.getActivity().getApplicationContext();
        assertEquals("com.pherodev.killddl", appContext.getPackageName());
    }

    @Test
    public void createTaskWithGoodInput() {
        // before every test to get to tasksactivity due to espresso contrib limitations
        // note that this code has already been tested in CategoryActivityTest
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(uniqueCategoryTitle), closeSoftKeyboard());
        onView(withId(R.id.fab_input_category_complete)).perform(click());
        categoryActivityRule.getActivity().programmaticallyLaunchTasksActivityWithLatestCategory();


        // test action execution
        onView(withId(R.id.fab_create_task)).perform(click());
        onView(withId(R.id.edit_text_input_task_title)).perform(typeText(uniqueTaskTitle), closeSoftKeyboard());
        onView(withId(R.id.edit_text_input_task_description)).perform(typeText(uniqueTaskDescription), closeSoftKeyboard());
        onView(withId(R.id.text_view_input_task_deadline)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.fab_input_task_complete)).perform(click());

        // test checks
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat df = new SimpleDateFormat("EEE MMM dd");
        String dateString = df.format(date);

        // back to tasks page
        onView(withId(R.id.fab_input_task_complete)).check(doesNotExist());
        // make sure the title is right
        onView(withId(R.id.recycler_view_tasks)).check(matches(hasDescendant(withText(uniqueTaskTitle))));
        // make sure the description is right
        onView(withId(R.id.recycler_view_tasks)).check(matches(hasDescendant(withText(uniqueTaskDescription))));
        // should be today's date
        onView(withId(R.id.recycler_view_tasks)).check(matches(hasDescendant(withText(containsString(dateString)))));

        // after every test to clean up the categories page/DB
        // note that this code has already been tested in CategoryActivityTest
        categoryActivityRule.getActivity().programmaticallyDeleteLastCategoryEntry();
        pressBack();
        onView(withText(uniqueCategoryTitle)).check(doesNotExist());
    }
/*
    @Test
    public void createTaskWithNoDeadline() {
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(uniqueCategoryTitle), closeSoftKeyboard());
        onView(withId(R.id.fab_input_category_complete)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_input_category_complete)).perform(click());
        onView(withId(R.id.fab_input_category_complete)).check(doesNotExist());
        onView(withId(R.id.recycler_view_categories)).check(matches(hasDescendant(withText(uniqueCategoryTitle))));
    }

    @Test
    public void createTaskWithNoTitle() {
//        onView(withId(R.id.fab_create_category)).perform(click());
//        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(""), closeSoftKeyboard());
//        onView(withId(R.id.fab_input_category_complete)).check(matches(isDisplayed()));
//        onView(withId(R.id.fab_input_category_complete)).perform(click());
//        // Rather than check if the error message appears, check that your activity hasn't finished
//        onView(withId(R.id.fab_input_category_complete)).check(matches(isDisplayed()));
    }

    @Test
    public void createTaskWithNoDescription() {
//        onView(withId(R.id.fab_create_category)).perform(click());
//        StringBuilder excessiveLengthSB = new StringBuilder();
//        for (int i = 0; i < 5; i++)
//            excessiveLengthSB.append("qwertyuiopasdfghjklzxcvbnm");
//        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(excessiveLengthSB.toString()), closeSoftKeyboard());
//        onView(withId(R.id.fab_input_category_complete)).check(matches(isDisplayed()));
//        onView(withId(R.id.fab_input_category_complete)).perform(click());
//        // Rather than check if the error message appears, check that your activity hasn't finished
//        onView(withId(R.id.fab_input_category_complete)).check(matches(isDisplayed()));
    }

    // Must be run after deleteCategoryItem()
    @Test
    public void deleteTask() {
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(uniqueCategoryTitle), closeSoftKeyboard());
        pressBack();
        // Otherwise this fails
        onView(withText(uniqueCategoryTitle)).check(doesNotExist());
    }

    @Test
    public void editTaskTitle() {
        // Create a Category through UI to reach TasksActivity
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(uniqueCategoryTitle), closeSoftKeyboard());
        onView(withId(R.id.fab_input_category_complete)).perform(click());
        categoryActivityRule.getActivity().programmaticallyLaunchTasksActivityWithLatestCategory();
        onView(withId(R.id.fab_create_task)).check(matches(isDisplayed()));
        // This is going to clutter up CategoryActivity without delete
        categoryActivityRule.getActivity().programmaticallyDeleteLastCategoryEntry();
        onView(withText(uniqueCategoryTitle)).check(doesNotExist());
    }

    @Test
    public void editTaskDescription() {
        // Create a Category through UI to reach TasksActivity
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(uniqueCategoryTitle), closeSoftKeyboard());
        onView(withId(R.id.fab_input_category_complete)).perform(click());
        categoryActivityRule.getActivity().programmaticallyLaunchTasksActivityWithLatestCategory();
        onView(withId(R.id.fab_create_task)).check(matches(isDisplayed()));
        // This is going to clutter up CategoryActivity without delete
        categoryActivityRule.getActivity().programmaticallyDeleteLastCategoryEntry();
        onView(withText(uniqueCategoryTitle)).check(doesNotExist());
    }

    @Test
    public void editTaskDate() {
        // Create a Category through UI to reach TasksActivity
        onView(withId(R.id.fab_create_category)).perform(click());
        onView(withId(R.id.edit_text_input_category_title)).perform(typeText(uniqueCategoryTitle), closeSoftKeyboard());
        onView(withId(R.id.fab_input_category_complete)).perform(click());
        categoryActivityRule.getActivity().programmaticallyLaunchTasksActivityWithLatestCategory();
        onView(withId(R.id.fab_create_task)).check(matches(isDisplayed()));
        // This is going to clutter up CategoryActivity without delete
        categoryActivityRule.getActivity().programmaticallyDeleteLastCategoryEntry();
        onView(withText(uniqueCategoryTitle)).check(doesNotExist());
    }

    @Test
    public void editTaskWithNoTitle() {

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
*/

}
