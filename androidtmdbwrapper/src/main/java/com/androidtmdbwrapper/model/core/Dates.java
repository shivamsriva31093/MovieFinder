package com.androidtmdbwrapper.model.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sHIVAM on 7/12/2017.
 */

public class Dates implements Parcelable {
    @JsonProperty("maximum")
    private String maxDate;
    @JsonProperty("minimum")
    private String mindate;

    public Dates() {
    }

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    public String getMindate() {
        return mindate;
    }

    public void setMindate(String mindate) {
        this.mindate = mindate;
    }

    protected Dates(Parcel in) {
        maxDate = in.readString();
        mindate = in.readString();
    }

    public static final Creator<Dates> CREATOR = new Creator<Dates>() {
        @Override
        public Dates createFromParcel(Parcel in) {
            return new Dates(in);
        }

        @Override
        public Dates[] newArray(int size) {
            return new Dates[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(maxDate);
        parcel.writeString(mindate);
    }
}
