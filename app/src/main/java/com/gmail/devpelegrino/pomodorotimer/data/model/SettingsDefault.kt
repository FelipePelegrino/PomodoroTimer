package com.gmail.devpelegrino.pomodorotimer.data.model

class SettingsDefault {
    companion object {
        fun getSettingsDefault(): SettingsModel {
            return SettingsModel(
                focusMinutes = 25,
                shortBreakMinutes = 5,
                longBreakMinutes = 10,
                focusUntilLongBreak = 4,
                isAutoResumeTimer = false,
                isSound = true
            )
        }
    }
}