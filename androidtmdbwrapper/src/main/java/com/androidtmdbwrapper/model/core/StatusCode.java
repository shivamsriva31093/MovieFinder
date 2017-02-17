package com.androidtmdbwrapper.model.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class StatusCode implements Parcelable {
    @JsonProperty("status_code")
    private int statusCode;
    @JsonProperty("status_message")
    private String statusMessage;

    public StatusCode() {
    }

    protected StatusCode(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
    }

    public static final Creator<StatusCode> CREATOR = new Creator<StatusCode>() {
        @Override
        public StatusCode createFromParcel(Parcel in) {
            return new StatusCode(in);
        }

        @Override
        public StatusCode[] newArray(int size) {
            return new StatusCode[size];
        }
    };

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statusCode);
        dest.writeString(statusMessage);
    }
}
