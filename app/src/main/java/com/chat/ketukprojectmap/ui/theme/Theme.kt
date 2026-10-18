package com.chat.ketukprojectmap.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class KetukExtendedColors(
    val isDark: Boolean,
    val screenBackground: Color,
    val cardSurface: Color,
    val cardHighlight: Color,
    val cardNeutral: Color,
    val sheetBackground: Color,
    val barBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val divider: Color,
    val cardBorder: Color,
    val activePill: Color,
    val authFieldBackground: Color,
    val authFieldBorder: Color,
    val inputSurface: Color,
    val chipFilter: Color,
    val chipSehat: Color,
    val chipAman: Color,
    val aksiAktivitas: Color,
    val aksiSapa: Color,
    val aksiHp: Color,
    val cardWali: Color,
    val borderWali: Color,
    val tintWali: Color,
    val tintPenjaga: Color,
    val accentGreen: Color,
    val accentAmber: Color,
    val statusSuccess: Color,
    val linkColor: Color,
    val qrSurface: Color,
    val qrCardWali: Color,
    val onAccent: Color,
    val scannerViewport: Color,
    val overlayScrim: Color
)

private val LightExtendedColors = KetukExtendedColors(
    isDark = false,
    screenBackground = KremBackground,
    cardSurface = CardPutih,
    cardHighlight = CardHome,
    cardNeutral = CardNetral,
    sheetBackground = SheetBackground,
    barBackground = BarBackground,
    textPrimary = Arang,
    textSecondary = Arang.copy(alpha = 0.7f),
    divider = KremGelap,
    cardBorder = ActivePill,
    activePill = ActivePill,
    authFieldBackground = Terakota.copy(alpha = 0.12f),
    authFieldBorder = Terakota.copy(alpha = 0.35f),
    inputSurface = CardPutih,
    chipFilter = ChipFilter,
    chipSehat = ChipSehat,
    chipAman = ChipAman,
    aksiAktivitas = AksiAktivitas,
    aksiSapa = AksiSapa,
    aksiHp = AksiHp,
    cardWali = CardWali,
    borderWali = BorderWali,
    tintWali = TintWali,
    tintPenjaga = TintPenjaga,
    accentGreen = HijauDaun,
    accentAmber = KuningMadu,
    statusSuccess = InfoHijau,
    linkColor = LinkBiru,
    qrSurface = CardPutih,
    qrCardWali = CardWali,
    onAccent = Color.White,
    scannerViewport = DarkSurface,
    overlayScrim = Color.Black.copy(alpha = 0.45f)
)

private val DarkExtendedColors = KetukExtendedColors(
    isDark = true,
    screenBackground = DarkBackground,
    cardSurface = DarkSurface,
    cardHighlight = DarkSurfaceElevated,
    cardNeutral = DarkSurfaceNeutral,
    sheetBackground = DarkSheetBackground,
    barBackground = DarkBarBackground,
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    divider = DarkDivider,
    cardBorder = DarkActivePill,
    activePill = DarkActivePill,
    authFieldBackground = Terakota.copy(alpha = 0.20f),
    authFieldBorder = Terakota.copy(alpha = 0.60f),
    inputSurface = DarkSurfaceNeutral,
    chipFilter = DarkChipFilter,
    chipSehat = DarkChipSehat,
    chipAman = DarkChipAman,
    aksiAktivitas = DarkAksiAktivitas,
    aksiSapa = DarkAksiSapa,
    aksiHp = DarkAksiHp,
    cardWali = DarkCardWali,
    borderWali = DarkBorderWali,
    tintWali = DarkTintWali,
    tintPenjaga = DarkTintPenjaga,
    accentGreen = DarkHijauDaun,
    accentAmber = DarkKuningMadu,
    statusSuccess = DarkHijauDaun,
    linkColor = DarkLinkBiru,
    qrSurface = CardPutih,
    qrCardWali = CardPutih,
    onAccent = Color.White,
    scannerViewport = DarkSurface,
    overlayScrim = Color.Black.copy(alpha = 0.45f)
)

val LocalKetukColors = staticCompositionLocalOf { LightExtendedColors }
val LocalDarkThemeToggle = staticCompositionLocalOf<(Boolean) -> Unit> { {} }

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

private val KetukDarkColorScheme = darkColorScheme(
    primary = Terakota,
    onPrimary = Color.White,
    primaryContainer = DarkCardWali,
    onPrimaryContainer = Peach,
    secondary = DarkKuningMadu,
    onSecondary = Arang,
    secondaryContainer = DarkAksiSapa,
    onSecondaryContainer = Peach,
    tertiary = DarkHijauDaun,
    onTertiary = Arang,
    tertiaryContainer = DarkChipSehat,
    onTertiaryContainer = DarkTextPrimary,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = BiruAbu,
    outlineVariant = DarkDivider,
    scrim = Color.Black.copy(alpha = 0.55f),
    inverseSurface = KremBackground,
    inverseOnSurface = Arang,
    inversePrimary = Terakota
)

object KetukTheme {
    val colors: KetukExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalKetukColors.current

    val isDark: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalKetukColors.current.isDark
}

@Composable
fun KetukTheme(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit = {},
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) KetukDarkColorScheme else KetukLightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    androidx.compose.runtime.CompositionLocalProvider(
        LocalKetukColors provides extendedColors,
        LocalDarkThemeToggle provides onDarkThemeChange
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = KetukShapes,
            content = content
        )
    }
}

/** Overload khusus @Preview mode terang bawaan (tidak mengikuti sistem HP). */
@Composable
fun KetukTheme(
    content: @Composable () -> Unit
) {
    KetukTheme(
        darkTheme = false,
        content = content
    )
}