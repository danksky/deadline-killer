package com.pherodev.killddl.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pherodev.killddl.R;

import dbhelpers.DatabaseHelper;

public class CategoryInputActivity extends AppCompatActivity {

    private FloatingActionButton completeInputFloatingActionButton;
    private EditText titleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Add a new Category");
        setContentView(R.layout.activity_category_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        completeInputFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_input_category_complete);
        titleEditText = (EditText) findViewById(R.id.edit_text_input_category_title);

        if (titleEditText != null) {
            titleEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        completeInputFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper database = new DatabaseHelper(CategoryInputActivity.this);
                long newRowId = database.createCategory(titleEditText.getText().toString() );
                Toast.makeText(view.getContext(), "The new row ID is: " + newRowId, Toast.LENGTH_LONG).show();
                Snackbar.make(view, "Added new category: " + titleEditText.getText(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                startActivity(intent);
                database.close();
            }
        });
    }

}
