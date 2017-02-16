package com.androidtmdbwrapper.model.collection;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.core.AbstractNameId;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sHIVAM on 2/16/2017.
 */

public class CollectionInfo extends AbstractNameId implements Parcelable {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("parts")
    private List<Collection> parts = new ArrayList<>();

    public CollectionInfo() {
    }

    protected CollectionInfo(Parcel in) {
        super(in);
        id = in.readInt();
        name = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        parts = in.createTypedArrayList(Collection.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeTypedList(parts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CollectionInfo> CREATOR = new Creator<CollectionInfo>() {
        @Override
        public CollectionInfo createFromParcel(Parcel in) {
            return new CollectionInfo(in);
        }

        @Override
        public CollectionInfo[] newArray(int size) {
            return new CollectionInfo[size];
        }
    };

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<Collection> getParts() {
        return parts;
    }

    public void setParts(List<Collection> parts) {
        this.parts = parts;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
