package task.application.com.moviefinder.ui.search;

import java.util.ArrayList;

import info.movito.themoviedbapi.model.MovieDb;
import task.application.com.moviefinder.ui.base.BasePresenter;
import task.application.com.moviefinder.ui.base.BaseView;

/**
 * Created by sHIVAM on 1/30/2017.
 */

public interface SearchContract {

    interface View extends BaseView<Presenter> {
        void showSearchListUi(ArrayList<MovieDb> movieDbs);
        void showLoadingResultsError();
        void showLoadingIndicator(boolean flag);
        void showNoResults();
    }

    interface Presenter extends BasePresenter {
        void searchByKeyword(String keyword);
        void setFilteringType(String filteringType);
        String getFilteringType();
    }
}
