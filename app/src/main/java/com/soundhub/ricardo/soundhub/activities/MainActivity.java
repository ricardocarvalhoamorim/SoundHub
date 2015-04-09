package com.soundhub.ricardo.soundhub.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;
import com.soundhub.ricardo.soundhub.R;
import com.soundhub.ricardo.soundhub.Utils.PrefsManager;
import com.soundhub.ricardo.soundhub.Utils.Utils;
import com.soundhub.ricardo.soundhub.adapters.GenresListAdapter;
import com.soundhub.ricardo.soundhub.adapters.ScrollListener;
import com.soundhub.ricardo.soundhub.async.AsyncTrackFetcher;
import com.soundhub.ricardo.soundhub.interfaces.AsyncCustomTaskHandler;
import com.soundhub.ricardo.soundhub.interfaces.OnItemClickListener;
import com.soundhub.ricardo.soundhub.models.GenreItem;
import com.soundhub.ricardo.soundhub.models.TrackLookupResponse;
import com.soundhub.ricardo.soundhub.services.SoundHubService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements OnItemClickListener {


    private static android.support.v7.widget.Toolbar toolbar;
    private static ActionButton actionButton;
    private ActionButton skipButton;
    private ActionButton prevButton;

    private SoundHubService mServer;
    private Intent playIntent;

    private int playQueueIndex;
    private int genreSelectionIndex;

    private GenresListAdapter mAdapter;
    private ArrayList<GenreItem> items;
    private ArrayList<TrackLookupResponse> playQueue;

    private long mLastRequestStamp = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionButton = (ActionButton) findViewById(R.id.action_button_play);
        skipButton = (ActionButton) findViewById(R.id.action_button_skip);
        prevButton = (ActionButton) findViewById(R.id.action_button_prev);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP
                && getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }

        playQueueIndex = -1;

        actionButton.hide();
        skipButton.hide();
        prevButton.hide();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServer.isPlaying()) {
                    mServer.pause();
                    actionButton.setImageDrawable(
                            getResources().getDrawable(android.R.drawable.ic_media_play));
                } else {
                    mServer.resume();
                    actionButton.setImageDrawable(
                            getResources().getDrawable(android.R.drawable.ic_media_pause));
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (System.currentTimeMillis() - mLastRequestStamp < 5000) {
                    Toast.makeText(MainActivity.this, "Woah.. take it easy..", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (playQueueIndex == playQueue.size()) {
                    //Preload more tracks?
                    Toast.makeText(MainActivity.this, "No more tracks to go", Toast.LENGTH_SHORT).show();
                    return;
                }
                playQueueIndex++;

                dispatchPlaySelection();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - mLastRequestStamp < 5000) {
                    Toast.makeText(MainActivity.this, "Woah.. take it easy..", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (playQueueIndex == 0) {
                    Toast.makeText(MainActivity.this, "Can't.... go... back.... mustn't", Toast.LENGTH_SHORT).show();
                    return;
                }
                playQueueIndex--;
                dispatchPlaySelection();
            }
        });


        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.list_genres);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        items = PrefsManager.getGenres(this);
        mAdapter = new GenresListAdapter(items, this, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnScrollListener(new ScrollListener() {
            @Override
            public void onHide() {
                actionButton.hide();
                skipButton.hide();
                prevButton.hide();
                toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }

            @Override
            public void onShow() {
                if (mServer.isPlaying()) {
                    actionButton.show();
                    skipButton.show();
                    prevButton.show();
                }

                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            }
        });

    }

    private void dispatchPlaySelection() {
        mServer.setTrackPos(playQueueIndex);
        mServer.playSong();

        items.get(genreSelectionIndex).addArtists(playQueue.get(playQueueIndex).getUser().getUsername());
        mAdapter.notifyDataSetChanged();
        PrefsManager.updateGenresAsync(MainActivity.this, items);

        toolbar.setTitle(playQueue.get(playQueueIndex).getTitle());
        toolbar.setSubtitle(playQueue.get(playQueueIndex).getUser().getUsername());

        actionButton.show();
        skipButton.show();
        prevButton.show();

        mLastRequestStamp = System.currentTimeMillis();
    }

    @Override
    public void onItemClick(View view, int position) {

        if (genreSelectionIndex >= 0) {
            items.get(genreSelectionIndex).setNowPlaying(false);
        }

        genreSelectionIndex = position;
        fetchTracks(items.get(position).getGenreValue());
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    private ServiceConnection playerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SoundHubService.MusicBinder binder = (SoundHubService.MusicBinder) service;
            mServer = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent == null) {
            playIntent = new Intent(this, SoundHubService.class);
            bindService(playIntent, playerConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_info) {
            if (playQueueIndex >= 0) {
                String url = playQueue.get(playQueueIndex).getPermalink_url();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Requests a set of tracks belonging to a specific genre and
     * if successfull, starts playing
     * @param activeGenre genre that was selected by the user
     */
    private void fetchTracks(String activeGenre) {

        Uri builtUri = Uri.parse(Utils.soundCloudBaseAddr).buildUpon()
                .appendQueryParameter("client_id", Utils.clientKey)
                .appendQueryParameter("genres", activeGenre)
                .appendQueryParameter("limit", "200")
                .build();

        new AsyncTrackFetcher(new AsyncCustomTaskHandler<ArrayList<TrackLookupResponse>>() {

            @Override
            public void onSuccess(ArrayList<TrackLookupResponse> result) {

                long seed = System.nanoTime();
                Collections.shuffle(result, new Random(seed));

                playQueueIndex = 0;
                playQueue = result;
                mServer.setTrackQueue(result);
                mServer.setTrackPos(playQueueIndex);
                mServer.playSong();

                toolbar.setTitle(result.get(playQueueIndex).getTitle());
                toolbar.setSubtitle(result.get(playQueueIndex).getUser().getUsername());

                items.get(genreSelectionIndex).addArtists(result.get(playQueueIndex).getUser().getUsername());
                items.get(genreSelectionIndex).setNowPlaying(true);

                mAdapter.notifyDataSetChanged();
                PrefsManager.updateGenresAsync(MainActivity.this, items);

                mAdapter.notifyDataSetChanged();

                actionButton.show();
                skipButton.show();
                prevButton.show();
                playQueueIndex++;
            }

            @Override
            public void onFailure(Exception error) {
                Log.e("Failure", error.toString());
            }
        }).execute(builtUri);
    }


    @Override
    protected void onDestroy() {
        stopService(playIntent);
        mServer = null;

        for (GenreItem item : items) {
            item.setNowPlaying(false);
        }
        PrefsManager.updateGenresAsync(this, items);
        super.onDestroy();
    }


    @Override
    protected void onResume() {

        SharedPreferences prefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        //First run - load base genres list
        if (prefs.getBoolean("firstrun", true)) {
            PrefsManager.dispatchBaseStatistics(this, prefs);
            prefs.edit().putBoolean("firstrun", false).apply();
        }

        super.onResume();
    }

}
