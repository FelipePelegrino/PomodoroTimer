package com.gmail.devpelegrino.pomodorotimer.ui.settings

import android.app.Application
import androidx.lifecycle.*
import com.gmail.devpelegrino.pomodorotimer.data.model.SettingsModel
import com.gmail.devpelegrino.pomodorotimer.data.repository.SettingsRepository
import com.gmail.devpelegrino.pomodorotimer.util.Constants
import com.gmail.devpelegrino.pomodorotimer.util.SharedPreferencesUtils
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class SettingsDialogViewModel(
    application: Application,
    private val settingsRepository: SettingsRepository
) : AndroidViewModel(application),
    DefaultLifecycleObserver {

    // Data
    private lateinit var settingsModel: SettingsModel

    // Settings
    private var _focusLength = MutableLiveData<Int>()
    val focusLength: LiveData<Int>
        get() = _focusLength

    private var _shortBreakLength = MutableLiveData<Int>()
    val shortBreakLength: LiveData<Int>
        get() = _shortBreakLength

    private var _longBreakLength = MutableLiveData<Int>()
    val longBreakLength: LiveData<Int>
        get() = _longBreakLength

    private var _untilLongBreak = MutableLiveData<Int>()
    val untilLongBreak: LiveData<Int>
        get() = _untilLongBreak

    private var _isAutoResume = MutableLiveData<Boolean>()
    val isAutoResume: LiveData<Boolean>
        get() = _isAutoResume

    private var _isNotification = MutableLiveData<Boolean>()
    val isNotification: LiveData<Boolean>
        get() = _isNotification

    private var _isSound = MutableLiveData<Boolean>()
    val isSound: LiveData<Boolean>
        get() = _isSound

    private var _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean>
        get() = _isDarkMode

    private var _isCloseDialog = MutableLiveData<Boolean>()
    val isCloseDialog: LiveData<Boolean>
        get() = _isCloseDialog

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        loadSharedPreferences()
        loadSettings()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        saveSettings()
    }

    fun closeSettingsDialog() {
        _isCloseDialog.value = true
    }

    fun setFocusLength(length: Int) {
        _focusLength.value = length
    }

    fun setShortBreakLength(length: Int) {
        _shortBreakLength.value = length
    }

    fun setLongBreakLength(length: Int) {
        _longBreakLength.value = length
    }

    fun setUntilLongBreak(length: Int) {
        _untilLongBreak.value = length
    }

    fun setAutoResume(isEnable: Boolean) {
        _isAutoResume.value = isEnable
    }

    fun setNotifications(isEnable: Boolean) {
        _isNotification.value = isEnable
    }

    fun setSound(isEnable: Boolean) {
        _isSound.value = isEnable
    }

    fun setDarkMode(isEnable: Boolean) {
        SharedPreferencesUtils.setDarkMode(getApplication<Application>().applicationContext, isEnable)
        _isDarkMode.value = isEnable
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsModel = settingsRepository.getSetting(Constants.UNIQUE_ROW_DATABASE)
            getSettingsModel()
        }
    }

    private fun saveSettings() {
        if (isNeededSave()) {
            putSettingsModel()
            viewModelScope.launch {
                settingsRepository.updateSettings(settingsModel)
            }
        }
    }

    private fun loadSharedPreferences() {
        _isDarkMode.value = SharedPreferencesUtils.getDarkMode(getApplication<Application>().applicationContext)
    }

    private fun getSettingsModel() = settingsModel.run {
        _focusLength.value = focusMinutes
        _shortBreakLength.value = shortBreakMinutes
        _longBreakLength.value = longBreakMinutes
        _untilLongBreak.value = focusUntilLongBreak
        _isAutoResume.value = isAutoResumeTimer
        _isNotification.value = isNotification
        _isSound.value = isSound
    }

    private fun putSettingsModel() = settingsModel.run {
        _focusLength.value?.let { focusMinutes = it }
        _shortBreakLength.value?.let { shortBreakMinutes = it }
        _longBreakLength.value?.let { longBreakMinutes = it }
        _untilLongBreak.value?.let { focusUntilLongBreak = it }
        _isAutoResume.value?.let { isAutoResumeTimer = it }
        _isNotification.value?.let { isNotification = it }
        _isSound.value?.let { isSound = it }
    }

    private fun isNeededSave(): Boolean = settingsModel.run {
        return (_focusLength.value != focusMinutes ||
                _shortBreakLength.value != shortBreakMinutes ||
                _longBreakLength.value != longBreakMinutes ||
                _untilLongBreak.value != focusUntilLongBreak ||
                _isAutoResume.value != isAutoResumeTimer ||
                _isNotification.value != isNotification ||
                _isSound.value != isSound)
    }

    class SettingsDialogViewModelFactory constructor(
        private val application: Application,
        private val settingsRepository: SettingsRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(SettingsDialogViewModel::class.java) -> SettingsDialogViewModel(
                        application,
                        settingsRepository
                    )
                    else -> throw IllegalArgumentException("Class not found")
                }
            } as T
    }
}