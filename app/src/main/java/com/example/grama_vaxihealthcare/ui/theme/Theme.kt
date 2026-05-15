package com.example.grama_vaxihealthcare.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = RuralGreen,
    onPrimary = TextOnPrimary,
    secondary = EarthBrown,
    onSecondary = TextOnPrimary,
    background = AppBackgroundLight,
    surface = SurfaceColor,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    primaryContainer = RuralGreenDark,
    onPrimaryContainer = TextOnPrimary,
    secondaryContainer = EarthBrownLight,
    onSecondaryContainer = EarthBrownDark,
    surfaceVariant = Color(0xFFE8EBE3),
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFFD0D3CB),
    outlineVariant = Color(0xFFE0E3DB)
)

@Composable
fun GramaVaxiHealthcareTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    // Always use light theme for professional healthcare look
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
