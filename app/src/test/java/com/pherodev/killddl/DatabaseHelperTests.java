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
    public void deleteCategory(){
        String title = "testDeleteCategoryDBTitle";
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

        dbHelper.deleteCategory(newRowId);
        cursor  =  dbHelper.getWritableDatabase().rawQuery(DatabaseHelper.Category.SELECT_CATEGORY_NAME_BY_ID,args);
        dbTitle = "";
        try {
            while (cursor.moveToNext()) {
                dbTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Category.COLUMN_TITLE));
            }
        } finally {
            cursor.close();
        }

        assertEquals("",dbTitle);

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

    @Test
    public void testDeleteTask(){
        String categoryTitle = "testDeleteTaskDBEdit";
        String originalTitle = "testDeleteTaskDBTitle";

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

        dbHelper.deleteTask(taskId);
        cursor  =  dbHelper.getWritableDatabase().rawQuery(DatabaseHelper.Task.SELECT_TASK_BY_ID,args);
        String dbTaskTitle = "";
        try {
            while (cursor.moveToNext()) {
                dbTaskTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_TITLE));
            }
        }finally {
            cursor.close();
        }

        assertEquals("",dbTaskTitle);

        dbHelper.close();
    }

    @Test
    public void testSelectTasksByCategory(){
        String categoryTitle = "testSelectTaskDBEdit";
        String otherCategoryTitle = "testSelectTaskDBDecoy";
        String taskTitle = "testSelectTaskDBTitle";

        TaskInputActivity activity = Robolectric.setupActivity(TaskInputActivity.class);
        DatabaseHelper dbHelper = new DatabaseHelper(activity);

        long catId = dbHelper.createCategory(categoryTitle);
        long catIdDecoy = dbHelper.createCategory(otherCategoryTitle);

        // task that should be included in db selection
        dbHelper.createTask(catId,taskTitle,"testSelect","10/27/18" );
        // task that should not be includec in db selection
        dbHelper.createTask(catIdDecoy,taskTitle,"testSelectDecoy","10/27/18" );

        String[] args = {Long.toString(catId)};
        Cursor cursor  =  dbHelper.selectTaskFromCategory(catId);

        try {
            // all rows should selected should be from category with id: catId 
            while (cursor.moveToNext()) {
                Long dbTaskCategoryId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_CATEGORY_ID));
                assertEquals( (Long) catId, (Long) dbTaskCategoryId);
            }
        }finally {
            cursor.close();
        }


        dbHelper.close();
    }



}
