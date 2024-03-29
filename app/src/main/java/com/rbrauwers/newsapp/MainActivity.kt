@file:OptIn(ExperimentalMaterial3Api::class)

package com.rbrauwers.newsapp

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rbrauwers.newsapp.authentication.authNavHost
import com.rbrauwers.newsapp.authentication.navigateToAuth
import com.rbrauwers.newsapp.headline.HeadlinesNavigationBarItem
import com.rbrauwers.newsapp.headline.headlinesBaseRoute
import com.rbrauwers.newsapp.headline.headlinesNavHost
import com.rbrauwers.newsapp.headline.pagedHeadlinesNavHost
import com.rbrauwers.newsapp.info.infoScreen
import com.rbrauwers.newsapp.info.navigateToInfo
import com.rbrauwers.newsapp.profile.navigateToProfile
import com.rbrauwers.newsapp.profile.profileScreen
import com.rbrauwers.newsapp.resetpassword.resetPasswordScreen
import com.rbrauwers.newsapp.settings.navigateToSettings
import com.rbrauwers.newsapp.settings.photoScreen
import com.rbrauwers.newsapp.settings.photoSummaryScreen
import com.rbrauwers.newsapp.settings.settingsScreen
import com.rbrauwers.newsapp.source.SourcesNavigationBarItem
import com.rbrauwers.newsapp.source.sourcesNavHost
import com.rbrauwers.newsapp.ui.AppState
import com.rbrauwers.newsapp.ui.LocalAppState
import com.rbrauwers.newsapp.ui.TopBarState
import com.rbrauwers.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

private enum class HeadlinesMode {
    Default,
    Paged
}

private val headlinesMode = HeadlinesMode.Default

/**
 * FragmentActivity is used over ComponentActivity because it is required by BiometricPrompt.
 * It is not an issue because FragmentActivity extends ComponentActivity.
 */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()

        setContent {
            Content()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content() {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    CompositionLocalProvider(LocalAppState provides AppState()) {
        val topBarState: TopBarState by LocalAppState.current.topBarStateFlow.collectAsStateWithLifecycle()
        val bottomBarState = LocalAppState.current.bottomBarStateFlow.collectAsStateWithLifecycle()

        NewsAppTheme {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = topBarState.title ?: { },
                        scrollBehavior = scrollBehavior,
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        actions = topBarState.actions ?: { },
                        navigationIcon = topBarState.navigationIcon ?: { },
                    )
                },
                bottomBar = {
                    AnimatedContent(
                        targetState = bottomBarState,
                        transitionSpec = {
                            slideInVertically(initialOffsetY = { it }) togetherWith
                                    slideOutVertically(targetOffsetY = { it })
                        }, label = ""
                    ) { state ->
                        if (state.value.isVisible) {
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
                    startDestination = headlinesBaseRoute,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    when (headlinesMode) {
                        HeadlinesMode.Paged -> {
                            pagedHeadlinesNavHost(
                                onNavigateToInfo = {
                                    navController.navigateToInfo()
                                },
                                onNavigateToSettings = {
                                    navController.navigateToSettings()
                                },
                                onNavigateToProfile = {
                                    navController.navigateToAuth()
                                }
                            )
                        }

                        HeadlinesMode.Default -> {
                            headlinesNavHost(
                                onNavigateToInfo = {
                                    navController.navigateToInfo()
                                },
                                onNavigateToSettings = {
                                    navController.navigateToSettings()
                                },
                                onNavigateToProfile = {
                                    navController.navigateToProfile()
                                }
                            )
                        }
                    }

                    sourcesNavHost(
                        onNavigateToInfo = {
                            navController.navigateToInfo()
                        },
                        onNavigateToSettings = {
                            navController.navigateToSettings()
                        },
                        onNavigateToProfile = {
                            navController.navigateToProfile()
                        }
                    )

                    authNavHost(onPopAuthGraph = { navController.popBackStack() })
                    infoScreen(navController = navController)
                    profileScreen(navController = navController)
                    resetPasswordScreen(navController = navController)
                    settingsScreen(navController = navController)
                    photoScreen(navController = navController)
                    photoSummaryScreen(navController = navController)
                }
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
