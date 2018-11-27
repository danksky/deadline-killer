package com.pherodev.killddl.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pherodev.killddl.R;
import com.pherodev.killddl.activities.CategoryActivity;
import com.pherodev.killddl.activities.TaskInputActivity;
import com.pherodev.killddl.activities.TasksActivity;
import com.pherodev.killddl.database.DatabaseHelper;
import com.pherodev.killddl.gestures.ItemTouchHelperAdapter;
import com.pherodev.killddl.models.Task;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> implements ItemTouchHelperAdapter {

    public ArrayList<Task> tasks;
    private Context context;

    public static final String BUNDLE_EDIT_TASK_MODE_KEY = "EDIT_TASK_MODE";
    public static final String BUNDLE_EDIT_TASK_ID_KEY = "EDIT_TASK_ID";
    public static final String BUNDLE_EDIT_TASK_DEADLINE_KEY = "EDIT_TASK_DEADLINE";
    public static final String BUNDLE_EDIT_TASK_TITLE_KEY = "EDIT_TASK_TITLE";
    public static final String BUNDLE_EDIT_TASK_DESCRIPTION_KEY = "EDIT_TASK_DESCRIPTION";
    public static final String BUNDLE_EDIT_TASK_COMPLETED_KEY = "EDIT_TASK_COMPLETED";
    public static final String BUNDLE_EDIT_TASK_COLOR_SPINNER_POSITION_KEY = "EDIT_TASK_COLOR_SPINNER_POSITION";
    public static final String BUNDLE_EDIT_TASK_RECURRING_SPINNER_POSITION_KEY = "EDIT_TASK_RECURRING_SPINNER_POSITION";
    public static final String BUNDLE_EDIT_TASK_PRIORITY_KEY = "EDIT_TASK_PRIORITY";

    public TasksAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TasksAdapter.TasksViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent,false);
        TasksViewHolder tvh = new TasksViewHolder(v);
        context = v.getContext();
        return tvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView title = holder.taskTitle;

        title.setText(tasks.get(position).getTitle());
        // if task is completed strike through format
        if(tasks.get(position).getIsCompleted() ){
            title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            title.setPaintFlags(title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.taskDeadline.setText(tasks.get(position).getDeadline().toString());
        holder.taskDescription.setText(tasks.get(position).getDescription());
        holder.taskHoursRemaining.setText(tasks.get(position).getHoursRemaining());
        holder.colorFlag.setBackgroundColor(tasks.get(position).getColor());

        // If the deadline is happened before the current time, then set it as past due.
        if(tasks.get(position).getDeadline().before(new java.util.Date())) {
            holder.taskDeadline.setBackgroundColor(context.getResources().getColor(R.color.colorPastDue));
            System.out.println("PAST DEADLINE");
        }
        else {
            holder.taskDeadline.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        System.out.println(dbHelper.shiftPriority(
                tasks.get(fromPosition).getPriority(),
                tasks.get(fromPosition).getId(),
                tasks.get(toPosition)  .getPriority(),
                tasks.get(toPosition)  .getId()));
        // Swap runtime priorities and adapter positions
        int tempPriority = tasks.get(fromPosition).getPriority();
        tasks.get(fromPosition).setPriority(tasks.get(toPosition).getPriority());
        tasks.get(toPosition).setPriority(tempPriority);
        Collections.swap(tasks, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.deleteTask(tasks.get(position).getId());
        dbHelper.close();
        tasks.remove(position);
        notifyItemRemoved(position);

    }

    public class TasksViewHolder extends RecyclerView.ViewHolder {

        CardView taskCardView;
        TextView taskTitle;
        TextView taskDeadline;
        TextView taskDescription;
        TextView taskHoursRemaining;
        View colorFlag;

        public TasksViewHolder(final View itemView) {
            super(itemView);
            taskCardView    = (CardView) itemView.findViewById(R.id.card_view_task);
            taskTitle       = (TextView) itemView.findViewById(R.id.text_view_task_title);
            taskDeadline    = (TextView) itemView.findViewById(R.id.text_view_task_deadline);
            taskDescription = (TextView) itemView.findViewById(R.id.text_view_task_description);
            taskHoursRemaining = (TextView) itemView.findViewById(R.id.text_view_task_hours_remaining);
            colorFlag       = (View) itemView.findViewById(R.id.view_task_color);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editIntent = new Intent(view.getContext(), TaskInputActivity.class);
                    int position = getLayoutPosition();
                    Task t = tasks.get(position);
                    editIntent.putExtra(BUNDLE_EDIT_TASK_MODE_KEY, true);
                    editIntent.putExtra(BUNDLE_EDIT_TASK_ID_KEY, t.getId());
                    editIntent.putExtra(CategoryActivity.CATEGORY_ID_KEY, t.getCategoryId());
                    editIntent.putExtra(BUNDLE_EDIT_TASK_DEADLINE_KEY, t.getDeadline().getTime());
                    editIntent.putExtra(BUNDLE_EDIT_TASK_TITLE_KEY, t.getTitle());
                    editIntent.putExtra(BUNDLE_EDIT_TASK_DESCRIPTION_KEY, t.getDescription());
                    editIntent.putExtra(BUNDLE_EDIT_TASK_COMPLETED_KEY, t.getIsCompleted() );
                    editIntent.putExtra(BUNDLE_EDIT_TASK_COLOR_SPINNER_POSITION_KEY, t.getColorPosition());
                    editIntent.putExtra(BUNDLE_EDIT_TASK_RECURRING_SPINNER_POSITION_KEY, t.getRecurringSchedule() );
                    editIntent.putExtra(BUNDLE_EDIT_TASK_PRIORITY_KEY, t.getPriority());
                    ((Activity)view.getContext()).startActivityForResult(editIntent, TasksActivity.TASK_EDIT_REQUEST);
                }
            });
        }
    }
}
