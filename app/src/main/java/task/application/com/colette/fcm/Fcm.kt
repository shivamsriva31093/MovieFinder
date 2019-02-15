package task.application.com.colette.fcm

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import task.application.com.colette.util.LogUtils.*
import java.util.*

/**
 * Created by sHIVAM on 2/10/2018.
 */
class Fcm {

    companion object {
        val TAG: String = makeLogTag("FcmUtils")

        fun register( deviceId: String) {
            val fcmIdRef = FirebaseDatabase.getInstance().reference.child("fcmIds")
            fcmIdRef.updateChildren(
                    mapOf(Pair(UUID.randomUUID().toString(), deviceId)
                    ),
                    { databaseError, _ ->
                        if (databaseError != null)
                            LOGE(TAG, "Unable to register user with server." + databaseError.message)
                        else
                            LOGI(TAG, "Successfully registered user with server.")
                    })
        }
    }


}