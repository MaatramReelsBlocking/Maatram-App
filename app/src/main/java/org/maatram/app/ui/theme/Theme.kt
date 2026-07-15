package org.maatram.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = CoralRust,
    onPrimary = CreamSurface,
    primaryContainer = CoralContainerLight,
    onPrimaryContainer = InkBlack,
    secondary = WarmGreyText,
    onSecondary = CreamSurface,
    background = CreamBackground,
    onBackground = InkBlack,
    surface = CreamSurface,
    onSurface = InkBlack,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = WarmGreyText,
    outline = WarmBorder,
    outlineVariant = WarmBorder,
    error = ErrorClay,
    onError = CreamSurface,
)

private val DarkColors = darkColorScheme(
    primary = CoralRustDark,
    onPrimary = OnCoralDark,
    primaryContainer = CoralContainerDark,
    onPrimaryContainer = BoneText,
    secondary = WarmGreyTextDark,
    onSecondary = CharcoalBackground,
    background = CharcoalBackground,
    onBackground = BoneText,
    surface = CharcoalSurface,
    onSurface = BoneText,
    surfaceVariant = CharcoalSurfaceVariant,
    onSurfaceVariant = WarmGreyTextDark,
    outline = WarmBorderDark,
    outlineVariant = WarmBorderDark,
    error = ErrorClayDark,
    onError = OnCoralDark,
)

/**
 * Dynamic colour is intentionally NOT used: Maatram's warm identity must stay
 * stable across devices rather than adopting the system wallpaper palette.
 */
@Composable
fun MaatramTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = MaatramTypography,
        shapes = MaatramShapes,
        content = content,
    )
}
