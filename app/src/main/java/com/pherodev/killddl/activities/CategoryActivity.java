package com.pherodev.killddl.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pherodev.killddl.R;
//import com.pherodev.killddl.databinding.ActivityMainBinding;
import com.pherodev.killddl.models.Category;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

//    private RecyclerView taskListsRecyclerView;
//    private RecyclerView.Adapter taskListsAdapter;
//    private RecyclerView.LayoutManager taskListsLayoutManager;
//    private ActivityMainBinding mActivityMainBinding;

    private ArrayList<Category> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Task Categories");
//        mActivityMainBinding = DataBindingUtil.setContentView(
//                this, R.layout.activity_main);
        setContentView(R.layout.activity_category);

//        loadTaskListsFromDB();
//
//        taskListsLayoutManager = new LinearLayoutManager(this);
//        mActivityMainBinding.recyclerView.setLayoutManager(taskListsLayoutManager);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryInputActivity.class);
                startActivity(intent);
            }
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        mActivityMainBinding.fabCreateTaskList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("I GOT CLICKED");
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    private void loadTaskListsFromDB() {
//        SQLiteDatabase database = new DatabaseHelper(this).getReadableDatabase();
//
//        Cursor cursor = database.rawQuery("SELECT * FROM categories", null);
//
//        if(cursor.moveToFirst()) {
//            while(!cursor.isAfterLast()) {
//                int id = cursor.getInt(cursor.getColumnIndex("task_list_id"));
//                String title = cursor.getString(cursor.getColumnIndex("task_list_title"));
//                categories.add(new Category(id, title));
//            }
//        }
    }
}
