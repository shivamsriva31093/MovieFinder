package com.androidtmdbwrapper.model.mediadetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.core.BaseMediaData;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class MediaBasic extends BaseMediaData implements Parcelable {
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("vote_average")
    private float voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;

    private Map<String, Object> other = new HashMap<>();

    private String imdbRating = "";

    public MediaBasic() {
    }

    protected MediaBasic(Parcel in) {
        super(in);
        posterPath = in.readString();
        backdropPath = in.readString();
        voteAverage = in.readFloat();
        voteCount = in.readInt();
        in.readMap(other, HashMap.class.getClassLoader());
        imdbRating = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeFloat(voteAverage);
        dest.writeInt(voteCount);
        dest.writeMap(other);
        dest.writeString(imdbRating);
    }

    @Override
    public int describeContents() {
        return 0;
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

    @JsonAnyGetter
    public Map<String, Object> any() {
        return other;
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        other.put(name, value);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
