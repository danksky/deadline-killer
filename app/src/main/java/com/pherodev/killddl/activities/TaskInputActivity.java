package com.pherodev.killddl.activities;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.pherodev.killddl.R;
import com.pherodev.killddl.models.Task;

import java.util.Calendar;
import java.util.Date;

public class TaskInputActivity extends AppCompatActivity {

    private FloatingActionButton completeInputFloatingActionButton;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView deadlineTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize
        completeInputFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_input_task_complete);
        deadlineTextView = (TextView) findViewById(R.id.text_view_input_task_deadline);
        titleEditText = (EditText) findViewById(R.id.edit_text_input_task_title);
        descriptionEditText = (EditText) findViewById(R.id.edit_text_input_task_description);

        if (deadlineTextView != null)
            deadlineTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();

                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(TaskInputActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,  mDateSetListener,
                            year,month,day);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1; // January is 0
                String date = month + "/" + day + "/" + year;
                deadlineTextView.setText(date);
            }
        };

        completeInputFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verify().equals("OKAY")) {
                    // Add new task to the database, and open the tasks intent again 


                    // Task t = new Task(...);
                    // dbHelper.addTask(t);
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.coordinator_layout_activity_task_input), "Task compleet.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    public String verify() {
        // TODO: Actually implement date selection / population
        Date deadline = new Date();
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

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
