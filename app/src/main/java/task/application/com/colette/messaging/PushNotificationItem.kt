package task.application.com.colette.messaging

import android.app.PendingIntent
import com.androidtmdbwrapper.model.mediadetails.MediaBasic

/**
 * Created by sHIVAM on 1/27/2018.
 */

internal interface PushNotificationItem {
    fun channel(): PushNotificationChannel
    fun title(): String
    fun message(): String
    fun smallIcon(): Int
    fun pendingIntent(): PendingIntent
    fun data(): MediaBasic
}