package com.androidtmdbwrapper.apiservices;

import com.androidtmdbwrapper.model.OmdbMovieDetails;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sHIVAM on 5/1/2017.
 */

public interface OmdbApiService {
    @GET(".")
    Observable<OmdbMovieDetails> omdbSummary(
            @Query("i") String imdbId
    );
}
