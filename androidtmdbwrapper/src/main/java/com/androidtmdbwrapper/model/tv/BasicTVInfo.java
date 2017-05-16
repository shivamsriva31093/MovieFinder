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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "overview",
        "first_air_date",
        "origin_country",
        "genre_ids",
        "original_language",
        "name",
        "original_name"
})
public class BasicTVInfo extends MediaBasic implements Parcelable {

    @JsonProperty("overview")
    private String overview;
    @JsonProperty("first_air_date")
    private String firstAirDate;
    @JsonProperty("origin_country")
    private List<String> originCountry = new ArrayList<String>();
    @JsonProperty("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("name")
    private String name;
    @JsonProperty("original_name")
    private String originalName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public BasicTVInfo() {
    }

    /**
     * @param genreIds
     * @param originCountry
     * @param overview
     * @param name
     * @param firstAirDate
     * @param originalName
     * @param originalLanguage
     */
    public BasicTVInfo(String overview, String firstAirDate, List<String> originCountry, List<Integer> genreIds, String originalLanguage, String name, String originalName) {
        super();
        this.overview = overview;
        this.firstAirDate = firstAirDate;
        this.originCountry = originCountry;
        this.genreIds = genreIds;
        this.originalLanguage = originalLanguage;
        this.name = name;
        this.originalName = originalName;
    }

    protected BasicTVInfo(Parcel in) {
        overview = in.readString();
        firstAirDate = in.readString();
        originCountry = in.createStringArrayList();
        originalLanguage = in.readString();
        name = in.readString();
        originalName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(overview);
        parcel.writeString(firstAirDate);
        parcel.writeStringList(originCountry);
        parcel.writeString(originalLanguage);
        parcel.writeString(name);
        parcel.writeString(originalName);
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

    @JsonProperty("overview")
    public String getOverview() {
        return overview;
    }

    @JsonProperty("overview")
    public void setOverview(String overview) {
        this.overview = overview;
    }

    @JsonProperty("first_air_date")
    public String getFirstAirDate() {
        return firstAirDate;
    }

    @JsonProperty("first_air_date")
    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    @JsonProperty("origin_country")
    public List<String> getOriginCountry() {
        return originCountry;
    }

    @JsonProperty("origin_country")
    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    @JsonProperty("genre_ids")
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    @JsonProperty("genre_ids")
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    @JsonProperty("original_language")
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    @JsonProperty("original_language")
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("original_name")
    public String getOriginalName() {
        return originalName;
    }

    @JsonProperty("original_name")
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
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


}