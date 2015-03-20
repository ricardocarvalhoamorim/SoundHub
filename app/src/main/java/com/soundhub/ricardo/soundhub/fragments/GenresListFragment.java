package com.soundhub.ricardo.soundhub.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.FrameworkSampleSource;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
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


public class GenresListFragment extends Fragment implements OnItemClickListener, AsyncCustomTaskHandler<ArrayList<TrackLookupResponse>>, ExoPlayer.Listener {

    /**
     * Which track is currently playing
     */
    private int trackNr;

    /**
     * Player to stream for uri
     */
    private ExoPlayer exoPlayer;

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


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItemPostition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPostition == 0) {
                    playerStatusListener.onListScroll(View.VISIBLE);
                } else if (firstVisibleItemPostition > 9) {
                    playerStatusListener.onListScroll(View.GONE);
                }
            }
        });

        clearStatistics();

        mAdapter = new GenresListAdapter(items, this, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        trackNr = 0;
        return rootView;
    }

    private void fetchTracks(String activeGenre) {
        Uri builtUri = Uri.parse(Utils.soundCloudBaseAddr).buildUpon()
                .appendQueryParameter("client_id", Utils.clientKey)
                .appendQueryParameter("genres", activeGenre)
                .build();

        new AsyncTrackFetcher(this).execute(builtUri);
        Log.v("URI", "BUILT URI FOR STREAMING: " + builtUri);
    }

    public void stopStream() {
        if (exoPlayer != null) {
            exoPlayer.stop();

            exoPlayer.release();
            exoPlayer = null;

            mAdapter.notifyDataSetChanged();
            playerStatusListener.onPlayerStopped();
            return;
        }
        Toast.makeText(getActivity(), "Not playing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view, int position) {

        //selected item is already playing -> stop stream
        if (items.get(position).isNowPlaying()) {
            items.get(position).setNowPlaying(false);
            stopStream();
            return;
        }

        //selected item is not playing & others are playing
        //TODO maybe improve this block
        int itemsSize = items.size();
        for (int i = 0; i < itemsSize; ++i) {
            if (items.get(i).isNowPlaying()
                    && i != position) {
                items.get(i).setNowPlaying(false);
                stopStream();
            } else if (!items.get(i).isNowPlaying()
                    && i == position) {
                items.get(i).setNowPlaying(true);
                fetchTracks(items.get(i).getGenreValue());
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
            mAdapter = new GenresListAdapter(items, this, getActivity());
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
            mAdapter = new GenresListAdapter(items, this, getActivity());
        } else {
            mAdapter.notifyDataSetChanged();
        }

        Toast.makeText(getActivity(), "Successfully created entries", Toast.LENGTH_SHORT).show();
        return;
    }

    @Override
    public void onSuccess(ArrayList<TrackLookupResponse> result) {
        playQueue = result;

        try {
            Uri builtUri = Uri.parse(result.get(trackNr++).getStream_url()).buildUpon()
                    .appendQueryParameter("client_id", Utils.clientKey)
                    .build();

            // Build the ExoPlayer and start playback
            attatchPlayer(builtUri);
        } catch (NullPointerException e ) {
            Log.e("NP", e.toString());
        }


    }

    private void attatchPlayer(Uri builtUri) {
        if (exoPlayer != null) {
            stopStream();
        }

        this.onResume();
        playerStatusListener.onPlayerStopped();

        // Build the sample source
        FrameworkSampleSource sampleSource = new FrameworkSampleSource(getActivity(), builtUri, null, 1);

        // Build the track renderers
        MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, null, true);

        exoPlayer.prepare(audioRenderer);
        exoPlayer.setPlayWhenReady(true);

        playerStatusListener.onPlayerStart(playQueue.get(trackNr));
    }

    @Override
    public void onFailure(Exception error) {
        Log.e("Failure", error.toString());
    }

    @Override
    public void onProgressUpdate(ProgressUpdateItem progress) {

    }


    //---------- Player related calls ----------------------------------------------------------------------
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                playerStatusListener.onPlayerBuffering();
                break;

            case ExoPlayer.STATE_PREPARING:
                playerStatusListener.onPlayerBuffering();
                break;

            case ExoPlayer.STATE_READY:
                playerStatusListener.onPlayerStart(playQueue.get(trackNr));
                break;

            case ExoPlayer.STATE_ENDED:
                playerStatusListener.onPlayerStopped();
                playNextInQueue();
                break;

            case ExoPlayer.STATE_IDLE:
                break;
        }
    }

    public void playNextInQueue() {

        playerStatusListener.onPlayerBuffering();

        if (playQueue.size() > 0 &&
                trackNr < playQueue.size()-2) {
            attatchPlayer(Uri.parse(playQueue.get(trackNr).getStream_url() + "&client_id=" + Utils.clientKey));
        }
    }

    @Override
    public void onPlayWhenReadyCommitted() {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
        Log.e("PlayerError", error.toString());
        playerStatusListener.onPlayerStopped();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }

    @Override
    public void onResume() {

        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Factory.newInstance(1);
            exoPlayer.addListener(this);
        }
        super.onResume();
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

    public void onFabPlayTap() {

        playerStatusListener.onPlayerBuffering();
        if (exoPlayer.getPlaybackState() == ExoPlayer.STATE_READY) {
            playerStatusListener.onPlayerStopped();
            exoPlayer.stop();
        } else {
            //TODO resume
            onFabSkipTap();
        }

        Uri builtUri = Uri.parse(playQueue.get(trackNr++).getStream_url()).buildUpon()
                .appendQueryParameter("client_id", Utils.clientKey)
                .build();

        attatchPlayer(builtUri);
    }

    public void onFabSkipTap() {

        playerStatusListener.onPlayerBuffering();

        Uri builtUri = Uri.parse(playQueue.get(trackNr++).getStream_url()).buildUpon()
                .appendQueryParameter("client_id", Utils.clientKey)
                .build();

        attatchPlayer(builtUri);
    }


    public void onFabButtonLongTap() {

    }
}