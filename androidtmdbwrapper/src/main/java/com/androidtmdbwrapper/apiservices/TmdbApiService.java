package com.androidtmdbwrapper.apiservices;

import com.androidtmdbwrapper.model.core.ApiConfiguration;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by sHIVAM on 2/18/2017.
 */

public interface TmdbApiService {
    @GET("configuration")
    Call<ApiConfiguration> loadConfiguration();
}
