package com.soundhub.ricardo.soundhub.models;

import com.soundhub.ricardo.soundhub.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ricardo on 17-03-2015.
 */
public class GenreItem {

    private String GenreValue;
    private ArrayList<String> singers;
    private boolean nowPlaying;
    private String lastPlayed;
    private int playCount;

    public String getGenreValue() {
        return GenreValue;
    }

    public void setGenreValue(String genreValue) {
        GenreValue = genreValue;
    }

    public ArrayList<String> getSingers() {
        return singers == null? new ArrayList<String>() : singers;
    }

    public void setSingers(ArrayList<String> singers) {
        this.singers = singers;
    }

    public boolean isNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(boolean nowPlaying) {
        if (nowPlaying) {
            this.playCount += 1;
            this.lastPlayed = new Date().toString();
        }
        this.nowPlaying = nowPlaying;
    }

    public String getLastPlayed() {
        return lastPlayed == null ? "never" : lastPlayed;
    }

    public void setLastPlayed(String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
}
