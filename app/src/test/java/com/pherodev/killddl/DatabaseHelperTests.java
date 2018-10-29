package com.pherodev.killddl;

import android.database.Cursor;


import com.pherodev.killddl.activities.CategoryInputActivity;
import com.pherodev.killddl.activities.TaskInputActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;


import dbhelpers.DatabaseHelper;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class DatabaseHelperTests {
    @Test
    public void testCreatesCategory() {
        String title = "testCategoryDBTitle";
        CategoryInputActivity activity = Robolectric.setupActivity(CategoryInputActivity.class);
        DatabaseHelper dbHelper = new DatabaseHelper(activity);
        long newRowId = dbHelper.createCategory( title);
        String[] args = {Long.toString(newRowId)};

        Cursor cursor  =  dbHelper.getWritableDatabase().rawQuery(DatabaseHelper.Category.SELECT_CATEGORY_NAME_BY_ID,args);
        String dbTitle = "";
        try {
            while (cursor.moveToNext()) {
                 dbTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Category.COLUMN_TITLE));
            }
        } finally {
            cursor.close();
        }

        assertEquals(title,dbTitle);
        dbHelper.close();

    }

    @Test
    public void testCreateTask(){
        String categoryTitle = "testTaskDBCreate";
        String originalTitle = "testTaskDBCreateTitle";
        TaskInputActivity activity = Robolectric.setupActivity(TaskInputActivity.class);
        DatabaseHelper dbHelper = new DatabaseHelper(activity);
        long catId = dbHelper.createCategory(categoryTitle);
        dbHelper.createTask(catId,originalTitle,"testEditTask","10/27/18" );
        String[] args = {Long.toString(catId)};
        Cursor cursor  =  dbHelper.getWritableDatabase().rawQuery(DatabaseHelper.Task.SELECT_TASK_BY_ID,args);

        String dbTitle = "";
        try {
            while (cursor.moveToNext()) {
                dbTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_TITLE));
            }
        } finally {
            cursor.close();
        }

        assertEquals(originalTitle,dbTitle);


        dbHelper.close();
    }

    @Test
    public void testEditTask(){
        String categoryTitle = "testTaskDBEdit";
        String originalTitle = "testTaskDBEditTitle-Original";
        String editedTitle = "testTaskDBEditTitle-Edited";
        TaskInputActivity activity = Robolectric.setupActivity(TaskInputActivity.class);
        DatabaseHelper dbHelper = new DatabaseHelper(activity);
        long catId = dbHelper.createCategory(categoryTitle);
        long taskId = dbHelper.createTask(catId,originalTitle,"testEditTask","10/27/18" );
        String[] args = {Long.toString(catId)};
        Cursor cursor  =  dbHelper.getWritableDatabase().rawQuery(DatabaseHelper.Task.SELECT_TASK_BY_ID,args);


        String dbOriginalTaskTitle = "";
        try {
            while (cursor.moveToNext()) {
                dbOriginalTaskTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_TITLE));
            }
        }finally {
            cursor.close();
        }

        assertEquals(originalTitle,dbOriginalTaskTitle);

        dbHelper.updateTask(taskId,catId,editedTitle,"testEditTask-Edited","10/27/18");
        cursor  =  dbHelper.getWritableDatabase().rawQuery(DatabaseHelper.Task.SELECT_TASK_BY_ID,args);
        String dbEditedTaskTitle = "";
        try {
            while (cursor.moveToNext()) {
                dbEditedTaskTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_TITLE));
            }
        }finally {
            cursor.close();
        }

        assertEquals(editedTitle,dbEditedTaskTitle);

        dbHelper.close();
    }




}
