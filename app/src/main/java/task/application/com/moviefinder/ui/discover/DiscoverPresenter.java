package task.application.com.moviefinder.ui.discover;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.OmdbMovieDetails;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.movies.MiscellaneousResults;
import com.androidtmdbwrapper.model.movies.MovieInfo;
import com.androidtmdbwrapper.model.tv.ExternalIds;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.util.TmdbApi;

/**
 * Created by sHIVAM on 7/12/2017.
 */

public class DiscoverPresenter implements DiscoverContract.Presenter, MediaInfoResponseListener {

    private final DiscoverContract.View view;
    private DiscoverActivity.QueryType queryType;
    private final String viewID;
    private String lang;
    private String region;
    private CompositeDisposable disposables = new CompositeDisposable();
    private MediaInfoResponseListener listener;

    public DiscoverPresenter(DiscoverContract.View view, String ID) {
        this.view = view;
        this.viewID = ID;
        view.setPresenter(this);
        this.listener = this;
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
                    view.setEndlessScrollLoading(false);
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

    @Override
    public void getRatings(MediaType filter, MediaBasic item, int pos) {
        TmdbApi api = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        switch (filter) {
            case MOVIES:
                getMovieInfoObservable(api, item)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(movieInfo -> listener.onImdbIdReceived(api, movieInfo.getImdbId(), pos),
                                throwable -> {
                                    throwable.printStackTrace();
                                    view.setImdbRatings("N/A", pos);
                                });
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
    public String getViewID() {
        return viewID;
    }
}

interface MediaInfoResponseListener {
    void onImdbIdReceived(TmdbApi tmdb, String imdb, int pos);
}
