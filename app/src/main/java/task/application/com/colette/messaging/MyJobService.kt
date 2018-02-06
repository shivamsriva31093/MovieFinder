package task.application.com.colette.messaging

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService


/**
 * Created by sHIVAM on 2/3/2018.
 */

class MyJobService : JobService() {

    override fun onStopJob(job: JobParameters?): Boolean = false

    override fun onStartJob(job: JobParameters?): Boolean {


        return false
    }
}