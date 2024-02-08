package com.rbrauwers.newsapp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rbrauwers.newsapp.model.PhotoWorkerInfo
import java.util.UUID

@Entity(tableName = "photo_workers")
data class PhotoWorkerEntity(
    @PrimaryKey
    @ColumnInfo(name = "photo_id")
    val photoId: Int,
    val uuid: String
)

fun PhotoWorkerEntity.toExternalModel() = PhotoWorkerInfo(
    photoId = photoId,
    uuid = UUID.fromString(uuid)
)