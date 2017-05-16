package task.application.com.moviefinder.ui.itemdetail;

import com.androidtmdbwrapper.model.mediadetails.MediaBasic;

import task.application.com.moviefinder.ui.base.BasePresenter;
import task.application.com.moviefinder.ui.base.BaseView;

/**
 * Created by sHIVAM on 2/11/2017.
 */

public interface SearchItemDetailContract {
    interface View extends BaseView<Presenter> {
        void showLoadingIndicator(boolean show);

        void showLoadingError();

        void showUi(SearchItemDetailPresenter.PackTmdbOmdbData item);
    }

    interface Presenter extends BasePresenter {
        void getMovieDetails(MediaBasic clickedItem);

        void setUpUiForItem(SearchItemDetailPresenter.PackTmdbOmdbData item);
    }
}
