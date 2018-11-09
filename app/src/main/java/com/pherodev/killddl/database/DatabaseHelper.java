package com.pherodev.killddl.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Overall Database Name and Version
    private static final String DATABASE_NAME ="KillDDL_DB";
    private static final int DATABASE_VERSION = 5;

    private Context context;

    /*
        This class outlines the Category table schema, and operations
     */
    public static class Category implements BaseColumns {
        // These strings define the Category table
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_TITLE = "title";

        // These strings are SQL code to create and drop the Category table
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT" + ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        // These strings are SQL code to select rows from the Category table
        public static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
        public static final String SELECT_CATEGORY_NAME_BY_ID = "SELECT " + COLUMN_TITLE +
                " FROM " + TABLE_NAME +
                " WHERE " + _ID + " = ?";
        public static final String WHERE_ID = _ID + " = ?;";
     }

    /*
        This class outlines the Deadline table schema, and operations
     */
    public static class Task implements BaseColumns {
        // These strings define the  Deadline table
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DUE_DATE = "due_date";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_IS_COMPLETED = "is_completed";
        public static final String COLUMN_PRIORITY = "priority";


        // TODO - Some sort of list position like https://stackoverflow.com/questions/36474658/save-reordered-recyclerview-in-sqlite

        // These strings are SQL code to create and drop the Deadline table
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                // id
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                // title
                COLUMN_TITLE + " TEXT, " +
                // desc
                COLUMN_DESCRIPTION + " TEXT, " +
                // due date
                COLUMN_DUE_DATE + " TEXT, " +
                // category id
                COLUMN_CATEGORY_ID + " INTEGER, " +
                // color
                COLUMN_COLOR + " INTEGER, " +
                // is completed?
                COLUMN_IS_COMPLETED + " INTEGER DEFAULT 0, " +
                // list position
                COLUMN_PRIORITY + " INTEGER, " +
                // make category_id foreign key
                "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " +
                Category.TABLE_NAME + "("+Category._ID+")" + ");";


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        // These strings are SQL code to select rows from the Deadline table
        public static final String SELECT_ALL_TASKS = "SELECT * FROM " + TABLE_NAME;
        public static final String SELECT_TASK_BY_ID = "SELECT * FROM " +
                TABLE_NAME + " WHERE " + _ID + " = ?";
        public static final String SELECT_ALL_TASKS_BY_CATEGORY_ID = "SELECT * FROM " +
                TABLE_NAME + " WHERE " + COLUMN_CATEGORY_ID + " = ?";
        public static final String WHERE_ID = _ID + " = ?;";

        public static final String WHERE_CATEGORY_ID = COLUMN_CATEGORY_ID + " = ?;";
    }

    // Database Creation and Updating
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Category.CREATE_TABLE); // Need to create this first
        database.execSQL(Task.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(Category.DROP_TABLE);
        database.execSQL(Task.DROP_TABLE);
        onCreate(database);
    }

    /*
     Custom methods added to Database helper, specific to Killddl
      */

    public long createCategory(String title){
        ContentValues values = new ContentValues();
        values.put(Category.COLUMN_TITLE, title);

        return getWritableDatabase().insert(Category.TABLE_NAME, null, values);
    }


    public long createTask(long categoryId, String title, String description , String deadline, boolean isComplete, int color, int priority){
        ContentValues values = new ContentValues();
        values.put(Task.COLUMN_TITLE, title);
        values.put(Task.COLUMN_DESCRIPTION, description);
        values.put(Task.COLUMN_DUE_DATE, deadline);
        values.put(Task.COLUMN_CATEGORY_ID, categoryId);
        values.put(Task.COLUMN_IS_COMPLETED, isComplete);
        values.put(Task.COLUMN_COLOR, color);
        values.put(Task.COLUMN_PRIORITY, priority);

        return getWritableDatabase().insert(Task.TABLE_NAME, null, values);

    }

    public void updateTask(long taskId, long categoryId, String title, String description , String deadline, Boolean isCompleted, int color, int priority){

        ContentValues values = new ContentValues();
        values.put(Task.COLUMN_TITLE, title);
        values.put(Task.COLUMN_DESCRIPTION, description);
        values.put(Task.COLUMN_DUE_DATE, deadline);
        values.put(Task.COLUMN_CATEGORY_ID, categoryId);
        int completedInt = isCompleted? 1 : 0;
        values.put(Task.COLUMN_IS_COMPLETED, completedInt);
        values.put(Task.COLUMN_COLOR, color);
        values.put(Task.COLUMN_PRIORITY, priority);
        String[] args = {Long.toString(taskId)};

        getWritableDatabase().update(Task.TABLE_NAME, values, Task.WHERE_ID, args);

    }


    public void deleteTask(Long taskId){
        String[] args = {Long.toString(taskId)};
        getWritableDatabase().delete(Task.TABLE_NAME, Task.WHERE_ID, args);
        return;
    }

    public void deleteCategory(Long categoryId) {
        String[] args = {Long.toString(categoryId)};
        getWritableDatabase().delete(Category.TABLE_NAME, Category.WHERE_ID, args);
        getWritableDatabase().delete(Task.TABLE_NAME, Task.WHERE_CATEGORY_ID, args);
        return;
    }

    public Cursor selectTaskFromCategory(long categoryId){

        String[] args = {Long.toString(categoryId)};
        return getWritableDatabase().rawQuery(Task.SELECT_ALL_TASKS_BY_CATEGORY_ID, args);
    }

    public String getCategoryTitleById(String categoryId) {
        String categoryTitle = "";

        String[] args = {categoryId};
        Cursor cursor = getWritableDatabase().rawQuery(Category.SELECT_CATEGORY_NAME_BY_ID, args);
        try {
            while (cursor.moveToNext()) {
                categoryTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Category.COLUMN_TITLE));
            }
        } finally {
            cursor.close();
        }
        return categoryTitle;
    }

    public com.pherodev.killddl.models.Task getTaskById(long taskId) {
        com.pherodev.killddl.models.Task task = null;

        String[] args = {Long.toString(taskId)};
        Cursor cursor = getWritableDatabase().rawQuery(Task.SELECT_TASK_BY_ID, args);
        try {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                long categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_CATEGORY_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_DESCRIPTION));
                String deadlineString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_DUE_DATE));
                DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                Date deadline = format.parse(deadlineString);
                boolean isComplete = (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_IS_COMPLETED)) != 0) ? true : false;
                int color = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_COLOR));
                int priority = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_PRIORITY));
                task = new com.pherodev.killddl.models.Task(id, categoryId, title, description, deadline, isComplete, color, priority);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }finally {
            cursor.close();
        }
        return task;
    }

    public String shiftPriority(int fromPriority, long fromId, int toPriority) {
        // increment position
        if (toPriority > fromPriority) {
            String shiftAllUp = "UPDATE " + Task.TABLE_NAME + " SET " + Task.COLUMN_PRIORITY
                    + "=" + Task.COLUMN_PRIORITY + "-1 WHERE " + Task.COLUMN_PRIORITY + ">" + fromPriority + " AND " + Task.COLUMN_PRIORITY + "<=" + toPriority +";";
            // move other items
            getWritableDatabase().execSQL(shiftAllUp);
            // move
            String moveOneDown = "UPDATE " + Task.TABLE_NAME + " SET " + Task.COLUMN_PRIORITY
                    + "=" + toPriority + " WHERE " + Task.COLUMN_PRIORITY + "=" + fromPriority +" AND " + BaseColumns._ID + "=" + fromId + ";";
            getWritableDatabase().execSQL(moveOneDown);
            return shiftAllUp + "\n" + moveOneDown;
        }
        // decrement position
        else if (toPriority < fromPriority) {
            String shiftAllDown = "UPDATE " + Task.TABLE_NAME + " SET " + Task.COLUMN_PRIORITY
                    + "=" + Task.COLUMN_PRIORITY + "+1 WHERE " + Task.COLUMN_PRIORITY + ">=" + toPriority + " AND " + Task.COLUMN_PRIORITY + "<" + fromPriority +";";
            // move other items
            getWritableDatabase().execSQL(shiftAllDown);
            // move
            String moveOneUp = "UPDATE " + Task.TABLE_NAME + " SET " + Task.COLUMN_PRIORITY
                    + "=" + toPriority + " WHERE " + Task.COLUMN_PRIORITY + "=" + fromPriority +" AND " + BaseColumns._ID + "=" + fromId + ";";
            getWritableDatabase().execSQL(moveOneUp);
            return shiftAllDown + "\n" + moveOneUp;
        }
        return "";
    }
}