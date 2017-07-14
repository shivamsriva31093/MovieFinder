package task.application.com.moviefinder.ui.discover;

import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.movies.MiscellaneousResults;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.util.TmdbApi;

/**
 * Created by sHIVAM on 7/12/2017.
 */

public class DiscoverPresenter implements DiscoverContract.Presenter {

    private final DiscoverContract.View view;
    private DiscoverActivity.QueryType queryType;
    private final int viewID;
    private String lang;
    private String region;

    public DiscoverPresenter(DiscoverContract.View view, int ID) {
        this.view = view;
        this.viewID = ID;
        view.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void makeQuery(String ISO639_1_language, int page, String ISO3166_1_region) {
        this.lang = ISO639_1_language;
        this.region = ISO3166_1_region;
        view.showLoadingIndicator(true);
        switch (queryType) {
            case NOW_PLAYING:
                getResults(getNowPlayingObservable(ISO639_1_language, page, ISO3166_1_region));
                break;
            case UPCOMING:
                getResults(getUpcomingObservable(ISO639_1_language, page, ISO3166_1_region));
                break;
            case POPULAR:
                getResults(getPopularObservable(ISO639_1_language, page, ISO3166_1_region));
                break;
            case TOP_RATED:
                getResults(getTopRatedObservable(ISO639_1_language, page, ISO3166_1_region));
                break;
        }
    }

    @Override
    public void loadNextPage(int page) {
        switch (queryType) {
            case NOW_PLAYING:
                getNextItems(getNowPlayingObservable(lang, page, region));
                break;
            case UPCOMING:
                getNextItems(getUpcomingObservable(lang, page, region));
                break;
            case POPULAR:
                getNextItems(getPopularObservable(lang, page, region));
                break;
            case TOP_RATED:
                getNextItems(getTopRatedObservable(lang, page, region));
                break;
        }
    }

    @Override
    public void setQueryType(DiscoverActivity.QueryType queryType) {
        this.queryType = queryType;
    }

    @Override
    public DiscoverActivity.QueryType getQueryType() {
        return queryType;
    }

    private void getResults(Observable<MiscellaneousResults<BasicMovieInfo>> observable) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(basicMovieInfos -> {
                    view.showResultList(basicMovieInfos.getResults(), basicMovieInfos.getTotalPages(), basicMovieInfos.getTotalResults());
                    view.showLoadingIndicator(false);
                }, throwable -> {
                    throwable.printStackTrace();
                    view.showLoadingIndicator(false);
                    view.showNoResults();
                });
    }

    private void getNextItems(Observable<MiscellaneousResults<BasicMovieInfo>> observable) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(basicMovieInfos -> {
                    view.updateNewItems(basicMovieInfos.getResults());
                }, throwable -> {
                    throwable.printStackTrace();
                    view.updateNewItems(new ArrayList<>());
                });
    }


    private Observable<MiscellaneousResults<BasicMovieInfo>>
    getNowPlayingObservable(String ISO639_1_language,
                            int page,
                            String ISO3166_1_region) {
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        return tmdb.moviesService().getNowPlaying(ISO639_1_language, page, ISO3166_1_region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<MiscellaneousResults<BasicMovieInfo>> getUpcomingObservable(String ISO639_1_language,
                                                                                   int page,
                                                                                   String ISO3166_1_region) {
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        return tmdb.moviesService().getUpcoming(ISO639_1_language, page, ISO3166_1_region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<MiscellaneousResults<BasicMovieInfo>> getTopRatedObservable(String ISO639_1_language,
                                                                                   int page,
                                                                                   String ISO3166_1_region) {
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        return tmdb.moviesService().getTopRated(ISO639_1_language, page, ISO3166_1_region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<MiscellaneousResults<BasicMovieInfo>> getPopularObservable(
            String ISO639_1_language,
            int page,
            String ISO3166_1_region) {
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        return tmdb.moviesService().getPopular(ISO639_1_language, page, ISO3166_1_region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
