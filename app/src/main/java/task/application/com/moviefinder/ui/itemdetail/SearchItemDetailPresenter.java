package task.application.com.moviefinder.ui.itemdetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.androidtmdbwrapper.enums.AppendToResponseItem;
import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.OmdbMovieDetails;
import com.androidtmdbwrapper.model.core.AppendToResponse;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.MovieInfo;
import com.androidtmdbwrapper.model.tv.ExternalIds;
import com.androidtmdbwrapper.model.tv.TvInfo;

import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.model.local.realm.datamodels.MediaItem;
import task.application.com.moviefinder.util.TmdbApi;
import task.application.com.moviefinder.util.Util;

/**
 * Created by sHIVAM on 2/11/2017.
 */

public class SearchItemDetailPresenter implements SearchItemDetailContract.Presenter, MediaInfoResponseListener {

    private SearchItemDetailContract.View view;
    private Subscription subscription;
    private MediaType filter = MediaType.MOVIES;
    private MediaInfoResponseListener listener;
    private Realm realm;

    public SearchItemDetailPresenter(@NonNull SearchItemDetailContract.View view) {
        this.view = Util.checkNotNull(view, "view can't be null");
        view.setPresenter(this);
        listener = this;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void start() {

    }

    @Override
    public void getMovieDetails(MediaBasic clickedItem) {
        view.showLoadingIndicator(true);
        TmdbApi api = TmdbApi.getApiClient(ApplicationClass.API_KEY);

        if (filter.equals(MediaType.MOVIES)) {
            getMovieInfoObservable(api, clickedItem)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe((movieInfo -> {
                        view.showUi(movieInfo);
                        view.showLoadingIndicator(false);
                        view.showRatingsViewLoadingIndicator(true);
                        listener.onImdbIdReceived(api, movieInfo.getImdbId());
                    }), (throwable -> {
                        view.showLoadingError();
                        view.showLoadingIndicator(false);
                    }));
        } else {
            Log.d("test", clickedItem.getId() + "");
            getTvInfoObservable(api, clickedItem)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe((tvInfo -> {
                        Log.d("test", tvInfo.getOriginalName());
                        view.showUi(tvInfo);
                        view.showLoadingIndicator(false);

                    }), (throwable -> {
                        view.showLoadingError();
                        throwable.printStackTrace();
                        view.showLoadingIndicator(false);
                    }));
            view.showRatingsViewLoadingIndicator(true);
            getExternalTVIds(api, clickedItem)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe((externalIds -> {
                        listener.onImdbIdReceived(api, externalIds.getImdbId());
                    }), (throwable -> {
                        throwable.printStackTrace();
                        view.showRatingsViewLoadingIndicator(false);
                    }));

        }
    }

    @Override
    public void setFilteringType(MediaType filteringType) {
        this.filter = filteringType;
    }

    @Override
    public void checkMediaInDB(MediaBasic item) {
        final RealmResults<MediaItem> res = realm.where(MediaItem.class)
                .equalTo("tmdbId", String.valueOf(item.getId()))
                .findAll();
        if (!res.isEmpty() && res.isValid())
            view.setFavorite(true);
    }

    @Override
    public void addMediaToFavorites(MediaBasic item) {
        switch (getFilteringType()) {
            case MOVIES:
                MovieInfo mv = (MovieInfo) item;
                addToRealm(mv.getId(), MediaType.MOVIES, mv.getTitle(), mv.getBackdropPath());
                break;
            case TV:
                TvInfo tv = (TvInfo) item;
                addToRealm(tv.getId(), MediaType.TV, tv.getName(), tv.getBackdropPath());
                break;
        }
    }

    private void addToRealm(int tmdbId, MediaType mediaType, String title, String backDrop) {
        realm.executeTransactionAsync(realm1 -> {
                    realm1.copyToRealm(new MediaItem(
                            String.valueOf(tmdbId),
                            mediaType,
                            title,
                            backDrop
                    ));
                }, () -> view.showTestToast("Added to favorites!"),
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

    @Override
    public MediaType getFilteringType() {
        return filter;
    }

    @Override
    public void destroy() {
        realm.removeAllChangeListeners();
        realm.close();
    }


    private Observable<MovieInfo> getMovieInfoObservable(final TmdbApi tmdb, final MediaBasic item) {
        final AppendToResponse atr =
                new AppendToResponse(AppendToResponseItem.CREDITS,
                        AppendToResponseItem.VIDEOS);
        return tmdb.moviesService().summary(item.getId(), atr);

    }

    private Observable<TvInfo> getTvInfoObservable(final TmdbApi tmdb, final MediaBasic item) {
        final AppendToResponse atr =
                new AppendToResponse(AppendToResponseItem.CREDITS,
                        AppendToResponseItem.VIDEOS);
        return tmdb.tvService().summary(item.getId(), atr);

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
    public void onImdbIdReceived(final TmdbApi tmdb, String imdb) {
        getMediaRatings(tmdb, imdb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((omdbMovieDetails -> {
                    view.showRatingsViewLoadingIndicator(false);
                    view.showRatingsUi(omdbMovieDetails);
                }), (throwable -> {
                    view.showRatingsViewLoadingIndicator(false);
                }));
    }

}

interface MediaInfoResponseListener {
    void onImdbIdReceived(TmdbApi tmdb, String imdb);
}
