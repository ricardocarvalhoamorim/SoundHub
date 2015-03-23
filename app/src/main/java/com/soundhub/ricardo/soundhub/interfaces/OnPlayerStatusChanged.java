package com.soundhub.ricardo.soundhub.interfaces;

import com.soundhub.ricardo.soundhub.models.GenreItem;
import com.soundhub.ricardo.soundhub.models.TrackLookupResponse;

/**
 * Created by ricardo on 18-03-2015.
 */
public interface OnPlayerStatusChanged {

    public void onGenreSelected(GenreItem selection);
    public void onListScroll(int visibility);
}
