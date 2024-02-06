package com.rbrauwers.newsapp.photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rbrauwers.newsapp.settings.photoIdArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class PhotoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
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
                    photoId == null || photoId!! >= 3 -> null
                    else -> photoId!! + 1
                }
            )
        )
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val photoDescription: String = "",
        val photoId: Int = -1,
        val nextPhotoId: Int? = null,
        val nextPhotoIsEnabled: Boolean = nextPhotoId != null,
        val summaryIsEnabled: Boolean = nextPhotoId == null
    )

}