package com.rbrauwers.newsapp.settings

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rbrauwers.newsapp.R
import com.rbrauwers.newsapp.common.findActivity
import com.rbrauwers.newsapp.common.openAppSettings
import com.rbrauwers.newsapp.ui.BackNavigationIcon
import com.rbrauwers.newsapp.ui.BottomBarState
import com.rbrauwers.newsapp.ui.LocalAppState
import com.rbrauwers.newsapp.ui.NewsDefaultTopBar
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.TopBarState

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
        uiState = uiState
    )
}

@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    onPermissionResult: (PermissionResult) -> Unit,
    onDismissPermission: () -> Unit,
    uiState: SettingsUiState
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

            /*
            permissionsToRequest.forEach { permission ->
                onPermissionResult(
                    PermissionResult(
                        permission = permission,
                        isGranted = perms[permission] == true
                    )
                )
            }
             */
        }
    )

    val activity = LocalContext.current.findActivity()

    Column(modifier = modifier.padding(24.dp)) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                multiplePermissionResultLauncher.launch(permissionsToRequest)
            }
        ) {
            Text("Grant permissions")
        }
    }

    uiState.visiblePermissionDialogQueue
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
                    multiplePermissionResultLauncher.launch(
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
        uiState = SettingsUiState(),
        onDismissPermission = { }
    )
}