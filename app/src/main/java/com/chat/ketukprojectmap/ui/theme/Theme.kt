package com.chat.ketukprojectmap.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val KetukLightColorScheme = lightColorScheme(
    primary = Terakota,
    onPrimary = Color.White,
    primaryContainer = Peach,
    onPrimaryContainer = Arang,
    secondary = KuningMadu,
    onSecondary = Arang,
    secondaryContainer = Peach,
    onSecondaryContainer = Arang,
    tertiary = HijauDaun,
    onTertiary = Color.White,
    tertiaryContainer = Sage,
    onTertiaryContainer = Arang,
    error = Terakota,
    onError = Color.White,
    errorContainer = Peach,
    onErrorContainer = Arang,
    background = KremBackground,
    onBackground = Arang,
    surface = KremBackground,
    onSurface = Arang,
    surfaceVariant = KremGelap,
    onSurfaceVariant = Arang,
    outline = BiruAbu,
    outlineVariant = KremGelap,
    scrim = Color.Black.copy(alpha = 0.32f),
    inverseSurface = Arang,
    inverseOnSurface = KremBackground,
    inversePrimary = Peach
)

@Composable
fun KetukTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KetukLightColorScheme,
        typography = Typography,
        content = content
    )
}