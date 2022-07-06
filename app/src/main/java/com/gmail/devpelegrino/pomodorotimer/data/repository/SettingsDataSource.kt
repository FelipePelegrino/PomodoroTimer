package com.gmail.devpelegrino.pomodorotimer.data.repository

import com.gmail.devpelegrino.pomodorotimer.data.dao.SettingsDao
import com.gmail.devpelegrino.pomodorotimer.data.entity.toSettingsEntity
import com.gmail.devpelegrino.pomodorotimer.data.model.SettingsModel
import com.gmail.devpelegrino.pomodorotimer.data.model.toSettingsModel

class SettingsDataSource(
    private val settingsDao: SettingsDao
) : SettingsRepository {
    override suspend fun createSettings(settings: SettingsModel) {
        settingsDao.addSettings(settings.toSettingsEntity())
    }

    override suspend fun updateSettings(settings: SettingsModel) {
        settingsDao.updateSettings(settings.toSettingsEntity())
    }

    override suspend fun getSetting(id: Long): SettingsModel {
        return settingsDao.getSetting(id).toSettingsModel()
    }
}