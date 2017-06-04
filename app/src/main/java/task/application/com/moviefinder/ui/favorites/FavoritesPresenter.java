package task.application.com.moviefinder.ui.favorites;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import task.application.com.moviefinder.model.local.realm.datamodels.MediaItem;

/**
 * Created by sHIVAM on 6/4/2017.
 */

public class FavoritesPresenter implements FavoritesMediaContract.Presenter {

    private FavoritesMediaContract.View view;
    private Realm realm;

    FavoritesPresenter(FavoritesMediaContract.View view) {
        this.view = view;
        view.setPresenter(this);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {
        realm.removeAllChangeListeners();
        realm.close();
        realm = null;
    }

    @Override
    public void fetchDataFromRealm(String FILTER) {
        RealmResults<MediaItem> res = realm.where(MediaItem.class)
                .equalTo("mediaType", FILTER)
                .findAllSortedAsync("createdAt", Sort.DESCENDING);
        res.addChangeListener(mediaItems -> {
            if (mediaItems.isValid()) {
                view.updateListAdapter(mediaItems);
            }
        });
    }

    @Override
    public void showMediaDetails(MediaItem item) {
        view.showItemDetailsUI(item);
    }
}
