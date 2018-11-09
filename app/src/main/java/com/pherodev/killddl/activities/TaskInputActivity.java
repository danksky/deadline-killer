package com.pherodev.killddl.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pherodev.killddl.R;
import com.pherodev.killddl.receivers.NotificationPublisher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dbhelpers.DatabaseHelper;

public class TaskInputActivity extends AppCompatActivity {

    private FloatingActionButton completeInputFloatingActionButton;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private DatePickerDialog.OnDateSetListener taskInputDateSetListener;
    private TextView deadlineTextView;
    private CheckBox completedChechBox;

    private Spinner colorSpinner;
    private List<String> colors;

    private int taskCount;
    private long categoryId;

    private boolean editMode = false;

    private Date deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_input);
        initializeUI();
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String colorString = ((TextView) parent.getChildAt(0)).getText().toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor(colorString));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Intent extrasIntent = getIntent();

        if (deadlineTextView != null)
            deadlineTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();

                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(TaskInputActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth, taskInputDateSetListener,
                            year, month, day);

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
                String date = month + 1 + "/" + day + "/" + year;

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
                    // Submit task to the database, and open the tasks intent again
                    String title = titleEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    String deadlineText = deadline.toString();
                    Boolean isCompleted = completedChechBox.isChecked();
                    int color = Color.parseColor(colorSpinner.getSelectedItem().toString());
                    System.out.println("COLOR SELECTED = " + color);

                    DatabaseHelper database = new DatabaseHelper(TaskInputActivity.this);

                    if (editMode) {
                        long taskId = extrasIntent.getExtras().getLong("EDIT_TASK_ID");
                        int priority = extrasIntent.getExtras().getInt("EDIT_TASK_PRIORITY");
                        database.updateTask(taskId, categoryId, title, description, deadlineText, isCompleted, color, priority);
                    } else
                        database.createTask(categoryId, title, description, deadlineText, isCompleted, color, taskCount);
                    database.close();

                    // Restart the updated Tasks intent
                    if (editMode)
                        setResult(TasksActivity.TASK_EDIT_REQUEST);
                    else
                        setResult(TasksActivity.TASK_CREATE_REQUEST);

                    scheduleNotification(getApplicationContext(), title, 1000, (int) new Date().getTime());
                    finish();
                }
            }
        });
    }

    private void initializeUI() {
        setTitle("Add a new Deadline");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent extrasIntent = getIntent();
        // TODO: Change this to EDIT_MODE true/false
        if (extrasIntent != null
                && extrasIntent.getExtras() != null
                && extrasIntent.getExtras().containsKey("CATEGORY_ID")) {
            taskCount = new Integer(extrasIntent.getExtras().getInt("TASK_COUNT"));
            System.out.println("Task count: " + taskCount);
            // TODO: Move this to the final entry method
            categoryId = new Long(extrasIntent.getExtras().getLong("CATEGORY_ID"));
            System.out.println("In TaskInputActivity, have categoryId: " + categoryId);
            Toast.makeText(getApplicationContext(), "Looking at tasks of CategoryId: " +
                    categoryId, Toast.LENGTH_LONG).show();
        }
        else if (extrasIntent != null
                && extrasIntent.getExtras() != null
                && extrasIntent.getExtras().containsKey("EDIT_TASK_MODE")) {
            editMode = extrasIntent.getExtras().getBoolean("EDIT_TASK_MODE");
            System.out.println("Prev priority " + extrasIntent.getExtras().getInt("EDIT_TASK_PRIORITY"));
            Toast.makeText(getApplicationContext(), "Edit mode enabled.", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "No bundle.", Toast.LENGTH_LONG).show();
            setResult(TasksActivity.RESULT_CANCELED);
            finish();
        }

        // Initialize
        completeInputFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_input_task_complete);
        deadlineTextView = (TextView) findViewById(R.id.text_view_input_task_deadline);
        titleEditText = (EditText) findViewById(R.id.edit_text_input_task_title);
        descriptionEditText = (EditText) findViewById(R.id.edit_text_input_task_description);
        completedChechBox = (CheckBox) findViewById(R.id.check_box_input_task_completed);


        // Color spinner stuff
        colorSpinner = (Spinner) findViewById(R.id.spinner_input_task_color);
        colors = new ArrayList<String>();
        colors.add("Red");
        colors.add("Blue");
        colors.add("Green");
        colors.add("Purple");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(dataAdapter);

        // Edit mode stuff
        if (editMode) {
            // Populate accordingly
            categoryId = extrasIntent.getExtras().getLong("EDIT_TASK_CATEGORY_ID");
            Date date = new Date();
            date.setTime(extrasIntent.getExtras().getLong("EDIT_TASK_DEADLINE"));
            deadline = date;
            deadlineTextView.setText(date.toString());
            titleEditText.setText(extrasIntent.getExtras().getString("EDIT_TASK_TITLE"));
            descriptionEditText.setText(extrasIntent.getExtras().getString("EDIT_TASK_DESCRIPTION"));
            completedChechBox.setChecked( extrasIntent.getExtras().getBoolean("EDIT_TASK_COMPLETED"));
            colorSpinner.setSelection(extrasIntent.getExtras().getInt("EDIT_TASK_COLOR_SPINNER_POSITION"));
        }
    }

    /* TODO: Write logic to update notification according to deadline.
        - Programmatically populate notification field at time of reminder.
        - That means create unique (re-constructable) identifiers tags for each task
            - Currently, the time is being used as a unique int
            - That means remove the title and content from the builder below
        - In general move the building from below (it make it harder to replicate to cancel)
            - to the NotificationPublisher
        - That means passing something that cannot change about the task when editing.
            - There's no need to cancel if you're creating a Task for the first time.
    */

    private void scheduleNotification(Context context, String title, long delay, int notificationId) {//delay is after how much time(in millis) from current time you want to schedule the notification

        Intent intent = new Intent(context, CategoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent launchIntent = PendingIntent.getActivity(context, notificationId, intent, 0);

        NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(title + " is due.")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setLargeIcon(((BitmapDrawable) getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher_foreground)).getBitmap())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(launchIntent);

        Notification notification = notificationbuilder.build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
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
