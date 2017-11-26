package task.application.com.colette.ui.appsearch;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.core.BaseMediaData;

import java.util.List;

import task.application.com.colette.ui.base.BasePresenter;
import task.application.com.colette.ui.base.BaseView;

/**
 * Created by sHIVAM on 7/22/2017.
 */

public class AppSearchContract {
    public interface View extends BaseView<Presenter> {
        void updateNewItems(List<? extends BaseMediaData> data);

        void showResultList(List<? extends BaseMediaData> result, int totalPages, int totalResults);

        void showLoadingIndicator(boolean flag);

        void showNoResults();

        void setEndlessScrollLoading(boolean status);
    }

    public interface Presenter extends BasePresenter {
        void loadNextPage(int page);

        void setQueryType(MediaType queryType);

        MediaType getQueryType();

        String getViewID();

        void searchQuery(String query);

        String getQuery();

        void setQuery(String query);
    }
}
