package com.pherodev.killddl.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pherodev.killddl.R;
import com.pherodev.killddl.activities.TasksActivity;
import com.pherodev.killddl.database.DatabaseHelper;
import com.pherodev.killddl.gestures.ItemTouchHelperAdapter;
import com.pherodev.killddl.models.Category;

import java.util.ArrayList;
import java.util.Collections;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> implements ItemTouchHelperAdapter {

    public ArrayList<Category> categories;
    private Context context;

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
        context = v.getContext();
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

    @Override
    public void onItemDismiss(int position) {
        if (context != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            dbHelper.deleteCategory(categories.get(position).getId());
            dbHelper.close();
            categories.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(categories, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
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

        }
    }

}
