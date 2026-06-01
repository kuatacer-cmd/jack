package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ObsidianColorScheme = darkColorScheme(
    primary = AccentBlue,
    onPrimary = DeepBlack,
    secondary = AccentPurple,
    onSecondary = DeepBlack,
    tertiary = CardGraphite,
    background = DeepBlack,
    onBackground = TextWhite,
    surface = CardGraphite,
    onSurface = TextWhite,
    surfaceVariant = BorderGraphite,
    onSurfaceVariant = TextMuted,
    error = AlertRed,
    onError = TextWhite
)

private val LightColorScheme = lightColorScheme(
    primary = AccentBlue,
    onPrimary = Color.White,
    secondary = AccentPurple,
    onSecondary = Color.White,
    tertiary = Color(0xFFE0E0E0),
    background = Color(0xFFF5F5F5),
    onBackground = DeepBlack,
    surface = Color.White,
    onSurface = DeepBlack,
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF666666),
    error = AlertRed,
    onError = Color.White
)

@Composable
fun FocusMindTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ObsidianColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Keep MyApplicationTheme for compatibility with tests and main entry point templates
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    FocusMindTheme(darkTheme = darkTheme, content = content)
}
