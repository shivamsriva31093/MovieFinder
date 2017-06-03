package com.androidtmdbwrapper.enums;

import java.io.Serializable;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public enum MediaType implements Serializable {
    MOVIES("Movies"),
    TV("Tv");

    private String media;

    MediaType(String media) {
        this.media = media;
    }


    @Override
    public String toString() {
        return this.media;
    }
}
