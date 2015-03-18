package com.soundhub.ricardo.soundhub.interfaces;


import com.soundhub.ricardo.soundhub.models.ProgressUpdateItem;

/**
 * @param <T> Object type
 */
public interface AsyncCustomTaskHandler<T> {

    public void onSuccess(T result);

    public void onFailure(Exception error);

    public void onProgressUpdate(ProgressUpdateItem progress);

}
