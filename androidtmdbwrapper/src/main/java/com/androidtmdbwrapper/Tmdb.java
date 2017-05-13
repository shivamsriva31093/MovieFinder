package com.androidtmdbwrapper;

import android.util.Log;

import com.androidtmdbwrapper.apiservices.MoviesService;
import com.androidtmdbwrapper.apiservices.OmdbApiService;
import com.androidtmdbwrapper.apiservices.SearchService;
import com.androidtmdbwrapper.apiservices.TmdbApiService;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create());
        OkHttpClient.Builder clientBuilder = okHttpClient().newBuilder();
        configOkHttpClient(clientBuilder);
        return builder.client(clientBuilder.build());

    }

    protected synchronized OkHttpClient okHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
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

    public OmdbApiService getOmdbApi() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("http://www.omdbapi.com/?");
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.addConverterFactory(JacksonConverterFactory.create());

        OkHttpClient.Builder clientBuilder = okHttpClient().newBuilder();
        clientBuilder.build();
        clientBuilder.addInterceptor(new OmdbCustomInterceptor("http://www.omdbapi.com/"));
        builder.client(clientBuilder.build());
        Retrofit retrofit1 = builder.build();
        return retrofit1.create(OmdbApiService.class);
    }

    private class OmdbCustomInterceptor implements Interceptor {
        private final String BASE_URL;

        OmdbCustomInterceptor(String BASE_URL) {
            this.BASE_URL = BASE_URL;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            return handleIntercept(chain);
        }

        private Response handleIntercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (BASE_URL.equals(request.url().host())) {
                return chain.proceed(request);
            }
            HttpUrl.Builder urlBuilder = request.url().newBuilder();
            Request.Builder builder = request.newBuilder();
            Log.d("omdb_api", request.url().toString());
            builder.url(urlBuilder.build());
            return chain.proceed(builder.build());
        }
    }
}
