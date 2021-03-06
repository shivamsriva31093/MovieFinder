
package task.application.com.colette.ui.itemdetail;

import androidx.annotation.NonNull;
import android.util.Log;

import com.androidtmdbwrapper.enums.AppendToResponseItem;
import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.OmdbMovieDetails;
import com.androidtmdbwrapper.model.core.AppendToResponse;
import com.androidtmdbwrapper.model.core.BaseMediaData;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.MovieInfo;
import com.androidtmdbwrapper.model.tv.ExternalIds;
import com.androidtmdbwrapper.model.tv.TvInfo;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.HttpException;
import task.application.com.colette.ApplicationClass;
import task.application.com.colette.model.local.realm.datamodels.MediaItem;
import task.application.com.colette.util.TmdbApi;
import task.application.com.colette.util.Util;

/**
 * Created by sHIVAM on 2/11/2017.
 */

public class SearchItemDetailPresenter implements SearchItemDetailContract.Presenter, MediaInfoResponseListener {

    private SearchItemDetailContract.View view;
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
    public void getMovieDetails(BaseMediaData clickedItem) {
        view.showLoadingIndicator(true);
        TmdbApi api = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        if (filter.equals(MediaType.MOVIES)) {
            Disposable test_d = getMovieInfoObservable(api, clickedItem)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe((movieInfo -> {
                        Log.d("test_d", movieInfo.getTagline() + "");
                        view.showUi(movieInfo);
                        view.showLoadingIndicator(false);
                        view.showRatingsViewLoadingIndicator(true);
                        listener.onImdbIdReceived(api, movieInfo.getImdbId());
                    }), (throwable -> {
                        if (throwable instanceof HttpException) {
                            HttpException ex = (HttpException) throwable;
                            if (ex.code() == 429)
                                view.showTestToast("Issue in connectivity. Please swipe refresh to reload.");
                        }
                        view.showLoadingError();
                        throwable.printStackTrace();
                        view.showLoadingIndicator(false);
                    }));
        } else if (filter.equals(MediaType.TV)) {
            getTvInfoObservable(api, clickedItem)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe((tvInfo -> {
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

        } else if (filter.equals(MediaType.PEOPLE)) {
            view.showLoadingIndicator(false);
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
                try {
                    addToRealm(mv.getId(), MediaType.MOVIES, mv.getTitle(), mv.getPosterPath(), mv.getImdbRating());
                } catch (IllegalArgumentException | NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            case TV:
                TvInfo tv = (TvInfo) item;
                try {
                    addToRealm(tv.getId(), MediaType.TV, tv.getName(), tv.getPosterPath(), tv.getImdbRating());
                } catch (IllegalArgumentException | NullPointerException e) {
                    e.printStackTrace();
                }
                break;
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

    @Override
    public MediaType getFilteringType() {
        return filter;
    }

    @Override
    public void destroy() {
        realm.removeAllChangeListeners();
        realm.close();
    }


    private Observable<MovieInfo> getMovieInfoObservable(final TmdbApi tmdb, final BaseMediaData item) {
        final AppendToResponse atr =
                new AppendToResponse(AppendToResponseItem.CREDITS,
                        AppendToResponseItem.VIDEOS);
        return tmdb.moviesService().summary(item.getId(), atr);

    }

    private Observable<TvInfo> getTvInfoObservable(final TmdbApi tmdb, final BaseMediaData item) {
        final AppendToResponse atr =
                new AppendToResponse(AppendToResponseItem.CREDITS,
                        AppendToResponseItem.VIDEOS);
        return tmdb.tvService().summary(item.getId(), atr);

    }

    private Observable<ExternalIds> getExternalTVIds(final TmdbApi tmdb, final BaseMediaData item) {
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
                    throwable.printStackTrace();
                    //view.showTestToast("Not Received rating");
                    if (throwable instanceof HttpException) {
                        if (((HttpException) throwable).code() == 403) {
                            view.showRatingsUi(null);
                        }
                    }

                }));
    }

}

interface MediaInfoResponseListener {
    void onImdbIdReceived(TmdbApi tmdb, String imdb);
}
