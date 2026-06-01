package com.example.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// Premium Obsidian Theme Palette
var DeepBlack by mutableStateOf(Color(0xFF070708))
var CardGraphite by mutableStateOf(Color(0xFF121215))
var BorderGraphite by mutableStateOf(Color(0xFF222228))
var AccentBlue by mutableStateOf(Color(0xFF2A85FF))
var AccentPurple by mutableStateOf(Color(0xFF9B6DFF))
var TextWhite by mutableStateOf(Color(0xFFFCFCFD))
var TextMuted by mutableStateOf(Color(0xFF8E8E9B))
var ActiveGreen by mutableStateOf(Color(0xFF10B981))
var AlertRed by mutableStateOf(Color(0xFFEF4444))
var AmberYellow by mutableStateOf(Color(0xFFFBBF24))

fun applyDarkThemeColors() {
    DeepBlack = Color(0xFF070708)
    CardGraphite = Color(0xFF121215)
    BorderGraphite = Color(0xFF222228)
    TextWhite = Color(0xFFFCFCFD)
    TextMuted = Color(0xFF8E8E9B)
    AccentBlue = Color(0xFF2A85FF)
    AccentPurple = Color(0xFF9B6DFF)
}

fun applyLightThemeColors() {
    DeepBlack = Color(0xFFF0F0F5)
    CardGraphite = Color(0xFFFFFFFF)
    BorderGraphite = Color(0xFFE0E0E5)
    TextWhite = Color(0xFF111111)
    TextMuted = Color(0xFF555555)
    AccentBlue = Color(0xFF1A5CCD) // Darker blue for visibility
    AccentPurple = Color(0xFF5A30D0) // Darker purple
}

// Keep default theme compatible declarations for safety
val Purple80 = Color(0xFF9B6DFF)
val PurpleGrey80 = Color(0xFF8E8E9B)
val Pink80 = Color(0xFF2A85FF)
val Purple40 = Color(0xFF5A30D0)
val PurpleGrey40 = Color(0xFF53535F)
val Pink40 = Color(0xFF1A5CCD)
