package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.Models.Movie;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {
    ActivityMovieDetailsBinding detailsBinding;
    //The movie to display
    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView backdrop_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsBinding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(detailsBinding.getRoot());

        // resolve the view objects
        tvTitle = detailsBinding.tvTitle;
        tvOverview = detailsBinding.tvOverview;
        rbVoteAverage = detailsBinding.rbVoteAverage;
        backdrop_img = detailsBinding.ivBackdropImageDetails;

        //unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);

        Glide.with(getApplicationContext())
                .load(movie.getBackdropPath())
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(backdrop_img);

        backdrop_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MovieTrailerActivity.class);
                i.putExtra("id",movie.getId());
                startActivity(i);
            }
        });

    }
}