package com.soundhub.ricardo.soundhub.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.FrameworkSampleSource;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.TrackRenderer;
import com.google.gson.Gson;
import com.soundhub.ricardo.soundhub.Async.AsyncTrackFetcher;
import com.soundhub.ricardo.soundhub.MainActivity;
import com.soundhub.ricardo.soundhub.R;
import com.soundhub.ricardo.soundhub.Utils.Utils;
import com.soundhub.ricardo.soundhub.adapters.GenresListAdapter;
import com.soundhub.ricardo.soundhub.interfaces.AsyncCustomTaskHandler;
import com.soundhub.ricardo.soundhub.interfaces.OnItemClickListener;
import com.soundhub.ricardo.soundhub.interfaces.OnPlayerStatusChanged;
import com.soundhub.ricardo.soundhub.models.GenreItem;
import com.soundhub.ricardo.soundhub.models.ProgressUpdateItem;
import com.soundhub.ricardo.soundhub.models.TrackLookupResponse;

import java.util.ArrayList;


public class GenresListFragment extends Fragment implements OnItemClickListener {

    private static OnPlayerStatusChanged playerStatusListener;
    private GenresListAdapter mAdapter;
    private ArrayList<GenreItem> items;

    private ArrayList<TrackLookupResponse> playQueue;

    public GenresListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_genres);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        clearStatistics();

        mAdapter = new GenresListAdapter(items, this);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }



    @Override
    public void onItemClick(View view, int position) {

        playerStatusListener.onGenreSelected(
                items.get(position));

            //selected item is not playing & others are playing
        //TODO maybe improve this block
        int itemsSize = items.size();
        for (int i = 0; i < itemsSize; ++i) {
            if (items.get(i).isNowPlaying()
                    && i != position) {
                items.get(i).setNowPlaying(false);
            } else if (!items.get(i).isNowPlaying()
                    && i == position) {
                items.get(i).setNowPlaying(true);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    /**
     * Removes the available statistics and set everything to zero
     */
    public void clearStatistics() {

        SharedPreferences settings = getActivity().getSharedPreferences(
                getResources().getString(R.string.app_name), Context.MODE_PRIVATE);

        if (!settings.contains(Utils.GENRE_STATS_ENTRY)) {
            dispatchBaseStatistics(settings);
            return;
        }

        items = new Gson().fromJson(
                settings.getString(Utils.GENRE_STATS_ENTRY, ""),
                Utils.ARRAY_GENRE_ITEMS);

        for (GenreItem item : items) {
            item.setLastPlayed("");
            item.setPlayCount(0);
            item.setSingers(new ArrayList<String>());
        }

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(
                Utils.GENRE_STATS_ENTRY,
                new Gson().toJson(items));

        editor.apply();

        if (mAdapter == null) {
            mAdapter = new GenresListAdapter(items, this);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        Toast.makeText(getActivity(), "Cleared", Toast.LENGTH_SHORT).show();
    }

    /**
     * Populates the entry with the base genres and usage statistics
     * @param settings Shared Preferences
     */
    public void dispatchBaseStatistics(SharedPreferences settings) {
        items = new ArrayList<>();
        for (String genre : Utils.genres) {
            GenreItem newItem = new GenreItem();
            newItem.setGenreValue(genre);
            items.add(newItem);
        }

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Utils.GENRE_STATS_ENTRY, new Gson().toJson(items));
        editor.apply();

        if (mAdapter == null) {
            mAdapter = new GenresListAdapter(items, this);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        Toast.makeText(getActivity(), "Successfully created entries", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(Activity activity) {
        try {
            playerStatusListener = (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPlayerStatusChanged");
        }
        super.onAttach(activity);
    }
}