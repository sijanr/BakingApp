package com.sijanrijal.bakingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sijanrijal.bakingapp.databinding.ListItemRecipeBinding;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private List<String> mRecipeSteps;
    private OnRecipeListClickListener listClickListener;
    private int recipePosition;

    public RecipeListAdapter(List<String> recipeSteps, OnRecipeListClickListener listClickListener, int recipePosition) {
        mRecipeSteps = recipeSteps;
        this.listClickListener = listClickListener;
        this.recipePosition = recipePosition;
    }

    public interface OnRecipeListClickListener {
        void onClick(int recipePosition, int stepPosition, String stepTitle);
    }

    @NonNull
    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemRecipeBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.list_item_recipe, parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.ViewHolder holder, int position) {
        holder.setStepsTitle();
    }

    @Override
    public int getItemCount() {

        return mRecipeSteps.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ListItemRecipeBinding binding;

        public ViewHolder(@NonNull ListItemRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listClickListener.onClick(recipePosition,
                            getAdapterPosition()-1,
                            mRecipeSteps.get(getAdapterPosition()));
                }
            });
        }

        public void setStepsTitle() {
            String stepShortDesc = mRecipeSteps.get(getAdapterPosition());
            binding.recipeCardTitle.setText(stepShortDesc);
        }
    }
}
