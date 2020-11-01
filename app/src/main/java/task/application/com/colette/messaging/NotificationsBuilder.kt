package task.application.com.colette.messaging

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.androidtmdbwrapper.model.movies.MovieInfo
import com.squareup.picasso.Picasso
import task.application.com.colette.R
import task.application.com.colette.messaging.notifications.LatestTrailer
import task.application.com.colette.messaging.notifications.MovieOfTheDay
import task.application.com.colette.messaging.notifications.PopularThisWeek
import task.application.com.colette.messaging.notifications.UpdateAvailable

/**
 * Created by sHIVAM on 1/28/2018.
 */
internal interface NotificationBuilder {
    fun build(context: Context, manager: NotificationManager, item: PushNotificationItem, id: Int): Notification?
}

internal class DefaultNotificationBuilder : NotificationBuilder {

    override fun build(context: Context, manager: NotificationManager, item: PushNotificationItem, id: Int): Notification? {
        return when (item.channel()) {
            is PushNotificationChannel.PopularMoviesNotification -> handleMediaNotifications(context, manager, item, id)
            is PushNotificationChannel.DailyMovieNotification -> handleMediaNotifications(context, manager, item, id)
            is PushNotificationChannel.LatestTrailerNotification -> handleMediaNotifications(context, manager, item, id)
            is PushNotificationChannel.UpdateAvailableNotification -> handleGenericNotifications(context, manager, item, id)
            is PushNotificationChannel.Empty -> null
        }
    }

    private fun handleGenericNotifications(context: Context, manager: NotificationManager, item: PushNotificationItem, id: Int): Notification? {
        val builder = NotificationCompat.Builder(context, item.channel().channelId)
                .setSmallIcon(item.smallIcon())
                .setContentTitle(item.title())
                .setLargeIcon(ContextCompat.getDrawable(context, R.mipmap.ic_launcher)?.toBitmap())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(item.pendingIntent())
                .setStyle(NotificationCompat.BigTextStyle().bigText(item.message()))
        when (item) {
            is UpdateAvailable -> builder.setGroupSummary(true).setGroup(UpdateAvailable.KEY_NOTIFICATION_GROUP)
        }
        manager.notify(id, builder.build())
        return builder.build()

    }

    private fun handleMediaNotifications(context: Context, manager: NotificationManager, item: PushNotificationItem, id: Int): Notification? {
        var movieData: MovieInfo = MovieInfo()
        if (item is PopularThisWeek || item is MovieOfTheDay || item is LatestTrailer) {
            movieData = item.data() as MovieInfo
        }

        /**
         * Accordingly handle the notification to show based on TV or Movie. Adding in TO-DO
         *
         * **/

        val builder = NotificationCompat.Builder(context, item.channel().channelId)
                .setSmallIcon(item.smallIcon())
                .setContentTitle(item.title())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(movieData.title)
                .setContentIntent(item.pendingIntent())

        try {
            builder.setLargeIcon(
                    Picasso.with(context)
                            .load("https://image.tmdb.org/t/p/original" + movieData.backdropPath)
                            .get()
            )
            builder.setStyle(
                    NotificationCompat
                            .BigPictureStyle()
//                            .bigPicture(AppCompatResources.getDrawable(context, R.drawable.the_post)?.toBitmap())
                            .bigPicture(
                                    Picasso.with(context)
                                            .load("https://image.tmdb.org/t/p/original" + movieData.posterPath)
                                            .get()
                            )
                            .bigLargeIcon(null)
            )
            when (item) {
                is PopularThisWeek -> builder.setGroupSummary(true).setGroup(PopularThisWeek.KEY_NOTIFICATION_GROUP)
                is MovieOfTheDay -> builder.setGroupSummary(true).setGroup(MovieOfTheDay.KEY_NOTIFICATION_GROUP)
                is LatestTrailer -> builder.setGroupSummary(true).setGroup(LatestTrailer.KEY_NOTIFICATION_GROUP)
            }
            manager.notify(id, builder.build())
            return builder.build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return bitmap
    }

    val width = if (bounds.isEmpty) intrinsicWidth else bounds.width()
    val height = if (bounds.isEmpty) intrinsicHeight else bounds.height()

    return Bitmap.createBitmap(width.nonZero(), height.nonZero(), Bitmap.Config.ARGB_8888).also {
        val canvas = Canvas(it)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
    }
}

private fun Int.nonZero() = if (this <= 0) 1 else this