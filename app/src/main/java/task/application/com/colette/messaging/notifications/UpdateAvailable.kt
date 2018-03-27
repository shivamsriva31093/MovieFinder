package task.application.com.colette.messaging.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import com.androidtmdbwrapper.model.mediadetails.MediaBasic
import task.application.com.colette.R
import task.application.com.colette.messaging.PushNotificationChannel
import task.application.com.colette.messaging.PushNotificationItem

internal class UpdateAvailable(val context: Context) : PushNotificationItem {

    override fun data(): MediaBasic {
        return MediaBasic()
    }

    override fun channel() = PushNotificationChannel.UpdateAvailableNotification()

    override fun smallIcon(): Int = R.drawable.rotate

    override fun title(): String = context.getString(R.string.update_notification)

    override fun message(): String = "Upgrade to latest version of Colette to enjoy seamless app experience."

    override fun pendingIntent(): PendingIntent {
        val resultIntent = Intent(ACTION_VIEW)
        resultIntent.data = Uri.parse(context.getString(R.string.app_link))
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val requestID = System.currentTimeMillis().toInt()
        return PendingIntent.getActivity(context, requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        val KEY_NOTIFICATION_GROUP = "update_available"
    }
}
