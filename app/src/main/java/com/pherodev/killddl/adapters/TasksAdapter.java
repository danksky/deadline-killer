package com.pherodev.killddl.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pherodev.killddl.R;
import com.pherodev.killddl.models.Task;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    public ArrayList<Task> tasks;

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
        return tvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.taskTitle.setText(tasks.get(position).getTitle());
        holder.taskDeadline.setText(tasks.get(position).getDeadline().toString());
        holder.taskDescription.setText(tasks.get(position).getDescription());
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

    public static class TasksViewHolder extends RecyclerView.ViewHolder {

        CardView taskCardView;
        TextView taskTitle;
        TextView taskDeadline;
        TextView taskDescription;

        public TasksViewHolder(View itemView) {
            super(itemView);
            taskCardView = (CardView) itemView.findViewById(R.id.card_view_task);
            taskTitle = (TextView) itemView.findViewById(R.id.text_view_task_title);
            taskDeadline = (TextView) itemView.findViewById(R.id.text_view_task_deadline);
            taskDescription = (TextView) itemView.findViewById(R.id.text_view_task_description);
        }
    }
}
