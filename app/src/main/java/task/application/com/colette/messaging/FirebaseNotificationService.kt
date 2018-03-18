package task.application.com.colette.messaging

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import task.application.com.colette.util.LogUtils.LOGI
import task.application.com.colette.util.LogUtils.makeLogTag

/**
 * Created by sHIVAM on 1/25/2018.
 */

class FirebaseNotificationService : FirebaseMessagingService() {

    private var TAG = makeLogTag(FirebaseNotificationService::class.java)

    override fun onMessageReceived(p0: RemoteMessage?) {
//        android.os.Debug.waitForDebugger()
        super.onMessageReceived(p0)
        handleNow(p0?.data)
        LOGI(TAG, "message received " + p0?.data?.get("value"))
    }

    private fun handleNow(data: Map<String, String>?) {
        val intent = Intent()
        if (data != null && data.isNotEmpty() && data["value"] != null) {
            intent.putExtra("id", data["id"])
            intent.putExtra("value", data["value"])
            LOGI(TAG, "starting job service")
            MyJobService.enqueue(this, intent)
        }
    }
}