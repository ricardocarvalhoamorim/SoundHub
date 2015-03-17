package com.soundhub.ricardo.soundhub.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soundhub.ricardo.soundhub.R;
import com.soundhub.ricardo.soundhub.adapters.GenresListAdapter;
import com.soundhub.ricardo.soundhub.interfaces.OnItemClickListener;
import com.soundhub.ricardo.soundhub.models.GenreItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;


public class GenresListFragment extends Fragment implements OnItemClickListener {

    private static final Type ARRAY_GENRE_ITEMS = new TypeToken<ArrayList<GenreItem>>(){}.getType();
    private static final String GENRE_STATS_ENTRY = "genre_statistics";

    private static final String[] genres = {
            "80s"                  , "Acid Jazz"          , "Acoustic Rock"      , "African"
            , "Alternative"        , "Ambient"            , "Americana"          ,"Arabic"
            ,"Avantgarde"          ,"Bachata"             ,"Bhangra"             ,"Blues"
            ,"Blues Rock"          ,"Bossa Nova"
            ,"Chanson"             ,"Chillout"            ,"Chiptunes"           ,"Choir"
            ,"Classic Rock"        ,"Classical"           ,"Classical Guitar"    ,"Contemporary"
            ,"Country"             ,"Cumbia"              ,"Dance"               ,"Dancehall"
            ,"Death Metal"         ,"Dirty South"         ,"Disco"               ,"Dream Pop"
            ,"Drum & Bass"         ,"Dub"                 ,"Dubstep"             ,"Easy Listening"
            ,"Electro House"       ,"Electronic"          ,"Electronic Pop"      ,"Electronic Rock"
            ,"Folk"                ,"Folk Rock"           ,"Funk"                ,"Glitch"
            ,"Gospel"              ,"Grime"               ,"Grindcore"           ,"Grunge"
            ,"Hard Rock"           ,"Hardcore"            ,"Heavy Metal"         ,"Hip-Hop"
            ,"House"               ,"Indie"               ,"Indie Pop"           ,"Industrial Metal"
            ,"Instrumental Rock"   ,"J-Pop"               ,"Jazz"                ,"Jazz Funk"
            ,"Jazz Fusion"         ,"K-Pop"               ,"Latin"               ,"Latin Jazz"
            ,"Mambo"               ,"Metalcore"           ,"Middle Eastern"      ,"Minimal"
            ,"Modern Jazz"         ,"Moombahton"          ,"New Wave"            ,"Nu Jazz"
            ,"Opera"               ,"Orchestral"          ,"Piano"               ,"Pop"
            ,"Post Hardcore"       ,"Post Rock"           ,"Progressive House"   ,"Progressive Metal"
            ,"Progressive Rock"    ,"Punk"                ,"R&B"                 ,"Rap"
            ,"Reggae"              ,"Reggaeton"           ,"Riddim"              ,"Rock"
            ,"Rock 'n' Roll"       ,"Salsa"               ,"Samba"               ,"Shoegaze"
            ,"Singer / Songwriter" ,"Smooth Jazz"         ,"Soul"                ,"Synth Pop"
            ,"Tech House"          ,"Techno"              ,"Thrash Metal"        ,"Trance"
            ,"Trap"                ,"Trip-hop"            ,"Turntablism" };

    private GenresListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public GenresListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_genres);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        ArrayList<GenreItem> items = new ArrayList<>();
        for (String genre : genres) {
            GenreItem newItem = new GenreItem();
            newItem.setGenreValue(genre);
            items.add(newItem);
        }

        mAdapter = new GenresListAdapter(items, this, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onItemClick(View view, int position) {
        view.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    //TODO use this function
    /**
     * Removes the available statistics and set everything to zero
     */
    public void clearStatistics() {
        SharedPreferences settings = getActivity().getSharedPreferences(
                getResources().getString(R.string.app_name), Context.MODE_PRIVATE);

        ArrayList<GenreItem> items;

        if (!settings.contains(GENRE_STATS_ENTRY)) {
            items = new ArrayList<>();
            for (String genre : genres) {
                GenreItem newItem = new GenreItem();
                newItem.setGenreValue(genre);
                items.add(newItem);
            }
            mAdapter.notifyDataSetChanged();
            return;
        }

        items = new Gson().fromJson(settings.getString(GENRE_STATS_ENTRY, ""), ARRAY_GENRE_ITEMS);

        for (GenreItem item : items) {
            item.setLastPlayed("");
            item.setPlayCount(0);
            item.setSingers(new ArrayList<String>());
        }
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Cleared", Toast.LENGTH_SHORT).show();
    }

}