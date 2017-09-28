package task.application.com.moviefinder.remote.tmdb;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sHIVAM on 9/28/2017.
 */

public class LiveNetworkMonitor implements NetworkMonitor {

    private final Context appContext;

    public LiveNetworkMonitor(Context context) {
        appContext = context;
    }

    @Override
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
