package com.pherodev.killddl.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pherodev.killddl.R;

import java.util.Calendar;
import java.util.Date;

import dbhelpers.DatabaseHelper;

public class TaskInputActivity extends AppCompatActivity {

    private FloatingActionButton completeInputFloatingActionButton;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private DatePickerDialog.OnDateSetListener taskInputDateSetListener;
    private TextView deadlineTextView;
    private long categoryId;

    private boolean editMode = false;

    private Date deadline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add a new Deadline");
        setContentView(R.layout.activity_task_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent extrasIntent = getIntent();
        if (extrasIntent != null
                && extrasIntent.getExtras() != null
                && extrasIntent.getExtras().containsKey("CATEGORY_ID")) {
            categoryId = new Long(extrasIntent.getExtras().getLong("CATEGORY_ID"));
            System.out.println("In TaskInputActivity, have categoryId: " + categoryId);
            Toast.makeText(getApplicationContext(), "Looking at tasks of CategoryId: " +
                    categoryId, Toast.LENGTH_LONG).show();
        }
        else if (extrasIntent != null
                && extrasIntent.getExtras() != null
                && extrasIntent.getExtras().containsKey("EDIT_TASK_MODE")) {
            editMode = extrasIntent.getExtras().getBoolean("EDIT_TASK_MODE");
            Toast.makeText(getApplicationContext(), "Edit mode enabled.", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getApplicationContext(), "No bundle.", Toast.LENGTH_LONG).show();

        // Initialize
        completeInputFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_input_task_complete);
        deadlineTextView = (TextView) findViewById(R.id.text_view_input_task_deadline);
        titleEditText = (EditText) findViewById(R.id.edit_text_input_task_title);
        descriptionEditText = (EditText) findViewById(R.id.edit_text_input_task_description);

        if (editMode) {
            // Populate accordingly
            categoryId = extrasIntent.getExtras().getLong("EDIT_TASK_CATEGORY_ID");
            Date date = new Date();
            date.setTime(extrasIntent.getExtras().getLong("EDIT_TASK_DEADLINE"));
            deadline = date;
            deadlineTextView.setText(date.toString());
            titleEditText.setText(extrasIntent.getExtras().getString("EDIT_TASK_TITLE"));
            descriptionEditText.setText(extrasIntent.getExtras().getString("EDIT_TASK_DESCRIPTION"));
        }

        if (deadlineTextView != null)
            deadlineTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();

                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(TaskInputActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,  taskInputDateSetListener,
                            year,month,day);

                    dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            deadlineTextView.setError("MUST ENTER DATE");
                        }
                    });
                    dialog.show();
                }
            });
        taskInputDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1; // January is 0
                String date = month + "/" + day + "/" + year;

                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                deadline = cal.getTime();
                System.out.println("INPUT DEADLINE " + deadline);
                if (deadlineTextView.getError() != null)
                    deadlineTextView.setError(null);
                deadlineTextView.setText(date);
            }
        };

        completeInputFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verify().equals("OKAY")) {
                    // Add new task to the database, and open the tasks intent again
                    String title = titleEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    String deadlineText = deadline.toString();

                    DatabaseHelper database = new DatabaseHelper(TaskInputActivity.this);

                    if (editMode) {
                        long taskId = extrasIntent.getExtras().getLong("EDIT_TASK_ID");
                        database.updateTask(taskId, categoryId, title, description, deadlineText);
                    } else
                        database.createTask(categoryId, title, description, deadlineText);
                    database.close();

                    // Restart the updated Tasks intent
                    if (editMode)
                        setResult(TasksActivity.TASK_EDIT_REQUEST);
                    else
                        setResult(TasksActivity.TASK_CREATE_REQUEST);
                    finish();
                }
            }
        });
    }

    public String verify() {
        String title = titleEditText.getText().toString();

        if (deadline == null) {
            deadlineTextView.setError("BAD DATE");
            return "NOT OKAY, K?";
        }
        if (title.equals("")) {
            titleEditText.setError("TITLE EMPTY");
            return "NOT OKAY, K?";
        }
        if (title.length() > 128) {
            titleEditText.setError("TITLE TOO LONG");
            return "NOT OKAY, K?";
        }
        return "OKAY";
    }
}
