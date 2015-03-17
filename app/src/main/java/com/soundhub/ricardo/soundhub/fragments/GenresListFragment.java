package com.soundhub.ricardo.soundhub.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soundhub.ricardo.soundhub.R;
import com.soundhub.ricardo.soundhub.Utils;
import com.soundhub.ricardo.soundhub.adapters.GenresListAdapter;
import com.soundhub.ricardo.soundhub.interfaces.OnItemClickListener;
import com.soundhub.ricardo.soundhub.models.GenreItem;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class GenresListFragment extends Fragment implements OnItemClickListener {



    private GenresListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public GenresListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_genres);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        ArrayList<GenreItem> items = new ArrayList<>();
        for (String genre : Utils.genres) {
            GenreItem newItem = new GenreItem();
            newItem.setGenreValue(genre);
            items.add(newItem);
        }

        mAdapter = new GenresListAdapter(items, this, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onItemClick(View view, int position) {
        view.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    //TODO use this function
    /**
     * Removes the available statistics and set everything to zero
     */
    public void clearStatistics() {
        SharedPreferences settings = getActivity().getSharedPreferences(
                getResources().getString(R.string.app_name), Context.MODE_PRIVATE);

        ArrayList<GenreItem> items;

        if (!settings.contains(Utils.GENRE_STATS_ENTRY)) {
            items = new ArrayList<>();
            for (String genre : Utils.genres) {
                GenreItem newItem = new GenreItem();
                newItem.setGenreValue(genre);
                items.add(newItem);
            }
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Utils.GENRE_STATS_ENTRY, new Gson().toJson(items));
            editor.apply();
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Successfully created entries", Toast.LENGTH_SHORT).show();
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
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Cleared", Toast.LENGTH_SHORT).show();
    }
}