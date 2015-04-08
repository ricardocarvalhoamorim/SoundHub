package com.soundhub.ricardo.soundhub.adapters;

/**
 * Created by ricardo on 17-03-2015.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soundhub.ricardo.soundhub.R;
import com.soundhub.ricardo.soundhub.Utils.Utils;
import com.soundhub.ricardo.soundhub.interfaces.OnItemClickListener;
import com.soundhub.ricardo.soundhub.models.GenreItem;

import java.util.ArrayList;


public class GenresListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<GenreItem> items;
    private static OnItemClickListener listener;


    public GenresListAdapter(ArrayList<GenreItem> srcItems,
                                 OnItemClickListener clickListener) {

        this.items = srcItems;
        listener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        switch (viewType) {
            case Utils.VIEW_TYPE_GENRE_ITEM:
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_genre, parent, false);
            return new GenreItemViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case Utils.VIEW_TYPE_GENRE_ITEM:
                GenreItem item = items.get(position);

                ((GenreItemViewHolder) holder).tvItemValue.setText(item.getGenreValue());
                if (!item.getArtists().equals("")) {
                    ((GenreItemViewHolder) holder).tvItemArtists.setText(item.getArtists());
                    ((GenreItemViewHolder) holder).tvItemArtists.setVisibility(View.VISIBLE);
                } else  {
                    ((GenreItemViewHolder) holder).tvItemArtists.setVisibility(View.GONE);
                }


                if (item.isNowPlaying()) {
                    ((GenreItemViewHolder) holder).tvNowPlaying.setVisibility(View.VISIBLE);
                } else {
                    ((GenreItemViewHolder) holder).tvNowPlaying.setVisibility(View.GONE);
                }
                break;

            case Utils.VIEW_TYPE_TRACK_INFO:
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        return Utils.VIEW_TYPE_GENRE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class GenreItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private TextView tvItemValue;
        private TextView tvItemArtists;
        private TextView tvNowPlaying;
        private CardView itemCard;

        public GenreItemViewHolder(View rowView) {
            super(rowView);

            tvItemValue = (TextView) rowView.findViewById(R.id.genre_value);
            tvItemArtists = (TextView) rowView.findViewById(R.id.genre_artists);
            tvNowPlaying = (TextView) rowView.findViewById(R.id.genre_now_playing);
            itemCard = (CardView) rowView.findViewById(R.id.item_card_container);

            rowView.setOnClickListener(this);
            rowView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
