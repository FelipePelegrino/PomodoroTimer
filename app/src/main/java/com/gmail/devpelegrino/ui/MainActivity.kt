package com.gmail.devpelegrino.ui

import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.devpelegrino.R
import com.gmail.devpelegrino.databinding.ActivityMainBinding
import com.gmail.devpelegrino.util.Constants
import com.gmail.devpelegrino.util.PomodoroState

class MainActivity : AppCompatActivity() {

    private lateinit var itemPomodoroState: ItemPomodoroState
    private lateinit var focusState: PomodoroState
    private lateinit var shortBreak: PomodoroState
    private lateinit var longBreak: PomodoroState
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupMainActivity()
        setStates()
        setObservables()
        teste()
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

    private fun setupMainActivity() {
        viewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(application)
        )[MainViewModel::class.java]
        lifecycle.addObserver(viewModel)
        itemPomodoroState = findViewById(R.id.itemPomodoroState)
    }

    private fun setObservables() {
        viewModel.run {
            this.countDownMinutes.observe(this@MainActivity, Observer {
                binding.minutesText.text = addZeroLeftToString(it)
            })
            this.countDownSeconds.observe(this@MainActivity, Observer {
                binding.secondsText.text = addZeroLeftToString(it)
            })
        }
    }

    private fun addZeroLeftToString(number: Int): String {
        return if(number < Constants.TEN_SECONDS) {
            "0$number"
        } else {
            number.toString()
        }
    }

    private fun teste() {
        itemPomodoroState.setStateType(focusState)
    }
}