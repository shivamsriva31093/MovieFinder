package task.application.com.colette.messaging

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by sHIVAM on 1/25/2018.
 */

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val token = FirebaseInstanceId.getInstance().token
        Log.d("test", "Token received: " + if (token.isNullOrBlank()) "" else token)
    }
}