package com.gmail.devpelegrino.ui

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.gmail.devpelegrino.R
import com.gmail.devpelegrino.util.PomodoroState

class MainActivity : AppCompatActivity() {

    private lateinit var itemPomodoroState: ItemPomodoroState
    private lateinit var focusState: PomodoroState
    private lateinit var shortBreak: PomodoroState
    private lateinit var longBreak: PomodoroState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ConstraintLayout>(R.id.constraintRoot).setOnClickListener {
            changeAppTheme()
        }

        itemPomodoroState = findViewById(R.id.item_pomodoro_state)
        setStates()
        teste()
    }

    private fun changeAppTheme() {
        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (isNightTheme) {
            Configuration.UI_MODE_NIGHT_YES ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Configuration.UI_MODE_NIGHT_NO ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun teste() {
        itemPomodoroState.setStateType(longBreak)
    }

    private fun setStates() {
        focusState = PomodoroState(
            name = getString(R.string.focus_name),
            primaryColor = resources.getColor(R.color.colorFocus, applicationContext.theme),
            secondaryColor = resources.getColor(R.color.colorFocusSecondary, applicationContext.theme),
            tertiaryColor = resources.getColor(R.color.colorFocusTertiary, applicationContext.theme),
            iconSrc = resources.getDrawable(R.drawable.ic_focus, applicationContext.theme)
        )
        shortBreak = PomodoroState(
            name = getString(R.string.short_break_name),
            primaryColor = resources.getColor(R.color.colorShortBreak, applicationContext.theme),
            secondaryColor = resources.getColor(R.color.colorShortBreakSecondary, applicationContext.theme),
            tertiaryColor = resources.getColor(R.color.colorShortBreakTertiary, applicationContext.theme),
            iconSrc = resources.getDrawable(R.drawable.ic_coffee, applicationContext.theme)
        )
        longBreak = PomodoroState(
            name = getString(R.string.long_break_name),
            primaryColor = resources.getColor(R.color.colorLongBreak, applicationContext.theme),
            secondaryColor = resources.getColor(R.color.colorLongBreakSecondary, applicationContext.theme),
            tertiaryColor = resources.getColor(R.color.colorLongBreakTertiary, applicationContext.theme),
            iconSrc = resources.getDrawable(R.drawable.ic_coffee, applicationContext.theme)
        )
    }
}