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
import task.application.com.moviefinder.ApplicationClass;
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

    public SearchItemDetailPresenter(@NonNull SearchItemDetailContract.View view) {
        this.view = Util.checkNotNull(view, "view can't be null");
        view.setPresenter(this);
        listener = this;
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
    public MediaType getFilteringType() {
        return filter;
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

    class PackTmdbOmdbData {
        private MovieInfo movieInfo;
        private OmdbMovieDetails omdbMovieDetails;

        public PackTmdbOmdbData() {
        }

        public PackTmdbOmdbData(MovieInfo movieInfo, OmdbMovieDetails omdbMovieDetails) {
            this.movieInfo = movieInfo;
            this.omdbMovieDetails = omdbMovieDetails;
        }

        public MovieInfo getMovieInfo() {
            return movieInfo;
        }

        public void setMovieInfo(MovieInfo movieInfo) {
            this.movieInfo = movieInfo;
        }

        public OmdbMovieDetails getOmdbMovieDetails() {
            return omdbMovieDetails;
        }

        public void setOmdbMovieDetails(OmdbMovieDetails omdbMovieDetails) {
            this.omdbMovieDetails = omdbMovieDetails;
        }

        @Override
        public String toString() {
            return "PackTmdbOmdbData{" +
                    "omdbMovieDetails=" + omdbMovieDetails.getImdbRating() +
                    '}';
        }
    }

}

interface MediaInfoResponseListener {
    void onImdbIdReceived(TmdbApi tmdb, String imdb);
}
