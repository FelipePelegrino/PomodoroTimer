package com.gmail.devpelegrino.pomodorotimer.data.model

class SettingsFake {
    companion object {
        fun getSettingsFake(): SettingsModel {
            return SettingsModel(
                focusMinutes = 25,
                shortBreakMinutes = 5,
                longBreakMinutes = 10,
                focusUntilLongBreak = 4,
                isDarkMode = false,
                isAutoResumeTimer = false,
                isSound = false, //TODO: true
                isNotification = false, //TODO: true
                isEnglish = false
            )
        }
    }
}