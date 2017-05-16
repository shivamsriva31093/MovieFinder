package com.androidtmdbwrapper.model.tv;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.core.AbstractNameId;
import com.fasterxml.jackson.annotation.JsonProperty;


public class CreatedBy extends AbstractNameId implements Parcelable {

    @JsonProperty("profile_path")
    private String profilePath;

    /**
     * No args constructor for use in serialization
     */
    public CreatedBy() {
    }


    protected CreatedBy(Parcel in) {
        super(in);
        profilePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(profilePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CreatedBy> CREATOR = new Creator<CreatedBy>() {
        @Override
        public CreatedBy createFromParcel(Parcel in) {
            return new CreatedBy(in);
        }

        @Override
        public CreatedBy[] newArray(int size) {
            return new CreatedBy[size];
        }
    };

    @JsonProperty("profile_path")
    public String getProfilePath() {
        return profilePath;
    }

    @JsonProperty("profile_path")
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }


}
