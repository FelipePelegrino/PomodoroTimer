package com.gmail.devpelegrino.data.model

data class SettingsModel(
    var focusMinutes: Int,
    var shortBreakMinutes: Int,
    var longBreakMinutes: Int,
    var focusUntilLongBreak: Int,
    var isDarkMode: Boolean,
    var isAutoResumeTimer: Boolean,
    var isSound: Boolean,
    var isNotification: Boolean
)