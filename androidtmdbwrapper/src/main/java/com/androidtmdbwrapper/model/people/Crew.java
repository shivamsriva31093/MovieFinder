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
        "id",
        "department",
        "original_language",
        "original_title",
        "job",
        "overview",
        "vote_count",
        "video",
        "poster_path",
        "backdrop_path",
        "title",
        "popularity",
        "genre_ids",
        "vote_average",
        "adult",
        "release_date",
        "credit_id"
})
public class Crew implements Parcelable {

    public final static Creator<Crew> CREATOR = new Creator<Crew>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Crew createFromParcel(Parcel in) {
            return new Crew(in);
        }

        public Crew[] newArray(int size) {
            return (new Crew[size]);
        }

    };
    @JsonProperty("id")
    private int id;
    @JsonProperty("department")
    private String department;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("original_title")
    private String originalTitle;
    @JsonProperty("job")
    private String job;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("vote_count")
    private int voteCount;
    @JsonProperty("video")
    private boolean video;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("title")
    private String title;
    @JsonProperty("popularity")
    private double popularity;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();
    @JsonProperty("vote_average")
    private double voteAverage;
    @JsonProperty("adult")
    private boolean adult;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("credit_id")
    private String creditId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    protected Crew(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.department = ((String) in.readValue((String.class.getClassLoader())));
        this.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
        this.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.job = ((String) in.readValue((String.class.getClassLoader())));
        this.overview = ((String) in.readValue((String.class.getClassLoader())));
        this.voteCount = ((int) in.readValue((int.class.getClassLoader())));
        this.video = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        this.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.popularity = ((double) in.readValue((double.class.getClassLoader())));
        in.readList(this.genreIds, (Integer.class.getClassLoader()));
        this.voteAverage = ((double) in.readValue((double.class.getClassLoader())));
        this.adult = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.creditId = ((String) in.readValue((String.class.getClassLoader())));
        this.additionalProperties = ((Map<String, Object>) in.readValue((Map.class.getClassLoader())));
    }

    public Crew() {
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    public Crew withId(int id) {
        this.id = id;
        return this;
    }

    @JsonProperty("department")
    public String getDepartment() {
        return department;
    }

    @JsonProperty("department")
    public void setDepartment(String department) {
        this.department = department;
    }

    public Crew withDepartment(String department) {
        this.department = department;
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

    public Crew withOriginalLanguage(String originalLanguage) {
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

    public Crew withOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    @JsonProperty("job")
    public void setJob(String job) {
        this.job = job;
    }

    public Crew withJob(String job) {
        this.job = job;
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

    public Crew withOverview(String overview) {
        this.overview = overview;
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

    public Crew withVoteCount(int voteCount) {
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

    public Crew withVideo(boolean video) {
        this.video = video;
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

    public Crew withPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    @JsonProperty("backdrop_path")
    public String getBackdropPath() {
        return backdropPath;
    }

    @JsonProperty("backdrop_path")
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Crew withBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
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

    public Crew withTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("popularity")
    public double getPopularity() {
        return popularity;
    }

    @JsonProperty("popularity")
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public Crew withPopularity(double popularity) {
        this.popularity = popularity;
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

    public Crew withGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
        return this;
    }

    @JsonProperty("vote_average")
    public double getVoteAverage() {
        return voteAverage;
    }

    @JsonProperty("vote_average")
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Crew withVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
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

    public Crew withAdult(boolean adult) {
        this.adult = adult;
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

    public Crew withReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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

    public Crew withCreditId(String creditId) {
        this.creditId = creditId;
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

    public Crew withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(department);
        dest.writeValue(originalLanguage);
        dest.writeValue(originalTitle);
        dest.writeValue(job);
        dest.writeValue(overview);
        dest.writeValue(voteCount);
        dest.writeValue(video);
        dest.writeValue(posterPath);
        dest.writeValue(backdropPath);
        dest.writeValue(title);
        dest.writeValue(popularity);
        dest.writeList(genreIds);
        dest.writeValue(voteAverage);
        dest.writeValue(adult);
        dest.writeValue(releaseDate);
        dest.writeValue(creditId);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}