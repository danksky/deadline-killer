package com.pherodev.killddl.activities;

import android.app.Activity;
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
import com.pherodev.killddl.adapters.TasksAdapter;
import com.pherodev.killddl.models.Category;

import java.util.ArrayList;

import dbhelpers.DatabaseHelper;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoriesRecyclerView;
    private RecyclerView.Adapter categoriesAdapter;
    private RecyclerView.LayoutManager categoriesLayoutManager;

    private FloatingActionButton createCategoryFloatingActionButton;

    private ArrayList<Category> categories;

    public static final int CATEGORY_CREATE_REQUEST = 1;
    public static final int CATEGORY_EDIT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Deadline Categories");
        setContentView(R.layout.activity_category);

        categories = new ArrayList<Category>();
        loadCategoriesFromDB();

        categoriesRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_categories);
        categoriesRecyclerView.setHasFixedSize(true);

        categoriesLayoutManager = new LinearLayoutManager(this);
        categoriesRecyclerView.setLayoutManager(categoriesLayoutManager);
        loadCategoriesFromDB();

        setTitle("Category");
        categoriesAdapter = new CategoryAdapter(categories);
        categoriesRecyclerView.setAdapter(categoriesAdapter);

        createCategoryFloatingActionButton = findViewById(R.id.fab_create_category);
        createCategoryFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryInputActivity.class);
                startActivityForResult(intent, CATEGORY_CREATE_REQUEST);
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
            case CATEGORY_CREATE_REQUEST:
                loadCategoriesFromDB();
                categoriesRecyclerView.getAdapter().notifyDataSetChanged();
                return;
            case CATEGORY_EDIT_REQUEST:
                loadCategoriesFromDB();
                categoriesRecyclerView.getAdapter().notifyDataSetChanged();
                return;
        }
    }

    private void loadCategoriesFromDB() {
        if (this.categories == null)
            this.categories = new ArrayList<>();
        categories.clear();

        SQLiteDatabase database = new DatabaseHelper(this).getReadableDatabase();
        Cursor cursor = database.rawQuery(DatabaseHelper.Category.SELECT_ALL, null);

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Category._ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Category.COLUMN_TITLE));
                categories.add(new Category(id,title));
                System.out.println(id + " " + title);
            }
        } finally {
            cursor.close();
            database.close();
        }
    }
}
