package com.rbrauwers.newsapp.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rbrauwers.newsapp.R
import com.rbrauwers.newsapp.ui.BackNavigationIcon
import com.rbrauwers.newsapp.ui.BottomBarState
import com.rbrauwers.newsapp.ui.LocalAppState
import com.rbrauwers.newsapp.ui.NewsDefaultTopBar
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.TopBarState

val emailScreen = Screen(
    baseRoute = authBaseRoute,
    route = "${authBaseRoute}/email",
    title = R.string.email,
    icon = Icons.Default.AccountCircle
)

@Composable
internal fun EmailRoute(
    onBackClick: () -> Unit,
    onNavigateToPassword: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EmailViewModel = hiltViewModel()
) {
    val uiState: EmailViewModel.UiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocalAppState.current.apply {
        LaunchedEffect(Unit) {
            setTopBarState(
                topBarState = TopBarState(
                    title = { NewsDefaultTopBar(title = stringResource(id = R.string.email)) },
                    navigationIcon = {
                        BackNavigationIcon(onBackClick = onBackClick)
                    }
                )
            )
            setBottomBarState(bottomBarState = BottomBarState(isVisible = false))
        }
    }

    EmailScreen(
        uiState = uiState,
        modifier = modifier.fillMaxSize(),
        onEmailChange = viewModel::update,
        onNavigateToPassword = onNavigateToPassword
    )
}

@Composable
internal fun EmailScreen(
    uiState: EmailViewModel.UiState,
    modifier: Modifier = Modifier,
    onEmailChange: (String) -> Unit,
    onNavigateToPassword: (String) -> Unit
) {
    Column(modifier = modifier.padding(24.dp)) {
        TextField(
            value = uiState.email.orEmpty(),
            onValueChange = onEmailChange,
            isError = !uiState.error.isNullOrEmpty(),
            placeholder = {
                Text(text = stringResource(id = R.string.email))
            },
            supportingText = {
                uiState.error?.let { error ->
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
            onClick = {
                onNavigateToPassword.invoke(uiState.email.orEmpty())
            },
            enabled = uiState.canProceed,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}

@Preview
@Composable
internal fun EmailScreenPreview() {
    EmailScreen(
        uiState = EmailViewModel.UiState(
            email = "some@example.com",
            error = "Invalid email",
            canProceed = true
        ),
        onEmailChange = {},
        onNavigateToPassword = {}
    )
}