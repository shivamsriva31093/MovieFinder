package com.androidtmdbwrapper.model.credits;

import android.os.Parcel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class MediaCreditCrew extends MediaCredit {
    @JsonProperty("department")
    private String department;
    @JsonProperty("job")
    private String job;

    public MediaCreditCrew() {
    }

    protected MediaCreditCrew(Parcel in) {
        super(in);
        department = in.readString();
        job = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(department);
        dest.writeString(job);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaCreditCrew> CREATOR = new Creator<MediaCreditCrew>() {
        @Override
        public MediaCreditCrew createFromParcel(Parcel in) {
            return new MediaCreditCrew(in);
        }

        @Override
        public MediaCreditCrew[] newArray(int size) {
            return new MediaCreditCrew[size];
        }
    };

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
