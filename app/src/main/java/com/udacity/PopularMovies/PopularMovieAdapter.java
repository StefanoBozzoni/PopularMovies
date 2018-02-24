package com.udacity.PopularMovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.PopularMovies.model.MovieItem;
import com.udacity.PopularMovies.utils.JsonUtils;

import java.net.URL;

public class PopularMovieAdapter extends RecyclerView.Adapter<PopularMovieAdapter.PMViewHolder> {
    // moviesData;
    MovieItem[] moviesData;
    Context rcContext;

    public class PMViewHolder extends RecyclerView.ViewHolder {
        public final ImageView movieImage;
        
        public PMViewHolder(View view) {
            super(view);
            movieImage = (ImageView) view.findViewById(R.id.imageView_pm);
        }
    }

    @Override
    public PMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout in the view and return the viewholder
        Context context = parent.getContext();
        rcContext=context;
        int layoutIdForMovies = R.layout.popular_movies;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForMovies, parent, shouldAttachToParentImmediately);
        return new PMViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PMViewHolder holder, int position) {
        //Get the data[position] and load it in the viewholder
        //if ((getItemCount()!=0) && (!moviesData[position].equals(""))) {
            //example uri : http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
            String url = JsonUtils.POSTER_BASE_URL + moviesData[position].getPoster_path();   //   moviesData[position]
            if (holder.movieImage!=null) {
                Picasso.with(rcContext).load(url).into(holder.movieImage);
            }
    }

    @Override
    public int getItemCount() {
        int len=-1;
        if (moviesData!=null)
           len = moviesData.length;
        return len;
    }

    public void setMoviesData(MovieItem[] movies) {
        moviesData = movies;
        notifyDataSetChanged();
    }

}
