package task.application.com.colette.messaging

import android.support.annotation.StringRes
import task.application.com.colette.R

/**
 * Created by sHIVAM on 1/27/2018.
 */
internal sealed class PushNotificationChannel(val channelId: String, @StringRes val titleRes: Int) {
    class PopularMoviesNotification : PushNotificationChannel("popularMovies", R.string.popularMoviesNotifications)

    class Empty : PushNotificationChannel("empty", R.string.genericNotifications)
}