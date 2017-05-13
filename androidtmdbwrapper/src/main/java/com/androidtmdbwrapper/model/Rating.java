package com.androidtmdbwrapper.model;

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
@JsonPropertyOrder({
        "Source",
        "Value"
})
public class Rating implements Parcelable {

    @JsonProperty("Source")
    private String source;
    @JsonProperty("Value")
    private String value;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    public final static Parcelable.Creator<Rating> CREATOR = new Creator<Rating>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Rating createFromParcel(Parcel in) {
            Rating instance = new Rating();
            instance.source = ((String) in.readValue((String.class.getClassLoader())));
            instance.value = ((String) in.readValue((String.class.getClassLoader())));
            instance.additionalProperties = ((Map<String, Object>) in.readValue((Map.class.getClassLoader())));
            return instance;
        }

        public Rating[] newArray(int size) {
            return (new Rating[size]);
        }

    };

    /**
     * No args constructor for use in serialization
     */
    public Rating() {
    }

    /**
     * @param source
     * @param value
     */
    public Rating(String source, String value) {
        super();
        this.source = source;
        this.value = value;
    }

    @JsonProperty("Source")
    public String getSource() {
        return source;
    }

    @JsonProperty("Source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("Value")
    public String getValue() {
        return value;
    }

    @JsonProperty("Value")
    public void setValue(String value) {
        this.value = value;
    }


    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(source);
        dest.writeValue(value);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}