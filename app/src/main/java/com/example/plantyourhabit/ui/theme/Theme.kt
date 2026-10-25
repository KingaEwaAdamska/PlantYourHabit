package com.example.plantyourhabit.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = Green300,
    onPrimary = Green900,

    secondary = Green300,
    onSecondary = Green900,

    tertiary = Terracotta300,
    onTertiary = Green900,

    background = Green900,
    onBackground = CreamBackground,

    surface = Color(0xFF30483A),
    onSurface = CreamBackground,

    surfaceVariant = Color(0xFF3B604D),
    onSurfaceVariant = Green200,

    outline = Color(0xFF68806D),
    outlineVariant = Color(0xFF496454),

    error = Terracotta300,
    onError = Green900,
)

private val LightColorScheme = lightColorScheme(
    primary = Green900,
    onPrimary = Color.White,

    primaryContainer = Green200,

    secondary = Gold100,
    onSecondary = Gold700,

    tertiary = Terracotta600,
    onTertiary = Color.White,

    background = CreamBackground,
    onBackground = Green900,

    surface = CreamSurface,
    onSurface = Green900,

    surfaceVariant = Green100,
    onSurfaceVariant = Green700,

    outline = Color(0xFFB9C4BA),
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}