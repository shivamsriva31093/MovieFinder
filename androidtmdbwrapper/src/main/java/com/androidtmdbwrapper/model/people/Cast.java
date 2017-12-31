package com.androidtmdbwrapper.model.people;

import android.os.Parcel;
import android.os.Parcelable;

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
        "character",
        "credit_id",
        "release_date",
        "vote_count",
        "video",
        "adult",
        "vote_average",
        "title",
        "genre_ids",
        "original_language",
        "original_title",
        "popularity",
        "id",
        "backdrop_path",
        "overview",
        "poster_path"
})
public class Cast implements Parcelable {

    public final static Creator<Cast> CREATOR = new Creator<Cast>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        public Cast[] newArray(int size) {
            return (new Cast[size]);
        }

    };
    @JsonProperty("character")
    private String character;
    @JsonProperty("credit_id")
    private String creditId;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("vote_count")
    private int voteCount;
    @JsonProperty("video")
    private boolean video;
    @JsonProperty("adult")
    private boolean adult;
    @JsonProperty("vote_average")
    private int voteAverage;
    @JsonProperty("title")
    private String title;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("original_title")
    private String originalTitle;
    @JsonProperty("popularity")
    private int popularity;
    @JsonProperty("id")
    private int id;
    @JsonProperty("backdrop_path")
    private Object backdropPath;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    protected Cast(Parcel in) {
        this.character = ((String) in.readValue((String.class.getClassLoader())));
        this.creditId = ((String) in.readValue((String.class.getClassLoader())));
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.voteCount = ((int) in.readValue((int.class.getClassLoader())));
        this.video = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.adult = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.voteAverage = ((int) in.readValue((int.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.genreIds, (Integer.class.getClassLoader()));
        this.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
        this.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.popularity = ((int) in.readValue((int.class.getClassLoader())));
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.backdropPath = ((Object) in.readValue((Object.class.getClassLoader())));
        this.overview = ((String) in.readValue((String.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        this.additionalProperties = ((Map<String, Object>) in.readValue((Map.class.getClassLoader())));
    }

    public Cast() {
    }

    @JsonProperty("character")
    public String getCharacter() {
        return character;
    }

    @JsonProperty("character")
    public void setCharacter(String character) {
        this.character = character;
    }

    public Cast withCharacter(String character) {
        this.character = character;
        return this;
    }

    @JsonProperty("credit_id")
    public String getCreditId() {
        return creditId;
    }

    @JsonProperty("credit_id")
    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public Cast withCreditId(String creditId) {
        this.creditId = creditId;
        return this;
    }

    @JsonProperty("release_date")
    public String getReleaseDate() {
        return releaseDate;
    }

    @JsonProperty("release_date")
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Cast withReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    @JsonProperty("vote_count")
    public int getVoteCount() {
        return voteCount;
    }

    @JsonProperty("vote_count")
    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Cast withVoteCount(int voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    @JsonProperty("video")
    public boolean isVideo() {
        return video;
    }

    @JsonProperty("video")
    public void setVideo(boolean video) {
        this.video = video;
    }

    public Cast withVideo(boolean video) {
        this.video = video;
        return this;
    }

    @JsonProperty("adult")
    public boolean isAdult() {
        return adult;
    }

    @JsonProperty("adult")
    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public Cast withAdult(boolean adult) {
        this.adult = adult;
        return this;
    }

    @JsonProperty("vote_average")
    public int getVoteAverage() {
        return voteAverage;
    }

    @JsonProperty("vote_average")
    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Cast withVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Cast withTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("genre_ids")
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    @JsonProperty("genre_ids")
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Cast withGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
        return this;
    }

    @JsonProperty("original_language")
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    @JsonProperty("original_language")
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public Cast withOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    @JsonProperty("original_title")
    public String getOriginalTitle() {
        return originalTitle;
    }

    @JsonProperty("original_title")
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Cast withOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    @JsonProperty("popularity")
    public int getPopularity() {
        return popularity;
    }

    @JsonProperty("popularity")
    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public Cast withPopularity(int popularity) {
        this.popularity = popularity;
        return this;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    public Cast withId(int id) {
        this.id = id;
        return this;
    }

    @JsonProperty("backdrop_path")
    public Object getBackdropPath() {
        return backdropPath;
    }

    @JsonProperty("backdrop_path")
    public void setBackdropPath(Object backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Cast withBackdropPath(Object backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    @JsonProperty("overview")
    public String getOverview() {
        return overview;
    }

    @JsonProperty("overview")
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Cast withOverview(String overview) {
        this.overview = overview;
        return this;
    }

    @JsonProperty("poster_path")
    public String getPosterPath() {
        return posterPath;
    }

    @JsonProperty("poster_path")
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Cast withPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Cast withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(character);
        dest.writeValue(creditId);
        dest.writeValue(releaseDate);
        dest.writeValue(voteCount);
        dest.writeValue(video);
        dest.writeValue(adult);
        dest.writeValue(voteAverage);
        dest.writeValue(title);
        dest.writeList(genreIds);
        dest.writeValue(originalLanguage);
        dest.writeValue(originalTitle);
        dest.writeValue(popularity);
        dest.writeValue(id);
        dest.writeValue(backdropPath);
        dest.writeValue(overview);
        dest.writeValue(posterPath);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}