package com.gmail.devpelegrino.pomodorotimer.ui.pomodoro

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.gmail.devpelegrino.pomodorotimer.data.model.SettingsDefault
import com.gmail.devpelegrino.pomodorotimer.data.model.SettingsModel
import com.gmail.devpelegrino.pomodorotimer.data.repository.SettingsRepository
import com.gmail.devpelegrino.pomodorotimer.enums.PomodoroState
import com.gmail.devpelegrino.pomodorotimer.enums.TimerState
import com.gmail.devpelegrino.pomodorotimer.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

class MainViewModel(
    application: Application,
    private val settingsRepository: SettingsRepository
) : AndroidViewModel(application),
    DefaultLifecycleObserver {

    // Settings
    private lateinit var settingsModel: SettingsModel
    private var pomodoroStateHandle = PomodoroStateHandle()

    private var _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean>
        get() = _isDarkMode

    private var _isEnglish = MutableLiveData<Boolean>()
    val isEnglish: LiveData<Boolean>
        get() = _isEnglish

    private var _isAutoResume = MutableLiveData<Boolean>()
    val isAutoResume: LiveData<Boolean>
        get() = _isAutoResume

    private var _isNotification = MutableLiveData<Boolean>()
    val isNotification: LiveData<Boolean>
        get() = _isNotification

    private var _isSound = MutableLiveData<Boolean>()
    val isSound: LiveData<Boolean>
        get() = _isSound

    private var _isSettingsOpen = MutableLiveData<Boolean>()
    val isSettingsOpen: LiveData<Boolean>
        get() = _isSettingsOpen

    // CountDownVariables
    private var timerStateNow: TimerState = TimerState.STOP
    private var countDownSecondsLeft: Int = 0
    private var countDownTimer: CountDownTimer? = null

    private var _countDownMinutes = MutableLiveData<Int>().apply { value = 0 }
    val countDownMinutes: LiveData<Int>
        get() = _countDownMinutes

    private var _countDownSeconds = MutableLiveData<Int>().apply { value = 0 }
    val countDownSeconds: LiveData<Int>
        get() = _countDownSeconds

    // UI updates
    private var _setStartIcon = MutableLiveData<Boolean>().apply { value = true }
    val setStartIcon: LiveData<Boolean>
        get() = _setStartIcon

    private var _pomodoroState = MutableLiveData<PomodoroState>()
    val pomodoroState: LiveData<PomodoroState>
        get() = _pomodoroState

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        loadSharedPreferences()
        loadSettings()
    }

    fun goNextState() {
        stopCountDownTimer()
    }

    fun handlePlayResume() {
        val time = when(pomodoroState.value) {
            PomodoroState.SHORT_BREAK -> {
                settingsModel.shortBreakMinutes
            }
            PomodoroState.LONG_BREAK -> {
                settingsModel.longBreakMinutes
            }
            else -> {
                settingsModel.focusMinutes
            }
        }
        handleCountDownTimer(time)
    }

    fun setIsSettingsOpen(isOpen: Boolean) {
        _isSettingsOpen.value = isOpen
    }

    fun refreshSettings() {
        val oldSettings = settingsModel
        viewModelScope.launch {
            delay(Constants.DELAY_TO_LOAD_AFTER_SAVE)
            settingsModel = settingsRepository.getSetting(Constants.UNIQUE_ROW_DATABASE)
            if(oldSettings != settingsModel) {
                if(isNumberValuesDistinct(oldSettings, settingsModel)) {
                    resetTimer()
                }
                setNoNumberSettings()
            }
        }
    }

    private fun isNumberValuesDistinct(oldSettings: SettingsModel, newSettings: SettingsModel): Boolean {
        return (oldSettings.focusMinutes != newSettings.focusMinutes ||
                oldSettings.shortBreakMinutes != newSettings.shortBreakMinutes ||
                oldSettings.longBreakMinutes != newSettings.longBreakMinutes ||
                oldSettings.focusUntilLongBreak != newSettings.focusUntilLongBreak )
    }

    private fun handleCountDownTimer(timeMinutes: Int) {
        when (timerStateNow) {
            TimerState.STOP -> {
                startCountDownTimer(timeMinutes.getSecondsByMinutes())
            }
            TimerState.PAUSE -> {
                startCountDownTimer(countDownSecondsLeft)
            }
            else -> {
                pauseCountDownTimer()
            }
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                settingsModel =  settingsRepository.getSetting(Constants.UNIQUE_ROW_DATABASE)
            } catch (ex: NullPointerException) {
                insertDefaultSettings()
            }
            setNoNumberSettings()
            resetTimer()
        }
    }

    private fun resetTimer() {
        pomodoroStateHandle.setFocusUntilLong(settingsModel.focusUntilLongBreak)
        _pomodoroState.value = pomodoroStateHandle.getNextState()
        setTimeInUiAfterNextState()
    }

    private fun setNoNumberSettings() {
        _isAutoResume.value = settingsModel.isAutoResumeTimer
        _isNotification.value = settingsModel.isNotification
        _isSound.value = settingsModel.isSound
    }

    private fun loadSharedPreferences() {
        _isDarkMode.value = SharedPreferencesUtils.getDarkMode(getApplication<Application>().applicationContext)
        _isEnglish.value = SharedPreferencesUtils.getEnglish(getApplication<Application>().applicationContext)
    }

    private fun setCountDownTimer(timeSeconds: Int) {
        countDownTimer = getCountDownTimer(timeSeconds.getMillisBySecond() + Constants.DELAY_COUNT_DOWN)
    }

    private fun startCountDownTimer(timeSeconds: Int) {
        timerStateNow = TimerState.RUNNING
        setCountDownTimer(timeSeconds)
        _setStartIcon.value = false
        countDownTimer?.start()
    }

    private fun pauseCountDownTimer() {
        timerStateNow = TimerState.PAUSE
        _setStartIcon.value = true
        countDownTimer?.cancel()
    }

    private fun stopCountDownTimer() {
        _pomodoroState.value = pomodoroStateHandle.getNextState()
        setTimeInUiAfterNextState()
        timerStateNow = TimerState.STOP
        _setStartIcon.value = true
        countDownTimer?.cancel()
    }

    private fun setTimeInUiAfterNextState() {
        when(pomodoroState.value) {
            PomodoroState.SHORT_BREAK -> {
                _countDownSeconds.value = 0
                _countDownMinutes.value = settingsModel.shortBreakMinutes
            }
            PomodoroState.LONG_BREAK -> {
                _countDownSeconds.value = 0
                _countDownMinutes.value = settingsModel.longBreakMinutes
            }
            else -> {
                _countDownSeconds.value = 0
                _countDownMinutes.value = settingsModel.focusMinutes
            }
        }
    }

    private fun getCountDownTimer(timeMillis: Long): CountDownTimer {
        return object : CountDownTimer(timeMillis, Constants.MILLISECONDS_TO_ONE_SECOND_LONG) {
            override fun onTick(millisUntilFinished: Long) {
                countDownSecondsLeft = millisUntilFinished.getSecondsByMillis()
                _countDownSeconds.value = countDownSecondsLeft.getSecondsInMinuteByTotalSeconds()
                _countDownMinutes.value = countDownSecondsLeft.getMinuteByTotalSeconds()
            }

            override fun onFinish() {
                stopCountDownTimer()
            }
        }
    }

    private fun insertDefaultSettings() {
        viewModelScope.launch {
            settingsModel = SettingsDefault.getSettingsDefault()
            settingsRepository.createSettings(settingsModel)
        }
    }

    class MainViewModelFactory constructor(
        private val application: Application,
        private val settingsRepository: SettingsRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                        application,
                        settingsRepository
                    )
                    else -> throw IllegalArgumentException("Class not found")
                }
            } as T
    }
}