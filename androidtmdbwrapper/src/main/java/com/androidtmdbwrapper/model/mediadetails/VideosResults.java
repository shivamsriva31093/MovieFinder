package com.androidtmdbwrapper.model.mediadetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * Created by sHIVAM on 3/27/2017.
 */

public class VideosResults implements Parcelable {
    @JsonProperty("results")
    private List<Video> videos = Collections.EMPTY_LIST;

    public VideosResults() {

    }

    protected VideosResults(Parcel in) {
        videos = in.createTypedArrayList(Video.CREATOR);
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public static final Creator<VideosResults> CREATOR = new Creator<VideosResults>() {
        @Override
        public VideosResults createFromParcel(Parcel in) {
            return new VideosResults(in);
        }

        @Override
        public VideosResults[] newArray(int size) {
            return new VideosResults[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(videos);
    }
}
