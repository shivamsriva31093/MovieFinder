package com.androidtmdbwrapper.model.tv;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.movies.ProductionCompany;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "created_by",
        "episode_run_time",
        "homepage",
        "in_production",
        "languages",
        "last_air_date",
        "name",
        "networks",
        "number_of_episodes",
        "number_of_seasons",
        "origin_country",
        "original_language",
        "production_companies",
        "seasons",
        "status",
        "type"
})
public class TvInfo implements Parcelable {

    @JsonProperty("created_by")
    private List<CreatedBy> createdBy = Collections.EMPTY_LIST;
    @JsonProperty("episode_run_time")
    private List<Integer> episodeRunTime = new ArrayList<Integer>();
    @JsonProperty("homepage")
    private String homepage;
    @JsonProperty("in_production")
    private boolean inProduction;
    @JsonProperty("languages")
    private List<String> languages = new ArrayList<String>();
    @JsonProperty("last_air_date")
    private String lastAirDate;
    @JsonProperty("name")
    private String name;
    @JsonProperty("networks")
    private List<Network> networks = new ArrayList<Network>();
    @JsonProperty("number_of_episodes")
    private int numberOfEpisodes;
    @JsonProperty("number_of_seasons")
    private int numberOfSeasons;
    @JsonProperty("origin_country")
    private List<String> originCountry = new ArrayList<String>();
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies = new ArrayList<ProductionCompany>();
    @JsonProperty("seasons")
    private List<Season> seasons = new ArrayList<Season>();
    @JsonProperty("status")
    private String status;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public TvInfo() {
    }

    /**
     * @param networks
     * @param status
     * @param lastAirDate
     * @param numberOfSeasons
     * @param originalLanguage
     * @param type
     * @param homepage
     * @param numberOfEpisodes
     * @param languages
     * @param originCountry
     * @param createdBy
     * @param inProduction
     * @param seasons
     * @param name
     * @param productionCompanies
     * @param episodeRunTime
     */
    public TvInfo(List<CreatedBy> createdBy, List<Integer> episodeRunTime, String homepage, boolean inProduction, List<String> languages, String lastAirDate, String name, List<Network> networks, int numberOfEpisodes, int numberOfSeasons, List<String> originCountry, String originalLanguage, List<ProductionCompany> productionCompanies, List<Season> seasons, String status, String type) {
        super();
        this.createdBy = createdBy;
        this.episodeRunTime = episodeRunTime;
        this.homepage = homepage;
        this.inProduction = inProduction;
        this.languages = languages;
        this.lastAirDate = lastAirDate;
        this.name = name;
        this.networks = networks;
        this.numberOfEpisodes = numberOfEpisodes;
        this.numberOfSeasons = numberOfSeasons;
        this.originCountry = originCountry;
        this.originalLanguage = originalLanguage;
        this.productionCompanies = productionCompanies;
        this.seasons = seasons;
        this.status = status;
        this.type = type;
    }

    protected TvInfo(Parcel in) {
        createdBy = in.createTypedArrayList(CreatedBy.CREATOR);
        homepage = in.readString();
        inProduction = in.readByte() != 0;
        languages = in.createStringArrayList();
        lastAirDate = in.readString();
        name = in.readString();
        networks = in.createTypedArrayList(Network.CREATOR);
        numberOfEpisodes = in.readInt();
        numberOfSeasons = in.readInt();
        originCountry = in.createStringArrayList();
        originalLanguage = in.readString();
        productionCompanies = in.createTypedArrayList(ProductionCompany.CREATOR);
        seasons = in.createTypedArrayList(Season.CREATOR);
        status = in.readString();
        type = in.readString();
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

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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

    @JsonProperty("original_language")
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    @JsonProperty("original_language")
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
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
        parcel.writeTypedList(createdBy);
        parcel.writeString(homepage);
        parcel.writeByte((byte) (inProduction ? 1 : 0));
        parcel.writeStringList(languages);
        parcel.writeString(lastAirDate);
        parcel.writeString(name);
        parcel.writeTypedList(networks);
        parcel.writeInt(numberOfEpisodes);
        parcel.writeInt(numberOfSeasons);
        parcel.writeStringList(originCountry);
        parcel.writeString(originalLanguage);
        parcel.writeTypedList(productionCompanies);
        parcel.writeTypedList(seasons);
        parcel.writeString(status);
        parcel.writeString(type);
    }
}