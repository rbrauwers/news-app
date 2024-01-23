package com.rbrauwers.newsapp.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.rbrauwers.newsapp.database.dao.DefaultHeadlineDao
import com.rbrauwers.newsapp.database.dao.DefaultSourceDao
import com.rbrauwers.newsapp.database.dao.HeadlineDao
import com.rbrauwers.newsapp.database.dao.SourceDao
import com.rbrauwers.newsapp.database.model.ArticleEntity
import com.rbrauwers.newsapp.database.model.NewsSourceEntity

interface NewsDatabase {
    fun headlineDao(): HeadlineDao
    fun sourceDao(): SourceDao
    suspend fun <R> runWithTransaction(block: suspend () -> R)
}

@Database(
    entities = [
        ArticleEntity::class,
        NewsSourceEntity::class
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ]
)
internal abstract class DefaultNewsDatabase : RoomDatabase(), NewsDatabase {
    abstract override fun headlineDao(): DefaultHeadlineDao
    abstract override fun sourceDao(): DefaultSourceDao

    override suspend fun <R> runWithTransaction(block: suspend () -> R) {
        withTransaction(block)
    }
}