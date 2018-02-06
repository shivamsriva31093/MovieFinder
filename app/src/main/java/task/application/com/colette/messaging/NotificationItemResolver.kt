package task.application.com.colette.messaging

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.androidtmdbwrapper.model.mediadetails.MediaBasic
import task.application.com.colette.R
import task.application.com.colette.messaging.notifications.PopularThisWeek
import task.application.com.colette.ui.splash.SplashActivity

/**
 * Created by sHIVAM on 1/27/2018.
 */

internal interface NotificationItemResolver {
    fun resolve(context: Context, data: Map<String, String>?, mediaInfo: MediaBasic): PushNotificationItem
}

internal class PushNotificationItemResolver : NotificationItemResolver {
    override fun resolve(context: Context, data: Map<String, String>?, mediaInfo: MediaBasic): PushNotificationItem {
        Log.d("test", "channel is " + data?.get("id"))
        return when (data?.get("id")) {
            "popularMovies" -> PopularThisWeek(context, mediaInfo)
            else -> object : PushNotificationItem {
                override fun channel(): PushNotificationChannel = PushNotificationChannel.Empty()

                override fun title(): String = "Colette"

                override fun message(): String = "Hello from the other side!"

                override fun smallIcon(): Int = R.drawable.imdb_icon

                override fun data(): MediaBasic = MediaBasic()

                override fun pendingIntent(): PendingIntent {
                    val intent = Intent(context, SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    val reqId = System.currentTimeMillis().toInt()
                    return PendingIntent.getActivity(context, reqId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }

            }
        }
    }
}