package org.maatram.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Maatram palette — warm claude.ai-style tokens.
 *
 * Deliberate override of the green+blue specified in the project PDFs
 * (decision: 2026-07-15). Never reference raw hex outside this file; compose
 * against [MaatramTheme]'s MaterialTheme.colorScheme instead.
 */

// ── Light ────────────────────────────────────────────────────────────────────
internal val CreamBackground = Color(0xFFFAF9F5)
internal val CreamSurface = Color(0xFFFFFFFF)
internal val CreamSurfaceVariant = Color(0xFFF0EEE6)
internal val CoralRust = Color(0xFFCC785C)
internal val CoralRustPressed = Color(0xFFB5644A)
internal val CoralContainerLight = Color(0xFFF7E9E2)
internal val InkBlack = Color(0xFF141413)
internal val WarmGreyText = Color(0xFF6B6A65)
internal val WarmBorder = Color(0xFFDEDBD0)
internal val ErrorClay = Color(0xFFB3402A)

// ── Dark (warm charcoals) ────────────────────────────────────────────────────
internal val CharcoalBackground = Color(0xFF262624)
internal val CharcoalSurface = Color(0xFF30302E)
internal val CharcoalSurfaceVariant = Color(0xFF3A3A37)
internal val CoralRustDark = Color(0xFFD8836A)
internal val CoralContainerDark = Color(0xFF4A2C21)
internal val OnCoralDark = Color(0xFF2A1710)
internal val BoneText = Color(0xFFF5F4EE)
internal val WarmGreyTextDark = Color(0xFFA8A79F)
internal val WarmBorderDark = Color(0xFF4A4945)
internal val ErrorClayDark = Color(0xFFE0785F)
