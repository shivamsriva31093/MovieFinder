package task.application.com.moviefinder.ui.searchlist;

import java.util.ArrayList;

import info.movito.themoviedbapi.model.MovieDb;
import task.application.com.moviefinder.ui.base.BasePresenter;
import task.application.com.moviefinder.ui.base.BaseView;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public interface SearchListContract  {
    interface View extends BaseView<Presenter> {
        void showSearchList(ArrayList<MovieDb> movieDbs);

        void showItemDetailsUi(MovieDb item);
        void showLoadingIndicator(boolean show);
        void showNoResults();
        void showLoadingResultsError();
    }

    interface Presenter extends BasePresenter {
        void searchByKeyword(String keyword, String searchType);
        void clearRecyclerView();

        void onSearchItemClick(MovieDb Item);
        void setFilteringType(String filteringType);
        String getFilteringType();
    }
}
