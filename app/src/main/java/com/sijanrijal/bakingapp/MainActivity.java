package com.sijanrijal.bakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.sijanrijal.bakingapp.databinding.ActivityMainBinding;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RecipeListAdapter.OnRecipeListClickListener {

    private static final String TAG = "MainActivity";
    private static final String URL_STRING
            = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static List<Recipe> mRecipeList;

    private ActivityMainBinding binding;

    private RecipeFragment mCurrentFragment;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.mainToolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.mainToolbar,
                R.string.openNavDrawer, R.string.closeNavDrawer
        );

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);

        binding.navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle.syncState();

        if(binding.frameLeftContainer != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        getJSONFromNetwork();
    }


    /**
     * Fetch and parse JSON from network and add the parsed recipes to the Navigation Drawer
     **/
    public void getJSONFromNetwork() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL_STRING)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        binding.errorMessage.setVisibility(VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                try {
                    final String jsonString = response.body().string();
                    parseJSON(jsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        binding.errorMessage.setVisibility(View.INVISIBLE);
                        setnavigationMenuItem();
                        setFragment(0);
                    }
                });

            }
        });
    }


    /**
     * Parse the JSON to get the recipes
     **/
    public void parseJSON(String jsonString) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, Recipe.class);
        JsonAdapter<List<Recipe>> adapter = moshi.adapter(type);
        mRecipeList = adapter.fromJson(jsonString);
    }


    /**
     * Set the fragment to display as per the user's recipe choice
     **/
    private void setFragment(int itemPosition) {
        binding.mainToolbar.setTitle(mRecipeList.get(itemPosition).name);
        mCurrentFragment = new RecipeFragment(itemPosition, this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(mTwoPane) {
            fragmentTransaction.replace(binding.frameLeftContainer.getId(),mCurrentFragment);
            fragmentTransaction.replace(binding.frameRightContainer.getId(),new Fragment());
        } else {
            fragmentTransaction.replace(binding.frameContainer.getId(), mCurrentFragment);
        }
        fragmentTransaction.commit();
    }


    /**
     * Set the navigation menu items with the recipe names parsed from the JSON object
     **/
    private void setnavigationMenuItem() {

        List<String> navMenuTitle = new ArrayList<>();

        if (mRecipeList != null && mRecipeList.size() > 0) {

            for (Recipe recipe : mRecipeList) {
                navMenuTitle.add(recipe.name);
            }
        }

        Menu menu = binding.navigationView.getMenu();

        for (int i = 0; i < navMenuTitle.size(); i++) {
            menu.add(0, mRecipeList.get(i).id, mRecipeList.get(i).id, navMenuTitle.get(i));
        }

    }


    /**
     * Navigate to the selected fragment from the navigation drawer
     **/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        for (Recipe recipe : mRecipeList) {
            if (recipe.id == item.getItemId()) {
                setFragment(recipe.id - 1);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        }
        return false;
    }


    /**
     *
     **/
    @Override
    public void onClick(int recipePosition, int stepPosition, String stepTitle) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment(recipePosition, stepPosition, stepTitle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(mTwoPane) {
            transaction.replace(binding.frameRightContainer.getId(), recipeDetailFragment);
        } else {
            transaction.add(binding.frameContainer.getId(), recipeDetailFragment);
            transaction.addToBackStack("DETAIL");
            transaction.hide(mCurrentFragment);
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mCurrentFragment.isHidden()) {
            getSupportFragmentManager().beginTransaction().show(mCurrentFragment);
        }
    }
}