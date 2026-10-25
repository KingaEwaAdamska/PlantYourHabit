package com.example.plantyourhabit.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFC1D0C4),
    onPrimary = Color(0xFF26392F),

    secondary = Color(0xFFC1D0C4),
    onSecondary = Color(0xFF26392F),

    tertiary = Terracotta300,
    onTertiary = Color(0xFF26392F),

    background = Color(0xFF202224),
    onBackground = Color(0xFFE9E9E5),

    surface = Color(0xFF2B2E30),
    onSurface = Color(0xFFE9E9E5),

    surfaceVariant = Color(0xFF3A3E40),
    onSurfaceVariant = Color(0xFFD8DDD8),

    outline = DarkThemeOutline,
    outlineVariant = DarkThemeOutlineVariant,

    error = Terracotta300,
    onError = Color(0xFF26392F),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF26392F),
    onPrimary = Color.White,

    primaryContainer = Color(0xFFDCE8D9),

    secondary = Color(0xFFF7E6B4),
    onSecondary = Color(0xFF8D6424),

    tertiary = Terracotta600,
    onTertiary = Color.White,

    background = Color(0xFFF8F5ED),
    onBackground = Color(0xFF26392F),

    surface = Color(0xFFFFFDF8),
    onSurface = Color(0xFF26392F),

    surfaceVariant = Color(0xFFEDF2E8),
    onSurfaceVariant = Color(0xFF496454),

    outline = PlantCardBorder,
    outlineVariant = BorderColor,

    error = Terracotta700,
    onError = Color.White,
)

@Composable
fun PlantYourHabitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalAppDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}