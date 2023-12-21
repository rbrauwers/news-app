package com.rbrauwers.newsapp.info

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rbrauwers.newsapp.ui.AppState
import com.rbrauwers.newsapp.ui.TopBarState

fun NavController.navigateToInfo(navOptions: NavOptions? = null) {
    this.navigate(infoScreen.route, navOptions)
}

fun NavGraphBuilder.infoScreen(
    modifier: Modifier = Modifier,
    onComposeTopBarState: (TopBarState) -> Unit,
    navController: NavController
) {
    composable(
        route = infoScreen.route,
    ) {
        InfoRoute(
            modifier = modifier,
            onComposeTopBarState = onComposeTopBarState,
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}