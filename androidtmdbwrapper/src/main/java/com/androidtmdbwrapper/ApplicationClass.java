package com.androidtmdbwrapper;

import android.app.Application;
import android.util.Log;

import com.androidtmdbwrapper.model.core.ApiConfiguration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sHIVAM on 2/19/2017.
 */

public class ApplicationClass extends Application {
    private static final String TAG = ApplicationClass.class.getName();
    private static final String API_KEY = "1d49e17fa9eb8f8d72d20a75af1099b1";
    private static Tmdb api;
    private static ApiConfiguration apiConfig;

    public ApplicationClass() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        api = new TmdbApi(API_KEY);
        api.apiService().loadConfiguration().enqueue(new Callback<ApiConfiguration>() {
            @Override
            public void onResponse(Call<ApiConfiguration> call, Response<ApiConfiguration> response) {
                if (response.isSuccessful()) {
                    apiConfig = response.body();
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiConfiguration> call, Throwable t) {
                Log.d(TAG, "Failure in connecting to API " + call.request().url().toString());
                t.printStackTrace();
            }
        });
    }

    public Tmdb getApi() {
        return api;
    }

    public ApiConfiguration getApiConfig() {
        return apiConfig;
    }

    private static class TmdbApi extends Tmdb {
        public TmdbApi(String apiKey) {
            super(apiKey);
        }
    }
}
