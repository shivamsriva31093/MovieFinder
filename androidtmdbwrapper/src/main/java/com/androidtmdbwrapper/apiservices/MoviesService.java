package com.androidtmdbwrapper.apiservices;

import com.androidtmdbwrapper.model.core.AppendToResponse;
import com.androidtmdbwrapper.model.movies.MovieInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sHIVAM on 2/18/2017.
 */

public interface MoviesService {

    @GET("movie/{id}")
    Observable<MovieInfo> summary(
            @Path("id") int tmdbId,
            @Query("append_to_response") AppendToResponse appendToResponse
    );
}
