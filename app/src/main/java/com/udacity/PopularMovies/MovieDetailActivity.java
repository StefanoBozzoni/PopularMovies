package com.udacity.PopularMovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.PopularMovies.model.MovieItem;
import com.udacity.PopularMovies.utils.JsonUtils;

public class MovieDetailActivity extends AppCompatActivity {

    private final String TAG = MovieDetailActivity.class.getSimpleName();

    private ImageView m_IV_poster;
    private TextView m_TV_title;
    private TextView m_TV_releaseDate;
    private TextView m_TV_voteAverage;
    private TextView m_TV_overview;
    private RatingBar m_RB_voterAverage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        m_IV_poster = (ImageView)findViewById(R.id.iv_movie_details_tb);
        m_TV_title = (TextView)findViewById(R.id.tv_title);
        m_TV_releaseDate = (TextView)findViewById(R.id.tv_date_released);
        m_TV_voteAverage = (TextView)findViewById(R.id.tv_vote_average);
        m_TV_overview = (TextView)findViewById(R.id.tv_overview);
        m_RB_voterAverage = (RatingBar)findViewById(R.id.rb_voter_average);

        Intent intent = getIntent();
        if ((intent!=null) && (intent.hasExtra("ARRAY"))) {
            MovieItem thisMovie = (MovieItem) intent.getExtras().get("ARRAY");
            Toast.makeText(this,thisMovie.getOriginal_title(),Toast.LENGTH_SHORT).show();

            String url = JsonUtils.POSTER_BASE_URL + thisMovie.getPoster_path();   //   moviesData[position]
            Picasso.with(this).load(url).into(m_IV_poster);

            //Picasso.with(this).load(thisMovie.getPosterDetailsImageUri()).into(m_IV_poster);
            m_TV_title.setText(thisMovie.getOriginal_title());
            m_TV_releaseDate.setText(thisMovie.getRelease_date().toString());
            m_TV_voteAverage.setText(String.valueOf(thisMovie.getVote_avarage()));
            m_TV_overview.setText(thisMovie.getOverview());
            m_RB_voterAverage.setRating(thisMovie.getVote_avarage());

        }

        /*
        Intent intentThatStartedMovieDetails = getIntent();
        if(intentThatStartedMovieDetails != null) {
            if(intentThatStartedMovieDetails.hasExtra(Intent.EXTRA_TEXT)) {
                String movieAsJson = intentThatStartedMovieDetails.getStringExtra(Intent.EXTRA_TEXT);
                MovieItem moviePassed = new Gson().fromJson(movieAsJson, Movie.class);

                Picasso.with(this).load(moviePassed.getPosterDetailsImageUri()).into(m_IV_poster);
                m_TV_title.setText(moviePassed.getTitle());
                m_TV_releaseDate.setText(moviePassed.getReleaseDateForDisplay());
                m_TV_voteAverage.setText(String.valueOf(moviePassed.getVoteAverage()));
                m_TV_overview.setText(moviePassed.getOverview());
                m_RB_voterAverage.setRating(moviePassed.getVoteAverage());
            }
        }
        */
    }

    public void OnClickBackToList(View view) {
        Context context = this;
        Class destinationClass = MainActivity.class;
        Intent intentToStartMainActivity = new Intent(context, destinationClass);
        startActivity(intentToStartMainActivity);
    }
}
