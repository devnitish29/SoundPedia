package com.development.nitish.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.development.nitish.R;
import com.development.nitish.interfaces.ItemClickListener;
import com.development.nitish.model.Result;
import com.development.nitish.utils.Util;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nitish Singh Rathore on 25/6/17.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    Context mContext;
    List<Result> trackList;
    ItemClickListener itemClickListener;


    public SongsAdapter(Context mContext, List<Result> trackList, ItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.trackList = trackList;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_track_layout, parent, false);
        return new SongsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (trackList.get(position) != null) {
            Result track = trackList.get(position);
            if (track.getTrackName() != null) {
                holder.txtTrackName.setText(track.getTrackName());
            }

            if (track.getArtistName() != null) {
                holder.txtArtistName.setText(track.getArtistName());
            }

            if (track.getPrimaryGenreName() != null) {
                holder.txtGenre.setText(track.getPrimaryGenreName());
            }

            if (track.getTrackPrice() != null) {
                holder.txtPrice.setText("$" + track.getTrackPrice());
            }

            if (track.getTrackTimeMillis() != null) {
                holder.txtDuration.setText(Util.convertTime(track.getTrackTimeMillis()));
            }


            Glide.with(mContext).load(track.getArtworkUrl100())
                    .into(holder.imgTrack);


            holder.cvMainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClickListener(v, position);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cvMainView;
        CircleImageView imgTrack;
        TextView txtTrackName;
        TextView txtArtistName;
        TextView txtGenre;
        TextView txtPrice;
        TextView txtDuration;


        public ViewHolder(View itemView) {
            super(itemView);

            cvMainView = (CardView) itemView.findViewById(R.id.cvMainLayout);
            imgTrack = (CircleImageView) itemView.findViewById(R.id.imgTrack);
            txtTrackName = (TextView) itemView.findViewById(R.id.txtTrackName);
            txtArtistName = (TextView) itemView.findViewById(R.id.txtArtistName);
            txtGenre = (TextView) itemView.findViewById(R.id.txtgenre);
            txtPrice = (TextView) itemView.findViewById(R.id.txtprice);
            txtDuration = (TextView) itemView.findViewById(R.id.txtduration);
        }
    }
}