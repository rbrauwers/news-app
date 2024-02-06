package com.rbrauwers.newsapp.photo

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.rbrauwers.newsapp.R
import com.rbrauwers.newsapp.SERVICE_CHANNEL_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlin.math.roundToLong

@HiltWorker
class PhotoWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val photoId by lazy {
        inputData.getInt(KEY_PHOTO_ID, -1)
    }

    private val specs = listOf(
        PhotoSpecs(initialDuration = 4000, uploadDuration = 10000, finalDuration = 4000),
        PhotoSpecs(initialDuration = 4000, uploadDuration = 20000, finalDuration = 4000),
        PhotoSpecs(initialDuration = 2000, uploadDuration = 7000, finalDuration = 4000)
    )

    override suspend fun doWork(): Result {
        upload()
        return Result.success()
    }

    private suspend fun upload() {
        val specs = specs.getOrNull(photoId - 1) ?: return
        val step = 500L

        setForeground(createForegroundInfo("Initializing"))
        delay(specs.initialDuration)

        for (progress in 0..specs.uploadDuration step step) {
            val relativeProgress = ((progress.toDouble() / specs.uploadDuration.toDouble()) * 100.0).roundToLong()
            //println("progress: $progress")
            //println("[$photoId] relativeProgress: $relativeProgress")
            setForeground(createForegroundInfo("Uploading $relativeProgress%"))
            delay(step)
        }

        setForeground(createForegroundInfo("Finishing"))
        delay(specs.finalDuration)
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val title = "Processing photo $photoId"

        // This PendingIntent can be used to cancel the worker
        val cancelIntent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)

        val notification = NotificationCompat.Builder(applicationContext, SERVICE_CHANNEL_ID)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(R.drawable.baseline_cloud_upload_24)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            .addAction(android.R.drawable.ic_delete, "Cancel", cancelIntent)
            .build()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(photoId, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            ForegroundInfo(photoId, notification)
        }
    }

    private data class PhotoSpecs(
        val initialDuration: Long,
        val uploadDuration: Long,
        val finalDuration: Long
    )

    companion object {
        const val KEY_PHOTO_ID = "photoId"

        fun enqueue(context: Context, photoId: Int) {
            val workRequest = OneTimeWorkRequestBuilder<PhotoWorker>().setInputData(
                workDataOf(
                    Pair(KEY_PHOTO_ID, photoId)
                )
            ).build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }

    }

}