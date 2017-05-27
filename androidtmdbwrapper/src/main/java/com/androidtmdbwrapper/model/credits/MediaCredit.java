package com.androidtmdbwrapper.model.credits;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.core.AbstractNameId;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sHIVAM on 2/14/2017.
 */
@JsonIgnoreProperties({
        "gender"
})
public class MediaCredit extends AbstractNameId implements Parcelable {
    @JsonProperty("credit_id")
    private String creditId;
    @JsonProperty("profile_path")
    private String profilePath;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public MediaCredit() {
    }

    protected MediaCredit(Parcel in) {
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
