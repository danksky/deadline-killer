package dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Strings
    private static final String DATABASE_NAME ="KillDDL_DB";
    private static final int DATABASE_VERSION = 1;

    // Task Table Schema

    // Category Table Schema
    public static class Category implements BaseColumns {
        // Table Definition
        public static final String CATEGORY_TABLE_NAME = "category";
        public static final String CATEGORY_COLUMN_TITLE = "title";

        // Table Creation and Deletion
        public static final String CREATE_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS " +
                CATEGORY_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CATEGORY_COLUMN_TITLE + " TEXT" + ")";
        public static final String DROP_CATEGORY_TABLE = "DROP TABLE IF EXISTS " +
                CATEGORY_TABLE_NAME;

        // Table CRUD Operations
        public static final String SELECT_ALL_CATEGORIES = "SELECT * FROM " + CATEGORY_TABLE_NAME;
    }


    // Database Creation and Updating
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Category.CREATE_CATEGORY_TABLE); // Need to create this first
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(Category.DROP_CATEGORY_TABLE);
        onCreate(database);
    }

}