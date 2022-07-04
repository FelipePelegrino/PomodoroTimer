package com.gmail.devpelegrino.ui.pomodoro

import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.gmail.devpelegrino.R
import com.gmail.devpelegrino.databinding.ActivityMainBinding
import com.gmail.devpelegrino.enum.PomodoroState
import com.gmail.devpelegrino.ui.settings.SettingsDialogFragment
import com.gmail.devpelegrino.util.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var focusState: PomodoroStateUI
    private lateinit var shortBreak: PomodoroStateUI
    private lateinit var longBreak: PomodoroStateUI
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupMainActivity()
        setStates()
        setObservables()
        setListeners()
    }

    private fun setupMainActivity() {
        viewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(application)
        )[MainViewModel::class.java]
        lifecycle.addObserver(viewModel)
    }

    private fun setObservables() {
        viewModel.run {
            this.countDownMinutes.observe(this@MainActivity) {
                binding.minutesText.text = addZeroLeftToString(it)
            }
            this.countDownSeconds.observe(this@MainActivity) {
                binding.secondsText.text = addZeroLeftToString(it)
            }
            this.setStartIcon.observe(this@MainActivity) {
                if (it) {
                    binding.playButton.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_play,
                            applicationContext.theme
                        )
                    )
                } else {
                    binding.playButton.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_pause,
                            applicationContext.theme
                        )
                    )
                }
            }
            this.pomodoroState.observe(this@MainActivity) { updateUI(it) }
        }
    }

    private fun setListeners() {
        binding.settingsButton.setOnClickListener { openSettings() }
        binding.playButton.setOnClickListener { handlePlayButton() }
        binding.nextButton.setOnClickListener { actionNextState() }
    }

    private fun openSettings() {
        val fragmentManager = supportFragmentManager
        val newFragment = SettingsDialogFragment()
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction
            .add(android.R.id.content, newFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun handlePlayButton() {
        viewModel.handlePlayResume()
    }

    private fun actionNextState() {
        viewModel.goNextState()
    }

    private fun updateUI(state: PomodoroState) {
        when (state) {
            PomodoroState.SHORT_BREAK -> {
                binding.itemPomodoroState.setStateType(shortBreak)
                updateColors(shortBreak)
            }
            PomodoroState.LONG_BREAK -> {
                binding.itemPomodoroState.setStateType(longBreak)
                updateColors(longBreak)
            }
            else -> {
                binding.itemPomodoroState.setStateType(focusState)
                updateColors(focusState)
            }
        }
    }

    private fun updateColors(stateUi: PomodoroStateUI) = binding.run {
        this.secondsText.setTextColor(stateUi.primaryColor)
        this.minutesText.setTextColor(stateUi.primaryColor)
        this.settingsButton.run {
            val gradient = this.background.mutate() as GradientDrawable
            gradient.setTint(stateUi.secondaryColor)
            this.setColorFilter(stateUi.primaryColor)
        }
        this.playButton.run {
            val gradient = this.background.mutate() as GradientDrawable
            gradient.setTint(stateUi.tertiaryColor)
            this.setColorFilter(stateUi.primaryColor)
        }
        this.nextButton.run {
            val gradient = this.background.mutate() as GradientDrawable
            gradient.setTint(stateUi.secondaryColor)
            this.setColorFilter(stateUi.primaryColor)
        }
    }

    private fun addZeroLeftToString(number: Int): String {
        return if (number < Constants.TEN_SECONDS) {
            "0$number"
        } else {
            number.toString()
        }
    }

    private fun setStates() {
        focusState = PomodoroStateUI(
            name = getString(R.string.focus_name),
            primaryColor = resources.getColor(R.color.colorFocus, applicationContext.theme),
            secondaryColor = resources.getColor(
                R.color.colorFocusSecondary,
                applicationContext.theme
            ),
            tertiaryColor = resources.getColor(
                R.color.colorFocusTertiary,
                applicationContext.theme
            ),
            iconSrc = resources.getDrawable(R.drawable.ic_focus, applicationContext.theme)
        )
        shortBreak = PomodoroStateUI(
            name = getString(R.string.short_break_name),
            primaryColor = resources.getColor(R.color.colorShortBreak, applicationContext.theme),
            secondaryColor = resources.getColor(
                R.color.colorShortBreakSecondary,
                applicationContext.theme
            ),
            tertiaryColor = resources.getColor(
                R.color.colorShortBreakTertiary,
                applicationContext.theme
            ),
            iconSrc = resources.getDrawable(R.drawable.ic_coffee, applicationContext.theme)
        )
        longBreak = PomodoroStateUI(
            name = getString(R.string.long_break_name),
            primaryColor = resources.getColor(R.color.colorLongBreak, applicationContext.theme),
            secondaryColor = resources.getColor(
                R.color.colorLongBreakSecondary,
                applicationContext.theme
            ),
            tertiaryColor = resources.getColor(
                R.color.colorLongBreakTertiary,
                applicationContext.theme
            ),
            iconSrc = resources.getDrawable(R.drawable.ic_coffee, applicationContext.theme)
        )
    }
}