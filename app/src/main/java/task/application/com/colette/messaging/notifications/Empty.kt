package task.application.com.colette.messaging.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.androidtmdbwrapper.model.mediadetails.MediaBasic
import task.application.com.colette.R
import task.application.com.colette.messaging.PushNotificationChannel
import task.application.com.colette.messaging.PushNotificationItem
import task.application.com.colette.ui.splash.SplashActivity

/**
 * Created by sHIVAM on 3/25/2018.
 */

internal class Empty(val context: Context) : PushNotificationItem {
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