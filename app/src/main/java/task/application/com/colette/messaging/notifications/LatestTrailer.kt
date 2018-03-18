package task.application.com.colette.messaging.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.androidtmdbwrapper.model.mediadetails.MediaBasic
import com.androidtmdbwrapper.model.mediadetails.Video
import com.androidtmdbwrapper.model.movies.MovieInfo
import task.application.com.colette.R
import task.application.com.colette.messaging.PushNotificationChannel
import task.application.com.colette.messaging.PushNotificationItem
import java.util.*


/**
 * Created by sHIVAM on 1/28/2018.
 */

internal class LatestTrailer(private val context: Context, private val content: MovieInfo) : PushNotificationItem {
    override fun data(): MediaBasic {
        return content
    }

    override fun channel() = PushNotificationChannel.PopularMoviesNotification()

    override fun smallIcon(): Int = R.drawable.play

    override fun title(): String = "Trending today!"

    override fun message(): String = "Bet you didn't watch this awesome trailer!!"

    override fun pendingIntent(): PendingIntent {
        val resultIntent = startYouTubeIntent()
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val requestID = System.currentTimeMillis().toInt()
        return PendingIntent.getActivity(context, requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun startYouTubeIntent(): Intent {
        val id: String = getVideoUrl()

        val intent: Intent
        intent = if (id.length == 11) {
            // youtube video id
            Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://$id"))
        } else {
            // url to video
            Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
        }
        try {
            if (context.packageManager.getPackageInfo("com.google.android.youtube", 0) != null) {
                intent.`package` = "com.google.android.youtube"
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return intent

    }

    private fun getVideoUrl(): String {
        var list: MutableList<Video> = mutableListOf()
        try {
            list = content.videos.videos
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val trailerList = ArrayList<String>()
        for (res in list) {
            if (res.site == "YouTube")
                trailerList.add(res.key)
        }
        return if (trailerList.isEmpty()) "" else trailerList[0]
    }

    companion object {
        const val KEY_NOTIFICATION_GROUP = "latest_trailer"
    }
}