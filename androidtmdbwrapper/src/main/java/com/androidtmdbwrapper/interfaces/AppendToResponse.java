package com.androidtmdbwrapper.interfaces;

/**
 * Created by sHIVAM on 2/15/2017.
 */

public interface AppendToResponse<T extends AppendToResponseMethod> {
    boolean hasMethod(T method);
}
