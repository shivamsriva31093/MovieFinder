package task.application.com.moviefinder.ui.advancedsearch.searchlist;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;

import java.util.ArrayList;
import java.util.List;

import task.application.com.moviefinder.ui.base.BasePresenter;
import task.application.com.moviefinder.ui.base.BaseView;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public interface SearchListContract  {
    interface View extends BaseView<Presenter> {
        void showSearchList(ArrayList<? extends MediaBasic> movieDbs, int totalResults, int totalPages);

        void updateNewItems(List<? extends MediaBasic> data);
        void setImdbRatings(String rating, int pos);
        <T extends MediaBasic> Void showItemDetailsUi(T Item);
        void showLoadingIndicator(boolean show);
        void showNoResults();

        void setEndlessScrollLoading(boolean status);
        void showLoadingResultsError();
    }

    interface Presenter extends BasePresenter {
        void searchByKeyword(String keyword);
        void clearRecyclerView();

        void searchByKeyword(String keyword, int page);
        <T extends MediaBasic> Void onSearchItemClick(T Item);
        void getRatings(MediaType filter, MediaBasic item, int pos);
        void setFilteringType(MediaType filteringType);

        String getQuery();

        void setQuery(String query);
        MediaType getFilteringType();
    }
}
