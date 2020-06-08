package com.sijanrijal.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sijanrijal.bakingapp.databinding.FragmentRecipeDetailBinding;

import java.util.List;

public class RecipeDetailFragment extends Fragment {

    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    private String video_url;

    private FragmentRecipeDetailBinding binding;

    private int mRecipePosition;
    private int mStepPosition;
    private String mStepTitle;

    public RecipeDetailFragment(int recipePosition, int stepPosition, String stepTitle) {
        mRecipePosition = recipePosition;
        mStepPosition = stepPosition;
        mStepTitle = stepTitle;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_detail, container, false);

        if (mStepPosition < 0) {
            binding.videoView.setVisibility(View.GONE);
        }
        setDescription();
        binding.detialDescTitle.setText(mStepTitle);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24 && mStepPosition >= 0 && video_url != null) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT < 24 || player == null) && mStepPosition >= 0 && video_url != null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24 && mStepPosition >= 0 && video_url != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24 && mStepPosition >= 0 && video_url != null) {
            releasePlayer();
        }
    }


    /**
     * Create an instance of Exoplayer object
     **/
    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(getContext()).build();
        binding.videoView.setPlayer(player);
        Uri uri = Uri.parse(video_url);
        MediaSource mediaSource = buildMediaSource(uri);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }


    /**
     * Create media source
     **/
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), "exoplayer-recipe");
        ProgressiveMediaSource.Factory progressiveMediaSourceFactory = new ProgressiveMediaSource.Factory(dataSourceFactory);
        return progressiveMediaSourceFactory.createMediaSource(uri);
    }

    /**
     * Release the player resources
     **/
    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    /**
     * Sets description and URL of the video to play
     **/
    private void setDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        Recipe recipe = MainActivity.mRecipeList.get(mRecipePosition);
        if (mStepPosition < 0) {
            List<Recipe.Ingredients> ingredients = recipe.ingredients;
            for (Recipe.Ingredients ingredient : ingredients) {
                stringBuilder.append("- ")
                        .append(ingredient.quantity)
                        .append(ingredient.measure)
                        .append(" ")
                        .append(ingredient.ingredient)
                        .append("\n\n");
            }
        } else {
            Recipe.Steps step = recipe.steps.get(mStepPosition);
            stringBuilder.append(step.description);
            video_url = step.videoURL;
            if (video_url.equals("")) {
                video_url = step.thumbnailURL;
            }
            if (video_url.equals("")) {
                video_url = null;
                binding.videoView.setVisibility(View.GONE);
            }
        }
        binding.detailDescText.setText(stringBuilder.toString());
    }


}
