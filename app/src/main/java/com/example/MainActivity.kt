package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.ui.components.FocusMindApp
import com.example.ui.theme.FocusMindTheme
import com.example.ui.theme.applyDarkThemeColors
import com.example.ui.theme.applyLightThemeColors
import com.example.ui.viewmodel.FocusMindViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val viewModel: FocusMindViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("FocusMindSettings", Context.MODE_PRIVATE)
        val lang = prefs.getString("language", "English") ?: "English"
        val localeCode = when (lang) {
            "Русский" -> "ru"
            "Español" -> "es"
            "中文" -> "zh"
            "العربية" -> "ar"
            else -> "en"
        }
        val locale = Locale(localeCode)
        Locale.setDefault(locale)
        val config = newBase.resources.configuration
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeState = viewModel.currentTheme.collectAsState()
            val languageState = viewModel.currentLanguage.collectAsState()
            
            val isDark = when (themeState.value) {
                "Dark" -> true
                "Light" -> false
                else -> isSystemInDarkTheme()
            }
            
            androidx.compose.runtime.LaunchedEffect(isDark) {
                if (isDark) applyDarkThemeColors() else applyLightThemeColors()
            }

            FocusMindTheme(darkTheme = isDark) {
                FocusMindApp(viewModel = viewModel)
            }
        }
    }
}
