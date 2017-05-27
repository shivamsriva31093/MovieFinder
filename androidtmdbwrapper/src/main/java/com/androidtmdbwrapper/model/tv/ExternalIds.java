package com.androidtmdbwrapper.model.tv;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)

public class ExternalIds implements Parcelable {

    @JsonProperty("imdb_id")
    private String imdbId;
    @JsonProperty("freebase_mid")
    private String freebaseMid;
    @JsonProperty("freebase_id")
    private String freebaseId;
    @JsonProperty("tvdb_id")
    private int tvdbId;
    @JsonProperty("tvrage_id")
    private int tvrageId;
    @JsonProperty("id")
    private int id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public ExternalIds() {
    }

    protected ExternalIds(Parcel in) {
        imdbId = in.readString();
        freebaseMid = in.readString();
        freebaseId = in.readString();
        tvdbId = in.readInt();
        tvrageId = in.readInt();
        id = in.readInt();
    }

    public static final Creator<ExternalIds> CREATOR = new Creator<ExternalIds>() {
        @Override
        public ExternalIds createFromParcel(Parcel in) {
            return new ExternalIds(in);
        }

        @Override
        public ExternalIds[] newArray(int size) {
            return new ExternalIds[size];
        }
    };

    @JsonProperty("imdb_id")
    public String getImdbId() {
        return imdbId;
    }

    @JsonProperty("imdb_id")
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    @JsonProperty("freebase_mid")
    public String getFreebaseMid() {
        return freebaseMid;
    }

    @JsonProperty("freebase_mid")
    public void setFreebaseMid(String freebaseMid) {
        this.freebaseMid = freebaseMid;
    }

    @JsonProperty("freebase_id")
    public String getFreebaseId() {
        return freebaseId;
    }

    @JsonProperty("freebase_id")
    public void setFreebaseId(String freebaseId) {
        this.freebaseId = freebaseId;
    }

    @JsonProperty("tvdb_id")
    public int getTvdbId() {
        return tvdbId;
    }

    @JsonProperty("tvdb_id")
    public void setTvdbId(int tvdbId) {
        this.tvdbId = tvdbId;
    }

    @JsonProperty("tvrage_id")
    public int getTvrageId() {
        return tvrageId;
    }

    @JsonProperty("tvrage_id")
    public void setTvrageId(int tvrageId) {
        this.tvrageId = tvrageId;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imdbId);
        parcel.writeString(freebaseMid);
        parcel.writeString(freebaseId);
        parcel.writeInt(tvdbId);
        parcel.writeInt(tvrageId);
        parcel.writeInt(id);
    }
}