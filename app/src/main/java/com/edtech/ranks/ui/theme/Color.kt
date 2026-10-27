package com.edtech.ranks.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

// Brand Colors (Kinetic Neo-Academic)
val Surface = Color(0xFFFAF8FF)
val SurfaceDim = Color(0xFFD2D9F4)
val SurfaceBright = Color(0xFFFAF8FF)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFF2F3FF)
val SurfaceContainer = Color(0xFFEAEDFF)
val SurfaceContainerHigh = Color(0xFFE2E7FF)
val SurfaceContainerHighest = Color(0xFFDAE2FD)

val OnSurface = Color(0xFF131B2E)
val OnSurfaceVariant = Color(0xFF464555)
val InverseSurface = Color(0xFF283044)
val InverseOnSurface = Color(0xFFEEF0FF)

val Outline = Color(0xFF777587)
val OutlineVariant = Color(0xFFC7C4D8)

val Primary = Color(0xFF3525CD)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFF4F46E5)
val OnPrimaryContainer = Color(0xFFDAD7FF)
val InversePrimary = Color(0xFFC3C0FF)

val Secondary = Color(0xFF006C49)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFF6CF8BB)
val OnSecondaryContainer = Color(0xFF00714D)

val Tertiary = Color(0xFF684000)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFF885500)
val OnTertiaryContainer = Color(0xFFFFD4A4)

val ErrorColor = Color(0xFFBA1A1A)
val OnErrorColor = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)
val OnErrorContainer = Color(0xFF93000A)

// Legacy aliases for backwards compatibility with un-refactored screens
val background: Color @Composable get() = MaterialTheme.colorScheme.background
val onBackground: Color @Composable get() = MaterialTheme.colorScheme.onBackground
val surface: Color @Composable get() = MaterialTheme.colorScheme.surface
val onSurface: Color @Composable get() = MaterialTheme.colorScheme.onSurface
val surfaceVariant: Color @Composable get() = MaterialTheme.colorScheme.surfaceVariant
val onSurfaceVariant: Color @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
val primary: Color @Composable get() = MaterialTheme.colorScheme.primary
val onPrimary: Color @Composable get() = MaterialTheme.colorScheme.onPrimary
val primaryContainer: Color @Composable get() = MaterialTheme.colorScheme.primaryContainer
val onPrimaryContainer: Color @Composable get() = MaterialTheme.colorScheme.onPrimaryContainer
val secondary: Color @Composable get() = MaterialTheme.colorScheme.secondary
val onSecondary: Color @Composable get() = MaterialTheme.colorScheme.onSecondary
val secondaryContainer: Color @Composable get() = MaterialTheme.colorScheme.secondaryContainer
val onSecondaryContainer: Color @Composable get() = MaterialTheme.colorScheme.onSecondaryContainer
val tertiary: Color @Composable get() = MaterialTheme.colorScheme.tertiary
val onTertiary: Color @Composable get() = MaterialTheme.colorScheme.onTertiary
val tertiaryContainer: Color @Composable get() = MaterialTheme.colorScheme.tertiaryContainer
val onTertiaryContainer: Color @Composable get() = MaterialTheme.colorScheme.onTertiaryContainer
val error: Color @Composable get() = MaterialTheme.colorScheme.error
val onError: Color @Composable get() = MaterialTheme.colorScheme.onError
val errorContainer: Color @Composable get() = MaterialTheme.colorScheme.errorContainer
val onErrorContainer: Color @Composable get() = MaterialTheme.colorScheme.onErrorContainer
val outline: Color @Composable get() = MaterialTheme.colorScheme.outline
val outlineVariant: Color @Composable get() = MaterialTheme.colorScheme.outlineVariant

// Custom surface containers mapped to standard material tokens
val surfaceContainerHighest: Color @Composable get() = SurfaceContainerHighest
val surfaceContainerHigh: Color @Composable get() = SurfaceContainerHigh
val surfaceContainer: Color @Composable get() = SurfaceContainer
val surfaceContainerLow: Color @Composable get() = SurfaceContainerLow
val surfaceContainerLowest: Color @Composable get() = SurfaceContainerLowest
val surfaceBright: Color @Composable get() = SurfaceBright

val GlassBackground: Color @Composable get() = SurfaceContainerLowest.copy(alpha = 0.85f)
val GlassBorder: Color @Composable get() = OutlineVariant
val GlowColor: Color @Composable get() = PrimaryContainer

