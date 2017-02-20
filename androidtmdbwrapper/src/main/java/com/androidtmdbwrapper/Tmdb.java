package com.androidtmdbwrapper;

import com.androidtmdbwrapper.apiservices.MoviesService;
import com.androidtmdbwrapper.apiservices.SearchService;
import com.androidtmdbwrapper.apiservices.TmdbApiService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by sHIVAM on 2/18/2017.
 */

public class Tmdb {
    public static final String API_BASE_URL = "api.themoviedb.org";
    public static final String API_VERSION = "3";
    public static final String API_URL = "https://" + API_BASE_URL + "/" + API_VERSION + "/";

    public static final String API_KEY = "api_key";

    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;

    private String apiKey;

    public Tmdb(String apiKey) {
        this.apiKey = apiKey;
    }

    String getApiKey() {
        return apiKey;
    }

    protected OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    protected void setOkHttpClient(OkHttpClient client) {
        okHttpClient = client;
    }

    private Retrofit.Builder retrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient());
    }

    protected synchronized OkHttpClient okHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            configOkHttpClient(builder);
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    protected void configOkHttpClient(OkHttpClient.Builder builder) {

    }

    private Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = retrofitBuilder().build();
        }
        return retrofit;
    }

    public TmdbApiService apiService() {
        return getRetrofit().create(TmdbApiService.class);
    }

    public SearchService searchService() {
        return getRetrofit().create(SearchService.class);
    }

    public MoviesService moviesService() {
        return getRetrofit().create(MoviesService.class);
    }
}
