package com.androidtmdbwrapper;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sHIVAM on 2/19/2017.
 */

public class CustomInterceptor implements Interceptor {
    private Tmdb tmdb;

    public CustomInterceptor(Tmdb tmdb) {
        this.tmdb = tmdb;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return handleIntercept(chain, tmdb.getApiKey());
    }

    public static Response handleIntercept(Chain chain, String apiKey) throws IOException {
        Request request = chain.request();
        if (!Tmdb.API_HOST.equals(request.url().host())) {
            return chain.proceed(request);
        }
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
        urlBuilder.setEncodedQueryParameter(Tmdb.API_KEY, apiKey);
        Request.Builder builder = request.newBuilder();
        builder.url(urlBuilder.build());
        return chain.proceed(builder.build());
    }
}
