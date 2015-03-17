package com.soundhub.ricardo.soundhub.adapters;

/**
 * Created by ricardo on 17-03-2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.soundhub.ricardo.soundhub.R;
import com.soundhub.ricardo.soundhub.interfaces.OnItemClickListener;
import com.soundhub.ricardo.soundhub.models.GenreItem;

import java.util.ArrayList;


public class GenresListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<GenreItem> items;
    private int lastPosition = -1;

    private static OnItemClickListener listener;


    public GenresListAdapter(ArrayList<GenreItem> srcItems,
                                 OnItemClickListener clickListener,
                                 Context context) {

        this.context = context;
        this.items = srcItems;
        listener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_genre, parent, false);
        return new GenreItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            //holder.tvTal.setText
        GenreItem item = items.get(position);

        ((GenreItemViewHolder) holder).tvItemValue.setText(item.getGenreValue());

        if (item.getSingers().size() != 0) {
            ((GenreItemViewHolder) holder).tvItemArtists.setVisibility(View.VISIBLE);
            ((GenreItemViewHolder) holder).tvItemArtists.setText(item.getSingers().toString());
        } else {
            ((GenreItemViewHolder) holder).tvItemArtists.setVisibility(View.GONE);
        }

        if (item.getPlayCount() > 0) {
            ((GenreItemViewHolder) holder).tvItemPlayCount.setVisibility(View.VISIBLE);
            ((GenreItemViewHolder) holder).tvItemPlayCount.setText("Played: " + item.getPlayCount());
        } else {
            ((GenreItemViewHolder) holder).tvItemPlayCount.setVisibility(View.GONE);
        }

        if (item.isNowPlaying()) {
            ((GenreItemViewHolder) holder).tvNowPlaying.setVisibility(View.VISIBLE);
        } else {
            ((GenreItemViewHolder) holder).tvNowPlaying.setVisibility(View.GONE);
        }

        ((GenreItemViewHolder) holder).tvItemLastPlayed.setText("Last played: " + item.getLastPlayed());
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class GenreItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private TextView tvItemValue;
        private TextView tvItemPlayCount;
        private TextView tvItemLastPlayed;
        private TextView tvItemArtists;
        private TextView tvNowPlaying;


        public GenreItemViewHolder(View rowView) {
            super(rowView);

            tvItemValue = (TextView) rowView.findViewById(R.id.genre_value);
            tvItemArtists = (TextView) rowView.findViewById(R.id.genre_play_artists);
            tvItemLastPlayed = (TextView) rowView.findViewById(R.id.genre_play_last_played);
            tvItemPlayCount = (TextView) rowView.findViewById(R.id.genre_play_count);
            tvNowPlaying = (TextView) rowView.findViewById(R.id.genre_now_playing);

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

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
