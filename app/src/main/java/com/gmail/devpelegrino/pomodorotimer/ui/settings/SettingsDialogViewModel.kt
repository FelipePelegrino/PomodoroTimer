package com.gmail.devpelegrino.pomodorotimer.ui.settings

import android.app.Application
import androidx.lifecycle.*
import com.gmail.devpelegrino.pomodorotimer.data.model.SettingsModel
import com.gmail.devpelegrino.pomodorotimer.data.repository.SettingsRepository
import com.gmail.devpelegrino.pomodorotimer.util.Constants
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
    private var _focusLength = MutableLiveData<Int>().apply { value = 0 }
    val focusLength: LiveData<Int>
        get() = _focusLength

    private var _shortBreakLength = MutableLiveData<Int>().apply { value = 0 }
    val shortBreakLength: LiveData<Int>
        get() = _shortBreakLength

    private var _longBreakLength = MutableLiveData<Int>().apply { value = 0 }
    val longBreakLength: LiveData<Int>
        get() = _longBreakLength

    private var _untilLongBreak = MutableLiveData<Int>().apply { value = 0 }
    val untilLongBreak: LiveData<Int>
        get() = _untilLongBreak

    private var _isAutoResume = MutableLiveData<Boolean>().apply { value = false }
    val isAutoResume: LiveData<Boolean>
        get() = _isAutoResume

    private var _isNotification = MutableLiveData<Boolean>().apply { value = false }
    val isNotification: LiveData<Boolean>
        get() = _isNotification

    private var _isSound = MutableLiveData<Boolean>().apply { value = false }
    val isSound: LiveData<Boolean>
        get() = _isSound

    private var _isDarkMode = MutableLiveData<Boolean>().apply { value = false }
    val isDarkMode: LiveData<Boolean>
        get() = _isDarkMode

    private var _isEnglish = MutableLiveData<Boolean>().apply { value = false }
    val isEnglish: LiveData<Boolean>
        get() = _isEnglish

    private var _isCloseDialog = MutableLiveData<Boolean>().apply { value = false }
    val isCloseDialog: LiveData<Boolean>
        get() = _isCloseDialog

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        loadSettings()
    }

    fun closeSettingsDialog() {
        putSettingsModel()
        _isCloseDialog.value = true
    }

    fun setFocusLength(length: Int) {

    }

    fun setShortBreakLength(length: Int) {

    }

    fun setLongBreakLength(length: Int) {

    }

    fun setUntilLongBreak(length: Int) {

    }

    fun setAutoResume(isEnable: Boolean) {

    }

    fun setNotifications(isEnable: Boolean) {

    }

    fun setSound(isEnable: Boolean) {

    }

    fun setDarkMode(isEnable: Boolean) {

    }

    fun setEnglish(isEnable: Boolean) {

    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsModel = settingsRepository.getSetting(Constants.UNIQUE_ROW_DATABASE)
            getSettingsModel()
        }
    }

    private fun getSettingsModel() = settingsModel.run {
        _focusLength.value = focusMinutes
        _shortBreakLength.value = shortBreakMinutes
        _longBreakLength.value = longBreakMinutes
        _untilLongBreak.value = focusUntilLongBreak
        _isAutoResume.value = isAutoResumeTimer
        _isNotification.value = isNotification
        _isSound.value = isSound
        _isDarkMode.value = isDarkMode
        _isEnglish.value = isEnglish
    }

    private fun putSettingsModel() = settingsModel.run {
        _focusLength.value?.let { focusMinutes = it }
        _shortBreakLength.value?.let { shortBreakMinutes = it }
        _longBreakLength.value?.let { longBreakMinutes = it }
        _untilLongBreak.value?.let { focusUntilLongBreak = it }
        _isAutoResume.value?.let { isAutoResumeTimer = it }
        _isNotification.value?.let { isNotification = it }
        _isSound.value?.let { isSound = it }
        _isDarkMode.value?.let { isDarkMode = it }
        _isEnglish.value?.let { isEnglish = it }
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