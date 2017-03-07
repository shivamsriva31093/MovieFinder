package com.androidtmdbwrapper.model.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class ApiConfiguration implements Parcelable {
    @JsonProperty("images")
    private ImageConfig imageConfig;
    @JsonProperty("change_keys")
    private List<String> changeKeys;

    public ApiConfiguration() {

    }

    protected ApiConfiguration(Parcel in) {
        imageConfig = in.readParcelable(ImageConfig.class.getClassLoader());
        changeKeys = in.createStringArrayList();
    }

    public static final Creator<ApiConfiguration> CREATOR = new Creator<ApiConfiguration>() {
        @Override
        public ApiConfiguration createFromParcel(Parcel in) {
            return new ApiConfiguration(in);
        }

        @Override
        public ApiConfiguration[] newArray(int size) {
            return new ApiConfiguration[size];
        }
    };

    public ImageConfig getImageConfig() {
        return imageConfig;
    }

    public void setImageConfig(ImageConfig imageConfig) {
        this.imageConfig = imageConfig;
    }

    public List<String> getChangeKeys() {
        return changeKeys;
    }

    public void setChangeKeys(List<String> changeKeys) {
        this.changeKeys = changeKeys;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(imageConfig, flags);
        dest.writeStringList(changeKeys);
    }

    @Override
    public String toString() {
        return "ApiConfiguration{" +
                "changeKeys=" + changeKeys +
                ", imageConfig=" + imageConfig +
                '}';
    }

    public String getImageDownloadURL(String fileUrl, String size) {
        return imageConfig.getImageUrl(fileUrl, size);
    }
}
