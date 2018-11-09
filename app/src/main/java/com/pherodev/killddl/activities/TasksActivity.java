package com.pherodev.killddl.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.pherodev.killddl.R;
import com.pherodev.killddl.adapters.PriorityComparator;
import com.pherodev.killddl.adapters.TasksAdapter;
import com.pherodev.killddl.database.DatabaseHelper;
import com.pherodev.killddl.gestures.SimpleItemTouchHelperCallback;
import com.pherodev.killddl.models.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TasksActivity extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private TasksAdapter tasksAdapter;
    private RecyclerView.LayoutManager tasksLayoutManager;
    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.Callback callback;

    private FloatingActionButton createTaskFloatingActionButton;

    private long categoryId;
    private String categoryTitle;
    private ArrayList<Task> tasks;

    public static final int TASK_CREATE_REQUEST = 1;
    public static final int TASK_EDIT_REQUEST = 2;

    public static final String NEWEST_TASK_COUNT_KEY = "TASK_COUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeUI();

        // Setup event listeners
        createTaskFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchInputIntent = new Intent(getApplicationContext(), TaskInputActivity.class);
                launchInputIntent.putExtra(CategoryActivity.CATEGORY_ID_KEY, categoryId);
                launchInputIntent.putExtra(NEWEST_TASK_COUNT_KEY, tasks.size());
                launchInputIntent.putExtra(TasksAdapter.BUNDLE_EDIT_TASK_MODE_KEY, false);
                startActivityForResult(launchInputIntent, TASK_CREATE_REQUEST);
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

    private void initializeUI() {
        Bundle extras = (getIntent() == null ? null : getIntent().getExtras());
        if (extras != null && extras.containsKey(CategoryActivity.CATEGORY_ID_KEY)) {
            categoryId = extras.getLong(CategoryActivity.CATEGORY_ID_KEY);
            loadTasksFromDB();
            setTitle(categoryTitle + " Deadlines");
        }
        else {
            Toast.makeText(getApplicationContext(), "ERROR: Starting " + this.getClass().getName(), Toast.LENGTH_SHORT);
            setResult(CategoryActivity.RESULT_CANCELED);
            finish();
        }

        // Setup RecyclerView, LayoutManager, and Adapter
        tasksRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_tasks);
        tasksRecyclerView.setHasFixedSize(true);
        tasksLayoutManager = new LinearLayoutManager(this);
        tasksAdapter = new TasksAdapter(tasks);
        tasksRecyclerView.setLayoutManager(tasksLayoutManager);
        tasksRecyclerView.setAdapter(tasksAdapter);
        callback = new SimpleItemTouchHelperCallback(tasksAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);
        // Setup FAB
        createTaskFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_create_task);
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
                int priority = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_PRIORITY));

                // Initialize new task and add to tasks ArrayList
                tasks.add(new Task(id, categoryId, title, description, deadline, isComplete, color, priority));

                System.out.println("ADDING " + "ID " + id + " CATEGORYID " + categoryId +
                        " TITLE " + title + " DESCRIPTION " + description + " DEADLINE " + deadline + " COMPLETED " + isComplete + " COLOR " + color + " PRIORITY " + priority);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            cursor.close();
            database.close();
            // TODO: Implement some default sort type.
            Collections.sort(tasks, new PriorityComparator());
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
