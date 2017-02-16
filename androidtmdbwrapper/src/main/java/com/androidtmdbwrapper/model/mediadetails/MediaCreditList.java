package com.androidtmdbwrapper.model.mediadetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.credits.MediaCreditCast;
import com.androidtmdbwrapper.model.credits.MediaCreditCrew;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * Created by sHIVAM on 2/15/2017.
 */

public class MediaCreditList implements Parcelable {
    @JsonProperty("cast")
    private List<MediaCreditCast> cast = Collections.EMPTY_LIST;
    @JsonProperty("crew")
    private List<MediaCreditCrew> crew = Collections.EMPTY_LIST;

    public MediaCreditList() {
    }

    protected MediaCreditList(Parcel in) {
        cast = in.createTypedArrayList(MediaCreditCast.CREATOR);
        crew = in.createTypedArrayList(MediaCreditCrew.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(cast);
        dest.writeTypedList(crew);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaCreditList> CREATOR = new Creator<MediaCreditList>() {
        @Override
        public MediaCreditList createFromParcel(Parcel in) {
            return new MediaCreditList(in);
        }

        @Override
        public MediaCreditList[] newArray(int size) {
            return new MediaCreditList[size];
        }
    };

    public List<MediaCreditCrew> getCrew() {
        return crew;
    }

    public void setCrew(List<MediaCreditCrew> crew) {
        this.crew = crew;
    }

    public List<MediaCreditCast> getCast() {
        return cast;
    }

    public void setCast(List<MediaCreditCast> cast) {
        this.cast = cast;
    }
}
