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
        "cast",
        "crew",
        "id"
})
public class PeopleMovieCredits implements Parcelable {

    public final static Creator<PeopleMovieCredits> CREATOR = new Creator<PeopleMovieCredits>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PeopleMovieCredits createFromParcel(Parcel in) {
            return new PeopleMovieCredits(in);
        }

        public PeopleMovieCredits[] newArray(int size) {
            return (new PeopleMovieCredits[size]);
        }

    };
    @JsonProperty("cast")
    private List<Cast> cast = new ArrayList<Cast>();
    @JsonProperty("crew")
    private List<Crew> crew = new ArrayList<Crew>();
    @JsonProperty("id")
    private int id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    protected PeopleMovieCredits(Parcel in) {
        in.readList(this.cast, (Object.class.getClassLoader()));
        in.readList(this.crew, (com.androidtmdbwrapper.model.people.Crew.class.getClassLoader()));
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.additionalProperties = ((Map<String, Object>) in.readValue((Map.class.getClassLoader())));
    }

    public PeopleMovieCredits() {
    }

    @JsonProperty("cast")
    public List<Cast> getCast() {
        return cast;
    }

    @JsonProperty("cast")
    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public PeopleMovieCredits withCast(List<Cast> cast) {
        this.cast = cast;
        return this;
    }

    @JsonProperty("crew")
    public List<Crew> getCrew() {
        return crew;
    }

    @JsonProperty("crew")
    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

    public PeopleMovieCredits withCrew(List<Crew> crew) {
        this.crew = crew;
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

    public PeopleMovieCredits withId(int id) {
        this.id = id;
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

    public PeopleMovieCredits withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(cast);
        dest.writeList(crew);
        dest.writeValue(id);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}