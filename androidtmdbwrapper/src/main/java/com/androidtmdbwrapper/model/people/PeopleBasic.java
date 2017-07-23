package com.androidtmdbwrapper.model.people;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.core.BaseMediaData;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by sHIVAM on 7/24/2017.
 */

public class PeopleBasic extends BaseMediaData implements Parcelable {
    @JsonProperty("profile_path")
    private String profilePath;
    @JsonProperty("adult")
    private boolean isAdult;
    @JsonProperty("known_for")
    private List<MediaBasic> knownFor;
    @JsonProperty("name")
    private String name;

    public PeopleBasic() {
    }

    protected PeopleBasic(Parcel in) {
        super(in);
        profilePath = in.readString();
        isAdult = in.readByte() != 0;
        knownFor = in.createTypedArrayList(MediaBasic.CREATOR);
        name = in.readString();
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public List<MediaBasic> getKnownFor() {
        return knownFor;
    }

    public void setKnownFor(List<MediaBasic> knownFor) {
        this.knownFor = knownFor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(profilePath);
        dest.writeByte((byte) (isAdult ? 1 : 0));
        dest.writeTypedList(knownFor);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PeopleBasic> CREATOR = new Creator<PeopleBasic>() {
        @Override
        public PeopleBasic createFromParcel(Parcel in) {
            return new PeopleBasic(in);
        }

        @Override
        public PeopleBasic[] newArray(int size) {
            return new PeopleBasic[size];
        }
    };
}
