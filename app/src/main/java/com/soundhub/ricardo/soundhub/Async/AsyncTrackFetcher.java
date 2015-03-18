package com.soundhub.ricardo.soundhub.Async;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.soundhub.ricardo.soundhub.Utils.Utils;
import com.soundhub.ricardo.soundhub.interfaces.AsyncCustomTaskHandler;
import com.soundhub.ricardo.soundhub.models.TrackLookupResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

/**
 * Created by ricardo on 18-03-2015.
 */
public class AsyncTrackFetcher extends AsyncTask<Uri, Void, ArrayList<TrackLookupResponse>> {

    private final AsyncCustomTaskHandler<ArrayList<TrackLookupResponse>> mHandler;
    private Exception error;

    public AsyncTrackFetcher(AsyncCustomTaskHandler<ArrayList<TrackLookupResponse>> mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    protected ArrayList<TrackLookupResponse> doInBackground(Uri... params) {

        if (! (params[0] instanceof Uri)) {
            error = new Exception("Should receive Uri, received " + params[0].getClass());
            return null;
        }

        HttpResponse response;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();

            //its ridiculous we have to deal with this
            //android.net.Uri -> java.net.Uri
            request.setURI(java.net.URI.create(params[0].toString()));
            response = client.execute(request);
            String result = EntityUtils.toString(response.getEntity());

            return new Gson().fromJson(result, Utils.ARRAY_GENRE_ITEMS_RESPONSE);
        } catch (Exception e) {
            error = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<TrackLookupResponse> result) {
        super.onPostExecute(result);
        if (error != null) {
            mHandler.onFailure(error);
        } else {
            mHandler.onSuccess(result);
        }
    }
}
