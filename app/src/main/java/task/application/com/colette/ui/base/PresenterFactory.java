package task.application.com.colette.ui.base;

import android.support.annotation.NonNull;

/**
 * Created by sHIVAM on 9/8/2017.
 */

public interface PresenterFactory<T> {
    @NonNull
    T createPresenter();
}
