package com.gmail.devpelegrino.pomodorotimer.ui.pomodoro

import android.app.Application
import androidx.lifecycle.*
import com.gmail.devpelegrino.pomodorotimer.data.repository.PomodoroRepository
import com.gmail.devpelegrino.pomodorotimer.enums.PomodoroState
import com.gmail.devpelegrino.pomodorotimer.enums.TimerState
import com.gmail.devpelegrino.pomodorotimer.util.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(
    application: Application,
    pomodoroRepository: PomodoroRepository
) : AndroidViewModel(application),
    DefaultLifecycleObserver {

    // Settings
    private var _isSettingsOpen = MutableLiveData<Boolean>()
    val isSettingsOpen: LiveData<Boolean>
        get() = _isSettingsOpen

    private var hasBeenStop = false

    private var _pomodoroState = MutableLiveData<PomodoroState>()
    val pomodoroState: LiveData<PomodoroState>
        get() = _pomodoroState

    private var _countDownMinutes = MutableLiveData<Int>().apply { value = 0 }
    val countDownMinutes: LiveData<Int>
        get() = _countDownMinutes

    private var _countDownSeconds = MutableLiveData<Int>().apply { value = 0 }
    val countDownSeconds: LiveData<Int>
        get() = _countDownSeconds

    // Pomodoro
    private var pomodoroHandle = PomodoroHandle(
        context = application.applicationContext,
        pomodoroRepository = pomodoroRepository,
        onTimerTicker = { convertTime(it) },
        onTimerFinish = { }
    )

    private var _setStartIcon = MutableLiveData<Boolean>().apply { value = true }
    val setStartIcon: LiveData<Boolean>
        get() = _setStartIcon

    // Command service
    private var _startForeground = MutableLiveData<Boolean>().apply { value = false }
    val startForeground: LiveData<Boolean>
        get() = _startForeground

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        loadSettings()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        hasBeenStop = true
        pomodoroHandle.actionCountDown()
        _startForeground.value = true
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if(hasBeenStop) {
            pomodoroHandle.loadSettings()
        }
    }

    fun setTimerState(timerStateName: String) {
        if(timerStateName != "STOP") {
            pomodoroHandle.actionCountDown()
        }
    }

    fun setStartForegroundFalse() {
        _startForeground.value = false
    }

    fun setIsSettingsOpen(isOpen: Boolean) {
        _isSettingsOpen.value = isOpen
    }

    fun handlePlayResume() {
        pomodoroHandle.actionCountDown()
    }

    fun goNextState() {
        pomodoroHandle.nextAction()
    }

    private fun convertTime(time: Int) {
        if (time > 0) {
            _countDownSeconds.value = time.getSecondsInMinuteByTotalSeconds()
            _countDownMinutes.value = time.getMinuteByTotalSeconds()
        }
    }

    private fun loadSettings() {
        pomodoroHandle.loadSettings()
        viewModelScope.launch {
            pomodoroHandle.timeSecondsScreen.collect {
                convertTime(it)
            }
        }
        viewModelScope.launch {
            pomodoroHandle.pomodoroState.collect {
                _pomodoroState.value = it
            }
        }
        viewModelScope.launch {
            pomodoroHandle.timerState.collect {
                when (it) {
                    TimerState.RUNNING -> _setStartIcon.value = false
                    else -> _setStartIcon.value = true
                }
            }
        }
    }

    fun refreshSettings() {
        pomodoroHandle.refreshData()
    }

    class MainViewModelFactory constructor(
        private val application: Application,
        private val pomodoroRepository: PomodoroRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                        application,
                        pomodoroRepository
                    )
                    else -> throw IllegalArgumentException("Class not found")
                }
            } as T
    }
}