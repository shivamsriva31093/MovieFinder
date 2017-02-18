package com.androidtmdbwrapper.enums;

/**
 * Created by sHIVAM on 2/18/2017.
 */

public enum AppendToResponseItem {
    VIDEOS("videos"),
    RELEASE_DATES("release_dates"),
    CREDITS("credits"),
    SIMILAR("similar"),
    IMAGES("images"),
    ALTERNATIVE_TITLES("alternative_titles"),
    EXTERNAL_IDS("external_ids"),
    CONTENT_RATINGS("content_ratings");

    private final String value;

    AppendToResponseItem(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
