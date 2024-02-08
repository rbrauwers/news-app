package com.rbrauwers.newsapp.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class PhotoSummaryViewModel @Inject constructor(
    photoWorkerManager: PhotoWorkerManager
) : ViewModel() {

    val uiState = photoWorkerManager
        .getProgressData()
        .map { it.toItems() }
        .map { UiState(items = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState()
        )

    data class UiState(val items: List<Item> = emptyList()) {
        data class Item(
            val available: Boolean,
            val title: String,
            val state: String,
            val progress: Float? = null,
            val isError: Boolean = false,
            val isSuccess: Boolean = false
        )
    }

}

private fun List<PhotoWorker.ProgressData>.toItems(): List<PhotoSummaryViewModel.UiState.Item> {
    val items = mutableListOf<PhotoSummaryViewModel.UiState.Item>()

    for (photoId in 1 .. PhotoWorker.photosCount) {
        val title = "Photo #$photoId"

        val item = when (val data = firstOrNull { it.photoId == photoId }) {
            null -> {
                PhotoSummaryViewModel.UiState.Item(
                    available = false,
                    title = title,
                    state = "Data not available"
                )
            }

            is PhotoWorker.ProgressData.Finished -> {
                PhotoSummaryViewModel.UiState.Item(
                    available = false,
                    title = title,
                    state = "Finished",
                    progress = 1.0f,
                    isSuccess = true
                )
            }

            is PhotoWorker.ProgressData.InProgress -> {
                PhotoSummaryViewModel.UiState.Item(
                    available = false,
                    title = title,
                    state = "In Progress",
                    progress = (data.progress.toFloat() / 100.0f)
                )
            }

            is PhotoWorker.ProgressData.Initializing -> {
                PhotoSummaryViewModel.UiState.Item(
                    available = false,
                    title = title,
                    state = "Initializing",
                    progress = 0.0f
                )
            }

            is PhotoWorker.ProgressData.Blocked -> {
                PhotoSummaryViewModel.UiState.Item(
                    available = false,
                    title = title,
                    state = "Blocked",
                    progress = 0.0f
                )
            }

            is PhotoWorker.ProgressData.Cancelled -> {
                PhotoSummaryViewModel.UiState.Item(
                    available = false,
                    title = title,
                    state = "Cancelled",
                    progress = 0.0f
                )
            }

            is PhotoWorker.ProgressData.Enqueued -> {
                PhotoSummaryViewModel.UiState.Item(
                    available = false,
                    title = title,
                    state = "Enqueued",
                    progress = 0.0f
                )
            }

            is PhotoWorker.ProgressData.Failed -> {
                PhotoSummaryViewModel.UiState.Item(
                    available = false,
                    title = title,
                    state = "Failed",
                    progress = 0.0f,
                    isError = true
                )
            }
        }

        items.add(item)
    }

    return items
}