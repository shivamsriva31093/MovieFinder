package task.application.com.moviefinder.ui.itemdetail;

import android.support.annotation.NonNull;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;
import task.application.com.moviefinder.model.remote.api.tmdb.GetSearchDetail;
import task.application.com.moviefinder.util.Util;

/**
 * Created by sHIVAM on 2/11/2017.
 */

public class SearchItemDetailPresenter implements SearchItemDetailContract.Presenter,
        GetSearchDetail.GetMovieDetailInterface {

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
        GetSearchDetail obj = new GetSearchDetail(clickedItem, this);
        obj.startAsyncSearch();
    }

    @Override
    public void onGetCastDetailsListener(List<PersonCast> cast) {
        if (cast != null && !cast.isEmpty()) {
            view.prepareUI(cast);
            view.showLoadingIndicator(false);
        } else {
            view.showLoadingError();
            view.showLoadingIndicator(false);
        }
    }

    @Override
    public void setUpUiForItem(MovieDb clickedItem) {
        view.showUi(clickedItem);
    }
}
