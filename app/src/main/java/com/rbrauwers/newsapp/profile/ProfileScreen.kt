package com.rbrauwers.newsapp.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rbrauwers.newsapp.R
import com.rbrauwers.newsapp.model.UserSettings
import com.rbrauwers.newsapp.ui.BackNavigationIcon
import com.rbrauwers.newsapp.ui.BottomBarState
import com.rbrauwers.newsapp.ui.LocalAppState
import com.rbrauwers.newsapp.ui.NewsDefaultTopBar
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.TopBarState

val profileScreen = Screen(
    baseRoute = profileBaseRoute,
    route = profileBaseRoute,
    title = R.string.profile,
    icon = Icons.Default.AccountCircle
)

@Composable
internal fun ProfileRoute(
    onBackClick: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToResetPassword: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState: ProfileViewModel.UiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocalAppState.current.apply {
        LaunchedEffect(Unit) {
            setTopBarState(
                topBarState = TopBarState(
                    title = { NewsDefaultTopBar(title = stringResource(id = R.string.profile)) },
                    navigationIcon = {
                        BackNavigationIcon(onBackClick = onBackClick)
                    }
                )
            )
            setBottomBarState(bottomBarState = BottomBarState(isVisible = false))
        }
    }

    ProfileScreen(
        uiState = uiState,
        modifier = modifier,
        onNavigateToAuth = onNavigateToAuth,
        onNavigateToResetPassword = onNavigateToResetPassword,
        onSignOut = viewModel::signOut
    )
}

@Composable
private fun ProfileScreen(
    uiState: ProfileViewModel.UiState,
    onNavigateToAuth: () -> Unit,
    onNavigateToResetPassword: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        uiState.userSettings?.let {
            Text(text = it.username.orEmpty())
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = it.password.orEmpty())
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (uiState.isLoginEnabled) {
            FilledTonalButton(onClick = onNavigateToAuth, modifier = Modifier.fillMaxWidth()) {
                Text("Login")
            }
        }

        if (uiState.isSignOutEnabled) {
            OutlinedButton(
                onClick = onNavigateToResetPassword,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset password")
                Spacer(modifier = Modifier.height(24.dp))
            }

            FilledTonalButton(onClick = onSignOut, modifier = Modifier.fillMaxWidth()) {
                Text("Sign Out")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(
        uiState = ProfileViewModel.UiState(
            userSettings = UserSettings(username = "some@email.com", password = "abcd"),
            isLoading = false
        ),
        onNavigateToAuth = { },
        onNavigateToResetPassword = { },
        onSignOut = { })
}