package task.application.com.colette.messaging

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.app.NotificationCompat
import com.androidtmdbwrapper.model.movies.MovieInfo
import com.squareup.picasso.Picasso
import task.application.com.colette.messaging.notifications.LatestTrailer
import task.application.com.colette.messaging.notifications.MovieOfTheDay
import task.application.com.colette.messaging.notifications.PopularThisWeek

/**
 * Created by sHIVAM on 1/28/2018.
 */
internal interface NotificationBuilder {
    fun build(context: Context, manager: NotificationManager, item: PushNotificationItem, id: Int): Notification?
}

internal class DefaultNotificationBuilder : NotificationBuilder {

    override fun build(context: Context, manager: NotificationManager, item: PushNotificationItem, id: Int): Notification? {
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
                .setContentText(item.message())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(item.pendingIntent())

        try {
            builder.setStyle(
                    NotificationCompat
                            .BigPictureStyle()
//                            .bigPicture(AppCompatResources.getDrawable(context, R.drawable.the_post)?.toBitmap())
                            .bigPicture(
                                    Picasso.with(context)
                                            .load("https://image.tmdb.org/t/p/original" + movieData.backdropPath)
                                            .get()
                            )
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