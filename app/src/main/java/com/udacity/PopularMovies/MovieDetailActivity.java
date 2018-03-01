package com.udacity.PopularMovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.PopularMovies.model.MovieItem;
import com.udacity.PopularMovies.utils.JsonUtils;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_OBJ_EXTRA = "ARRAY";
    private final String TAG = MovieDetailActivity.class.getSimpleName();

    private ImageView m_poster;
    private TextView  m_title;
    private TextView  m_releaseDate;
    private TextView  m_voteAverage;
    private TextView  m_overview;
    private RatingBar m_voterAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        m_poster = (ImageView)findViewById(R.id.moviePoster);
        m_title = (TextView)findViewById(R.id.title_tv);
        m_releaseDate = (TextView)findViewById(R.id.date_released_tv);
        m_voteAverage = (TextView)findViewById(R.id.vote_average_tv);
        m_overview = (TextView)findViewById(R.id.overview_tv);
        m_voterAverage = (RatingBar)findViewById(R.id.voter_average);

        Intent intent = getIntent();
        if ((intent.getExtras()!=null) && (intent.hasExtra(MOVIE_OBJ_EXTRA))) {
            MovieItem thisMovie =  (MovieItem) intent.getExtras().get(MOVIE_OBJ_EXTRA);
            if (thisMovie.getOriginal_title()!=null)
                Toast.makeText(this,thisMovie.getOriginal_title(),Toast.LENGTH_SHORT).show();

            String url = JsonUtils.POSTER_BASE_URL + thisMovie.getPoster_path();   //   moviesData[position]
            Picasso.with(this).load(url).error(R.drawable.ic_error).into(m_poster);

            m_title.setText(thisMovie.getOriginal_title());
            m_releaseDate.setText(thisMovie.getRelease_date_printable());
            m_voteAverage.setText(String.valueOf(thisMovie.getVote_average()));
            m_overview.setText(thisMovie.getOverview());
            m_voterAverage.setRating(thisMovie.getVote_average());
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        //Up button must behave like back button otherwise animation doesn't show
        onBackPressed();
        return true;
    }
}
