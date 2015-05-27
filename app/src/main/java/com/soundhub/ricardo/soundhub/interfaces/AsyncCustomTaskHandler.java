package com.soundhub.ricardo.soundhub.interfaces;


/**
 * @param <T> Object type
 */
public interface AsyncCustomTaskHandler<T> {

    void onSuccess(T result);

    void onFailure(Exception error);
}
