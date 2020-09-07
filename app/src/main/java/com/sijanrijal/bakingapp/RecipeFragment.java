package com.sijanrijal.bakingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sijanrijal.bakingapp.databinding.FragmentRecipeBinding;

import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment {

    private static final String TAG = "RecipeFragment";
    private FragmentRecipeBinding binding;
    private RecipeListAdapter mAdapter;
    private int mItemPosition;
    private RecipeListAdapter.OnRecipeListClickListener clickListener;

    public RecipeFragment(int itemPosition, RecipeListAdapter.OnRecipeListClickListener clickListener) {
        mItemPosition = itemPosition;
        this.clickListener = clickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe, container, false
        );

        Log.d(TAG, "onCreateView: " + MainActivity.mRecipeList.size());

        List<String> recipeInstructions = new ArrayList<>();
        recipeInstructions.add("Ingredients");

        for (Recipe.Steps steps : MainActivity.mRecipeList.get(mItemPosition).steps) {
            recipeInstructions.add(steps.shortDescription);
        }
        mAdapter = new RecipeListAdapter(recipeInstructions, clickListener, mItemPosition);
        binding.recyclerViewRecipe.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.recyclerViewRecipe.setLayoutManager(linearLayoutManager);
        binding.recyclerViewRecipe.setAdapter(mAdapter);

        return binding.getRoot();
    }
}
