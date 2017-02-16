package com.androidtmdbwrapper.model.credits;

import android.os.Parcel;

import com.androidtmdbwrapper.model.core.AbstractNameId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class MediaCredit extends AbstractNameId {
    @JsonProperty("credit_id")
    private String creditId;
    @JsonProperty("profile_path")
    private String profilePath;

    public MediaCredit() {
    }

    public MediaCredit(Parcel in) {
        super(in);
        creditId = in.readString();
        profilePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(creditId);
        dest.writeString(profilePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaCredit> CREATOR = new Creator<MediaCredit>() {
        @Override
        public MediaCredit createFromParcel(Parcel in) {
            return new MediaCredit(in);
        }

        @Override
        public MediaCredit[] newArray(int size) {
            return new MediaCredit[size];
        }
    };

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
