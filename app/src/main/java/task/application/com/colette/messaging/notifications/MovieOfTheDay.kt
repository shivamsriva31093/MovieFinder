package task.application.com.colette.messaging.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.androidtmdbwrapper.enums.MediaType
import com.androidtmdbwrapper.model.mediadetails.MediaBasic
import task.application.com.colette.R
import task.application.com.colette.messaging.PushNotificationChannel
import task.application.com.colette.messaging.PushNotificationItem
import task.application.com.colette.ui.itemdetail.SearchItemDetailActivity

/**
 * Created by sHIVAM on 1/28/2018.
 */

internal class MovieOfTheDay(private val context: Context, private val content: MediaBasic) : PushNotificationItem {
    override fun data(): MediaBasic {
        return content
    }

    override fun channel() = PushNotificationChannel.DailyMovieNotification()

    override fun smallIcon(): Int = R.mipmap.ic_launcher

    override fun title(): String = "Movie for the day!"

    override fun message(): String = "Don't forget to watch today."

    override fun pendingIntent(): PendingIntent {
        val resultIntent = Intent(context, SearchItemDetailActivity::class.java)
        val bundle = Bundle()
        val item = MediaBasic()
        item.id = content.id
        bundle.putParcelable("clickedItem", item)
        bundle.putSerializable("filtering_type", MediaType.MOVIES)
        resultIntent.putExtra(SearchItemDetailActivity.SEARCH_ITEM, bundle)
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val requestID = System.currentTimeMillis().toInt()
        return PendingIntent.getActivity(context, requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        const val KEY_NOTIFICATION_GROUP = "daily_popular"
    }
}