package com.androidtmdbwrapper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androidtmdbwrapper.model.core.SearchResult;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationClass app = (ApplicationClass) getApplicationContext();
        Tmdb manager = app.getApi();
        manager.searchService().searchMovies("Harry", null).enqueue(new Callback<SearchResult<BasicMovieInfo>>() {
            @Override
            public void onResponse(Call<SearchResult<BasicMovieInfo>> call, Response<SearchResult<BasicMovieInfo>> response) {
                if (response.isSuccessful()) {
                    SearchResult<BasicMovieInfo> result = response.body();
                    Log.d("test", result.getResults().get(190).getTitle());
                }
            }

            @Override
            public void onFailure(Call<SearchResult<BasicMovieInfo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
