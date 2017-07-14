package task.application.com.moviefinder.ui.discover;

import com.androidtmdbwrapper.model.mediadetails.MediaBasic;

import java.util.List;

import task.application.com.moviefinder.ui.base.BasePresenter;
import task.application.com.moviefinder.ui.base.BaseView;

/**
 * Created by sHIVAM on 7/12/2017.
 */

public class DiscoverContract {
    public interface View extends BaseView<Presenter> {
        void updateNewItems(List<? extends MediaBasic> data);

        void showResultList(List<? extends MediaBasic> result, int totalPages, int totalResults);

        void showLoadingIndicator(boolean flag);

        void showNoResults();
    }

    public interface Presenter extends BasePresenter {
        void makeQuery(String ISO639_1_language, int page, String ISO3166_1_region);

        void loadNextPage(int page);

        void setQueryType(DiscoverActivity.QueryType queryType);

        DiscoverActivity.QueryType getQueryType();
    }
}
