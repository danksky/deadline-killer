package com.pherodev.killddl.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.pherodev.killddl.R;
//import com.pherodev.killddl.databinding.ActivityMainBinding;
import com.pherodev.killddl.adapters.CategoryAdapter;
import com.pherodev.killddl.models.Category;

import java.util.ArrayList;

import dbhelpers.DatabaseHelper;

public class CategoryActivity extends AppCompatActivity {

//    private RecyclerView taskListsRecyclerView;
//    private RecyclerView.Adapter taskListsAdapter;
//    private RecyclerView.LayoutManager taskListsLayoutManager;
//    private ActivityMainBinding mActivityMainBinding;

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private ArrayList<Category> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Task Categories");
//        mActivityMainBinding = DataBindingUtil.setContentView(
//                this, R.layout.activity_main);
        setContentView(R.layout.activity_category);
        categories = new ArrayList<Category>();
        loadCategoriesFromDB();

        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        CategoryAdapter adapter = new CategoryAdapter(categories);
        rv.setAdapter(adapter);

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


    private void loadCategoriesFromDB() {
        System.out.println("HEY THERE");
        SQLiteDatabase database = new DatabaseHelper(this).getReadableDatabase();

        Cursor cursor = database.rawQuery(DatabaseHelper.Category.SELECT_ALL_CATEGORIES, null);

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Category._ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Category.CATEGORY_COLUMN_TITLE));
                categories.add(new Category(id,title));
                System.out.println(id + " " + title);
            }
        } finally {
            cursor.close();
        }
    }
}
