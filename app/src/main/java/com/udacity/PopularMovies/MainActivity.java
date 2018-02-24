package com.udacity.PopularMovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.udacity.PopularMovies.model.MovieItem;
import com.udacity.PopularMovies.utils.JsonUtils;

import java.net.URL;



public class MainActivity extends AppCompatActivity
         implements PopularMovieAdapter.PopularMovieAdapterOnClickHandler,
                    LoaderManager.LoaderCallbacks<MovieItem[]> {

    RecyclerView mRecyclerView;
    PopularMovieAdapter mPopularMovieAdapter;

    private static final int MOVIEDB_SEARCH_LOADER_ID = 24;
    private static final String SEARCH_QUERY_URL_EXTRA="QUERY";
    private static final String MOST_POPULAR_QUERY_TAG="popular";
    private static final String TOP_RATED_QUERY_TAG="top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String movieDBQuery =JsonUtils.buildUrl("popular").toString();
        //Bundle queryBundle = new Bundle();
        //queryBundle.putString(SEARCH_QUERY_URL_EXTRA, movieDBQuery);

        //Set the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.movies_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPopularMovieAdapter = new PopularMovieAdapter(this);
        mRecyclerView.setAdapter(mPopularMovieAdapter); /* Setting the adapter attaches it to the RecyclerView in our layout. */

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(MOVIEDB_SEARCH_LOADER_ID, null, MainActivity.this).forceLoad();

        /*
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_names);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sandwiches);

        // Simplification: Using a ListView instead of a RecyclerView
        ListView listView = findViewById(R.id.sandwiches_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchDetailActivity(position);
            }
        });
        */

    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        startActivity(intent);
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<MovieItem[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieItem[]>(this) {
            MovieItem[] mMovies = null;
            @Override
            protected void onStartLoading() {
                forceLoad();
                /*
                if (mMovies != null) {
                    deliverResult(mMovies);
                } else {
                    forceLoad();
                }
                */
            }

            @Override
            public MovieItem[] loadInBackground() {
                URL MovieDBURL = JsonUtils.buildUrl(MOST_POPULAR_QUERY_TAG);
                try {
                    String jsonMovieDBResponse = JsonUtils.getResponseFromHttpUrl(MovieDBURL);

                    //MovieItem[] JsonMovies = JsonUtils.getMovies(MainActivity.this, jsonMovieDBResponse);
                    MovieItem[] JsonMovies = JsonUtils.parseMoviesJson(jsonMovieDBResponse);

                    return JsonMovies;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(MovieItem[] movies) {
                mMovies=movies;
                super.deliverResult(movies);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieItem[]> loader, MovieItem[] movies) {
        if (movies!=null) {
            /*  titoli dei film in una listview
            String[] moviesTitles=new String[movies.length];
            for (int i=0;i<movies.length;i++) {
                moviesTitles[i]=movies[i].getOriginal_title();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, moviesTitles);
            // Simplification: Using a ListView instead of a RecyclerView
            ListView listView = findViewById(R.id.sandwiches_listview);
            listView.setAdapter(adapter);
            */

            mPopularMovieAdapter.setMoviesData(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieItem[]> loader) {

    }
    @Override
    public void onClick(MovieItem aMovie) {
        //Toast.makeText(this, "ciao", Toast.LENGTH_SHORT).show();
    }
}
