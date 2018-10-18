package com.pherodev.killddl.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.pherodev.killddl.R;
import com.pherodev.killddl.databinding.ActivityMainBinding;
import com.pherodev.killddl.models.TaskList;

import java.util.ArrayList;

import dbhelpers.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    // RecyclerView https://developer.android.com/guide/topics/ui/layout/recyclerview
    private RecyclerView taskListsRecyclerView;
    private RecyclerView.Adapter taskListsAdapter;
    private RecyclerView.LayoutManager taskListsLayoutManager;
    private ActivityMainBinding mActivityMainBinding;

    private ArrayList<TaskList> taskLists;

    private FloatingActionButton createTaskListFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Task Lists");
        mActivityMainBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main);
        setContentView(R.layout.activity_main);
        loadTaskListsFromDB();
        



    }

    private void loadTaskListsFromDB() {
        SQLiteDatabase database = new DatabaseHelper(this).getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM taskLists", null);

        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex("task_list_id"));
                String title = cursor.getString(cursor.getColumnIndex("task_list_title"));
                taskLists.add(new TaskList(id, title));
            }
        }
    }
}
