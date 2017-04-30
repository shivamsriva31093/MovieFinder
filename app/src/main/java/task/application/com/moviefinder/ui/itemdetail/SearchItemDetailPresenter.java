package task.application.com.moviefinder.ui.itemdetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.androidtmdbwrapper.enums.AppendToResponseItem;
import com.androidtmdbwrapper.model.core.AppendToResponse;
import com.androidtmdbwrapper.model.movies.MovieInfo;

import info.movito.themoviedbapi.model.MovieDb;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.util.TmdbApi;
import task.application.com.moviefinder.util.Util;

/**
 * Created by sHIVAM on 2/11/2017.
 */

public class SearchItemDetailPresenter implements SearchItemDetailContract.Presenter {

    private SearchItemDetailContract.View view;

    public SearchItemDetailPresenter(@NonNull SearchItemDetailContract.View view) {
        this.view = Util.checkNotNull(view, "view can't be null");
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getMovieDetails(MovieDb clickedItem) {
        view.showLoadingIndicator(true);
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        AppendToResponse atr = new AppendToResponse(AppendToResponseItem.CREDITS, AppendToResponseItem.VIDEOS);
        Call<MovieInfo> call = tmdb.moviesService().summary(clickedItem.getId(), atr);
        call.enqueue(new Callback<MovieInfo>() {
            @Override
            public void onResponse(Call<MovieInfo> call, Response<MovieInfo> response) {
                if (response.isSuccessful()) {
                    Log.d("tmdb_api", call.request().url().toString() + ": Response received");
                    view.showUi(response.body());
                    view.showLoadingIndicator(false);
                } else {
                    view.showLoadingError();
                    view.showLoadingIndicator(false);
                }
            }

            @Override
            public void onFailure(Call<MovieInfo> call, Throwable t) {
                view.showLoadingError();
                view.showLoadingIndicator(false);
                t.printStackTrace();
            }
        });
    }

    @Override
    public void setUpUiForItem(MovieInfo clickedItem) {
        view.showUi(clickedItem);
    }
}
