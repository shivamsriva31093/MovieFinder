package task.application.com.moviefinder.ui.splash;

import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.movies.MiscellaneousResults;

import java.util.ArrayList;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.remote.tmdb.NoNetworkException;
import task.application.com.moviefinder.ui.discover.DiscoverActivity;
import task.application.com.moviefinder.util.TmdbApi;

/**
 * Created by sHIVAM on 11/3/2017.
 */

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View view;
    private WeakHashMap<DiscoverActivity.QueryType, ArrayList<? extends MediaBasic>> data;
    private WeakHashMap<DiscoverActivity.QueryType, int[]> dataPages;
    private int count = 0;

    SplashPresenter(SplashContract.View view) {
        this.view = view;
        data = new WeakHashMap<>();
        dataPages = new WeakHashMap<>();
    }

    @Override
    public void start() {

    }

    @Override
    public void getInitialData(String ISO639_1_language, int page, String ISO3166_1_region) {
        for (DiscoverActivity.QueryType queryType : DiscoverActivity.QueryType.values()) {
            switch (queryType) {
                case NOW_PLAYING:
                    getResults(getNowPlayingObservable(ISO639_1_language, page, ISO3166_1_region), queryType);
                    break;
                case UPCOMING:
                    getResults(getUpcomingObservable(ISO639_1_language, page, ISO3166_1_region), queryType);
                    break;
                case POPULAR:
                    getResults(getPopularObservable(ISO639_1_language, page, ISO3166_1_region), queryType);
                    break;
                case TOP_RATED:
                    getResults(getTopRatedObservable(ISO639_1_language, page, ISO3166_1_region), queryType);
                    break;
            }
        }
    }

    private void getResults(Observable<MiscellaneousResults<BasicMovieInfo>> observable,
                            DiscoverActivity.QueryType queryType) {
        observable.subscribeOn(Schedulers.io());
        observable.observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(basicMovieInfos -> {
            data.put(queryType, new ArrayList<>(basicMovieInfos.getResults()));
            dataPages.put(queryType, new int[]{basicMovieInfos.getTotalPages(), basicMovieInfos.getTotalResults()});

            count++;
            if (count == DiscoverActivity.QueryType.values().length) {
                //To-Do
                view.setData(data, dataPages);
                view.stopSplash();
            }
        }, throwable -> {
            throwable.printStackTrace();
            if (throwable instanceof NoNetworkException) {
                //To-Do
            } else {
                //To-Do
            }
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
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    private Observable<MiscellaneousResults<BasicMovieInfo>> getPopularObservable(
            String ISO639_1_language,
            int page,
            String ISO3166_1_region) {
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        return tmdb.moviesService().getPopular(ISO639_1_language, page, ISO3166_1_region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }
}
