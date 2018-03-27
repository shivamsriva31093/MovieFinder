package task.application.com.colette.messaging

import android.support.annotation.StringRes
import task.application.com.colette.R

/**
 * Created by sHIVAM on 1/27/2018.
 */
internal sealed class PushNotificationChannel(val channelId: String, @StringRes val titleRes: Int) {
    class PopularMoviesNotification : PushNotificationChannel("popularMovies", R.string.popularMoviesNotifications)
    class LatestTrailerNotification : PushNotificationChannel("latestTrailer", R.string.latest_trailer_notification)
    class DailyMovieNotification : PushNotificationChannel("dailyMovie", R.string.featured_today_notification)
    class UpdateAvailableNotification : PushNotificationChannel("updateAvailable", R.string.update_notification)
    class Empty : PushNotificationChannel("empty", R.string.genericNotifications)
}