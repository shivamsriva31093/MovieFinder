package task.application.com.moviefinder.ui.favorites;

import io.realm.RealmResults;
import task.application.com.moviefinder.model.local.realm.datamodels.MediaItem;
import task.application.com.moviefinder.ui.base.BasePresenter;
import task.application.com.moviefinder.ui.base.BaseView;

/**
 * Created by sHIVAM on 6/4/2017.
 */

public interface FavoritesMediaContract {
    interface View extends BaseView<Presenter> {
        void updateListAdapter(RealmResults<MediaItem> res);
    }

    interface Presenter extends BasePresenter {
        void destroy();

        void fetchDataFromRealm(final String FILTER);

    }
}
