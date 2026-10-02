package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ChemTeal,
    secondary = ChemAccent,
    background = BackgroundLight,
    surface = CardBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,      // Recomendado
    onBackground = Color(0xFF1C1B1F), // Recomendado (texto escuro para fundo claro)
    onSurface = Color(0xFF1C1B1F)     // Recomendado (texto escuro para cards)
)

@Composable
fun QuimicaStoreTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography, // Opcional: caso tenha o Typography.kt configurado
        content = content
    )
}