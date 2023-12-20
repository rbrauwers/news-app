package com.rbrauwers.newsapp.info

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rbrauwers.newsapp.R
import com.rbrauwers.newsapp.ui.AppState
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.TopBarState

val infoScreen = Screen(
    route = "info",
    title = R.string.app_info,
    icon = Icons.Outlined.Info,
    isHome = false
)

private data class Lib(
    val name: String,
    val url: String
)

private val libs = listOf(
    Lib(name = "Chucker", url = "https://github.com/ChuckerTeam/chucker"),
    Lib(name = "Coil", url = "https://coil-kt.github.io/coil/"),
    Lib(name = "Jetpack Compose", url = "https://developer.android.com/jetpack/compose"),
    Lib(name = "Hilt", url = "https://dagger.dev/hilt/"),
    Lib(name = "Material", url = "https://m3.material.io/develop/android/jetpack-compose"),
    Lib(name = "Retrofit", url = "https://square.github.io/retrofit/"),
    Lib(name = "Room", url = "https://developer.android.com/jetpack/androidx/releases/room")
)

@Composable
internal fun InfoRoute(
    modifier: Modifier = Modifier,
    appState: AppState
) {
    appState.setTopBarState(TopBarState(stringResource(id = R.string.app_info)))
    InfoScreen(modifier = modifier.fillMaxSize())
}

@Composable
private fun InfoScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Surface(modifier = modifier) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                Text(text = "Libraries and frameworks", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Divider(modifier = Modifier.height(2.dp))
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(
                items = libs
            ) { lib ->
                val isLast = lib == libs.last()

                Row(modifier = Modifier
                    .clickable {
                        Uri
                            .parse(lib.url)
                            ?.run {
                                val intent = Intent(Intent.ACTION_VIEW, this)
                                context.startActivity(intent)
                            }
                    }
                    .padding(16.dp)
                ) {
                    Text(text = lib.name)

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!isLast) {
                    Divider(modifier = Modifier.height(1.dp))
                }
            }
        }
    }
}

@Preview(widthDp = 400)
@Composable
private fun InfoScreenPreview() {
    InfoScreen()
}