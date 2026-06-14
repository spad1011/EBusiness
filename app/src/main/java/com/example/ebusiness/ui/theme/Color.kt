package com.example.ebusiness.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// StagePot Brand Colors
val StagePotPurple = Color(0xFF7C3AED)
val StagePotPurpleDark = Color(0xFF5B21B6)
val StagePotGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF4A8AFF), Color(0xFF6B60F0))
)

// Light Mode Palette
val LightPrimary         = Color(0xFF7C3AED)   // Violet-600
val LightOnPrimary       = Color(0xFFFFFFFF)
val LightPrimaryContainer    = Color(0xFFEDE9FE)   // Violet-100
val LightOnPrimaryContainer  = Color(0xFF3B0764)

val LightSecondary       = Color(0xFF8B5CF6)   // Violet-500
val LightOnSecondary     = Color(0xFFFFFFFF)
val LightSecondaryContainer  = Color(0xFFF5F3FF)
val LightOnSecondaryContainer = Color(0xFF5B21B6)

val LightTertiary        = Color(0xFFEC4899)   // Pink-500
val LightOnTertiary      = Color(0xFFFFFFFF)
val LightTertiaryContainer   = Color(0xFFFCE7F3)
val LightOnTertiaryContainer = Color(0xFF831843)

val LightBackground      = Color(0xFFFAF9FF)   // Leicht violett getöntes Weiß
val LightOnBackground    = Color(0xFF1A1625)
val LightSurface         = Color(0xFFFFFFFF)
val LightOnSurface       = Color(0xFF1A1625)
val LightSurfaceVariant  = Color(0xFFF3F0FF)
val LightOnSurfaceVariant = Color(0xFF49454F)
val LightOutline         = Color(0xFF7C3AED).copy(alpha = 0.4f)
val LightError           = Color(0xFFEF4444)
val LightOnError         = Color(0xFFFFFFFF)

// Dark Mode Palette
val DarkPrimary          = Color(0xFFA78BFA)   // Violet-400 – heller auf dunklem BG
val DarkOnPrimary        = Color(0xFF2E1065)
val DarkPrimaryContainer     = Color(0xFF5B21B6)
val DarkOnPrimaryContainer   = Color(0xFFEDE9FE)

val DarkSecondary        = Color(0xFFC4B5FD)   // Violet-300
val DarkOnSecondary      = Color(0xFF3B0764)
val DarkSecondaryContainer   = Color(0xFF4C1D95)
val DarkOnSecondaryContainer = Color(0xFFEDE9FE)

val DarkTertiary         = Color(0xFFF9A8D4)   // Pink-300
val DarkOnTertiary       = Color(0xFF831843)
val DarkTertiaryContainer    = Color(0xFF9D174D)
val DarkOnTertiaryContainer  = Color(0xFFFCE7F3)

val DarkBackground       = Color(0xFF0F0E17)   // Sehr dunkles Lila-Schwarz
val DarkOnBackground     = Color(0xFFEFECFF)
val DarkSurface          = Color(0xFF1A1826)   // Dunkle lila Oberfläche
val DarkOnSurface        = Color(0xFFEFECFF)
val DarkSurfaceVariant   = Color(0xFF251F3D)
val DarkOnSurfaceVariant = Color(0xFFC4B5FD)
val DarkOutline          = Color(0xFF7C7589)
val DarkError            = Color(0xFFF87171)
val DarkOnError          = Color(0xFF7F1D1D)
