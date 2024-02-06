package com.rbrauwers.newsapp.settings

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

internal const val settingsBaseRoute = "settings"
internal const val photoIdArg = "id"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(settingsScreen.route, navOptions)
}

fun NavController.navigateToPhoto(navOptions: NavOptions? = null, photoId: Int) {
    println("navigating to ${buildPhotoRoute(photoId)}")
    navigate(buildPhotoRoute(photoId), navOptions)
}

fun NavController.navigateToPhotoSummary(navOptions: NavOptions? = null) {
    navigate(photoSummaryScreen.route, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    composable(route = settingsScreen.route) {
        SettingsRoute(
            modifier = modifier,
            onBackClick = { navController.popBackStack() },
            onNavigateToPhoto = {
                navController.navigateToPhoto(photoId = 1)
            }
        )
    }
}

fun NavGraphBuilder.photoScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    composable(
        route = photoScreen.route,
        arguments = listOf(navArgument(photoIdArg) { type = NavType.IntType })
    ) {
        PhotoRoute(
            modifier = modifier,
            onBackClick = navController::popBackStack,
            onNavigateToNextPhoto = { photoId -> navController.navigateToPhoto(photoId = photoId) },
            onNavigateToSummary = navController::navigateToPhotoSummary
        )
    }
}

fun NavGraphBuilder.photoSummaryScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    composable(
        route = photoSummaryScreen.route
    ) {
        PhotoSummaryRoute(
            modifier = modifier,
            onCloseClick = {
                navController.popBackStack(settingsScreen.route, inclusive = false)
            }
        )
    }
}

private fun buildPhotoRoute(photoId: Int): String {
    return photoScreen.route.replace("{$photoIdArg}", photoId.toString())
}