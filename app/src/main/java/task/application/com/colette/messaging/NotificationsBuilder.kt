package task.application.com.colette.messaging

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.app.NotificationCompat
import com.squareup.picasso.Picasso

/**
 * Created by sHIVAM on 1/28/2018.
 */
internal interface NotificationBuilder {
    fun build(context: Context, manager: NotificationManager, item: PushNotificationItem, id: Int): Notification
}

internal class DefaultNotificationBuilder : NotificationBuilder {

    override fun build(context: Context, manager: NotificationManager, item: PushNotificationItem, id: Int): Notification {
        val builder = NotificationCompat.Builder(context, item.channel().channelId)
                .setSmallIcon(item.smallIcon())
                .setContentTitle(item.title())
                .setContentText(item.message())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(item.pendingIntent())
                .setStyle(
                        NotificationCompat
                                .BigPictureStyle()
//                            .bigPicture(AppCompatResources.getDrawable(context, R.drawable.the_post)?.toBitmap())
                                .bigPicture(Picasso.with(context).load("https://image.tmdb.org/t/p/original" + item.data().backdropPath).get())
                )
        manager.notify(id, builder.build())
        return builder.build()
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