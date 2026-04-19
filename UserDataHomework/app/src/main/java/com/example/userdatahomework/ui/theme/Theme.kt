package com.example.userdatahomework.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// acik yesil ve beyaz renk semasi
private val LightColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = GreenLight80,
    onPrimaryContainer = GreenDark40,
    secondary = GreenGrey40,
    onSecondary = Color.White,
    secondaryContainer = GreenGrey80,
    onSecondaryContainer = GreenDark40,
    background = BackgroundLight,
    onBackground = Color(0xFF1C1C1C),
    surface = SurfaceLight,
    onSurface = Color(0xFF1C1C1C),
    surfaceVariant = Color(0xFFE8F5E9),
    onSurfaceVariant = Color(0xFF555555),
    error = Color(0xFFB00020),
    onError = Color.White
)

// koyu yesil renk semasi
private val DarkColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green90,
    primaryContainer = GreenDark90,
    onPrimaryContainer = GreenLight80,
    secondary = GreenGrey80,
    onSecondary = Green90,
    secondaryContainer = GreenDark40,
    onSecondaryContainer = GreenGrey80,
    background = BackgroundDark,
    onBackground = Color(0xFFE0E0E0),
    surface = SurfaceDark,
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFBDBDBD),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

// sistem ayarina gore otomatik tema secimi
@Composable
fun UserDataHomeworkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}