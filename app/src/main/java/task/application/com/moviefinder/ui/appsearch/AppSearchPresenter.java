package task.application.com.moviefinder.ui.appsearch;

import com.androidtmdbwrapper.enums.MediaType;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.util.TmdbApi;

/**
 * Created by sHIVAM on 7/22/2017.
 */

public class AppSearchPresenter implements AppSearchContract.Presenter {

    private final String TAG;
    private MediaType queryType;
    private String query;
    private AppSearchContract.View view;


    public AppSearchPresenter(AppSearchContract.View view, String tag) {
        this.view = view;
        this.view.setPresenter(this);
        TAG = tag;
    }

    @Override
    public void start() {

    }

    @Override
    public void searchQuery(String keyword) {
        view.showLoadingIndicator(true);
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        switch (queryType) {
            case MOVIES:
                tmdb.searchService().searchMovies(keyword, null)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchRes -> {
                            view.showLoadingIndicator(false);
                            view.showResultList(new ArrayList<>(searchRes.getResults()),
                                    searchRes.getTotalPages(), searchRes.getTotalResults());
                        }, throwable -> {
                            throwable.printStackTrace();
                            view.showLoadingIndicator(false);
                            view.showNoResults();
                        });
                break;
            case TV:
                tmdb.searchService().searchTv(keyword, null)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchRes -> {
                            view.showLoadingIndicator(false);
                            view.showResultList(new ArrayList<>(searchRes.getResults()),
                                    searchRes.getTotalPages(), searchRes.getTotalResults());
                        }, throwable -> {
                            throwable.printStackTrace();
                            view.showLoadingIndicator(false);
                            view.showNoResults();
                        });
                break;
            case PEOPLE:
                tmdb.searchService().searchPeople(keyword, null)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchRes -> {
                            view.showLoadingIndicator(false);
                            view.showResultList(new ArrayList<>(searchRes.getResults()),
                                    searchRes.getTotalPages(), searchRes.getTotalResults());
                        }, throwable -> {
                            throwable.printStackTrace();
                            view.showLoadingIndicator(false);
                            view.showNoResults();
                        });
                break;

            default:
                view.showLoadingIndicator(false);
                break;
        }
    }

    @Override
    public void loadNextPage(int page) {
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        switch (queryType) {
            case MOVIES:
                tmdb.searchService().searchMovies(query, page)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchRes -> {
                            view.updateNewItems(searchRes.getResults());
                        }, throwable -> {
                            throwable.printStackTrace();
                            view.setEndlessScrollLoading(false);
                        });
                break;
            case TV:
                tmdb.searchService().searchTv(query, page)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchRes -> {
                            view.updateNewItems(searchRes.getResults());
                        }, throwable -> {
                            view.setEndlessScrollLoading(false);
                            throwable.printStackTrace();
                        });
                break;
            case PEOPLE:
                tmdb.searchService().searchPeople(query, page)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchRes -> {
                            view.updateNewItems(searchRes.getResults());
                        }, throwable -> {
                            view.setEndlessScrollLoading(false);
                            throwable.printStackTrace();
                        });
                break;
        }
    }

    @Override
    public void setQueryType(MediaType queryType) {
        this.queryType = queryType;
    }

    @Override
    public MediaType getQueryType() {
        return queryType;
    }

    @Override
    public String getViewID() {
        return TAG;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public void setQuery(String query) {
        this.query = query;
    }
}
