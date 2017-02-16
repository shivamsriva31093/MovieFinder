package com.androidtmdbwrapper.retrofittmdb;

import android.util.Log;

import com.androidtmdbwrapper.model.core.SearchResult;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.movies.MovieInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.androidtmdbwrapper.retrofittmdb.TmdbAPI.API_KEY;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class Test {
    Retrofit retrofit = TmdbAPI.retrofit;
    TmdbAPI api = retrofit.create(TmdbAPI.class);

    public void start() {

        Call<SearchResult> call = api.searchMovie(API_KEY, "Harry Potter");
        final BasicMovieInfo[] movieInfo = new BasicMovieInfo[1];
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.isSuccessful()) {
                    SearchResult result = response.body();
                    movieInfo[0] = result.getResults().get(0);
                    call.cancel();
                    getMovieDetails(movieInfo[0]);
                    Log.d("test", result.getResults().get(0).getTitle());
                } else {
                    Log.d("test", response.code() + " " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getMovieDetails(final BasicMovieInfo movieInfo) {
        Call<MovieInfo> call = api.getMovieByID(movieInfo.getId(), TmdbAPI.API_KEY, "credits");
        call.enqueue(new Callback<MovieInfo>() {
            @Override
            public void onResponse(Call<MovieInfo> call, Response<MovieInfo> response) {
                if (response.isSuccessful()) {
                    MovieInfo movie = response.body();
                    Log.d("test", movie.getTitle() + " " + movie.getCredits().getCast().get(0).getCharacter());
                } else {
                    Log.d("test", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<MovieInfo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
