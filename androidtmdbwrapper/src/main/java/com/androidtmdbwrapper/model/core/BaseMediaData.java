package com.androidtmdbwrapper.model.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sHIVAM on 7/24/2017.
 */

public class BaseMediaData implements Parcelable {

    @JsonProperty("id")
    private int id;
    @JsonProperty("popularity")
    private float popularity;

    public BaseMediaData() {
    }


    protected BaseMediaData(Parcel in) {
        id = in.readInt();
        popularity = in.readFloat();
    }

    public static final Creator<BaseMediaData> CREATOR = new Creator<BaseMediaData>() {
        @Override
        public BaseMediaData createFromParcel(Parcel in) {
            return new BaseMediaData(in);
        }

        @Override
        public BaseMediaData[] newArray(int size) {
            return new BaseMediaData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeFloat(popularity);
    }

    @Override
    public boolean equals(Object obj) {
        BaseMediaData that = (BaseMediaData) obj;
        return this.getId() == that.getId();
    }
}
