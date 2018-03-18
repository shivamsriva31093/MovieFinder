package task.application.com.colette.messaging

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import com.androidtmdbwrapper.model.mediadetails.MediaBasic

/**
 * Created by sHIVAM on 1/28/2018.
 */

internal class PushNotification constructor(private val notificationManager: NotificationManager,
                                            private val resolver: NotificationItemResolver,
                                            private val notificationBuilder: NotificationBuilder?) {

    fun show(context: Context, data: Map<String, String>?, mediaInfo: MediaBasic) {
        val id = data?.get("id")?.hashCode() ?: 0
        val notification = resolver.resolve(context, data, mediaInfo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(context, notification.channel())
        }
        Thread({
            notificationBuilder?.build(context, notificationManager, notification, id)
        }).start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(context: Context, channel: PushNotificationChannel) {
        val channelTitle = context.getString(channel.titleRes)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channel.channelId, channelTitle, importance)
        notificationChannel.setShowBadge(true)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)
    }
}