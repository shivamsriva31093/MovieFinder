package com.androidtmdbwrapper.model.movies;

import android.os.Parcel;

import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
/**
 * Created by sHIVAM on 2/14/2017.
 */

public class BasicMovieInfo extends MediaBasic {
    @JsonProperty("adult")
    private boolean adult;
    @JsonProperty("original_title")
    private String originalTitle;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("title")
    private String title;
    @JsonProperty("video")
    private Boolean video = null;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    @JsonProperty("original_language")
    private String originalLanguage;

    public BasicMovieInfo() {

    }


    protected BasicMovieInfo(Parcel in) {
        super(in);
        adult = in.readByte() != 0;
        originalTitle = in.readString();
        releaseDate = in.readString();
        title = in.readString();
        overview = in.readString();
        originalLanguage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(originalTitle);
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(originalLanguage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BasicMovieInfo> CREATOR = new Creator<BasicMovieInfo>() {
        @Override
        public BasicMovieInfo createFromParcel(Parcel in) {
            return new BasicMovieInfo(in);
        }

        @Override
        public BasicMovieInfo[] newArray(int size) {
            return new BasicMovieInfo[size];
        }
    };

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }
}
