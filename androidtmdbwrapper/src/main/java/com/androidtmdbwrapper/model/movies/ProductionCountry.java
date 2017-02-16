package com.androidtmdbwrapper.model.movies;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class ProductionCountry implements Parcelable {
    @JsonProperty("iso_3166_1")
    private String country;
    @JsonProperty("name")
    private String name;

    public ProductionCountry() {
    }

    protected ProductionCountry(Parcel in) {
        country = in.readString();
        name = in.readString();
    }

    public static final Creator<ProductionCountry> CREATOR = new Creator<ProductionCountry>() {
        @Override
        public ProductionCountry createFromParcel(Parcel in) {
            return new ProductionCountry(in);
        }

        @Override
        public ProductionCountry[] newArray(int size) {
            return new ProductionCountry[size];
        }
    };

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ProductionCountry) {
            final ProductionCountry other = (ProductionCountry) o;
            return this.country.equals(other.country) && this.name.equals(other.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country);
        dest.writeString(name);
    }
}
