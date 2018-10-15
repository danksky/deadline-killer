package dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // task table
    public static final String CREATE_TASK_TABLE = "";

    // task list table



    public DatabaseHelper(Context context) {

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // sqLiteDatabase.execSQL();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
