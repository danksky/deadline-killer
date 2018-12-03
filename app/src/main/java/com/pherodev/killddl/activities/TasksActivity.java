package com.pherodev.killddl.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pherodev.killddl.R;
import com.pherodev.killddl.adapters.ColorComparator;
import com.pherodev.killddl.adapters.DateComparator;
import com.pherodev.killddl.adapters.PriorityComparator;
import com.pherodev.killddl.adapters.TasksAdapter;
import com.pherodev.killddl.database.DatabaseHelper;
import com.pherodev.killddl.gestures.SimpleItemTouchHelperCallback;
import com.pherodev.killddl.models.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TasksActivity extends AppCompatActivity {

    private CalendarView tasksCalendarView;
    private RecyclerView tasksRecyclerView;
    private TasksAdapter tasksAdapter;
    private RecyclerView.LayoutManager tasksLayoutManager;
    private ItemTouchHelper itemTouchHelper;
    private SimpleItemTouchHelperCallback callback;

    private FloatingActionButton createTaskFloatingActionButton;

    private long categoryId;
    private String categoryTitle;
    private Date selectedDate;
    private ArrayList<Task> filteredTasks;
    private ArrayList<Task> tasks;




    // MENU
    private DisplayMode tasksDisplayMode;
    private MenuItem tasksDisplayCalendarItem;
    private MenuItem tasksDisplayListItem;
    private MenuItem sortTaskSpinnerItem;
    private Spinner sortSpinner;
    private SortMode taskSortMode;


    public static final int TASK_CREATE_REQUEST = 1;
    public static final int TASK_EDIT_REQUEST = 2;

    public static final String NEWEST_TASK_COUNT_KEY = "TASK_COUNT";

    enum DisplayMode {
        DISPLAY_CALENDAR, DISPLAY_LIST
    }

    enum SortMode{
        PRIORITY,DATE,COLOR
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_menu, menu);
        tasksDisplayCalendarItem =  menu.findItem(R.id.action_calendar);
        tasksDisplayListItem =  menu.findItem(R.id.action_list);
        tasksDisplayCalendarItem.setVisible(false);

        // setup sortBy spinner
        sortTaskSpinnerItem = menu.findItem(R.id.sort_spinner);
        sortSpinner = (Spinner) sortTaskSpinnerItem.getActionView();

        List<String> sortOptions = new ArrayList<>();
        sortOptions.add("By Priority");
        sortOptions.add("By Deadline");
        sortOptions.add("By Color");

        ArrayAdapter<String> sortDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        sortDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortDataAdapter);


//         Setup sortBy toolbar dropdown selected listener

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        taskSortMode = SortMode.PRIORITY;
//                        sortComparator = new PriorityComparator();
                        callback.enableLongPress();
//                        Collections.sort(tasks, new PriorityComparator());
                        break;
                    case 1:
                        taskSortMode = SortMode.DATE;
//                        Collections.sort(tasks, new DateComparator());
//                        sortComparator = new DateComparator();
                        callback.disableLongPress();
                        break;
                    case 2:
                        taskSortMode = SortMode.COLOR;
//                        Collections.sort(tasks, new ColorComparator());
//                        sortComparator = new ColorComparator();
                        callback.disableLongPress();
                }
//
                updateUI(DisplayMode.DISPLAY_LIST);


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_calendar:
                tasksDisplayMode = DisplayMode.DISPLAY_CALENDAR;
                updateUI(tasksDisplayMode);
                break;
            case R.id.action_list:
                tasksDisplayMode = DisplayMode.DISPLAY_LIST;
                updateUI(tasksDisplayMode);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // init sortmode
        taskSortMode = SortMode.PRIORITY;
        setSupportActionBar(toolbar);

        initializeUI();

        // Setup event listeners
        createTaskFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchInputIntent = new Intent(getApplicationContext(), TaskInputActivity.class);
                launchInputIntent.putExtra(CategoryActivity.CATEGORY_ID_KEY, categoryId);
                launchInputIntent.putExtra(NEWEST_TASK_COUNT_KEY, tasks.size());
                launchInputIntent.putExtra(TasksAdapter.BUNDLE_EDIT_TASK_MODE_KEY, false);
                startActivityForResult(launchInputIntent, TASK_CREATE_REQUEST);
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
            case TASK_CREATE_REQUEST:
                loadTasksFromDB();
                tasksRecyclerView.getAdapter().notifyDataSetChanged();
                return;
            case TASK_EDIT_REQUEST:
                loadTasksFromDB();
                tasksRecyclerView.getAdapter().notifyDataSetChanged();
                return;
        }
    }

    private void initializeUI() {
        tasksDisplayMode = DisplayMode.DISPLAY_CALENDAR;
        selectedDate = new Date(new Date().getTime()+86400000);
        tasksCalendarView = (CalendarView) findViewById(R.id.calendar_view_tasks);
        Bundle extras = (getIntent() == null ? null : getIntent().getExtras());
        if (extras != null && extras.containsKey(CategoryActivity.CATEGORY_ID_KEY)) {
            categoryId = extras.getLong(CategoryActivity.CATEGORY_ID_KEY);
            loadTasksFromDB();
            setTitle(categoryTitle + " Deadlines");
        } else {
            Toast.makeText(getApplicationContext(), "ERROR: Starting " + this.getClass().getName(), Toast.LENGTH_SHORT);
            setResult(CategoryActivity.RESULT_CANCELED);
            finish();
        }

        tasksCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = new GregorianCalendar( year, month, dayOfMonth+1 ).getTime();
                Log.d("onSelectedDayChange", Long.toString(selectedDate.getTime()/86400000));
                filter(selectedDate);
            }
        });

        // Setup RecyclerView, LayoutManager, and Adapter
        tasksRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_tasks);
        tasksRecyclerView.setHasFixedSize(true);
        tasksLayoutManager = new LinearLayoutManager(this);
        tasksAdapter = new TasksAdapter(filteredTasks);
        tasksRecyclerView.setLayoutManager(tasksLayoutManager);
        tasksRecyclerView.setAdapter(tasksAdapter);
        callback = new SimpleItemTouchHelperCallback(tasksAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);
        // Setup FAB
        createTaskFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_create_task);
    }

    private void updateUI (DisplayMode displayMode) {
        switch (displayMode) {
            case DISPLAY_CALENDAR:
                tasksDisplayListItem.setVisible(true);
                tasksDisplayCalendarItem.setVisible(false);
                tasksCalendarView.setVisibility(View.VISIBLE);
                filter(selectedDate);
                break;
            case DISPLAY_LIST:
                tasksDisplayListItem.setVisible(false);
                tasksDisplayCalendarItem.setVisible(true);
                tasksCalendarView.setVisibility(View.GONE);
                unfilter();
                break;
        }
    }

    // TODO: Move this Cursor magic to DatabaseHelper
    public void loadTasksFromDB() {
        if (this.tasks == null)
            this.tasks = new ArrayList<>();
        if (this.filteredTasks == null)
            this.filteredTasks = new ArrayList<>();
        tasks.clear();

        DatabaseHelper database = new DatabaseHelper(this);
        // Get all tasks by category Id
        Cursor cursor = database.selectTaskFromCategory(categoryId);
        categoryTitle = database.getCategoryTitleById(Long.toString(categoryId));

        try {
            while (cursor.moveToNext()) {
                // Variables to initialize each task object in the result set
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task._ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_DESCRIPTION));
                String deadlineString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_DUE_DATE));
                DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                Date deadline = format.parse(deadlineString);
                boolean isComplete = (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_IS_COMPLETED)) != 0) ? true : false;
                int color = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_COLOR));
                int priority = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Task.COLUMN_PRIORITY));
                int recurringSchedule = cursor.getInt(cursor.getColumnIndex((DatabaseHelper.Task.COLUMN_RECURRING)));


                if(recurringSchedule != 0){
                    Date date = new Date();
                    if( date.after(deadline) ){


                        long diff = date.getTime() - deadline.getTime();
                        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        int recurrenceInterval = 1;
                        switch (recurringSchedule){
                            case 2:
                                recurrenceInterval = 7;
                                break;
                            case 3:
                                recurrenceInterval = 30;
                                break;
                        }
                        int numNewTasks = days/recurrenceInterval + 1;

                        for(int i = 1; i <= numNewTasks; i++){
                            Date newDeadline = new Date(deadline.getTime() + (i * recurrenceInterval * TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS) ) );

                            int nextRecurringSched = 0;
                            if(i == numNewTasks){
                                nextRecurringSched = recurringSchedule;
                            }

                            long newId = database.createTask(categoryId,title,description, newDeadline.toString() , false, color, priority, nextRecurringSched );
                            tasks.add(new Task(newId, categoryId, title, description, newDeadline, isComplete, color, priority, recurringSchedule));

                        }

                        database.updateTask(id,categoryId,title,description, deadline.toString(),isComplete,color,priority,0);
                        recurringSchedule = 0;


                    }


                }

                // Initialize new task and add to tasks ArrayList
                tasks.add(new Task(id, categoryId, title, description, deadline, isComplete, color, priority, recurringSchedule));

                System.out.println("ADDING " + "ID " + id + " CATEGORYID " + categoryId +
                        " TITLE " + title + " DESCRIPTION " + description + " DEADLINE " + deadline + " COMPLETED " + isComplete + " COLOR " + color + " PRIORITY " + priority + " RECURRING SCHEDULE " + recurringSchedule);

            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            cursor.close();
            database.close();
            // TODO: Implement some default sort type
            switch (taskSortMode){
                case PRIORITY:
                    Collections.sort(tasks, new PriorityComparator());
                    break;
                case DATE:
                    Collections.sort(tasks, new DateComparator());
                    break;
                case COLOR:
                    Collections.sort(tasks, new ColorComparator());
                    break;
            }

            filter(selectedDate);
        }

    }

    public void deleteTaskAtPosition(int position) {
        if (position < tasks.size() && position >= 0) {
            DatabaseHelper db = new DatabaseHelper(this);
            db.deleteCategory(tasks.get(position).getId());
            tasks.remove(position);
            tasksRecyclerView.getAdapter().notifyItemRemoved(position);
        }
    }

    private void filter(Date selectedDate) {
        filteredTasks.clear();
        for (Task t : tasks) {
            if (t.getDeadline().getTime() / 86400000 == selectedDate.getTime() / 86400000)
                filteredTasks.add(t);
        }
        Collections.sort(filteredTasks, new DateComparator());
        if (tasksAdapter != null)
            tasksAdapter.notifyDataSetChanged();
    }

    private void unfilter() {
        // This here is jank. Lol. 
        loadTasksFromDB();
        filteredTasks.clear();
        filteredTasks.addAll(tasks);
        if (tasksAdapter != null)
            tasksAdapter.notifyDataSetChanged();
    }

    public void programaticallyDeleteLastTask() {
        deleteTaskAtPosition(tasks.size()-1);
    }


}
