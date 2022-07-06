package com.gmail.devpelegrino.pomodorotimer.data.repository

import com.gmail.devpelegrino.pomodorotimer.data.model.SettingsModel

interface SettingsRepository {

    suspend fun createSettings(settings: SettingsModel)

    suspend fun updateSettings(settings: SettingsModel)

    suspend fun getSetting(id: Long): SettingsModel
}