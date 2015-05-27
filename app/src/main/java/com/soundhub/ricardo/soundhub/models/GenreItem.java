package com.soundhub.ricardo.soundhub.models;

public class GenreItem {

    private String GenreValue;
    private boolean nowPlaying;
    private String artists = "";
    private int playCount;

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

    public void onPlay() {
        this.playCount = playCount + 1;
    }
}
