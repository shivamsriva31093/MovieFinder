package task.application.com.moviefinder.ui.search;

import android.support.annotation.NonNull;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.tv.BasicTVInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.util.TmdbApi;
import task.application.com.moviefinder.util.Util;

/**
 * Created by sHIVAM on 1/30/2017.
 */

public class SearchPresenter implements SearchContract.Presenter {

    private final SearchContract.View searchView;
    private MediaType filterType = MediaType.MOVIES;

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
            tmdb.searchService().searchMovies(keyword, null)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(searchRes -> {
                        searchView.showSearchListUi(new ArrayList<BasicMovieInfo>(searchRes.getResults()),
                                searchRes.getTotalResults(), searchRes.getTotalPages());
                        searchView.showLoadingIndicator(false);
                    }, throwable -> {
                        searchView.showLoadingResultsError();
                        searchView.showLoadingIndicator(false);
                    });

        } else {

            tmdb.searchService().searchTv(keyword, null)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(searchRes -> {
                        searchView.showSearchListUi(new ArrayList<BasicTVInfo>(searchRes.getResults()),
                                searchRes.getTotalResults(), searchRes.getTotalPages());
                        searchView.showLoadingIndicator(false);
                    }, throwable -> {
                        searchView.showLoadingResultsError();
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

}
