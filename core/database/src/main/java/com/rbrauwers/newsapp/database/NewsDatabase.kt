package com.rbrauwers.newsapp.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.rbrauwers.newsapp.database.dao.HeadlineDao
import com.rbrauwers.newsapp.database.dao.SourceDao
import com.rbrauwers.newsapp.database.model.ArticleEntity
import com.rbrauwers.newsapp.database.model.NewsSourceEntity

@Database(
    entities = [
        ArticleEntity::class,
        NewsSourceEntity::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun headlineDao(): HeadlineDao

    abstract fun sourceDao(): SourceDao

}