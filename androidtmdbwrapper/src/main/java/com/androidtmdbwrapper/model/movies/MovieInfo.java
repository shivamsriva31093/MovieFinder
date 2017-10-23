package com.androidtmdbwrapper.model.movies;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.collection.CollectionInfo;
import com.androidtmdbwrapper.model.core.Genre;
import com.androidtmdbwrapper.model.core.Language;
import com.androidtmdbwrapper.model.mediadetails.MediaCreditList;
import com.androidtmdbwrapper.model.mediadetails.VideosResults;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Collections;
import java.util.List;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class MovieInfo extends BasicMovieInfo implements Parcelable {
    @JsonProperty("belongs_to_collection")
    private CollectionInfo belongsToCollection;
    @JsonProperty("budget")
    private long budget;
    @JsonProperty("genres")
    private List<Genre> genresList = Collections.EMPTY_LIST;
    @JsonProperty("homepage")
    private String homepage;
    @JsonProperty("imdb_id")
    private String imdbId;
    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies = Collections.EMPTY_LIST;
    @JsonProperty("production_countries")
    private List<ProductionCountry> productionCountries = Collections.EMPTY_LIST;
    @JsonProperty("revenue")
    private Long revenue;
    @JsonProperty("runtime")
    private int runtime;
    @JsonProperty("spoken_languages")
    private List<Language> spokenLanguage = Collections.EMPTY_LIST;
    @JsonProperty("tagline")
    private String tagline;
    @JsonProperty("status")
    private String status;

    private VideosResults videos;
    private MediaCreditList credits;

    public MovieInfo() {
    }

    protected MovieInfo(Parcel in) {
        super(in);
        belongsToCollection = in.readParcelable(CollectionInfo.class.getClassLoader());
        budget = in.readLong();
        genresList = in.createTypedArrayList(Genre.CREATOR);
        homepage = in.readString();
        imdbId = in.readString();
        productionCompanies = in.createTypedArrayList(ProductionCompany.CREATOR);
        productionCountries = in.createTypedArrayList(ProductionCountry.CREATOR);
        revenue = in.readLong();
        runtime = in.readInt();
        spokenLanguage = in.createTypedArrayList(Language.CREATOR);
        tagline = in.readString();
        status = in.readString();
        videos = in.readParcelable(VideosResults.class.getClassLoader());
        credits = in.readParcelable(MediaCreditList.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(belongsToCollection, flags);
        dest.writeLong(budget);
        dest.writeTypedList(genresList);
        dest.writeString(homepage);
        dest.writeString(imdbId);
        dest.writeTypedList(productionCompanies);
        dest.writeTypedList(productionCountries);
        dest.writeLong(revenue);
        dest.writeInt(runtime);
        dest.writeTypedList(spokenLanguage);
        dest.writeString(tagline);
        dest.writeString(status);
        dest.writeParcelable(videos, flags);
        dest.writeParcelable(credits, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    public CollectionInfo getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(CollectionInfo belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public MediaCreditList getCredits() {
        return credits;
    }

    @JsonSetter("credits")
    public void setCredits(MediaCreditList credits) {
        this.credits = credits;
    }

    public List<Genre> getGenresList() {
        return genresList;
    }

    public void setGenresList(List<Genre> genresList) {
        this.genresList = genresList;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public List<Language> getSpokenLanguage() {
        return spokenLanguage;
    }

    public void setSpokenLanguage(List<Language> spokenLanguage) {
        this.spokenLanguage = spokenLanguage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public VideosResults getVideos() {
        return videos;
    }

    @JsonSetter("videos")
    public void setVideos(VideosResults videos) {
        this.videos = videos;
    }
}
