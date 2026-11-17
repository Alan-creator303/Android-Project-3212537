package com.example.shakeit.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shakeit.ui.components.ScreenTopBar

@Composable
fun HistoryScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ScreenTopBar(title = "History", onBack = onBack)

        Spacer(modifier = Modifier.weight(1f))
    }
}
