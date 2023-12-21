package com.rbrauwers.newsapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun BackNavigationIcon(onBackClick: () -> Unit) {
    IconButton(
        onClick = {
            onBackClick()
        }
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null
        )
    }
}