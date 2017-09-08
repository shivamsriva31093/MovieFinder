package task.application.com.moviefinder.ui.base;

import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

/**
 * Created by sHIVAM on 9/8/2017.
 */

public class PresenterCache<T> {
    private static PresenterCache instance = null;

    private SimpleArrayMap<String, T> map;

    private PresenterCache() {
    }

    public static PresenterCache getInstance() {
        if (instance == null)
            instance = new PresenterCache();
        return instance;
    }

    public final T getPresenter(String tag, PresenterFactory<T> factory) {
        if (map == null)
            map = new SimpleArrayMap<>();
        T presenter = null;
        try {
            presenter = (T) map.get(tag);
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
