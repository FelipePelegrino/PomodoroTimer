package com.gmail.devpelegrino.util

import com.gmail.devpelegrino.enum.PomodoroState

class PomodoroStateHandle {

    fun getNextState(): PomodoroState {
        when(lastState) {
            PomodoroState.FOCUS -> {
                focusUntilLong--
                lastState = if(focusUntilLong == 0) {
                    resetState()
                    PomodoroState.LONG_BREAK
                } else {
                    PomodoroState.SHORT_BREAK
                }
            }
            PomodoroState.SHORT_BREAK -> {
                lastState = PomodoroState.FOCUS
            }
            else -> {
                lastState = PomodoroState.FOCUS
            }
        }
        return lastState
    }

    fun setFocusUntilLong(newFocusUntilLong: Int) {
        focusUntilLongSettings = newFocusUntilLong
        resetState()
    }

    private fun resetState() {
        focusUntilLong = focusUntilLongSettings
        lastState = PomodoroState.LONG_BREAK
    }

    companion object {
        private var focusUntilLong: Int = 0
        private var focusUntilLongSettings: Int = 0
        private var lastState: PomodoroState = PomodoroState.LONG_BREAK
    }
}