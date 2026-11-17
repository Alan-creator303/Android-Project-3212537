package com.example.shakeit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Purple,
    onPrimary = Color.White,
    background = Page,
    onBackground = Ink,
    surface = Page,
    onSurface = Ink
)

@Composable
fun ShakeItTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
