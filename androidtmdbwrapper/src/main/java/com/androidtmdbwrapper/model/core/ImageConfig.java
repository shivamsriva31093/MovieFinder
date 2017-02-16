package com.androidtmdbwrapper.model.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class ImageConfig implements Parcelable {
    @JsonProperty("base_url")
    private String baseUrl;
    @JsonProperty("secure_base_url")
    private String secureBaseUrl;
    @JsonProperty("backdrop_sizes")
    private List<String> backdropSizes;
    @JsonProperty("logo_sizes")
    private List<String> logoSizes;
    @JsonProperty("poster_sizes")
    private List<String> posterSizes;
    @JsonProperty("profile_sizes")
    private List<String> profileSizes;
    @JsonProperty("still_sizes")
    private List<String> stillSizes;

    public ImageConfig() {
    }

    protected ImageConfig(Parcel in) {
        baseUrl = in.readString();
        secureBaseUrl = in.readString();
        backdropSizes = in.createStringArrayList();
        logoSizes = in.createStringArrayList();
        posterSizes = in.createStringArrayList();
        profileSizes = in.createStringArrayList();
        stillSizes = in.createStringArrayList();
    }

    public static final Creator<ImageConfig> CREATOR = new Creator<ImageConfig>() {
        @Override
        public ImageConfig createFromParcel(Parcel in) {
            return new ImageConfig(in);
        }

        @Override
        public ImageConfig[] newArray(int size) {
            return new ImageConfig[size];
        }
    };

    public List<String> getBackdropSizes() {
        return backdropSizes;
    }

    public void setBackdropSizes(List<String> backdropSizes) {
        this.backdropSizes = backdropSizes;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> getLogoSizes() {
        return logoSizes;
    }

    public void setLogoSizes(List<String> logoSizes) {
        this.logoSizes = logoSizes;
    }

    public List<String> getPosterSizes() {
        return posterSizes;
    }

    public void setPosterSizes(List<String> posterSizes) {
        this.posterSizes = posterSizes;
    }

    public List<String> getProfileSizes() {
        return profileSizes;
    }

    public void setProfileSizes(List<String> profileSizes) {
        this.profileSizes = profileSizes;
    }

    public String getSecureBaseUrl() {
        return secureBaseUrl;
    }

    public void setSecureBaseUrl(String secureBaseUrl) {
        this.secureBaseUrl = secureBaseUrl;
    }

    public List<String> getStillSizes() {
        return stillSizes;
    }

    public void setStillSizes(List<String> stillSizes) {
        this.stillSizes = stillSizes;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean isValidSize(String sizeToCheck) {
        return isValidSize(sizeToCheck, posterSizes)
                || isValidSize(sizeToCheck, backdropSizes)
                || isValidSize(sizeToCheck, profileSizes)
                || isValidSize(sizeToCheck, logoSizes);
    }

    private boolean isValidSize(String size, List<String> imageSizes) {
        if (TextUtils.isEmpty(size) || imageSizes.isEmpty())
            return false;
        return imageSizes.contains(size);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(baseUrl);
        dest.writeString(secureBaseUrl);
        dest.writeStringList(backdropSizes);
        dest.writeStringList(logoSizes);
        dest.writeStringList(posterSizes);
        dest.writeStringList(profileSizes);
        dest.writeStringList(stillSizes);
    }
}
