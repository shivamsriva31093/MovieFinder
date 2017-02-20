package com.androidtmdbwrapper.apiservices;

import com.androidtmdbwrapper.model.core.SearchResult;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sHIVAM on 2/18/2017.
 */

public interface SearchService {

    @GET("search/movie")
    Call<SearchResult<BasicMovieInfo>> searchMovies(
            @Query("query") String query
//            @Query("language") String language,
//            @Query("page") Integer page
            );

/*    @GET("search/tv")
    Call<SearchResult<BasicTVInfo>> tv(
            @Query("query") String query,
            @Query("page") Integer page,
            @Query("language") String language,
            @Query("first_air_date_year") Integer firstAirDateYear,
            @Query("search_type") String searchType
    );*/
}
