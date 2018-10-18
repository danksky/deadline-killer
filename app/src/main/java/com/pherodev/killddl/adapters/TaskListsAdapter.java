//package com.pherodev.killddl.adapters;
//
//import android.databinding.DataBindingUtil;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.pherodev.killddl.databinding.TaskListCardBinding;
//import com.pherodev.killddl.models.Category;
//
//import java.util.ArrayList;
//
//public class TaskListsAdapter extends RecyclerView.Adapter<TaskListsAdapter.TaskListsViewHolder> {
//
//    private ArrayList<Category> mTaskLists;
//
//    public TaskListsAdapter(ArrayList<Category> taskLists) {
//        mTaskLists = taskLists;
//    }
//
//
//    @Override
//    public void onBindViewHolder(TaskListsViewHolder holder, int position) {
//        final Category taskList = mTaskLists.get(position);
//      //  holder.getBinding().setVariable(BR., taskList)
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return mTaskLists.size();
//    }
//
//    public static class TaskListsViewHolder extends RecyclerView.ViewHolder {
//
//        private TaskListCardBinding taskListCardBinding;
//
//        public TaskListsViewHolder(View v) {
//            super(v);
//            taskListCardBinding = DataBindingUtil.bind(v);
//        }
//
//        public TaskListCardBinding getBinding() {
//            return taskListCardBinding;
//        }
//
//    }
//
//
//}
