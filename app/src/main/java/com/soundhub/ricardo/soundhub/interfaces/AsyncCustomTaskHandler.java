package com.soundhub.ricardo.soundhub.interfaces;


/**
 * @param <T> Object type
 */
public interface AsyncCustomTaskHandler<T> {

    public void onSuccess(T result);

    public void onFailure(Exception error);
}
