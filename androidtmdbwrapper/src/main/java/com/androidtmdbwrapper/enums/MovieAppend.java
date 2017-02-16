package com.androidtmdbwrapper.enums;

import android.text.TextUtils;

import com.androidtmdbwrapper.interfaces.AppendToResponseMethod;

/**
 * Created by sHIVAM on 2/15/2017.
 */

public enum MovieAppend implements AppendToResponseMethod {

    CREDITS,
    IMAGES,
    VIDEOS;

    @Override
    public String getAppendedProperty() {
        return this.name().toLowerCase();
    }

    public static MovieAppend fromString(String method) {
        if (!TextUtils.isEmpty(method)) {
            try {
                return MovieAppend.valueOf(method.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Method" + method + "does not exist.", e);
            }
        }
        throw new IllegalArgumentException("Method must not be null");
    }
}
