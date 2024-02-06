package com.rbrauwers.newsapp.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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
                photoId = if (photoId == null) "N/A" else photoId.toString(),
                nextPhotoId = when {
                    photoId == null || photoId!! >= 3 -> null
                    else -> photoId!! + 1
                }
            )
        )
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val photoId: String = "",
        val nextPhotoId: Int? = null,
        val nextPhotoIsEnabled: Boolean = nextPhotoId != null,
        val summaryIsEnabled: Boolean = nextPhotoId == null
    )

}