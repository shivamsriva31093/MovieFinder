package com.androidtmdbwrapper.model.mediadetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class MediaBasic implements Parcelable {
    @JsonProperty("id")
    private int id;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("popularity")
    private float popularity;
    @JsonProperty("vote_average")
    private float voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;

    private String imdbRating;

    public MediaBasic() {
    }

    protected MediaBasic(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        backdropPath = in.readString();
        popularity = in.readFloat();
        voteAverage = in.readFloat();
        voteCount = in.readInt();
        imdbRating = in.readString();
    }

    public static final Creator<MediaBasic> CREATOR = new Creator<MediaBasic>() {
        @Override
        public MediaBasic createFromParcel(Parcel in) {
            return new MediaBasic(in);
        }

        @Override
        public MediaBasic[] newArray(int size) {
            return new MediaBasic[size];
        }
    };

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeFloat(popularity);
        parcel.writeFloat(voteAverage);
        parcel.writeInt(voteCount);
        parcel.writeString(imdbRating);
    }
}
