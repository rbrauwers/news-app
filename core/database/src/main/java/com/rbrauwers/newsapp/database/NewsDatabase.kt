package com.rbrauwers.newsapp.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.rbrauwers.newsapp.database.dao.DefaultHeadlineDao
import com.rbrauwers.newsapp.database.dao.DefaultPhotoWorkerDao
import com.rbrauwers.newsapp.database.dao.DefaultSourceDao
import com.rbrauwers.newsapp.database.dao.HeadlineDao
import com.rbrauwers.newsapp.database.dao.PhotoWorkerDao
import com.rbrauwers.newsapp.database.dao.SourceDao
import com.rbrauwers.newsapp.database.model.ArticleEntity
import com.rbrauwers.newsapp.database.model.NewsSourceEntity
import com.rbrauwers.newsapp.database.model.PhotoWorkerEntity

interface NewsDatabase {
    fun headlineDao(): HeadlineDao
    fun sourceDao(): SourceDao
    fun photoWorkerDao(): PhotoWorkerDao
    suspend fun <R> runWithTransaction(block: suspend () -> R)
}

@Database(
    entities = [
        ArticleEntity::class,
        NewsSourceEntity::class,
        PhotoWorkerEntity::class
    ],
    version = 1,
    autoMigrations = [
    ]
)
internal abstract class DefaultNewsDatabase : RoomDatabase(), NewsDatabase {
    abstract override fun headlineDao(): DefaultHeadlineDao
    abstract override fun sourceDao(): DefaultSourceDao
    abstract override fun photoWorkerDao(): DefaultPhotoWorkerDao

    override suspend fun <R> runWithTransaction(block: suspend () -> R) {
        withTransaction(block)
    }
}