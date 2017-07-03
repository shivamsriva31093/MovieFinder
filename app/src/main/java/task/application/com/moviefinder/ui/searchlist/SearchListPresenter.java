package task.application.com.moviefinder.ui.searchlist;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.OmdbMovieDetails;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.MovieInfo;
import com.androidtmdbwrapper.model.tv.ExternalIds;
import com.androidtmdbwrapper.model.tv.TvInfo;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.util.TmdbApi;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public class SearchListPresenter implements SearchListContract.Presenter, MediaInfoResponseListener {

    private SearchListContract.View view;
    private MediaType filterType;
    private String query = "";
    private MediaInfoResponseListener listener;

    public SearchListPresenter(SearchListContract.View view) {
        this.view = view;
        view.setPresenter(this);
        this.listener = this;
    }

    @Override
    public void searchByKeyword(String keyword) {
        view.showLoadingIndicator(true);
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        switch (getFilteringType()) {
            case MOVIES:
                tmdb.searchService().searchMovies(keyword, null)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchRes -> {
                            view.showLoadingIndicator(false);
                            view.showSearchList(new ArrayList<>(searchRes.getResults()),
                                    searchRes.getTotalResults(), searchRes.getTotalPages());
                        }, throwable -> {
                            view.showLoadingIndicator(false);
                            view.showLoadingResultsError();
                        });
                break;
            case TV:
                tmdb.searchService().searchTv(keyword, null)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchRes -> {
                            view.showLoadingIndicator(false);
                            view.showSearchList(new ArrayList<>(searchRes.getResults()),
                                    searchRes.getTotalResults(), searchRes.getTotalPages());
                        }, throwable -> {
                            view.showLoadingIndicator(false);
                            view.showLoadingResultsError();
                        });
                break;
        }
    }


    @Override
    public void clearRecyclerView() {

    }

    @Override
    public void searchByKeyword(String keyword, int page) {
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        switch (getFilteringType()) {
            case MOVIES:
                tmdb.searchService().searchMovies(keyword, page)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchRes -> {
                            view.updateNewItems(searchRes.getResults());
                        }, throwable -> {
                            view.updateNewItems(Collections.EMPTY_LIST);
                            throwable.printStackTrace();
                        });
                break;
            case TV:
                tmdb.searchService().searchTv(keyword, page)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchRes -> {
                            view.updateNewItems(searchRes.getResults());
                        }, throwable -> {
                            view.updateNewItems(Collections.EMPTY_LIST);
                            throwable.printStackTrace();
                        });
                break;
        }
    }

    @Override
    public <T extends MediaBasic> Void onSearchItemClick(T item) {
        view.showItemDetailsUi(item);
        return null;
    }

    @Override
    public void setFilteringType(MediaType filteringType) {
        this.filterType = filteringType;
    }

    @Override
    public MediaType getFilteringType() {
        return filterType;
    }

    @Override
    public void start() {

    }

    @Override
    public void getRatings(MediaType filter, MediaBasic item, int pos) {
        TmdbApi api = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        switch (filter) {
            case MOVIES:
                getMovieInfoObservable(api, item)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(movieInfo -> listener.onImdbIdReceived(api, movieInfo.getImdbId(), pos),
                                Throwable::printStackTrace);
                break;

            case TV:
                getExternalTVIds(api, item)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(externalIds -> listener.onImdbIdReceived(api, externalIds.getImdbId(), pos),
                                Throwable::printStackTrace);
                break;
        }
    }

    private Observable<MovieInfo> getMovieInfoObservable(final TmdbApi tmdb, final MediaBasic item) {
        return tmdb.moviesService().summary(item.getId(), null);

    }

    private Observable<TvInfo> getTvInfoObservable(final TmdbApi tmdb, final MediaBasic item) {
        return tmdb.tvService().summary(item.getId(), null);

    }

    private Observable<ExternalIds> getExternalTVIds(final TmdbApi tmdb, final MediaBasic item) {
        return tmdb.tvService().getExternalIds(item.getId());
    }

    private Observable<OmdbMovieDetails> getMediaRatings(final TmdbApi tmdb, final String imdbId) {
        return tmdb.getOmdbApi().omdbSummary(imdbId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onImdbIdReceived(final TmdbApi tmdb, String imdb, int pos) {
        getMediaRatings(tmdb, imdb)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((omdbMovieDetails -> {
                    String imdbRating = omdbMovieDetails.getImdbRating();
                    view.setImdbRatings(imdbRating, pos);
                }), throwable -> {
                    throwable.printStackTrace();
                    view.setImdbRatings("N/A", pos);
                });
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

interface MediaInfoResponseListener {
    void onImdbIdReceived(TmdbApi tmdb, String imdb, int pos);
}