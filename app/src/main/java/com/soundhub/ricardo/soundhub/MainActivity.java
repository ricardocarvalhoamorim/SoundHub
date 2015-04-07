package com.soundhub.ricardo.soundhub;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.software.shell.fab.ActionButton;
import com.soundhub.ricardo.soundhub.async.AsyncTrackFetcher;
import com.soundhub.ricardo.soundhub.Utils.Utils;
import com.soundhub.ricardo.soundhub.fragments.GenresListFragment;
import com.soundhub.ricardo.soundhub.interfaces.AsyncCustomTaskHandler;
import com.soundhub.ricardo.soundhub.interfaces.OnPlayerStatusChanged;
import com.soundhub.ricardo.soundhub.models.GenreItem;
import com.soundhub.ricardo.soundhub.models.ProgressUpdateItem;
import com.soundhub.ricardo.soundhub.models.TrackLookupResponse;
import com.soundhub.ricardo.soundhub.services.SoundHubService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class MainActivity extends Activity implements OnPlayerStatusChanged {


    private ActionButton actionButton;
    private ActionButton skipButton;

    ArrayList<TrackLookupResponse> playQueue;

    private static ImageView trackCover;
    private boolean expandedViewAccessible;

    private SoundHubService mServer;
    private Intent playIntent;

    private int trackNr;
    private boolean skipBaseDeploy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionButton = (ActionButton) findViewById(R.id.action_button_play);
        skipButton = (ActionButton) findViewById(R.id.action_button_skip);
        trackCover = (ImageView) findViewById(R.id.track_cover);

        trackNr = 0;

        actionButton.hide();
        skipButton.hide();

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
                            getResources().getDrawable(android.R.drawable.ic_media_play));
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackNr++;

                mServer.setTrackPos(trackNr);
                mServer.playSong();
                preloadTrackInfo(playQueue.get(trackNr));
            }
        });

        findViewById(R.id.player_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandedViewAccessible) {
                    findViewById(R.id.player_status_small).setVisibility(View.VISIBLE);
                    findViewById(R.id.player_layout).setVisibility(View.GONE);
                    expandedViewAccessible = false;
                } else {
                    findViewById(R.id.player_status_small).setVisibility(View.GONE);
                    findViewById(R.id.player_layout).setVisibility(View.VISIBLE);
                    expandedViewAccessible = true;
                }
            }
        });

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new GenresListFragment())
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .commit();
        }
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
        if(playIntent == null && !skipBaseDeploy) {
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
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

                trackNr = 0;
                playQueue = result;
                mServer.setTrackQueue(result);
                mServer.setTrackPos(trackNr);
                mServer.playSong();

                preloadTrackInfo(result.get(trackNr));
                trackNr ++;
            }

            @Override
            public void onFailure(Exception error) {
                Log.e("Failure", error.toString());
            }

            @Override
            public void onProgressUpdate(ProgressUpdateItem progress) {

            }
        }).execute(builtUri);
        Log.v("URI", "BUILT URI FOR STREAMING: " + builtUri);
    }

    private void preloadTrackInfo(TrackLookupResponse trackLookupResponse) {
        if (trackLookupResponse.getArtwork_url() != null &&
                !trackLookupResponse.getArtwork_url().equals("")) {

            trackCover.setVisibility(View.VISIBLE);

            ((TextView) findViewById(R.id.player_status_small)).setText(trackLookupResponse.getTitle());
            ((TextView) findViewById(R.id.player_status)).setText(trackLookupResponse.getTitle());

            ((TextView) findViewById(R.id.player_status_tags)).setText(trackLookupResponse.getTag_list());
            ((TextView) findViewById(R.id.player_status_favoritings)).setText(trackLookupResponse.getFavoritings_count() + " times favorited");

            Glide.with(MainActivity.this)
                    .load(trackLookupResponse.getArtwork_url())
                    .placeholder(android.R.drawable.stat_sys_download_done)
                    .crossFade()
                    .into(trackCover);

        } else {
            trackCover.setVisibility(View.GONE);
        }

        actionButton.show();
        skipButton.show();

        //TODO register singers
        playQueue.get(trackNr).addSingers(trackLookupResponse.getUser().getUsername());

        //TODO: update db

    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        mServer = null;
        super.onDestroy();
    }

    @Override
    public void onGenreSelected(GenreItem selection) {
        fetchTracks(selection.getGenreValue());
    }


    @Override
    public void onListScroll(int visibility) {

    }

    @Override
    protected void onResume() {
        if (mServer == null) {
            skipBaseDeploy = false;
        } else if (mServer.isPlaying()) {
            skipBaseDeploy = true;
        }
        super.onResume();
    }
}
