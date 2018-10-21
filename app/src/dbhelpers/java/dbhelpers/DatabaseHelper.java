package dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Overall Database Name and Version
    private static final String DATABASE_NAME ="KillDDL_DB";
    private static final int DATABASE_VERSION = 1;

    /*
        This class outlines the Category table schema, and operations
     */
    public static class Category implements BaseColumns {
        // These strings define the Category table
        public static final String CATEGORY_TABLE_NAME = "category";
        public static final String CATEGORY_COLUMN_TITLE = "title";

        // These strings are SQL code to create and drop the Category table
        public static final String CREATE_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS " +
                CATEGORY_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_COLUMN_TITLE + " TEXT" + ")";
        public static final String DROP_CATEGORY_TABLE = "DROP TABLE IF EXISTS " +
                CATEGORY_TABLE_NAME;

        // These strings are SQL code to select rows from the Category table
        public static final String SELECT_ALL_CATEGORIES = "SELECT * FROM " + CATEGORY_TABLE_NAME;
    }

    /*
        This class outlines the Deadline table schema, and operations
     */
    public static class Deadline implements BaseColumns {
        // These strings define the  Deadline table
        public static final String DEADLINE_TABLE_NAME = "deadline";
        public static final String DEADLINE_COLUMN_TITLE = "title";
        public static final String DEADLINE_COLUMN_DESCRIPTION = "description";
        public static final String DEADLINE_COLUMN_DUE_DATE = "due_date";
        public static final String DEADLINE_COLUMN_CATEGORY_ID = "category_id";

        // These strings are SQL code to create and drop the Deadline table
        public static final String CREATE_DEADLINE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                DEADLINE_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DEADLINE_COLUMN_TITLE + " TEXT, " +
                DEADLINE_COLUMN_DESCRIPTION + " TEXT, " +
                DEADLINE_COLUMN_DUE_DATE + " TEXT, " +
                DEADLINE_COLUMN_CATEGORY_ID + " INTEGER, " +
                // Turning DEADLINE_COLUMN_CATEGORY_ID into foreign key that references
                // the Category table's column, id."
                "FOREIGN KEY("+DEADLINE_COLUMN_CATEGORY_ID+") REFERENCES "+Category.CATEGORY_TABLE_NAME+"("+Category._ID+");";

        public static final String DROP_DEADLINE_TABLE = "DROP TABLE IF EXISTS " +
                DEADLINE_TABLE_NAME;

        // These strings are SQL code to select rows from the Deadline table
        public static final String SELECT_ALL_DEADLINES = "SELECT * FROM " + DEADLINE_TABLE_NAME;
        public static final String SELECT_ALL_DEADLINES_BY_CATEGORY_ID = "SELECT * FROM " +
                DEADLINE_TABLE_NAME +
                "WHERE " + DEADLINE_COLUMN_CATEGORY_ID + " = ?";

    }

    // Database Creation and Updating
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Category.CREATE_CATEGORY_TABLE); // Need to create this first
        database.execSQL(Deadline.CREATE_DEADLINE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(Category.DROP_CATEGORY_TABLE);
        database.execSQL(Deadline.DROP_DEADLINE_TABLE);
        onCreate(database);
    }

}