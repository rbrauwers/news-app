@file:OptIn(ExperimentalMaterial3Api::class)

package com.rbrauwers.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rbrauwers.newsapp.headline.HeadlinesNavigationBarItem
import com.rbrauwers.newsapp.headline.headlineScreen
import com.rbrauwers.newsapp.headline.headlinesScreen
import com.rbrauwers.newsapp.info.infoScreen
import com.rbrauwers.newsapp.info.navigateToInfo
import com.rbrauwers.newsapp.source.SourcesNavigationBarItem
import com.rbrauwers.newsapp.source.sourceScreen
import com.rbrauwers.newsapp.source.sourcesScreen
import com.rbrauwers.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

private val screens = listOf(headlineScreen, sourcesScreen, sourceScreen, infoScreen)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun Content() {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val currentScreen = currentRoute?.let { route ->
        screens.firstOrNull {
            it.route.contains(route)
        }
    }

    val title = currentScreen?.title?.run {
        stringResource(id = this)
    } ?: ""

    val bottomBarState = currentScreen?.isHome == true

    NewsAppTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    actions = {
                        if (currentScreen?.isHome == true) {
                            IconButton(
                                onClick = {
                                    navController.navigateToInfo()
                                }
                            ) {
                                Icon(
                                    imageVector = infoScreen.icon,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        if (currentScreen?.isHome == false) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                AnimatedContent(
                    targetState = bottomBarState,
                    transitionSpec = {
                        slideInVertically(initialOffsetY = { it }) togetherWith
                        slideOutVertically(targetOffsetY = { it })
                    }, label = ""
                ) { isVisible ->
                    if (isVisible) {
                        NavigationBar {
                            NewsBottomBar(navController = navController)
                            SourcesBottomBar(navController = navController)
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = headlineScreen.route
            ) {
                headlinesScreen(modifier = Modifier.padding(innerPadding))
                sourcesScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController
                )
                sourceScreen(modifier = Modifier.padding(innerPadding))
                infoScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
private fun RowScope.NewsBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    HeadlinesNavigationBarItem(
        navController = navController,
        currentDestination = currentDestination
    )
}

@Composable
private fun RowScope.SourcesBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    SourcesNavigationBarItem(
        navController = navController,
        currentDestination = currentDestination
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NewsAppTheme {
        Content()
    }
}