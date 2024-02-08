package com.rbrauwers.newsapp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rbrauwers.newsapp.database.model.PhotoWorkerEntity
import kotlinx.coroutines.flow.Flow

interface PhotoWorkerDao {
    fun list(): Flow<List<PhotoWorkerEntity>>
    suspend fun get(photoId: Int): PhotoWorkerEntity
    suspend fun upsert(entity: PhotoWorkerEntity)
    suspend fun deleteAll()
}

@Dao
internal interface DefaultPhotoWorkerDao : PhotoWorkerDao {

    @Query(
        value = """
            SELECT * FROM photo_workers
        """
    )
    override fun list(): Flow<List<PhotoWorkerEntity>>

    @Query(
        value = """
            SELECT * FROM photo_workers 
            WHERE photo_id = :photoId
        """)
    override suspend fun get(photoId: Int): PhotoWorkerEntity

    @Upsert(entity = PhotoWorkerEntity::class)
    override suspend fun upsert(entity: PhotoWorkerEntity)

    @Query("DELETE FROM photo_workers")
    override suspend fun deleteAll()

}