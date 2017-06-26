package com.development.nitish.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.development.nitish.R;
import com.development.nitish.model.Result;
import com.development.nitish.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SongDetailActivity extends AppCompatActivity {

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.txtArtistName)
    TextView txtArtistName;

    @BindView(R.id.txtAlbumName)
    TextView txtAlbumName;

    @BindView(R.id.txtPrice)
    TextView txtTrackPrice;

    @BindView(R.id.txtAlbumPrice)
    TextView txtAlbumPrice;

    @BindView(R.id.txtGenre)
    TextView txtGenre;

    @BindView(R.id.txtReleaseDate)
    TextView txtReleaseDate;

    @BindView(R.id.txtDuration)
    TextView txtDuration;

    @BindView(R.id.txtTotalTracks)
    TextView txtTrackCount;

    @BindView(R.id.txtCountry)
    TextView txtCountry;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imgTrack)
    ImageView imgTrack;


    private static final String TAG = "NITISH";
    Result track = new Result();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        track = (Result) intent.getSerializableExtra("TRACK");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);


        collapsingToolbarLayout.setTitle(track.getTrackName());
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);


        Glide.with(SongDetailActivity.this).asBitmap().load(track.getArtworkUrl100()).into(new BitmapImageViewTarget(imgTrack) {
            @Override
            public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
                dynamicToolbarColor(resource);
            }
        });

        populateViews(track);
    }

    private void populateViews(Result track) {


        txtArtistName.setText(track.getArtistName());
        txtAlbumName.setText(track.getCollectionName());
        txtAlbumPrice.setText("$" + track.getCollectionPrice());
        txtCountry.setText(track.getCountry());
        txtDuration.setText(Util.convertTime(track.getTrackTimeMillis()));
        txtGenre.setText(track.getPrimaryGenreName());
        txtReleaseDate.setText(Util.convertDate(track.getReleaseDate()));
        txtTrackPrice.setText("$" + track.getTrackPrice());
        txtTrackCount.setText("" + track.getTrackCount());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }


    private void dynamicToolbarColor(Bitmap bitmap) {


        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {

                int mutedColor = palette.getMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                int mutedDarkColor = palette.getDarkMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));


                collapsingToolbarLayout.setContentScrimColor(mutedColor);
                collapsingToolbarLayout.setStatusBarScrimColor(mutedDarkColor);
            }
        });
    }


}
