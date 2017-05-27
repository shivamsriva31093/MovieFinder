package task.application.com.moviefinder.ui.searchlist;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;

import java.util.ArrayList;

import task.application.com.moviefinder.ui.base.BasePresenter;
import task.application.com.moviefinder.ui.base.BaseView;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public interface SearchListContract  {
    interface View extends BaseView<Presenter> {
        void showSearchList(ArrayList<? extends MediaBasic> movieDbs);

        <T extends MediaBasic> Void showItemDetailsUi(T Item);
        void showLoadingIndicator(boolean show);
        void showNoResults();
        void showLoadingResultsError();
    }

    interface Presenter extends BasePresenter {
        void searchByKeyword(String keyword);
        void clearRecyclerView();

        <T extends MediaBasic> Void onSearchItemClick(T Item);

        void setFilteringType(MediaType filteringType);

        MediaType getFilteringType();
    }
}
