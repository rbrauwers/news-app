package com.rbrauwers.newsapp.settings

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rbrauwers.newsapp.R
import com.rbrauwers.newsapp.common.findActivity
import com.rbrauwers.newsapp.common.openAppSettings
import com.rbrauwers.newsapp.model.Article
import com.rbrauwers.newsapp.ui.BackNavigationIcon
import com.rbrauwers.newsapp.ui.BottomBarState
import com.rbrauwers.newsapp.ui.CenteredError
import com.rbrauwers.newsapp.ui.LocalAppState
import com.rbrauwers.newsapp.ui.NewsAppDefaultProgressIndicator
import com.rbrauwers.newsapp.ui.NewsDefaultTopBar
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.TopBarState
import kotlinx.coroutines.launch

val settingsScreen = Screen(
    baseRoute = "settings",
    route = "settings",
    title = R.string.settings,
    icon = Icons.Default.Settings
)

private val permissionsToRequest = arrayOf(
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.CALL_PHONE,
)

@Composable
internal fun SettingsRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState: SettingsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocalAppState.current.apply {
        LaunchedEffect(Unit) {
            setTopBarState(
                topBarState = TopBarState(
                    title = { NewsDefaultTopBar(title = stringResource(id = R.string.settings)) },
                    navigationIcon = {
                        BackNavigationIcon(onBackClick = onBackClick)
                    }
                )
            )
            setBottomBarState(bottomBarState = BottomBarState(isVisible = false))
        }
    }

    SettingsScreen(
        modifier = modifier.fillMaxSize(),
        onPermissionResult = viewModel::onPermissionResult,
        onDismissPermission = viewModel::dismissDialog,
        onRemoveLikes = viewModel::onRemoveLikes,
        uiState = uiState
    )
}

@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    onPermissionResult: (PermissionResult) -> Unit,
    onDismissPermission: () -> Unit,
    onRemoveLikes: (List<Article>) -> Unit,
    uiState: SettingsUiState
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is SettingsUiState.Loading -> {
                NewsAppDefaultProgressIndicator(placeOnCenter = true)
            }

            is SettingsUiState.Error -> {
                CenteredError(text = "Something went wrong.")
            }

            is SettingsUiState.Success -> {
                Success(
                    uiState = uiState,
                    onPermissionResult = onPermissionResult,
                    onDismissPermission = onDismissPermission,
                    onRemoveLikes = onRemoveLikes
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Success(
    uiState: SettingsUiState.Success,
    onPermissionResult: (PermissionResult) -> Unit,
    onDismissPermission: () -> Unit,
    onRemoveLikes: (List<Article>) -> Unit
) {
    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            perms.forEach { (permission, isGranted) ->
                onPermissionResult(
                    PermissionResult(
                        permission = permission,
                        isGranted = isGranted
                    )
                )
            }
        }
    )

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.padding(24.dp)) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                multiplePermissionResultLauncher.launch(permissionsToRequest)
            }
        ) {
            Text("Grant permissions")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Likes count: ${uiState.likesCount ?: "N/A"}")

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = {
                isSheetOpen = true
            }) {
                Text("SEE")
            }
        }
    }

    PermissionDialogs(
        permissionResultLauncher = multiplePermissionResultLauncher,
        onDismissPermission = onDismissPermission,
        permissionsQueue = uiState.permissionsQueue
    )

    if (isSheetOpen) {
        LikedArticlesBottomSheet(
            sheetState = sheetState,
            uiState = uiState,
            onDismissRequest = {
                isSheetOpen = false
            },
            onRemoveLikes = onRemoveLikes
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LikedArticlesBottomSheet(
    sheetState: SheetState,
    uiState: SettingsUiState.Success,
    onDismissRequest: () -> Unit,
    onRemoveLikes: (List<Article>) -> Unit
) {
    val selectedArticles = remember {
        mutableStateListOf<Article>()
    }

    val coroutineScope = rememberCoroutineScope()

    /**
     * TODO: Currently BottomSheetDefaults.windowInsets is zero.
     * Therefore we need to add an arbitrary inset.
     * Check if it would be fixed in future versions.
     */
    val defaultInsets = BottomSheetDefaults.windowInsets
    val customInsets = if (defaultInsets.getBottom(Density(LocalContext.current)) == 0) {
        defaultInsets.add(WindowInsets(bottom = 48.dp))
    } else defaultInsets

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .heightIn(min = 300.dp)
            .scrollable(state = rememberScrollState(), orientation = Orientation.Vertical),
        windowInsets = customInsets
    ) {
        Text(
            text = "Liked articles",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(20.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
        ) {
            items(
                items = uiState.likedArticles,
                key = { it.id }
            ) { article ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = article.title ?: "N/A",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Checkbox(
                        checked = selectedArticles.contains(article),
                        onCheckedChange = { checked ->
                            if (checked) {
                                selectedArticles.add(article)
                            } else {
                                selectedArticles.remove(article)
                            }
                        }
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }

        TextButton(
            onClick = {
                coroutineScope.launch {
                    sheetState.hide()
                }
                onRemoveLikes(selectedArticles)
            },
            modifier = Modifier.padding(horizontal = 4.dp),
            enabled = selectedArticles.isNotEmpty()
        ) {
            Text(text = "REMOVE")
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun PermissionDialogs(
    permissionResultLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    onDismissPermission: () -> Unit,
    permissionsQueue: List<String>
) {
    if (permissionsQueue.isEmpty()) {
        return
    }

    val activity = LocalContext.current.findActivity()

    permissionsQueue
        .reversed()
        .forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.CAMERA -> {
                        CameraPermissionTextProvider()
                    }

                    Manifest.permission.RECORD_AUDIO -> {
                        RecordAudioPermissionTextProvider()
                    }

                    Manifest.permission.CALL_PHONE -> {
                        PhoneCallPermissionTextProvider()
                    }

                    else -> return@forEach
                },
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                    activity,
                    permission
                ),
                onDismiss = onDismissPermission,
                onConfirmClick = {
                    onDismissPermission()
                    permissionResultLauncher.launch(
                        arrayOf(permission)
                    )
                },
                onGoToAppSettingsClick = {
                    activity.openAppSettings()
                }
            )
        }
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    SettingsScreen(
        onPermissionResult = { },
        uiState = SettingsUiState.Success(likesCount = "4"),
        onDismissPermission = { },
        onRemoveLikes = { }
    )
}