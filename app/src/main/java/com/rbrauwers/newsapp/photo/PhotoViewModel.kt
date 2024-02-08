package com.rbrauwers.newsapp.photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.newsapp.settings.photoIdArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PhotoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val photoWorkerManager: PhotoWorkerManager
) : ViewModel() {

    private val photoId by lazy {
        savedStateHandle.get<Int>(photoIdArg)
    }

    private val _uiState =
        MutableStateFlow(
            UiState(
                photoDescription = if (photoId == null) "N/A" else photoId.toString(),
                photoId = photoId ?: -1,
                nextPhotoId = when {
                    photoId == null || photoId!! >= PhotoWorker.photosCount -> null
                    else -> photoId!! + 1
                }
            )
        )
    val uiState = _uiState.asStateFlow()

    fun enqueueWork() {
        val id = photoId ?: run {
            println("Could not enqueue work because photoId is null")
            return
        }

        viewModelScope.launch {
            photoWorkerManager.enqueue(photoId = id)
        }
    }

    data class UiState(
        val photoDescription: String = "",
        val photoId: Int = -1,
        val nextPhotoId: Int? = null,
        val nextPhotoIsEnabled: Boolean = nextPhotoId != null,
        val summaryIsEnabled: Boolean = nextPhotoId == null
    )

}