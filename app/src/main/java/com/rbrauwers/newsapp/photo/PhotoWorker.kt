package com.rbrauwers.newsapp.photo

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
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

    override suspend fun doWork(): Result {
        upload()
        return Result.success()
    }

    private suspend fun upload() {
        val specs = PhotoWorker.specs.firstOrNull { it.photoId == photoId } ?: return
        val step = 300L

        setForeground(createForegroundInfo("Initializing"))
        setProgress(ProgressData.Initializing(photoId = photoId))
        delay(specs.initialDuration)

        for (progress in 0..specs.uploadDuration step step) {
            val relativeProgress =
                ((progress.toDouble() / specs.uploadDuration.toDouble()) * 100.0).roundToLong()
            setForeground(createForegroundInfo("Uploading $relativeProgress%"))
            setProgress(ProgressData.InProgress(photoId = photoId, progress = relativeProgress))
            delay(step)
        }

        setForeground(createForegroundInfo("Finishing"))
        setProgress(ProgressData.Finished(photoId = photoId))
        delay(specs.finalDuration)
    }

    private suspend fun setProgress(progressData: ProgressData) {
        setProgress(progressData.toWorkData())
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
        val photoId: Int,
        val initialDuration: Long,
        val uploadDuration: Long,
        val finalDuration: Long
    )

    sealed interface ProgressData {
        val photoId: Int

        data class Blocked(override  val photoId: Int): ProgressData
        data class Cancelled(override  val photoId: Int): ProgressData
        data class Enqueued(override  val photoId: Int): ProgressData
        data class Failed(override  val photoId: Int): ProgressData
        data class Initializing(override val photoId: Int) : ProgressData
        data class Finished(override val photoId: Int) : ProgressData
        data class InProgress(override val photoId: Int, val progress: Long) : ProgressData
    }

    companion object {
        const val KEY_PHOTO_ID = "photoId"
        const val KEY_PROGRESS = "progress"
        const val KEY_PROGRESS_STATE = "progressState"

        private val specs = listOf(
            PhotoSpecs(
                photoId = 1,
                initialDuration = 4000,
                uploadDuration = 10000,
                finalDuration = 4000
            ),
            PhotoSpecs(
                photoId = 2,
                initialDuration = 4000,
                uploadDuration = 20000,
                finalDuration = 4000
            ),
            PhotoSpecs(
                photoId = 3,
                initialDuration = 2000,
                uploadDuration = 7000,
                finalDuration = 4000
            )
        )

        val photosCount = specs.size
    }

}

private fun PhotoWorker.ProgressData.toWorkData(): Data {
    val photoIdPair = Pair(PhotoWorker.KEY_PHOTO_ID, photoId)
    val progressStatePair = Pair(PhotoWorker.KEY_PROGRESS_STATE, this.javaClass.name)

    return when (this) {
        is PhotoWorker.ProgressData.InProgress -> {
            workDataOf(photoIdPair, progressStatePair, Pair(PhotoWorker.KEY_PROGRESS, progress))
        }

        else -> {
            workDataOf(photoIdPair, progressStatePair)
        }
    }
}