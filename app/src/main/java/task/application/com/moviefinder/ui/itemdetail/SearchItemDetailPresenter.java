package task.application.com.moviefinder.ui.itemdetail;

import android.support.annotation.NonNull;

import com.androidtmdbwrapper.enums.AppendToResponseItem;
import com.androidtmdbwrapper.model.OmdbMovieDetails;
import com.androidtmdbwrapper.model.core.AppendToResponse;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.MovieInfo;

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

public class SearchItemDetailPresenter implements SearchItemDetailContract.Presenter {

    private SearchItemDetailContract.View view;
    private Subscription subscription;

    public SearchItemDetailPresenter(@NonNull SearchItemDetailContract.View view) {
        this.view = Util.checkNotNull(view, "view can't be null");
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getMovieDetails(MediaBasic clickedItem) {
        view.showLoadingIndicator(true);
        final PackTmdbOmdbData data = new PackTmdbOmdbData();
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        getMovieInfoObservable(tmdb, clickedItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(movieInfo -> {
                    if (movieInfo != null && !movieInfo.getImdbId().isEmpty()) {
                        data.setMovieInfo(movieInfo);
                        return getMediaRatings(tmdb, movieInfo);
                    }
                    return Observable.<OmdbMovieDetails>error(new NullPointerException("Null response!"));
                })
                .subscribe((omdbMovieDetails) -> {
                            data.setOmdbMovieDetails(omdbMovieDetails);
                            view.showUi(data);
                            view.showLoadingIndicator(false);
                        },
                        (throwable -> {
                            view.showLoadingError();
                            view.showLoadingIndicator(false);
                            throwable.printStackTrace();
                        }));
    }


    private Observable<MovieInfo> getMovieInfoObservable(final TmdbApi tmdb, final MediaBasic item) {
        final AppendToResponse atr =
                new AppendToResponse(AppendToResponseItem.CREDITS,
                        AppendToResponseItem.VIDEOS);
        return tmdb.moviesService().summary(item.getId(), atr);

    }

    private Observable<OmdbMovieDetails> getMediaRatings(final TmdbApi tmdb, final MovieInfo item) {
        return tmdb.getOmdbApi().omdbSummary(item.getImdbId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void setUpUiForItem(PackTmdbOmdbData clickedItem) {
        view.showUi(clickedItem);
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
