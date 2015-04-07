package com.soundhub.ricardo.soundhub.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soundhub.ricardo.soundhub.MainActivity;
import com.soundhub.ricardo.soundhub.R;
import com.soundhub.ricardo.soundhub.Utils.PrefsManager;
import com.soundhub.ricardo.soundhub.adapters.GenresListAdapter;
import com.soundhub.ricardo.soundhub.interfaces.OnItemClickListener;
import com.soundhub.ricardo.soundhub.interfaces.OnPlayerStatusChanged;
import com.soundhub.ricardo.soundhub.models.GenreItem;
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

        items = PrefsManager.getGenres(getActivity());
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