package com.rbrauwers.newsapp.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rbrauwers.newsapp.R
import com.rbrauwers.newsapp.ui.BackNavigationIcon
import com.rbrauwers.newsapp.ui.BottomBarState
import com.rbrauwers.newsapp.ui.LocalAppState
import com.rbrauwers.newsapp.ui.NewsAppDefaultProgressIndicator
import com.rbrauwers.newsapp.ui.NewsDefaultTopBar
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.TopBarState

internal const val emailArg = "email"

val passwordScreen = Screen(
    baseRoute = authBaseRoute,
    route = "${authBaseRoute}/password/{$emailArg}",
    title = R.string.email,
    icon = Icons.Default.AccountCircle
)

@Composable
internal fun PasswordRoute(
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val uiState: SignupViewModel.UiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocalAppState.current.apply {
        LaunchedEffect(Unit) {
            setTopBarState(
                topBarState = TopBarState(
                    title = { NewsDefaultTopBar(title = stringResource(id = R.string.password)) },
                    navigationIcon = {
                        BackNavigationIcon(onBackClick = onBackClick)
                    }
                )
            )
            setBottomBarState(bottomBarState = BottomBarState(isVisible = false))
        }
    }

    PasswordScreen(
        uiState = uiState,
        modifier = modifier.fillMaxSize(),
        onSignupClick = viewModel::signup,
        onNavigateToHome = onNavigateToHome,
        onPasswordChange = viewModel::updatePassword,
        onPasswordConfirmationChange = viewModel::updatePasswordConfirmation
    )
}

@Composable
internal fun PasswordScreen(
    uiState: SignupViewModel.UiState,
    modifier: Modifier = Modifier,
    onSignupClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmationChange: (String) -> Unit
) {
    Column(modifier = modifier.padding(24.dp)) {
        Text(text = uiState.email)

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            placeholder = {
                Text("Password")
            },
            isError = !uiState.passwordError.isNullOrEmpty(),
            supportingText = {
                uiState.passwordError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = uiState.passwordConfirmation,
            onValueChange = onPasswordConfirmationChange,
            placeholder = {
                Text("Confirm password")
            },
            isError = !uiState.passwordConfirmationError.isNullOrEmpty(),
            supportingText = {
                uiState.passwordConfirmationError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        FilledTonalButton(
            onClick = onSignupClick,
            enabled = uiState.canProceed,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign up")
        }

        if (uiState.isProcessing) {
            NewsAppDefaultProgressIndicator(placeOnCenter = true)
        }

        if (uiState.isSuccess) {
            LaunchedEffect(Unit) {
                onNavigateToHome()
            }
        }
    }
}