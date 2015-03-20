package com.soundhub.ricardo.soundhub.interfaces;

import com.soundhub.ricardo.soundhub.models.TrackLookupResponse;

/**
 * Created by ricardo on 18-03-2015.
 */
public interface OnPlayerStatusChanged {

    public void onPlayerStart(TrackLookupResponse trackLookupResponse);
    public void onPlayerBuffering();
    public void onPlayerPaused();
    public void onPlayerStopped();
    public void onListScroll(int visibility);
}
