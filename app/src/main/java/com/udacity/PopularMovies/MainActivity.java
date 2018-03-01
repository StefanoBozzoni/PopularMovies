package com.udacity.PopularMovies;

import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.udacity.PopularMovies.model.MovieItem;
import com.udacity.PopularMovies.utils.JsonUtils;
import com.udacity.PopularMovies.MyApplication;

import java.net.URL;

public class MainActivity extends AppCompatActivity
         implements PopularMovieAdapter.PopularMovieAdapterOnClickHandler,
                    LoaderManager.LoaderCallbacks<MovieItem[]> {

    PopularMovieAdapter mPopularMovieAdapter;
    Parcelable mRecyclerViewState;

    private static final int    MOVIEDB_SEARCH_LOADER_ID = 25;
    private static final String SEARCH_QUERY_URL_EXTRA   ="QUERY";
    private static final String MOST_POPULAR_QUERY_TAG   ="popular";
    private static final String TOP_RATED_QUERY_TAG      ="top_rated";
    private static final String MENU_FILTER_TAG          = "used_filter_choice";
    private static final int    GRID_SPAN_COUNT          = 3;

    private Menu mMenu;
    private boolean appIsLaunched;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("RecyclerView-State",mRecyclerViewState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        mRecyclerViewState=savedInstanceState.getParcelable("RecyclerView-State");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!=null)
           mRecyclerViewState=savedInstanceState.getParcelable("RecyclerView-State");
        setContentView(R.layout.activity_main);

        MyRecyclerView myRecyclerView = (MyRecyclerView) findViewById(R.id.movies_rv);
        MyRecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setHasFixedSize(true);
        final MyApplication myApplication = (MyApplication) getApplicationContext();
        appIsLaunched = myApplication.isJustStarted();
        myApplication.setJustStarted(false);

        Bundle queryBundle = new Bundle();

        if (appIsLaunched) {
            myRecyclerView.resetScrollPosition();
            queryBundle.putString(SEARCH_QUERY_URL_EXTRA, MOST_POPULAR_QUERY_TAG); //set loader with most popular filter

        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String current_Tag_filter = prefs.getString(MENU_FILTER_TAG, MOST_POPULAR_QUERY_TAG);
            queryBundle.putString(SEARCH_QUERY_URL_EXTRA, current_Tag_filter); //restore loader with last used filter
        }


        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(MOVIEDB_SEARCH_LOADER_ID, queryBundle, MainActivity.this);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<MovieItem[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieItem[]>(this) {
            MovieItem[] mMovies = null;
            @Override
            protected void onStartLoading() {
                ProgressBar myProgressBar=(ProgressBar) findViewById(R.id.progressBar);
                myProgressBar.setVisibility(View.VISIBLE);
                //ProgressDialog m_PD_loadingIndicator = ProgressDialog.show(MainActivity.this, "Wait...", "loading movies...", true);
                if (mMovies != null) {
                    deliverResult(mMovies);
                    myProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    forceLoad();
                }

            }

            @Override
            public MovieItem[] loadInBackground() {
                String tipoQuery;
                if (args!=null)
                    tipoQuery = args.getString(SEARCH_QUERY_URL_EXTRA);
                else
                    tipoQuery = MOST_POPULAR_QUERY_TAG;

                URL MovieDBURL;
                if (tipoQuery.equals(MOST_POPULAR_QUERY_TAG))
                    MovieDBURL = JsonUtils.buildUrl(MOST_POPULAR_QUERY_TAG);
                else
                    MovieDBURL = JsonUtils.buildUrl(TOP_RATED_QUERY_TAG);

                try {

                    String jsonMovieDBResponse = JsonUtils.getResponseFromHttpUrl(MovieDBURL);
                    /* MovieItem[] JsonMovies = JsonUtils.parseMoviesJson(jsonMovieDBResponse); */
                    return JsonUtils.parseMoviesJson(jsonMovieDBResponse);

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
        ProgressBar myProgressBar=(ProgressBar) findViewById(R.id.progressBar);
        myProgressBar.setVisibility(View.INVISIBLE);

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

            MyRecyclerView myRecyclerView = (MyRecyclerView) findViewById(R.id.movies_rv);
            myRecyclerView.restoreScrollPosition();
            mPopularMovieAdapter = new PopularMovieAdapter(this);
            myRecyclerView.setAdapter(mPopularMovieAdapter); /* Setting the adapter attaches it to the RecyclerView in our layout. */
            mPopularMovieAdapter.setMoviesData(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieItem[]> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu,menu);
        mMenu = menu;
        super.onCreateOptionsMenu(menu);
        if (appIsLaunched) {
            ShowActiveMenuItem(mMenu,getString(R.string.popular_menu));
        }
        else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String menuToActivate = prefs.getString(MENU_FILTER_TAG,getString(R.string.popular_menu));
            ShowActiveMenuItem(mMenu,menuToActivate);
        }
        return true;
    }
    @Override
    public void onClick(View viewStart, MovieItem aMovie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        MyRecyclerView myRecyclerView = (MyRecyclerView) findViewById(R.id.movies_rv);
        myRecyclerView.storeScrollPosition();
        intent.putExtra(MovieDetailActivity.MOVIE_OBJ_EXTRA,aMovie);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //mRecyclerViewState=myRecyclerView.onSaveInstanceState();
        String transitionName=getString(R.string.transition_string);
        //View viewStart=findViewById(R.id.imageView_pm);
        ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(this,viewStart,transitionName);
        //startActivity(intent);
        ActivityCompat.startActivity(this,intent,options.toBundle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ((item.getItemId()==R.id.topRated) || (item.getItemId()==R.id.mostPopular)) {
            LoaderManager loaderManager=getSupportLoaderManager();
            Bundle queryBundle = new Bundle();

            if (item.getItemId()==R.id.topRated)
                queryBundle.putString(SEARCH_QUERY_URL_EXTRA, TOP_RATED_QUERY_TAG);
            else
                queryBundle.putString(SEARCH_QUERY_URL_EXTRA, MOST_POPULAR_QUERY_TAG);

            ShowActiveMenuItem(mMenu,item.getTitle().toString());

            mPopularMovieAdapter.setMoviesData(null);
            loaderManager.restartLoader(MOVIEDB_SEARCH_LOADER_ID, queryBundle, MainActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowActiveMenuItem(Menu pMenu, String menuActivated) {
        MenuItem aMenuItem;
        aMenuItem = pMenu.findItem(R.id.topRated);
        aMenuItem.setTitle(getString(R.string.topRated_menu));
        aMenuItem = pMenu.findItem(R.id.mostPopular);
        aMenuItem.setTitle(getString(R.string.popular_menu));
        if (menuActivated.equals(getString(R.string.popular_menu)))
            aMenuItem = pMenu.findItem(R.id.mostPopular);
         else
            aMenuItem = pMenu.findItem(R.id.topRated);

        aMenuItem.setTitle(Html.fromHtml("<B><U>" + menuActivated + "</U><B>"));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString(MENU_FILTER_TAG, menuActivated).apply();
    }


}
