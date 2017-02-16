package com.androidtmdbwrapper.model.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class AbstractNameId implements Parcelable {

    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private int id;

    public AbstractNameId() {
    }

    protected AbstractNameId(Parcel in) {
        name = in.readString();
        id = in.readInt();
    }

    public static final Creator<AbstractNameId> CREATOR = new Creator<AbstractNameId>() {
        @Override
        public AbstractNameId createFromParcel(Parcel in) {
            return new AbstractNameId(in);
        }

        @Override
        public AbstractNameId[] newArray(int size) {
            return new AbstractNameId[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
    }
}
