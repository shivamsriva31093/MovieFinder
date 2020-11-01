package task.application.com.colette.ui.discover;

import androidx.collection.SimpleArrayMap;
import android.util.Log;

/**
 * Created by sHIVAM on 7/16/2017.
 */

public class DiscoverPresenterCache {
    private static DiscoverPresenterCache instance = null;

    private SimpleArrayMap<String, DiscoverContract.Presenter> map;

    private DiscoverPresenterCache() {
    }

    public static DiscoverPresenterCache getInstance() {
        if (instance == null)
            instance = new DiscoverPresenterCache();
        return instance;
    }

    protected final DiscoverPresenter getPresenter(String tag, DiscoverPresenterFactory factory) {
        if (map == null)
            map = new SimpleArrayMap<>();
        DiscoverPresenter presenter = null;
        try {
            presenter = (DiscoverPresenter) map.get(tag);
        } catch (ClassCastException ex) {
            Log.w("DiscoverActivity", "Duplicate presenter " +
                    "tag : " + tag);
        }
        if (presenter == null) {
            presenter = factory.createPresenter();
            map.put(tag, presenter);
        }
        return presenter;
    }

    public final void removePresenter(String tag) {
        if (map != null)
            map.remove(tag);
    }
}
