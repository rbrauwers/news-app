@file:OptIn(ExperimentalMaterial3Api::class)

package com.rbrauwers.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rbrauwers.newsapp.headline.HeadlinesNavigationBarItem
import com.rbrauwers.newsapp.headline.headlinesScreen
import com.rbrauwers.newsapp.network.NetworkDataSource
import com.rbrauwers.newsapp.source.SourcesNavigationBarItem
import com.rbrauwers.newsapp.source.sourceScreen
import com.rbrauwers.newsapp.source.sourcesScreen
import com.rbrauwers.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Content()
        }
    }
}

@Composable
private fun Content() {
    val navController = rememberNavController()

    NewsAppTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    SourcesBottomBar(navController = navController)
                    NewsBottomBar(navController = navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = sourceScreen.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                sourcesScreen()
                headlinesScreen()
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