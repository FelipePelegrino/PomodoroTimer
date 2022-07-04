package com.gmail.devpelegrino.ui.settings

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import java.lang.IllegalArgumentException

class SettingsDialogViewModel(application: Application) : AndroidViewModel(application),
    DefaultLifecycleObserver {

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
        //TODO: save all data
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
        //TODO: load all data
        Log.i("teste", "loadSettings: teste")
    }

    class SettingsDialogViewModelFactory constructor(
        private val application: Application
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(SettingsDialogViewModel::class.java) -> SettingsDialogViewModel(application)
                    else -> throw IllegalArgumentException("Class not found")
                }
            } as T
    }
}