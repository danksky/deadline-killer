package com.pherodev.killddl.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.pherodev.killddl.R;
import com.pherodev.killddl.adapters.TasksAdapter;
import com.pherodev.killddl.models.Category;
import com.pherodev.killddl.models.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import dbhelpers.DatabaseHelper;

public class TasksActivity extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private RecyclerView.Adapter tasksAdapter;
    private RecyclerView.LayoutManager tasksLayoutManager;

    private FloatingActionButton createTaskFloatingActionButton;

    private int categoryId;
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Tasks");


        Intent extrasIntent = getIntent();
        if (extrasIntent != null
                && extrasIntent.getExtras() != null
                && extrasIntent.getExtras().containsKey("CATEGORY_ID")) {
            categoryId = extrasIntent.getExtras().getInt("CATEGORY_ID");
            System.out.println("In TasksActivity, have categoryId: " + categoryId);
            Toast.makeText(getApplicationContext(), "Looking at tasks of CategoryId: " + categoryId, Toast.LENGTH_LONG);
        }
        else Toast.makeText(getApplicationContext(), "did not get categoryId", Toast.LENGTH_LONG);


        // Setup RecyclerView, LayoutManager, and Adapter
        tasksRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_tasks);
        tasksRecyclerView.setHasFixedSize(true);
        tasksLayoutManager = new LinearLayoutManager(this);
        tasksRecyclerView.setLayoutManager(tasksLayoutManager);
        loadTasksFromDB();
        tasksAdapter = new TasksAdapter(tasks);
        tasksRecyclerView.setAdapter(tasksAdapter);

        createTaskFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_create_task);
        createTaskFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskInputActivity.class);
                intent.putExtra("CATEGORY_ID", categoryId);
                v.getContext().startActivity(intent);
            }
        });
    }

    // TODO: Implement single item on-long-click listener

    // TODO: Implement

    // TODO: Replace with a call  dbHelper.getTasks(taskListID);
    public void loadTasksFromDB() {
        this.tasks = new ArrayList<>();

        SQLiteDatabase database = new DatabaseHelper(this).getReadableDatabase();
        String[] args = {Integer.toString(categoryId)};
        // Get all tasks by category Id
        Cursor cursor = database.rawQuery(DatabaseHelper.Task.SELECT_ALL_TASKS_BY_CATEGORY_ID, args);

        try {
            while (cursor.moveToNext()) {
                // Variables to initialize each task object in the result set
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task._ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_DESCRIPTION));
                String deadlineString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_DUE_DATE));
                DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                Date deadline = format.parse(deadlineString);

                // Initialize new task and add to tasks ArrayList
                tasks.add(new Task(id, categoryId, title, description, deadline));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }


    }
}
