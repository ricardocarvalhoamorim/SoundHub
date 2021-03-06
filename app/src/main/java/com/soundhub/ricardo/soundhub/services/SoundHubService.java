package com.soundhub.ricardo.soundhub.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.soundhub.ricardo.soundhub.Utils.Utils;
import com.soundhub.ricardo.soundhub.models.TrackLookupResponse;

import java.io.IOException;
import java.util.ArrayList;


public class SoundHubService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{


    private MediaPlayer player;
    private ArrayList<TrackLookupResponse> playQueue;
    private int trackPos;

    private final IBinder musicBind = new MusicBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        trackPos=0;
        player = new MediaPlayer();
        attatchPlayer();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (playQueue == null)
            return;

        if (playQueue.size() > trackPos) {
            setTrackPos(++trackPos);
            playSong();
        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }


    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    private void attatchPlayer() {
        //keep music playing after device lock
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setTrackQueue(ArrayList<TrackLookupResponse> queue){
        playQueue = queue;
    }

    public void playSong() {
        player.reset();
        TrackLookupResponse track = playQueue.get(trackPos);

        try {
            Uri builtUri = Uri.parse(track.getStream_url()).buildUpon()
                    .appendQueryParameter("client_id", Utils.clientKey)
                    .build();

            // Build the ExoPlayer and start playback
            player.setDataSource(getApplicationContext(), builtUri);
        } catch (NullPointerException e ) {
            Log.e("NP", e.toString());
        } catch (IOException e) {
            Log.e("IO", e.toString());
        }


        try {
            player.prepareAsync();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplication(), "Stream error", Toast.LENGTH_SHORT).show();
        }

        //after this -> onPrepared will be called
    }

    public void pause() {
        if (player.isPlaying()) {
            player.pause();
        }
    }

    public void resume() {
        if (!player.isPlaying()) {
            player.start();
        }
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void setTrackPos(int srcPos){
        trackPos=srcPos;
    }



    public class MusicBinder extends Binder {
        public SoundHubService getService() {
            return SoundHubService.this;
        }
    }
}
