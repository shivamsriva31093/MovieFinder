package task.application.com.moviefinder.ui.splash;

import com.androidtmdbwrapper.model.mediadetails.MediaBasic;

import java.util.ArrayList;
import java.util.WeakHashMap;

import task.application.com.moviefinder.ui.base.BasePresenter;
import task.application.com.moviefinder.ui.base.BaseView;
import task.application.com.moviefinder.ui.discover.DiscoverActivity;

/**
 * Created by sHIVAM on 11/3/2017.
 */

public interface SplashContract {
    public interface View extends BaseView<Presenter> {
        void setData(WeakHashMap<DiscoverActivity.QueryType, ArrayList<? extends MediaBasic>> data, WeakHashMap<DiscoverActivity.QueryType, int[]> pages);

        void stopSplash();
    }

    public interface Presenter extends BasePresenter {
        void getInitialData(String ISO639_1_language, int page, String ISO3166_1_region);
    }
}
