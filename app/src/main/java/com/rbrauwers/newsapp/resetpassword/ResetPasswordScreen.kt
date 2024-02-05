package com.rbrauwers.newsapp.resetpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
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

val resetPasswordScreen = Screen(
    baseRoute = resetPasswordBaseRoute,
    route = resetPasswordBaseRoute,
    title = R.string.reset_password,
    icon = Icons.Default.AccountCircle
)

@Composable
internal fun ResetPasswordRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val uiState: ResetPasswordViewModel.UiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocalAppState.current.apply {
        LaunchedEffect(Unit) {
            setTopBarState(
                topBarState = TopBarState(
                    title = { NewsDefaultTopBar(title = stringResource(id = R.string.reset_password)) },
                    navigationIcon = {
                        BackNavigationIcon(onBackClick = onBackClick)
                    }
                )
            )
            setBottomBarState(bottomBarState = BottomBarState(isVisible = false))
        }
    }

    ResetPasswordScreen(
        modifier = modifier,
        uiState = uiState,
        onResetPassword = viewModel::resetPassword,
        onEmailChange = viewModel::update,
        onDismissDialog = onBackClick
    )
}

@Composable
internal fun ResetPasswordScreen(
    modifier: Modifier,
    uiState: ResetPasswordViewModel.UiState,
    onResetPassword: () -> Unit,
    onEmailChange: (String) -> Unit,
    onDismissDialog: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        if (uiState.isProcessing) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(24.dp))
        }

        TextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth()
                .testTag("emailField"),
            isError = !uiState.isValidEmail,
            supportingText = {
                uiState.emailError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            placeholder = {
                Text("Email")
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        FilledTonalButton(
            onClick = onResetPassword,
            modifier = Modifier.fillMaxWidth().testTag("resetPasswordButton"),
            enabled = uiState.isResetPasswordEnabled
        ) {
            Text("Reset password")
        }
    }

    if (uiState.isSuccess) {
        AlertDialog(onDismissRequest = onDismissDialog, confirmButton = {
            TextButton(onClick = {
                onDismissDialog()
            }) {
                Text("Ok")
            }
        }, title = {
            Text(text = "Password reseted.")
        })
    }
}

@Preview(showBackground = true)
@Composable
private fun ResetPasswordScreenPreview() {
    ResetPasswordScreen(
        modifier = Modifier,
        uiState = ResetPasswordViewModel.UiState(
            email = "typed@email.com",
            isResetPasswordEnabled = true,
            emailError = "Incorrect email",
            isProcessing = true,
            isSuccess = true
        ),
        onResetPassword = { },
        onEmailChange = { _ -> },
        onDismissDialog = { }
    )
}