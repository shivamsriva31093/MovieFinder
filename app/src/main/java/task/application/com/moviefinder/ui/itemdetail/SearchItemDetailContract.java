package task.application.com.moviefinder.ui.itemdetail;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;
import task.application.com.moviefinder.ui.base.BasePresenter;
import task.application.com.moviefinder.ui.base.BaseView;

/**
 * Created by sHIVAM on 2/11/2017.
 */

public interface SearchItemDetailContract {
    interface View extends BaseView<Presenter> {
        void showLoadingIndicator(boolean show);

        void showLoadingError();

        void prepareUI(List<PersonCast> cast);

        void showUi(MovieDb item);
    }

    interface Presenter extends BasePresenter {
        void getMovieDetails(MovieDb clickedItem);

        void setUpUiForItem(MovieDb item);
    }
}
