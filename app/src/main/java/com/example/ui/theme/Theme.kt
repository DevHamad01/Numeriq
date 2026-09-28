package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NumeriqLightColorScheme = lightColorScheme(
    primary = NumeriqGreenPrimary,
    onPrimary = TextDark,
    primaryContainer = NumeriqGreenBoxBg,
    onPrimaryContainer = NumeriqGreenDarkText,
    secondary = NumeriqGreenDark,
    onSecondary = TextDark,
    background = AppWhite,
    onBackground = TextDark,
    surface = CardWhite,
    onSurface = TextDark,
    surfaceVariant = LightGraySurface,
    onSurfaceVariant = TextMuted,
    outline = NumeriqGreenBorder
)

@Composable
fun NumeriqTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NumeriqLightColorScheme,
        typography = Typography,
        content = content
    )
}
