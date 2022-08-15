package com.gmail.devpelegrino.pomodorotimer.data.repository

import com.gmail.devpelegrino.pomodorotimer.data.dao.PomodoroDao
import com.gmail.devpelegrino.pomodorotimer.data.entity.toSettingsEntity
import com.gmail.devpelegrino.pomodorotimer.data.model.SettingsModel
import com.gmail.devpelegrino.pomodorotimer.data.model.toSettingsModel

class PomodoroDataSource(
    private val pomodoroDao: PomodoroDao
) : PomodoroRepository {
    override suspend fun createSettings(settings: SettingsModel) {
        pomodoroDao.addSettings(settings.toSettingsEntity())
    }

    override suspend fun updateSettings(settings: SettingsModel) {
        pomodoroDao.updateSettings(settings.toSettingsEntity())
    }

    override suspend fun getSetting(id: Long): SettingsModel {
        return pomodoroDao.getSetting(id).toSettingsModel()
    }
}