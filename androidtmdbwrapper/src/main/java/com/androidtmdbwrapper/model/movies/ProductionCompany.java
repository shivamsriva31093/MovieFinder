package com.androidtmdbwrapper.model.movies;

import android.os.Parcel;

import com.androidtmdbwrapper.model.core.AbstractNameId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class ProductionCompany extends AbstractNameId {



    public ProductionCompany() {
    }

    protected ProductionCompany(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductionCompany> CREATOR = new Creator<ProductionCompany>() {
        @Override
        public ProductionCompany createFromParcel(Parcel in) {
            return new ProductionCompany(in);
        }

        @Override
        public ProductionCompany[] newArray(int size) {
            return new ProductionCompany[size];
        }
    };
}
