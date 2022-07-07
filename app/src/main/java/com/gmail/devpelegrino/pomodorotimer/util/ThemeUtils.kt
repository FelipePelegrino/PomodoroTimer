package com.gmail.devpelegrino.pomodorotimer.util

import androidx.appcompat.app.AppCompatDelegate

class ThemeUtils {
    companion object {
        fun changeAppTheme(isDarkMode: Boolean) {
            if(isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}