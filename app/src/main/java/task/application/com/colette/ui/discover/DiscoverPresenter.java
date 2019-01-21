package task.application.com.colette.ui.discover;

import android.util.Pair;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.OmdbMovieDetails;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.movies.MiscellaneousResults;
import com.androidtmdbwrapper.model.movies.MovieInfo;
import com.androidtmdbwrapper.model.tv.ExternalIds;
import com.google.common.base.Throwables;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.HttpException;
import task.application.com.colette.ApplicationClass;
import task.application.com.colette.model.local.realm.datamodels.MediaItem;
import task.application.com.colette.remote.tmdb.NoNetworkException;
import task.application.com.colette.util.TmdbApi;

/**
 * Created by sHIVAM on 7/12/2017.
 */

public class DiscoverPresenter implements DiscoverContract.Presenter, MediaInfoResponseListener {

    private static final int COUNTER_START = 1;
    private static final int ATTEMPTS = 4;
    private final DiscoverContract.View view;
    private DiscoverActivity.QueryType queryType;
    private final String viewID;
    private String lang;
    private String region;
    private MediaInfoResponseListener listener;
    private Realm realm;

    public DiscoverPresenter(DiscoverContract.View view, String ID) {
        this.view = view;
        this.viewID = ID;
        view.setPresenter(this);
        this.listener = this;
        realm = Realm.getDefaultInstance();
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

        observable.subscribeOn(Schedulers.io());
        observable.observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(basicMovieInfos -> {
            view.showResultList(basicMovieInfos.getResults(), basicMovieInfos.getTotalPages(), basicMovieInfos.getTotalResults());
            view.showLoadingIndicator(false);
        }, throwable -> {
            throwable.printStackTrace();
            view.showNetworkError();
            /*if (throwable instanceof NoNetworkException) {
                view.showNetworkError();
            } else {
                view.showNoResults();
            }*/
            view.showLoadingIndicator(false);
        });
    }

    private void getNextItems(Observable<MiscellaneousResults<BasicMovieInfo>> observable) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(basicMovieInfos -> {
                    view.updateNewItems(basicMovieInfos.getResults());
                }, throwable -> {
                    if (throwable instanceof NoNetworkException) {
                        view.showNetworkError();
                    } else {
                        throwable.printStackTrace();
                    }
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
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(throwableObservable ->
                                throwableObservable.zipWith(Observable.range(COUNTER_START, ATTEMPTS), Pair::new)
                                        .flatMap(throwableIntegerPair -> {
                                            if (throwableIntegerPair.second < ATTEMPTS) {
                                                return Observable.timer((long) Math.pow(2, throwableIntegerPair.second), TimeUnit.SECONDS);
                                            }
                                            throw Throwables.propagate(throwableIntegerPair.first);
                                        })
                );
    }

    private Observable<MiscellaneousResults<BasicMovieInfo>> getUpcomingObservable(String ISO639_1_language,
                                                                                   int page,
                                                                                   String ISO3166_1_region) {
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        return tmdb.moviesService().getUpcoming(ISO639_1_language, page, ISO3166_1_region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(throwableObservable ->
                                throwableObservable.zipWith(Observable.range(COUNTER_START, ATTEMPTS), Pair::new)
                                        .flatMap(throwableIntegerPair -> {
                                            if (throwableIntegerPair.second < ATTEMPTS) {
                                                return Observable.timer((long) Math.pow(2, throwableIntegerPair.second), TimeUnit.SECONDS);
                                            }
                                            throw Throwables.propagate(throwableIntegerPair.first);
                                        })
                );
    }

    private Observable<MiscellaneousResults<BasicMovieInfo>> getTopRatedObservable(String ISO639_1_language,
                                                                                   int page,
                                                                                   String ISO3166_1_region) {
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        return tmdb.moviesService().getTopRated(ISO639_1_language, page, ISO3166_1_region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(throwableObservable ->
                                throwableObservable.zipWith(Observable.range(COUNTER_START, ATTEMPTS), Pair::new)
                                        .flatMap(throwableIntegerPair -> {
                                            if (throwableIntegerPair.second < ATTEMPTS) {
                                                return Observable.timer((long) Math.pow(2, throwableIntegerPair.second), TimeUnit.SECONDS);
                                            }
                                            throw Throwables.propagate(throwableIntegerPair.first);
                                        })
                );
    }

    private Observable<MiscellaneousResults<BasicMovieInfo>> getPopularObservable(
            String ISO639_1_language,
            int page,
            String ISO3166_1_region) {
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        return tmdb.moviesService().getPopular(ISO639_1_language, page, ISO3166_1_region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(throwableObservable ->
                                throwableObservable.zipWith(Observable.range(COUNTER_START, ATTEMPTS), Pair::new)
                                        .flatMap(throwableIntegerPair -> {
                                            if (throwableIntegerPair.second < ATTEMPTS) {
                                                return Observable.timer((long) Math.pow(2, throwableIntegerPair.second), TimeUnit.SECONDS);
                                            }
                                            throw Throwables.propagate(throwableIntegerPair.first);
                                        })
                );
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
                    if (throwable instanceof HttpException) {
                        HttpException msg = (HttpException) throwable;
                        if (msg.code() == 403) {
                            view.setImdbRatings("-", pos);
                        }
                    } else {
                        view.setImdbRatings("Unrated", pos);
                    }
                });
    }

    @Override
    public String getViewID() {
        return viewID;
    }

    @Override
    public void addMediaToFavorites(MediaBasic item) {
        BasicMovieInfo mv = (BasicMovieInfo) item;
        try {
            addToRealm(mv.getId(), MediaType.MOVIES, mv.getTitle(), mv.getPosterPath(), mv.getImdbRating());
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void addToRealm(int tmdbId, MediaType mediaType, String title, String backDrop, String imdbRating) {
        realm.executeTransactionAsync(realm1 -> {
                    realm1.copyToRealm(new MediaItem(
                            String.valueOf(tmdbId),
                            mediaType,
                            title,
                            backDrop,
                            imdbRating));
                }, () -> view.showTestToast("Added to favorites"),
                error -> {
                    view.showTestToast("Error in adding to favorites. Please try again later.");
                    error.printStackTrace();
                });
    }

    @Override
    public void removeMediaFromFavorites(MediaBasic item) {
        final RealmResults<MediaItem> res = realm.where(MediaItem.class).equalTo("tmdbId", String.valueOf(item.getId()))
                .findAll();
        if (res.isValid() && !res.isEmpty()) {
            realm.executeTransaction((realm1 -> {
                try {
                    res.deleteAllFromRealm();
                    view.showTestToast("Removed from favorites");
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                }
            }));
        }

    }
}

interface MediaInfoResponseListener {
    void onImdbIdReceived(TmdbApi tmdb, String imdb, int pos);
}
