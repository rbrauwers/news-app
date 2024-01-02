package com.rbrauwers.newsapp.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )


    fun dismissDialog() {
        _uiState.update {
            it.removeTopPermission()
        }
    }

    fun onPermissionResult(
        result: PermissionResult
    ) {
        with(result) {
            if (!isGranted && !_uiState.value.visiblePermissionDialogQueue.contains(permission)) {
                _uiState.update {
                    it.addPermission(permission)
                }
            }
        }
    }

}

internal data class PermissionResult(
    val permission: String,
    val isGranted: Boolean
)

internal data class SettingsUiState(
    val visiblePermissionDialogQueue: List<String> = emptyList()
) {
    fun removeTopPermission(): SettingsUiState = copy(
        visiblePermissionDialogQueue = visiblePermissionDialogQueue.toMutableList().also {
            it.removeFirst()
        }
    )

    fun addPermission(permission: String): SettingsUiState = copy(
        visiblePermissionDialogQueue = visiblePermissionDialogQueue.toMutableList().also {
            if (!it.contains(permission)) {
                it.add(permission)
            }
        }
    )
}