package com.rbrauwers.newsapp.settings

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(settingsScreen.route, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    composable(route = settingsScreen.route) {
        SettingsRoute(modifier = modifier, onBackClick = { navController.popBackStack() })
    }
}