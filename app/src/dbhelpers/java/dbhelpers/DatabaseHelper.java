package dbhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import com.pherodev.killddl.models.Category;

import java.sql.PreparedStatement;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Overall Database Name and Version
    private static final String DATABASE_NAME ="KillDDL_DB";
    private static final int DATABASE_VERSION = 3;

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
        public static final String COLUMN_IS_DONE = "is_done";
        public static final String COLUMN_LIST_POSITION = "list_position";


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
                // is done?
                COLUMN_IS_DONE + " INTEGER DEFAULT 0, " +
                // list position
                COLUMN_LIST_POSITION + " INTEGER, " +
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


    public long createTask(long categoryId, String title, String description , String deadline){
        ContentValues values = new ContentValues();
        values.put(Task.COLUMN_TITLE, title);
        values.put(Task.COLUMN_DESCRIPTION, description);
        values.put(Task.COLUMN_DUE_DATE, deadline);
        values.put(Task.COLUMN_CATEGORY_ID, categoryId);

        return getWritableDatabase().insert(Task.TABLE_NAME, null, values);

    }

    public void updateTask(long taskId, long categoryId, String title, String description , String deadline ){

        ContentValues values = new ContentValues();
        values.put(Task.COLUMN_TITLE, title);
        values.put(Task.COLUMN_DESCRIPTION, description);
        values.put(Task.COLUMN_DUE_DATE, deadline);
        values.put(Task.COLUMN_CATEGORY_ID, categoryId);
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


}