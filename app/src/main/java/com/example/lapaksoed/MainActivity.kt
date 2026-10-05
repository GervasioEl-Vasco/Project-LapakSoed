package com.example.lapaksoed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.lapaksoed.ui.navigation.AppNavigation
import com.example.lapaksoed.ui.theme.LapakSoedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val preferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            var darkTheme by remember { mutableStateOf(preferences.getBoolean("dark_theme", false)) }
            LapakSoedTheme(darkTheme = darkTheme, dynamicColor = false) {
                AppNavigation(
                    isDarkTheme = darkTheme,
                    onDarkThemeChange = { enabled ->
                        darkTheme = enabled
                        preferences.edit().putBoolean("dark_theme", enabled).apply()
                    }
                )
            }
        }
    }
}
