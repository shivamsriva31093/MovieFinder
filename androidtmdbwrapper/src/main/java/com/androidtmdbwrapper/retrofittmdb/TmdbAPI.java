package com.androidtmdbwrapper.retrofittmdb;

import com.androidtmdbwrapper.model.core.ApiConfiguration;
import com.androidtmdbwrapper.model.core.SearchResult;
import com.androidtmdbwrapper.model.movies.MovieInfo;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sHIVAM on 2/15/2017.
 */

public interface TmdbAPI {
    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY = "1d49e17fa9eb8f8d72d20a75af1099b1";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    @GET("configuration")
    Call<ApiConfiguration> loadConfiguration(@Query("api_key") String API_KEY);

    @GET("search/movie")
    Call<SearchResult> searchMovie(@Query("api_key") String API_KEY, @Query("query") String query);

    @GET("movie/{movie_id}")
    Call<MovieInfo> getMovieByID(
            @Path("movie_id") final int id,
            @Query("api_key") final String API_KEY,
            @Query("append_to_response") final String appendedProperties
    );
}
