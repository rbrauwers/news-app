package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.database.dao.PhotoWorkerDao
import com.rbrauwers.newsapp.database.model.PhotoWorkerEntity
import com.rbrauwers.newsapp.database.model.toExternalModel
import com.rbrauwers.newsapp.model.PhotoWorkerInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PhotoWorkerRepository {
    fun list(): Flow<List<PhotoWorkerInfo>>
    suspend fun get(photoId: Int): PhotoWorkerInfo
    suspend fun upsert(info: PhotoWorkerInfo)
    suspend fun deleteAll()
}

internal class DefaultPhotoWorkerRepository(private val dao: PhotoWorkerDao) :
    PhotoWorkerRepository {

    override fun list(): Flow<List<PhotoWorkerInfo>> {
        return dao.list().map { list ->
            list.map { it.toExternalModel() }
        }
    }

    override suspend fun get(photoId: Int): PhotoWorkerInfo {
        return dao.get(photoId = photoId).toExternalModel()
    }

    override suspend fun upsert(info: PhotoWorkerInfo) {
        dao.upsert(info.toEntity())
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

}

private fun PhotoWorkerInfo.toEntity() = PhotoWorkerEntity(
    photoId = photoId,
    uuid = uuid.toString()
)