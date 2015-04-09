package com.soundhub.ricardo.soundhub.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundhub.ricardo.soundhub.R;
import com.soundhub.ricardo.soundhub.models.GenreItem;

import java.util.ArrayList;

/**
 * Manager for the usage history and user configuration
 */
public class PrefsManager {

    public static ArrayList<GenreItem> getGenres(Context context) {
        SharedPreferences settings = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);

        if (!settings.contains(Utils.GENRE_STATS_ENTRY)) {
            return dispatchBaseStatistics(context, settings);
        }

        return new Gson().fromJson(settings.getString(Utils.GENRE_STATS_ENTRY, ""), Utils.ARRAY_GENRE_ITEMS);
    }

    public static  void updateGenresAsync(Context context, ArrayList<GenreItem> items) {

        SharedPreferences settings = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);

        if (!settings.contains(Utils.GENRE_STATS_ENTRY)) {
            dispatchBaseStatistics(context, settings);
            return;
        }

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(
                Utils.GENRE_STATS_ENTRY,
                new Gson().toJson(items));

        editor.apply();
    }

    /**
     * Populates the entry with the base genres and usage statistics
     * @param settings Shared Preferences
     */
    public static ArrayList<GenreItem> dispatchBaseStatistics(Context context, SharedPreferences settings) {
        ArrayList<GenreItem> items = new ArrayList<>();
        for (String genre : Utils.genres) {
            GenreItem newItem = new GenreItem();
            newItem.setGenreValue(genre);
            items.add(newItem);
        }

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Utils.GENRE_STATS_ENTRY, new Gson().toJson(items));
        editor.apply();

        Toast.makeText(context, "Successfully created entries", Toast.LENGTH_SHORT).show();
        return items;
    }
}
