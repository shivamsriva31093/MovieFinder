package com.androidtmdbwrapper.model.tv;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.core.Genre;
import com.androidtmdbwrapper.model.mediadetails.MediaCreditList;
import com.androidtmdbwrapper.model.mediadetails.VideosResults;
import com.androidtmdbwrapper.model.movies.ProductionCompany;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collections;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class TvInfo extends BasicTVInfo implements Parcelable {

    @JsonProperty("created_by")
    private List<CreatedBy> createdBy = Collections.EMPTY_LIST;
    @JsonProperty("episode_run_time")
    private List<Integer> episodeRunTime = Collections.EMPTY_LIST;
    @JsonProperty("genres")
    private List<Genre> genres = Collections.EMPTY_LIST;
    @JsonProperty("homepage")
    private String homepage;
    @JsonProperty("in_production")
    private boolean inProduction;
    @JsonProperty("languages")
    private List<String> languages = Collections.EMPTY_LIST;
    @JsonProperty("last_air_date")
    private String lastAirDate;
    @JsonProperty("networks")
    private List<Network> networks = Collections.EMPTY_LIST;
    @JsonProperty("number_of_episodes")
    private int numberOfEpisodes;
    @JsonProperty("number_of_seasons")
    private int numberOfSeasons;
    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies = Collections.EMPTY_LIST;
    @JsonProperty("seasons")
    private List<Season> seasons = Collections.EMPTY_LIST;
    @JsonProperty("status")
    private String status;
    @JsonProperty("type")
    private String type;

    private VideosResults videos;
    private MediaCreditList credits;

    public TvInfo() {
    }


    protected TvInfo(Parcel in) {
        super(in);
        createdBy = in.createTypedArrayList(CreatedBy.CREATOR);
        genres = in.createTypedArrayList(Genre.CREATOR);
        homepage = in.readString();
        inProduction = in.readByte() != 0;
        languages = in.createStringArrayList();
        lastAirDate = in.readString();
        networks = in.createTypedArrayList(Network.CREATOR);
        numberOfEpisodes = in.readInt();
        numberOfSeasons = in.readInt();
        productionCompanies = in.createTypedArrayList(ProductionCompany.CREATOR);
        seasons = in.createTypedArrayList(Season.CREATOR);
        status = in.readString();
        type = in.readString();
        videos = in.readParcelable(VideosResults.class.getClassLoader());
        credits = in.readParcelable(MediaCreditList.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(createdBy);
        dest.writeTypedList(genres);
        dest.writeString(homepage);
        dest.writeByte((byte) (inProduction ? 1 : 0));
        dest.writeStringList(languages);
        dest.writeString(lastAirDate);
        dest.writeTypedList(networks);
        dest.writeInt(numberOfEpisodes);
        dest.writeInt(numberOfSeasons);
        dest.writeTypedList(productionCompanies);
        dest.writeTypedList(seasons);
        dest.writeString(status);
        dest.writeString(type);
        dest.writeParcelable(videos, flags);
        dest.writeParcelable(credits, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TvInfo> CREATOR = new Creator<TvInfo>() {
        @Override
        public TvInfo createFromParcel(Parcel in) {
            return new TvInfo(in);
        }

        @Override
        public TvInfo[] newArray(int size) {
            return new TvInfo[size];
        }
    };

    @JsonProperty("created_by")
    public List<CreatedBy> getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("created_by")
    public void setCreatedBy(List<CreatedBy> createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("episode_run_time")
    public List<Integer> getEpisodeRunTime() {
        return episodeRunTime;
    }

    @JsonProperty("episode_run_time")
    public void setEpisodeRunTime(List<Integer> episodeRunTime) {
        this.episodeRunTime = episodeRunTime;
    }

    @JsonProperty("genres")
    public List<Genre> getGenres() {
        return genres;
    }

    @JsonProperty("genres")
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @JsonProperty("homepage")
    public String getHomepage() {
        return homepage;
    }

    @JsonProperty("homepage")
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    @JsonProperty("in_production")
    public boolean isInProduction() {
        return inProduction;
    }

    @JsonProperty("in_production")
    public void setInProduction(boolean inProduction) {
        this.inProduction = inProduction;
    }

    @JsonProperty("languages")
    public List<String> getLanguages() {
        return languages;
    }

    @JsonProperty("languages")
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    @JsonProperty("last_air_date")
    public String getLastAirDate() {
        return lastAirDate;
    }

    @JsonProperty("last_air_date")
    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    @JsonProperty("networks")
    public List<Network> getNetworks() {
        return networks;
    }

    @JsonProperty("networks")
    public void setNetworks(List<Network> networks) {
        this.networks = networks;
    }

    @JsonProperty("number_of_episodes")
    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    @JsonProperty("number_of_episodes")
    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    @JsonProperty("number_of_seasons")
    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    @JsonProperty("number_of_seasons")
    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    @JsonProperty("origin_country")
    public List<String> getOriginCountry() {
        return originCountry;
    }

    @JsonProperty("origin_country")
    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    @JsonProperty("production_companies")
    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    @JsonProperty("production_companies")
    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    @JsonProperty("seasons")
    public List<Season> getSeasons() {
        return seasons;
    }

    @JsonProperty("seasons")
    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }
    public VideosResults getVideos() {
        return videos;
    }

    public void setVideos(VideosResults videos) {
        this.videos = videos;
    }

    public MediaCreditList getCredits() {
        return credits;
    }

    public void setCredits(MediaCreditList credits) {
        this.credits = credits;
    }
}