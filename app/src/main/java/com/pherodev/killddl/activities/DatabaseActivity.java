package com.pherodev.killddl.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pherodev.killddl.R;

import dbhelpers.DatabaseHelper;

public class DatabaseActivity extends AppCompatActivity {

    private TextView sqliteQueryTextView;
    private TextView sqliteResultSetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        sqliteQueryTextView = (TextView) findViewById(R.id.text_view_sqlite_query);
        sqliteResultSetTextView = (TextView) findViewById(R.id.text_view_sqlite_result_set);

        sqliteQueryTextView.setText(DatabaseHelper.Category.CREATE_TABLE + "\n\n" + DatabaseHelper.Category.SELECT_ALL);

        // however you decide to format the results that are returned from dbHelper's methods you write
        sqliteResultSetTextView.setText("here go results");

    }
}
