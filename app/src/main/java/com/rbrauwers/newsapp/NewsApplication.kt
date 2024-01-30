package com.rbrauwers.newsapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.data.repository.SourceRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class NewsApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var sourceRepository: SourceRepository

    @Inject
    lateinit var headlinesRepository: HeadlineRepository

    override fun onCreate() {
        super.onCreate()
        //sync()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .build()
    }

    private fun sync() {
        MainScope().launch(context = Dispatchers.IO) {
            sourceRepository.sync()
            headlinesRepository.sync()
        }
    }

}