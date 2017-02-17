package com.androidtmdbwrapper.model.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by sHIVAM on 2/14/2017.
 */

@JsonRootName("spoken_languages")
public class Language implements Parcelable {
    @JsonProperty("iso_639_1")
    private String code;
    @JsonProperty("name")
    private String name;

    public Language() {
    }

    protected Language(Parcel in) {
        code = in.readString();
        name = in.readString();
    }

    public static final Creator<Language> CREATOR = new Creator<Language>() {
        @Override
        public Language createFromParcel(Parcel in) {
            return new Language(in);
        }

        @Override
        public Language[] newArray(int size) {
            return new Language[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Language)) return false;

        Language language = (Language) o;

        if (!getCode().equals(language.getCode())) return false;
        return getName().equals(language.getName());

    }

    @Override
    public int hashCode() {
        int result = getCode().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
    }
}
