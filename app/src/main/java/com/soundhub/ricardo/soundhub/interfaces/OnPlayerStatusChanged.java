package com.soundhub.ricardo.soundhub.interfaces;

/**
 * Created by ricardo on 18-03-2015.
 */
public interface OnPlayerStatusChanged {

    public void onPlayerChanged(String message);
    public void onPlayerStopped();
}
