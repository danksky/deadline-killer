package com.pherodev.killddl.activities;

import android.app.Activity;
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

    private long categoryId;
    private String categoryTitle;
    private ArrayList<Task> tasks;

    public static final int TASK_CREATE_REQUEST = 1;
    public static final int TASK_EDIT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent extrasIntent = getIntent();
        if (extrasIntent != null
                && extrasIntent.getExtras() != null
                && extrasIntent.getExtras().containsKey("CATEGORY_ID")) {
            categoryId = extrasIntent.getExtras().getLong("CATEGORY_ID");
        }
        else Toast.makeText(getApplicationContext(), "did not get categoryId", Toast.LENGTH_SHORT);

        // Setup RecyclerView, LayoutManager, and Adapter
        tasksRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_tasks);
        tasksRecyclerView.setHasFixedSize(true);
        tasksLayoutManager = new LinearLayoutManager(this);
        tasksRecyclerView.setLayoutManager(tasksLayoutManager);
        loadTasksFromDB();

        setTitle(categoryTitle + " Deadlines");

        tasksAdapter = new TasksAdapter(tasks);
        tasksRecyclerView.setAdapter(tasksAdapter);

        createTaskFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_create_task);
        createTaskFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskInputActivity.class);
                intent.putExtra("CATEGORY_ID", categoryId);
                startActivityForResult(intent, TASK_CREATE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "result code " + resultCode + " (cancel)", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case TASK_CREATE_REQUEST:
                loadTasksFromDB();
                tasksRecyclerView.getAdapter().notifyDataSetChanged();
                return;
            case TASK_EDIT_REQUEST:
                loadTasksFromDB();
                tasksRecyclerView.getAdapter().notifyDataSetChanged();
                return;
        }
    }

    // TODO: Move this Cursor magic to DatabaseHelper
    public void loadTasksFromDB() {
        if (this.tasks == null)
            this.tasks = new ArrayList<>();
        tasks.clear();

        DatabaseHelper database = new DatabaseHelper(this);
        // Get all tasks by category Id
        Cursor cursor = database.selectTaskFromCategory(categoryId);
        categoryTitle = database.getCategoryTitleById(Long.toString(categoryId));

        try {
            while (cursor.moveToNext()) {
                // Variables to initialize each task object in the result set
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task._ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_DESCRIPTION));
                String deadlineString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_DUE_DATE));
                DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                Date deadline = format.parse(deadlineString);
                boolean isComplete = (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_IS_COMPLETED)) != 0) ? true : false;
                int color = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_COLOR));

                // Initialize new task and add to tasks ArrayList
                tasks.add(new Task(id, categoryId, title, description, deadline, isComplete, color));

                System.out.println("ADDING " + "ID " + id + " CATEGORYID " + categoryId +
                        " TITLE " + title + " DESCRIPTION " + description + " DEADLINE " + deadline + " COMPLETED " + isComplete + " COLOR " + color);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            cursor.close();
            database.close();
        }



    }

    public void deleteTaskAtPosition(int position) {
        if (position < tasks.size() && position >= 0) {
            DatabaseHelper db = new DatabaseHelper(this);
            db.deleteCategory(tasks.get(position).getId());
            tasks.remove(position);
            tasksRecyclerView.getAdapter().notifyItemRemoved(position);
        }
    }

    public void programaticallyDeleteLastTask() {
        deleteTaskAtPosition(tasks.size()-1);
    }
}
