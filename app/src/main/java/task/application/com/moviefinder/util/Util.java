package task.application.com.moviefinder.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by sHIVAM on 12/21/2016.
 */

public class Util {
    public Util(){}

    public static @NonNull <T> T checkNotNull(final T reference) {
        if( reference == null)
            throw new NullPointerException();
        return reference;
    }

    public static @NonNull <T> T checkNotNull(final T reference, final Object message) {
        if( reference == null)
            throw new NullPointerException(String.valueOf(message));
        return reference;
    }

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int containerId, String tag) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(containerId, fragment, tag);
        transaction.commit();
    }

    public static void replaceFragmentFromActivity(@NonNull FragmentManager fragmentManager,
                                                   @NonNull Fragment fragment, int containerId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }
}
