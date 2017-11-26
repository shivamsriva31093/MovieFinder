package task.application.com.colette.ui.itemdetail;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.OmdbMovieDetails;
import com.androidtmdbwrapper.model.core.BaseMediaData;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;

import task.application.com.colette.ui.base.BasePresenter;
import task.application.com.colette.ui.base.BaseView;

/**
 * Created by sHIVAM on 2/11/2017.
 */

public interface SearchItemDetailContract {
    interface View extends BaseView<Presenter> {
        void showLoadingIndicator(boolean show);
        void showLoadingError();
        void showUi(MediaBasic item);

        void setFavorite(boolean status);
        void showRatingsUi(OmdbMovieDetails data);

        void showRatingsViewLoadingIndicator(boolean show);

        void showTestToast(String msg);
    }

    interface Presenter extends BasePresenter {
        void getMovieDetails(BaseMediaData clickedItem);
        void setFilteringType(MediaType filteringType);

        void checkMediaInDB(MediaBasic item);

        void addMediaToFavorites(MediaBasic item);

        void removeMediaFromFavorites(MediaBasic item);
        MediaType getFilteringType();

        void destroy();
    }
}
