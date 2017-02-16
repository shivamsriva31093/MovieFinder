package com.androidtmdbwrapper.model.credits;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class MediaCreditCast extends MediaCredit implements Parcelable {
    @JsonProperty("cast_id")
    private int castId = 0;
    @JsonProperty("character")
    private String character;
    @JsonProperty("order")
    private String order;

    public MediaCreditCast() {
    }


    protected MediaCreditCast(Parcel in) {
        super(in);
        castId = in.readInt();
        character = in.readString();
        order = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(castId);
        dest.writeString(character);
        dest.writeString(order);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaCreditCast> CREATOR = new Creator<MediaCreditCast>() {
        @Override
        public MediaCreditCast createFromParcel(Parcel in) {
            return new MediaCreditCast(in);
        }

        @Override
        public MediaCreditCast[] newArray(int size) {
            return new MediaCreditCast[size];
        }
    };

    public int getCastId() {
        return castId;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
