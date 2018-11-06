package com.pherodev.killddl.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pherodev.killddl.R;
import com.pherodev.killddl.activities.TaskInputActivity;
import com.pherodev.killddl.activities.TasksActivity;
import com.pherodev.killddl.gestures.ItemTouchHelperAdapter;
import com.pherodev.killddl.models.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;

import dbhelpers.DatabaseHelper;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> implements ItemTouchHelperAdapter {

    public ArrayList<Task> tasks;
    private Context context;

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
        holder.colorFlag.setBackgroundColor(tasks.get(position).getColor());
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
        View colorFlag;

        public TasksViewHolder(final View itemView) {
            super(itemView);
            taskCardView    = (CardView) itemView.findViewById(R.id.card_view_task);
            taskTitle       = (TextView) itemView.findViewById(R.id.text_view_task_title);
            taskDeadline    = (TextView) itemView.findViewById(R.id.text_view_task_deadline);
            taskDescription = (TextView) itemView.findViewById(R.id.text_view_task_description);
            colorFlag       = (View) itemView.findViewById(R.id.view_task_color);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editIntent = new Intent(view.getContext(), TaskInputActivity.class);
                    int position = getLayoutPosition();
                    Task t = tasks.get(position);
                    editIntent.putExtra("EDIT_TASK_MODE", true);
                    editIntent.putExtra("EDIT_TASK_ID", t.getId());
                    editIntent.putExtra("EDIT_TASK_CATEGORY_ID", t.getCategoryId());
                    editIntent.putExtra("EDIT_TASK_DEADLINE", t.getDeadline().getTime());
                    editIntent.putExtra("EDIT_TASK_TITLE", t.getTitle());
                    editIntent.putExtra("EDIT_TASK_DESCRIPTION", t.getDescription());
                    editIntent.putExtra("EDIT_TASK_COMPLETED", t.getIsCompleted() );
                    editIntent.putExtra("EDIT_TASK_COLOR_SPINNER_POSITION", t.getColorPosition());
                    ((Activity)view.getContext()).startActivityForResult(editIntent, TasksActivity.TASK_EDIT_REQUEST);
                }
            });
        }
    }
}
