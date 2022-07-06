package com.gmail.devpelegrino.pomodorotimer.data.model

import com.gmail.devpelegrino.pomodorotimer.data.entity.SettingsEntity

data class SettingsModel(
    var focusMinutes: Int,
    var shortBreakMinutes: Int,
    var longBreakMinutes: Int,
    var focusUntilLongBreak: Int,
    var isDarkMode: Boolean,
    var isAutoResumeTimer: Boolean,
    var isSound: Boolean,
    var isNotification: Boolean,
    var isEnglish: Boolean
)

fun SettingsEntity.toSettingsModel(): SettingsModel {
    return with(this) {
        SettingsModel(
            focusMinutes = this.focusLength,
            shortBreakMinutes = this.shortBreakLength,
            longBreakMinutes = this.longBreakLength,
            focusUntilLongBreak = this.untilLongBreak,
            isAutoResumeTimer = this.autoResume,
            isNotification = this.notification,
            isSound = this.sound,
            isDarkMode = this.darkMode,
            isEnglish = this.english
        )
    }
}