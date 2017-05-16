package com.androidtmdbwrapper.apiservices;

import com.androidtmdbwrapper.model.core.SearchResult;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.tv.BasicTVInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sHIVAM on 2/18/2017.
 */

public interface SearchService {

    @GET("search/movie")
    Observable<SearchResult<BasicMovieInfo>> searchMovies(
            @Query("query") String query
//            @Query("language") String language,
//            @Query("page") Integer page
            );

    @GET("search/tv")
    Observable<SearchResult<BasicTVInfo>> searchTv(
            @Query("query") String query
    );
}
