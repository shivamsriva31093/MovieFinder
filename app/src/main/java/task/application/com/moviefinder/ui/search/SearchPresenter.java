package task.application.com.moviefinder.ui.search;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import info.movito.themoviedbapi.model.MovieDb;
import task.application.com.moviefinder.model.remote.api.tmdb.TmdbWrapper;
import task.application.com.moviefinder.util.Util;

/**
 * Created by sHIVAM on 1/30/2017.
 */

public class SearchPresenter implements SearchContract.Presenter, TmdbWrapper.TmdbWrapperCallbackInterface{

    private final SearchContract.View searchView;
    private String filterType;
    public SearchPresenter(@NonNull SearchContract.View searchView) {
        Util.checkNotNull(searchView, "view object can't be null");
        this.searchView = searchView;
        searchView.setPresenter(this);
    }

    @Override
    public void start() {
        
    }

    @Override
    public void searchByKeyword(String keyword) {
        searchView.showLoadingIndicator(true);
        TmdbWrapper wrapper = new TmdbWrapper(this);
        wrapper.execute(keyword);
    }

    @Override
    public void setFilteringType(@NonNull String filteringType) {
        this.filterType = filteringType;
    }

    @Override
    public String getFilteringType() {
        return filterType;
    }

    @Override
    public void onSearchResult(ArrayList<MovieDb> movieDbs) {
        searchView.showLoadingIndicator(false);
        if(movieDbs != null && !movieDbs.isEmpty())
            searchView.showSearchListUi(movieDbs);
        else
            searchView.showLoadingResultsError();
    }
}
