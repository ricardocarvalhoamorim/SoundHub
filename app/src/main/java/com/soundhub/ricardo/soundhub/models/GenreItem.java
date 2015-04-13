package com.soundhub.ricardo.soundhub.models;

/**
 * Created by ricardo on 17-03-2015.
 */
public class GenreItem {

    private String GenreValue;
    private boolean nowPlaying;
    private String artists = "";
    private int playCount;
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

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public void onPlay() {
        this.playCount = playCount + 1;
    }
}
