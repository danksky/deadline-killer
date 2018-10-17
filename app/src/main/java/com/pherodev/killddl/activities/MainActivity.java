package com.pherodev.killddl.activities;

import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.pherodev.killddl.R;
import com.pherodev.killddl.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // RecyclerView https://developer.android.com/guide/topics/ui/layout/recyclerview
    private RecyclerView taskListsRecyclerView;
    private RecyclerView.Adapter taskListsAdapter;
    private RecyclerView.LayoutManager taskListsLayoutManager;
    private ActivityMainBinding mActivityMainBinding;

    private FloatingActionButton createTaskListFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Task Lists");
        mActivityMainBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main);

        // now get list of taskLists with SQLite

        setContentView(R.layout.activity_main);
        loadTaskLists();
    }

    private void loadTaskLists() {

    }
}
