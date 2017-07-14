package com.androidtmdbwrapper.model.movies;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.core.Dates;
import com.androidtmdbwrapper.model.core.SearchResult;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sHIVAM on 7/12/2017.
 */

public class MiscellaneousResults<T extends MediaBasic> extends SearchResult<T> implements Parcelable {
    @JsonProperty("dates")
    private Dates duration;

    public MiscellaneousResults() {
    }

    public MiscellaneousResults(Parcel in, Dates duration) {
        super(in);
        this.duration = duration;
    }

    public Dates getDuration() {
        return duration;
    }

    public void setDuration(Dates duration) {
        this.duration = duration;
    }

    protected MiscellaneousResults(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MiscellaneousResults> CREATOR = new Creator<MiscellaneousResults>() {
        @Override
        public MiscellaneousResults createFromParcel(Parcel in) {
            return new MiscellaneousResults(in);
        }

        @Override
        public MiscellaneousResults[] newArray(int size) {
            return new MiscellaneousResults[size];
        }
    };
}
