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
        "adult",
        "also_known_as",
        "biography",
        "birthday",
        "deathday",
        "gender",
        "homepage",
        "id",
        "imdb_id",
        "name",
        "place_of_birth",
        "popularity",
        "profile_path"
})
public class PeopleDetails implements Parcelable {

    public final static Creator<PeopleDetails> CREATOR = new Creator<PeopleDetails>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PeopleDetails createFromParcel(Parcel in) {
            return new PeopleDetails(in);
        }

        public PeopleDetails[] newArray(int size) {
            return (new PeopleDetails[size]);
        }

    };
    @JsonProperty("adult")
    private boolean adult;
    @JsonProperty("also_known_as")
    private List<Object> alsoKnownAs = new ArrayList<Object>();
    @JsonProperty("biography")
    private String biography;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("deathday")
    private String deathday;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("homepage")
    private String homepage;
    @JsonProperty("id")
    private int id;
    @JsonProperty("imdb_id")
    private String imdbId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("place_of_birth")
    private String placeOfBirth;
    @JsonProperty("popularity")
    private double popularity;
    @JsonProperty("profile_path")
    private String profilePath;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    protected PeopleDetails(Parcel in) {
        this.adult = ((boolean) in.readValue((boolean.class.getClassLoader())));
        in.readList(this.alsoKnownAs, (Object.class.getClassLoader()));
        this.biography = ((String) in.readValue((String.class.getClassLoader())));
        this.birthday = ((String) in.readValue((String.class.getClassLoader())));
        this.deathday = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((int) in.readValue((int.class.getClassLoader())));
        this.homepage = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.imdbId = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.placeOfBirth = ((String) in.readValue((String.class.getClassLoader())));
        this.popularity = ((double) in.readValue((double.class.getClassLoader())));
        this.profilePath = ((String) in.readValue((String.class.getClassLoader())));
        this.additionalProperties = ((Map<String, Object>) in.readValue((Map.class.getClassLoader())));
    }

    public PeopleDetails() {
    }

    @JsonProperty("adult")
    public boolean isAdult() {
        return adult;
    }

    @JsonProperty("adult")
    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public PeopleDetails withAdult(boolean adult) {
        this.adult = adult;
        return this;
    }

    @JsonProperty("also_known_as")
    public List<Object> getAlsoKnownAs() {
        return alsoKnownAs;
    }

    @JsonProperty("also_known_as")
    public void setAlsoKnownAs(List<Object> alsoKnownAs) {
        this.alsoKnownAs = alsoKnownAs;
    }

    public PeopleDetails withAlsoKnownAs(List<Object> alsoKnownAs) {
        this.alsoKnownAs = alsoKnownAs;
        return this;
    }

    @JsonProperty("biography")
    public String getBiography() {
        return biography;
    }

    @JsonProperty("biography")
    public void setBiography(String biography) {
        this.biography = biography;
    }

    public PeopleDetails withBiography(String biography) {
        this.biography = biography;
        return this;
    }

    @JsonProperty("birthday")
    public String getBirthday() {
        return birthday;
    }

    @JsonProperty("birthday")
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public PeopleDetails withBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    @JsonProperty("deathday")
    public String getDeathday() {
        return deathday;
    }

    @JsonProperty("deathday")
    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public PeopleDetails withDeathday(String deathday) {
        this.deathday = deathday;
        return this;
    }

    @JsonProperty("gender")
    public int getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(int gender) {
        this.gender = gender;
    }

    public PeopleDetails withGender(int gender) {
        this.gender = gender;
        return this;
    }

    @JsonProperty("homepage")
    public String getHomepage() {
        return homepage;
    }

    @JsonProperty("homepage")
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public PeopleDetails withHomepage(String homepage) {
        this.homepage = homepage;
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

    public PeopleDetails withId(int id) {
        this.id = id;
        return this;
    }

    @JsonProperty("imdb_id")
    public String getImdbId() {
        return imdbId;
    }

    @JsonProperty("imdb_id")
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public PeopleDetails withImdbId(String imdbId) {
        this.imdbId = imdbId;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public PeopleDetails withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("place_of_birth")
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    @JsonProperty("place_of_birth")
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public PeopleDetails withPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
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

    public PeopleDetails withPopularity(double popularity) {
        this.popularity = popularity;
        return this;
    }

    @JsonProperty("profile_path")
    public String getProfilePath() {
        return profilePath;
    }

    @JsonProperty("profile_path")
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public PeopleDetails withProfilePath(String profilePath) {
        this.profilePath = profilePath;
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

    public PeopleDetails withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(adult);
        dest.writeList(alsoKnownAs);
        dest.writeValue(biography);
        dest.writeValue(birthday);
        dest.writeValue(deathday);
        dest.writeValue(gender);
        dest.writeValue(homepage);
        dest.writeValue(id);
        dest.writeValue(imdbId);
        dest.writeValue(name);
        dest.writeValue(placeOfBirth);
        dest.writeValue(popularity);
        dest.writeValue(profilePath);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}
