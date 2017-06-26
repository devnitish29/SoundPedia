package com.development.nitish.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.development.nitish.R;
import com.development.nitish.adapter.SongsAdapter;
import com.development.nitish.interfaces.ItemClickListener;
import com.development.nitish.model.Result;
import com.development.nitish.model.Song;
import com.development.nitish.network.RestClientAdapter;
import com.development.nitish.restapi.SongApi;
import com.development.nitish.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchScreenActivity extends AppCompatActivity implements ItemClickListener {

    @BindView(R.id.edArtist)
    EditText edArtist;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.btnClear)
    Button btnClear;

    @BindView(R.id.btnMike)
    ImageButton btnMike;

    @BindView(R.id.rcTrackList)
    RecyclerView rcTrackList;

    SongApi apiAdapter;
    List<Result> tracksList = new ArrayList<>();
    SongsAdapter songsAdapter;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        ButterKnife.bind(this);
        initRestAdapter();
        initViews();
    }

    private void initViews() {
/*
        edArtist = (EditText) findViewById(R.id.edArtist);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnMike = (ImageButton) findViewById(R.id.btnMike);
        btnMike.setOnClickListener(this);
        rcTrackList = (RecyclerView) findViewById(R.id.rcTrackList);*/

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchScreenActivity.this);
        rcTrackList.setLayoutManager(linearLayoutManager);
        songsAdapter = new SongsAdapter(SearchScreenActivity.this, tracksList, this);
        rcTrackList.setAdapter(songsAdapter);

        getListVisibility();

    }

    private void initRestAdapter() {

        apiAdapter = RestClientAdapter.createRestAdapter(SongApi.class, Constants.BASE_URL, this);
    }


    @OnClick(R.id.btnSubmit)
    public void getAllSongs() {

        if (edArtist.getText().toString().trim().length() > 0) {
            String artistName = edArtist.getText().toString().trim();
            getTracks(artistName);

        } else {
            Toast.makeText(SearchScreenActivity.this, "Please enter an artist name", Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick(R.id.btnClear)
    public void clearAll() {
        if (edArtist != null) {

            edArtist.setText("");
            tracksList.clear();
            getListVisibility();
        }
    }


    @OnClick(R.id.btnMike)
    public void showGoogleStt() {
        if (edArtist != null) {

            edArtist.setText("");
            tracksList.clear();
            getListVisibility();
        }
        promptSpeechInput();

    }
/*    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnSubmit) {



        } else if (v.getId() == R.id.btnClear) {

            if (edArtist != null) {

                edArtist.setText("");
                tracksList.clear();
                getListVisibility();
            }
        } else if (v.getId() == R.id.btnMike) {
            if (edArtist != null) {

                edArtist.setText("");
                tracksList.clear();
                getListVisibility();
            }
            promptSpeechInput();
        }

    }*/


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void getListVisibility() {
        if (tracksList.size() <= 0) {
            rcTrackList.setVisibility(View.GONE);
            if (songsAdapter != null) {
                songsAdapter.notifyDataSetChanged();
            }
        } else {
            rcTrackList.setVisibility(View.VISIBLE);
            if (songsAdapter != null) {
                songsAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edArtist.setText("");
                    edArtist.setText(result.get(0));
                    getTracks(result.get(0));
                }
                break;
            }

        }
    }

    private void getTracks(String artistName) {

        Call<Song> trackApiCall = apiAdapter.getSongs(artistName);

        trackApiCall.enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {


                if (response.isSuccessful()) {
                    tracksList.clear();

                    tracksList.addAll(response.body().getResults());
                    songsAdapter.notifyDataSetChanged();
                    getListVisibility();

                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {

                Toast.makeText(SearchScreenActivity.this, "No song Found For this artist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClickListener(View view, int position) {


        Intent intent = new Intent(SearchScreenActivity.this, SongDetailActivity.class);
        intent.putExtra("TRACK", tracksList.get(position));
        startActivity(intent);
    }
}
