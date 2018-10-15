package dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="KillDDL_DB";
    private static final int DATABASE_VERSION = 1;

    // task table
    public static final String CREATE_TASKS_TABLE = "CREATE TABLE IF NOT EXISTS tasks" +
            "(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }

}