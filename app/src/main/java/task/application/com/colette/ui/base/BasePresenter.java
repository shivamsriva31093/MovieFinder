package task.application.com.colette.ui.base;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by sHIVAM on 1/30/2017.
 */

public interface BasePresenter {
    CompositeDisposable disposable = new CompositeDisposable();
    void start();
}
