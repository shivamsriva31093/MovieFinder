package task.application.com.colette.messaging

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import com.androidtmdbwrapper.enums.AppendToResponseItem
import com.androidtmdbwrapper.model.core.AppendToResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import task.application.com.colette.ApplicationClass
import task.application.com.colette.util.LogUtils.LOGI
import task.application.com.colette.util.LogUtils.makeLogTag
import task.application.com.colette.util.TmdbApi


/**
 * Created by sHIVAM on 2/3/2018.
 */

class MyJobService : JobIntentService() {

    private lateinit var pushNotification: PushNotification

    companion object {
        val TAG = makeLogTag(MyJobService::class.java)
        val JOB_ID = 3589
        const val WORK_ACTION = ".DOWNLOAD_IMAGE"


        fun enqueue(context: Context, work: Intent) {
            LOGI(TAG, "start notification building")
//

            enqueueWork(context, MyJobService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        LOGI(TAG, "start notification building")
//        android.os.Debug.waitForDebugger()
        pushNotification = PushNotification(
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager,
                PushNotificationItemResolver(),
                DefaultNotificationBuilder()
        )
        val movieId = intent.getStringExtra("value")
        val notificationType = intent.getStringExtra("id")
        val data: MutableMap<String, String> = hashMapOf(Pair("id", notificationType), Pair("value", movieId!!))

        if (notificationType == "updateAvailable") {
            pushNotification.show(applicationContext, data)
        } else {
            buildMediaNotification(movieId, notificationType, data)
        }
    }

    private fun buildMediaNotification(movieId: String?, notificationType: String?, data: MutableMap<String, String>) {
        val api = TmdbApi.getApiClient(ApplicationClass.API_KEY)
        var atr: AppendToResponse? = null;
        if (notificationType != null && notificationType == "latestTrailer") {
            atr = AppendToResponse(AppendToResponseItem.CREDITS,
                    AppendToResponseItem.VIDEOS)
        }
        api.moviesService().summary(movieId?.toInt(), atr)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ movieInfo ->
                    pushNotification.show(applicationContext, data, movieInfo)
                }, { error ->
                    error.printStackTrace()
                }
                )
    }


}