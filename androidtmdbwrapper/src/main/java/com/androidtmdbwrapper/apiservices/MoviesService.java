package com.androidtmdbwrapper.apiservices;

import com.androidtmdbwrapper.model.core.AppendToResponse;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.movies.MiscellaneousResults;
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
            @Path("id") Integer tmdbId,
            @Query("append_to_response") AppendToResponse appendToResponse
    );

    @GET("movie/now_playing")
    Observable<MiscellaneousResults<BasicMovieInfo>> getNowPlaying(
            @Query("language") String ISO639_1_language,
            @Query("page") int page,
            @Query("region") String ISO3166_1_region
    );

    @GET("movie/popular")
    Observable<MiscellaneousResults<BasicMovieInfo>> getPopular(
            @Query("language") String ISO639_1_language,
            @Query("page") int page,
            @Query("region") String ISO3166_1_region
    );

    @GET("movie/top_rated")
    Observable<MiscellaneousResults<BasicMovieInfo>> getTopRated(
            @Query("language") String ISO639_1_language,
            @Query("page") int page,
            @Query("region") String ISO3166_1_region
    );

    @GET("movie/upcoming")
    Observable<MiscellaneousResults<BasicMovieInfo>> getUpcoming(
            @Query("language") String ISO639_1_language,
            @Query("page") int page,
            @Query("region") String ISO3166_1_region
    );

}
