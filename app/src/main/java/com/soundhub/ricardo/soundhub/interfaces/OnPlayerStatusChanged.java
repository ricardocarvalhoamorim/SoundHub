package com.soundhub.ricardo.soundhub.interfaces;

/**
 * Created by ricardo on 18-03-2015.
 */
public interface OnPlayerStatusChanged {

    public void onFeedbackAvailable(String message);
    public void onPlayerStart();
    public void onPlayerPaused();
    public void onPlayerStopped();
}
