package task.application.com.moviefinder.ui.search;

import android.support.annotation.NonNull;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.tv.BasicTVInfo;

import java.util.ArrayList;

import info.movito.themoviedbapi.model.MovieDb;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.model.remote.api.tmdb.TmdbWrapper;
import task.application.com.moviefinder.util.TmdbApi;
import task.application.com.moviefinder.util.Util;

/**
 * Created by sHIVAM on 1/30/2017.
 */

public class SearchPresenter implements SearchContract.Presenter, TmdbWrapper.TmdbWrapperCallbackInterface{

    private final SearchContract.View searchView;
    private MediaType filterType;
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
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        if (getFilteringType().equals(MediaType.MOVIES)) {
            tmdb.searchService().searchMovies(keyword)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(searchRes -> {
                        searchView.showSearchListUi(new ArrayList<BasicMovieInfo>(searchRes.getResults()));
                        searchView.showLoadingIndicator(false);
                    });

        } else {

            tmdb.searchService().searchTv(keyword)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(searchRes -> {
                        searchView.showSearchListUi(new ArrayList<BasicTVInfo>(searchRes.getResults()));
                        searchView.showLoadingIndicator(false);
                    });
        }
    }


    @Override
    public void setFilteringType(@NonNull MediaType filteringType) {
        this.filterType = filteringType;
    }

    @Override
    public MediaType getFilteringType() {
        return filterType;
    }

    @Override
    public void onSearchResult(ArrayList<MovieDb> movieDbs) {
//        searchView.showLoadingIndicator(false);
//        if(movieDbs != null && !movieDbs.isEmpty())
//            searchView.showSearchListUi(movieDbs);
//        else
//            searchView.showLoadingResultsError();
    }
}
