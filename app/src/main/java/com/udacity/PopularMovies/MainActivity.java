package com.udacity.PopularMovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.udacity.PopularMovies.model.MovieItem;
import com.udacity.PopularMovies.utils.JsonUtils;

import java.net.URL;



public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<MovieItem[]> {


    private static final int MOVIEDB_SEARCH_LOADER = 22;
    private static final String SEARCH_QUERY_URL_EXTRA="QUERY";
    private static final String MOST_POPULAR_QUERY_TAG="popular";
    private static final String TOP_RATED_QUERY_TAG="top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportLoaderManager().initLoader(MOVIEDB_SEARCH_LOADER, null, this);
        String movieDBQuery =JsonUtils.buildUrl("popular").toString();
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, movieDBQuery);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(MOVIEDB_SEARCH_LOADER, queryBundle, this);

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

    @Override
    public Loader<MovieItem[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieItem[]>(this) {
            @Override
            protected void onStartLoading() {
                /* If no arguments were passed, we don't have a query to perform. Simply return. */
                if (args == null) {
                    return;
                }
                forceLoad();
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
                super.deliverResult(movies);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieItem[]> loader, MovieItem[] movies) {
        if (movies!=null) {
            String[] moviesTitles=new String[movies.length];

            for (int i=0;i<movies.length;i++) {
                moviesTitles[i]=movies[i].getOriginal_title();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, moviesTitles);

            // Simplification: Using a ListView instead of a RecyclerView
            ListView listView = findViewById(R.id.sandwiches_listview);
            listView.setAdapter(adapter);

        }
    }

    @Override
    public void onLoaderReset(Loader<MovieItem[]> loader) {

    }
}
