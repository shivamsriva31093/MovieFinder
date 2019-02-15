package task.application.com.colette

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


import task.application.com.colette.fcm.Fcm

import task.application.com.colette.util.LogUtils.LOGD
import task.application.com.colette.util.LogUtils.makeLogTag

/**
 * Created by sHIVAM on 2/20/2018.
 */

class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {


    override fun onTokenRefresh() {
        super.onTokenRefresh()
        LOGD(TAG, "On token refresh called")
        val token = FirebaseInstanceId.getInstance().token
        sendToServer(token ?: "")
    }

    private fun sendToServer(token: String) {
        LOGD(TAG, "Registering on the FCM server with FCM token: $token")
        Fcm.register( token)
    }

    companion object {
        private val TAG = makeLogTag(MyFirebaseInstanceIdService::class.java)
    }
}
