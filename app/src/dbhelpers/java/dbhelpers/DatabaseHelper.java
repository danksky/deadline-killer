package dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Strings
    private static final String DATABASE_NAME ="KillDDL_DB";
    private static final int DATABASE_VERSION = 1;

    // Task Table Strings

    // Category Table Class
    public static class Category implements BaseColumns {
        public static final String CATEGORY_TABLE_NAME = "category";
        public static final String CATEGORY_COLUMN_TITLE = "title";
        public static final String CREATE_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS " +
                CATEGORY_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CATEGORY_COLUMN_TITLE + " TEXT" + ")";
        public static final String DROP_CATEGORY_TABLE = "DROP TABLE IF EXISTS " +
                CATEGORY_TABLE_NAME;
    }


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Category.CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(Category.DROP_CATEGORY_TABLE);
        onCreate(database);
    }

}