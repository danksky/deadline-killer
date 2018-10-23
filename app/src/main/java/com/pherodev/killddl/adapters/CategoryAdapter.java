package com.pherodev.killddl.adapters;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pherodev.killddl.R;
import com.pherodev.killddl.activities.CategoryInputActivity;
import com.pherodev.killddl.activities.TasksActivity;
import com.pherodev.killddl.models.Category;

import java.util.ArrayList;

import dbhelpers.DatabaseHelper;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public ArrayList<Category> categories;

    public CategoryAdapter(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_card, viewGroup, false);
        CategoryViewHolder cvh = new CategoryViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder categoryViewHolder, int i) {
        categoryViewHolder.categoryTitle.setText(categories.get(i).getTitle());
        categoryViewHolder.categoryId = Long.valueOf(categories.get(i).getId());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView categoryCardView;
        TextView categoryTitle;
        long categoryId;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryCardView = (CardView)itemView.findViewById(R.id.card_view_category);
            categoryTitle = (TextView)itemView.findViewById(R.id.category_title);

            categoryCardView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // Based on which item was clicked, we want to open the Tasks activity with that ID
                    // Maybe we do this in OnCreateViewHolder?
                    Intent intent = new Intent (v.getContext(), TasksActivity.class);
                    intent.putExtra("CATEGORY_ID", categoryId);
                    v.getContext().startActivity(intent);
                }
            });

            categoryCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getLayoutPosition();
                    DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
                    dbHelper.deleteCategory(categories.get(position).getId());
                    dbHelper.close();
                    categories.remove(position);
                    notifyItemRemoved(position);
                    return true;
                }
            });
        }
    }

}
