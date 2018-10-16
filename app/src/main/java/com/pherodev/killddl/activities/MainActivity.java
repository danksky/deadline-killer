package com.pherodev.killddl.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.pherodev.killddl.R;

public class MainActivity extends AppCompatActivity {

    // RecyclerView https://developer.android.com/guide/topics/ui/layout/recyclerview
    private RecyclerView taskLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadTaskLists();
    }

    private void loadTaskLists() {

    }
}
