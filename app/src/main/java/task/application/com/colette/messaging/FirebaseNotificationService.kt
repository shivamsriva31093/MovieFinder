package task.application.com.colette.messaging

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import task.application.com.colette.ApplicationClass
import task.application.com.colette.util.TmdbApi

/**
 * Created by sHIVAM on 1/25/2018.
 */

class FirebaseNotificationService : FirebaseMessagingService() {
    internal lateinit var pushNotification: PushNotification


    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        pushNotification = PushNotification(
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager,
                PushNotificationItemResolver(),
                DefaultNotificationBuilder()
        )
        sendNotification(p0?.data)
    }

    private fun sendNotification(data: Map<String, String>?) {
        val api = TmdbApi.getApiClient(ApplicationClass.API_KEY)
        if (data != null && data.isNotEmpty() && data["value"] != null) {
            Log.d("test", data["value"])
            api.moviesService().summary(data["value"]?.toInt(), null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .doOnError(Consumer { t ->
                        t.printStackTrace()
                    })
                    .subscribe { movieInfo ->
                        Log.d("test_not", movieInfo.imdbId)
                        pushNotification.show(applicationContext, data, movieInfo)
                    }
        }

    }
}