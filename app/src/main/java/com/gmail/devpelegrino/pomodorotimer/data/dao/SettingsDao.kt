package com.gmail.devpelegrino.pomodorotimer.data.dao

import androidx.room.*
import com.gmail.devpelegrino.pomodorotimer.data.entity.SettingsEntity
import com.gmail.devpelegrino.pomodorotimer.util.Constants

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSettings(settings: SettingsEntity)

    @Update
    suspend fun updateSettings(settings: SettingsEntity)

    @Query("SELECT * FROM ${Constants.SETTINGS_TABLE_NAME} WHERE id = :id")
    suspend fun getSetting(id: Long): SettingsEntity
}