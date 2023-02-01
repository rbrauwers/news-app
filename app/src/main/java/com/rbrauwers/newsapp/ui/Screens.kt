package com.rbrauwers.newsapp.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.rbrauwers.newsapp.R

sealed class Screen(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {

    object Headlines : Screen(
        route = "headlines",
        title = R.string.headlines,
        icon = Icons.Filled.List)

    object Sources : Screen(
        route = "sources",
        title = R.string.sources,
        icon = Icons.Filled.Person)

    companion object {
        fun getNavigationBarItems() = listOf(Headlines, Sources)
    }

}