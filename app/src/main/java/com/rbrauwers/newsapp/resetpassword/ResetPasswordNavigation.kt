package com.rbrauwers.newsapp.resetpassword

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

internal const val resetPasswordBaseRoute = "resetPassword"

fun NavController.navigateToResetPassword(navOptions: NavOptions? = null) {
    this.navigate(resetPasswordScreen.route, navOptions)
}

fun NavGraphBuilder.resetPasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    composable(
        route = resetPasswordScreen.route,
    ) {
        ResetPasswordRoute(
            modifier = modifier,
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}