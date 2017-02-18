package com.androidtmdbwrapper.apiservices;

import com.androidtmdbwrapper.model.core.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sHIVAM on 2/18/2017.
 */

public interface SearchService {

    @GET("search/movie")
    Call<SearchResult> searchMovies(
            @Query("")
            );
}
