package com.pherodev.killddl.activities;

import android.content.Intent;
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
import com.pherodev.killddl.models.Task;

import java.util.ArrayList;
import java.util.Date;

public class TasksActivity extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private RecyclerView.Adapter tasksAdapter;
    private RecyclerView.LayoutManager tasksLayoutManager;

    private FloatingActionButton createTaskFloatingActionButton;

    private int taskListID;
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent extrasIntent = getIntent();
        if (extrasIntent != null
                && extrasIntent.getExtras() != null
                && extrasIntent.getExtras().containsKey("CATEGORY_ID"))
            taskListID = extrasIntent.getExtras().getInt("CATEGORY_ID");
        else Toast.makeText(getApplicationContext(), "did not get taskListID", Toast.LENGTH_LONG);


        // Setup RecyclerView, LayoutManager, and Adapter
        tasksRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_tasks);
        tasksRecyclerView.setHasFixedSize(true);
        tasksLayoutManager = new LinearLayoutManager(this);
        tasksRecyclerView.setLayoutManager(tasksLayoutManager);
        initTasks();
        tasksAdapter = new TasksAdapter(tasks);
        tasksRecyclerView.setAdapter(tasksAdapter);

        createTaskFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_create_task);
        createTaskFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startTaskInputActivityIntent = new Intent(getApplicationContext(), TaskInputActivity.class);
                startActivity(startTaskInputActivityIntent);
            }
        });
    }

    // TODO: Implement single item on-long-click listener

    // TODO: Implement

    // TODO: Replace with a call  dbHelper.getTasks(taskListID);
    public void initTasks() {
        this.tasks = new ArrayList<>();
        tasks.add(new Task("0Tickky Bobby", new Date()));
        tasks.add(new Task("Tickky1 Bobby", new Date()));
        tasks.add(new Task("Tickky Bo2bby", new Date()));
        tasks.add(new Task("Tickky Bobb3y", new Date()));
    }
}
