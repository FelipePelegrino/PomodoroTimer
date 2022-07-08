package com.gmail.devpelegrino.pomodorotimer.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtils {
    companion object {
        fun getDarkMode(context: Context): Boolean {
            val preferences = getSharedPreferences(context)
            return preferences.getBoolean(Constants.KEY_SHARED_PREF_DARK_MODE, false)
        }

        fun setDarkMode(context: Context, isDarkMode: Boolean) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(Constants.KEY_SHARED_PREF_DARK_MODE, isDarkMode)
            editor.apply()
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(Constants.KEY_SHARED_PREF_APP, Context.MODE_PRIVATE)
        }
    }
}