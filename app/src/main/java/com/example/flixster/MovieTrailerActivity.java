package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.Models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    public static final String GET_VIDEOS_URL1 = "https://api.themoviedb.org/3/movie/";
    public static final String GET_VIDEOS_URL2 = "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";
    public String GET_VIDEOS_URL = "";
    String videoKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        int videoId = getIntent().getIntExtra("id",0);
        GET_VIDEOS_URL = GET_VIDEOS_URL1 + Integer.toString(videoId) + GET_VIDEOS_URL2;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(GET_VIDEOS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                JSONArray results = null;
                try {
                    results = jsonObject.getJSONArray("results");
                    videoKey = results.getJSONObject(1).getString("key");

                    // initialize with API key stored in secrets.xml
                    // resolve the player view from the layout
                    YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);
                    playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            // do any work here to cue video, play video, etc.
                            youTubePlayer.loadVideo(videoKey);
                        }
                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                            // log the error
                            Log.e("MovieTrailerActivity", "Error initializing YouTube player");
                        }
                    });
                } catch (JSONException e) {
                    Log.e("Youtube", "There is no trailer" );
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {

            }
        });
        //iterates through the json array and constructs a movie for each element in the JSON Array
    }
}
