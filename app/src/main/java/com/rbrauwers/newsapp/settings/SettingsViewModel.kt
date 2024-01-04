package com.rbrauwers.newsapp.settings

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.newsapp.common.BiometricAuthenticator
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val headlineRepository: HeadlineRepository,
    val biometricAuthenticator: BiometricAuthenticator
) : ViewModel() {

    private val headlinesFlow = headlineRepository
        .getHeadlines()
        .asResult()

    private val permissionsQueueFlow = MutableStateFlow<List<String>>(emptyList())

    private val biometricStatusFlow = MutableStateFlow<BiometricAuthenticator.BiometricStatus>(
        BiometricAuthenticator.BiometricStatus.NotRequested
    )

    val uiState =
        combine(
            headlinesFlow,
            permissionsQueueFlow,
            biometricStatusFlow
        ) { headlinesResult, permissionsQueue, biometricStatus ->
            headlinesResult.toSettingsUiState(
                permissionsQueue = permissionsQueue,
                biometricStatus = biometricStatus
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SettingsUiState.Loading
            )

    fun dismissDialog() {
        permissionsQueueFlow.update {
            it.toMutableList().also { l -> l.removeFirst() }
        }
    }

    fun onPermissionResult(
        result: PermissionResult
    ) {
        with(result) {
            if (!isGranted && !permissionsQueueFlow.value.contains(permission)) {
                permissionsQueueFlow.update {
                    it.toMutableList().also { l -> l.add(permission) }
                }
            }
        }
    }

    fun onAuthenticationResult(result: BiometricAuthenticator.BiometricStatus) {
        biometricStatusFlow.value = result

        if (result is BiometricAuthenticator.BiometricStatus.Error || result is BiometricAuthenticator.BiometricStatus.Success) {
            viewModelScope.launch {
                delay(3000)
                biometricStatusFlow.value = BiometricAuthenticator.BiometricStatus.NotRequested
            }
        }
    }

    fun onRemoveLikes(articles: List<Article>) {
        viewModelScope.launch {
            val map = articles.associate {
                Pair(it.id, false)
            }

            headlineRepository.updateLikes(map)
        }
    }

}

internal data class PermissionResult(
    val permission: String,
    val isGranted: Boolean
)

@Immutable
internal sealed interface SettingsUiState {

    @Immutable
    data object Loading : SettingsUiState

    @Immutable
    data object Error : SettingsUiState

    @Immutable
    data class Success(
        val permissionsQueue: List<String> = emptyList(),
        val likedArticles: List<Article> = emptyList(),
        val likesCount: String? = likedArticles.size.toString(),
        val biometricStatus: BiometricAuthenticator.BiometricStatus = BiometricAuthenticator.BiometricStatus.NotRequested
    ) : SettingsUiState

}

private fun Result<List<Article>>.toSettingsUiState(
    permissionsQueue: List<String>,
    biometricStatus: BiometricAuthenticator.BiometricStatus
): SettingsUiState {
    return when (this) {
        is Result.Loading -> SettingsUiState.Loading
        is Result.Error -> SettingsUiState.Error
        is Result.Success -> SettingsUiState.Success(
            permissionsQueue = permissionsQueue,
            likedArticles = data.filter { it.liked },
            biometricStatus = biometricStatus
        )
    }
}

