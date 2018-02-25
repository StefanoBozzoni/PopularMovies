package com.udacity.PopularMovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.preference.ListPreference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieItem implements Parcelable {
    private int vote_count;
    private int id;
    private boolean video;
    private float vote_avarage;
    private int popularity;
    private String poster_path;


    private String original_language;
    private String original_title;
    private List<Integer> genre_ids;
    private String backdrop_path;
    private boolean adult;
    private String overview;
    private Date release_date;

    public MovieItem() {

    };

    public MovieItem(int id, int vote_count, boolean video, int popularity, String poster_path,
                     String original_language, String original_title, List<Integer> genre_ids,
                     String backdrop_path, boolean adult, String overview, Date release_date) {
        this.id=id;
        this.vote_count    =vote_count;
        this.video         =video;
        this.popularity    =popularity;
        this.poster_path   =poster_path;
        this.original_language=original_language;
        this.original_title=original_title;
        this.genre_ids     =genre_ids;
        this.backdrop_path =backdrop_path;
        this.adult         =adult;
        this.overview      =overview;
        this.release_date  =release_date;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVote_avarage() {
        return vote_avarage;
    }

    public void setVote_avarage(float vote_avarage) {
        this.vote_avarage = vote_avarage;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.vote_count);
        dest.writeInt(this.id);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.vote_avarage);
        dest.writeInt(this.popularity);
        dest.writeString(this.poster_path);
        dest.writeString(this.original_language);
        dest.writeString(this.original_title);
        dest.writeList(this.genre_ids);
        dest.writeString(this.backdrop_path);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeLong(this.release_date != null ? this.release_date.getTime() : -1);
    }

    protected MovieItem(Parcel in) {
        this.vote_count = in.readInt();
        this.id = in.readInt();
        this.video = in.readByte() != 0;
        this.vote_avarage = in.readFloat();
        this.popularity = in.readInt();
        this.poster_path = in.readString();
        this.original_language = in.readString();
        this.original_title = in.readString();
        this.genre_ids = new ArrayList<Integer>();
        in.readList(this.genre_ids, Integer.class.getClassLoader());
        this.backdrop_path = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        long tmpRelease_date = in.readLong();
        this.release_date = tmpRelease_date == -1 ? null : new Date(tmpRelease_date);
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}
