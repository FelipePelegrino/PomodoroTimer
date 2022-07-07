package com.gmail.devpelegrino.pomodorotimer.util

object Constants {

    // Handle Time
    const val MILLISECONDS_TO_ONE_SECOND_LONG = 1000L
    const val SECONDS_TO_ONE_MINUTE = 60
    const val TEN_SECONDS = 10
    const val DELAY_COUNT_DOWN = 500L

    // Settings
    const val MAX_FOCUS_MINUTES = 90
    const val MAX_SHORT_BREAK_MINUTES = 30
    const val MAX_LONG_BREAK_MINUTES = 60
    const val MAX_UNTIL_LONG_BREAK = 10

    // Room
    const val SETTINGS_TABLE_NAME = "settings"
    const val APP_DATABASE_VERSION = 1
    const val APP_DATABASE_NAME = "pomodoro_database"
    const val UNIQUE_ROW_DATABASE = 0L

    // SharedPreferences
    const val KEY_SHARED_PREF_APP = "com.gmail.devepelegrino.pomodorotimer"
    const val KEY_SHARED_PREF_DARK_MODE = "isdarkmode"
    const val KEY_SHARED_PREF_ENGLISH = "isenglish"
}