package com.gmail.devpelegrino.data

class SettingsFake {
    companion object {
        fun getSettingsFake(): SettingsModel {
            return SettingsModel(
                focusMinutes = 2,
                shortBreakMinutes = 1,
                longBreakMinutes = 3,
                focusUntilLongBreak = 2,
                isDarkMode = false,
                isAutoResumeTimer = false,
                isSound = false,
                isNotification = false
            )
        }
    }
}