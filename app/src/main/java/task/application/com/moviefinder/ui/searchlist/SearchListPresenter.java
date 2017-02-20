package task.application.com.moviefinder.ui.searchlist;

import java.util.ArrayList;

import info.movito.themoviedbapi.model.MovieDb;
import task.application.com.moviefinder.model.remote.api.tmdb.TmdbWrapper;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public class SearchListPresenter implements SearchListContract.Presenter,
        TmdbWrapper.TmdbWrapperCallbackInterface {

    private SearchListContract.View view;

    public SearchListPresenter(SearchListContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void searchByKeyword(String keyword, String searchType) {
        view.showLoadingIndicator(true);
        TmdbWrapper tmdbWrapper = new TmdbWrapper(this);
        tmdbWrapper.execute(keyword);
    }

    @Override
    public void clearRecyclerView() {

    }

    @Override
    public void onSearchItemClick(MovieDb item) {
        view.showItemDetailsUi(item);
    }

    @Override
    public void setFilteringType(String filteringType) {
    }

    @Override
    public String getFilteringType() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void onSearchResult(ArrayList<MovieDb> movieDbs) {
        view.showLoadingIndicator(false);
        if(movieDbs != null && !movieDbs.isEmpty()) {
            view.showSearchList(movieDbs);
        } else {
            view.showNoResults();
        }
    }
}
