package com.gmail.devpelegrino.pomodorotimer.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gmail.devpelegrino.pomodorotimer.data.dao.SettingsDao
import com.gmail.devpelegrino.pomodorotimer.data.entity.SettingsEntity
import com.gmail.devpelegrino.pomodorotimer.util.Constants

@Database(
    entities = [SettingsEntity::class],
    version = Constants.APP_DATABASE_VERSION,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.APP_DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
