package task.application.com.colette.messaging

import android.content.Context
import com.androidtmdbwrapper.model.mediadetails.MediaBasic
import com.androidtmdbwrapper.model.movies.MovieInfo
import task.application.com.colette.messaging.notifications.*

/**
 * Created by sHIVAM on 1/27/2018.
 */

internal interface NotificationItemResolver {
    fun resolve(context: Context, data: Map<String, String>?, mediaInfo: MediaBasic): PushNotificationItem

    fun resolve(context: Context, data: Map<String, String>?): PushNotificationItem
}

internal class PushNotificationItemResolver : NotificationItemResolver {
    override fun resolve(context: Context, data: Map<String, String>?, mediaInfo: MediaBasic): PushNotificationItem {

        return when (data?.get("id")) {
            "popularMovies" -> PopularThisWeek(context, mediaInfo)
            "dailyMovie" -> MovieOfTheDay(context, mediaInfo)
            "latestTrailer" -> LatestTrailer(context, mediaInfo as MovieInfo)
            else -> Empty(context)
        }
    }

    override fun resolve(context: Context, data: Map<String, String>?): PushNotificationItem {
        return when (data?.get("id")) {
            "updateAvailable" -> UpdateAvailable(context)
            else -> Empty(context)
        }
    }
}