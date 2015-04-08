package com.soundhub.ricardo.soundhub.models;

import com.soundhub.ricardo.soundhub.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ricardo on 17-03-2015.
 */
public class GenreItem {

    private String GenreValue;
    private boolean nowPlaying;
    private String artists = "";
    private TrackLookupResponse currentTrack;


    public TrackLookupResponse getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(TrackLookupResponse currentTrack) {
        this.currentTrack = currentTrack;
    }

    public String getGenreValue() {
        return GenreValue;
    }

    public void setGenreValue(String genreValue) {
        GenreValue = genreValue;
    }

    public boolean isNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(boolean nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public String getArtists() {
        return artists;
    }

    public void addArtists(String newArtists) {
        if (this.artists.equals("")) {
            this.artists = newArtists;
        } else {
            this.artists += ", " +newArtists;
        }

    }
}
