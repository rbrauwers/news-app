package com.rbrauwers.newsapp.profile

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rbrauwers.newsapp.authentication.navigateToAuth
import com.rbrauwers.newsapp.resetpassword.navigateToResetPassword

internal const val profileBaseRoute = "profile"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(profileScreen.route, navOptions)
}

fun NavGraphBuilder.profileScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    composable(
        route = profileScreen.route,
    ) {
        ProfileRoute(
            modifier = modifier,
            onBackClick = {
                navController.popBackStack()
            },
            onNavigateToAuth = {
                navController.navigateToAuth()
            },
            onNavigateToResetPassword = {
                navController.navigateToResetPassword()
            }
        )
    }
}