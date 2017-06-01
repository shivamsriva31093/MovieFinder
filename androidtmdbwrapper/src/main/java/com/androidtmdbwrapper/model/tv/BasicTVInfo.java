package com.androidtmdbwrapper.model.tv;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sHIVAM on 5/27/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)

public class BasicTVInfo extends MediaBasic implements Parcelable {

    @JsonProperty("overview")
    private String overview;
    @JsonProperty("first_air_date")
    private String firstAirDate;
    @JsonProperty("origin_country")
    List<String> originCountry = Collections.EMPTY_LIST;
    @JsonProperty("genre_ids")
    List<Integer> genreIds = Collections.EMPTY_LIST;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("name")
    private String name;
    @JsonProperty("original_name")
    private String originalName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public BasicTVInfo() {
    }

    protected BasicTVInfo(Parcel in) {
        super(in);
        overview = in.readString();
        firstAirDate = in.readString();
        originCountry = in.createStringArrayList();
        originalLanguage = in.readString();
        name = in.readString();
        originalName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(overview);
        dest.writeString(firstAirDate);
        dest.writeStringList(originCountry);
        dest.writeString(originalLanguage);
        dest.writeString(name);
        dest.writeString(originalName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BasicTVInfo> CREATOR = new Creator<BasicTVInfo>() {
        @Override
        public BasicTVInfo createFromParcel(Parcel in) {
            return new BasicTVInfo(in);
        }

        @Override
        public BasicTVInfo[] newArray(int size) {
            return new BasicTVInfo[size];
        }
    };

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
