package com.gmail.devpelegrino.pomodorotimer.util

object Constants {

    // Handle Time
    const val MILLISECONDS_TO_ONE_SECOND_LONG = 1000L
    const val SECONDS_TO_ONE_MINUTE = 60
    const val TEN_SECONDS = 10
    const val DELAY_COUNTDOWN = 250L

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

    // Delay
    const val DELAY_TO_LOAD_AFTER_SAVE = 70L

    // Service Actions
    const val MOVE_TO_FOREGROUND = "MOVE_TO_FOREGROUND"
    const val MOVE_TO_BACKGROUND = "MOVE_TO_BACKGROUND"
    const val ACTION_TIME = "ACTION_TIME"

    // Intent Extras
    const val COUNTDOWN_ACTION = "COUNTDOWN_ACTION"
    const val COUNTDOWN_STATE = "COUNTDOWN_STATE"

    // Intent Actions
    const val COUNTDOWN_STATUS = "COUNTDOWN_STATUS"
}