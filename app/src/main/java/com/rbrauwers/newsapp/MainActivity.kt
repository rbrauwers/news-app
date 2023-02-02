@file:OptIn(ExperimentalMaterial3Api::class)

package com.rbrauwers.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rbrauwers.newsapp.headline.headlineScreen
import com.rbrauwers.newsapp.headline.headlinesNavigationBarItem
import com.rbrauwers.newsapp.headline.headlinesScreen
import com.rbrauwers.newsapp.network.NetworkDataSource
import com.rbrauwers.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkDataSource: NetworkDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NewsAppTheme {
                Scaffold(
                    bottomBar = {
                        NewsBottomBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = headlineScreen.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        headlinesScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun NewsBottomBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        headlinesNavigationBarItem(
            navController = navController,
            currentDestination = currentDestination
        )
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NewsAppTheme {
        Greeting("Android")
    }
}