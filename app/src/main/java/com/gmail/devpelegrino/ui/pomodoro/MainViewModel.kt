package com.gmail.devpelegrino.ui.pomodoro

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.gmail.devpelegrino.data.SettingsFake
import com.gmail.devpelegrino.data.SettingsModel
import com.gmail.devpelegrino.enum.PomodoroState
import com.gmail.devpelegrino.enum.TimerState
import com.gmail.devpelegrino.util.*
import java.lang.IllegalArgumentException

class MainViewModel(application: Application) : AndroidViewModel(application),
    DefaultLifecycleObserver {

    // Settings
    private var settingsModel: SettingsModel = SettingsFake.getSettingsFake()
    private var pomodoroStateHandle = PomodoroStateHandle()

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
        pomodoroStateHandle.setFocusUntilLong(settingsModel.focusUntilLongBreak)
        _pomodoroState.value = pomodoroStateHandle.getNextState()
        setTimeInUiAfterNextState()
        //TODO: carregara as configurações locais de um SQLite ou SharedPreferences
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

    class MainViewModelFactory constructor(
        private val application: Application
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(MainViewModel::class.java) -> MainViewModel(application)
                    else -> throw IllegalArgumentException("Class not found")
                }
            } as T
    }
}