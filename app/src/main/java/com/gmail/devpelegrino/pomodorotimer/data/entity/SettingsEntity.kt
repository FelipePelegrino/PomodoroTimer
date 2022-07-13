package com.gmail.devpelegrino.pomodorotimer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gmail.devpelegrino.pomodorotimer.data.model.SettingsModel
import com.gmail.devpelegrino.pomodorotimer.util.Constants

@Entity(tableName = Constants.SETTINGS_TABLE_NAME)
data class SettingsEntity(
    @PrimaryKey val id: Long = 0,
    val focusLength: Int,
    val shortBreakLength: Int,
    val longBreakLength: Int,
    val untilLongBreak: Int,
    val autoResume: Boolean,
    val sound: Boolean
)

fun SettingsModel.toSettingsEntity(): SettingsEntity {
    return with(this) {
        SettingsEntity(
            focusLength = this.focusMinutes,
            shortBreakLength = this.shortBreakMinutes,
            longBreakLength = this.longBreakMinutes,
            untilLongBreak = this.focusUntilLongBreak,
            autoResume = this.isAutoResumeTimer,
            sound = this.isSound
        )
    }
}