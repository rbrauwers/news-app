package com.rbrauwers.newsapp.photo

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.rbrauwers.newsapp.data.repository.PhotoWorkerRepository
import com.rbrauwers.newsapp.model.PhotoWorkerInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class PhotoWorkerManager @Inject constructor(
    val context: Context,
    val repository: PhotoWorkerRepository
) {

    suspend fun enqueue(photoId: Int) {
        // TODO cancel previous work related to this photoId

        val uuid = UUID.randomUUID()
        repository.upsert(PhotoWorkerInfo(photoId = photoId, uuid = uuid))

        val workRequest = OneTimeWorkRequestBuilder<PhotoWorker>()
            .setInputData(
                workDataOf(
                    Pair(PhotoWorker.KEY_PHOTO_ID, photoId)
                )
            )
            .setId(uuid)
            .build()

        with(WorkManager.getInstance(context)) {
            enqueue(workRequest)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getProgressData(): Flow<List<PhotoWorker.ProgressData>> {
        val workManager = WorkManager.getInstance(context)

        return repository.list()
            .flatMapLatest { infos ->
                combine(
                    infos.map { info ->
                        runCatching {
                            workManager.getWorkInfoByIdFlow(info.uuid)
                        }
                            .getOrDefault(flowOf(null))
                            .map { workInfo ->
                                WorkInfoWrapper(photoId = info.photoId, workInfo = workInfo)
                            }
                    }
                ) {
                    it.asList()
                }
            }
            .map { workInfos ->
                workInfos.mapNotNull { wrapper ->
                    wrapper.toProgressData()
                }
            }
    }
}

private data class WorkInfoWrapper(
    val photoId: Int,
    val workInfo: WorkInfo?
)

private fun WorkInfoWrapper.toProgressData(): PhotoWorker.ProgressData? {
    return when (workInfo?.state) {
        WorkInfo.State.RUNNING -> {
            val progress = workInfo.progress
            val photoId = progress.getInt(PhotoWorker.KEY_PHOTO_ID, -1)

            return when (progress.getString(PhotoWorker.KEY_PROGRESS_STATE)) {
                PhotoWorker.ProgressData.Initializing::class.java.name -> {
                    PhotoWorker.ProgressData.Initializing(photoId = photoId)
                }

                PhotoWorker.ProgressData.Finished::class.java.name -> {
                    PhotoWorker.ProgressData.Finished(photoId = photoId)
                }

                PhotoWorker.ProgressData.InProgress::class.java.name -> {
                    val relativeProgress = progress.getLong(PhotoWorker.KEY_PROGRESS, 0L)
                    PhotoWorker.ProgressData.InProgress(
                        photoId = photoId,
                        progress = relativeProgress
                    )
                }

                else -> {
                    null
                }
            }
        }

        WorkInfo.State.ENQUEUED -> {
            PhotoWorker.ProgressData.Enqueued(photoId = photoId)
        }

        WorkInfo.State.SUCCEEDED -> {
            PhotoWorker.ProgressData.Finished(photoId = photoId)
        }

        WorkInfo.State.FAILED -> {
            PhotoWorker.ProgressData.Failed(photoId = photoId)
        }

        WorkInfo.State.BLOCKED -> {
            PhotoWorker.ProgressData.Blocked(photoId = photoId)
        }

        WorkInfo.State.CANCELLED -> {
            PhotoWorker.ProgressData.Cancelled(photoId = photoId)
        }

        null -> {
            null
        }
    }
}