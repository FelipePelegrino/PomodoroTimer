package com.gmail.devpelegrino.pomodorotimer.util

import android.content.Context
import android.os.CountDownTimer
import androidx.annotation.RawRes
import com.gmail.devpelegrino.R
import com.gmail.devpelegrino.pomodorotimer.data.model.SettingsDefault
import com.gmail.devpelegrino.pomodorotimer.data.model.SettingsModel
import com.gmail.devpelegrino.pomodorotimer.data.repository.PomodoroRepository
import com.gmail.devpelegrino.pomodorotimer.enums.PomodoroState
import com.gmail.devpelegrino.pomodorotimer.enums.TimerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PomodoroHandle(
    private val context: Context,
    private val pomodoroRepository: PomodoroRepository,
    private val onTimerTicker: (countDownTimerLeft: Int) -> Unit,
    private val onTimerFinish: () -> Unit
) {

    private var coroutineScope = CoroutineScope(Dispatchers.IO)
    private var mediaPlayerUtils: MediaPlayerUtils? = null

    val pomodoroState = _pomodoroStateFlow.asStateFlow()
    val timerState = _timerStateFlow.asStateFlow()
    val timeSecondsScreen = _timeSecondsScreen.asStateFlow()

    fun actionCountDown() {
        handleAction()
    }

    fun nextAction() {
        resetCountDown()
        nextStage()
    }

    fun loadSettings() {
        if (!classHasBeenOpened) {
            coroutineScope.launch {
                try {
                    settingsModel = pomodoroRepository.getSetting(Constants.UNIQUE_ROW_DATABASE)
                } catch (ex: NullPointerException) {
                    insertDefaultSettings()
                }
                resetState()
                nextStage()
                if (settingsModel.isSound) {
                    mediaPlayerUtils = MediaPlayerUtils(context)
                }
            }
            classHasBeenOpened = true
        } else {
            updateData()
        }
    }

    fun refreshData() {
        val oldSettings = settingsModel
        coroutineScope.launch {
            delay(Constants.DELAY_TO_LOAD_AFTER_SAVE)
            settingsModel = pomodoroRepository.getSetting(Constants.UNIQUE_ROW_DATABASE)
            if (oldSettings != settingsModel) {
                if (isNumberValuesDistinct(oldSettings, settingsModel)) {
                    resetState()
                    nextStage()
                }
            }
        }
    }

    private fun handleAction() {
        val time = getTimeSeconds().getMillisBySecond() + Constants.DELAY_COUNTDOWN
        when (lastTimerState) {
            TimerState.STOP -> {
                countDownTimer = getCountDownTimer(timeMillis = time)
                countDownTimer?.start()
                stopSound()
                playSoundPomodoro()
            }
            TimerState.PAUSE -> {
                countDownTimer = getCountDownTimer(timeMillis = time)
                countDownTimer?.start()
                stopSound()
                playSoundPomodoro()
            }
            else -> {
                stopSound()
                isCountDownSoundPlaying = false
                lastTimerState = TimerState.PAUSE
                countDownTimer?.cancel()
                countDownTimer = null
            }
        }
        coroutineScope.launch {
            delay(100L)
            updateData()
        }
    }

    private suspend fun insertDefaultSettings() {
        coroutineScope.launch {
            settingsModel = SettingsDefault.getSettingsDefault()
            pomodoroRepository.createSettings(settingsModel)
        }.join()
    }

    private fun isNumberValuesDistinct(
        oldSettings: SettingsModel,
        newSettings: SettingsModel
    ): Boolean {
        return (oldSettings.focusMinutes != newSettings.focusMinutes ||
                oldSettings.shortBreakMinutes != newSettings.shortBreakMinutes ||
                oldSettings.longBreakMinutes != newSettings.longBreakMinutes ||
                oldSettings.focusUntilLongBreak != newSettings.focusUntilLongBreak)
    }

    private fun getTimeSeconds(): Int {
        val time = when (lastTimerState) {
            TimerState.STOP -> getTimeSecondsByPomodoroState()
            else -> countDownTimeSecondsLeft
        }
        return time
    }

    private fun getTimeSecondsByPomodoroState(): Int {
        val time = when (lastPomodoroState) {
            PomodoroState.FOCUS -> settingsModel.focusMinutes.getSecondsByMinutes()
            PomodoroState.SHORT_BREAK -> settingsModel.shortBreakMinutes.getSecondsByMinutes()
            else -> settingsModel.longBreakMinutes.getSecondsByMinutes()
        }
        return time
    }

    private fun getNextState(): PomodoroState {
        when (lastPomodoroState) {
            PomodoroState.FOCUS -> {
                focusUntilLongBreak--
                lastPomodoroState = if (focusUntilLongBreak == 0) {
                    resetState()
                    PomodoroState.LONG_BREAK
                } else {
                    PomodoroState.SHORT_BREAK
                }
            }
            PomodoroState.SHORT_BREAK -> {
                lastPomodoroState = PomodoroState.FOCUS
            }
            else -> {
                lastPomodoroState = PomodoroState.FOCUS
            }
        }
        return lastPomodoroState
    }

    private fun nextStage() {
        getNextState()
        updateData()
    }

    private fun updateData() {
        val timeScreen = getTimeSeconds()
        val timerState = getTimerState()

        updateStateFlow(
            pomodoroState = lastPomodoroState,
            timeScreen = timeScreen,
            timerState = timerState
        )
    }

    private fun updateStateFlow(
        pomodoroState: PomodoroState,
        timeScreen: Int,
        timerState: TimerState
    ) {
        _pomodoroStateFlow.update {
            pomodoroState
        }
        _timeSecondsScreen.update {
            timeScreen
        }
        _timerStateFlow.update {
            timerState
        }
    }

    private fun getTimerState(): TimerState {
        return if (countDownTimeSecondsLeft > 0) {
            lastTimerState
        } else {
            TimerState.STOP
        }
    }

    private fun getCountDownTimer(timeMillis: Long): CountDownTimer {
        lastTimerState = TimerState.RUNNING
        return object : CountDownTimer(timeMillis, Constants.MILLISECONDS_TO_ONE_SECOND_LONG) {
            override fun onTick(millisUntilFinished: Long) {
                countDownTimeSecondsLeft = millisUntilFinished.getSecondsByMillis()
                onTimerTicker(millisUntilFinished.getSecondsByMillis())
                if (countDownTimeSecondsLeft < 31 && !isCountDownSoundPlaying) {
                    isCountDownSoundPlaying = true
                    startSound(R.raw.clock)
                }
            }

            override fun onFinish() {
                isCountDownSoundPlaying = false
                stopSound()
                _timeSecondsScreen.update {
                    0
                }
                countDownTimeSecondsLeft = 0
                lastTimerState = TimerState.STOP
                nextStage()
                onTimerFinish()
                if (settingsModel.isAutoResumeTimer) {
                    actionCountDown()
                }
            }
        }
    }

    private fun resetCountDown() {
        _timeSecondsScreen.update {
            0
        }
        countDownTimer?.cancel()
        lastTimerState = TimerState.STOP
        countDownTimeSecondsLeft = 0
    }

    private fun resetState() {
        resetCountDown()
        focusUntilLongBreak = settingsModel.focusUntilLongBreak
        lastPomodoroState = PomodoroState.LONG_BREAK
    }

    private fun playSoundPomodoro() {
        when (lastPomodoroState) {
            PomodoroState.FOCUS -> startSound(R.raw.focus)
            else -> startSound(R.raw.next)
        }
    }

    private fun startSound(@RawRes soundId: Int) {
        if (settingsModel.isSound) {
            mediaPlayerUtils?.let { media ->
                if (media.isMediaPlaying()) {
                    media.stopSound()
                    media.playSound(soundId)
                } else {
                    media.playSound(soundId)
                }
            }
        }
    }

    private fun stopSound() {
        mediaPlayerUtils?.let { media ->
            if (media.isMediaPlaying()) {
                media.stopSound()
            }
        }
    }

    companion object {
        private lateinit var settingsModel: SettingsModel
        private var isCountDownSoundPlaying = false
        private var focusUntilLongBreak = 0
        private var lastPomodoroState = PomodoroState.LONG_BREAK
        private var classHasBeenOpened = false
        private val _pomodoroStateFlow = MutableStateFlow(PomodoroState.LONG_BREAK)
        private val _timeSecondsScreen = MutableStateFlow(0)
        private val _timerStateFlow = MutableStateFlow(TimerState.STOP)

        var lastTimerState = TimerState.STOP
        var countDownTimeSecondsLeft = 0
        var countDownTimer: CountDownTimer? = null
    }
}