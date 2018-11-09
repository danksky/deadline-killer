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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.pherodev.killddl.R;
//import com.pherodev.killddl.databinding.ActivityMainBinding;
import com.pherodev.killddl.adapters.CategoryAdapter;
import com.pherodev.killddl.adapters.TasksAdapter;
import com.pherodev.killddl.gestures.OnStartDragListener;
import com.pherodev.killddl.gestures.SimpleItemTouchHelperCallback;
import com.pherodev.killddl.models.Category;

import java.util.ArrayList;

import dbhelpers.DatabaseHelper;

public class CategoryActivity extends AppCompatActivity implements OnStartDragListener {

    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoriesAdapter;
    private RecyclerView.LayoutManager categoriesLayoutManager;
    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.Callback callback;

    private FloatingActionButton createCategoryFloatingActionButton;

    private ArrayList<Category> categories;

    public static final int CATEGORY_CREATE_REQUEST = 1;
    public static final int CATEGORY_EDIT_REQUEST = 2;

    public static final String CATEGORY_ID_KEY = "CATEGORY_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initializeUI();

        // Setup event handlers
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

    private void initializeUI() {
        setTitle("Category");
        categories = new ArrayList<Category>();
        loadCategoriesFromDB();

        categoriesRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_categories);
        categoriesRecyclerView.setHasFixedSize(true);

        categoriesLayoutManager = new LinearLayoutManager(this);
        categoriesRecyclerView.setLayoutManager(categoriesLayoutManager);
        categoriesAdapter = new CategoryAdapter(categories);
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        callback = new SimpleItemTouchHelperCallback(categoriesAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(categoriesRecyclerView);
        createCategoryFloatingActionButton = findViewById(R.id.fab_create_category);
        loadCategoriesFromDB();
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

    private void deleteCategoryAtPosition(int position) {
        if (position < categories.size() && position >= 0) {
            DatabaseHelper database = new DatabaseHelper(this);
            database.deleteCategory(categories.get(position).getId());
            categories.remove(position);
            categoriesRecyclerView.getAdapter().notifyItemRemoved(position);
        }
    }

    public void programmaticallyDeleteLastCategoryEntry() {
        deleteCategoryAtPosition(categories.size()-1);
    }

    private void programmaticallyLaunchTasksActivity(int position) {
        if (position >= 0) {
            Intent intent = new Intent(this, TasksActivity.class);
            intent.putExtra("CATEGORY_ID", categories.get(position).getId());
            startActivity(intent);
        }
    }

    public void programmaticallyLaunchTasksActivityWithLatestCategory () {
        if (categories != null)
            programmaticallyLaunchTasksActivity(categories.size()-1);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

}
