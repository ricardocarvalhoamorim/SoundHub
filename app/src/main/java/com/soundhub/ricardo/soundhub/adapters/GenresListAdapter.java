package com.soundhub.ricardo.soundhub.adapters;

/**
 * Created by ricardo on 17-03-2015.
 */

import android.content.Context;
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
    private Context mContext;


    public GenresListAdapter(ArrayList<GenreItem> srcItems,
                                 OnItemClickListener clickListener,
                                 Context context) {

        this.items = srcItems;
        this.mContext = context;
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

                int colorResId;
                if (item.isNowPlaying()) {
                    colorResId =  mContext.getResources().getColor(R.color.fab_material_teal_900);
                    ((GenreItemViewHolder) holder).itemCard.setCardElevation(26);
                    ((GenreItemViewHolder) holder).tvItemValue.setTextColor(mContext.getResources().getColor(R.color.fab_material_white));
                    ((GenreItemViewHolder) holder).tvItemArtists.setTextColor(mContext.getResources().getColor(R.color.fab_material_white));
                    ((GenreItemViewHolder) holder).tvNowPlaying.setTextColor(mContext.getResources().getColor(R.color.fab_material_white));
                    ((GenreItemViewHolder) holder).tvPlayCount.setTextColor(mContext.getResources().getColor(R.color.fab_material_white));
                    ((GenreItemViewHolder) holder).tvNowPlaying.setVisibility(View.VISIBLE);
                } else {
                    colorResId =  mContext.getResources().getColor(R.color.fab_material_white);
                    ((GenreItemViewHolder) holder).itemCard.setCardElevation(6);
                    ((GenreItemViewHolder) holder).tvItemValue.setTextColor(mContext.getResources().getColor(R.color.background_floating_material_dark));
                    ((GenreItemViewHolder) holder).tvItemArtists.setTextColor(mContext.getResources().getColor(R.color.background_floating_material_dark));
                    ((GenreItemViewHolder) holder).tvPlayCount.setTextColor(mContext.getResources().getColor(R.color.background_floating_material_dark));
                    ((GenreItemViewHolder) holder).tvNowPlaying.setVisibility(View.INVISIBLE);
                }

                ((GenreItemViewHolder) holder).itemCard.setCardBackgroundColor(colorResId);

                ((GenreItemViewHolder) holder).tvItemValue.setText(item.getGenreValue());
                if (!item.getArtists().equals("")) {
                    ((GenreItemViewHolder) holder).tvItemArtists.setText(item.getArtists());
                    ((GenreItemViewHolder) holder).tvItemArtists.setVisibility(View.VISIBLE);
                } else  {
                    ((GenreItemViewHolder) holder).tvItemArtists.setVisibility(View.GONE);
                }

                if (item.getPlayCount() > 0) {
                    ((GenreItemViewHolder) holder).tvPlayCount.setVisibility(View.VISIBLE);
                    ((GenreItemViewHolder) holder).tvPlayCount.setText(item.getPlayCount() + " times");
                } else {
                    ((GenreItemViewHolder) holder).tvPlayCount.setVisibility(View.INVISIBLE);
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
        private TextView tvPlayCount;
        private CardView itemCard;

        public GenreItemViewHolder(View rowView) {
            super(rowView);

            tvItemValue = (TextView) rowView.findViewById(R.id.genre_value);
            tvItemArtists = (TextView) rowView.findViewById(R.id.genre_artists);
            tvNowPlaying = (TextView) rowView.findViewById(R.id.genre_now_playing);
            tvPlayCount = (TextView) rowView.findViewById(R.id.genre_play_count);
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
